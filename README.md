# trendy-tracker-backend

<br/>

## CI/CD 
> github action 
- 라즈베리파이 환경 (arm64/v8) 아키텍처에서 Docker build 가 정상적으로 되도록 self-hosted 환경으로 구성
- gradle build -> docker build -> docker hub push 

<br/>

> webhook
- docker hub 로 push 가 정상적으로 수행되면 라즈베리파이 webhook flask 서버를 활용해서 docker image 를 가져와 docker-compse 를 실행시킨다 
- flask 에서 export 를 통한 환경변수 주입 필요. 

## 이벤트 스토밍
![trendy-tracker](https://github.com/Tech-Frontier/trendy-tracker-backend/assets/19955904/4774e50b-40e7-42fe-abf3-ac3084241564)
