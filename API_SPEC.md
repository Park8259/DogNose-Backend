# Dog Nose Backend API Spec

비문인식 기반 반려견 분양 플랫폼의 Flutter 연동용 API 명세입니다.

## 1. Base URL

로컬 개발:

```text
http://localhost:8080
```

Flutter 실행 환경별 주의:

```text
Android Emulator: http://10.0.2.2:8080
iOS Simulator: http://localhost:8080
실제 휴대폰: http://{개발자_컴퓨터_IP}:8080
배포 후: http://{서버_IP_또는_도메인}
```

## 2. 공통 응답 형식

성공:

```json
{
  "success": true,
  "data": {},
  "message": null
}
```

실패:

```json
{
  "success": false,
  "data": null,
  "message": "오류 메시지"
}
```

## 3. 인증 방식

로그인/회원가입 응답의 `accessToken`을 저장한 뒤, 로그인 이후 API 호출마다 아래 헤더를 넣습니다.

```http
Authorization: Bearer {accessToken}
```

## 4. 상태값 Enum

### UserRole

```text
USER, SELLER, ADMIN
```

### DogGender

```text
MALE, FEMALE, UNKNOWN
```

### DogStatus

```text
ACTIVE, ADOPTED, HIDDEN, DELETED
```

### NosePrintStatus

```text
PENDING, VALID, REJECTED
```

### VerificationType

```text
REGISTER, PRE_CONTRACT, DELIVERY, MANUAL
```

### VerificationResult

```text
MATCH, NON_MATCH, UNCERTAIN, FAILED
```

### AdoptionPostStatus

```text
DRAFT, PENDING_REVIEW, OPEN, RESERVED, CONTRACTED, DELIVERED, CLOSED, REJECTED, CANCELLED
```

### ApplicationStatus

```text
PENDING, ACCEPTED, REJECTED, CANCELLED
```

### ReservationStatus

```text
REQUESTED, CONFIRMED, CANCELLED, COMPLETED
```

### HealthRecordType

```text
CHECKUP, DISEASE, SURGERY, MEDICATION, ETC
```

### ReportTargetType

```text
POST, USER, APPLICATION, RESERVATION, DOG
```

### ReportStatus

```text
OPEN, IN_REVIEW, RESOLVED, REJECTED
```

## 5. Auth API

### 5.1 회원가입

```http
POST /api/auth/signup
Content-Type: application/json
```

Request:

```json
{
  "email": "seller@example.com",
  "password": "password1234",
  "name": "분양자",
  "phone": "010-1111-2222",
  "role": "SELLER"
}
```

Response `data`:

```json
{
  "accessToken": "jwt-token",
  "tokenType": "Bearer",
  "user": {
    "id": 1,
    "email": "seller@example.com",
    "name": "분양자",
    "phone": "010-1111-2222",
    "role": "SELLER",
    "status": "ACTIVE"
  }
}
```

### 5.2 로그인

```http
POST /api/auth/login
Content-Type: application/json
```

Request:

```json
{
  "email": "seller@example.com",
  "password": "password1234"
}
```

Response는 회원가입과 동일합니다.

### 5.3 내 정보 조회

```http
GET /api/users/me
Authorization: Bearer {accessToken}
```

## 6. Dog API

### 6.1 반려견 등록

```http
POST /api/dogs
Authorization: Bearer {accessToken}
Content-Type: application/json
```

Request:

```json
{
  "name": "초코",
  "breed": "푸들",
  "gender": "MALE",
  "birthDate": "2024-03-15",
  "description": "사람을 좋아하고 활발한 강아지입니다.",
  "profileImageUrl": "/uploads/profile.jpg"
}
```

Response `data`:

```json
{
  "id": 1,
  "ownerId": 1,
  "name": "초코",
  "breed": "푸들",
  "gender": "MALE",
  "birthDate": "2024-03-15",
  "description": "사람을 좋아하고 활발한 강아지입니다.",
  "profileImageUrl": "/uploads/profile.jpg",
  "noseRegistered": false,
  "status": "ACTIVE",
  "createdAt": "2026-05-05T10:00:00",
  "updatedAt": "2026-05-05T10:00:00"
}
```

### 6.2 내 반려견 목록

