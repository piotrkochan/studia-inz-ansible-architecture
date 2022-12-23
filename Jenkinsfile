pipeline {
    agent any
    options {
        disableConcurrentBuilds()
        ansicolor('xterm')
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
                    def jobDescription = "Ansible Playbook"

                    if (!currentBuild.rawBuild.getCauses()[0].toString().contains('UserIdCause')) {
                        currentBuild.setParameter(name: 'MODE', value: 'norun')
                        jobDescription += "\nJust reload"
                    }

                    if (params.HOST_LIMIT) {
                        jobDescription += "\nHost limit: ${params.HOST_LIMIT}"
                    }

                    if (params.TAGS) {
                        jobDescription += "\nTags: ${params.TAGS}"
                    }

                    if (params.MODE) {
                        jobDescription += "\nMode: ${params.MODE}"
                    }

                    currentBuild.displayName = jobDescription
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