#!/usr/bin/env bash

../gradlew :spotify-cli-jvm:install

bash ./build/install/spotify-cli-jvm/bin/spotify-cli-jvm $@