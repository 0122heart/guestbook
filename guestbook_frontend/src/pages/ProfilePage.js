import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import './ProfilePage.css';

const API_BASE_URL = 'http://localhost:8080';

function ProfilePage() {
  const { nickname } = useParams(); // URL에서 닉네임 가져오기 (없으면 내 프로필)
  const [currentUser, setCurrentUser] = useState(null);
  const [targetUser, setTargetUser] = useState(null);
  const [friends, setFriends] = useState([]);
  const [isFriend, setIsFriend] = useState(false);
  const [isOwnProfile, setIsOwnProfile] = useState(false);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchCurrentUser();
  }, []);

  useEffect(() => {
    if (currentUser) {
      if (nickname) {
        // URL에 닉네임이 있으면 해당 유저 조회
        fetchTargetUser();
      } else {
        // URL에 닉네임이 없으면 내 프로필
        setTargetUser(currentUser);
        setIsOwnProfile(true);
        fetchFriends();
        setLoading(false);
      }
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [currentUser, nickname]); // fetchTargetUser를 의존성에 넣으면 무한 루프

  const fetchCurrentUser = async () => {
    try {
      const response = await fetch(`${API_BASE_URL}/api/current`, {
        credentials: 'include'
      });
      
      if (response.ok) {
        const data = await response.json();
        setCurrentUser(data);
      } else {
        window.location.href = '/login';
      }
    } catch (error) {
      console.error('사용자 정보 불러오기 실패:', error);
      window.location.href = '/login';
    }
  };

  const fetchTargetUser = async () => {
    try {
      const response = await fetch(
        `${API_BASE_URL}/api/search/${encodeURIComponent(nickname)}`,
        { credentials: 'include' }
      );
      
      if (response.ok) {
        const data = await response.json();
        setTargetUser(data);
        
        // 본인인지 확인
        if (data.nickname === currentUser.nickname) {
          setIsOwnProfile(true);
          fetchFriends();
        } else {
          setIsOwnProfile(false);
          checkFriendship();
        }
      } else {
        alert('사용자를 찾을 수 없습니다.');
        window.location.href = '/home';
      }
    } catch (error) {
      console.error('사용자 정보 불러오기 실패:', error);
    } finally {
      setLoading(false);
    }
  };

  const checkFriendship = async () => {
    try {
      const response = await fetch(`${API_BASE_URL}/api/friend`, {
        credentials: 'include'
      });
      
      if (response.ok) {
        const friendsList = await response.json();
        const isFriendCheck = friendsList.some(
          friend => friend.nickname === nickname
        );
        setIsFriend(isFriendCheck);
      }
    } catch (error) {
      console.error('친구 관계 확인 실패:', error);
    }
  };

  const fetchFriends = async () => {
    try {
      const response = await fetch(`${API_BASE_URL}/api/friend`, {
        credentials: 'include'
      });
      
      if (response.ok) {
        const data = await response.json();
        setFriends(data);
      }
    } catch (error) {
      console.error('친구 목록 불러오기 실패:', error);
    }
  };

  const handleAddFriend = async () => {
    if (!targetUser) return;

    try {
      const response = await fetch(
        `${API_BASE_URL}/api/friend/request/${targetUser.id}`,
        {
          method: 'POST',
          credentials: 'include'
        }
      );

      if (response.ok) {
        alert('친구 요청을 보냈습니다!');
      } else {
        alert('친구 요청에 실패했습니다.');
      }
    } catch (error) {
      console.error('친구 요청 실패:', error);
      alert('친구 요청 중 오류가 발생했습니다.');
    }
  };

  if (loading || !targetUser || !currentUser) {
    return (
      <div className="profile-container">
        <div className="loading">로딩 중...</div>
      </div>
    );
  }

  return (
    <div className="profile-container">
      <header className="profile-header">
        <button 
          onClick={() => window.location.href = isOwnProfile ? '/home' : '/friends'}
          className="back-btn"
        >
          ← {isOwnProfile ? '홈으로' : '돌아가기'}
        </button>
        <h1>{isOwnProfile ? '내 프로필' : `${targetUser.nickname}님의 프로필`}</h1>
      </header>

      <main className="profile-main">
        {/* 프로필 정보 - 항상 표시 */}
        <section className="profile-info-section">
          <div className="profile-avatar-large">
            {targetUser.nickname?.charAt(0).toUpperCase() || '?'}
          </div>
          <h2>{targetUser.nickname}</h2>
          {targetUser.statusMsg && (
            <p className="status-message">{targetUser.statusMsg}</p>
          )}
        </section>

        {/* 1. 본인 프로필 - 모든 정보 표시 */}
        {isOwnProfile && (
          <>
            <section className="profile-actions">
              <button 
                onClick={() => window.location.href = `/guestbook/${currentUser.nickname}`}
                className="action-btn primary"
              >
                내 방명록 보기
              </button>
              <button 
                onClick={() => window.location.href = '/friends'}
                className="action-btn"
              >
                친구 관리
              </button>
            </section>

            <section className="friends-section">
              <h3>친구 목록 ({friends.length})</h3>
              {friends.length === 0 ? (
                <p className="no-friends">아직 친구가 없습니다.</p>
              ) : (
                <div className="friends-grid">
                  {friends.map((friend) => (
                    <div key={friend.userId} className="friend-card">
                      <div className="friend-avatar">
                        {friend.nickname?.charAt(0).toUpperCase() || '?'}
                      </div>
                      <h4>{friend.nickname}</h4>
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
          </>
        )}

        {/* 2. 친구 프로필 - 친구 정보 + 방명록 접근 */}
        {!isOwnProfile && isFriend && (
          <>
            <section className="profile-actions">
              <button 
                onClick={() => window.location.href = `/guestbook/${targetUser.nickname}`}
                className="action-btn primary"
              >
                방명록 보기
              </button>
            </section>
            <section className="friend-status">
              <p className="friend-badge">이미 친구입니다</p>
            </section>
          </>
        )}

        {/* 3. 친구가 아닌 사용자 - 최소 정보 + 친구 추가 */}
        {!isOwnProfile && !isFriend && (
          <>
            <section className="profile-actions">
              <button 
                onClick={handleAddFriend}
                className="action-btn primary"
              >
                친구 추가
              </button>
            </section>
            <section className="not-friend-notice">
              <p>친구가 되면 방명록을 볼 수 있습니다.</p>
            </section>
          </>
        )}
      </main>
    </div>
  );
}

export default ProfilePage;