# Dog Nose Backend

Spring Boot 기반 비문인식 반려견 분양 플랫폼 백엔드 초안입니다.

## 실행 방법

IntelliJ에서 `dog-nose-backend` 폴더를 열고 Maven import가 끝난 뒤 `DogNoseBackendApplication`을 실행합니다.

터미널로 실행할 경우:

```bash
mvn spring-boot:run
```

기본 서버 주소:

```text
http://localhost:8080
```

기본 프로필은 `h2`입니다. 별도 설정 없이 실행하면 H2 파일 DB를 사용합니다.

## 현재 구현된 API

### 회원가입

```http
POST /api/auth/signup
Content-Type: application/json
```

```json
{
  "email": "seller@example.com",
  "password": "password1234",
  "name": "박성근",
  "phone": "010-1234-5678",
  "role": "SELLER"
}
```

### 로그인

```http
POST /api/auth/login
Content-Type: application/json
```

```json
{
  "email": "seller@example.com",
  "password": "password1234"
}
```

응답의 `accessToken`을 복사해서 이후 요청에 넣습니다.

```http
Authorization: Bearer {accessToken}
```

### 내 정보 조회

```http
GET /api/users/me
Authorization: Bearer {accessToken}
```

### 반려견 등록

```http
POST /api/dogs
Authorization: Bearer {accessToken}
Content-Type: application/json
```

```json
{
  "name": "초코",
  "breed": "푸들",
  "gender": "MALE",
  "birthDate": "2024-03-15",
  "description": "사람을 좋아하고 활발한 강아지입니다.",
  "profileImageUrl": "https://example.com/dogs/choco.jpg"
}
```

### 내 반려견 목록

```http
GET /api/dogs/me
Authorization: Bearer {accessToken}
```

### 반려견 상세 조회

```http
GET /api/dogs/{dogId}
Authorization: Bearer {accessToken}
```

### 비문 이미지 등록

현재 단계에서는 실제 파일 업로드와 AI 추론 대신 이미지 URL과 mock 품질 점수를 저장합니다.

```http
POST /api/dogs/{dogId}/nose-prints
Authorization: Bearer {accessToken}
Content-Type: application/json
```

```json
{
  "imageUrl": "https://example.com/nose/choco-nose.jpg",
  "qualityScore": 87.50,
  "vectorPointId": "nose-dog-1-reference",
  "embeddingModel": "mock-s101_224",
  "reference": true
}
```

등록 성공 시 `dogs.nose_registered`가 `true`로 변경됩니다.

### 반려견 비문 목록 조회

```http
GET /api/dogs/{dogId}/nose-prints
Authorization: Bearer {accessToken}
```

### 이미지 업로드

로컬 개발 단계에서는 업로드한 파일이 프로젝트의 `uploads` 폴더에 저장되고, 응답의 `url` 값을 이미지 URL로 사용합니다.

```http
POST /api/files/images
Authorization: Bearer {accessToken}
Content-Type: multipart/form-data
```

form-data:

```text
file: 업로드할 jpg/png/webp 이미지
```

응답 예시:

```json
{
  "success": true,
  "data": {
    "originalFilename": "choco-nose.jpg",
    "storedFilename": "uuid.jpg",
    "contentType": "image/jpeg",
    "size": 12345,
    "url": "/uploads/uuid.jpg"
  },
  "message": null
}
```

응답의 `url`은 브라우저에서 `http://localhost:8080/uploads/uuid.jpg`로 확인할 수 있습니다.

### 비문 1:1 검증

현재 단계에서는 실제 AI 모델 대신 mock matcher가 cosine similarity를 생성하고, 결과를 `verification_logs`에 저장합니다. 나중에 Python AI 서버가 준비되면 matcher 부분만 실제 AI 호출로 교체합니다.

```http
POST /api/dogs/{dogId}/verify
Authorization: Bearer {accessToken}
Content-Type: application/json
```

```json
{
  "probeImageUrl": "/uploads/uuid.jpg",
  "verificationType": "DELIVERY",
  "threshold": 0.75
}
```

응답 예시:

```json
{
  "success": true,
  "data": {
    "id": 1,
    "dogId": 1,
    "referenceNosePrintId": 1,
    "referenceImageUrl": "/uploads/reference.jpg",
    "probeImageUrl": "/uploads/probe.jpg",
    "verificationType": "DELIVERY",
    "cosineSimilarity": 0.950000,
    "euclideanDistance": 0.316228,
    "threshold": 0.750000,
    "result": "MATCH",
    "modelName": "mock-s101_224"
  },
  "message": null
}
```

### 비문 검증 로그 조회

```http
GET /api/dogs/{dogId}/verification-logs
Authorization: Bearer {accessToken}
```

### 분양글 등록

기준 비문이 등록된 반려견만 분양글을 등록할 수 있습니다.

```http
POST /api/adoption-posts
Authorization: Bearer {accessToken}
Content-Type: application/json
```

```json
{
  "dogId": 1,
  "title": "비문 인증 완료된 푸들 초코 분양합니다",
  "content": "사람을 좋아하고 건강한 아이입니다. 기준 비문 등록과 검증 로그가 있습니다.",
  "price": 300000,
  "region": "서울",
  "adoptionReason": "개인 사정으로 좋은 가족을 찾고 있습니다.",
  "contractTerms": "인도 시점에 비문 재검증 후 분양을 진행합니다."
}
```

### 분양글 목록/검색

```http
GET /api/adoption-posts
Authorization: Bearer {accessToken}
```

지역 검색:

```http
GET /api/adoption-posts?region=서울
Authorization: Bearer {accessToken}
```

### 분양글 상세 조회

```http
GET /api/adoption-posts/{postId}
Authorization: Bearer {accessToken}
```

