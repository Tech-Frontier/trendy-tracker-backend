# trendy-tracker-backend
> public <br/>
https://www.trendy-tracker.kr

>admin <br/>
https://admin.trendy-tracker.kr

> backend  <br/>
https://api.trendy-tracker.kr

## Vision
> 모든 개발자의 정보 불균등을 없앤다.

Objective 1
> 개발자 기술 경향을 한눈에 파악할 수 있는 환경을 만든다.

Objective 2
> 개발자 채용 시장을 한눈에 파악할 수 있는 환경을 만든다.
<br/>

<h2 align="center">📚 Tech Stack 📚</h2>
<p align="center">
  <img src="https://img.shields.io/badge/Java-007396?style=flat-square&logo=Java&logoColor=white"/></a>&nbsp
  <img src="https://img.shields.io/badge/Python-3766AB?style=flat-square&logo=Python&logoColor=white"/></a>&nbsp 
  
  <br>
  <img src="https://img.shields.io/badge/SpringBoot-6DB33F?style=flat-square&logo=SpringBoot&logoColor=white"/></a>&nbsp 
  <img src="https://img.shields.io/badge/Flask-000000?style=flat-square&logo=Flask&logoColor=white"/></a>&nbsp 
  <br>  
  <img src="https://img.shields.io/badge/Nginx-009639?style=flat-square&logo=Nginx&logoColor=white"/></a>&nbsp 
  <img src="https://img.shields.io/badge/Let's Encrypt-003A70?style=flat-square&logo=Let's Encrypt&logoColor=white"/></a>&nbsp
  <img src="https://img.shields.io/badge/Docker-2496ED?style=flat-square&logo=Docker&logoColor=white"/></a>&nbsp 
  <br/>
  <img src="https://img.shields.io/badge/Apache-kafka-231F20?style=flat-square&logo=Apache-kafka&logoColor=white"/></a>&nbsp 
  <img src="https://img.shields.io/badge/Logstash-005571?style=flat-square&logo=Logstash&logoColor=white"/></a>&nbsp 
  <img src="https://img.shields.io/badge/Elasticsearch-005571?style=flat-square&logo=Elasticsearch&logoColor=white"/></a>&nbsp 
  <br/>
  <img src="https://img.shields.io/badge/PostgreSQL-4169E1?style=flat-square&logo=PostgreSQL&logoColor=white"/></a>&nbsp 
  <img src="https://img.shields.io/badge/Jpa-67A4AC?style=flat-square&logo=Jpa&logoColor=white"/></a>&nbsp 
  <img src="https://img.shields.io/badge/QueryDsl-7D929E?style=flat-square&logo=QueryDsl&logoColor=white"/></a>&nbsp 
  <img src="https://img.shields.io/badge/Redis-DC382D?style=flat-square&logo=Redis&logoColor=white"/></a>&nbsp 
  <br/>
   <img src="https://img.shields.io/badge/RaspberryPi-A22846?style=flat-square&logo=RaspBerryPi&logoColor=white"/></a>&nbsp 
  <img src="https://img.shields.io/badge/Linux-E95420?style=flat-square&logo=Linux&logoColor=white"/></a>&nbsp 
  <br/>
  <img src="https://img.shields.io/badge/Kibana-005571?style=flat-square&logo=Kibana&logoColor=white"/></a>&nbsp 
  <img src="https://img.shields.io/badge/Selenium-43B02A?style=flat-square&logo=Selenium&logoColor=white"/></a>&nbsp 
</p>
<br/>

<h3 align="center">🌈 Follow Me 🌈</h3>
<p align="center">
  <a href="https://www.owl-dev.me"><img src="https://img.shields.io/badge/Tech%20Blog-11B48A?style=flat-square&logo=Vimeo&logoColor=white&link=https://www.owl-dev.me"/></a>&nbsp
  <a href="https://www.linkedin.com/in/jinsu-jang-0b2269107"><img src="https://img.shields.io/badge/LinkedIn-0A66C2?style=flat-square&logo=LinkedIn&logoColor=white"/></a>
</p>


