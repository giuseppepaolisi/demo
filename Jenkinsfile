pipeline {
    agent any
    tools {
        maven 'Maven 3.8.1'
        jdk 'jdk-21'
    }
    stages {
            stage('Build') {
                steps {
                    sh "echo 'Current workspace is $WORKSPACE'"
                    sh 'mvn clean install'
                    sh 'echo "Build successful!"'
                }
                post {
                    always {
                        sh 'echo "Cleaning up workspace..."'
                        cleanWs()
                    }
                }
            }
    }
}