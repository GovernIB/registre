#!/bin/bash


env mvn versions:set -DnewVersion=$@


echo -e "\n"
echo -e "\n"
echo --------------------------- IMPORTANT ------------------------------
echo "|  El projectes del directori ws no s\'actualitzen automaticament. |"
echo "|  Per favor actualitzar la versio manualment.                     |"
echo --------------------------------------------------------------------
echo -e "\n"
echo -e "\n"
