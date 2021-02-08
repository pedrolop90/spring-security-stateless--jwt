pipeline {
  agent any

  parameters {
    string(
      name: 'SOURCE_URL',
      defaultValue: 'https://github.com/pedrolop90/springboot-oauth2',
      description: 'Url del repositorio donde esta el codigo.'
    )
    string(
      name: 'REPOSITORY_DOCKER_NAME',
      defaultValue: 'pedrolop90/biblioteca-prueba',
      description: 'Nombre del repositorio donde se va alojar el docker.'
    )
    string(
          name: 'BRANCH_DEFAULT',
          defaultValue: 'develop',
          description: 'Branch del cual se va a sacar el codigo.'
    )
    string(
          name: 'CREDENDIALS_NAME_SONARQUBE',
          defaultValue: 'SONARCLOUD_TOKEN',
          description: 'Nombre de las credenciales por defecto de sonarqube.'
    )
    booleanParam(
          name: 'ABORT_SONARQUBE_FAIL',
          defaultValue: true,
          description: 'Hacer que el pipeline se pare si sonarqube dice que hay problemas.'
    )
    string(
          name: 'CREDENDIALS_NAME_DOCKERHUB',
          defaultValue: 'DockerHubCredentials',
          description: 'Nombre de las credenciales por defecto de dockerhub.'
    )
    string(
          name: 'POLL_SCM',
          defaultValue: '* * * * *',
          description: 'Periodo de consulta contra el repositorio.'
    )
    string(
          name: 'TIMEOUT_UNIT',
          defaultValue: 'MINUTES',
          description: 'Unidad de medida del tiempo timeout.'
    )
    string(
          name: 'TIMEOUT_TIME',
          defaultValue: '15',
          description: 'Tiempo del timeout.'
    )
    string(
          name: 'CREDENTIALS_NAME_TERRAFORM',
          defaultValue: 'terraform-auth'
    )
    string(
          name: 'VARIABLES_NAME_TERRAFORM',
          defaultValue: 'variables-terraform'
    )
  }

  environment {
    ARTIFACT_ID                     = "${env.REPOSITORY_DOCKER_NAME}:${env.BUILD_NUMBER}"
    SOURCE_URL                      =  ${params.SOURCE_URL}
    BRANCH_DEFAULT                  =  ${params.BRANCH_DEFAULT}
    CREDENDIALS_NAME_SONARQUBE      =  ${params.CREDENDIALS_NAME_SONARQUBE}
    ABORT_SONARQUBE_FAIL            =  ${params.ABORT_SONARQUBE_FAIL}
    CREDENDIALS_NAME_DOCKERHUB      =  ${params.CREDENDIALS_NAME_DOCKERHUB}
    POLL_SCM                        =  ${params.POLL_SCM}
    TIMEOUT_UNIT                    =  ${params.TIMEOUT_UNIT}
    TIMEOUT_TIME                    =  ${params.TIMEOUT_TIME}
    CREDENTIALS_NAME_TERRAFORM      =  credentials('${params.CREDENTIALS_NAME_TERRAFORM}')
    VARIABLES_NAME_TERRAFORM        = '${params.VARIABLES_NAME_TERRAFORM}'
  }

  options {
    timeout(time: ${env.TIMEOUT_TIME}, unit: ${env.TIMEOUT_UNIT})
  }

  triggers {
        pollSCM('${env.POLL_SCM}')
  }

  stages {
    stage('Checkout Source') {
      steps {
        git url:'${env.SOURCE_URL}', branch:'${env.BRANCH_DEFAULT}'
         sh 'mkdir -p creds'
         sh 'echo CREDENTIALS_NAME_TERRAFORM | base64 -d > ./creds/serviceaccount.json'
      }
    }
    stage('Compile') {
        steps {
            withGradle() {
                gradlew('clean', 'classes')
            }
        }
    }
    stage('Unit Tests') {
        steps {
            withGradle() {
                gradlew('test')
            }
        }
        post {
            always {
                junit '**/build/test-results/test/TEST-*.xml'
            }
        }
    }
    stage('SonarQube analysis') {
        environment {
            SONAR_LOGIN = credentials('${env.CREDENDIALS_NAME_SONARQUBE}')
        }
        steps {
            withSonarQubeEnv('dev') {
                withGradle(){
                    gradlew('sonarqube')
                }
            }
        }
    }
    stage("Quality gate") {
        steps {
            waitForQualityGate abortPipeline: ${env.ABORT_SONARQUBE_FAIL}
        }
    }
    stage('Build') {
      steps {
        script {
           dockerImage = docker.build "${env.ARTIFACT_ID}"
        }
      }
    }
    stage('Publish') {
      steps {
        script {
          docker.withRegistry("", "${env.CREDENDIALS_NAME_DOCKERHUB}") {
            dockerImage.push()
            dockerImage.push('latest')
          }
        }
      }
    }
    stage('Plan Terraform'){
       steps {
         container('terraform') {
           sh '${env.VARIABLES_NAME_TERRAFORM} | base64 -d > terraform.tfvars'
           sh 'terraform init'
           sh 'terraform plan -out myplan'
         }
       }
    }
     stage('Apply Terraform') {
      steps {
        container('terraform') {
          sh terraform apply -input=false -auto-approve'
        }
      }
    }
  }
}

def gradlew(String... args) {
    sh "./gradlew ${args.join(' ')} -s"
}