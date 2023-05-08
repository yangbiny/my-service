#!/usr/bin/env bash

/home/ubuntu/soft/third/java/bin/java -Dappname=my.service -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=8081 -Xms1g -Xmx1g -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=512m -Xloggc:/home/ubuntu/soft/app/my-service/my-service.log  -Dfile.encoding=UTF-8 -Djava.io.tmpdir=/home/ubuntu/soft/app/my-service -jar /home/ubuntu/soft/app/my-service/my-service.jar
