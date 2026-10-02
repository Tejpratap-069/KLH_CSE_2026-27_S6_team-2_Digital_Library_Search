@echo off
setlocal
cd /d "%~dp0"
call scripts\compile.bat
if errorlevel 1 (
  echo Compilation failed.
  pause
  exit /b 1
)
set PORT=%1
if "%PORT%"=="" set PORT=8080
echo.
echo Starting Digital Library Search System on http://localhost:%PORT%
echo Press Ctrl+C to stop the server.
echo.
java -cp build\classes digitallibrary.Main %PORT%
pause
