def call(Map config) {
    cross.command("cargo build ${config.buildArgs}")
}

