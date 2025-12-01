import React from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';

// 페이지 컴포넌트 import
import MainPage from './pages/MainPage';
import LoginPage from './pages/LoginPage';
import SignupPage from './pages/SignupPage';
import HomePage from './pages/HomePage';
import ProfilePage from './pages/ProfilePage';
import GuestbookPage from './pages/GuestbookPage';
import FriendsPage from './pages/FriendsPage';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        {/* 공개 페이지 */}
        <Route path="/" element={<MainPage />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/signup" element={<SignupPage />} />

        {/* 로그인 필요한 페이지 */}
        <Route 
          path="/home" 
          element={<ProtectedRoute><HomePage /></ProtectedRoute>} 
        />
        <Route 
          path="/profile" 
          element={<ProtectedRoute><ProfilePage /></ProtectedRoute>}
        />
        <Route 
          path="/profile/:nickname" 
          element={<ProtectedRoute><ProfilePage /></ProtectedRoute>} 
        />
        <Route 
          path="/guestbook/:nickname" 
          element={<ProtectedRoute><GuestbookPage /></ProtectedRoute>} 
        />
        <Route 
          path="/friends" 
          element={<ProtectedRoute><FriendsPage /></ProtectedRoute>} 
        />

        {/* 404 페이지 */}
        <Route path="*" element={<NotFoundPage />} />
      </Routes>
    </BrowserRouter>
  );
}

// 로그인 보호 컴포넌트
function ProtectedRoute({ children }) {
  const isLoggedIn = localStorage.getItem('isLoggedIn') === 'true';
  
  if (!isLoggedIn) {
    return <Navigate to="/login" replace />;
  }
  
  return children;
}

// 404 페이지
function NotFoundPage() {
  return (
    <div style={{ 
      display: 'flex', 
      flexDirection: 'column',
      alignItems: 'center', 
      justifyContent: 'center', 
      height: '100vh',
      fontFamily: 'sans-serif'
    }}>
      <h1 style={{ fontSize: '72px', margin: '0' }}>404</h1>
      <p style={{ fontSize: '24px', color: '#666' }}>페이지를 찾을 수 없습니다</p>
      <a 
        href="/" 
        style={{ 
          marginTop: '20px', 
          padding: '10px 20px', 
          backgroundColor: '#007bff', 
          color: 'white', 
          textDecoration: 'none',
          borderRadius: '5px'
        }}
      >
        홈으로 돌아가기
      </a>
    </div>
  );
}

export default App;