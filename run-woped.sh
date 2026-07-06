#!/usr/bin/env bash
# Build and start WoPeD (RunWoPeD) without a system-wide Maven install.
set -euo pipefail

ROOT="$(cd "$(dirname "$0")" && pwd)"
cd "$ROOT"

MAVEN_VERSION="3.9.11"
MAVEN_DIR="$ROOT/.tools/apache-maven-$MAVEN_VERSION"
MAVEN_TAR="https://archive.apache.org/dist/maven/maven-3/$MAVEN_VERSION/binaries/apache-maven-$MAVEN_VERSION-bin.tar.gz"

# Prefer JDK 21 (full JDK); fall back to whatever /usr/libexec/java_home finds.
if [ -z "${JAVA_HOME:-}" ]; then
  if /usr/libexec/java_home -v 21 >/dev/null 2>&1; then
    export JAVA_HOME="$(/usr/libexec/java_home -v 21)"
  else
    export JAVA_HOME="$(/usr/libexec/java_home)"
  fi
fi
export PATH="$JAVA_HOME/bin:$PATH"

if [ ! -x "$MAVEN_DIR/bin/mvn" ]; then
  echo "Downloading portable Maven $MAVEN_VERSION..."
  mkdir -p "$ROOT/.tools"
  curl -fsSL "$MAVEN_TAR" -o "$ROOT/.tools/maven.tar.gz"
  tar -xzf "$ROOT/.tools/maven.tar.gz" -C "$ROOT/.tools"
  rm "$ROOT/.tools/maven.tar.gz"
fi

MVN="$MAVEN_DIR/bin/mvn"

# Optional: GITHUB_TOKEN with read:packages scope (see README "Configuration for development").
if [ -n "${GITHUB_TOKEN:-}" ] && [ ! -f "$HOME/.m2/settings.xml" ]; then
  mkdir -p "$HOME/.m2"
  cat > "$HOME/.m2/settings.xml" <<EOF
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 https://maven.apache.org/xsd/settings-1.0.0.xsd">
  <servers>
    <server>
      <id>github</id>
      <username>${GITHUB_ACTOR:-github}</username>
      <password>${GITHUB_TOKEN}</password>
    </server>
  </servers>
</settings>
EOF
  echo "Created ~/.m2/settings.xml from GITHUB_TOKEN."
fi

echo "Building WoPeD-Starter (skip tests)..."
"$MVN" -pl WoPeD-Starter -am clean install -DskipTests --no-transfer-progress

JAR="$(ls -1 "$ROOT"/WoPeD-Starter/target/WoPeD-Starter-*-jar-with-dependencies.jar 2>/dev/null | head -1 || true)"
if [ -z "$JAR" ]; then
  JAR="$(ls -1 "$ROOT"/WoPeD-Starter/target/WoPeD-Starter-*.jar 2>/dev/null | grep -v 'jar-with-dependencies' | head -1 || true)"
fi

if [ -z "$JAR" ]; then
  echo "Build finished but no runnable JAR was found under WoPeD-Starter/target/."
  exit 1
fi

echo "Starting WoPeD: $JAR"
exec java -jar "$JAR"
