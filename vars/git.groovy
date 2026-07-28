/**
 * Calls the github API and returns a JSON response.
 *
 * @param config A map that contains the config for the Github API:
 *   - `owner` (String): Owner of the repo, such as an org or user. *Required*.
 *   - `repo` (String): The repo to call in the API. *Required*.
 *   - `path` (String): End of the url to call. *Required*.
 */
def gitRepo(Map config) {
    withCredentials([usernamePassword(
                    credentialsId: 'GITHUB APP ID',
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
