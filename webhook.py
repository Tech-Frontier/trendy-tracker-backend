import os
import logging
from flask import Flask, request
import subprocess

app = Flask(__name__)

# 로깅 설정
logging.basicConfig(level=logging.INFO, encoding="utf-8")  # utf-8 인코딩 설정

# 상태 변수
processing_request = False


@app.route("/webhook", methods=["GET", "POST"])
def handle_webhook():
    global processing_request
    if processing_request:
        return "다른 요청을 무시 중입니다.", 503

    processing_request = True

    try:
        repository = "jinsujj/trendy-tracker-backend:latest"

        username = f"'{os.getenv('USERNAME')}'"
        password = f"'{os.getenv('PASSWORD')}'"

        login_command = f"docker login --username={username} --password={password}"
        pull_command = f"docker pull {repository}"
        down_command = f"docker-compose stop trendy_tracker "
        network_down_command = f"docker network prune -f"
        run_command = f"docker-compose -f ./docker-compose.yml up -d"

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

        # Docker 이미지 Down
        logging.info("**************************")
        logging.info(down_command)
        logging.info("**************************")
        down_process = subprocess.Popen(
            down_command, shell=True, stdout=subprocess.PIPE, stderr=subprocess.PIPE
        )
        down_output, down_error = down_process.communicate()

        # Docker 네트워크 Down
        logging.info("**************************")
        logging.info(network_down_command)
        logging.info("**************************")
        network_down_process = subprocess.Popen(
            network_down_command,
            shell=True,
            stdout=subprocess.PIPE,
            stderr=subprocess.PIPE,
        )
        network_down_output, down_error = network_down_process.communicate()

        if network_down_process.returncode != 0:
            error_message = f"Docker 네트워크 종료 중 오류 발생: {down_error.decode()}"
            logging.error(error_message)
            return error_message, 500

        # Docker 이미지 Run
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
    finally:
        # 처리가 완료되면 상태 변수를 다시 False로 설정하여 다른 요청을 받을 수 있도록 함
        processing_request = False


if __name__ == "__main__":
    app.run(
        host="0.0.0.0",
        port=8000,
        use_reloader=False,
        use_debugger=False,
        threaded=True,
    )
