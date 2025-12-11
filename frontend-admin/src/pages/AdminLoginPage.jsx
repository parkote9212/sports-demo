import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../api/axiosConfig';

function AdminLoginPage() {
  const navigate = useNavigate();
  const [loginData, setLoginData] = useState({ email: '', password: '' });

  const handleLogin = async () => {
    try {
      // [Backend] 관리자 로그인 요청
      const response = await api.post('/api/login', loginData);
      
      // (선택) 권한 체크 로직
      // if (response.data.role !== 'ADM') return alert("관리자만 접속 가능합니다.");

      localStorage.setItem('adminId', response.data.id);
      localStorage.setItem('adminName', response.data.name);
      
      alert("관리자 페이지에 접속합니다.");
      // ✅ 변경점: URL에서 /admin을 뺌 (어차피 관리자 전용 사이트니까요)
      navigate('/dashboard'); 
    } catch (error) {
      alert("로그인 실패: 정보를 확인해주세요.");
    }
  };

  return (
    <div style={{ height: '100vh', background: '#2c3e50', display: 'flex', justifyContent: 'center', alignItems: 'center' }}>
      <div style={{ background: '#fff', padding: '40px', borderRadius: '10px', width: '350px', textAlign: 'center' }}>
        <h2 style={{ color: '#2c3e50' }}>ADMIN SYSTEM</h2>
        <input 
            type="text" placeholder="Admin ID" 
            onChange={(e) => setLoginData({...loginData, email: e.target.value})}
            style={{ width: '90%', padding: '10px', marginBottom: '10px' }} 
        />
        <input 
            type="password" placeholder="Password" 
            onChange={(e) => setLoginData({...loginData, password: e.target.value})}
            style={{ width: '90%', padding: '10px', marginBottom: '20px' }} 
        />
        <button onClick={handleLogin}
            style={{ width: '100%', padding: '12px', background: '#3498db', color: '#fff', border: 'none', cursor: 'pointer', fontWeight: 'bold' }}>
            접속하기
        </button>
      </div>
    </div>
  );
}

export default AdminLoginPage;