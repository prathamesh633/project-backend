pipeline {
    agent any 
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
         stage('Deploy-K8s'){
            steps {
               sh '''
                    docker build . -t prathameshbhujade/project-backend-img:latest
                    docker push prathameshbhujade/project-backend-img:latest
                    docker rmi prathameshbhujade/project-backend-img:latest
                    kubectl apply -f ./deploy/

               '''
            }
        }
    }
}