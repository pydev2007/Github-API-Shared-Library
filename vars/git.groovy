/**
 * Calls the github API and returns a JSON response.
 *
 * @param config A map that contains the config for the Github API:
 *   - `method` (String): API methods such as GET, POST, PATCH, etc. *Optional*.
 *   - `owner` (String): Owner of the repo, such as an org or user. *Required*.
 *   - `repo` (String): The repo to call in the API. *Required*.
 *   - `path` (String): End of the url to call. *Required*.
 *   - `body` (String): The body for the API. *Optional*.
 */
 def gitRepo(Map config = [:]) {
     config = [
         method: 'GET',
         owner : '',
         repo  : '',
         path  : '',
         body  : ''
     ] + config

     withCredentials([usernamePassword(
         credentialsId: 'JenkinsGTTest',
         usernameVariable: 'USER',
         passwordVariable: 'TOKEN'
     )]) {

        def dataArg = config.body ? "-d '${config.body}'" : ""

        def response = sh(
            script: """
                curl -sL \
                -X ${config.method} \
                -H "Accept: application/vnd.github+json" \
                -H "Authorization: Bearer \$TOKEN" \
                -H "X-GitHub-Api-Version: 2022-11-28" \
                ${dataArg} \
                "https://api.github.com/repos/${config.owner}/${config.repo}/${config.path}"
            """,
            returnStdout: true
        ).trim()

         return readJSON(text: response)
     }
 }


/**
  * Pre-filled function for creating a new release with semantic versioning.
  *
  * @param config A map that contains the config for the Github API:
  *   - `repo` (String): The repo to create a release on. *Required*.
  *   - `version` (List): A list of each version type: major, minor, patch. ["1", "0", "0"]. *Required*.
  */
def createRelease(repo, version) {
    gitRepo(
         method: "POST",
         owner: "bindustries",
         repo: repo,
         path: "releases",
         body: """{
             "tag_name":"v${version[0]}.${version[1]}.${version[2]}",
             "target_commitish":"main",
             "name":"v${version[0]}.${version[1]}.${version[2]}",
             "body":"Description of the release",
             "draft":false,
             "prerelease":false,
             "generate_release_notes":false
         }"""
    )
}
