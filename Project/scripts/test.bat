@echo off
setlocal EnableExtensions EnableDelayedExpansion
cd /d "%~dp0\.."
if exist build\classes rmdir /s /q build\classes
mkdir build\classes
> build\test-sources.txt type nul
for /r backend\src %%F in (*.java) do (
  set "SOURCE=%%~fF"
  set "SOURCE=!SOURCE:\=/!"
  >> build\test-sources.txt echo "!SOURCE!"
)
for /r backend\test %%F in (*.java) do (
  set "SOURCE=%%~fF"
  set "SOURCE=!SOURCE:\=/!"
  >> build\test-sources.txt echo "!SOURCE!"
)
javac -encoding UTF-8 -d build\classes @build\test-sources.txt
if errorlevel 1 exit /b 1
java -ea -cp build\classes digitallibrary.AllTests
if errorlevel 1 exit /b 1
python scripts\check_frontend.py
if errorlevel 1 exit /b 1
python scripts\check_project.py
if errorlevel 1 exit /b 1
python scripts\check_windows_batch_paths.py
if errorlevel 1 exit /b 1
python scripts\validate_dataset.py
