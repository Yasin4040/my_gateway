pipeline {
    agent any
    parameters {
        choice(name: 'env_type', choices: ['dev', 'test', 'pre', 'prod'], description: '部署环境')
        string(name: 'branch', defaultValue: 'master', description: '分支')
        string(name: 'cpu', defaultValue: '200m', description: '分配cpu，默认：0.2核')
        string(name: 'memory', defaultValue: '512Mi', description: '分配内存，默认：512兆')
        string(name: 'limitCpu', defaultValue: '1000m', description: '最大cpu，默认：1核')
        string(name: 'limitMemory', defaultValue: '512Mi', description: '最大内存，默认：512兆')
    }
	environment {
	    //----------需要修改的内容---------------
	    appname="jtyjy-api-gateway" //项目名称
        version="1.0-SNAPSHOT" //版本号
        jar_name = "jtyjy-api-gateway.jar" //打包成功的jar包名
        enable_service = "NodePort" //不启用填空字符串 可选 ClusterIP或NodePort
        port="11001" //springboot的http端口
        gitUrl = "http://192.168.4.93/zxw20943/jtyjy-api-gateway.git" //git地址
        //------------------------------------

        docker_tagname = "test.harbor.jtyjy.com/library/${appname}:${version}"
        env_type="${env_type}"
    }
    stages {
        stage('check out source') {
            steps {
              script {
                git branch: "${branch}", credentialsId: 'gitlab-yx-passwd', url: "${gitUrl}"
                env.gitVersion = sh returnStdout: true, script: "git log --abbrev-commit --pretty=format:%h -1"
                if ( env_type != 'prod' ) {
                    env.tagname = "${docker_tagname}_${env.gitVersion}"
                }else{ //如果是生产发布版则不带git版本
                    env.tagname = "${docker_tagname}"
                }

              }
            }
        }
        stage('maven构建') {
          steps {
            sh 'echo "开始maven构建"'
            sh '''
                mvn clean install -Dmaven.test.skip=true -Denv=$env_type
                cp target/$jar_name docker/
            '''
          }
        }
        stage('打包镜像') {
            steps {
                sh 'echo "docker打包镜像"'
                echo "${env.tagname}"
                sh """
                    cd docker/
                    echo $jar_name
                    echo ${env.tagname}
                    docker -H 192.168.5.108:2375 build --build-arg env_type=$env_type --build-arg jar_name=$jar_name --no-cache -t ${env.tagname} .
                    docker -H 192.168.5.108:2375 login -u admin -p Harbor12345 test.harbor.jtyjy.com
                    docker -H 192.168.5.108:2375 push ${env.tagname}
                """
            }
        }

        stage('部署') {
            steps {
                script {
                    data = """\
                        {\
                          "enableIngress": false,\
                          "enableService": "NodePort",\
                          "image": "${env.tagname}",\
                          "limitCpu": "${limitCpu}",\
                          "limitMemory": "${limitMemory}",\
                          "portMappingList": [\
                            {\
                              "name": "http",\
                              "port": ${port},\
                              "targetPort": ${port}\
                            }\
                          ],\
                          "projectName": "${appname}",\
                          "requestCpu": "${cpu}",\
                          "requestMemory": "${memory}"\
                        }\
                    """
                    sh """
                        curl -H "Content-Type:application/json" -X POST -d '${data}' 'http://192.168.5.106:31615/deploy_springboot_simple'
                    """
                }
            }
        }
    }
}