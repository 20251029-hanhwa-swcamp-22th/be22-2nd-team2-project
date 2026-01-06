# be22-2nd-team2-project

# 📚 Next Page : 우리가 함께 만드는 이야기
> **"당신의 한 문장이 베스트셀러의 시작이 됩니다."**  
> 누구나 작가가 되어 릴레이로 소설을 완성하는 집단 지성 창작 플랫폼

<br>

## 1. 📝 프로젝트 소개
**Next Page**는 한 사람이 모든 이야기를 쓰는 것이 아니라, 여러 사용자가 **문장 단위로 이어 쓰며 하나의 소설을 완성**하는 릴레이 창작 서비스입니다.

단순한 게시판이 아닙니다. **순서(Sequence)와 상태(Status) 관리**가 핵심인 이 프로젝트는, 앞사람이 글을 완료해야만 뒷사람이 쓸 수 있는 **동시성 제어**와 **도메인 규칙**을 엄격하게 준수합니다.

### 📅 개발 기간
* **2025.XX.XX ~ 2025.XX.XX (N주)**

<br>

## 2. 👥 팀원 및 역할 분담 (Team Next Page)
우리는 **도메인 주도 설계(DDD)** 원칙에 따라, 기능 단위가 아닌 **도메인(Context)** 단위로 역할을 분담하여 전문성을 높였습니다.

| 이름 | 포지션 | 담당 도메인 & 핵심 역할 |
|:---:|:---:|:---|
| **정진호** | **Team Leader** | **🏛 Core & Architecture**<br>- 프로젝트 아키텍처 설계 (CQRS 패턴 적용)<br>- `Story` 애그리거트 상태 관리 및 순서 제어 로직<br>- 전역 예외 처리 및 공통 응답 포맷 정의 |
| **김태형** | **Sub Leader** | **🔐 Member & Auth**<br>- Spring Security 기반 인증/인가 (회원가입, 로그인)<br>- JWT/Session 관리 및 사용자 권한(Writer/Admin) 제어<br>- 마이페이지 (내 서재, 프로필 관리) |
| **최현지** | **Core Dev** | **✍️ Writing (Command)**<br>- 문장 작성(Append) 비즈니스 로직 구현<br>- 연속 작성 방지 및 입력 데이터 유효성 검사(Validation)<br>- 이야기 완결(State Transition) 처리 |
| **윤성원** | **Core Dev** | **📖 Reading (Query)**<br>- 소설 조회 및 검색 최적화 (Read Model 설계)<br>- 장르별/인기별 필터링 및 페이징 처리<br>- 완성된 소설 '책 뷰' 렌더링 API |
| **정병진** | **Developer** | **❤️ Reaction & Support**<br>- 문장별 이모지 반응(좋아요/슬퍼요) 기능<br>- 관심 소설 북마크 및 좋아요 기능<br>- UI/UX 인터랙션 요소 개발 지원 |

<br>

## 3. 🛠️ 기술 스택 (Tech Stack)

### Backend
![Java](https://img.shields.io/badge/Java-17-ED8B00?style=flat-square&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-6DB33F?style=flat-square&logo=springboot&logoColor=white)
![JPA](https://img.shields.io/badge/JPA-Hibernate-59666C?style=flat-square&logo=hibernate&logoColor=white)
![MariaDB](https://img.shields.io/badge/MariaDB-10.6-003545?style=flat-square&logo=mariadb&logoColor=white)

### Architecture Strategy
*   **RESTful API:** 자원(Resource) 중심의 명확한 URI 설계 및 HTTP Method 활용
*   **DDD (Domain-Driven Design):** 비즈니스 로직을 Service가 아닌 **Entity(도메인 객체)에 위임**하여 응집도 강화
*   **CQRS (Command Query Responsibility Segregation):**
    *   **Command (쓰기):** 데이터 무결성이 중요한 로직 (JPA Dirty Checking 활용)
    *   **Query (읽기):** 조회 성능 최적화를 위한 별도 DTO 조회 로직 분리

<br>

## 4. ✨ 주요 기능 (Key Features)

### 🚀 1. 릴레이 소설 창작 (Core)
*   **이야기 시작:** 제목, 장르, 첫 문장을 등록하여 방을 개설합니다.
*   **이어 쓰기 (Sequence Control):**
    *   현재 순서(`current_sequence`)인 경우에만 작성 권한이 부여됩니다.
    *   **연속 작성 금지:** 바로 앞 문장을 쓴 사람은 연달아 쓸 수 없습니다.
*   **자동 완결:** 설정된 최대 문장 수에 도달하면 이야기는 `COMPLETED` 상태로 잠깁니다.

### 📚 2. 완결 소설 서재 (Query)
*   **명예의 전당:** 완결된 소설은 별도의 '서재' 공간에 전시됩니다.
*   **뷰어 모드:** 문장 단위가 아닌, 한 권의 책을 읽는 듯한 매끄러운 뷰를 제공합니다.

### 🔐 3. 회원 서비스
*   **작가 프로필:** 필명(Nickname)을 사용하여 익명성을 보장하되, 개성을 표현합니다.
*   **활동 내역:** 내가 만든 이야기, 내가 참여한 문장을 모아볼 수 있습니다.

### ❤️ 4. 소통 및 반응
*   **감정 표현:** 문장마다 이모지(😲, 🤣, 😭)를 남겨 작가와 공감할 수 있습니다.
*   **북마크:** 재미있는 소설은 보관함에 저장해두고 언제든 다시 읽습니다.

<br>

## 5. 🗂️ ERD 설계 (Entity Relationship)
핵심 도메인인 `Member`, `Story`, `Sentence`의 유기적인 관계입니다.

```mermaid
erDiagram
    MEMBER ||--o{ STORY : creates
    MEMBER ||--o{ SENTENCE : writes
    STORY ||--|{ SENTENCE : contains
    STORY ||--o{ REACTION : has

    MEMBER {
        long member_id PK
        string email
        string nickname
        string password
    }
    
    STORY {
        long story_id PK
        string title
        string status "WRITING/COMPLETED"
        int current_sequence
        int max_sequence
        long last_writer_id FK
    }

    SENTENCE {
        long sentence_id PK
        long story_id FK
        long writer_id FK
        string content
        int sequence_no
    }
