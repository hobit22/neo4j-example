# Neo4j Example - IT 기술 블로그 지식 맵

Spring Boot와 Neo4j를 활용한 IT 기술 블로그 지식 맵 프로젝트입니다.

## 프로젝트 소개

국내 IT 기업들의 기술 블로그 글들을 그래프 데이터베이스로 관리하고, 글들 간의 관계를 시각화할 수 있는 시스템입니다.

### 주요 기능

- 블로그 글 CRUD (생성, 조회, 수정, 삭제)
- 주제(Topic)별 글 검색
- 회사별 기술 블로그 글 조회
- 작성자별 글 조회
- 연관 글 추천 (같은 주제를 다루는 글)
- 기술 스택 관계 조회

### 그래프 모델

**노드(Node) 타입:**
- `BlogPost`: 블로그 글 (제목, URL, 작성일, 내용 요약)
- `Author`: 작성자 (이름, 블로그명) - **nullable**
- `Topic`: 주제/기술 태그 (Spring, Kotlin 등) - **여러 개 가능**
- `Company`: 기술 블로그 운영 회사

**관계(Relationship):**
- `WRITTEN_BY`: BlogPost → Author (선택적)
- `TAGGED_WITH`: BlogPost → Topic (다중)
- `BELONGS_TO`: BlogPost → Company
- `REFERENCES`: BlogPost → BlogPost (글 간 참조)
- `RELATED_TO`: Topic → Topic (관련 기술)

## 기술 스택

- Java 17
- Spring Boot 3.2.0
- Spring Data Neo4j
- Neo4j 5.14.0
- Gradle
- Docker & Docker Compose
- Lombok

## 시작하기

### 사전 요구사항

- Java 17 이상
- Docker & Docker Compose

### 설치 및 실행

1. **저장소 클론**
```bash
git clone https://github.com/hobit22/neo4j-example.git
cd neo4j-example
```

2. **Neo4j 데이터베이스 실행**
```bash
docker-compose up -d
```

Neo4j 브라우저: http://localhost:7474
- Username: `neo4j`
- Password: `password`

3. **애플리케이션 실행**
```bash
./gradlew bootRun
```

또는 IDE에서 `Neo4jExampleApplication` 클래스를 실행합니다.

4. **샘플 데이터 확인**

애플리케이션이 시작되면 자동으로 샘플 데이터가 생성됩니다:
- 4개 회사 (카카오, 네이버, 우아한형제들, 토스)
- 8개 주제 (Spring, Kotlin, Java, Kubernetes 등)
- 7개 블로그 글

## API 엔드포인트

### BlogPost API

- `GET /api/posts` - 모든 블로그 글 조회
- `GET /api/posts/{id}` - ID로 글 조회
- `GET /api/posts/search?title={title}` - 제목으로 검색
- `GET /api/posts/company/{companyName}` - 회사별 글 조회
- `GET /api/posts/topic/{topicName}` - 주제별 글 조회
- `GET /api/posts/author/{authorName}` - 작성자별 글 조회
- `GET /api/posts/{id}/related?limit={limit}` - 연관 글 추천
- `POST /api/posts/topics` - 여러 주제로 글 검색
- `POST /api/posts` - 새 글 생성
- `PUT /api/posts/{id}` - 글 수정
- `DELETE /api/posts/{id}` - 글 삭제

### Topic API

- `GET /api/topics` - 모든 주제 조회
- `GET /api/topics/{id}` - ID로 주제 조회
- `GET /api/topics/name/{name}` - 이름으로 주제 조회
- `GET /api/topics/category/{category}` - 카테고리별 주제 조회
- `GET /api/topics/{name}/related` - 관련 주제 조회
- `GET /api/topics/most-used?limit={limit}` - 가장 많이 사용된 주제
- `POST /api/topics` - 새 주제 생성

## API 사용 예제

### 1. 모든 블로그 글 조회
```bash
curl http://localhost:8080/api/posts
```

### 2. 주제별 글 조회 (예: Spring)
```bash
curl http://localhost:8080/api/posts/topic/Spring
```

### 3. 회사별 글 조회 (예: 카카오)
```bash
curl http://localhost:8080/api/posts/company/카카오
```

### 4. 연관 글 추천
```bash
curl http://localhost:8080/api/posts/1/related?limit=5
```

### 5. 새 블로그 글 생성
```bash
curl -X POST http://localhost:8080/api/posts \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Neo4j와 Spring Boot 연동하기",
    "url": "https://example.com/neo4j-spring",
    "publishedAt": "2024-01-15T10:00:00",
    "summary": "Neo4j를 Spring Boot 프로젝트에 통합하는 방법을 소개합니다.",
    "authorName": "김개발",
    "topicNames": ["Spring", "Database"],
    "companyName": "카카오"
  }'
```

