@echo off

copy ..\target\demo.war .\ROOT.war

set DOCKER_REPO=descom
set IMAGE_NAME=iac-webapp
set VERSION=0.0.2

@FOR /f %%i IN ('git rev-parse --short HEAD') DO SET GIT_COMMIT_HASH=%%i
set DOCKER_VERSION_TAG=%IMAGE_NAME%:%VERSION%-%GIT_COMMIT_HASH%
set DOCKER_LATEST_TAG=%IMAGE_NAME%:latest
set DOCKER_REPO_VERSION_TAG=%DOCKER_REPO%/%DOCKER_VERSION_TAG%
set DOCKER_REPO_LATEST_TAG=%DOCKER_REPO%/%DOCKER_LATEST_TAG%

@FOR /f %%i IN ('docker-machine ip default') DO SET NO_PROXY=%%i
@FOR /f "tokens=*" %%i IN ('docker-machine env default') DO @%%i

echo build docker image with tags: %DOCKER_VERSION_TAG%, %DOCKER_LATEST_TAG%, %DOCKER_REPO_VERSION_TAG%, %DOCKER_REPO_LATEST_TAG%

docker build -t %DOCKER_VERSION_TAG% -t %DOCKER_LATEST_TAG% -t %DOCKER_REPO_VERSION_TAG% -t %DOCKER_REPO_LATEST_TAG% .

echo and now push it...

docker push %DOCKER_REPO_VERSION_TAG%
docker push %DOCKER_REPO_LATEST_TAG%

echo done
