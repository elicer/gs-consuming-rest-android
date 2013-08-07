#!/bin/sh
cd $(dirname $0)
cd ../complete
mvn clean package
rm -rf target
cd ../initial
mvn clean package
rm -rf target
exit $ret
