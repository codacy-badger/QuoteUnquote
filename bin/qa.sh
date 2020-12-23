#!/usr/bin/env bash

export JDK_HOME=$HOME/bin/android-studio/jre

cd ..

./gradlew clean

####################

# utilsLib/build/reports/lint-results-googleplayDebug.html
# cloudLib/build/reports/lint-results-googleplayDebug.html
# app/build/reports/lint-results-googleplayDebug.html
./gradlew lintGooglePlayDebug

# utilsLib/build/reports/lint-results-frdoidDebug.html
# cloudLib/build/reports/lint-results-frdoidDebug.html
# app/build/reports/lint-results-frdoidDebug.html
./gradlew lintFdroidDebug

####################

# ./gradlew ktlintformat
./gradlew ktlint

# ./gradlew detektBaseline
./gradlew detekt

####################

# assuming suitable emulator already up...

# app/build/jacoco/testDebugUnitTest.exec
# app/build/outputs/code_coverage/debugAndroidTest/connected/Pixel_3a_API_24(AVD) - 7.0-coverage.ec
#
# app/build/reports/tests/testDebugUnitTest/index.html
# app/build/reports/androidTests/connected/index.html
./gradlew testGoogleplayDebugUnitTest connectedGoogleplayDebugAndroidTest

####################

# combined coverage - assumes previous two tasks run
# app/build/reports/jacoco/index.html
./gradlew jacocoReport

./gradlew coveralls

####################

# CODECOV_TOKEN in env.
# https://codecov.io/gh/jameshnsears/QuoteUnquote
bash <(curl -s https://codecov.io/bash)

# https://sonarcloud.io/dashboard?id=jameshnsears_QuoteUnquote
./gradlew sonarqube -Dsonar.host.url=https://sonarcloud.io -Dsonar.login="$SONAR_TOKEN"
