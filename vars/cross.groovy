def isUnixAgent = isUnix()

def command(String cmd) {
  if (isUnixAgent) {
    sh(cmd)
  } else {
    bat(cmd)
  }
}
