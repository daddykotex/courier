#!/bin/bash -x
git status
if [ "$TRAVIS_PULL_REQUEST" = "false" ]; then
    sbt ci release
else
    sbt ci
fi