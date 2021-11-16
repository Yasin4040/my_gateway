env.appname = "jtyjy-api-gateway"
env.version="1.0-SNAPSHOT"
env.jar_name = "jtyjy-api-gateway.jar"
env.enable_service = "NodePort"
env.port = "11001"
env.gitUrl = "ssh://git@192.168.4.93:222/jtyjy_mall/jtyjy-api-gateway.git"
env.swAddr = "192.168.5.106:30218"
env.JAVA_OPTS = "-Xms384m -Xmx384m -javaagent:/usr/local/agent/skywalking-agent.jar -Dskywalking.trace.ignore_path=Lettuce/INFO,/actuator/**,/actuator"
if(env.env_type == 'local'){
    env.enableSW = true
    //env.hostNetwork = "enable"
}
