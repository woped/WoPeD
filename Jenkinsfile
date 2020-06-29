pipeline {
    environment {
        MVN_SET = credentials('nexus-credentials')
    }
    agent {
        docker {
            image 'maven:3.6.3-jdk-8'
            args '-u root'
        }
    }

    stages {
        stage('prepare') {
            steps {
                sh 'apt-get update'
                sh 'apt-get install zlib1g'
            }
        }
        stage('build') {
            steps {
                sh 'mvn install -Dmaven.test.skip=true'
            }
        }
        stage('deploy jar') {
            steps {
                sh 'mvn -s $MVN_SET deploy'
            }
        }
    }
}