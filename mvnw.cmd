@echo off
setlocal

set "BASEDIR=%~dp0"
if "%BASEDIR:~-1%"=="\" set "BASEDIR=%BASEDIR:~0,-1%"
set "WRAPPER_JAR=%BASEDIR%\.mvn\wrapper\maven-wrapper.jar"

if not defined JAVA_HOME (
  set "JAVACMD=java"
) else (
  set "JAVACMD=%JAVA_HOME%\bin\java.exe"
)

if not exist "%WRAPPER_JAR%" (
  echo Maven wrapper jar is missing. >&2
  echo Download it with: curl -o .mvn/wrapper/maven-wrapper.jar https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.3.2/maven-wrapper-3.3.2.jar >&2
  exit /b 1
)

if not defined MAVEN_USER_HOME (
  set "MAVEN_USER_HOME=%BASEDIR%\.m2"
)

"%JAVACMD%" -classpath "%WRAPPER_JAR%" "-Dmaven.multiModuleProjectDirectory=%BASEDIR%" "-Dmaven.user.home=%MAVEN_USER_HOME%" org.apache.maven.wrapper.MavenWrapperMain "-Dmaven.repo.local=%MAVEN_USER_HOME%\repository" %*
exit /b %ERRORLEVEL%
