def call(Map config = [:]) {
    // Merge with default configuration
    def defaults = [
        enableBenchmarks: true,
        osList: ['linux', 'windows', 'macos'],
        rustVersion: 'stable',
        buildArgs: ''
    ]
    config = defaults << config

    pipeline {
        agent none
        stages {
            stage('Build & Test') {
                parallel {
                    config.osList.collect { os ->
                        stage("${os.capitalize()}") {
                            agent { label os }
                            stages {
                                stage('Compile') {
                                    steps { rustCompile(config) }
                                }
                                stage('Test') {
                                    steps { rustTest(config) }
                                }
                                stage('Benchmark') {
                                    when { expression { config.enableBenchmarks } }
                                    steps { rustBenchmark(config) }
                                }
                            }
                            post {
                                always {
                                    cleanWs()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


