# trendy-tracker-backend

## Vision
> 모든 개발자의 정보 불균등을 없앤다.

Objective 1
> 개발자 기술 경향을 한눈에 파악할 수 있는 환경을 만든다.

Objective 2
> 개발자 채용 시장을 한눈에 파악할 수 있는 환경을 만든다.
<br/>

## 이벤트 스토밍
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
src/main/java/com/trendyTracker/common/
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

src/main/java/com/trendyTracker/common/kibana/kibana.yml
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
path: src/main/java/com/trendyTracker/common/logstash/logstash.conf
update: hosts => ["http://owl-dev.me:9200"]

[logstash.yml]
path: src/main/java/com/trendyTracker/common/logstash/logstash.yml
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
db 모델링 히스토리
https://github.com/Tech-Frontier/trendy-tracker-backend/pull/15
> @OneToMany , @manytoone 처럼 관습적으로 매핑하는 구조를 없애고 필요한 엔티티에만 @OnetoOne 으로 연관관계 매핑함
   즉 User, Template, Company 테이블에 subscribe 에 해당하는 fk 칼럼을 따로 구성하지 않도록 한다

> 각 바운디드 컨텍스트 마다 Repository 로 분리해서, 각 Repository 내의 Table 간 조인은 하지만, 바운디트 컨텍스트간(Repository)의 조인은 구조적으로 분리한다.
> 
![image](https://github.com/Tech-Frontier/trendy-tracker-backend/assets/19955904/c8388e3d-4933-4dec-8330-752f4bd77af5)

<br/>
