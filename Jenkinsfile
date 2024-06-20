pipeline {
    agent any

    tools {
        jdk 'A408_BE_Build'
        nodejs 'A408_FE_Build'
    }
    environment {
        CONTAINER_NAME = "auto-dev-server"
        SSH_CREDENTIALS = 'DevOps'
        REMOTE_HOST = '172.31.41.136'
        SCRIPT_PATH = '/temp/AutoDevServer.sh'
        SSH_REMOTE_CONFIG = 'ubuntu'
    }

    stages {

        stage('Build BE && sendArtifact') {
            when {
                expression { env.GIT_BRANCH == 'be' }
            }
            steps {
                script {
                    dir('BE') {
                        sh 'chmod +x gradlew'
                        sh 'ls -l'

                        sh './gradlew clean build'
                        sh 'jq --version'
                        sh 'cd build/libs && ls -al'

                        sh 'echo manual Auto CI Start'
                        sh 'curl "https://ssafycontrol.shop/control/dev/be"'
                        sh 'echo manual Auto CI Done...!'

                        sh 'echo Auto Control CD Start'
                        sh 'ls -al'


                        sshPublisher(
                            publishers: [
                                sshPublisherDesc(
                                    configName: 'ssafycontrol',
                                        transfers: [
                                            sshTransfer(
                                                sourceFiles: 'build/libs/I10A709BE-0.0.1-SNAPSHOT.jar',
                                                removePrefix: 'build/libs',
                                                remoteDirectory: '/sendData',
                                            )
                                        ]
                                )
                            ]
                        )
                        sshPublisher(
                            publishers: [
                                sshPublisherDesc(
                                    configName: 'ssafymain',
                                        transfers: [
                                            sshTransfer(
                                                sourceFiles: 'build/libs/I10A709BE-0.0.1-SNAPSHOT.jar',
                                                removePrefix: 'build/libs',
                                                remoteDirectory: '/sendData',
                                                execCommand: 'sh /home/ubuntu/infra/AutoDevServer.sh'
                                            )
                                        ]
                                )
                            ]
                        )

                        sh 'echo Auto Control CD Done'
                        sh 'echo Auto Helpler CD Start'
                        sshPublisher(
                            publishers: [
                                sshPublisherDesc(
                                    configName: 'ssafyhelper',
                                    transfers: [
                                        sshTransfer(
                                            sourceFiles: 'build/libs/I10A709BE-0.0.1-SNAPSHOT.jar',
                                            removePrefix: 'build/libs',
                                            remoteDirectory: '/sendData',
                                        )
                                    ]
                                )
                            ]
                        )
                        sh 'echo Auto Helper CD Done'

                    }
                }
            }
        }

        stage('Build FE && sendArtifact') {
        	    when {
                        expression { env.GIT_BRANCH == 'fe' }
                    }
                    steps {
                        script {
                            // FE 폴더로 이동
                            dir('FE') {

                                sh 'node -v'
                                sh 'npm -v'
                                sh 'rm -rf node_modules'
                                // sh 'rm package-lock.json'
        			            sh 'npm install --global pnpm'
                                sh 'pnpm i'
                                sh 'pnpm run build'


                                sh 'echo manual Auto CI Start'
                                sh 'curl "https://ssafycontrol.shop/control/dev/fe"'

                                sh 'echo Auto CI Done Auto Control CD start'

                                sh 'ls -l'
                                sh 'tar -cvf febuild.tar ./dist/**'

                                sshPublisher(
                                    publishers: [
                                        sshPublisherDesc(
                                            configName: 'ssafycontrol',
                                                transfers: [
                                                    sshTransfer(
                                                        sourceFiles: 'febuild.tar',
                                                        remoteDirectory: '/sendData',
                                                    )
                                                ]
                                        )
                                    ]
                                )

                            }
                        }
                    }
                }




        stage('Continuous Delivery') {
            when {
                expression { env.GIT_BRANCH == 'main' }
            }
            steps {
                script {
                    sh '''curl -X POST -H "Content-Type: application/json" -d '{"isBe": false}' "https://ssafycontrol.shop/control/dev/deploy"'''
                    sh '''curl -X POST -H "Content-Type: application/json" -d '{"isBe": true}' "https://ssafycontrol.shop/control/dev/deploy"'''
                }
            }
        }
    }

    post {
        success {
            echo 'Build successful!'
        }
        failure {
            echo 'Build failed!'
        }
    }
}
