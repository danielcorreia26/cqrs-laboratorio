
pipeline {
    agent any

    options {
        disableConcurrentBuilds()
    }

    stages {

        stage('Build e Test - Write Service') {
            steps {
                dir('write-service') {
                    sh './mvnw -B clean verify'
                }
            }
        }

        stage('Build e Test - Read Service') {
            steps {
                dir('read-service') {
                    sh './mvnw -B clean verify'
                }
            }
        }

        stage('Build e Test - API Gateway') {
            steps {
                dir('api-gateway') {
                    sh './mvnw -B clean verify'
                }
            }
        }
    }

    post {
        success {
            echo 'Todos os serviços foram compilados e testados com sucesso!'
        }

        failure {
            echo 'O pipeline falhou. Verifique os logs da etapa com erro.'
        }
    }
}
