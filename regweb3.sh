#!/usr/bin/env bash

env mvn $@ -DskipTests clean install

rm -rf /Users/earrivi/Documents/iBit/servidores/jboss-5.2.0-caib/server/default/log
rm -rf /Users/earrivi/Documents/iBit/servidores/jboss-5.2.0-caib/server/default/tmp
rm -rf /Users/earrivi/Documents/iBit/servidores/jboss-5.2.0-caib/server/default/work

rm -rf /Users/earrivi/Documents/iBit/servidores/jboss-5.2.0-caib/server/default/deploy/regweb3.ear

cp ./ear/target/regweb3.ear /Users/earrivi/Documents/iBit/servidores/jboss-5.2.0-caib/server/default/deploy