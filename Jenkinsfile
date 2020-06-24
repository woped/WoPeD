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
                sh 'mvn install'
            }
        }
        stage('test') {
            steps {
                sh 'mvn test'
            }
        }
        stage('deploy jar') {
            steps {
                sh 'mvn -s $MVN_SET deploy'
            }
        }
        /*
        stage('exe erzeugen') {
            steps {
                sh 'cd WoPed-Installer'
                sh 'mvn package'
            }
        }
        stage('installer erzeugen (jar)') {
            steps {
                sh 'cd ../WoPeD-IzPack'
                sh 'mvn package'
            }
        }
        stage('exe fuer installer erzeugen') {
            steps {
                sh 'exe fuer installer erzeugen'
            }
        }
        */
    }
}