```http
GET /api/dogs/me
Authorization: Bearer {accessToken}
```

### 6.3 반려견 상세

```http
GET /api/dogs/{dogId}
Authorization: Bearer {accessToken}
```

## 7. File API

### 7.1 이미지 업로드

```http
POST /api/files/images
Authorization: Bearer {accessToken}
Content-Type: multipart/form-data
```

Form data:

```text
file: jpg/png/webp 이미지 파일
```

Response `data`:

```json
{
  "originalFilename": "choco-nose.jpg",
  "storedFilename": "uuid.jpg",
  "contentType": "image/jpeg",
  "size": 12345,
  "url": "/uploads/uuid.jpg"
}
```

이미지 표시 URL:

```text
{baseUrl}/uploads/uuid.jpg
```

## 8. Nose Print API

### 8.1 기준 비문 등록

```http
POST /api/dogs/{dogId}/nose-prints
Authorization: Bearer {accessToken}
Content-Type: application/json
```

Request:

```json
{
  "imageUrl": "/uploads/nose.jpg",
  "qualityScore": 87.5,
  "vectorPointId": "nose-dog-1-reference",
  "embeddingModel": "mock-s101_224",
  "reference": true
}
```

성공 시 해당 강아지의 `noseRegistered`가 `true`로 변경됩니다.

### 8.2 비문 목록

```http
GET /api/dogs/{dogId}/nose-prints
Authorization: Bearer {accessToken}
```

## 9. Verification API

### 9.1 비문 1:1 검증

```http
POST /api/dogs/{dogId}/verify
Authorization: Bearer {accessToken}
Content-Type: application/json
```

Request:

```json
{
  "probeImageUrl": "/uploads/probe.jpg",
  "postId": 1,
  "verificationType": "DELIVERY",
  "threshold": 0.75
}
```

`postId`는 선택값입니다.

Response `data`:

```json
{
  "id": 1,
  "dogId": 1,
  "postId": 1,
  "requestedBy": 1,
  "referenceNosePrintId": 1,
  "referenceImageUrl": "/uploads/reference.jpg",
  "probeImageUrl": "/uploads/probe.jpg",
  "verificationType": "DELIVERY",
  "cosineSimilarity": 0.95,
  "euclideanDistance": 0.316228,
  "threshold": 0.75,
  "result": "MATCH",
  "modelName": "mock-s101_224",
  "createdAt": "2026-05-05T10:00:00"
}
```

### 9.2 검증 로그 조회

```http
GET /api/dogs/{dogId}/verification-logs
Authorization: Bearer {accessToken}
```

## 10. Adoption Post API

### 10.1 분양글 등록

기준 비문이 등록된 반려견만 분양글을 등록할 수 있습니다.

```http
POST /api/adoption-posts
Authorization: Bearer {sellerAccessToken}
Content-Type: application/json
```

Request:

```json
{
  "dogId": 1,
  "title": "비문 인증 완료된 푸들 초코 분양합니다",
  "content": "사람을 좋아하고 건강한 아이입니다.",
  "price": 300000,
  "region": "서울",
  "adoptionReason": "개인 사정으로 좋은 가족을 찾고 있습니다.",
  "contractTerms": "인도 시점에 비문 재검증 후 분양을 진행합니다."
}
```

### 10.2 분양글 목록

```http
GET /api/adoption-posts
Authorization: Bearer {accessToken}
```

지역 검색:

```http
GET /api/adoption-posts?region=서울
Authorization: Bearer {accessToken}
```

### 10.3 분양글 상세

```http
GET /api/adoption-posts/{postId}
Authorization: Bearer {accessToken}
```

## 10-1. Health API

### 10-1.1 건강기록 등록

반려견 소유자만 등록할 수 있습니다.

```http
POST /api/dogs/{dogId}/health-records
Authorization: Bearer {sellerAccessToken}
Content-Type: application/json
```

Request:

```json
{
  "recordType": "CHECKUP",
  "title": "기본 건강검진",
  "description": "특이사항 없음",
  "recordDate": "2026-05-01",
  "attachmentUrl": "/uploads/health-check.jpg"
}
```

### 10-1.2 건강기록 목록

```http
GET /api/dogs/{dogId}/health-records
Authorization: Bearer {accessToken}
```

