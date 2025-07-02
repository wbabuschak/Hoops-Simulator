@echo off

REM Move to the folder where this batch file lives
cd /d "%~dp0"

REM Delete old classes
if exist bin rmdir /s /q bin
mkdir bin

echo Compiling...
javac -d bin src\*.java

if %errorlevel% neq 0 (
    echo Compilation failed.
    pause
    exit /b %errorlevel%
)

REM Copy ctrl folder resources into bin so they're available at runtime
xcopy /e /i /y src\ctrl bin\ctrl

echo Running...
start javaw -cp bin Main


