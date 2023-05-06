#!/usr/bin/env bash
#
# Startup script for spring boot uber jar under centos, macos.

NAME=$(echo $(basename $0) | sed -e 's/^[SK][0-9]*//' -e 's/\.sh$//')

ITEM_NAME=${NAME}
JAVA=/home/ubuntu/soft/third/java/bin/java
JAVA_OPTIONS="$JAVA_OPTIONS -Dappname=$ITEM_NAME"
TMPDIR=/duitang/tmp/${ITEM_NAME}/
JAVA_VERSION=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}')

LOGS="/duitang/logs/usr/$ITEM_NAME"
APP_PID="${LOGS}/${ITEM_NAME}.pid"

APP_HOME="/duitang/dist/app/main/${ITEM_NAME}"

SPRING_PROFILE="$DT_APP_ENV"

mkdir -p ${LOGS} ${TMPDIR}

usage() {
  echo "Usage: ${0##*/} [-d] {start|stop|run|restart|check} "
  exit 1
}

[ $# -gt 0 ] || usage

##################################################
# Some utility functions
##################################################
running() {
  if [ -f "$1" ]; then
    local PID=$(cat "$1" 2>/dev/null) || return 1
    kill -0 "$PID" 2>/dev/null
    return
  fi
  rm -f "$1"
  return 1
}

DEBUG=0
while [[ $1 = -* ]]; do
  case $1 in
  -d) DEBUG=1 ;;
  esac
  shift
done
ACTION=$1
shift

##################################################
# Must define JAVA arg
##################################################
if [ -z "$JAVA" ]; then
  JAVA=$(which java)
fi

if [ -z "$JAVA" ]; then
  echo "Cannot find a Java JDK cfg. Please set JAVA." 2>&2
  exit 1
fi

#####################################################
# Must define LOGS
#####################################################
if [ -z "$LOGS" ]; then
  echo "Cannot find a LOGS cfg. Please set LOGS." 2>&2
  exit 1
fi

JAVA_OPTIONS=("$JAVA_OPTIONS" "-Djetty.logs=$LOGS")
#####################################################
# Add jetty properties to Java VM options.
#####################################################

JAVA_OPTIONS=("$JAVA_OPTIONS" "-Djava.io.tmpdir=$TMPDIR")

# This is how the Jetty server will be started

## Memory Options##
MEM_OPTS="-Xms128m -Xmx128m"
MEM_OPTS="$MEM_OPTS -XX:+AlwaysPreTouch"
MEM_OPTS="$MEM_OPTS -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=512m"

ENV_OPTS="-Dspring.profiles.active=${SPRING_PROFILE}"

## GC Options##
GC_OPTS="-XX:MaxGCPauseMillis=50"

# System.gc() 使用CMS算法
GC_OPTS="$GC_OPTS -XX:+ExplicitGCInvokesConcurrent"

# 根据应用的对象生命周期设定，减少事实上的老生代对象在新生代停留时间，加快YGC速度
GC_OPTS="$GC_OPTS -XX:MaxTenuringThreshold=3"

# GC 日志
GC_LOG_OPTS="-Xlog:gc*=debug,stringdedup*=debug,gc+ergo*=trace,gc+age=trace,gc+phases=trace,gc+humongous=trace,safepoint=debug:${LOGS}/gc.log:level,tags,time,uptime,pid:filecount=5,filesize=100M"

## Optimization Options##
# 更换 random 为 urandom 避免高并发加密证书的时候通信的时带来的阻塞, 例如 mysql
OPTIMIZE_OPTS="-XX:AutoBoxCacheMax=20000 -Djava.security.egd=file:/dev/./urandom"
# 指定编码 UTF-8, 其实 java 18 之后默认就是 UTF-8 了
OTHER_OPTS="-Djava.net.preferIPv4Stack=true -Dfile.encoding=UTF-8 -Dzookeeper.sasl.client=false"
# dns 缓存默认是 30s, 修改 10s, 有利于 k8s 环境
OTHER_OPTS="${OTHER_OPTS} -Dnetworkaddress.cache.ttl=10"

