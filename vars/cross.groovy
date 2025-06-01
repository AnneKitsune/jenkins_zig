def isUnixAgent = isUnix()

def command(String cmd) {
  echo "running command ${cmd}"
  if (isUnixAgent) {
    sh(cmd)
  } else {
    bat(cmd)
  }
}
