
for /f "tokens=* delims=" %%x in (versio.txt) do set REGWEB_VERSIO=%%x

zip -r registre%1-%REGWEB_VERSIO%.zip scripts doc/pdf versio.txt ear/target/regweb.ear ws/regweb_api/target/regweb-ws-api-*.jar ws/regweb_api_axis/target/regweb-ws-api-axis-*.jar ws/sir_api/target/regweb-ws-sir-api-*.jar -x "**/.svn**"  -x "scripts/sqlgenenerator/**" -x "scripts/pom.xml" 