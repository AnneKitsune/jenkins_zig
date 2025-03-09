def call(Map config) {
    sh 'cargo test --all --verbose'
    junit '**/target/test-results/*.xml'
}


