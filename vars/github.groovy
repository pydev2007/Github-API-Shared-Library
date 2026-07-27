

def gitRepo(Map config) {
    withCredentials([usernamePassword(
                    credentialsId: 'JenkinsGTTest',
                    usernameVariable: 'USER',
                    passwordVariable: 'TOKEN'
                )])  {


                def response = sh(
                    script: """
                        curl -sL \
                        -H "Accept: application/vnd.github+json" \
                        -H "Authorization: Bearer $TOKEN" \
                        -H "X-GitHub-Api-Version: 2022-11-28" \
                        "https://api.github.com/repos/${config.owner}/${config.repo}/${config.path}"
                    """,
                    returnStdout: true
                ).trim()
                def json = readJSON text: response

                return json
    }
}

def repoDiscovery() {
    def defaultYamlText = libraryResource('repos.yaml')
    def config = readYaml text: defaultYamlText
    echo defaultYamlText.discovery_config.repos
}
