#!/bin/sh

JAVA_HOME="${JAVA_HOME:-}"
if [ -n "$JAVA_HOME" ]; then
  JAVACMD="$JAVA_HOME/bin/java"
else
  JAVACMD="$(command -v java)"
fi

if [ ! -x "$JAVACMD" ]; then
  echo "Java is required to run Maven Wrapper." >&2
  exit 1
fi

WRAPPER_JAR="$(dirname "$0")/.mvn/wrapper/maven-wrapper.jar"
if [ ! -f "$WRAPPER_JAR" ]; then
  echo "Maven wrapper jar is missing." >&2
  echo "Download it with: curl -o .mvn/wrapper/maven-wrapper.jar https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.3.2/maven-wrapper-3.3.2.jar" >&2
  exit 1
fi

MAVEN_USER_HOME="${MAVEN_USER_HOME:-$(pwd)/.m2}"
exec "$JAVACMD" -classpath "$WRAPPER_JAR" "-Dmaven.multiModuleProjectDirectory=$(pwd)" "-Dmaven.user.home=$MAVEN_USER_HOME" org.apache.maven.wrapper.MavenWrapperMain "-Dmaven.repo.local=$MAVEN_USER_HOME/repository" "$@"
