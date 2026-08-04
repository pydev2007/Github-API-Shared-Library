@Library("shared-library@YOUR SHARED REPO/BRANCH") _

pipeline {
    agent { label 'YOUR AGENT HERE' }
    stages {
        stage('Discovery') {
            steps {
                script {
                    def config = readYaml text: libraryResource('repos.yaml')

                    def html = ""

                    for (repo in config.repos) {

                        def release = git.gitRepo(
                            owner: "ORG OR USER HERE",
                            repo: repo,
                            path: "releases/latest"
                        )

                        def compare = git.gitRepo(
                            owner: "ORG OR USER HERE",
                            repo: repo,
                            path: "compare/${release.name}...main" // "main" may change depending on the brach you want to compare.
                        )

                        if (compare.total_commits > 0) {
                            def section = config.discovery_config.html_template

                            section = section.replace('${repo.name}', repo)
                            section = section.replace('${commits}', "${compare.total_commits}")
                            section = section.replace('${url}', "${compare.html_url}")

                            html += section + "\n"
                        }
                        // If there are no commits, skip the repo.
                        else {
                            echo "No commits in ${repo}. Skipping."
                        }

                    }

                    writeFile(file: 'report.html', text: html) // Change the file location if needed.
                }
            }
        }
    }

    post {
        success {
            publishHTML(target: [
                allowMissing: false,
                alwaysLinkToLastBuild: true,
                keepAll: true,
                reportDir: '.', // Directory containing report.html.
                reportFiles: 'report.html',
                reportName: 'HTML Report',
                reportTitles: 'Commit Discovery'
            ])
            emailext(
                subject: "SUCCESS - Discovery Pipeline",
                mimeType: "text/html",
                attachmentsPattern: "report.html",
                to: "${env.SUCCESS_RECIPIENTS}",
                body: "<h2>SUCCESS - Discovery Pipeline</h2>")
            cleanWs()
        }
    }
}
