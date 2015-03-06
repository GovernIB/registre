#!/usr/bin/env bash

cat help.txt

env mvn $@ -DskipTests install 

if [ $? == 0 ]; then
  if [ "$REGWEB_DEPLOY_DIR" == "" ];  then
    echo  =================================================================
    echo    Definex la variable d\'entorn REGWEB_DEPLOY_DIR apuntant al
    echo    directori de deploy del JBOSS  i automaticament s\'hi copiara
    echo    l\'ear generat.
    echo  =================================================================  
  else
    if [ -f 'versio.txt' ]; then
	echo --------- COPIANT EAR `cat versio.txt` ---------
    else
	echo --------- COPIANT EAR ---------
    fi
    if [ -f './ear/target/regweb.ear' ]; then
      cp ./ear/target/regweb.ear $REGWEB_DEPLOY_DIR
    else
      echo NO S\'HA TROBAT regweb.ear!
    fi
  fi
fi
