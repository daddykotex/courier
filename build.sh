#!/bin/bash
if [ "$TRAVIS_PULL_REQUEST" = "false" && "$TRAVIS_TAG" != "" ]; then
    sbt ci release
else
    sbt ci
fi