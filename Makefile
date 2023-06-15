docker-build:
	docker build -f ./src/build/Dockerfile -t trendy-tracker .

docker-run:
	docker run -p 80:8080 trendy-tracker 