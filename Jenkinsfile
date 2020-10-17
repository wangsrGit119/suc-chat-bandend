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
                         steps {
                              sh 'java -version'
                           }
                       }

    }
    post {
            always {
                echo 'One way or another, I have finished'
                deleteDir()  /* clean up our workspace */
            }
            success {
                echo 'I succeeeded!'
            }
            unstable {
                echo 'I am unstable :/'
            }
            failure {
                echo 'I failed :('
            }
            changed {
                echo 'Things were different before...'
            }
        }
}
