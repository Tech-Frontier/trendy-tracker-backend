import os
import logging
from flask import Flask, request
import subprocess

app = Flask(__name__)

# 로깅 설정
logging.basicConfig(level=logging.INFO, encoding="utf-8")  # utf-8 인코딩 설정


@app.route("/webhook", methods=["GET", "POST"])
def handle_webhook():
    repository = "jinsujj/trendy-tracker-backend:latest"

    username = f"'{os.getenv('USERNAME')}'"
    password = f"'{os.getenv('PASSWORD')}'"

    login_command = f"docker login --username={username} --password={password}"
    pull_command = f"docker pull {repository}"
    run_command = f"docker-compose up -d"

    # Docker 이미지 정보
    logging.info("********* docker image info **********")
    logging.info(str(repository))
    logging.info("**************************************")

    # Docker hub 로그인
    logging.info("********* docker hub login **********")
    logging.info(str(login_command))
    logging.info("**************************************")
    login_process = subprocess.Popen(
        login_command, shell=True, stdout=subprocess.PIPE, stderr=subprocess.PIPE
    )
    login_output, login_error = login_process.communicate()

    if login_process.returncode != 0:
        error_message = f"Docker hub 로그인 중 오류 발생: {login_error.decode()}"
        logging.error(error_message)
        return error_message, 500

    # Docker 이미지 Pull
    logging.info("********** docker pull ****************")
    logging.info(str(pull_command))
    logging.info("**************************************")

    pull_process = subprocess.Popen(
        pull_command, shell=True, stdout=subprocess.PIPE, stderr=subprocess.PIPE
    )
    pull_output, pull_error = pull_process.communicate()

    if pull_process.returncode != 0:
        error_message = f"Docker 이미지 Pull 중 오류 발생: {pull_error.decode()}"
        logging.error(error_message)
        return error_message, 500

    # Docker 이미지 빌드 및 실행
    logging.info("**************************")
    logging.info(run_command)
    logging.info("**************************")
    run_process = subprocess.Popen(
        run_command, shell=True, stdout=subprocess.PIPE, stderr=subprocess.PIPE
    )
    run_output, run_error = run_process.communicate()

    if run_process.returncode != 0:
        error_message = f"Docker 컨테이너 실행 중 오류 발생: {run_error.decode()}"
        logging.error(error_message)
        return error_message, 500

    logging.info(f"Docker 컨테이너 빌드 및 실행 완료: {repository}")
    return f"Docker 컨테이너 빌드 및 실행 완료: {repository}", 200


if __name__ == "__main__":
    app.run(
        host="0.0.0.0",
        port=8000,
        use_reloader=False,
        use_debugger=False,
        threaded=True,
    )
