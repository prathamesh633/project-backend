pipeline {
    agent any
    stages {
        stage ("Code Pull") { 
            steps {
                branch 'dev' , url: 'https://github.com/prathamesh633/project-backend.git'
            }
        }
        stage ("Code Build") { 
            steps {
                sh 'mvn clean package'
            }
        }
        stage ("Deploy K8s") { 
            steps {
                sh ''' 
                docker build -t prathameshbhujade/project-backend:latest .
                docker push prathameshbhujade/project-backend:latest
                docker rmi prathameshbhujade/project-backend:latest
                kubectl apply -f ./k8s/
                '''
            }
        }
    }
}
