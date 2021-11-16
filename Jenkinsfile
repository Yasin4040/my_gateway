import groovy.json.JsonOutput

pipeline {
    agent any
    //parameters {
    //    choice(name: 'env_type', choices: ['dev', 'test', 'pre', 'prod', 'local'], description: '部署环境')
    //    string(name: 'branch', defaultValue: 'master', description: '分支')
    //    string(name: 'cpu', defaultValue: '200m', description: '分配cpu，默认：0.2核')
    //    string(name: 'memory', defaultValue: '768Mi', description: '分配内存，默认：512兆')
    //    string(name: 'limitCpu', defaultValue: '2000m', description: '最大cpu，默认：1核')
    //    string(name: 'limitMemory', defaultValue: '768Mi', description: '最大内存，默认：512兆')
    //}
	environment {
	    //----------需要修改的内容---------------
	    //appname="jtyjy-api-gateway" //项目名称
        //version="1.0-SNAPSHOT" //版本号
        //jar_name = "jtyjy-api-gateway.jar" //打包成功的jar包名
        //enable_service = "NodePort" //不启用填空字符串 可选 ClusterIP或NodePort
        //port="11001" //springboot的http端口
        gitUrl = "ssh://git@192.168.4.93:222/jtyjy_mall/jtyjy-api-gateway.git" //git地址
        //swAddr = "192.168.5.106:30218" //skywalking 地址
        //------------------------------------

        env_type="${env_type}"
    }
    stages {
        stage('check out source') {
            steps {
              script {
                def repositoryUrl = scm.userRemoteConfigs[0].url
                println(repositoryUrl)
                git branch: "${branch}", credentialsId: 'gitlab-ssh', url: "${gitUrl}"
                env.gitVersion = sh returnStdout: true, script: "git log --abbrev-commit --pretty=format:%h -1"

                //def pom = readMavenPom file: 'pom.xml'
                //println(pom)

                //def project = new XmlSlurper().parse(new File(pwd() + "/pom.xml"))
                //def pomv = project.version.toString()
                //println(pomv)
                load pwd() + "/Jenkinsfile.groovy"
                //println(env.swAddr)
                docker_tagname = "test.harbor.jtyjy.com/library/${appname}:${version}"
                if ( env_type != 'prod' ) {
                    env.tagname = "${docker_tagname}_${env.gitVersion}"
                }else{ //如果是生产发布版则不带git版本
                    env.tagname = "${docker_tagname}"
                }

                //添加k8s环境变量
                def map = [:]
                map.put("spring.profiles.active", "${env_type}")
                map.put("SW_AGENT_NAMESPACE", "${env_type}")
                map.put("SW_AGENT_NAME", "${appname}")
                map.put("SW_AGENT_COLLECTOR_BACKEND_SERVICES", "${swAddr}")
                map.put("JAVA_OPTS", "${JAVA_OPTS}")
                env.k8sEnv = map
              }
            }
        }
        stage('maven构建') {
          steps {
            sh 'echo "开始maven构建"'
            sh '''
                mvn clean install -Dmaven.test.skip=true
                cp target/${jar_name} docker/
            '''
          }
        }
        stage('打包镜像') {
            steps {
                sh 'echo "docker打包镜像"'
                echo "${tagname}"
                sh """
                    cd docker/
                    echo ${jar_name}
                    echo ${tagname}
                    docker -H 192.168.5.108:2375 build --build-arg jar_name=${jar_name} --no-cache -t ${tagname} .
                    docker -H 192.168.5.108:2375 login -u admin -p Harbor12345 test.harbor.jtyjy.com
                    docker -H 192.168.5.108:2375 push ${tagname}
                """
            }
        }

        stage('部署') {
            steps {
                script {
                    def config = [:]
                    config.enableIngress = false
                    config.enableService = "${enable_service}"
                    config.image = "${tagname}"
                    config.limitCpu = "${limitCpu}"
                    config.limitMemory = "${limitMemory}"
                    config.portMappingList = []
                    config.portMappingList[0] = ["name": "http", "port": "${port}", "targetPort": "${port}"]
                    config.env = env.k8sEnv
                    config.projectName = "${appname}"
                    config.namespace = "${env_type}"
                    config.requestCpu = "${cpu}"
                    config.requestMemory = "${memory}"
                    //config.command = array 填写会覆盖docker默认的执行
                    //config.args = ["-Xms384m", "-Xmx384m", "-javaagent:/usr/local/agent/skywalking-agent.jar", "-jar", "/$jar_name"]
                    def data = JsonOutput.toJson(config);
                    println(data)
                    sh """
                        curl -H "Content-Type:application/json" -X POST -d '${data}' 'http://192.168.5.106:31615/deploy_springboot_simple'
                    """
                }
            }
        }
    }
}