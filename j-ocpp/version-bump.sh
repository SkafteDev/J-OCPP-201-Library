#!/bin/bash

# Script to bump Maven module versions

# Usage function to display help
usage() {
    cat << EOF
Usage: $0 [OPTIONS]
Options:
  -s, --snapshot                Append -SNAPSHOT to the version
  --bump-major                  Bump the major version (reset minor and incremental)
  --bump-minor                  Bump the minor version (reset incremental)
  --bump-incremental            Bump the incremental version
  --set VERSION                 Set the version directly (e.g., --set 1.0.1)
  --remove-snapshot             Remove the -SNAPSHOT postfix if it exists
  --get-version                 Get the current version
  -h, --help                    Show this help message
EOF
}

# Functions for version operations
commit() {
    mvn versions:commit
}

get_version() {
    mvn -q -Dexec.executable=echo -Dexec.args='${project.version}' --non-recursive exec:exec
}

set_version() {
    local version=$1
    mvn build-helper:parse-version versions:set -DnewVersion="${version}${SNAPSHOT}"
    commit
}

remove_snapshot() {
    mvn build-helper:parse-version versions:set -DnewVersion='${parsedVersion.majorVersion}.${parsedVersion.minorVersion}.${parsedVersion.incrementalVersion}'
    commit
}

set_snapshot() {
    mvn build-helper:parse-version versions:set -DnewVersion='${parsedVersion.majorVersion}.${parsedVersion.minorVersion}.${parsedVersion.incrementalVersion}-SNAPSHOT'
    commit
}

bump_incremental() {
    mvn build-helper:parse-version versions:set -DnewVersion='${parsedVersion.majorVersion}.${parsedVersion.minorVersion}.${parsedVersion.nextIncrementalVersion}'${SNAPSHOT}
    commit
}

bump_minor() {
    mvn build-helper:parse-version versions:set -DnewVersion='${parsedVersion.majorVersion}.${parsedVersion.nextMinorVersion}.0'${SNAPSHOT}
    commit
}

bump_major() {
    mvn build-helper:parse-version versions:set -DnewVersion='${parsedVersion.nextMajorVersion}.0.0'${SNAPSHOT}
    commit
}

# Parse arguments
SNAPSHOT=""
while [[ "$#" -gt 0 ]]; do
    case $1 in
        -s|--snapshot) SNAPSHOT="-SNAPSHOT" ;;
        --bump-major) bump_major; exit ;;
        --bump-minor) bump_minor; exit ;;
        --bump-incremental) bump_incremental; exit ;;
        --set) shift; set_version "$1"; exit ;;
        --remove-snapshot) remove_snapshot; exit ;;
        --get-version) get_version; exit ;;
        -h|--help) usage; exit ;;
        *) echo "Unknown option: $1" >&2; usage; exit 1 ;;
    esac
    shift
done

if [[ $# -eq 0 ]]; then
    usage
    exit 1
fi
