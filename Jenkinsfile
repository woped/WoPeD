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
                sh 'mvn install -X -Dmaven.test.skip=true'
            }
        }
        stage('deploy installers') {
            environment {
                VERSION = getVersion()
            }

            steps {
                configFileProvider([configFile(fileId: 'nexus-credentials', variable: 'MAVEN_SETTINGS')]) {
                    sh "mvn -s $MAVEN_SETTINGS deploy:deploy-file -X -Durl=http://vesta.dh-karlsruhe.de/nexusdocker/repository/WoPeD-Installers/ -DgroupId=de.dhbw.woped -DartifactId=installers -Dversion='${VERSION}' -DrepositoryId=WoPeD-Installers -Dpackaging=exe -Dfile=./WoPeD-Installer/target/Windows/WoPeD-install-windows-'${VERSION}'.exe"
                }
                configFileProvider([configFile(fileId: 'nexus-credentials', variable: 'MAVEN_SETTINGS')]) {
                    sh "mvn -s $MAVEN_SETTINGS deploy:deploy-file -X -Durl=http://vesta.dh-karlsruhe.de/nexusdocker/repository/WoPeD-Installers/ -DgroupId=de.dhbw.woped -DartifactId=installers -Dversion='${VERSION}' -DrepositoryId=WoPeD-Installers -Dpackaging=jar -Dfile=./WoPeD-Installer/target/Linux/WoPeD-install-linux-'${VERSION}'.jar"
                }
            }
        }
    }
    post {
        always {
            cleanWs()
            
            sh 'docker image prune -af'
        }
        success {
            setBuildStatus("Build succeeded", "SUCCESS");
        }
        failure {
            setBuildStatus("Build not Successfull", "FAILURE");
            
            emailext body: "Something is wrong with ${env.BUILD_URL}",
                subject: "Failed Pipeline: ${currentBuild.fullDisplayName}",
                to: '${DEFAULT_RECIPIENTS}'
        }
    }
}

def getVersion() {   
    pom = readMavenPom file: 'pom.xml'
    version = pom.version
    
    return version
}

void setBuildStatus(String message, String state) {
  step([
      $class: "GitHubCommitStatusSetter",
      reposSource: [$class: "ManuallyEnteredRepositorySource", url: "https://github.com/tfreytag/WoPeD"],
      contextSource: [$class: "ManuallyEnteredCommitContextSource", context: "ci/jenkins/build-status"],
      errorHandlers: [[$class: "ChangingBuildStatusErrorHandler", result: "UNSTABLE"]],
      statusResultSource: [ $class: "ConditionalStatusResultSource", results: [[$class: "AnyBuildResult", message: message, state: state]] ]
  ]);
}
