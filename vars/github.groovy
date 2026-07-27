def gitRepo(Map config) {
    withCredentials([usernamePassword(
                    credentialsId: 'JenkinsGTTest',
                    usernameVariable: 'USER',
                    passwordVariable: 'TOKEN'
                )])  {

        def response = sh """curl -L \
        -H "Accept: application/vnd.github+json" \
        -H "Authorization: Bearer ${TOKEN}" \
        -H "X-GitHub-Api-Version: 2026-03-10" \
        https://api.github.com/repos/${config.owner}/${config.repo}/${config.path}"""


        // Kept in case httpRequest is prefered
        //
        // def response = httpRequest(
        //     url: "https://api.github.com/repos/${config.owner}/${config.repo}/${config.path}",
        //     httpMode: 'GET',
        //     customHeaders: ["Accept: application/vnd.github+json", "Authorization: Bearer ${TOKEN}", "X-GitHub-Api-Version: 2026-03-10"],
        //     validResponseCodes: '200'
        // )

        return response.content
    }


}
