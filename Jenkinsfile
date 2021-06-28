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
        stage('deploy installers') {
            environment {
                VERSION = getVersion()
            }
            steps {
                configFileProvider([configFile(fileId: 'nexus-credentials', variable: 'MAVEN_SETTINGS')]) {
                    sh "mvn -s $MAVEN_SETTINGS deploy:deploy-file -Durl=http://vesta.dh-karlsruhe.de/nexus/repository/WoPeD-Installers/ -DgroupId=de.dhbw.woped -DartifactId=installers -Dversion='${VERSION}' -DrepositoryId=WoPeD-Installers -Dpackaging=exe -Dfile=./WoPeD-Installer/target/Windows/WoPeD-install-windows-'${VERSION}'.exe"
                }
                configFileProvider([configFile(fileId: 'nexus-credentials', variable: 'MAVEN_SETTINGS')]) {
                    sh "mvn -s $MAVEN_SETTINGS deploy:deploy-file -Durl=http://vesta.dh-karlsruhe.de/nexus/repository/WoPeD-Installers/ -DgroupId=de.dhbw.woped -DartifactId=installers -Dversion='${VERSION}' -DrepositoryId=WoPeD-Installers -Dpackaging=jar -Dfile=./WoPeD-Installer/target/Linux/WoPeD-install-linux-'${VERSION}'.jar"
                }
            }
        }
    }

    post {
        always {
            echo 'I will always say Hello again!'
        }

    }
}

def getVersion() {
    pom = readMavenPom file: 'pom.xml'
    return pom.version
}