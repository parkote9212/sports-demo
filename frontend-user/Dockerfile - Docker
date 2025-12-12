# STAGE 1: 빌드
FROM node:lts-alpine AS builder
WORKDIR /app
COPY package.json package-lock.json ./
RUN npm ci

COPY . .

# 👇 [수정 1] .env 파일을 빌드 명령보다 '앞서서' 복사해야 합니다.
# 이 파일 안에 "VITE_API_URL=/api"가 들어있어야 합니다.
COPY .env .env

# 이제 빌드하면 .env 값을 읽어서 코드에 적용합니다.
RUN npm run build

# STAGE 2: 실행
FROM nginx:alpine AS final

# 빌드 결과물 복사 (기존과 동일)
COPY --from=builder /app/dist /usr/share/nginx/html

# 👇 [수정 2] 가지고 계신 Nginx 설정 파일의 '정확한 이름'을 적어주세요.
# (예: 폴더에 있는 파일명이 'nginx - admin.conf'라면 아래처럼 작성)
# 목적지 파일명은 무조건 'default.conf'여야 합니다.
COPY nginx.conf /etc/nginx/conf.d/default.conf 
# 팁: 헷갈리면 로컬 파일명을 그냥 nginx.conf로 통일해도 됩니다.

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]