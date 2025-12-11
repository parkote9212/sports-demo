import { defineConfig, loadEnv } from 'vite'
import react from '@vitejs/plugin-react'

// https://vitejs.dev/config/
export default defineConfig(({ mode }) => {
  // 현재 작업 경로(process.cwd())에서 .env 파일을 찾아 환경 변수를 로드합니다.
  const env = loadEnv(mode, process.cwd(), '')

  return {
    plugins: [react()],
    server: {
      port: 5173, // 관리자 페이지 포트 유지
      proxy: {
        '/api': {
          // .env 파일에 있는 VITE_API_URL 값을 우선 사용하고,
          // 없으면 localhost를 씁니다. (안전장치)
          target: env.VITE_API_URL || 'http://localhost:8080',
          
          changeOrigin: true,
          secure: false,
        }
      }
    }
  }
})