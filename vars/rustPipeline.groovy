def call(Map config = [:]) {
    // Merge with default configuration
    def defaults = [
        enableBenchmarks: false,
        osList: ['linux', 'win', 'osx', 'freebsd'],
        rustVersion: 'stable',
        buildArgs: ''
    ]
    config = defaults << config  // Proper config merging

    pipeline {
        agent none
        stages {
            stage('Cross-Platform Build') {
                steps {
                    script {  // Switch to scripted pipeline for dynamic stages
                        parallel(
                            config.osList.collectEntries { os ->
                                ["${os}": {
                                    node(os) {
                                        stage("${os} Clone") {
                                            checkout scm
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
                                    }
                                }]
                            }
                        )
                    }
                }
            }
        }
    }
}


