import groovy.json.JsonSlurper

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
                        -H "Authorization: Bearer ${TOKEN}" \
                        -H "X-GitHub-Api-Version: 2022-11-28" \
                        "https://api.github.com/repos/${config.owner}/${config.repo}/${config.path}"
                    """,
                    returnStdout: true
                ).trim()

        // Kept in case httpRequest is prefered
        //
        // def response = httpRequest(
        //     url: "https://api.github.com/repos/${config.owner}/${config.repo}/${config.path}",
        //     httpMode: 'GET',
        //     customHeaders: ["Accept: application/vnd.github+json", "Authorization: Bearer ${TOKEN}", "X-GitHub-Api-Version: 2026-03-10"],
        //     validResponseCodes: '200'
        // )
        //
        // def slurper = new JsonSlurper()

        // Schlurp before returning
        return response
    }
}

def gitRelease(String name) {

    def output = gitRepo("owner": "Bindustries", "repo": name, "path": "releases/latest")

    def release = new JsonSlurper().parseText(output)

    return release



}
