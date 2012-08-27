#!/bin/sh

DEVELOP_MODE=true

if [ -n "$1" ] ; then DEVELOP_MODE=$1; fi

echo "development mode: $DEVELOP_MODE"
echo "current path: `pwd`"

java -DdevelopmentMode=$DEVELOP_MODE -DprojectDir=`pwd` -cp lib/ContestApplet.jar com.topcoder.client.contestApplet.runner.generic www.topcoder.com 5001 http://tunnel1.topcoder.com/tunne?dummy TopCoder
