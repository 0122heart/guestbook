import React, { useState } from 'react';
import './LoginPage.css';

function LoginPage() {
  const [formData, setFormData] = useState({
    loginId: '',
    password: ''
  });
  const [isLoading, setIsLoading] = useState(false);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setIsLoading(true);
    
    try {
      const response = await fetch('/api/sign-in', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(formData)
      });

      if (response.ok) {
        const data = await response.json();
        // 로그인 성공 처리 (토큰 저장)
        localStorage.setItem('token', data.token);
        localStorage.setItem('nickname', data.nickname);
        alert('로그인 성공!');
        window.location.href = '/home';
      } else {
        alert('로그인 실패: 아이디 또는 비밀번호를 확인해주세요.');
      }
    } catch (error) {
      console.error('로그인 에러:', error);
      alert('로그인 중 오류가 발생했습니다.');
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="login-container">
      <div className="login-box">
        <h1>로그인</h1>
        <form onSubmit={handleSubmit}>
          <div className="input-group">
            <label htmlFor="loginId">아이디</label>
            <input
              type="text"
              id="loginId"
              name="loginId"
              value={formData.loginId}
              onChange={handleChange}
              required
            />
          </div>
          
          <div className="input-group">
            <label htmlFor="password">비밀번호</label>
            <input
              type="password"
              id="password"
              name="password"
              value={formData.password}
              onChange={handleChange}
              required
            />
          </div>

          <button type="submit" className="login-btn" disabled={isLoading}>
            {isLoading ? '로그인 중...' : '로그인'}
          </button>
        </form>
        
        <div className="login-footer">
          <a href="/signup">회원가입</a>
        </div>
      </div>
    </div>
  );
}

export default LoginPage;