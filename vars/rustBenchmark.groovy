def call(Map config) {
    command('''
        cargo bench --verbose
        # Process benchmark results
        awk '/^test/ {print $1,$2}' target/criterion/**/*.rs >> benchmarks.txt
    ''')
    archiveArtifacts 'benchmarks.txt'
}

