def call(Map config = [:]) {
    // Merge with default configuration
    def defaults = [
        enableBenchmarks: false,
        osList: ['linux', 'win', 'osx', 'freebsd'],
        repo: '',
        branch: 'main',
        buildArgs: '',
        artifactPatterns: ['zig-out/*/*']
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
                stage("${os} Setup Env Vars") {
                    if (isUnix()) {
                        env.PATH = "${env.HOME}/.cargo/bin:${env.PATH}:/zig/bin:/Users/user/zig/bin"
                    } else {
                        env.PATH = "${env.USERPROFILE}\\.cargo\\bin;${env.PATH};C:\\zig\\bin"
                    }
                }
                stage("${os} Compile") {
                    compile(config)
                }
                stage("${os} Test") {
                    test(config)
                }
                if (config.enableBenchmarks) {
                    stage("${os} Benchmark") {
                        benchmark(config)
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


