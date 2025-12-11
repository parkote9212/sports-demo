import { defineConfig, loadEnv } from 'vite'
import react from '@vitejs/plugin-react'

// https://vitejs.dev/config/
export default defineConfig(({ mode }) => {
  // 현재 작업 경로(process.cwd())에서 .env 파일을 찾아 환경 변수를 로드합니다.
  const env = loadEnv(mode, process.cwd(), '')

  // 안전한 proxy target 결정: 환경변수가 HTTP/HTTPS 스킴을 포함한 경우에만 사용.
  const rawApiUrl = env.VITE_API_URL || '';
  const proxyTarget = rawApiUrl.startsWith('http') ? rawApiUrl : 'http://localhost:8080';

  return {
    plugins: [react()],
    server: {
      port: 5173, // 관리자 페이지 포트 유지
      proxy: {
        '/api': {
          target: proxyTarget,
          changeOrigin: true,
          secure: false,
        }
      }
    }
  }
})