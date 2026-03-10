pipeline {
    agent any

    tools {
        maven 'maven-3.9.13'
        jdk 'jdk-21'
    }

    stages {
       stage('Verify') {
            steps {
                sh 'echo JAVA_HOME is $JAVA_HOME'
                sh 'java -version'
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean install'
            }
        }

    }
    
}
