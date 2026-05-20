@echo off
REM Run original JDBC console app (place mysql-connector-j jar in ..\lib\)

cd /d "%~dp0\.."
if not exist "out-legacy" mkdir out-legacy

javac -encoding UTF-8 -cp ".;lib\*" -d out-legacy legacy-console\*.java
if errorlevel 1 (
    echo Compilation failed. Check JDK and mysql-connector-j in lib\
    exit /b 1
)

java -cp "out-legacy;lib\*" Main
