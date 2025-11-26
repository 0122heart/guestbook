import React, { useState, useEffect } from 'react';
import './ProfilePage.css';

function ProfilePage() {
  const [profile, setProfile] = useState({
    userId: '',
    loginId: '',
    nickname: '',
    email: '',
    createdAt: ''
  });
  const [isEditing, setIsEditing] = useState(false);
  const [editData, setEditData] = useState({});
  const [passwordData, setPasswordData] = useState({
    currentPassword: '',
    newPassword: '',
    confirmPassword: ''
  });
  const [showPasswordChange, setShowPasswordChange] = useState(false);

  useEffect(() => {
    fetchProfile();
  }, []);

  const fetchProfile = async () => {
    try {
      // 실제로는 세션/토큰으로 현재 사용자 정보 가져오기
      const response = await fetch('/api/user/profile');
      if (response.ok) {
        const data = await response.json();
        setProfile(data);
        setEditData(data);
      }
    } catch (error) {
      console.error('프로필 불러오기 실패:', error);
    }
  };

  const handleEdit = () => {
    setIsEditing(true);
    setEditData(profile);
  };

  const handleCancel = () => {
    setIsEditing(false);
    setEditData(profile);
  };

  const handleSave = async () => {
    try {
      const response = await fetch('/api/user', {
        method: 'PATCH',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          nickname: editData.nickname,
          email: editData.email
        })
      });

      if (response.ok) {
        alert('프로필이 수정되었습니다!');
        setProfile(editData);
        setIsEditing(false);
      } else {
        alert('프로필 수정에 실패했습니다.');
      }
    } catch (error) {
      console.error('프로필 수정 실패:', error);
      alert('프로필 수정 중 오류가 발생했습니다.');
    }
  };

  const handlePasswordChange = async (e) => {
    e.preventDefault();

    if (passwordData.newPassword !== passwordData.confirmPassword) {
      alert('새 비밀번호가 일치하지 않습니다.');
      return;
    }

    try {
      const response = await fetch('/api/user/password', {
        method: 'PATCH',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          currentPassword: passwordData.currentPassword,
          newPassword: passwordData.newPassword
        })
      });

      if (response.ok) {
        alert('비밀번호가 변경되었습니다!');
        setPasswordData({ currentPassword: '', newPassword: '', confirmPassword: '' });
        setShowPasswordChange(false);
      } else {
        alert('비밀번호 변경에 실패했습니다. 현재 비밀번호를 확인해주세요.');
      }
    } catch (error) {
      console.error('비밀번호 변경 실패:', error);
      alert('비밀번호 변경 중 오류가 발생했습니다.');
    }
  };

  return (
    <div className="profile-container">
      <header className="profile-header">
        <h1>내 프로필</h1>
        <button onClick={() => window.location.href = '/home'}>홈으로</button>
      </header>

      <div className="profile-main">
        <section className="profile-card">
          <div className="profile-avatar">
            {profile.nickname.charAt(0).toUpperCase()}
          </div>

          <div className="profile-info">
            {isEditing ? (
              <>
                <div className="info-row">
                  <label>로그인 아이디</label>
                  <input value={profile.loginId} disabled />
                </div>
                <div className="info-row">
                  <label>닉네임</label>
                  <input
                    value={editData.nickname}
                    onChange={(e) => setEditData({ ...editData, nickname: e.target.value })}
                  />
                </div>
                <div className="info-row">
                  <label>이메일</label>
                  <input
                    type="email"
                    value={editData.email}
                    onChange={(e) => setEditData({ ...editData, email: e.target.value })}
                  />
                </div>
                <div className="button-group">
                  <button onClick={handleSave} className="save-btn">저장</button>
                  <button onClick={handleCancel} className="cancel-btn">취소</button>
                </div>
              </>
            ) : (
              <>
                <div className="info-row">
                  <span className="label">로그인 아이디</span>
                  <span className="value">{profile.loginId}</span>
                </div>
                <div className="info-row">
                  <span className="label">닉네임</span>
                  <span className="value">{profile.nickname}</span>
                </div>
                <div className="info-row">
                  <span className="label">이메일</span>
                  <span className="value">{profile.email}</span>
                </div>
                <div className="info-row">
                  <span className="label">가입일</span>
                  <span className="value">{new Date(profile.createdAt).toLocaleDateString()}</span>
                </div>
                <button onClick={handleEdit} className="edit-btn">프로필 수정</button>
              </>
            )}
          </div>
        </section>

        <section className="password-section">
          <h2>비밀번호 변경</h2>
          {showPasswordChange ? (
            <form onSubmit={handlePasswordChange}>
              <div className="input-group">
                <label>현재 비밀번호</label>
                <input
                  type="password"
                  value={passwordData.currentPassword}
                  onChange={(e) => setPasswordData({ ...passwordData, currentPassword: e.target.value })}
                  required
                />
              </div>
              <div className="input-group">
                <label>새 비밀번호</label>
                <input
                  type="password"
                  value={passwordData.newPassword}
                  onChange={(e) => setPasswordData({ ...passwordData, newPassword: e.target.value })}
                  required
                />
              </div>
              <div className="input-group">
                <label>새 비밀번호 확인</label>
                <input
                  type="password"
                  value={passwordData.confirmPassword}
                  onChange={(e) => setPasswordData({ ...passwordData, confirmPassword: e.target.value })}
                  required
                />
              </div>
              <div className="button-group">
                <button type="submit" className="save-btn">변경하기</button>
                <button
                  type="button"
                  onClick={() => setShowPasswordChange(false)}
                  className="cancel-btn"
                >
                  취소
                </button>
              </div>
            </form>
          ) : (
            <button onClick={() => setShowPasswordChange(true)} className="change-password-btn">
              비밀번호 변경
            </button>
          )}
        </section>

        <section className="actions-section">
          <button
            onClick={() => window.location.href = `/guestbook/${profile.nickname}`}
            className="guestbook-btn"
          >
            내 방명록 보기
          </button>
          <button
            onClick={() => window.location.href = '/friends'}
            className="friends-btn"
          >
            친구 관리
          </button>
        </section>
      </div>
    </div>
  );
}

export default ProfilePage;