### 6. 여러 주제로 글 검색
```bash
curl -X POST http://localhost:8080/api/posts/topics \
  -H "Content-Type: application/json" \
  -d '["Spring", "Kotlin"]'
```

### 7. 가장 많이 사용된 주제 조회
```bash
curl http://localhost:8080/api/topics/most-used?limit=10
```

## Neo4j 브라우저에서 Cypher 쿼리 예제

Neo4j 브라우저(http://localhost:7474)에서 다음 쿼리들을 실행해볼 수 있습니다:

### 모든 노드와 관계 시각화
```cypher
MATCH (n) RETURN n LIMIT 25
```

### Spring 관련 블로그 글과 관계 조회
```cypher
MATCH (post:BlogPost)-[:TAGGED_WITH]->(topic:Topic {name: 'Spring'})
RETURN post, topic
```

### 특정 회사의 기술 블로그 글 조회
```cypher
MATCH (post:BlogPost)-[:BELONGS_TO]->(company:Company {name: '카카오'})
RETURN post, company
```

### 주제 간 관계 시각화
```cypher
MATCH (t1:Topic)-[:RELATED_TO]->(t2:Topic)
RETURN t1, t2
```

### 가장 많은 글이 작성된 주제 찾기
```cypher
MATCH (topic:Topic)<-[:TAGGED_WITH]-(post:BlogPost)
RETURN topic.name, COUNT(post) as postCount
ORDER BY postCount DESC
LIMIT 5
```

### 블로그 글 간 참조 관계 조회
```cypher
MATCH (post1:BlogPost)-[:REFERENCES]->(post2:BlogPost)
RETURN post1, post2
```

## 프로젝트 구조

```
src/main/java/com/example/neo4j/
├── domain/                 # 엔티티 클래스
│   ├── Author.java
│   ├── BlogPost.java
│   ├── Company.java
│   └── Topic.java
├── repository/             # Spring Data Neo4j 리포지토리
│   ├── AuthorRepository.java
│   ├── BlogPostRepository.java
│   ├── CompanyRepository.java
│   └── TopicRepository.java
├── service/                # 비즈니스 로직
│   ├── BlogPostService.java
│   └── TopicService.java
├── controller/             # REST API 컨트롤러
│   ├── BlogPostController.java
│   └── TopicController.java
├── dto/                    # 데이터 전송 객체
│   ├── BlogPostRequest.java
│   └── BlogPostResponse.java
├── config/                 # 설정 클래스
│   └── DataInitializer.java
└── Neo4jExampleApplication.java
```

## 개발 팁

### Neo4j 브라우저 활용

Neo4j 브라우저를 통해 그래프 데이터를 시각적으로 확인하고 Cypher 쿼리를 직접 실행해볼 수 있습니다.

### 커스텀 쿼리 추가

`@Query` 어노테이션을 사용하여 리포지토리에 커스텀 Cypher 쿼리를 추가할 수 있습니다:

```java
@Query("MATCH (post:BlogPost)-[:TAGGED_WITH]->(topic:Topic) " +
       "WHERE topic.name = $topicName " +
       "RETURN post")
List<BlogPost> findByTopicName(@Param("topicName") String topicName);
```

## 문제 해결

### Docker가 실행되지 않는 경우
Docker Desktop이 실행 중인지 확인하세요.

### Neo4j 연결 오류
`application.yml` 파일의 Neo4j 연결 정보를 확인하세요:
- URI: `bolt://localhost:7687`
- Username: `neo4j`
- Password: `password`

### 포트 충돌
다른 애플리케이션이 7474, 7687, 8080 포트를 사용 중이라면 docker-compose.yml 또는 application.yml에서 포트를 변경하세요.

## 향후 개선 사항

- [ ] RSS 피드 크롤러 구현
- [ ] 그래프 시각화 웹 UI 추가
- [ ] 전문 검색(Full-text Search) 기능
- [ ] 학습 경로 추천 알고리즘
- [ ] 사용자 인증 및 권한 관리
- [ ] API 문서화 (Swagger/OpenAPI)

## 라이선스

MIT License

## 참고 자료

- [Neo4j Documentation](https://neo4j.com/docs/)
- [Spring Data Neo4j](https://spring.io/projects/spring-data-neo4j)
- [Cypher Query Language](https://neo4j.com/docs/cypher-manual/current/)
