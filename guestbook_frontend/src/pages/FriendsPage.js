import React, { useState, useEffect } from 'react';
import './FriendsPage.css';

function FriendsPage() {
  const [friends, setFriends] = useState([]);
  const [friendRequests, setFriendRequests] = useState([]);
  const [searchNickname, setSearchNickname] = useState('');
  const [activeTab, setActiveTab] = useState('friends'); // 'friends' or 'requests'
  const currentUserNickname = 'myNickname'; // 실제로는 세션에서 가져와야 함

  useEffect(() => {
    fetchFriends();
    fetchFriendRequests();
  }, []);

  const fetchFriends = async () => {
    try {
      const response = await fetch(`/api/friend/${currentUserNickname}`);
      if (response.ok) {
        const data = await response.json();
        setFriends(data);
      }
    } catch (error) {
      console.error('친구 목록 불러오기 실패:', error);
    }
  };

  const fetchFriendRequests = async () => {
    // API가 있다고 가정
    try {
      const response = await fetch('/api/friend/request');
      if (response.ok) {
        const data = await response.json();
        setFriendRequests(data);
      }
    } catch (error) {
      console.error('친구 요청 불러오기 실패:', error);
    }
  };

  const handleSendRequest = async (e) => {
    e.preventDefault();
    if (!searchNickname.trim()) return;

    try {
      const response = await fetch('/api/friend/request', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ targetNickname: searchNickname })
      });

      if (response.ok) {
        alert('친구 요청을 보냈습니다!');
        setSearchNickname('');
      } else {
        alert('친구 요청 실패: 닉네임을 확인해주세요.');
      }
    } catch (error) {
      console.error('친구 요청 실패:', error);
      alert('친구 요청 중 오류가 발생했습니다.');
    }
  };

  const handleRequestResponse = async (requestId, accept) => {
    try {
      const response = await fetch(`/api/friend/request/${requestId}`, {
        method: 'PATCH',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ accept })
      });

      if (response.ok) {
        alert(accept ? '친구 요청을 수락했습니다!' : '친구 요청을 거절했습니다.');
        fetchFriendRequests();
        if (accept) fetchFriends();
      }
    } catch (error) {
      console.error('친구 요청 응답 실패:', error);
    }
  };

  const handleDeleteFriend = async (friendId) => {
    if (!window.confirm('정말 친구를 삭제하시겠습니까?')) return;

    try {
      const response = await fetch(`/api/friend/${friendId}`, {
        method: 'DELETE'
      });

      if (response.ok) {
        fetchFriends();
      }
    } catch (error) {
      console.error('친구 삭제 실패:', error);
    }
  };

  return (
    <div className="friends-container">
      <header className="friends-header">
        <h1>친구 관리</h1>
        <button onClick={() => window.location.href = '/home'}>홈으로</button>
      </header>

      <div className="friends-main">
        {/* 친구 추가 섹션 */}
        <section className="add-friend-section">
          <h2>친구 추가</h2>
          <form onSubmit={handleSendRequest}>
            <input
              type="text"
              placeholder="친구의 닉네임을 입력하세요"
              value={searchNickname}
              onChange={(e) => setSearchNickname(e.target.value)}
            />
            <button type="submit">친구 요청</button>
          </form>
        </section>

        {/* 탭 메뉴 */}
        <div className="tabs">
          <button
            className={activeTab === 'friends' ? 'active' : ''}
            onClick={() => setActiveTab('friends')}
          >
            친구 목록 ({friends.length})
          </button>
          <button
            className={activeTab === 'requests' ? 'active' : ''}
            onClick={() => setActiveTab('requests')}
          >
            친구 요청 ({friendRequests.length})
          </button>
        </div>

        {/* 친구 목록 */}
        {activeTab === 'friends' && (
          <section className="list-section">
            {friends.length === 0 ? (
              <p className="empty-message">아직 친구가 없습니다.</p>
            ) : (
              <div className="friends-list">
                {friends.map((friend) => (
                  <div key={friend.friendId} className="friend-item">
                    <div className="friend-avatar">
                      {friend.nickname.charAt(0).toUpperCase()}
                    </div>
                    <div className="friend-info">
                      <h3>{friend.nickname}</h3>
                      <p>친구가 된 날: {new Date(friend.createdAt).toLocaleDateString()}</p>
                    </div>
                    <div className="friend-actions">
                      <button
                        onClick={() => window.location.href = `/guestbook/${friend.nickname}`}
                        className="visit-btn"
                      >
                        방명록 보기
                      </button>
                      <button
                        onClick={() => handleDeleteFriend(friend.friendId)}
                        className="delete-btn"
                      >
                        삭제
                      </button>
                    </div>
                  </div>
                ))}
              </div>
            )}
          </section>
        )}

        {/* 친구 요청 목록 */}
        {activeTab === 'requests' && (
          <section className="list-section">
            {friendRequests.length === 0 ? (
              <p className="empty-message">받은 친구 요청이 없습니다.</p>
            ) : (
              <div className="requests-list">
                {friendRequests.map((request) => (
                  <div key={request.requestId} className="request-item">
                    <div className="friend-avatar">
                      {request.senderNickname.charAt(0).toUpperCase()}
                    </div>
                    <div className="request-info">
                      <h3>{request.senderNickname}</h3>
                      <p>{new Date(request.createdAt).toLocaleDateString()}</p>
                    </div>
                    <div className="request-actions">
                      <button
                        onClick={() => handleRequestResponse(request.requestId, true)}
                        className="accept-btn"
                      >
                        수락
                      </button>
                      <button
                        onClick={() => handleRequestResponse(request.requestId, false)}
                        className="reject-btn"
                      >
                        거절
                      </button>
                    </div>
                  </div>
                ))}
              </div>
            )}
          </section>
        )}
      </div>
    </div>
  );
}

export default FriendsPage;