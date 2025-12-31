#!/usr/bin/env sh
set -e

mkdir -p gradle/wrapper

echo "Downloading official Gradle wrapper jar..."
curl -L https://services.gradle.org/distributions/gradle-8.6-bin.zip -o /tmp/gradle.zip

unzip -p /tmp/gradle.zip gradle-8.6/lib/gradle-wrapper-8.6.jar > gradle/wrapper/gradle-wrapper.jar

rm /tmp/gradle.zip

echo "Gradle wrapper jar installed."
