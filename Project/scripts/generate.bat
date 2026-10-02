@echo off
setlocal
cd /d "%~dp0\.."
set COUNT=%1
if "%COUNT%"=="" set COUNT=50000
set SEED=%2
if "%SEED%"=="" set SEED=2520030477
call scripts\compile.bat
if errorlevel 1 exit /b 1
java -cp build\classes digitallibrary.storage.DatasetGenerator %COUNT% data\resources.txt %SEED%
