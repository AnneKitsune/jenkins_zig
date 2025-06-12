
def command(String cmd) {
  echo "Running command: ${cmd}"
  if (isUnix()) {
    sh script: cmd, returnStatus: false
  } else {
    bat script: cmd, returnStatus: false
  }
}

def isUnix() {
    return (env['PATH'] ?: '').contains('/bin') || 
           (env['PATH'] ?: '').contains('/usr/bin') || 
           env['SHELL']?.contains('sh') || 
           env['SHELL']?.contains('bash') || 
           env['SHELL']?.contains('zsh')
}
