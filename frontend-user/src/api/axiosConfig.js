import axios from 'axios';

const api = axios.create({
  baseURL: '', // vite.config.js의 proxy가 처리하므로 비워둡니다.
  headers: {
    'Content-Type': 'application/json',
  },
});

// 요청 인터셉터: 로컬 스토리지에 userId가 있으면 헤더에 자동 추가
api.interceptors.request.use((config) => {
  const userId = localStorage.getItem('userId');
  if (userId) {
    config.headers['X-USER-ID'] = userId; // 백엔드 @RequestHeader와 일치
  }
  return config;
});

export default api;