# open deep reflection
OTHER_OPTS="${OTHER_OPTS} --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.io=ALL-UNNAMED --add-opens java.base/java.math=ALL-UNNAMED --add-opens java.base/java.net=ALL-UNNAMED --add-opens java.base/java.nio=ALL-UNNAMED --add-opens java.base/java.security=ALL-UNNAMED --add-opens java.base/java.text=ALL-UNNAMED --add-opens java.base/java.time=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED --add-opens java.base/jdk.internal.access=ALL-UNNAMED --add-opens java.base/jdk.internal.misc=ALL-UNNAMED"

## Trouble shooting Options##
SHOOTING_OPTS="-XX:+PrintCommandLineFlags -XX:-OmitStackTraceInFastThrow -XX:ErrorFile=${LOGS}/hs_err_%p.log"
SHOOTING_OPTS="${SHOOTING_OPTS} -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=${LOGS}/dump_%p.bin"

JAVA_OPTIONS="$JAVA_OPTIONS $MEM_OPTS $GC_OPTS $GC_LOG_OPTS $OPTIMIZE_OPTS $SHOOTING_OPTS $OTHER_OPTS $ENV_OPTS"

#####################################################

STARTER=${APP_HOME}/${NAME}.jar

RUN_ARGS=(${JAVA_OPTIONS[@]} -jar "$STARTER" ${APP_ARGS[*]})

RUN_CMD=("$JAVA" ${RUN_ARGS[@]})

#####################################################
# Comment these out after you're happy with what
# the script is doing.
#####################################################
if ((DEBUG)); then
  echo "APP_HOME     =  $APP_HOME"
  echo "APP_PID      =  $APP_PID"
  echo "STARTER    =  $STARTER"
  echo "APP_ARGS     =  ${APP_ARGS[*]}"
  echo "JAVA_OPTIONS   =  ${JAVA_OPTIONS[*]}"
  echo "JAVA           =  $JAVA"
  echo "RUN_CMD        =  ${RUN_CMD[@]}"
fi

echo ""

##################################################
# Do the action
##################################################
case "$ACTION" in
start)
  echo -n "Starting ${ITEM_NAME}: "

  if running ${APP_PID}; then
    echo "Already Running $(cat ${APP_PID})!"
    exit 1
  fi

  "${RUN_CMD[@]}" >"$LOGS/stdout.log" 2>&1 &
  disown $!
  echo $! >"$APP_PID"
  echo "deploy $(date)" >>"$LOGS/start.log"
  echo OK

  ;;

stop)
  echo -n "Stopping ${ITEM_NAME}: "

  if [ ! -f "$APP_PID" ]; then
    echo "ERROR: no pid found at $APP_PID"
    exit 1
  fi

  PID=$(cat "$APP_PID" 2>/dev/null)
  if [ -z "$PID" ]; then
    echo "ERROR: no pid id found in $APP_PID"
    exit 1
  fi
  kill "$PID" 2>/dev/null

  TIMEOUT=10
  while running $APP_PID; do
    if ((TIMEOUT-- == 0)); then
      kill -KILL "$PID" 2>/dev/null
    fi

    sleep 1
  done

  rm -f "$APP_PID"
  echo OK

  ;;

restart)
  JETTY_SH=$0
  "$JETTY_SH" stop "$@"
  "$JETTY_SH" start "$@"
  ;;

\
  run)
  echo "Running $NAME: "

  if running "$APP_PID"; then
    echo Already Running $(cat "$APP_PID")!
    exit 1
  fi

  exec "${RUN_CMD[@]}"

  ;;

status)
  echo "Checking arguments to Jetty: "
  echo "APP_HOME     =  $APP_HOME"
  echo "APP_PID      =  $APP_PID"
  echo "STARTER    =  $STARTER"
  echo "LOGS     =  $LOGS"
  echo "JAVA           =  $JAVA"
  echo "JAVA_OPTIONS   =  ${JAVA_OPTIONS[*]}"
  echo "APP_ARGS     =  ${APP_ARGS[*]}"
  echo "RUN_CMD        =  ${RUN_CMD[*]}"
  echo

  if running "$APP_PID"; then
    echo "Jetty running pid=$(<"$APP_PID")"
    exit 0
  fi
  exit 1

  ;;

*)
  usage

  ;;
esac

exit 0
