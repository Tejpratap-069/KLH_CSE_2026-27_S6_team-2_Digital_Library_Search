@echo off
setlocal EnableExtensions EnableDelayedExpansion
cd /d "%~dp0\.."
if not exist build\classes mkdir build\classes
> build\sources.txt type nul
for /r backend\src %%F in (*.java) do (
  set "SOURCE=%%~fF"
  set "SOURCE=!SOURCE:\=/!"
  >> build\sources.txt echo "!SOURCE!"
)
javac -encoding UTF-8 -d build\classes @build\sources.txt
if errorlevel 1 exit /b 1
echo Compiled production sources into build\classes
