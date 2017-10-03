@echo off

call mvn package

cd docker
call build.bat
cd ..
