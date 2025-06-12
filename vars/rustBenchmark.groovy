def call(Map config) {
    cross.command('cargo bench --verbose')
    archiveArtifacts 'target/criterion/**/*'
}

