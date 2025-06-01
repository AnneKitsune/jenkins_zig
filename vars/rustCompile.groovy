def call(Map config) {
    command(["cargo", "build"] + config.buildArgs.tokenize())
}

