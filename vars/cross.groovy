
def command(String cmd) {
  echo "Running command: ${cmd}"
  if (isUnix()) {
    sh script: cmd, returnStatus: false
  } else {
    bat script: cmd, returnStatus: false
  }
}
