def call(Map config = [:]) {
    // Merge with default configuration
    def defaults = [
        enableBenchmarks: false,
        osList: ['linux', 'win', 'osx', 'freebsd'],
        rustVersion: 'stable',
        repo: '',
        branch: 'main',
        buildArgs: '',
        artifactPatterns: ['target/release/*']
    ]
    config = defaults << config  // Proper config merging

    // Switch to scripted pipeline for better dynamic stages
    def branches = [:]
    for (os in config.osList) {
        branches[os] = {
            node(os) {
                stage("${os} Clone") {
                    checkout scm
                }
                stage("${os} Install Rust") {
                    if (isUnix()) {
                        sh "curl --proto '=https' --tlsv1.2 -sSf https://sh.rustup.rs | sh -s -- -y --default-toolchain ${config.rustVersion}"
                        env.PATH = "${env.HOME}/.cargo/bin:${env.PATH}"
                    } else {
                        bat "curl -sSf -o rustup-init.exe https://win.rustup.rs"
                        bat "rustup-init.exe -y --default-toolchain ${config.rustVersion}"
                        env.PATH = "${env.USERPROFILE}\\.cargo\\bin;${env.PATH}"
                    }
                }
                stage("${os} Compile") {
                    rustCompile(config)
                }
                stage("${os} Test") {
                    rustTest(config)
                }
                if (config.enableBenchmarks) {
                    stage("${os} Benchmark") {
                        rustBenchmark(config)
                    }
                }
                stage("${os} Archive Artifacts") {
                    if (config.artifactPatterns) {
                        def pattern = config.artifactPatterns.join(',')
                        archiveArtifacts artifacts: pattern, allowEmptyArchive: true
                    }
                }
            }
        }
    }
    parallel branches
}


