pipeline {
    agent any
    stages {
            stage('Build') {
                steps {
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