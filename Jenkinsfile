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
        stage('Print') {
            steps {
                script {
                    sh "echo ${getProjectName}"
                    sh "echo ${getProjectVersion}"
                }
            }
        }
    }
    
}
def getProjectName() {
    def pom = readMavenPom file: 'pom.xml'
    return pom.artifactId
}

def getProjectVersion() {
    def pom = readMavenPom file: 'pom.xml'
    return pom.version
}
