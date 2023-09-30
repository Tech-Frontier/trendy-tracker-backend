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

1. 분산 시스템을 고려하여, 현재 기준 2대의 RaspberryPi 4 8GB 서버에서 구동된다. 
 > 1. Postgre + Elastic Search (DB 서버)
 > 2. Spring boot + kafka + zookeeper + kibana + logstash (Backend 서버)

2. Project 의 아래 경로는 각 logstash, kibana, elasticsearch 에 대한 환경설정 폴더를 만들고 <br/>
   docker volume 으로 지정해 docker-compose.yml 을 실행 시 같이 반영되어 실행된다.
```
src/main/java/com/trendyTracker/infrastructure/
```


### 실행 방법

#### DB 서버 (Raspberry Pi 4)

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

#### Backend 서버 (Raspberry Pi 4) 

3.  **Backend 서버** 에도 해당 프로젝트를 git clone 한 후, elasticsearch 에서 발급된 토큰을 Project kibana 설정폴더의 **kibana.yml** 의 토큰에 기입해준다.
```
git clone https://github.com/Tech-Frontier/trendy-tracker-backend.git

src/main/java/com/trendyTracker/infrastructure/kibana/kibana.yml
elasticsearch.serviceAccountToken: "여기에 기입"
```

4. Project root directory 의 docker-compose.yml 파일에서 **"trendy_tracker"** 에 대한 **'DB', 'Token'** 환경변수를 변경한다.
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

5. logstash 설정파일에서, DB 서버의 **elasticsearch** 의 주소를 바라보도록 변경해준다.
```
[logstash.conf]
path: src/main/java/com/trendyTracker/infrastructure/logstash/logstash.conf
update: hosts => ["http://owl-dev.me:9200"]

[logstash.yml]
path: src/main/java/com/trendyTracker/infrastructure/logstash/logstash.yml
update: xpack.monitoring.elasticsearch.hosts: ["http://owl-dev.me:9200"]
```

6. 'docker-compose.yml' 파일을 실행한다
```
docker-compose -f docker-compose.yml up -d 
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

<img width="100%" alt="DB Modeling" src="https://github.com/Tech-Frontier/trendy-tracker-backend/assets/19955904/ce4624fc-09c0-450e-8bbf-1cb00fe81866">


<br/>

## Logging System
#### 시각화
> Trendy-Tracker 의 로깅 시스템은 ELK 스택을 활용해서 Elasticsearch DB 에 저장하고, Kibana 를 통해서 지표를 보여주는 역할을 한다. <br/>

- kafka -> logstash -> elasticsearch -> kibana

![kibana](https://github.com/Tech-Frontier/trendy-tracker-backend/assets/19955904/1516f679-4e78-47c6-be4e-0f0e32d37668)

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

