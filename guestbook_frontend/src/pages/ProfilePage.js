import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import './ProfilePage.css';

function ProfilePage() {
  const { nickname } = useParams(); // URL에서 닉네임 가져오기
  const baseURL = 'http://localhost:8080';
  
  const [profile, setProfile] = useState({
    id: null,
    nickname: '',
    statusMsg: '',
    relationId: null,
    guestbooks: []
  });
  const [currentUser, setCurrentUser] = useState(null);
  const [isOwnProfile, setIsOwnProfile] = useState(false);
  const [isFriend, setIsFriend] = useState(false);
  const [friendRequestSent, setFriendRequestSent] = useState(false);

  useEffect(() => {
    fetchCurrentUser();
  }, []);

  useEffect(() => {
    if (currentUser) {
      if (nickname) {
        // 다른 사용자의 프로필
        fetchUserProfile(nickname);
      } else {
        // 본인 프로필
        fetchMyProfile();
        setIsOwnProfile(true);
      }
    }
  }, [currentUser, nickname]);

  const fetchCurrentUser = async () => {
    try {
      const response = await fetch(`${baseURL}/api/current`, {
        credentials: 'include'
      });
      
      if (response.ok) {
        const data = await response.json();
        setCurrentUser(data);
      } else {
        alert('로그인이 필요합니다.');
        window.location.href = '/login';
      }
    } catch (error) {
      console.error('사용자 정보 불러오기 실패:', error);
      window.location.href = '/login';
    }
  };

  const fetchMyProfile = async () => {
    try {
      const response = await fetch(`${baseURL}/api/profile`, {
        credentials: 'include'
      });
      if (response.ok) {
        const data = await response.json();
        setProfile(data);
        setIsOwnProfile(true);
        setIsFriend(false);
      }
    } catch (error) {
      console.error('프로필 불러오기 실패:', error);
    }
  };

  const fetchUserProfile = async (userNickname) => {
    try {
      // /api/profile/{nickname} 사용 (ProfileDto 반환)
      const response = await fetch(`${baseURL}/api/profile/${encodeURIComponent(userNickname)}`, {
        credentials: 'include'
      });
      
      if (response.ok) {
        const data = await response.json();
        setProfile(data);
        
        // 본인 프로필인지 확인
        setIsOwnProfile(currentUser.nickname === data.nickname);
        
        // 친구 관계 확인 (relationId가 있으면 친구)
        setIsFriend(data.relationId !== null);
      } else {
        alert('사용자를 찾을 수 없습니다.');
        window.location.href = '/friends';
      }
    } catch (error) {
      console.error('프로필 불러오기 실패:', error);
    }
  };

  const handleSendFriendRequest = async () => {
    if (!profile.id) {
      alert('사용자 정보를 불러올 수 없습니다.');
      return;
    }

    try {
      const response = await fetch(`${baseURL}/api/friend/request/${profile.id}`, {
        method: 'POST',
        credentials: 'include'
      });

      if (response.ok) {
        alert('친구 요청을 보냈습니다!');
        setFriendRequestSent(true);
      } else {
        const error = await response.text();
        alert(error || '친구 요청 전송에 실패했습니다.');
      }
    } catch (error) {
      console.error('친구 요청 전송 실패:', error);
      alert('친구 요청 전송 중 오류가 발생했습니다.');
    }
  };

  if (!currentUser || !profile.nickname) {
    return <div className="loading">로딩 중...</div>;
  }

  // 친구가 아닌 다른 사용자의 프로필 (제한된 뷰)
  if (!isOwnProfile && !isFriend) {
    return (
      <div className="profile-container">
        <header className="profile-header">
          <h1>{profile.nickname}님의 프로필</h1>
          <button onClick={() => window.location.href = '/friends'}>뒤로가기</button>
        </header>

        <div className="profile-main">
          <section className="profile-card limited">
            <div className="profile-avatar large">
              {profile.nickname.charAt(0).toUpperCase()}
            </div>

            <div className="profile-info-limited">
              <h2>{profile.nickname}</h2>
              <p className="status-message">
                {profile.statusMsg || '상태 메시지가 없습니다.'}
              </p>
              
              {!friendRequestSent ? (
                <button onClick={handleSendFriendRequest} className="add-friend-btn">
                  친구 추가
                </button>
              ) : (
                <p className="request-sent">친구 요청을 보냈습니다</p>
              )}
            </div>
          </section>
        </div>
      </div>
    );
  }

  // 본인 프로필 또는 친구 프로필 (전체 뷰)
  return (
    <div className="profile-container">
      <header className="profile-header">
        <h1>{isOwnProfile ? '내 프로필' : `${profile.nickname}님의 프로필`}</h1>
        <button onClick={() => window.location.href = isOwnProfile ? '/home' : '/friends'}>
          {isOwnProfile ? '홈으로' : '뒤로가기'}
        </button>
      </header>

      <div className="profile-main">
        <section className="profile-card">
          <div className="profile-avatar">
            {profile.nickname.charAt(0).toUpperCase()}
          </div>

          <div className="profile-info">
            <div className="info-row">
              <span className="label">닉네임</span>
              <span className="value">{profile.nickname}</span>
            </div>
            <div className="info-row">
              <span className="label">상태 메시지</span>
              <span className="value">{profile.statusMsg || '상태 메시지가 없습니다.'}</span>
            </div>
          </div>
        </section>

        {/* 방명록 섹션 */}
        {profile.guestbooks && profile.guestbooks.length > 0 && (
          <section className="guestbook-section">
            <h2>방명록</h2>
            <div className="guestbook-list">
              {profile.guestbooks.map((guestbook) => (
                <div key={guestbook.id} className="guestbook-item">
                  <div className="guestbook-header">
                    <h3>{guestbook.title}</h3>
                    <span className="guestbook-meta">
                      {guestbook.guestNickname} → {guestbook.ownerNickname}
                    </span>
                  </div>
                  <p className="guestbook-content">{guestbook.content}</p>
                  <span className="guestbook-date">
                    {new Date(guestbook.createdAt).toLocaleDateString()}
                  </span>
                  
                  {/* 댓글 */}
                  {guestbook.comments && guestbook.comments.length > 0 && (
                    <div className="comments-list">
                      {guestbook.comments.map((comment) => (
                        <div key={comment.commentId} className="comment-item">
                          <strong>{comment.nickname}</strong>: {comment.content}
                          <span className="comment-date">
                            {new Date(comment.createdAt).toLocaleDateString()}
                          </span>
                        </div>
                      ))}
                    </div>
                  )}
                </div>
              ))}
            </div>
          </section>
        )}

        <section className="actions-section">
          <button
            onClick={() => window.location.href = `/guestbook/${profile.nickname}`}
            className="guestbook-btn"
          >
            {isOwnProfile ? '내 방명록 보기' : '방명록 보기'}
          </button>
          {isOwnProfile && (
            <button
              onClick={() => window.location.href = '/friends'}
              className="friends-btn"
            >
              친구 관리
            </button>
          )}
        </section>
      </div>
    </div>
  );
}

export default ProfilePage;