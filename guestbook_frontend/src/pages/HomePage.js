import React, { useState, useEffect, useCallback, useRef } from 'react';
import './HomePage.css';

const API_BASE_URL = 'http://localhost:8080';

function HomePage() {
  const [guestbooks, setGuestbooks] = useState([]);
  const [currentUser, setCurrentUser] = useState(null);
  const [loading, setLoading] = useState(false);
  const [page, setPage] = useState(0);
  const [hasMore, setHasMore] = useState(true);
  
  // fetchGuestbooks를 useCallback으로 메모이제이션
  // eslint-disable-next-line react-hooks/exhaustive-deps
  const fetchGuestbooks = useCallback(async () => {
    if (loading) return;
    
    setLoading(true);
    try {
      const response = await fetch(
        `${API_BASE_URL}/api/guestbook/home/feed?page=${page}&size=10`,
        { credentials: 'include' }
      );
      
      if (response.status === 401) {
        alert('로그인이 필요합니다.');
        localStorage.removeItem('isLoggedIn');
        window.location.href = '/login';
        return;
      }
      
      if (response.ok) {
        const data = await response.json();
        
        setGuestbooks(prev => {
          // 중복 제거
          const newGuestbooks = data.content.filter(
            newItem => !prev.some(prevItem => prevItem.guestbookId === newItem.guestbookId)
          );
          return [...prev, ...newGuestbooks];
        });
        
        setHasMore(!data.last);
      }
    } catch (error) {
      console.error('방명록 불러오기 실패:', error);
    } finally {
      setLoading(false);
    }
  }, [page]); // loading을 의존성에 넣으면 무한 루프 발생

  const observer = useRef();
  const lastGuestbookRef = useCallback(node => {
    if (loading) return;
    if (observer.current) observer.current.disconnect();
    
    observer.current = new IntersectionObserver(entries => {
      if (entries[0].isIntersecting && hasMore) {
        setPage(prevPage => prevPage + 1);
      }
    });
    
    if (node) observer.current.observe(node);
  }, [loading, hasMore]);

  useEffect(() => {
    fetchCurrentUser();
  }, []);

  useEffect(() => {
    if (currentUser) {
      fetchGuestbooks();
    }
  }, [currentUser, page, fetchGuestbooks]);

  const fetchCurrentUser = async () => {
    try {
      const response = await fetch(`${API_BASE_URL}/api/current`, {
        credentials: 'include'
      });
      
      if (response.ok) {
        const data = await response.json();
        setCurrentUser(data);
      } else {
        localStorage.removeItem('isLoggedIn');
        window.location.href = '/login';
      }
    } catch (error) {
      console.error('사용자 정보 불러오기 실패:', error);
      localStorage.removeItem('isLoggedIn');
      window.location.href = '/login';
    }
  };

  const handleLogout = async () => {
    try {
      await fetch(`${API_BASE_URL}/api/user/signout`, {
        method: 'POST',
        credentials: 'include'
      });
      
      localStorage.removeItem('isLoggedIn');
      window.location.href = '/login';
    } catch (error) {
      console.error('로그아웃 실패:', error);
      localStorage.removeItem('isLoggedIn');
      window.location.href = '/login';
    }
  };

  const formatDate = (dateString) => {
    const date = new Date(dateString);
    const now = new Date();
    const diff = now - date;
    
    const minutes = Math.floor(diff / 60000);
    const hours = Math.floor(diff / 3600000);
    const days = Math.floor(diff / 86400000);
    
    if (minutes < 1) return '방금 전';
    if (minutes < 60) return `${minutes}분 전`;
    if (hours < 24) return `${hours}시간 전`;
    if (days < 7) return `${days}일 전`;
    
    return date.toLocaleDateString('ko-KR', {
      year: 'numeric',
      month: 'long',
      day: 'numeric'
    });
  };

  if (!currentUser) {
    return (
      <div className="home-container">
        <div className="loading-screen">
          <div className="spinner"></div>
          <p>로딩 중...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="home-container">
      <header className="home-header">
        <h1 className="logo">방명록 홈</h1>
        <div className="header-buttons">
          <span className="user-info">{currentUser.nickname}님</span>
          <button 
            onClick={() => window.location.href = '/profile'}
            className="profile-btn"
          >
            내 프로필
          </button>
          <button onClick={handleLogout} className="logout-btn">
            로그아웃
          </button>
        </div>
      </header>

      <main className="feed-main">
        <div className="feed-container">
          {guestbooks.length === 0 && !loading ? (
            <div className="empty-feed">
              <h2>방명록 피드가 비어있습니다</h2>
              <p>친구를 추가하고 친구들의 방명록을 확인해보세요!</p>
              <button 
                onClick={() => window.location.href = '/friends'}
                className="add-friend-btn"
              >
                친구 추가하기
              </button>
            </div>
          ) : (
            <>
              {guestbooks.map((guestbook, index) => {
                if (guestbooks.length === index + 1) {
                  return (
                    <div 
                      ref={lastGuestbookRef} 
                      key={guestbook.guestbookId} 
                      className="guestbook-card"
                    >
                      <GuestbookCard guestbook={guestbook} formatDate={formatDate} />
                    </div>
                  );
                } else {
                  return (
                    <div key={guestbook.guestbookId} className="guestbook-card">
                      <GuestbookCard guestbook={guestbook} formatDate={formatDate} />
                    </div>
                  );
                }
              })}
              
              {loading && (
                <div className="loading-more">
                  <div className="spinner"></div>
                  <p>로딩 중...</p>
                </div>
              )}
              
              {!hasMore && guestbooks.length > 0 && (
                <div className="end-of-feed">
                  <p>모든 방명록을 확인했습니다</p>
                </div>
              )}
            </>
          )}
        </div>
      </main>
    </div>
  );
}

// 방명록 카드 컴포넌트
function GuestbookCard({ guestbook, formatDate }) {
  return (
    <>
      <div className="card-header">
        <div className="owner-info">
          <div className="owner-avatar">
            {guestbook.ownerNickname?.charAt(0).toUpperCase() || '?'}
          </div>
          <div>
            <h3 className="owner-name">{guestbook.ownerNickname}</h3>
            <span className="post-time">{formatDate(guestbook.createdAt)}</span>
          </div>
        </div>
        <button 
          onClick={() => window.location.href = `/guestbook/${guestbook.ownerNickname}`}
          className="visit-guestbook-btn"
        >
          방명록 보기
        </button>
      </div>
      
      <div className="card-content">
        <h4 className="guestbook-title">{guestbook.title}</h4>
        <p className="guestbook-content">{guestbook.content}</p>
      </div>
      
      <div className="card-footer">
        <span className="guest-author">
          {guestbook.guestNickname}
        </span>
      </div>
    </>
  );
}

export default HomePage;