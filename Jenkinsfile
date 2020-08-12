properties([
  [$class: 'jenkins.model.BuildDiscarderProperty', strategy: [$class: 'LogRotator', numToKeepStr: '25']],
  pipelineTriggers([[$class:"SCMTrigger", scmpoll_spec:"H/30 * * * *"], snapshotDependencies()]),
])
pipeline {
  agent any
  stages {
    stage('Build') {
      steps {
        withMaven(maven: 'M3.6', mavenSettingsConfig: 'efaps11', mavenLocalRepo: "$WORKSPACE/../../.m2/${env.BRANCH_NAME}") {
          sh 'mvn clean install -DskipTests'
        }
      }
    }
    stage('Test') {
      steps {
        withMaven(maven: 'M3.6', mavenSettingsConfig: 'efaps11', mavenLocalRepo: "$WORKSPACE/../../.m2/${env.BRANCH_NAME}",
            options: [openTasksPublisher(disabled: true)]) {
          sh 'mvn test'
        }
      }
      post {
        always {
            step([$class: 'Publisher', reportFilenamePattern: '**/testng-results.xml'])
        }
      }
    }
    stage('Coverage') {
      steps {
        withMaven(maven: 'M3.6', mavenSettingsConfig: 'efaps11', mavenLocalRepo: "$WORKSPACE/../../.m2/${env.BRANCH_NAME}",
            options: [openTasksPublisher(disabled: true)]) {
          sh "mvn clean clover:setup test clover:aggregate clover:clover"
        }
        step([
          $class: 'CloverPublisher',
          cloverReportDir: 'target/site',
          cloverReportFileName: 'clover.xml',
          healthyTarget: [methodCoverage: 5, conditionalCoverage: 5, statementCoverage: 5]  // optional, default is: method=70, conditional=80, statement=80
        ])
      }
    }
  }
}
