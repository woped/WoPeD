pipeline {
    environment {
        MVN_SET = credentials('nexus-credentials')
        REPOSITORY = getRepository()
        VERSION = getVersion()
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
                sh 'mvn clean install -Dmaven.test.skip=true'
            }
        }
        stage('deploy jar') {
            steps {
                sh 'mvn -s $MVN_SET deploy -Dmaven.test.skip=true'
            }
        }
        stage('deploy exe') {
            steps {
                sh 'mvn deploy:deploy-file ' +
                        "-Durl=http://vesta.dh-karlsruhe.de/nexus/repository/$REPOSITORY/ \\\n" +
                        '-DgroupId=de.dhbw.woped \\\n' +
                        '-DartifactId=WoPeD-IzPack \\\n' +
                        "-Dversion=$VERSION \\\n" +
                        '-DrepositoryId=some.id \\\n' +
                        '-Dfile=./WoPeD-IzPack/target/WoPeD-Installer.exe \\'
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