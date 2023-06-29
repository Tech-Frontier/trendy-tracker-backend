                                                                                                   
from flask import Flask, request
import subprocess
import yaml
import json

app = Flask(__name__)

@app.route('/webhook', methods=['POST'])
def handle_webhook():
    payload = request.get_json()
    
    repository = payload['repository']['repo_name']
    tag = payload['push_data']['tag']
    
    # application.yml 파일 로드
    with open('./src/main/resources/application.yml', 'r') as file:
        config = yaml.safe_load(file)
    
    username = config['docker_hub']['username']
    password = config['docker_hub']['password']
    
    login_command = f'docker login --username={username} --password={password}'
    pull_command = f'docker pull {repository}:{tag}'
    
    # Docker 이미지 정보 
    print("********* docker image info **********")
    print(str(repository)+":"+str(tag))
    print("**************************************")

    # Docker hub 로그인 
    print("********* docker hub login **********")
    print(str(login_command))
    print("**************************************")
    login_process = subprocess.Popen(login_command, shell=True, stdout=subprocess.PIPE, stderr=subprocess.PIPE)
    login_output, login_error = login_process.communicate()

    if login_process.returncode != 0:
        error_message = f'Docker hub 로그인 중 오류 발생: {login_error.decode()}'
        print(error_message)
        return error_message, 500
    
    
    # Docker 이미지 Pull
    print("********** docker pull ****************")
    print(str(pull_command))
    print("**************************************")
    
    pull_process = subprocess.Popen(pull_command, shell=True, stdout=subprocess.PIPE, stderr=subprocess.PIPE)
    pull_output, pull_error = pull_process.communicate()

    if pull_process.returncode != 0:
        error_message = f'Docker 이미지 Pull 중 오류 발생: {pull_error.decode()}'
        print(error_message)
        return error_message, 500
    
    

    # Docker 이미지 빌드 및 실행
    run_process = f'make docker-run'
    print("**************************")
    print(run_process)
    print("**************************")
    run_process = subprocess.Popen(run_process, shell=True, stdout=subprocess.PIPE, stderr=subprocess.PIPE)
    run_output, run_error = run_process.communicate()

    if run_error.returncode != 0:
        error_message = f'Docker 컨테이너 실행 중 오류 발생: {run_error.decode()}'
        print(error_message)
        return error_message, 500

    print(f'Docker 컨테이너 빌드 및 실행 완료: {repository}:{tag}')
    return f'Docker 컨테이너 빌드 및 실행 완료: {repository}:{tag}', 200


if __name__ == "__main__":  # pragma: no cover
    app.run(
        host="0.0.0.0",
        port=8000,
        use_reloader=False,
        use_debugger=False,
        threaded=True, 
    )
