teamFolder("team-directory") {
  description("This is a team directory for the project")

  // Set the SCM settings for the team directory
  scm {
    git {
      url("https://git-codecommit.us-east-1.amazonaws.com/v1/repos/repository-name")
      branch("master")
    }
  }

  // Configure the team directory to automatically detect new repositories
  properties {
    orgFolder(
      // Set the scanning interval to every 5 minutes
      scanIntervalSeconds: 300,
      // Set the repository criteria
      targetRepoCriteria: '*',
      // Set the branch criteria
      targetBranchCriteria: '*',
      // Set the build strategy to multibranch pipeline using the Jenkinsfile in the repository
      pipelineStrategy: 'multibranchWithJenkinsfile'
    )
  }
}