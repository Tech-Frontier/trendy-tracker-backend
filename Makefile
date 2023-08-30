docker-build:
	docker build -f ./Dockerfile -t jinsujj/trendy-tracker-backend:latest .

docker-run:
	docker run --network host jinsujj/trendy-tracker-backend

docker-set:
	docker cp /usr/local/bin/ infallible_bell:/usr/local/
