def gitRepo(Map config) {
    withCredentials([usernamePassword(
                    credentialsId: 'JenkinsGTTest',
                    usernameVariable: 'USER',
                    passwordVariable: 'TOKEN'
                )])  {
        sh """curl -L \
        -H "Accept: application/vnd.github+json" \
        -H "Authorization: Bearer ${TOKEN}" \
        -H "X-GitHub-Api-Version: 2026-03-10" \
        https://api.github.com/repos/${config.owner}/${config.repo}/${config.path}"""
    }

}
