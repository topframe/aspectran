#!/bin/sh

ARG0="$0"
while [ -h "$ARG0" ]; do
  ls=$(ls -ld "$ARG0")
  link=$(expr "$ls" : '.*-> \(.*\)$')
  if expr "$link" : '/.*' >/dev/null; then
    ARG0="$link"
  else
    ARG0=$(dirname "$ARG0")/"$link"
  fi
done
PRG_DIR=$(dirname "$ARG0")
BASE_DIR="$PRG_DIR/.."
BASE_DIR="$(
  cd "$BASE_DIR" || exit
  pwd
)"
if [ -z "$JAVA_HOME" ]; then
  JAVA_BIN="$(command -v java 2>/dev/null || type java 2>&1)"
  while [ -h "$JAVA_BIN" ]; do
    ls=$(ls -ld "$JAVA_BIN")
    link=$(expr "$ls" : '.*-> \(.*\)$')
    if expr "$link" : '/.*' >/dev/null; then
      JAVA_BIN="$link"
    else
      JAVA_BIN="$(dirname "$JAVA_BIN")/$link"
    fi
  done
  test -x "$JAVA_BIN" && JAVA_HOME="$(dirname "$JAVA_BIN")"
  test ".$JAVA_HOME" != . && JAVA_HOME=$(
    cd "$JAVA_HOME/.." >/dev/null || exit
    pwd
  )
else
  JAVA_BIN="$JAVA_HOME/bin/java"
fi
while [ ".$1" != . ]; do
  case "$1" in
  --raw-mode)
    ASPECTRAN_SHELL_CLASS="com.aspectran.shell.AspectranShell"
    shift
    continue
    ;;
  --debug)
    LOGGING_CONFIG="$BASE_DIR/config/logback-debug.xml"
    shift
    continue
    ;;
  *)
    break
    ;;
  esac
done
if [ -z "$JAVA_OPTS" ]; then
  JAVA_OPTS="-Xms256m -Xmx1024m"
fi
CLASSPATH="$BASE_DIR/lib/*"
if [ -z "$LOGGING_CONFIG" ] || [ ! -f "$LOGGING_CONFIG" ]; then
  LOGGING_CONFIG="$BASE_DIR/config/logback.xml"
fi
TMP_DIR="$BASE_DIR/temp"
if [ -z "$ASPECTRAN_SHELL_CLASS" ]; then
  ASPECTRAN_SHELL_CLASS="com.aspectran.shell.jline.JLineAspectranShell"
fi
ASPECTRAN_CONFIG="$BASE_DIR/config/aspectran-config.apon"

"$JAVA_BIN" \
  $JAVA_OPTS \
  -classpath "$CLASSPATH" \
  -Djava.io.tmpdir="$TMP_DIR" \
  -Djava.awt.headless=true \
  -Djava.net.preferIPv4Stack=true \
  -Dlogback.configurationFile="$LOGGING_CONFIG" \
  -Daspectran.basePath="$BASE_DIR" \
  $ASPECTRAN_OPTS \
  $ASPECTRAN_SHELL_CLASS \
  "$ASPECTRAN_CONFIG"
