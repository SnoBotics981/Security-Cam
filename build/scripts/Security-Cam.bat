@if "%DEBUG%" == "" @echo off
@rem ##########################################################################
@rem
@rem  Security-Cam startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%..

@rem Add default JVM options here. You can also use JAVA_OPTS and SECURITY_CAM_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS="-Djava.library.path=/home/pi/Security-Cam/build/opencv"

@rem Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if "%ERRORLEVEL%" == "0" goto init

echo.
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_EXE%" goto init

echo.
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:init
@rem Get command-line arguments, handling Windows variants

if not "%OS%" == "Windows_NT" goto win9xME_args

:win9xME_args
@rem Slurp the command line arguments.
set CMD_LINE_ARGS=
set _SKIP=2

:win9xME_args_slurp
if "x%~1" == "x" goto execute

set CMD_LINE_ARGS=%*

:execute
@rem Setup the command line

set CLASSPATH=%APP_HOME%\lib\SecurityCamera.jar;%APP_HOME%\lib\NetworkTables-3.1.7-arm-raspbian.jar;%APP_HOME%\lib\cscore-1.0.2-arm-raspbian.jar;%APP_HOME%\lib\opencv-java-3.1.0.jar;%APP_HOME%\lib\commons-io-2.6.jar;%APP_HOME%\lib\json-20180813.jar;%APP_HOME%\lib\mbassador-1.3.2.jar;%APP_HOME%\lib\jade4j-1.2.7.jar;%APP_HOME%\lib\wpilibj-java-2019.1.1-beta-2.jar;%APP_HOME%\lib\wpiutil-java-2019.1.1-beta-2.jar;%APP_HOME%\lib\ntcore-java-2019.1.1-beta-2.jar;%APP_HOME%\lib\javax.el-api-3.0.1-b06.jar;%APP_HOME%\lib\juel-impl-2.2.7.jar;%APP_HOME%\lib\juel-spi-2.2.7.jar;%APP_HOME%\lib\commons-jexl-2.1.1.jar;%APP_HOME%\lib\commons-collections-3.2.2.jar;%APP_HOME%\lib\commons-lang3-3.4.jar;%APP_HOME%\lib\concurrentlinkedhashmap-lru-1.4.2.jar;%APP_HOME%\lib\pegdown-1.6.0.jar;%APP_HOME%\lib\commons-logging-1.1.1.jar;%APP_HOME%\lib\parboiled-java-1.1.7.jar;%APP_HOME%\lib\parboiled-core-1.1.7.jar;%APP_HOME%\lib\asm-5.0.3.jar;%APP_HOME%\lib\asm-tree-5.0.3.jar;%APP_HOME%\lib\asm-analysis-5.0.3.jar;%APP_HOME%\lib\asm-util-5.0.3.jar

@rem Execute Security-Cam
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %SECURITY_CAM_OPTS%  -classpath "%CLASSPATH%" Main %CMD_LINE_ARGS%

:end
@rem End local scope for the variables with windows NT shell
if "%ERRORLEVEL%"=="0" goto mainEnd

:fail
rem Set variable SECURITY_CAM_EXIT_CONSOLE if you need the _script_ return code instead of
rem the _cmd.exe /c_ return code!
if  not "" == "%SECURITY_CAM_EXIT_CONSOLE%" exit 1
exit /b 1

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:omega
