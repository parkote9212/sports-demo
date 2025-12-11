import axios from 'axios';

const api = axios.create({
  baseURL: '', // 프록시 사용
  headers: {
    'Content-Type': 'application/json',
  },
});

// 관리자도 로그인 후 ID나 토큰을 헤더에 실어 보냅니다.
api.interceptors.request.use((config) => {
  const userId = localStorage.getItem('adminId'); // 로컬스토리지 키를 다르게(adminId) 가져감
  if (userId) {
    config.headers['X-USER-ID'] = userId; 
  }
  return config;
});

export default api;