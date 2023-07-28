docker-build:
	docker build -f ./Dockerfile -t trendy-tracker .

docker-run:
	docker run -p 80:8080 trendy-tracker 