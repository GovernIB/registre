#!/bin/bash

env mvn -DgroupId=es.caib.regweb3 -DartifactId=* versions:set -DnewVersion=$@

