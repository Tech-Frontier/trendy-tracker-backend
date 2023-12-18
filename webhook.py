import os
import logging
from flask import Flask
import subprocess

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
        logging.info(
            f"********* docker image info **********\n{repository}\n{'*' * 40}"
        )
        # Docker hub 로그인
        login_command = f"docker login --username='{username}' --password='{password}'"
        run_subprocess(login_command)

        # Docker 이미지 Pull
        pull_command = f"docker pull {repository}"
        run_subprocess(pull_command)

        # Docker 이미지 Down
        down_command = "docker-compose stop trendy_tracker"
        run_subprocess(down_command)

        # Docker 이미지 Run
        run_command = "docker-compose -f ./docker-compose.yml up -d"
        run_subprocess(run_command)

        logging.info(f"Docker 컨테이너 빌드 및 실행 완료: {repository}")
        return f"Docker 컨테이너 빌드 및 실행 완료: {repository}", 200
    except Exception as e:
        error_message = f"오류 발생: {str(e)}"
        logging.error(error_message)
        return error_message, 500


def run_subprocess(command):
    logging.info(f"********** 실행 명령어 **********\n{command}\n{'*' * 40}")
    result = subprocess.run(
        command,
        shell=True,
        stdout=subprocess.PIPE,
        stderr=subprocess.PIPE,
        encoding="utf-8",
    )
    if result.returncode != 0:
        error_message = f"명령어 실행 중 오류 발생: {result.stderr}"
        logging.error(error_message)
        raise Exception(error_message)


if __name__ == "__main__":
    app.run(
        host="0.0.0.0",
        port=8000,
        use_reloader=False,
        use_debugger=False,
        threaded=True,
    )
