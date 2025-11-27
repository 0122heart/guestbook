import React, { useState, useEffect } from 'react';
import './HomePage.css';

const API_BASE_URL = 'http://localhost:8080';

function HomePage() {
  const [friends, setFriends] = useState([]);
  const [currentUser, setCurrentUser] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchCurrentUser();
  }, []);

  useEffect(() => {
    if (currentUser) {
      fetchFriends();
    }
  }, [currentUser]);

  // 현재 로그인한 사용자 정보 가져오기
  const fetchCurrentUser = async () => {
    try {
      const response = await fetch(`${API_BASE_URL}/api/current`, {
        credentials: 'include' // 세션 쿠키 포함
      });
      
      if (response.ok) {
        const data = await response.json();
        setCurrentUser(data);
      } else {
        // ⭐ [수정됨] 세션이 만료되었거나 없을 경우 로컬 스토리지 정리
        console.warn('세션이 유효하지 않습니다.');
        localStorage.removeItem('isLoggedIn');
        window.location.href = '/login';
      }
    } catch (error) {
      console.error('사용자 정보 불러오기 실패:', error);
      localStorage.removeItem('isLoggedIn');
      window.location.href = '/login';
    }
  };

  const fetchFriends = async () => {
    try {
      const response = await fetch(`${API_BASE_URL}/api/friend/${currentUser.nickname}`, {
        credentials: 'include' // 세션 쿠키 포함
      });
      
      if (response.ok) {
        const data = await response.json();
        setFriends(data);
      }
    } catch (error) {
      console.error('친구 목록 불러오기 실패:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleLogout = async () => {
    try {
      const response = await fetch(`${API_BASE_URL}/api/user/signout`, {
        method: 'POST',
        credentials: 'include' // 세션 쿠키 포함
      });
      
      // ⭐ [수정됨] 응답 성공 여부와 관계없이 클라이언트 로그아웃 처리
      localStorage.removeItem('isLoggedIn');
      window.location.href = '/login';
      
    } catch (error) {
      console.error('로그아웃 요청 실패:', error);
      // 에러가 나더라도 사용자는 로그아웃 처리
      localStorage.removeItem('isLoggedIn');
      window.location.href = '/login';
    }
  };

  if (!currentUser) {
    return (
      <div className="home-container">
        <p>로딩 중...</p>
      </div>
    );
  }

  return (
    <div className="home-container">
      <header className="home-header">
        <h1>방명록 홈</h1>
        <div className="header-buttons">
          <span className="user-info">{currentUser.nickname}님</span>
          <button onClick={() => window.location.href = '/profile'}>
            내 프로필
          </button>
          <button onClick={handleLogout} className="logout-btn">
            로그아웃
          </button>
        </div>
      </header>

      <main className="home-main">
        <section className="friends-section">
          <h2>친구 목록</h2>
          {loading ? (
            <p>로딩 중...</p>
          ) : friends.length === 0 ? (
            <p className="no-friends">아직 친구가 없습니다.</p>
          ) : (
            <div className="friends-grid">
              {friends.map((friend) => (
                <div key={friend.friendId} className="friend-card">
                  <div className="friend-avatar">
                    {friend.nickname.charAt(0).toUpperCase()}
                  </div>
                  <h3>{friend.nickname}</h3>
                  <button 
                    onClick={() => window.location.href = `/guestbook/${friend.nickname}`}
                    className="visit-btn"
                  >
                    방명록 보기
                  </button>
                </div>
              ))}
            </div>
          )}
        </section>

        <section className="actions-section">
          <button 
            onClick={() => window.location.href = '/friends'}
            className="action-btn"
          >
            친구 관리
          </button>
          <button 
            onClick={() => window.location.href = `/guestbook/${currentUser.nickname}`}
            className="action-btn primary"
          >
            내 방명록 보기
          </button>
        </section>
      </main>
    </div>
  );
}

export default HomePage;