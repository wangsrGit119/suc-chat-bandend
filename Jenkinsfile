pipeline {
    agent any
    stages {
        stage('拉取代码') {
            steps {
                echo "${params}"
                sh 'mvn --version'
            }
        }
        stage('构建jar包') {
                    steps {
                        sh 'mvn clean'
                        sh 'ls'
                    }
               }
        stage('发送至指定服务器') {
                     steps {
                          sh 'date'
                          echo "项目构建结果路径：${WORKSPACE}"
                       }
               }
        stage('项目启动') {
                    input {
                                message "是否要启动项目？"
                                ok "yes,continue"
                                submitter "suc"
                           }
                     steps {
                          sh 'java -version'
                       }
                     }

    }
    post {
            always {
                echo '删除构建文件夹'
                deleteDir()  /* clean up our workspace */
            }
            success {
                echo 'success'
            }
            unstable {
                echo 'I am unstable :/'
            }
            failure {
                echo 'failed'
            }
            changed {
                echo 'Things were different before...'
            }
        }
}
