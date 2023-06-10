pipeline {
    agent any
    
    triggers {
        pollSCM('*/3 * * * *')
    }
    
    environment {
        DOCKER = credentials('my-docker')
    }
    
    stages {
        stage('Prepare') {
            agent any
            
            steps {
                git url: 'https://github.com/sayyyho/Jenkins-Prac.git',
                    branch: 'main',
                    credentialsId: 'my-github'
            }
            post {
                success {
                    echo 'Successfully cloned Repository'
                }
                always {
                    echo "i tried..."
                }
                cleanup {
                    echo "after all ohter post condition"
                }
            }
        }
    
        stage('Build') {
            steps {
                dir('/var/jenkins_home/jobs/jksprac/workspace@2') {
                    sh "docker-compose build assignment"
                }
            }
        }
        
        stage('Tag') {
            steps {
                script {
                    sh "docker tag ${DOCKER_USR}/assignment ${DOCKER_USR}/assignment:${BUILD_NUMBER}"
                }
            }
        }
    
        stage('Push') {
            steps {
                script {
                    sh "docker login -u ${DOCKER_USR} -p ${DOCKER_PSW}"
                    sh "docker push ${DOCKER_USR}/assignment:${BUILD_NUMBER}"
                }
            }
        }
    }
}