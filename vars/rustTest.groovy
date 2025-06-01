def call(Map config) {
    command('cargo test --all --verbose')
    junit '**/target/test-results/*.xml'
}


