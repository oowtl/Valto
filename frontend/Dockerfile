# vue.js 프론트 빌드만을 위해 node 이미지를 사용하여 작업하는 과정
### vue.js 빌드만을 위한 node 이미지를 사용
### 작업 디렉토리를 /hompage로 설정한 후,
### 패키지 설치를 위해 package.json을 작업 디렉토리로 옮긴 후 설치
### 나머지 프론트 소스코드와 리소스를 복사하여 빌드 진행
FROM node:lts-alpine as build-stage
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build