FROM test.harbor.jtyjy.com/library/jdk-skywalking:oracle-jdk8-8.7

ADD ./jtyjy-api-gateway.jar /jtyjy-api-gateway.jar

#EXPOSE 8080
ENV TZ=Asia/Shanghai
#ENV LANG en_US.UTF-8
#ENV LANGUAGE en_US:en
#ENV LC_ALL en_US.UTF-8
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

WORKDIR /

ENTRYPOINT ["java", "-Xms384m", "-Xmx384m", "-javaagent:/usr/local/agent/skywalking-agent.jar", "-DSW_AGENT_NAME=jtyjy-api-gateway", "-DSW_AGENT_COLLECTOR_BACKEND_SERVICES=192.168.5.106:30218", "-jar", "jtyjy-api-gateway.jar"]