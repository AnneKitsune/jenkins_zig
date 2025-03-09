def call(Map config) {
    sh """
        rustup override set ${config.rustVersion}
        cargo build ${config.buildArgs}
    """
}

