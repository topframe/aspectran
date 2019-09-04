#!/bin/bash -e

basename=$(basename $0)
dirname=$(dirname $0)
dirname=$(cd "$dirname" && pwd)
cd "$dirname"

# show message and exit
function die() {
  echo "$1"
  exit 1
}

# show usage and exit
function usage() {
  echo "usage: $basename <command> [options]"
  exit 2
}

# run maven
function mvn() {
  ./mvnw $*
}

function command_rebuild() {
  mvn clean install $*
}

function command_demo() {
  exec demo/app/bin/shell.sh $*
}

command="$1"
if [ -z "$command" ]; then
  usage
fi
shift

case "$command" in
# build for CI
ci-build)
  mvn clean install $*
  ;;

# deploy for CI
ci-deploy)
  # get commit message
  commit_message=$(git log --format=%B -n 1)
  echo "Current commit detected: ${commit_message}"
  echo "Travis JDK Version: $TRAVIS_JDK_VERSION"
  echo "Travis Pull Request: $TRAVIS_PULL_REQUEST"
  echo "Travis Branch: $TRAVIS_BRANCH"
  if [ "$TRAVIS_JDK_VERSION" == 'openjdk8' ] && [ "$TRAVIS_PULL_REQUEST" == 'false' ] && [[ "$commit_message" != *"[maven-release-plugin]"* ]]; then
    # deploy to sonatype
    mvn clean deploy -q -P skip-test $*
    echo "Successfully deployed SNAPSHOT artifacts to Sonatype under Travis job ${TRAVIS_JOB_NUMBER}"
    # coverage reporting of coveralls
    mvn clean test jacoco:report coveralls:report -q $*
    echo "Successfully ran coveralls under Travis job ${TRAVIS_JOB_NUMBER}"
  else
    echo "Travis deploy skipped"
  fi
  ;;

# prepare release
release-prepare)
  mvn release:clean release:prepare
  ;;

# perform release
release-perform)
  mvn release:perform
  ;;

# clean release
release-clean)
  mvn release:clean
  ;;

# rollback release
release-rollback)
  mvn release:rollback
  ;;

*)
  # attempt to lookup command function
  fn="command_$command"
  if [ "$(type -t $fn)" = 'function' ]; then
    $fn $*
  else
    # complain about missing command function
    echo "Unknown command: $command"
    usage
  fi
  ;;
esac
