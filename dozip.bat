
for /f "tokens=* delims=" %%x in (versio.txt) do set REGWEB_VERSIO=%%x

zip -r registre%1-%REGWEB_VERSIO%.zip scripts doc/pdf versio.txt ear/target/regweb3.ear ws/regweb3_api/target/regweb3-ws-api-*.jar ws/regweb3_api_axis/target/regweb3-ws-api-axis-*.jar ws/sir_api/target/regweb3-ws-sir-api-*.jar -x "**/.svn**"  -x "scripts/sqlgenenerator/**" -x "scripts/pom.xml"