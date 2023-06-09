pipeline {
    agent any
    options {
        disableConcurrentBuilds()
        ansiColor('xterm')
    }
    environment {
        CONTINUE_BUILD = false
    }
    parameters {
        choice(
            name: 'INVENTORY',
            choices: ['aws_ec2.yml'],
            description: 'Select the Ansible inventory'
        )
        string(
            name: 'HOST_LIMIT',
            defaultValue: '',
            description: 'Specify the host limit'
        )
        string(
            name: 'TAGS',
            defaultValue: '',
            description: 'Specify the tags'
        )
        booleanParam(
            name: 'REQS_FORCE_INSTALL',
            defaultValue: false,
            description: 'Force requirements reinstall'
        )
        choice(
            name: 'MODE',
            choices: ['check', 'run'],
            description: 'Choose the mode'
        )
    }
    stages {
        stage('Update Job Description') {
            steps {
                script {
                    def displayName = "Ansible playbook"
                    def jobDescription = ""

                    if (!currentBuild.rawBuild.getCauses()[0].toString().contains('UserIdCause')) {
                        displayName = "Reload"
                        CONTINUE_BUILD = false
                    } else {
                        CONTINUE_BUILD = true
                    }

                    if (params.HOST_LIMIT) {
                        jobDescription += "Host limit: ${params.HOST_LIMIT}<br />"
                    }

                    if (params.TAGS) {
                        jobDescription += "Tags: ${params.TAGS}<br />"
                    }

                    if (CONTINUE_BUILD) {
                        jobDescription += "Mode: ${params.MODE}"
                    }

                    currentBuild.displayName = displayName
                    currentBuild.description = jobDescription
                }
            }
        }
        stage('Install requirements') {
            when {
                allOf {
                    expression { return fileExists ('requirements.yml') }
                    expression { return params.REQS_FORCE_INSTALL }
                }
            }
            steps {
                sh '/usr/local/bin/ansible-galaxy install --force -r requirements.yml'
            }
        }
        stage('Run Check') {
            when {
                expression { params.MODE == 'check' && CONTINUE_BUILD }
            }
            steps {
                ansiblePlaybook(
                    playbook: 'playbook-remote.yml',
                    inventory: "${params.INVENTORY}",
                    vaultCredentialsId: "vault_password",
                    extras: "--check --diff",
                    colorized: true,
                    limit: "${params.HOST_LIMIT}",
                    tags: "${params.TAGS}"
                )
            }
        }
        stage('Ansible Playbook') {
            when {
                expression { params.MODE == 'run' && CONTINUE_BUILD }
            }
            steps {
                ansiblePlaybook(
                    playbook: 'playbook-remote.yml',
                    inventory: "${params.INVENTORY}",
                    vaultCredentialsId: "vault_password",
                    extras: "--diff",
                    colorized: true,
                    limit: "${params.HOST_LIMIT}",
                    tags: "${params.TAGS}"
                )
            }
        }
    }
}