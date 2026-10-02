@echo off
setlocal EnableExtensions EnableDelayedExpansion
cd /d "%~dp0"

set "B64=Digital-Library-Search-System-FINAL.tar.xz.b64"
set "ARCHIVE=Digital-Library-Search-System-FINAL.tar.xz"
set "OUTDIR=Digital-Library-Search-System-FINAL"

echo.
echo ================================================
echo   DIGITAL LIBRARY SEARCH SYSTEM - RESTORE
echo ================================================
echo.

if exist "%B64%" del /q "%B64%"
if exist "%ARCHIVE%" del /q "%ARCHIVE%"

for /L %%I in (0,1,8) do (
    set "N=0%%I"
    set "N=!N:~-2!"
    if not exist "archive_part_!N!.b64" (
        echo ERROR: archive_part_!N!.b64 is missing.
        pause
        exit /b 1
    )
    type "archive_part_!N!.b64" >> "%B64%"
)

echo Reconstructing project archive...
certutil -f -decode "%B64%" "%ARCHIVE%" >nul
if errorlevel 1 (
    echo ERROR: Failed to decode the project archive.
    pause
    exit /b 1
)

if not exist "%OUTDIR%" mkdir "%OUTDIR%"

echo Extracting project...
tar -xf "%ARCHIVE%" -C "%OUTDIR%"
if errorlevel 1 (
    echo ERROR: Extraction failed.
    echo Make sure your Windows installation provides tar.exe.
    pause
    exit /b 1
)

del /q "%B64%" >nul 2>&1
del /q "%ARCHIVE%" >nul 2>&1

echo.
echo ================================================
echo   PROJECT RESTORED SUCCESSFULLY
echo ================================================
echo.
echo Folder:
echo   %CD%\%OUTDIR%
echo.
echo Next steps:
echo   1. Open Digital-Library-Search-System-FINAL
echo   2. Run run.bat
echo   3. Open http://localhost:8080
echo.
pause
