def call(Map config) {
    command("""
        #rustup override set ${config.rustVersion}
        cargo build ${config.buildArgs}
    """)
}

