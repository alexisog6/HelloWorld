@echo off

docker-machine env default 2> nul
if %ERRORLEVEL% == 1 goto :LOCAL_DOCKER

@FOR /f "tokens=*" %%i IN ('docker-machine env default --shell cmd') DO @%%i
set NO_PROXY=192.168.99.100

:LOCAL_DOCKER

echo call mvn clean
call mvn clean

echo call mvn install
call mvn install

pause
