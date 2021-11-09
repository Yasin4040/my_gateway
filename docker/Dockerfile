FROM test.harbor.jtyjy.com/library/oracle-jdk8:sw-arthas

ADD ./jtyjy-api-gateway.jar /jtyjy-api-gateway.jar

#EXPOSE 8080
ENV TZ=Asia/Shanghai
ENV LANG en_US.utf8
#ENV LANGUAGE en_US:en
ENV LC_ALL en_US.utf8
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

WORKDIR /

ENTRYPOINT ["java", "-Xms384m", "-Xmx384m", "-javaagent:/usr/local/agent/skywalking-agent.jar", "-jar", "jtyjy-api-gateway.jar"]