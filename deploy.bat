@echo off
echo
type help.txt
echo  

cmd /C mvn -DskipTests %* clean install

rem Perfiles de escaneo
rem mvn -DskipTests -Dhttps.protocols=TLSv1.2 clean install -Piecisascanweb -Pdwtscanweb -Pdigitalibscanweb -Pdigitalibmassivescanweb

if %errorlevel% EQU 0 (

	@echo off
	IF DEFINED REGWEB_DEPLOY_DIR (
      for /f "tokens=* delims=" %%x in (versio.txt) do set REGWEB_VERSIO=%%x
	  @echo on
	  echo --------- COPIANT EAR %REGWEB_VERSIO% ---------

	  xcopy /Y ear\target\regweb3.ear %REGWEB_DEPLOY_DIR%

	) ELSE (
	  echo  =================================================================
	  echo    Definex la variable d'entorn REGWEB_DEPLOY_DIR apuntant al
	  echo    directori de deploy del JBOSS  i automaticament s'hi copiara
	  echo    l'ear generat.
	  echo  =================================================================
	) 

)