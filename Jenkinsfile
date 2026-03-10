pipeline {
    agent {
        docker {
            image 'maven:3.9.9-eclipse-temurin-21-alpine'
        }
    }
    stages {
            stage('Checkout') {
                steps {
                    sh 'echo "Checking out code..."'
                    checkout scm
                }
            }
            stage('Build') {
                steps {
                    sh 'mvn clean install'
                    sh 'echo "Build successful!"'
                }
            }
            post {
                always {
                    sh 'echo "Cleaning up workspace..."'
                    cleanWs()
                }
            }
    }
}