import React, { useState, useEffect } from 'react';
import './GuestbookPage.css';

function GuestbookPage() {
  const [guestbookData, setGuestbookData] = useState([]);
  const [newPost, setNewPost] = useState({ title: '', content: '' });
  const [newComment, setNewComment] = useState({});
  const [editingPost, setEditingPost] = useState(null);
  const [editingComment, setEditingComment] = useState(null);
  const [loading, setLoading] = useState(true);

  // URL에서 닉네임 가져오기
  const nickname = window.location.pathname.split('/').pop();

  useEffect(() => {
    fetchGuestbook();
  }, [nickname]);

  const fetchGuestbook = async () => {
    try {
      const response = await fetch(`/api/guestbook/${nickname}`);
      if (response.ok) {
        const data = await response.json();
        setGuestbookData(data);
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
    try {
      const response = await fetch('/api/guestbook', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ ...newPost, ownerNickname: nickname })
      });

      if (response.ok) {
        setNewPost({ title: '', content: '' });
        fetchGuestbook();
      }
    } catch (error) {
      console.error('글 작성 실패:', error);
    }
  };

  // 글 수정
  const handlePostUpdate = async (guestbookId) => {
    try {
      const response = await fetch(`/api/guestbook/${guestbookId}`, {
        method: 'PATCH',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(editingPost)
      });

      if (response.ok) {
        setEditingPost(null);
        fetchGuestbook();
      }
    } catch (error) {
      console.error('글 수정 실패:', error);
    }
  };

  // 글 삭제
  const handlePostDelete = async (guestbookId) => {
    if (!window.confirm('정말 삭제하시겠습니까?')) return;

    try {
      const response = await fetch(`/api/guestbook/${guestbookId}`, {
        method: 'DELETE'
      });

      if (response.ok) {
        fetchGuestbook();
      }
    } catch (error) {
      console.error('글 삭제 실패:', error);
    }
  };

  // 댓글 작성
  const handleCommentSubmit = async (guestbookId) => {
    try {
      const response = await fetch(`/api/guestbook/${guestbookId}/comment`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ content: newComment[guestbookId] })
      });

      if (response.ok) {
        setNewComment({ ...newComment, [guestbookId]: '' });
        fetchGuestbook();
      }
    } catch (error) {
      console.error('댓글 작성 실패:', error);
    }
  };

  // 댓글 수정
  const handleCommentUpdate = async (commentId) => {
    try {
      const response = await fetch(`/api/guestbook/comment/${commentId}`, {
        method: 'PATCH',
        headers: { 'Content-Type': 'application/json' },
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
      const response = await fetch(`/api/guestbook/comment/${commentId}`, {
        method: 'DELETE'
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
        {/* 글 작성 폼 */}
        <section className="write-section">
          <h2>방명록 작성</h2>
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

        {/* 방명록 리스트 */}
        <section className="posts-section">
          {guestbookData.map((post) => (
            <div key={post.guestbookId} className="post-card">
              {editingPost?.guestbookId === post.guestbookId ? (
                <div className="edit-form">
                  <input
                    value={editingPost.title}
                    onChange={(e) => setEditingPost({ ...editingPost, title: e.target.value })}
                  />
                  <textarea
                    value={editingPost.content}
                    onChange={(e) => setEditingPost({ ...editingPost, content: e.target.value })}
                  />
                  <div className="button-group">
                    <button onClick={() => handlePostUpdate(post.guestbookId)}>저장</button>
                    <button onClick={() => setEditingPost(null)}>취소</button>
                  </div>
                </div>
              ) : (
                <>
                  <div className="post-header">
                    <h3>{post.title}</h3>
                    <div className="post-actions">
                      <button onClick={() => setEditingPost(post)}>수정</button>
                      <button onClick={() => handlePostDelete(post.guestbookId)}>삭제</button>
                    </div>
                  </div>
                  <p className="post-content">{post.content}</p>
                  <div className="post-info">
                    <span>{post.guestNickname}</span>
                    <span>{new Date(post.createdAt).toLocaleDateString()}</span>
                  </div>

                  {/* 댓글 섹션 */}
                  <div className="comments-section">
                    <h4>댓글 ({post.comments?.length || 0})</h4>
                    {post.comments?.map((comment) => (
                      <div key={comment.commentId} className="comment">
                        {editingComment?.commentId === comment.commentId ? (
                          <div className="edit-comment">
                            <input
                              value={editingComment.content}
                              onChange={(e) => setEditingComment({ ...editingComment, content: e.target.value })}
                            />
                            <button onClick={() => handleCommentUpdate(comment.commentId)}>저장</button>
                            <button onClick={() => setEditingComment(null)}>취소</button>
                          </div>
                        ) : (
                          <>
                            <p>{comment.content}</p>
                            <div className="comment-info">
                              <span>{comment.nickname}</span>
                              <span>{new Date(comment.createdAt).toLocaleDateString()}</span>
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
                        value={newComment[post.guestbookId] || ''}
                        onChange={(e) => setNewComment({ ...newComment, [post.guestbookId]: e.target.value })}
                      />
                      <button onClick={() => handleCommentSubmit(post.guestbookId)}>댓글 작성</button>
                    </div>
                  </div>
                </>
              )}
            </div>
          ))}
        </section>
      </div>
    </div>
  );
}

export default GuestbookPage;