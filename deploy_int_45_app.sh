#!/bin/sh

	sh release.sh
	scp build/deploy/jijin-app.war wls81opr@172.19.9.147:/wls/applications/jijin-app/apps/

ssh wls81opr@172.19.9.147 "~/bin/tomcatctl jijin-app restart &"

echo "Done"
