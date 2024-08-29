FROM node:18
WORKDIR /

# 의존성 파일 복사
COPY package.json package-lock.json ./

# 의존성 설치
RUN npm install

# 애플리케이션 파일 복사
COPY . .

# 빌드 실행
RUN npm run build

# 애플리케이션 서버 실행
CMD ["npx", "serve", "-s", "build", "-l", "3000"]