### 입양 신청

분양글 작성자는 본인 글에 신청할 수 없습니다. 신청자 계정으로 로그인한 뒤 호출합니다.

```http
POST /api/adoption-posts/{postId}/applications
Authorization: Bearer {buyerAccessToken}
Content-Type: application/json
```

```json
{
  "message": "초코를 책임감 있게 돌보고 싶습니다. 방문 상담 가능할까요?"
}
```

### 내 입양 신청 목록

```http
GET /api/applications/me
Authorization: Bearer {buyerAccessToken}
```

### 분양글 신청 목록

분양글 작성자만 조회할 수 있습니다.

```http
GET /api/adoption-posts/{postId}/applications
Authorization: Bearer {sellerAccessToken}
```

### 입양 신청 승인/거절

분양글 작성자만 처리할 수 있습니다.

```http
PATCH /api/applications/{applicationId}/accept
Authorization: Bearer {sellerAccessToken}
```

```http
PATCH /api/applications/{applicationId}/reject
Authorization: Bearer {sellerAccessToken}
```

### 예약 생성

승인된 입양 신청만 예약을 만들 수 있습니다. 분양자 또는 신청자만 생성할 수 있습니다.

```http
POST /api/reservations
Authorization: Bearer {buyerOrSellerAccessToken}
Content-Type: application/json
```

```json
{
  "applicationId": 1,
  "reservedAt": "2026-06-01T14:00:00",
  "place": "서울시 강남구 반려견 카페 앞"
}
```

### 내 예약 목록

```http
GET /api/reservations/me
Authorization: Bearer {buyerOrSellerAccessToken}
```

### 예약 확정/취소/완료

확정과 완료는 분양자만 처리할 수 있고, 취소는 분양자 또는 신청자가 처리할 수 있습니다.

```http
PATCH /api/reservations/{reservationId}/confirm
Authorization: Bearer {sellerAccessToken}
```

```http
PATCH /api/reservations/{reservationId}/cancel
Authorization: Bearer {buyerOrSellerAccessToken}
```

```http
PATCH /api/reservations/{reservationId}/complete
Authorization: Bearer {sellerAccessToken}
```

### 인도 시점 비문 재검증 및 완료 처리

확정된 예약만 인도 완료 처리할 수 있습니다. 분양자가 현장에서 재촬영한 비문 이미지 URL을 보내면, 서버가 `DELIVERY` 검증 로그를 남기고 `MATCH`일 때만 예약/분양글/반려견 상태를 완료 처리합니다.

완료 시 상태 변화:

```text
reservation.status = COMPLETED
adoptionPost.status = DELIVERED
dog.status = ADOPTED
```

```http
POST /api/reservations/{reservationId}/delivery/complete
Authorization: Bearer {sellerAccessToken}
Content-Type: application/json
```

```json
{
  "probeImageUrl": "/uploads/uuid.jpg",
  "threshold": 0.75
}
```

## AI 서버 연결

현재 기본 설정은 mock matcher입니다.

```yaml
app:
  ai:
    enabled: false
    base-url: http://localhost:8000
```

Python AI 서버가 준비되면 환경변수 또는 `application.yml`에서 아래처럼 바꿉니다.

```text
AI_ENABLED=true
AI_BASE_URL=http://localhost:8000
```

Spring Boot는 다음 AI API를 호출합니다.

```http
POST /ai/nose-prints/verify
Content-Type: application/json
```

Request:

```json
{
  "referenceImageUrl": "/uploads/reference.jpg",
  "probeImageUrl": "/uploads/probe.jpg",
  "threshold": 0.75
}
```

Response:

```json
{
  "result": "MATCH",
  "cosineSimilarity": 0.842113,
  "euclideanDistance": 0.561302,
  "threshold": 0.75,
  "modelName": "s101_224"
}
```

`result` 값은 아래 중 하나여야 합니다.

```text
MATCH, NON_MATCH, UNCERTAIN, FAILED
```

AI 서버가 이 계약만 맞춰주면 Spring Boot의 `verification_logs` 저장과 인도 완료 처리는 그대로 동작합니다.

## 로컬 DB - H2

처음에는 설치가 쉬운 H2 파일 DB를 사용합니다. DB 파일은 프로젝트의 `data` 폴더에 저장되므로 서버를 재시작해도 데이터가 유지됩니다.

H2 Console:

```text
http://localhost:8080/h2-console
```

접속 정보:

```text
JDBC URL: jdbc:h2:file:./data/dognose
User Name: sa
Password:
```

나중에 MySQL로 바꿀 때는 datasource 설정을 직접 수정하지 않고 `mysql` 프로필로 실행하면 됩니다.

## MySQL 실행

Docker가 설치되어 있으면 MySQL을 아래 명령으로 실행합니다.

```bash
docker compose up -d mysql
```

MySQL 접속 정보:

```text
Host: localhost
Port: 3306
Database: dognose
User: dognose
Password: dognose1234
Root Password: root1234
```

MySQL 프로필로 Spring Boot를 실행하려면 환경변수를 지정합니다.

터미널:

```bash
SPRING_PROFILES_ACTIVE=mysql mvn spring-boot:run
```

IntelliJ:

```text
Run Configuration
-> Environment variables
-> SPRING_PROFILES_ACTIVE=mysql
```

필요하면 DB 접속 정보를 환경변수로 바꿀 수 있습니다.

```text
MYSQL_URL=jdbc:mysql://localhost:3306/dognose?serverTimezone=Asia/Seoul&characterEncoding=UTF-8
MYSQL_USER=dognose
MYSQL_PASSWORD=dognose1234
```

평소 개발은 H2로 하고, 팀 통합/배포 준비 때 MySQL 프로필을 사용하면 됩니다.