### 10-1.3 접종기록 등록

반려견 소유자만 등록할 수 있습니다.

```http
POST /api/dogs/{dogId}/vaccinations
Authorization: Bearer {sellerAccessToken}
Content-Type: application/json
```

Request:

```json
{
  "vaccineName": "종합백신",
  "vaccinatedAt": "2026-04-20",
  "nextDueAt": "2027-04-20",
  "hospitalName": "서울동물병원",
  "attachmentUrl": "/uploads/vaccination.jpg"
}
```

### 10-1.4 접종기록 목록

```http
GET /api/dogs/{dogId}/vaccinations
Authorization: Bearer {accessToken}
```

## 11. Adoption Application API

### 11.1 입양 신청

분양글 작성자는 본인 글에 신청할 수 없습니다.

```http
POST /api/adoption-posts/{postId}/applications
Authorization: Bearer {buyerAccessToken}
Content-Type: application/json
```

Request:

```json
{
  "message": "초코를 책임감 있게 돌보고 싶습니다. 방문 상담 가능할까요?"
}
```

Response `data`:

```json
{
  "id": 1,
  "postId": 1,
  "postTitle": "비문 인증 완료된 푸들 초코 분양합니다",
  "applicantId": 2,
  "applicantName": "입양희망자",
  "message": "초코를 책임감 있게 돌보고 싶습니다. 방문 상담 가능할까요?",
  "status": "PENDING",
  "createdAt": "2026-05-05T10:00:00"
}
```

### 11.2 내 신청 목록

```http
GET /api/applications/me
Authorization: Bearer {buyerAccessToken}
```

### 11.3 분양글 신청 목록

분양글 작성자만 조회할 수 있습니다.

```http
GET /api/adoption-posts/{postId}/applications
Authorization: Bearer {sellerAccessToken}
```

### 11.4 신청 승인

```http
PATCH /api/applications/{applicationId}/accept
Authorization: Bearer {sellerAccessToken}
```

### 11.5 신청 거절

```http
PATCH /api/applications/{applicationId}/reject
Authorization: Bearer {sellerAccessToken}
```

## 12. Reservation API

### 12.1 예약 생성

승인된 입양 신청만 예약을 만들 수 있습니다.

```http
POST /api/reservations
Authorization: Bearer {buyerOrSellerAccessToken}
Content-Type: application/json
```

Request:

```json
{
  "applicationId": 1,
  "reservedAt": "2026-06-01T14:00:00",
  "place": "서울시 강남구 반려견 카페 앞"
}
```

Response `data.status`:

```text
REQUESTED
```

### 12.2 내 예약 목록

```http
GET /api/reservations/me
Authorization: Bearer {buyerOrSellerAccessToken}
```

### 12.3 예약 확정

분양자만 처리할 수 있습니다.

```http
PATCH /api/reservations/{reservationId}/confirm
Authorization: Bearer {sellerAccessToken}
```

### 12.4 예약 취소

분양자 또는 신청자가 처리할 수 있습니다.

```http
PATCH /api/reservations/{reservationId}/cancel
Authorization: Bearer {buyerOrSellerAccessToken}
```

### 12.5 예약 완료

단순 완료 API입니다. 서비스 핵심 시나리오에서는 아래 `delivery/complete` 사용을 권장합니다.

```http
PATCH /api/reservations/{reservationId}/complete
Authorization: Bearer {sellerAccessToken}
```

## 13. Delivery API

### 13.1 인도 시점 비문 재검증 및 완료

확정된 예약만 처리할 수 있습니다. 비문 검증 결과가 `MATCH`일 때만 완료됩니다.

```http
POST /api/reservations/{reservationId}/delivery/complete
Authorization: Bearer {sellerAccessToken}
Content-Type: application/json
```

Request:

```json
{
  "probeImageUrl": "/uploads/delivery-nose.jpg",
  "threshold": 0.75
}
```

성공 시 상태 변화:

```text
reservation.status = COMPLETED
adoptionPost.status = DELIVERED
dog.status = ADOPTED
```

## 14. 프론트 연결 권장 시나리오

## 14. Report API

### 14.1 신고 등록

