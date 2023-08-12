docker-build:
	docker build -f ./Dockerfile -t trendy-tracker .

docker-run:
	docker run --network host trendy-tracker 

docker-set:
	docker cp /usr/local/bin/ infallible_bell:/usr/local/
