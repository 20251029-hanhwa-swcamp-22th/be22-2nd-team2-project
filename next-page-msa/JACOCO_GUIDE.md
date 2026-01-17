# Jacoco 코드 커버리지 리포트 가이드

## ✅ Jacoco 설정 완료

Next-Page-MSA 프로젝트에 Jacoco 코드 커버리지 도구가 성공적으로 설정되었습니다.

---

## 🚀 빠른 시작

### Member Service 테스트 및 리포트 생성 (현재 작동)

```bash
# Windows
cd c:\Users\JinhoLap\Documents\team2\next-page-msa
.\gradlew :member-service:clean :member-service:test :member-service:jacocoTestReport

# 리포트 열기
start member-service\build\reports\jacoco\test\html\index.html
```

---

## 📊 Jacoco 설정 내용

### 1. 루트 build.gradle에 추가된 내용

```gradle
plugins {
    id 'jacoco'  // Jacoco 플러그인 추가
}

subprojects {
    apply plugin: 'jacoco'

    jacoco {
        toolVersion = "0.8.12"
    }

    tasks.withType(Test) {
        useJUnitPlatform()
        finalizedBy jacocoTestReport  // 테스트 후 자동 리포트 생성
    }

    jacocoTestReport {
        reports {
            xml.required = true
            html.required = true
            csv.required = false
        }

        // 제외 대상 (DTO, Entity, Config 등)
        afterEvaluate {
            classDirectories.setFrom(files(classDirectories.files.collect {
                fileTree(dir: it, exclude: [
                    '**/config/**',
                    '**/dto/**',
                    '**/entity/**',
                    '**/*Application.class',
                    '**/websocket/dto/**',
                    '**/feign/**'
                ])
            }))
        }
    }

    jacocoTestCoverageVerification {
        violationRules {
            rule {
                limit {
                    counter = 'LINE'
                    value = 'COVEREDRATIO'
                    minimum = 0.70  // 70% 커버리지 요구
                }
            }
        }
    }
}
```

### 2. 통합 리포트 생성 태스크

```gradle
task jacocoRootReport(type: JacocoReport) {
    description = 'Generates an aggregate report from all subprojects'
    dependsOn subprojects*.test

    // HTML: build/reports/jacoco/aggregate/index.html
    // XML: build/reports/jacoco/aggregate/jacocoTestReport.xml
}
```

---

## 📁 리포트 위치

### 개별 서비스 리포트

```
member-service/build/reports/jacoco/test/html/index.html
story-service/build/reports/jacoco/test/html/index.html
reaction-service/build/reports/jacoco/test/html/index.html
```

### 통합 리포트

```
build/reports/jacoco/aggregate/index.html
```

---

## 🎯 커버리지 목표

| 항목 | 목표 |
|------|------|
| Line Coverage | 70% 이상 |
| Branch Coverage | 70% 이상 |

---

## 📝 제외 대상

테스트 커버리지에서 제외되는 클래스:

- `**/config/**` - 설정 클래스
- `**/dto/**` - DTO 클래스
- `**/entity/**` - 엔티티 클래스
- `**/*Application.class` - 메인 애플리케이션 클래스
- `**/websocket/dto/**` - WebSocket DTO
- `**/feign/**` - Feign Client 인터페이스

---

## 🔧 명령어 모음

### 테스트 실행 + 리포트 생성

```bash
# Member Service
.\gradlew :member-service:test :member-service:jacocoTestReport

# Story Service (일부 수정 필요)
.\gradlew :story-service:test :story-service:jacocoTestReport

# Reaction Service (일부 수정 필요)
.\gradlew :reaction-service:test :reaction-service:jacocoTestReport
```

### 커버리지 검증

```bash
# 70% 커버리지 체크
.\gradlew :member-service:jacocoTestCoverageVerification
```

### 통합 리포트 (모든 서비스)

```bash
.\gradlew jacocoRootReport
```

### 리포트 열기 (Windows)

```bash
# Member Service
start member-service\build\reports\jacoco\test\html\index.html

# 통합 리포트
start build\reports\jacoco\aggregate\index.html
```

---

## 📈 리포트 보는 법

### HTML 리포트 구성

1. **Overall Coverage** - 전체 커버리지 요약
   - Missed Instructions (미실행 라인)
   - Cov. (커버리지 비율)
   - Missed Branches (미실행 분기)

2. **Package 별 커버리지** - 패키지 단위 통계

3. **Class 별 커버리지** - 클래스 단위 통계

4. **소스 코드 뷰**
   - 🟢 녹색 하이라이트: 테스트된 코드
   - 🔴 빨간색 하이라이트: 테스트되지 않은 코드
   - 🟡 노란색 하이라이트: 부분적으로 테스트된 분기

---

## ✅ Member Service 테스트 현황

### 작성된 테스트 (30개)

#### AuthServiceTest (13개)
- ✅ 로그인 성공/실패 시나리오
- ✅ 토큰 갱신 (Refresh Token) 검증
- ✅ 로그아웃 처리
- ✅ 관리자 승인 대기 상태 처리

#### MemberServiceTest (14개)
- ✅ 일반 사용자/관리자 등록
- ✅ 이메일/닉네임 중복 검증
- ✅ 관리자 승인 프로세스
- ✅ 회원 탈퇴 (Soft Delete)

### 예상 커버리지

Member Service는 서비스 레이어 기준 약 **80-90%** 커버리지가 예상됩니다.

---

## 🔨 Story Service & Reaction Service 수정 필요 사항

### Story Service
- DTO 클래스에 `@Builder` 추가 완료
- 일부 테스트 케이스 수정 필요 (WebSocket, 문장 삭제 테스트)

### Reaction Service
- Entity 클래스의 필드명 불일치 수정 필요
  - `commentId`, `voteId` 등의 빌더 메서드 확인 필요

---

## 📚 참고 자료

- [Jacoco 공식 문서](https://www.jacoco.org/jacoco/trunk/doc/)
- [Gradle Jacoco Plugin](https://docs.gradle.org/current/userguide/jacoco_plugin.html)
- [TESTING.md](./TESTING.md) - 전체 테스트 가이드

---

## 💡 팁

### 1. 빠른 피드백을 위한 테스트 실행

```bash
# 변경된 테스트만 실행
.\gradlew :member-service:test --rerun-tasks

# 특정 테스트만 실행
.\gradlew :member-service:test --tests "AuthServiceTest"
```

### 2. CI/CD 통합

```yaml
# GitHub Actions 예시
- name: Run tests with coverage
  run: ./gradlew test jacocoTestReport

- name: Upload coverage to Codecov
  uses: codecov/codecov-action@v3
  with:
    files: build/reports/jacoco/aggregate/jacocoTestReport.xml
```

### 3. 커버리지 향상 전략

1. **Service 레이어 우선** - 비즈니스 로직이 집중된 곳
2. **예외 케이스 추가** - 에러 핸들링 로직 테스트
3. **Edge Case 커버** - 경계값 테스트
4. **Integration Test** - Controller 레이어 통합 테스트

---

**Last Updated:** 2026-01-17
**Version:** 1.0.0
