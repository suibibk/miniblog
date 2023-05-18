#! /bin/sh
#kill
SERVER_NAME_ACTIVITY_MANAGEMENT="miniblog"
echo "stop miniblog..."
pid=`ps -ef |grep java |grep -w $SERVER_NAME_ACTIVITY_MANAGEMENT| grep -v grep | awk '{print $2}'`
echo "run pid:"$pid
if [ "$pid" != "" ]
then
        echo "Try to kill the SpringBoot service $SERVER_NAME_ACTIVITY_MANAGEMENT progress $pid......"
        kill -9 $pid
        sleep 1
fi

echo "begin start"

#启动
nohup java -jar miniblog.jar \
--server.port=80 \
--spring.config.location=application.yml>info.log &
tail -f info.log
echo "end"
