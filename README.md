# trendy-tracker-backend


## 이벤트 스토밍
![trendy-tracker](https://github.com/Tech-Frontier/trendy-tracker-backend/assets/19955904/4774e50b-40e7-42fe-abf3-ac3084241564)

<br/>

## Setting
**[구성]**

1. 분산 시스템을 고려하여, 현재 기준 2대의 RaspberryPi 4 8GB 서버에서 구동된다. 
- 1. Postgre + Elastic Search DB (RaspberryPi 4)서버
- 2. Spring boot + kafka + zookeeper + kibana + logstash 백엔드 (RaspberryPi 4) 서버 

2. Project 의 아래 경로는 각 logstash, kibana, elasticsearch 에 대한 환경설정 폴더를 만들어 docker volume 으로 지정해 docker-compose.yml 을 실행 시 같이 반영되어 실행된다.
```
src/main/java/com/trendyTracker/common/
```
 
**[실행]**

1. DB 서버에서 'Elastic Search' 를 구동 후에 Kibana 와 연동하기 위한 아래 인증 코드를 발행한다.
```
bin/elasticsearch-service-tokens create elastic/kibana jinsu
```

2. 발급한 토큰을 Project kibana 의 kibana.yml 의 토큰에 기입해준다.
```
src/main/java/com/trendyTracker/common/kibana/kibana.yml
```

4. Project root directory 의 docker-compose.yml 파일에서 "trendy_tracker" 에 대한 환경변수를 변경한다.
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

## CI/CD 
> github action 
- 라즈베리파이 환경 (arm64/v8) 아키텍처에서 Docker build 가 정상적으로 되도록 self-hosted 환경으로 구성
- gradle build -> docker build -> docker hub push 

<br/>

> webhook
- docker hub 로 push 가 정상적으로 수행되면 라즈베리파이 webhook flask 서버를 활용해서 docker image 를 가져와 docker-compse 를 실행시킨다 
- flask 에서 export 를 통한 환경변수 주입 필요.
- 공개되지 않은 docker-compose.yml 파일 필요

