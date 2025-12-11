import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../api/axiosConfig';

function LoginPage() {
  const navigate = useNavigate();
  // DTO(LoginRequest)와 필드명 일치: email, password
  const [loginData, setLoginData] = useState({ email: '', password: '' });

  const handleChange = (e) => {
    setLoginData({ ...loginData, [e.target.name]: e.target.value });
  };

  const handleLogin = async () => {
    if(!loginData.email || !loginData.password) return alert("값을 입력해주세요");
    
    try {
      const response = await api.post('/api/login', loginData);
      
      // LoginResponse: { id, name, role, token }
      const { id, name, role } = response.data;
      
      // 로그인 세션 처리 (간이 구현)
      localStorage.setItem('userId', id);
      localStorage.setItem('userName', name);
      
      alert(`${name}님 환영합니다!`);
      navigate('/mypage/tickets'); 
    } catch (error) {
      console.error("로그인 에러:", error);
      alert("로그인 실패: 이메일과 비밀번호를 확인하세요.");
    }
  };

  return (
    <div className="container" style={{ textAlign: 'center', marginTop: '100px' }}>
      <h1>체육시설 예약 시스템</h1>
      <div className="box" style={{ maxWidth: '400px', margin: '0 auto' }}>
        <input type="text" name="email" placeholder="Email" onChange={handleChange} style={{ display: 'block', width: '90%', margin: '10px auto', padding: '10px' }} />
        <input type="password" name="password" placeholder="Password" onChange={handleChange} style={{ display: 'block', width: '90%', margin: '10px auto', padding: '10px' }} />
        <button className="btn-primary" style={{width:'95%'}} onClick={handleLogin}>로그인</button>
      </div>
    </div>
  );
}
export default LoginPage;