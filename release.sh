#!/bin/bash

root_dir=$0/..

WAR_FILE=jijin-app.war

echo ------------package war begin------------

gradle -p $root_dir clean war

zip -d build/libs/$WAR_FILE WEB-INF/lib/grizzly-servlet-webserver-1.9.35.jar
zip -d build/libs/$WAR_FILE WEB-INF/lib/javax.servlet-3.0.jar

echo ------------package war end------------

mkdir -p build/deploy/config
cp build/libs/$WAR_FILE build/deploy
cp src/main/resources/*.properties build/deploy/config
cp release/* build/deploy


