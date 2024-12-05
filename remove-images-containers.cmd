@echo off
@REM Remove containers
FOR /F "tokens=*" %%A IN ('docker ps -a --filter "name=hackathon-dwi.*" -q') DO (
    docker rm -f %%A
)

@REM Remove images
FOR /F "tokens=*" %%A IN ('docker images --format "{{.Repository}}:{{.Tag}}" ^| findstr "hackathon-dwi"') DO (
    docker rmi -f %%A
)

@REM Remove volumes
FOR /F "tokens=2 delims= " %%A IN ('docker volume ls ^| findstr "hackathon-dwi"') DO (
    docker volume rm %%A
)
