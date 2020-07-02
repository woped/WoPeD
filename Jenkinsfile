pipeline {
    environment {
        MVN_SET = credentials('nexus-credentials')
    }
    agent {
        docker {
            image 'maven:3.6.3-jdk-11'
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
                sh 'mvn -s $MVN_SET deploy -Dmaven.test.skip=true'
            }
        }
        stage('deploy exe') {
            environment {
                VERSION = getVersion()
            }
            steps {
                configFileProvider([configFile(fileId: 'nexus-credentials', variable: 'MAVEN_SETTINGS')]) {
                    sh "mvn -s $MAVEN_SETTINGS deploy:deploy-file -Durl=http://vesta.dh-karlsruhe.de/nexus/repository/WoPeD-Installers/ -DgroupId=de.dhbw.woped -DartifactId=WoPeD-IzPack -Dversion=3.7.1-SNAPSHOT -DrepositoryId=WoPeD-Installers -Dpackaging=exe -Dfile=./WoPeD-IzPack/target/WoPeD-Installer.exe"
                }
            }
        }
    }
}

def getVersion() {
    pom = readMavenPom file: 'pom.xml'
    return pom.version
}