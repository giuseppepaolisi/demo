pipeline {
    agent any
    stages {
            stage('Build') {
                steps {
                    checkout scm
                    sh "echo 'Current workspace is $WORKSPACE'"
                    sh 'echo "*********Build successful!"'
                }

            }
    }
}