@echo off
setlocal enabledelayedexpansion
cd /d "%~dp0"
echo ==========================================
echo   Compilando Kiko Companion
echo ==========================================
echo.

rem --- JDK 21 portable dentro de la carpeta del mod ---
set "JDKDIR="
for /d %%D in ("%~dp0jdk\jdk-21*") do set "JDKDIR=%%~fD"
rem Reusa el JDK de otra carpeta del mod si ya lo bajaste antes
if not defined JDKDIR for /d %%D in ("%~dp0..\kiko-mod*") do if exist "%%~fD\jdk" for /d %%J in ("%%~fD\jdk\jdk-21*") do set "JDKDIR=%%~fJ"
if not defined JDKDIR (
    echo Bajando JDK 21 de Adoptium, aguanta tantito...
    echo DOWNLOADING_JDK > compilar_log.txt
    if not exist jdk mkdir jdk
    curl -L -f -o jdk\jdk21.zip "https://api.adoptium.net/v3/binary/latest/21/ga/windows/x64/jdk/hotspot/normal/eclipse" >> compilar_log.txt 2>&1
    if errorlevel 1 (
        echo [ERROR] No se pudo bajar el JDK
        echo JDK_DOWNLOAD_FAILED >> compilar_log.txt
        pause
        exit /b 1
    )
    echo Descomprimiendo...
    tar -xf jdk\jdk21.zip -C jdk >> compilar_log.txt 2>&1
    del jdk\jdk21.zip
    for /d %%D in ("%~dp0jdk\jdk-21*") do set "JDKDIR=%%~fD"
)
if not defined JDKDIR (
    echo [ERROR] No encontre el JDK descomprimido
    echo JDK_EXTRACT_FAILED >> compilar_log.txt
    pause
    exit /b 1
)

set "JAVA_HOME=%JDKDIR%"
set "PATH=%JDKDIR%\bin;%PATH%"
echo Usando Java en %JAVA_HOME%
"%JAVA_HOME%\bin\java" -version > compilar_log.txt 2>&1
echo. >> compilar_log.txt

echo Compilando (la primera vez tarda varios minutos)...
call gradlew.bat build --no-daemon >> compilar_log.txt 2>&1
if errorlevel 1 (
    echo [ERROR] Fallo la compilacion. Revisa compilar_log.txt
    echo BUILD_FAILED >> compilar_log.txt
    timeout /t 15
    exit /b 1
)

for %%F in (build\libs\kiko-companion-*.jar) do (
    echo %%~nF | findstr /c:"sources" >nul || copy /y "%%F" "%~dp0%%~nxF" >nul
)
echo BUILD_OK >> compilar_log.txt
echo.
echo Listo! El mod quedo en esta carpeta
timeout /t 15
