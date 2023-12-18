import os
import logging
from flask import Flask
import subprocess
import requests
import time

app = Flask(__name__)

# 로깅 설정
logging.basicConfig(level=logging.INFO, encoding="utf-8")


@app.route("/webhook", methods=["GET", "POST"])
def handle_webhook():
    try:
        repository = "jinsujj/trendy-tracker-backend:latest"
        username = os.getenv("USERNAME")
        password = os.getenv("PASSWORD")

        # Docker 이미지 정보
        logging.info(f"********* docker image **********\n{repository}\n{'*' * 40}")

        # Docker hub 로그인
        login_command = f"docker login --username='{username}' --password='{password}'"
        run_subprocess(login_command)

        # 새로운 이미지 Pull
        pull_command = f"docker pull {repository}"
        run_subprocess(pull_command)

        # 첫번째 Spring 프로젝트 update
        stop_and_start_service()

        # 첫 번째 Spring 프로젝트의 상태를 폴링하여 확인
        check_health("http://localhost:8080/api/appInfo/health-check")

        # 두 번째 Spring 프로젝트 시작
        start_command2 = "docker-compose -f ./docker-compose.yml up -d trendy_tracker2"
        run_subprocess(start_command2)

        logging.info(f"Docker 컨테이너 빌드 및 실행 완료: {repository}")
        return f"Docker 컨테이너 빌드 및 실행 완료: {repository}", 200
    except Exception as e:
        error_message = f"오류 발생: {str(e)}"
        logging.error(error_message)
        return error_message, 500


def run_subprocess(command):
    logging.info(f"************************\n{command}\n{'*' * 40}")
    process = subprocess.Popen(
        command, shell=True, stdout=subprocess.PIPE, stderr=subprocess.PIPE
    )
    output, error = process.communicate()
    if process.returncode != 0:
        raise Exception(f"실행 오류: {error.decode()}")


def stop_and_start_service():
    # 첫 번째 Spring 프로젝트 중지
    stop_command = "docker-compose stop trendy_tracker"
    run_subprocess(stop_command)

    # 첫 번째 Spring 프로젝트 다시 시작
    start_command = "docker-compose -f ./docker-compose.yml up -d trendy_tracker"   
    run_subprocess(start_command)


def check_health(api_url):
    max_attempts = 60  # 최대 시도 횟수
    interval_seconds = 5  # 폴링 간격 (초)

    for _ in range(max_attempts):
        try:
            response = requests.get(api_url)
            if response.status_code == 200:
                # API 정상 응답을 받으면 종료
                logging.info("API 정상 동작 확인")
                return
        except Exception as e:
            logging.warning(f"API 폴링 중 오류 발생: {str(e)}")

        # 정상 응답이 아닌 경우 잠시 대기 후 다시 시도
        time.sleep(interval_seconds)

    # 최대 시도 횟수를 넘어갈 경우 예외 처리
    raise Exception("API가 정상적으로 동작하지 않습니다.")


if __name__ == "__main__":
    app.run(
        host="0.0.0.0",
        port=8000,
        use_reloader=False,
        use_debugger=False,
        threaded=True,
    )
