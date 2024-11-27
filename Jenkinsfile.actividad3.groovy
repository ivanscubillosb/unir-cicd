pipeline {
    agent {
        label 'docker'
    }
    stages {
        stage('Cleanup') {
            steps {
                echo 'Limpiando archivos de resultados anteriores...'
                cleanWs() // Limpia todo el espacio de trabajo
            }
        }
        stage('Build') {
            steps {
                echo 'Building stage!'
                sh 'make build'
            }
        }
        
        stage('API tests') {
            steps {
                sh 'make test-api'
                archiveArtifacts artifacts: 'results/*.xml'
            }
        }
        stage('E2E tests') {
            steps {
                sh 'make test-e2e'
                archiveArtifacts artifacts: 'results/*.xml'
            }
        }
    }
    post {
        failure {
            echo 'El pipeline ha fallado. Aqui deberia enviar correo.'
        }
        always {
            junit 'results/*_result.xml'
        }
    }
}
