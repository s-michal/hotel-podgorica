#!/bin/bash

mvn install -Dmaven.test.skip=true
cd webapp
mvn tomcat7:run
