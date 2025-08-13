def call(Map config) {
    cross.command("zig build ${config.buildArgs}")
}

