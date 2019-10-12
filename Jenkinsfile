pipeline {
    agent any
    stages {
      stage('Clone repo') {
        steps {
          checkout scm
        }
      }

      stage('Build') {
        steps {
          sh "./gradlew clean build"
        }
      }
        
      stage('Run Sonar Quality Gate') {
          steps {
            sh "./gradlew sonarqube -Dsonar.host.url=http://sonarqube:9000 -Dsonar.projectKey=diff-service -Dsonar.verbose=true"    
          }
      }
    /*   
        stage('Project details') {
            withCredentials([usernamePassword(credentialsId: 'jenkins', usernameVariable: 'SONAR_USERNAME', passwordVariable: 'SONAR_PASSWORD')]) {
                sh "curl -u $SONAR_USERNAME:$SONAR_PASSWORD http://sonarqube:9000/api/projects/search?project=scrap"   
            }
        }

        stage('Results') {
            withCredentials([usernamePassword(credentialsId: 'jenkins', usernameVariable: 'SONAR_USERNAME', passwordVariable: 'SONAR_PASSWORD')]) {
                sh "curl -u $SONAR_USERNAME:$SONAR_PASSWORD http://sonarqube:9000/api/project_analyses/search?project=scrap"
            }
        }
    */
  }
}
