import React from 'react';
import './MainPage.css';

function MainPage() {
  return (
    <div className="main-container">
      <div className="main-content">
        <h1 className="main-title">방명록 서비스</h1>
        <p className="main-subtitle">친구들과 추억을 공유하세요</p>
        
        <div className="main-buttons">
          <button 
            onClick={() => window.location.href = '/login'}
            className="btn-login"
          >
            로그인
          </button>
          <button 
            onClick={() => window.location.href = '/signup'}
            className="btn-signup"
          >
            회원가입
          </button>
        </div>

        <div className="features">
          <div className="feature-card">
            <div className="feature-icon">📝</div>
            <h3>방명록 작성</h3>
            <p>친구들의 방명록에 글을 남기세요</p>
          </div>
          <div className="feature-card">
            <div className="feature-icon">👥</div>
            <h3>친구 관리</h3>
            <p>친구를 추가하고 관리하세요</p>
          </div>
          <div className="feature-card">
            <div className="feature-icon">💬</div>
            <h3>댓글 기능</h3>
            <p>방명록에 댓글을 달아보세요</p>
          </div>
        </div>
      </div>
    </div>
  );
}

export default MainPage;