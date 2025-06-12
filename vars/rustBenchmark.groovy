def call(Map config) {
    cross.command('cargo bench')
    archiveArtifacts 'target/criterion/**/*'
}

