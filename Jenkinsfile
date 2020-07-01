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
                REPOSITORY = getRepository()
                VERSION = getVersion()
            }
            steps {
                sh "echo $VERSION $REPOSITORY"
                configFileProvider([configFile(fileId: 'nexus-credentials', variable: 'MAVEN_SETTINGS')]) {
                    sh "mvn -s $MAVEN_SETTINGS deploy:deploy-file -Durl=http://vesta.dh-karlsruhe.de/nexus/repository/maven-snapshots/ -DgroupId=de.dhbw.woped -DartifactId=WoPeD-IzPack -Dversion=3.7.1 -DrepositoryId=maven-snapshots -Dfile=./WoPeD-IzPack/target/WoPeD-Installer.exe"
                }
            }
        }
    }
}

def getRepository() {
    pom = readMavenPom file: 'pom.xml'
    version = pom.version

    if(version.toString().contains("SNAPSHOT")) {
        return "maven-snapshots"
    } else {
        return "maven-releases"
    }
}

def getVersion() {
    pom = readMavenPom file: 'pom.xml'
    return pom.version
}