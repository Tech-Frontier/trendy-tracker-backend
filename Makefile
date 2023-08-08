docker-build:
	docker build -f ./Dockerfile -t trendy-tracker .

docker-run:
	docker run -p 8080:8080  trendy-tracker 

docker-set:
	docker cp /usr/lib/chromium-browser/chromedriver trendy-tracker:/usr/local/bin/chromedriver
