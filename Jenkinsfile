pipeline {
    agent any
    stages {
        stage('环境检查') {
            steps {
                sh 'mvn --version'
            }
        }
        }
        stage('构建jar包') {
                steps {
                    sh 'mvn clean package -Dmaven.test.skip=true'
                    sh 'ls'
                    echo '文件路径输出'
                    sh 'pwd'
                }
         }
        stage('发送至指定服务器') {
                 steps {
                      echo "区分构建ID： ${build}"
                      echo "项目构建结果路径：${WORKSPACE}"
                      echo '发送成功'
                   }
         }
        stage('项目启动') {
                 steps {
                      echo '项目启动'
                      sh 'cd target && ls'
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
