package com.example.neo4j.config;

import com.example.neo4j.domain.Author;
import com.example.neo4j.domain.BlogPost;
import com.example.neo4j.domain.Company;
import com.example.neo4j.domain.Topic;
import com.example.neo4j.repository.AuthorRepository;
import com.example.neo4j.repository.BlogPostRepository;
import com.example.neo4j.repository.CompanyRepository;
import com.example.neo4j.repository.TopicRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.Set;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(
            CompanyRepository companyRepository,
            AuthorRepository authorRepository,
            TopicRepository topicRepository,
            BlogPostRepository blogPostRepository) {

        return args -> {
            log.info("Initializing sample data...");

            // Clear existing data
            blogPostRepository.deleteAll();
            topicRepository.deleteAll();
            authorRepository.deleteAll();
            companyRepository.deleteAll();

            // Create Companies
            Company kakao = companyRepository.save(Company.builder()
                    .name("카카오")
                    .blogUrl("https://tech.kakao.com/blog")
                    .build());

            Company naver = companyRepository.save(Company.builder()
                    .name("네이버")
                    .blogUrl("https://d2.naver.com")
                    .build());

            Company woowa = companyRepository.save(Company.builder()
                    .name("우아한형제들")
                    .blogUrl("https://techblog.woowahan.com")
                    .build());

            Company toss = companyRepository.save(Company.builder()
                    .name("토스")
                    .blogUrl("https://toss.tech")
                    .build());

            // Create Authors
            Author author1 = authorRepository.save(Author.builder()
                    .name("김개발")
                    .blogName("개발자의 삶")
                    .build());

            Author author2 = authorRepository.save(Author.builder()
                    .name("박백엔드")
                    .blogName("백엔드 이야기")
                    .build());

            // Create Topics
            Topic spring = topicRepository.save(Topic.builder()
                    .name("Spring")
                    .description("스프링 프레임워크")
                    .category("Backend")
                    .build());

            Topic kotlin = topicRepository.save(Topic.builder()
                    .name("Kotlin")
                    .description("코틀린 프로그래밍 언어")
                    .category("Language")
                    .build());

            Topic java = topicRepository.save(Topic.builder()
                    .name("Java")
                    .description("자바 프로그래밍 언어")
                    .category("Language")
                    .build());

            Topic kubernetes = topicRepository.save(Topic.builder()
                    .name("Kubernetes")
                    .description("컨테이너 오케스트레이션")
                    .category("DevOps")
                    .build());

            Topic docker = topicRepository.save(Topic.builder()
                    .name("Docker")
                    .description("컨테이너 플랫폼")
                    .category("DevOps")
                    .build());

            Topic react = topicRepository.save(Topic.builder()
                    .name("React")
                    .description("리액트 프레임워크")
                    .category("Frontend")
                    .build());

            Topic database = topicRepository.save(Topic.builder()
                    .name("Database")
                    .description("데이터베이스 관리")
                    .category("Backend")
                    .build());

            Topic msa = topicRepository.save(Topic.builder()
                    .name("MSA")
                    .description("마이크로서비스 아키텍처")
                    .category("Architecture")
                    .build());

            // Set topic relationships
            spring.setRelatedTopics(Set.of(java, kotlin));
            kotlin.setRelatedTopics(Set.of(spring, java));
            kubernetes.setRelatedTopics(Set.of(docker, msa));
            docker.setRelatedTopics(Set.of(kubernetes));
            msa.setRelatedTopics(Set.of(spring, kubernetes));

            topicRepository.save(spring);
            topicRepository.save(kotlin);
            topicRepository.save(kubernetes);
            topicRepository.save(docker);
            topicRepository.save(msa);

            // Create Blog Posts
            BlogPost post1 = blogPostRepository.save(BlogPost.builder()
                    .title("Spring Boot 3.0 마이그레이션 가이드")
                    .url("https://tech.kakao.com/blog/spring-boot-3")
                    .publishedAt(LocalDateTime.now().minusDays(30))
                    .summary("Spring Boot 2.x에서 3.0으로 마이그레이션하는 방법과 주의사항을 다룹니다.")
                    .author(author1)
                    .topics(Set.of(spring, java))
                    .company(kakao)
                    .build());

            BlogPost post2 = blogPostRepository.save(BlogPost.builder()
                    .title("Kotlin 코루틴 완벽 가이드")
                    .url("https://d2.naver.com/kotlin-coroutine")
                    .publishedAt(LocalDateTime.now().minusDays(25))
                    .summary("Kotlin 코루틴의 기본 개념부터 고급 사용법까지 상세히 설명합니다.")
                    .author(author2)
                    .topics(Set.of(kotlin, spring))
                    .company(naver)
                    .build());

            BlogPost post3 = blogPostRepository.save(BlogPost.builder()
                    .title("Kubernetes를 활용한 MSA 운영")
                    .url("https://techblog.woowahan.com/k8s-msa")
                    .publishedAt(LocalDateTime.now().minusDays(20))
                    .summary("Kubernetes 환경에서 마이크로서비스를 효율적으로 운영하는 방법을 소개합니다.")
                    .author(author1)
                    .topics(Set.of(kubernetes, docker, msa))
                    .company(woowa)
                    .build());

            BlogPost post4 = blogPostRepository.save(BlogPost.builder()
                    .title("대규모 트래픽 처리를 위한 데이터베이스 설계")
                    .url("https://toss.tech/database-design")
                    .publishedAt(LocalDateTime.now().minusDays(15))
                    .summary("대용량 트래픽 환경에서의 데이터베이스 설계 전략과 최적화 기법을 다룹니다.")
                    .author(null)  // nullable author
                    .topics(Set.of(database, spring))
                    .company(toss)
                    .build());

            BlogPost post5 = blogPostRepository.save(BlogPost.builder()
                    .title("React와 Spring Boot로 구축하는 풀스택 애플리케이션")
                    .url("https://tech.kakao.com/blog/fullstack-app")
                    .publishedAt(LocalDateTime.now().minusDays(10))
                    .summary("React 프론트엔드와 Spring Boot 백엔드를 결합한 풀스택 개발 가이드입니다.")
                    .author(author2)
                    .topics(Set.of(react, spring, java))
                    .company(kakao)
                    .build());

            BlogPost post6 = blogPostRepository.save(BlogPost.builder()
                    .title("Docker 컨테이너 최적화 실전 가이드")
                    .url("https://d2.naver.com/docker-optimization")
                    .publishedAt(LocalDateTime.now().minusDays(5))
                    .summary("Docker 컨테이너 이미지 크기 최적화와 성능 향상 기법을 소개합니다.")
                    .author(null)  // nullable author
                    .topics(Set.of(docker, kubernetes))
                    .company(naver)
                    .build());

            BlogPost post7 = blogPostRepository.save(BlogPost.builder()
                    .title("Spring Cloud로 구현하는 마이크로서비스")
                    .url("https://techblog.woowahan.com/spring-cloud")
                    .publishedAt(LocalDateTime.now().minusDays(3))
                    .summary("Spring Cloud를 활용한 마이크로서비스 아키텍처 구축 사례를 공유합니다.")
                    .author(author1)
                    .topics(Set.of(spring, msa, java))
                    .company(woowa)
                    .build());

            // Add cross-references between posts
            post1.setReferencedPosts(Set.of(post5, post7));
            post2.setReferencedPosts(Set.of(post7));
            post3.setReferencedPosts(Set.of(post6, post7));

            blogPostRepository.save(post1);
            blogPostRepository.save(post2);
            blogPostRepository.save(post3);

            log.info("Sample data initialization completed!");
            log.info("Created {} companies", companyRepository.count());
            log.info("Created {} authors", authorRepository.count());
            log.info("Created {} topics", topicRepository.count());
            log.info("Created {} blog posts", blogPostRepository.count());
        };
    }
}
