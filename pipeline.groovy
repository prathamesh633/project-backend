pipeline {
    agent any 
    environment {
        DOCKER_CREDENTIALS = credentials('docker-hub-creds')
    }
    stages {
        stage('code-pull'){
            steps {
                git branch: 'dev', url: 'https://github.com/prathamesh633/project-backend.git'
            }
        }
        stage('code-Build'){
            steps {
               sh 'mvn clean package'
            }
        }
        stage('docker build'){
            steps {
               sh 'docker build . -t prathameshbhujade/project-backend-img:latest'
            }
        }    
        stage('docker login') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'docker-hub-creds', passwordVariable: 'DOCKERHUB_PASSWORD', usernameVariable: 'DOCKERHUB_USERNAME')]) {
                    sh 'echo "$DOCKERHUB_PASSWORD" | docker login -u "$DOCKERHUB_USERNAME" --password-stdin'

                }
            }
        }
        stage('docker push'){
            steps {
               sh '''
                docker push prathameshbhujade/project-backend-img:latest
                docker rmi prathameshbhujade/project-backend-img:latest
                '''
            }
        }
        stage('Deploy-K8s'){
            steps {
               sh '''
                curl -O https://s3.us-west-2.amazonaws.com/amazon-eks/1.30.6/2024-11-15/bin/linux/amd64/kubectl
                chmod +x ./kubectl
                mkdir -p $HOME/bin && cp ./kubectl $HOME/bin/kubectl && export PATH=$HOME/bin:$PATH
                echo 'export PATH=$HOME/bin:$PATH' >> ~/.bashrc
               kubectl apply -f ./k8s/
               '''
            }
        }
    }
}