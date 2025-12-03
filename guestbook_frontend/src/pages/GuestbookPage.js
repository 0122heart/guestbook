import React, { useState, useEffect } from 'react';
import './GuestbookPage.css';

const API_BASE_URL = 'http://localhost:8080';

function GuestbookPage() {
  const [guestbookData, setGuestbookData] = useState([]);
  const [newPost, setNewPost] = useState({ title: '', content: '' });
  const [newComment, setNewComment] = useState({});
  const [editingPost, setEditingPost] = useState(null);
  const [editingComment, setEditingComment] = useState(null);
  const [loading, setLoading] = useState(true);
  const [showWriteForm, setShowWriteForm] = useState(false);
  const [ownerId, setOwnerId] = useState(null);
  const [isAuthenticated, setIsAuthenticated] = useState(false);

  // URL에서 닉네임 가져오기 (디코딩 추가)
  const nickname = decodeURIComponent(window.location.pathname.split('/').pop());

  // 로그인 체크
  useEffect(() => {
    checkLoginStatus();
  }, []);

  useEffect(() => {
    if (isAuthenticated) {
      fetchGuestbook();
    }
  }, [nickname, isAuthenticated]);

  const checkLoginStatus = async () => {
    try {
      console.log('🔐 로그인 상태 확인 중...');
      const response = await fetch(`${API_BASE_URL}/api/current`, {
        credentials: 'include',
        headers: {
          'Accept': 'application/json',
        }
      });
      
      console.log('📡 응답 상태:', response.status);
      
      if (response.ok) {
        const userData = await response.json();
        console.log('✅ 로그인 확인됨:', userData);
        setIsAuthenticated(true);
      } else {
        console.log('❌ 로그인되지 않음');
        alert('로그인이 필요합니다.');
        window.location.href = '/login';
      }
    } catch (error) {
      console.error('❌ 로그인 체크 실패:', error);
      alert('로그인 확인 중 오류가 발생했습니다.');
      window.location.href = '/login';
    }
  };

  const fetchGuestbook = async () => {
    try {
      const response = await fetch(`${API_BASE_URL}/api/guestbook/${nickname}`, {
        credentials: 'include'
      });
      if (response.ok) {
        const data = await response.json();
        console.log('📚 방명록 데이터:', data);
        setGuestbookData(data);
        
        // owner ID 조회
        const userResponse = await fetch(`${API_BASE_URL}/api/search/${encodeURIComponent(nickname)}`, {
          credentials: 'include'
        });
        if (userResponse.ok) {
          const userData = await userResponse.json();
          console.log('🔍 검색된 사용자 데이터:', userData);
          console.log('📌 ownerId:', userData.id);
          setOwnerId(userData.id);
        }
      }
    } catch (error) {
      console.error('방명록 불러오기 실패:', error);
    } finally {
      setLoading(false);
    }
  };

  // 글 작성
  const handlePostSubmit = async (e) => {
    e.preventDefault();
    
    console.log('📝 방명록 작성 시도 - ownerId:', ownerId);
    
    if (!ownerId) {
      alert('방명록 주인 정보를 불러올 수 없습니다.');
      return;
    }
    
    const requestData = { 
      ownerId: ownerId,
      title: newPost.title, 
      content: newPost.content 
    };
    
    console.log('📤 전송할 데이터:', requestData);
    
    try {
      const response = await fetch(`${API_BASE_URL}/api/guestbook`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        credentials: 'include',
        body: JSON.stringify(requestData)
      });

      if (response.ok) {
        alert('방명록이 작성되었습니다!');
        setNewPost({ title: '', content: '' });
        setShowWriteForm(false);
        fetchGuestbook();
      } else {
        const errorText = await response.text();
        console.error('❌ 작성 실패:', errorText);
        alert('방명록 작성에 실패했습니다: ' + errorText);
      }
    } catch (error) {
      console.error('❌ 글 작성 실패:', error);
      alert('네트워크 오류가 발생했습니다.');
    }
  };

  // 글 수정
  const handlePostUpdate = async (postId) => {
    console.log('🔧 수정할 postId:', postId);
    
    if (!postId) {
      alert('글 ID를 찾을 수 없습니다.');
      return;
    }
    
    try {
      const response = await fetch(`${API_BASE_URL}/api/guestbook/${postId}`, {
        method: 'PATCH',
        headers: { 'Content-Type': 'application/json' },
        credentials: 'include',
        body: JSON.stringify({
          title: editingPost.title,
          content: editingPost.content
        })
      });

      if (response.ok) {
        setEditingPost(null);
        fetchGuestbook();
      } else {
        alert('글 수정에 실패했습니다.');
      }
    } catch (error) {
      console.error('글 수정 실패:', error);
      alert('네트워크 오류가 발생했습니다.');
    }
  };

  // 글 삭제
  const handlePostDelete = async (postId) => {
    console.log('🗑️ 삭제할 postId:', postId);
    
    if (!postId) {
      alert('글 ID를 찾을 수 없습니다.');
      return;
    }
    
    if (!window.confirm('정말 삭제하시겠습니까?')) return;

    try {
      const response = await fetch(`${API_BASE_URL}/api/guestbook/${postId}`, {
        method: 'DELETE',
        credentials: 'include'
      });

      if (response.ok) {
        fetchGuestbook();
      } else {
        alert('글 삭제에 실패했습니다.');
      }
    } catch (error) {
      console.error('글 삭제 실패:', error);
      alert('네트워크 오류가 발생했습니다.');
    }
  };

  // 댓글 작성
  const handleCommentSubmit = async (postId) => {
    console.log('💬 댓글 작성 postId:', postId);
    
    if (!postId) {
      alert('글 ID를 찾을 수 없습니다.');
      return;
    }
    
    try {
      const response = await fetch(`${API_BASE_URL}/api/guestbook/${postId}/comment`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        credentials: 'include',
        body: JSON.stringify({ content: newComment[postId] })
      });

      if (response.ok) {
        setNewComment({ ...newComment, [postId]: '' });
        fetchGuestbook();
      } else {
        alert('댓글 작성에 실패했습니다.');
      }
    } catch (error) {
      console.error('댓글 작성 실패:', error);
      alert('네트워크 오류가 발생했습니다.');
    }
  };

  // 댓글 수정
  const handleCommentUpdate = async (commentId) => {
    try {
      const response = await fetch(`${API_BASE_URL}/api/guestbook/comment/${commentId}`, {
        method: 'PATCH',
        headers: { 'Content-Type': 'application/json' },
        credentials: 'include',
        body: JSON.stringify({ content: editingComment.content })
      });

      if (response.ok) {
        setEditingComment(null);
        fetchGuestbook();
      }
    } catch (error) {
      console.error('댓글 수정 실패:', error);
    }
  };

  // 댓글 삭제
  const handleCommentDelete = async (commentId) => {
    if (!window.confirm('댓글을 삭제하시겠습니까?')) return;

    try {
      const response = await fetch(`${API_BASE_URL}/api/guestbook/comment/${commentId}`, {
        method: 'DELETE',
        credentials: 'include'
      });

      if (response.ok) {
        fetchGuestbook();
      }
    } catch (error) {
      console.error('댓글 삭제 실패:', error);
    }
  };

  if (loading) return <div className="loading">로딩 중...</div>;

  return (
    <div className="guestbook-container">
      <header className="guestbook-header">
        <h1>{nickname}님의 방명록</h1>
        <button onClick={() => window.location.href = '/home'}>홈으로</button>
      </header>

      <div className="guestbook-main">
        {/* 방명록 쓰기 버튼 */}
        {!showWriteForm && (
          <div className="write-button-container">
            <button 
              onClick={() => setShowWriteForm(true)}
              className="show-write-btn"
            >
              방명록 쓰기
            </button>
          </div>
        )}

        {/* 글 작성 폼 */}
        {showWriteForm && (
          <section className="write-section">
            <div className="write-header">
              <h2>방명록 작성</h2>
              <button 
                onClick={() => setShowWriteForm(false)}
                className="close-write-btn"
              >
                닫기
              </button>
            </div>
            <form onSubmit={handlePostSubmit}>
              <input
                type="text"
                placeholder="제목"
                value={newPost.title}
                onChange={(e) => setNewPost({ ...newPost, title: e.target.value })}
                required
              />
              <textarea
                placeholder="내용을 입력하세요"
                value={newPost.content}
                onChange={(e) => setNewPost({ ...newPost, content: e.target.value })}
                required
              />
              <button type="submit">작성하기</button>
            </form>
          </section>
        )}

        {/* 방명록 리스트 */}
        <section className="posts-section">
          {guestbookData.length === 0 ? (
            <div className="empty-guestbook">
              <p>방명록이 비어있습니다.</p>
              {!showWriteForm && (
                <button 
                  onClick={() => setShowWriteForm(true)}
                  className="write-first-btn"
                >
                  첫 번째 방명록 작성하기
                </button>
              )}
            </div>
          ) : (
            guestbookData.map((post) => {
              const postId = post.guestbookId || post.id;
              console.log('📝 Post 데이터:', post, 'ID:', postId);
              
              return (
                <div key={postId} className="post-card">
                  {editingPost && (editingPost.guestbookId === postId || editingPost.id === postId) ? (
                    <div className="edit-form">
                      <input
                        value={editingPost.title || ''}
                        onChange={(e) => setEditingPost({ ...editingPost, title: e.target.value })}
                      />
                      <textarea
                        value={editingPost.content || ''}
                        onChange={(e) => setEditingPost({ ...editingPost, content: e.target.value })}
                      />
                      <div className="button-group">
                        <button onClick={() => handlePostUpdate(postId)}>저장</button>
                        <button onClick={() => setEditingPost(null)}>취소</button>
                      </div>
                    </div>
                  ) : (
                    <>
                      <div className="post-header">
                        <h3>{post.title}</h3>
                        <div className="post-actions">
                          <button onClick={() => setEditingPost(post)}>수정</button>
                          <button onClick={() => handlePostDelete(postId)}>삭제</button>
                        </div>
                      </div>
                      <p className="post-content">{post.content}</p>
                      <div className="post-info">
                        <span>글 작성자: {post.guestNickname}</span>
                        <span>{new Date(post.createdAt).toLocaleDateString()}</span>
                      </div>

                      {/* 댓글 섹션 */}
                      <div className="comments-section">
                        <h4>댓글 ({post.comments?.length || 0})</h4>
                        {post.comments?.map((comment) => (
                          <div key={comment.commentId} className="comment">
                            {editingComment && editingComment.commentId === comment.commentId ? (
                              <div className="edit-comment">
                                <input
                                  value={editingComment.content || ''}
                                  onChange={(e) => setEditingComment({ ...editingComment, content: e.target.value })}
                                />
                                <button onClick={() => handleCommentUpdate(comment.commentId)}>저장</button>
                                <button onClick={() => setEditingComment(null)}>취소</button>
                              </div>
                            ) : (
                              <>
                                <p>{comment.content}</p>
                                <div className="comment-info">
                                  <span>댓글 작성자: {comment.user?.nickname || '익명'}</span>
                                  <span>
                                    {comment.createdAt 
                                      ? new Date(comment.createdAt).toLocaleDateString('ko-KR', {
                                          year: 'numeric',
                                          month: 'long',
                                          day: 'numeric',
                                          hour: '2-digit',
                                          minute: '2-digit'
                                        })
                                      : '날짜 없음'
                                    }
                                  </span>
                                  <button onClick={() => setEditingComment(comment)}>수정</button>
                                  <button onClick={() => handleCommentDelete(comment.commentId)}>삭제</button>
                                </div>
                              </>
                            )}
                          </div>
                        ))}

                        <div className="comment-write">
                          <input
                            placeholder="댓글을 입력하세요"
                            value={newComment[postId] || ''}
                            onChange={(e) => setNewComment({ ...newComment, [postId]: e.target.value })}
                          />
                          <button onClick={() => handleCommentSubmit(postId)}>댓글 작성</button>
                        </div>
                      </div>
                    </>
                  )}
                </div>
              );
            })
          )}
        </section>
      </div>
    </div>
  );
}

export default GuestbookPage;