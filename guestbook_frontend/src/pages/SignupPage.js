import { useState } from 'react';
import './SignupPage.css';

export default function SignupPage() {
  const [formData, setFormData] = useState({
    loginId: '',
    password: '',
    passwordConfirm: '',
    nickname: '',
    statusMessage: ''
  });
  
  const [errors, setErrors] = useState({});
  const [result, setResult] = useState(null);
  const [checkStatus, setCheckStatus] = useState({
    loginId: false,
    nickname: false
  });
  const [isChecking, setIsChecking] = useState({
    loginId: false,
    nickname: false
  });
  const [isSubmitting, setIsSubmitting] = useState(false);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
    
    // 입력 시 해당 필드의 에러 제거
    if (errors[name]) {
      setErrors(prev => ({
        ...prev,
        [name]: ''
      }));
    }
    
    // 입력 변경 시 중복체크 상태 초기화
    if (name === 'loginId' || name === 'nickname') {
      setCheckStatus(prev => ({
        ...prev,
        [name]: false
      }));
    }
  };

  // 아이디 중복체크
  const handleCheckLoginId = async () => {
    if (!formData.loginId.trim()) {
      alert('아이디를 입력해주세요.');
      return;
    }
    
    setIsChecking(prev => ({ ...prev, loginId: true }));
    
    try {
      const response = await fetch('http://localhost:8080/api/sign-up/duplicate-check/login-id', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ checkOBject: formData.loginId })
      });

      if (response.ok) {
        const data = await response.json();
        
        if (!data.isDuplicate) {
          alert('사용 가능한 아이디입니다!');
          setCheckStatus(prev => ({ ...prev, loginId: true }));
        } else {
          alert('이미 사용 중인 아이디입니다.');
          setCheckStatus(prev => ({ ...prev, loginId: false }));
        }
      } else {
        alert('중복 체크에 실패했습니다. 다시 시도해주세요.');
      }
    } catch (error) {
      console.error('아이디 중복체크 에러:', error);
      alert('서버와의 연결에 실패했습니다.');
    } finally {
      setIsChecking(prev => ({ ...prev, loginId: false }));
    }
  };

  // 닉네임 중복체크
  const handleCheckNickname = async () => {
    if (!formData.nickname.trim()) {
      alert('닉네임을 입력해주세요.');
      return;
    }
    
    setIsChecking(prev => ({ ...prev, nickname: true }));
    
    try {
      const response = await fetch('http://localhost:8080/api/sign-up/duplicate-check/nickname', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ checkObject: formData.nickname })
      });

      if (response.ok) {
        const data = await response.json();
        
        if (!data.isDuplicate) {
          alert('사용 가능한 닉네임입니다!');
          setCheckStatus(prev => ({ ...prev, nickname: true }));
        } else {
          alert('이미 사용 중인 닉네임입니다.');
          setCheckStatus(prev => ({ ...prev, nickname: false }));
        }
      } else {
        alert('중복 체크에 실패했습니다. 다시 시도해주세요.');
      }
    } catch (error) {
      console.error('닉네임 중복체크 에러:', error);
      alert('서버와의 연결에 실패했습니다.');
    } finally {
      setIsChecking(prev => ({ ...prev, nickname: false }));
    }
  };

  const validate = () => {
    const newErrors = {};
    
    if (!formData.loginId.trim()) {
      newErrors.loginId = '로그인 아이디를 입력해주세요.';
    } else if (!checkStatus.loginId) {
      newErrors.loginId = '아이디 중복체크를 해주세요.';
    }
    
    if (!formData.password) {
      newErrors.password = '비밀번호를 입력해주세요.';
    }
    
    if (!formData.passwordConfirm) {
      newErrors.passwordConfirm = '비밀번호 확인을 입력해주세요.';
    } else if (formData.password !== formData.passwordConfirm) {
      newErrors.passwordConfirm = '비밀번호가 일치하지 않습니다.';
    }
    
    if (!formData.nickname.trim()) {
      newErrors.nickname = '닉네임을 입력해주세요.';
    } else if (!checkStatus.nickname) {
      newErrors.nickname = '닉네임 중복체크를 해주세요.';
    }
    
    return newErrors;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    const newErrors = validate();
    
    if (Object.keys(newErrors).length > 0) {
      setErrors(newErrors);
      return;
    }
    
    // 전송할 데이터 (statusMessage가 비어있으면 null로 처리)
    const submitData = {
      loginId: formData.loginId,
      password: formData.password,
      nickname: formData.nickname,
      statusMsg: formData.statusMessage.trim() || null
    };
    
    setIsSubmitting(true);
    
    try {
      const response = await fetch('http://localhost:8080/api/sign-up', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(submitData)
      });

      if (response.ok) {
        const data = await response.json();
        alert('회원가입이 완료되었습니다!');
        console.log('회원가입 성공:', data);
        
        window.location.href = '/login';
      } else {
        const errorData = await response.json();
        alert(`회원가입 실패: ${errorData.message || '다시 시도해주세요.'}`);
      }
    } catch (error) {
      console.error('회원가입 에러:', error);
      alert('서버와의 연결에 실패했습니다.');
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <div className="signup-page">
      <div className="signup-card">
        <h1 className="signup-title">회원가입</h1>
        
        <form onSubmit={handleSubmit} className="signup-form">
          <div className="form-group-with-button">
            <label className="form-label">
              로그인 아이디
            </label>
            <div className="input-button-wrapper">
              <input
                type="text"
                name="loginId"
                value={formData.loginId}
                onChange={handleChange}
                className={`form-input ${errors.loginId ? 'error' : ''}`}
                placeholder="아이디를 입력하세요"
                disabled={isSubmitting}
              />
              <button
                type="button"
                onClick={handleCheckLoginId}
                className="check-button"
                disabled={!formData.loginId.trim() || isChecking.loginId || isSubmitting}
              >
                {isChecking.loginId ? '확인 중...' : '중복체크'}
              </button>
            </div>
            {errors.loginId && (
              <p className="error-message">{errors.loginId}</p>
            )}
            {checkStatus.loginId && !errors.loginId && (
              <p className="success-message">✓ 사용 가능한 아이디입니다</p>
            )}
          </div>

          <div className="form-group">
            <label className="form-label">
              비밀번호
            </label>
            <input
              type="password"
              name="password"
              value={formData.password}
              onChange={handleChange}
              className={`form-input ${errors.password ? 'error' : ''}`}
              placeholder="비밀번호를 입력하세요"
              disabled={isSubmitting}
            />
            {errors.password && (
              <p className="error-message">{errors.password}</p>
            )}
          </div>

          <div className="form-group">
            <label className="form-label">
              비밀번호 확인
            </label>
            <input
              type="password"
              name="passwordConfirm"
              value={formData.passwordConfirm}
              onChange={handleChange}
              className={`form-input ${errors.passwordConfirm ? 'error' : ''}`}
              placeholder="비밀번호를 다시 입력하세요"
              disabled={isSubmitting}
            />
            {errors.passwordConfirm && (
              <p className="error-message">{errors.passwordConfirm}</p>
            )}
          </div>

          <div className="form-group-with-button">
            <label className="form-label">
              닉네임
            </label>
            <div className="input-button-wrapper">
              <input
                type="text"
                name="nickname"
                value={formData.nickname}
                onChange={handleChange}
                className={`form-input ${errors.nickname ? 'error' : ''}`}
                placeholder="닉네임을 입력하세요"
                disabled={isSubmitting}
              />
              <button
                type="button"
                onClick={handleCheckNickname}
                className="check-button"
                disabled={!formData.nickname.trim() || isChecking.nickname || isSubmitting}
              >
                {isChecking.nickname ? '확인 중...' : '중복체크'}
              </button>
            </div>
            {errors.nickname && (
              <p className="error-message">{errors.nickname}</p>
            )}
            {checkStatus.nickname && !errors.nickname && (
              <p className="success-message">✓ 사용 가능한 닉네임입니다</p>
            )}
          </div>

          <div className="form-group">
            <label className="form-label">
              상태메시지 <span className="optional-text">(선택)</span>
            </label>
            <input
              type="text"
              name="statusMessage"
              value={formData.statusMessage}
              onChange={handleChange}
              className="form-input"
              placeholder="상태메시지를 입력하세요"
              disabled={isSubmitting}
            />
          </div>

          <button
            type="submit"
            className="submit-button"
            disabled={isSubmitting}
          >
            {isSubmitting ? '회원가입 중...' : '회원가입'}
          </button>
        </form>

        {result && (
          <div className="result-container">
            <h2 className="result-title">전송 데이터 (JSON)</h2>
            <pre className="result-json">
              {JSON.stringify(result, null, 2)}
            </pre>
          </div>
        )}
      </div>
    </div>
  );
}