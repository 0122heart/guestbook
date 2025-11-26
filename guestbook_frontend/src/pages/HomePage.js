import React, { useState, useEffect } from 'react';
import './HomePage.css';

function HomePage() {
  const [friends, setFriends] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchFriends();
  }, []);

  const fetchFriends = async () => {
    try {
      // 현재 로그인한 사용자의 닉네임 (실제로는 세션/토큰에서 가져와야 함)
      const currentUserNickname = 'myNickname';
      
      const response = await fetch(`/api/friend/${currentUserNickname}`);
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

  const handleLogout = () => {
    // 로그아웃 처리
    // localStorage.removeItem('token');
    window.location.href = '/login';
  };

  return (
    <div className="home-container">
      <header className="home-header">
        <h1>방명록 홈</h1>
        <div className="header-buttons">
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
            onClick={() => window.location.href = `/guestbook/myNickname`}
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