pipeline {
    agent any

    triggers {
        pollSCM('*/3 * * * *')
    }

    environment {
        AWS_ACCOUNT_ID = "145674359814"
        AWS_DEFAULT_REGION = "ap-northeast-2"
        NAME="test"
        IMAGE_NAME = "${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_DEFAULT_REGION}.amazonaws.com/${NAME}"
        ECR_PATH="${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_DEFAULT_REGION}.amazonaws.com"
        BRANCH = "main"
    }

    stages {
        

        stage('Prepare') {
            steps {
                git branch: "${BRANCH}", credentialsId: 'my-github', url: 'https://github.com/sayyyho/Jenkins-Prac'
            }
        }
        
        stage('Build and Tagging') {
            steps {
                sh '''
        		 docker build -t $IMAGE_NAME:$BUILD_NUMBER .
        		 docker tag $IMAGE_NAME:$BUILD_NUMBER $IMAGE_NAME:latest
        		 '''
            }
        }

       
        stage('upload aws ECR') {
            steps {
                script {
                    sh 'docker push ${IMAGE_NAME}:latest'
                    sh 'docker push ${IMAGE_NAME}:${BUILD_NUMBER}'
                }
            }
        }
        stage('Docker Purge') {
            steps {
                sh 'docker image prune -fa'
                deleteDir()
                }
        }   
    }
}

