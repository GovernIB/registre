#!/bin/sh


mvn exec:java -Dexec.mainClass="org.fundaciobit.genapp.gensql.SqlGenerator" -Dexec.args="regweb $1"
