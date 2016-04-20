#!/bin/bash
export GRADLE_OPTS="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=51003"
export root_dir=$0/..
gradle -p $root_dir jettyRun
