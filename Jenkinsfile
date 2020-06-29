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
                sh 'mvn clean install -Dmaven.test.skip=true'
            }
        }
        stage('deploy jar') {
            when {
                buildingTag()
            }
            steps {
                sh 'mvn -s $MVN_SET deploy -Dmaven.test.skip=true'
            }
        }
        stage('deploy exe') {
            when {
                buildingTag()
            }
            steps {
                sh 'mvn      -s $MVN_SET \\\n' +
                        '     deploy:deploy-file \\\n' +
                        '    -Durl=http://vesta.dh-karlsruhe.de/nexus/repository/maven-snapshots/ \\\n' +
                        '    -DrepositoryId=maven-snapshots \\\n' +
                        '    -DgroupId=org.woped \\\n' +
                        '    -DartifactId=Installer \\\n' +
                        '    -Dversion=3.7.1  \\\n' +
                        '    -Dpackaging=exe \\\n' +
                        '    -Dfile=WoPeD-IzPack/target/WoPeD-Installer.exe'
            }
        }
    }
}