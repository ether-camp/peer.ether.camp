#!/bin/bash

export APP_ENV="frontier"
export APP_SRC_DIR="/home/peer.ether.camp/src"
export APP_ARGS="-Dethereumj.conf.file=$APP_SRC_DIR/main/resources/conf/$APP_ENV.conf"


#export JMX_OPTS="-Dcom.sun.management.jmxremote -Djava.rmi.server.hostname=45.55.229.30 -Dcom.sun.management.jmxremote.port=3333 -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false"
export JAVA_OPTS="-Xmx2g $JMX_OPTS"

echo "JAVA_OPTS: $JAVA_OPTS"
echo "APP_ARGS: $APP_ARGS"

./gradlew --refresh-dependencies clean bootRun -PjvmArgs="$JAVA_OPTS" $APP_ARGS > out.log &
