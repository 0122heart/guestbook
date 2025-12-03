import React, { useState, useEffect } from 'react';
import './FriendsPage.css';

function FriendsPage() {
  const baseURL = 'http://localhost:8080';
  
  const [friends, setFriends] = useState([]);
  const [friendRequests, setFriendRequests] = useState([]);
  const [searchNickname, setSearchNickname] = useState('');
  const [searchResult, setSearchResult] = useState(null);
  const [searchError, setSearchError] = useState('');
  const [activeTab, setActiveTab] = useState('friends');
  const [currentUser, setCurrentUser] = useState(null);

  useEffect(() => {
    fetchCurrentUser();
  }, []);

  useEffect(() => {
    if (currentUser) {
      fetchFriends();
      fetchFriendRequests();
    }
  }, [currentUser]);

  const fetchCurrentUser = async () => {
    try {
      const response = await fetch(`${baseURL}/api/current`, {
        credentials: 'include'
      });
      
      console.log('fetchCurrentUser response status:', response.status);
      
      if (response.ok) {
        const data = await response.json();
        console.log('fetchCurrentUser data:', data);
        setCurrentUser(data);
      } else {
        console.error('fetchCurrentUser failed with status:', response.status);
        alert('로그인이 필요합니다.');
        window.location.href = '/login';
      }
    } catch (error) {
      console.error('사용자 정보 불러오기 실패:', error);
      alert('세션 정보를 불러올 수 없습니다.');
      window.location.href = '/login';
    }
  };

  const fetchFriends = async () => {
    try {
      const response = await fetch(`${baseURL}/api/friend`, {
        credentials: 'include'
      });
      if (response.ok) {
        const data = await response.json();
        // FriendListDto를 직접 사용 (변환 불필요)
        setFriends(data);
      }
    } catch (error) {
      console.error('친구 목록 불러오기 실패:', error);
    }
  };

  const fetchFriendRequests = async () => {
    try {
      const response = await fetch(`${baseURL}/api/friend/request`, {
        credentials: 'include'
      });
      if (response.ok) {
        const data = await response.json();
        console.log('친구 요청 데이터:', data);
        setFriendRequests(data);
      }
    } catch (error) {
      console.error('친구 요청 불러오기 실패:', error);
    }
  };

  const handleSearchUser = async (e) => {
    e.preventDefault();
    if (!searchNickname.trim()) {
      alert('닉네임을 입력해주세요.');
      return;
    }

    try {
      const response = await fetch(`${baseURL}/api/search/${encodeURIComponent(searchNickname)}`, {
        credentials: 'include'
      });
      
      if (response.ok) {
        const user = await response.json();
        setSearchResult(user);
        setSearchError('');
      } else {
        setSearchResult(null);
        setSearchError('존재하지 않는 사용자입니다.');
      }
    } catch (error) {
      console.error('사용자 검색 실패:', error);
      setSearchResult(null);
      setSearchError('검색 중 오류가 발생했습니다.');
    }
  };

  const handleViewProfile = (nickname) => {
    window.location.href = `/profile/${nickname}`;
  };

  const handleRequestResponse = async (requestId, accept) => {
    try {
      const response = await fetch(`${baseURL}/api/friend/${requestId}/${accept}`, {
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
        credentials: 'include'
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
      const response = await fetch(`${baseURL}/api/friend/${friendId}`, {
        method: 'DELETE',
        credentials: 'include'
      });

      if (response.ok) {
        fetchFriends();
      }
    } catch (error) {
      console.error('친구 삭제 실패:', error);
    }
  };

  if (!currentUser) {
    return <div className="loading">로딩 중...</div>;
  }

  return (
    <div className="friends-container">
      <header className="friends-header">
        <h1>친구 관리</h1>
        <div className="header-info">
          <span>안녕하세요, {currentUser.nickname}님!</span>
          <button onClick={() => window.location.href = '/home'}>홈으로</button>
        </div>
      </header>

      <div className="friends-main">
        <section className="add-friend-section">
          <h2>친구 검색</h2>
          <form onSubmit={handleSearchUser}>
            <input
              type="text"
              placeholder="친구의 닉네임을 입력하세요"
              value={searchNickname}
              onChange={(e) => setSearchNickname(e.target.value)}
            />
            <button type="submit">검색</button>
          </form>
          
          {searchResult && (
            <div className="search-result-box">
              <div className="friend-avatar">
                {searchResult.nickname.charAt(0).toUpperCase()}
              </div>
              <div className="search-info">
                <h3>{searchResult.nickname}</h3>
              </div>
              <button
                onClick={() => handleViewProfile(searchResult.nickname)}
                className="view-profile-btn"
              >
                프로필 보기
              </button>
            </div>
          )}
          
          {searchError && (
            <div className="search-error">
              {searchError}
            </div>
          )}
        </section>

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

        {activeTab === 'friends' && (
          <section className="list-section">
            {friends.length === 0 ? (
              <p className="empty-message">아직 친구가 없습니다.</p>
            ) : (
              <div className="friends-list">
                {friends.map((friend) => (
                  <div key={friend.userId} className="friend-item">
                    <div className="friend-avatar">
                      {friend.nickname?.charAt(0).toUpperCase() || '?'}
                    </div>
                    <div className="friend-info">
                      <h3>{friend.nickname}</h3>
                      {friend.createdAt && (
                        <p className="friend-date">
                          {new Date(friend.createdAt).toLocaleDateString()} 친구
                        </p>
                      )}
                    </div>
                    <div className="friend-actions">
                      <button
                        onClick={() => window.location.href = `/guestbook/${friend.nickname}`}
                        className="visit-btn"
                      >
                        방명록 보기
                      </button>
                      <button
                        onClick={() => handleDeleteFriend(friend.userId)}
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

        {activeTab === 'requests' && (
          <section className="list-section">
            {friendRequests.length === 0 ? (
              <p className="empty-message">받은 친구 요청이 없습니다.</p>
            ) : (
              <div className="requests-list">
                {friendRequests.map((request) => (
                  <div key={request.requestId} className="request-item">
                    <div className="friend-avatar">
                      {request.sender.charAt(0).toUpperCase()}
                    </div>
                    <div className="request-info">
                      <h3>{request.sender}</h3>
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