## Event Storming (draft)
![trendy-tracker](https://github.com/Tech-Frontier/trendy-tracker-backend/assets/19955904/4774e50b-40e7-42fe-abf3-ac3084241564)

<br/>




## Setting
### 구성

1. 분산 시스템을 고려하여, 3대의 RaspberryPi 서버에서 구동된다. 
 > 1. Postgre + Elastic Search (DB 서버)
 > 2. Kafka + Kafka ui + Zookeeper + Kibana + Logstash (InfraStructure 서버)
>  3. Spring boot 2개 + Redis (Backend 서버)

<br/>

2. **디렉터리 구조**  <br/> 
아키텍처 관점에서, **도메인주도 설계**와, **헥사고날 아키텍처**를 반영해 구성합니다. \
Domain 영역에서 각 도메인영역을 **레이어드 아키텍처로** 구현하면서, 실제 구현부는 Interface (Port) 로 결합을 분리하고, Adaptor 영역에서 실질적으로 구현합니다.

> Domain (도메인)
- 비즈니스의 핵심 도메인 영역을 집중시킵니다 
- 바운디드 컨텍스트의 단위인  **'AppService', 'Jobs', 'Subscription'** 를 상위 폴더를 잡고 내부 폴더에 하위 도메인을 명시합니다
- 각 하위 도메인 폴더는 **Layered Architecture** 를 따라 구현합니다
- 특정 도메인에는 Entity Table 이 존재하지는 않습니다 (* 특정 도메인은 여러 테이블간의 조인으로 구성되기에)
- 특정 도메인에는 VO, DTO 같은 객체를 따로 관리합니다 

> Adaptors (어댑터)
- 도메인영역에서 Interface **(Port)** 로 결합을 분리한 부분을 실제 구현하는 부분 **(Adaptor)** 입니다
- **Database** 의 구현부, **Messaging System** 의 구현부에 해당합니다 

> Api (프레젠테이션 계층)
- 프레젠테이션 영역으로 Client 와 접하는 부분입니다 
- API 서버를 만들고 있기에 폴더명을 Api 로 명시했습니다 

> Common (공통) 
- 서비스에서 공통적으로 사용할 영역입니다
- Config, Exception, Interceptor, Logging, Response 를 담당합니다
- Config 는 Cors, Email, Jwt, Swagger 를 관리합니다.

> Infrastructure (인프라)
-  ELK 스택을 위한 환경 설정 파일을 관리하는 폴더입니다 

```
/trendyTracker/
├── Adaptors
│   ├── CacheMemory
│   │   ├── EmailValidationCacheImpl.java
│   │   ├── RecruitsCacheImpl.java
│   │   ├── RedisManager.java
│   │   └── TechsCacheImpl.java
│   ├── Database
│   │   ├── CompanyRepositorylmpl.java
│   │   ├── RecruitRepositoryImpl.java
│   │   └── StaticsRepositoryImpl.java
│   └── MessagingSystem
│       ├── KafkaProducer.java
│       └── RecruitConsumerImpl.java
├── Api
│   ├── AppInfo
│   │   └── AppInfoController.java
│   ├── Email
│   │   └── EmailController.java
│   ├── Recruit
│   │   ├── ChartController.java
│   │   ├── CompanyController.java
│   │   └── RecruitController.java
│   ├── Tech
│   │   └── TechController.java
│   └── WelcomeController.java
├── Common
│   ├── Config
│   │   ├── CorsConfiguration.java
│   │   ├── EmailConfiguration.java
│   │   ├── JwtProvider.java
│   │   └── SwaggerConfig.java
│   ├── Exception
│   │   ├── ApiError.java
│   │   ├── ApiExceptionHandler.java
│   │   └── ExceptionDetail
│   │       ├── AlreadyExistException.java
│   │       ├── NoResultException.java
│   │       └── NotAllowedValueException.java
│   ├── Interceptor
│   │   ├── InterCeptorConfig.java
│   │   ├── JwtInterceptor.java
│   │   └── UUIDInterceptor.java
│   ├── Logging
│   │   ├── LogAspect.java
│   │   └── Loggable.java
│   └── Response
│       └── Response.java
├── Domain
│   ├── AppService
│   │   ├── UserSubscribeCompanies
│   │   │   ├── UserSubscribeCompanies.java
│   │   │   └── UserSubscribeCompaniesRepository.java
│   │   ├── UserSubscribeTemplates
│   │   │   ├── UserSubscribeTemplates.java
│   │   │   └── UserSubscribeTemplatesRepository.java
│   │   └── Users
│   │       ├── User.java
│   │       └── UserRepository.java
│   ├── Jobs
│   │   ├── Companies
│   │   │   ├── Company.java
│   │   │   ├── CompanyRepository.java
│   │   │   ├── CompanyRepositoryCustom.java
│   │   │   ├── CompanyService.java
│   │   │   └── Vo
│   │   │       └── CompanyInfo.java
│   │   ├── RecruitTechs
│   │   │   ├── RecruitTech.java
│   │   │   └── RecruitTechRepository.java
│   │   ├── Recruits
│   │   │   ├── Dto
│   │   │   │   ├── JobScrapInfoDto.java
│   │   │   │   └── RecruitInfoDto.java
│   │   │   ├── Recruit.java
│   │   │   ├── RecruitCache.java
│   │   │   ├── RecruitConsumer.java
│   │   │   ├── RecruitRepository.java
│   │   │   ├── RecruitRepositoryCustom.java
│   │   │   └── RecruitService.java
│   │   ├── Statistics
│   │   │   ├── Dto
│   │   │   │   └── ChartInfo.java
│   │   │   ├── StaticsRepository.java
│   │   │   └── StaticsService.java
│   │   └── Techs
│   │       ├── Tech.java
│   │       ├── TechRepository.java
│   │       ├── TechService.java
│   │       └── TechsCache.java
│   └── Subscription
│       ├── Emails
│       │   ├── EmailService.java
│       │   ├── EmailValidationCache.java
│       │   └── Vo
│       │       └── EmailValidation.java
│       ├── Schedulings
│       │   ├── Scheduling.java
│       │   └── SchedulingRepository.java
│       └── SubscribeTemplates
│           ├── Template.java
│           └── TemplateRepository.java
├── Infrastructure 
│   ├── elasticsearch
│   │   ├── README.me
│   │   └── elasticsearch.yml
│   ├── kibana
│   │   └── kibana.yml
│   └── logstash
│       ├── log4j2.properties
│       ├── logstash.conf
│       ├── logstash.yml
│       ├── pipeline.yml
│       └── pipelines.yml
├── TrendyTrackerApplication.java
└── Util
    ├── CompanyUtils.java
    ├── TechUtils.java
    └── UrlReader.java

```

## CI/CD 
> github action 
- 라즈베리파이 환경 (arm64/v8) 아키텍처에서 Docker build 가 정상적으로 되도록 self-hosted 환경으로 구성
- gradle build -> docker build -> docker hub push 

<br/>

> webhook
- docker hub 로 push 가 정상적으로 수행되면 라즈베리파이 webhook flask 서버를 활용해서 docker image 를 가져와 docker-compse 를 실행시킨다 
- flask 에서 export 를 통한 환경변수 주입 필요.
<br/>

## DB modeling
db 모델링 히스토리 PR (Pull Request) <br/>
[<a href="https://github.com/Tech-Frontier/trendy-tracker-backend/pull/15">https://github.com/Tech-Frontier/trendy-tracker-backend/pull/15 </a>](https://github.com/Tech-Frontier/trendy-tracker-backend/pull/15)
> @OneToMany , @manytoone 처럼 관습적으로 매핑하는 구조를 없애고 필요한 엔티티에만 @OnetoOne 으로 연관관계 매핑함
   즉 User, Template, Company 테이블에 subscribe 에 해당하는 fk 칼럼을 따로 구성하지 않도록 한다

> 각 바운디드 컨텍스트 마다 Repository 로 분리해서, 각 Repository 내의 Table 간 조인은 하지만, 바운디트 컨텍스트간(Repository)의 조인은 구조적으로 분리한다.

![스크린샷 2023-10-28 오후 3 13 17](https://github.com/Tech-Frontier/trendy-tracker-backend/assets/19955904/9b7863ec-1759-4807-8115-e2b67bb8abf0)


<br/>

## Logging System
#### 시각화
> Trendy-Tracker 의 로깅 시스템은 ELK 스택을 활용해서 Elasticsearch DB 에 저장하고, Kibana 를 통해서 지표를 보여주는 역할을 한다. <br/>

- kafka -> logstash -> elasticsearch -> kibana
![kibana](https://github.com/Tech-Frontier/trendy-tracker-backend/assets/19955904/10111319-b95f-4c12-a524-9ef0db144c2d)
<br/>

#### 로그 수집 내용
> logging 에서도 kafka를 사용하며 **logger, error** 토픽을 만들어서 아래 정보를 기록해 수집합니다.

- api_path       (API 경로)
- params         (API 파라미터)
- event_time     (발생 시각)
- duration_time  (API 소요시간)
- error_message  (Exception 메시지)
- header         (UUID * api 응답에도 같이 header에 남겨 디버깅 을 용이하게 합니다)
<br/>

![image](https://github.com/Tech-Frontier/trendy-tracker-backend/assets/19955904/0ebe71d6-e0eb-4d11-b6d7-0f74cbaa4de2)


<br/>

### Issue 
- Docker + RaspberryPi 환경에서 Selenium 구동기 \
https://www.owl-dev.me/blog/72

- 채용공고 API 성능개선 \
https://www.notion.so/API-2200569dcfba4bf0a8ad95674a59c9f6



### 실행 방법

> #### DB 서버 (Raspberry Pi 4)

1. **DB 서버**에서 해당 프로젝트를 git clone 후 '**docker-compose-elasticsearch.yml**' 파일을 실행한다
```
git clone https://github.com/Tech-Frontier/trendy-tracker-backend.git
docker-compose -f docker-compose-elasticsearch.yml up -d 
```
2. DB 서버에서 **'Elastic Search'** 를 구동 후에 **Kibana** 와 연동하기 위한 아래 인증 토큰을 발행한다.
```
docker exec -it elasticsearch bash
bin/elasticsearch-service-tokens create elastic/kibana jinsu
```
<br/>

> #### Infrastructure 서버 (Raspberry Pi 4) 

1.  **Infrastructure 서버** 에도 해당 프로젝트를 git clone 한 후, elasticsearch 에서 발급된 토큰을 Project kibana 설정폴더의 **kibana.yml** 의 토큰에 기입해준다.
```
git clone https://github.com/Tech-Frontier/trendy-tracker-backend.git

src/main/java/com/trendyTracker/infrastructure/kibana/kibana.yml
elasticsearch.serviceAccountToken: "여기에 기입"
```

2. logstash 설정파일에서, DB 서버의 **elasticsearch** 의 주소를 바라보도록 변경해준다.
```
[logstash.conf]
path: src/main/java/com/trendyTracker/infrastructure/logstash/logstash.conf
update: hosts => ["http://owl-dev.me:9200"]

[logstash.yml]
path: src/main/java/com/trendyTracker/infrastructure/logstash/logstash.yml
update: xpack.monitoring.elasticsearch.hosts: ["http://owl-dev.me:9200"]
```

3. elasticsearch 연동을 위한 세팅 후 docker-compose, 파일을 실행한다
```
docker-compose -f docker-compose-infra.yml up -d
```

<br/>

> #### Backend 서버 (Raspberry Pi 5) 

1. Project root directory 의 docker-compose.yml 파일에서 **"trendy_tracker"** 에 대한 **'DB', 'Token'** 환경변수를 변경한다.
```
 environment:
    SERVER_PORT:                  #수정 필요
    SPRING_DATASOURCE_URL:        #수정 필요
    SPRING_DATASOURCE_USERNAME:   #수정 필요
    SPRING_DATASOURCE_PASSWORD:   #수정 필요
    TOKEN_VALUE:                  #수정 필요
    SPRING_DATASOURCE_DRIVER_CLASS_NAME:  org.postgresql.Driver
    SPRING_JPA_HIBERNATE_DDL_AUTO: update
    SPRING_JPA_HIBERNATE_SHOW_SQL: "off"
    SPRING_JPA_HIBERNATE_FORMAT_SQL: "off"
    SPRING_KAFKA_BOOTSTRAP_SERVERS: localhost:9092,localhost:9093,localhost:9094
    LOGGING_LEVEL_ORG_APACHE_KAFKA: "off"
```

2. 'docker-compose.yml' , 'docker-compose-redis.yml' 파일을 실행한다
```
docker-compose -f docker-compose.yml up -d
docker-compose -f docker-compose-redis.yml up -d  
```
