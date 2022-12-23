pipeline {
    agent any
    options {
        disableConcurrentBuilds()
        ansiColor('xterm')
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
                    def jobDescription = ""

                    if (!currentBuild.rawBuild.getCauses()[0].toString().contains('UserIdCause')) {
                        currentBuild.setParameter(name: 'MODE', value: 'norun')
                        jobDescription += "Just reload<br />"
                    }
                    if (params.HOST_LIMIT) {
                        jobDescription += "<br />Host limit: ${params.HOST_LIMIT}<br />"
                    }
                    if (params.TAGS) {
                        jobDescription += "<br />Tags: ${params.TAGS}<br />"
                    }

                    currentBuild.displayName = 'Ansible'
                    currentBuild.description = jobDescription
                }
            }
        }
        stage('Run Check') {
            when {
                expression { params.MODE == 'check' }
            }
            steps {
                ansiblePlaybook(
                    playbook: 'playbook-remote.yml',
                    inventory: "${params.INVENTORY}",
                    vaultCredentialsId: "vault_password",
                    extras: "--check",
                    colorized: true,
                    limit: "${params.HOST_LIMIT}",
                    tags: "${params.TAGS}"
                )
            }
        }
        stage('Ansible Playbook') {
            when {
                expression { params.MODE == 'run' }
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