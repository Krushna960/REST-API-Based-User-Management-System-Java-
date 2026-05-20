@echo off
REM Place mysql-connector-j-*.jar in the lib folder before running.

if not exist "out" mkdir out

javac -encoding UTF-8 -cp ".;lib\*" -d out src\*.java
if errorlevel 1 (
    echo Compilation failed. Check JDK and mysql-connector-j in lib\
    exit /b 1
)

java -cp "out;lib\*" Main
