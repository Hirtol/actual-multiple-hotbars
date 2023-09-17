set shell := ["sh", "-c"]
set windows-shell := ["powershell.exe", "-NoLogo", "-Command"]

build-all:
  ./gradlew :forge:build
  ./gradlew :fabric:build
