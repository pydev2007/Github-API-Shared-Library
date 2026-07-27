import groovy.json.JsonSlurper

def gitRepo(Map config) {
    withCredentials([usernamePassword(
                    credentialsId: 'JenkinsGTTest',
                    usernameVariable: 'USER',
                    passwordVariable: 'TOKEN'
                )])  {

        def json = new JsonSlurper()

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

        // Schlurp before returning
        return json.parseText(response)
    }


}
