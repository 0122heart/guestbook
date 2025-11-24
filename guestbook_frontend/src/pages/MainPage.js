// src/pages/MainPage.js
import React from 'react';
import { useNavigate } from 'react-router-dom';
import { Users, MessageCircle, Heart } from 'lucide-react';
import './MainPage.css';

function MainPage() {
  const navigate = useNavigate();

  return (
    <div className="main-page">
      <div className="main-container">
        {/* 헤더 */}
        <header className="main-header">          
          <h1 className="main-title">
            Hello Guestbook
          </h1>
          
          <p className="main-subtitle">
            친구들과 따뜻한 마음을 나누는 공간
          </p>
          
          {/* 로그인/회원가입 버튼 */}
          <div className="button-group">
            <button
              onClick={() => navigate('/login')}
              className="btn btn-primary"
            >
              로그인
            </button>
            <button
              onClick={() => navigate('/signup')}
              className="btn btn-secondary"
            >
              회원가입
            </button>
          </div>
        </header>

        {/* 기능 소개 섹션 */}
        <div className="features-section">
          <h2 className="section-title">
            무엇을 할 수 있나요?
          </h2>
          
          <div className="features-grid">
            {/* 기능 1: 방명록 */}
            <div className="feature-card">
              <div className="feature-icon feature-icon-blue">
                <MessageCircle className="icon" />
              </div>
              <h3 className="feature-title">
                방명록 나누기
              </h3>
              <p className="feature-description">
                친구들의 페이지에 방문해 따뜻한 메시지를 남기고, 
                소중한 추억을 함께 만들어가세요
              </p>
            </div>

            {/* 기능 2: 친구 관리 */}
            <div className="feature-card">
              <div className="feature-icon feature-icon-purple">
                <Users className="icon" />
              </div>
              <h3 className="feature-title">
                친구와 연결
              </h3>
              <p className="feature-description">
                친구 요청을 보내고 수락하며, 
                소중한 인연들과 더 가까워지세요
              </p>
            </div>

            {/* 기능 3: 기분 표현 */}
            <div className="feature-card">
              <div className="feature-icon feature-icon-pink">
                <Heart className="icon" />
              </div>
              <h3 className="feature-title">
                기분 공유
              </h3>
              <p className="feature-description">
                오늘의 기분을 상태 메시지로 표현하고, 
                친구들과 감정을 나누세요
              </p>
            </div>
          </div>
        </div>

        {/* CTA 섹션 */}
        <div className="cta-section">
          <h2 className="cta-title">
            지금 바로 시작하세요!
          </h2>
          <p className="cta-subtitle">
            무료로 가입하고 친구들과 소통을 시작해보세요
          </p>
          <button
            onClick={() => navigate('/signup')}
            className="btn btn-cta"
          >
            무료로 시작하기 →
          </button>
        </div>

        {/* 푸터 */}
        <footer className="main-footer">
          <p>© 2024 Hello Guestbook. 따뜻한 마음을 전하는 공간</p>
        </footer>
      </div>
    </div>
  );
}

export default MainPage;