```http
POST /api/reports
Authorization: Bearer {accessToken}
Content-Type: application/json
```

Request:

```json
{
  "targetType": "POST",
  "targetId": 1,
  "reason": "허위 정보 의심",
  "description": "게시글의 건강 정보가 실제 설명과 다릅니다."
}
```

### 14.2 내 신고 목록

```http
GET /api/reports/me
Authorization: Bearer {accessToken}
```

### 14.3 관리자 신고 목록

`ADMIN` 역할만 접근할 수 있습니다.

```http
GET /api/admin/reports
Authorization: Bearer {adminAccessToken}
```

상태 필터:

```http
GET /api/admin/reports?status=OPEN
Authorization: Bearer {adminAccessToken}
```

### 14.4 관리자 신고 처리

`ADMIN` 역할만 접근할 수 있습니다.

```http
PATCH /api/admin/reports/{reportId}
Authorization: Bearer {adminAccessToken}
Content-Type: application/json
```

Request:

```json
{
  "status": "RESOLVED",
  "adminMemo": "게시글 내용을 확인하고 분양자에게 수정 안내 완료"
}
```

### 14.5 관리자 분양글 전체 조회

`ADMIN` 역할만 접근할 수 있습니다.

```http
GET /api/admin/adoption-posts
Authorization: Bearer {adminAccessToken}
```

상태 필터:

```http
GET /api/admin/adoption-posts?status=OPEN
Authorization: Bearer {adminAccessToken}
```

### 14.6 관리자 분양글 상태 변경

`ADMIN` 역할만 접근할 수 있습니다.

```http
PATCH /api/admin/adoption-posts/{postId}/status
Authorization: Bearer {adminAccessToken}
Content-Type: application/json
```

Request:

```json
{
  "status": "CLOSED"
}
```

### 14.7 관리자 검증 로그 전체 조회

`ADMIN` 역할만 접근할 수 있습니다.

```http
GET /api/admin/verification-logs
Authorization: Bearer {adminAccessToken}
```

결과 필터:

```http
GET /api/admin/verification-logs?result=MATCH
Authorization: Bearer {adminAccessToken}
```

## 15. 프론트 연결 권장 시나리오

### 분양자 플로우

```text
회원가입/로그인
-> 이미지 업로드
-> 반려견 등록
-> 건강기록/접종기록 등록
-> 비문 이미지 업로드
-> 기준 비문 등록
-> 분양글 등록
-> 신청 목록 조회
-> 신청 승인
-> 예약 확정
-> 인도 시점 비문 이미지 업로드
-> delivery/complete 호출
```

### 입양 희망자 플로우

```text
회원가입/로그인
-> 분양글 목록 조회
-> 분양글 상세 조회
-> 입양 신청
-> 내 신청 목록 조회
-> 예약 생성 또는 예약 목록 조회
```

## 16. AI 서버 계약

Spring Boot는 기본적으로 mock matcher를 사용합니다.

AI 서버가 준비되면 아래 설정으로 전환합니다.

```text
AI_ENABLED=true
AI_BASE_URL=http://localhost:8000
```

AI 서버는 아래 API를 제공해야 합니다.

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

## 17. Qdrant 벡터 DB 계약

Qdrant는 Flutter 앱이 직접 호출하지 않습니다. Spring Boot 또는 AI 서버 내부에서만 사용합니다.

컬렉션:

```text
dog_nose_embeddings
```

Vector 설정:

```json
{
  "size": 2048,
  "distance": "Cosine"
}
```

Point payload 권장 구조:

```json
{
  "dogId": 1,
  "nosePrintId": 1,
  "imageUrl": "/uploads/reference-nose.jpg",
  "ownerId": 1,
  "embeddingModel": "s101_224",
  "reference": true
}
```

예상 연결 흐름:

```text
비문 등록:
Spring Boot 이미지 업로드
-> AI 서버 embedding 추출
-> Qdrant에 vector + payload 저장
-> Spring Boot nosePrint.vectorPointId 저장

비문 인증:
Spring Boot 인증 요청
-> AI 서버 probe image embedding 추출
-> Qdrant 유사도 검색
-> AI 서버가 MATCH/NON_MATCH와 similarity 반환
-> Spring Boot verification_logs 저장
```
