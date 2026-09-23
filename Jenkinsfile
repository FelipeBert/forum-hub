def COLOR_MAP = [
    'SUCCESS': 'good',
    'FAILURE': 'danger'
]

pipeline{
    agent{
        label "jdk21-agent"
    }

    environment {
        DB_CREDS = credentials('rdsconnection')
        DB_URL = credentials('DB_URL')
        DB_NAME = credentials('DB_NAME')
        SLACK_CHANNEL = credentials('SLACK_CHANNEL')
        S3_NAME = credentials('S3_NAME')
    }

    stages{
        stage('Fetch Code') {
            steps {
                git branch: 'main', url: 'https://github.com/FelipeBert/forum-hub.git'
            }
        }

        stage('Checkstyle Analysis') {
            steps {
                sh 'mvn checkstyle:checkstyle'
            }
        }

        stage('Sonar Code analysis') {
            environment {
                scannerHome = tool 'SONAR8.0'
            }
            
            steps {
                withSonarQubeEnv('sonarserver') {
                    sh ''' ${scannerHome}/bin/sonar-scanner \
                        -Dsonar.projectKey=forum-hub \
                        -Dsonar.projectName=forum-hub \
                        -Dsonar.projectVersion=1.0 \
                        -Dsonar.sources=src/main/java \
                        -Dsonar.tests=src/test/java \
                        -Dsonar.java.binaries=target/classes \
                        -Dsonar.java.test.binaries=target/test-classes \
                        -Dsonar.junit.reportsPath=target/surefire-reports/ \
                        -Dsonar.jacoco.reportsPath=target/jacoco.exec \
                        -Dsonar.java.checkstyle.reportPaths=target/checkstyle-result.xml
                    '''
                }
            }
        }

        stage("Quality Gate") {
            steps {
                timeout(time: 1, unit: 'HOURS') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        stage('Build') {
           steps {
                sh 'mvn -B -DskipTests clean package' 
           }

           post {
                success {
                    echo "Archiving Artifact"
                    archiveArtifacts artifacts: '**/*.jar'
                }
           }
        }

        stage('Publish Artifact to S3') {
            steps {
                sh '''
                    echo "Publishing Artifact on S3"
                    aws s3 cp target/*.jar s3://${S3_NAME}/forum-hub-build-${BUILD_NUMBER}.jar
                '''
            }
        }

        stage('Apply Database migrations to RDS') {
            steps {
                sh '''
                    echo "Applying Migrations to RDS Database using flyway"
                    
                    mvn flyway:migrate \
                        -Dflyway.url=jdbc:mysql://${DB_URL}:3306/${DB_NAME}?createDatabaseIfNotExist=true \
                        -Dflyway.user=${DB_CREDS_USR} \
                        -Dflyway.password=${DB_CREDS_PSW}
                '''
            }
        }
    }

    post {
        always {
            echo 'Slack Notifications.'
            slackSend channel: '${SLACK_CHANNEL}',
                color: COLOR_MAP[currentBuild.currentResult],
                message: "*${currentBuild.currentResult}:* Job ${env.JOB_NAME} build ${env.BUILD_NUMBER}"
        }
    }
}