pipeline {
    agent any          // ← su quale agent girare (any = il primo disponibile)

    stages {           // ← le fasi della pipeline
        stage('Checkout') {
            steps {
                checkout scm   // ← clona la repo (scm = quello configurato nel Job)
            }
        }
        stage('Build') {
            steps {
                sh 'mvn clean package'   // ← comando shell
            }
        }
        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }
    }
}