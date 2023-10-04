REM set JAVA_HOME=C:\Program Files\Java\jdk1.7.0_80
REM set MAVEN_OPTS=-Xmx512m -XX:MaxPermSize=128m -Dhttps.protocols=TLSv1.2

REM mvn install -DskipTests -Dregenerateapi

REM El regenerateapi asi como está el pom ahora no lo hemos podido ejecutar a través de los plugins especificados en el
REM pom

REM se tiene que hacer directamente via consola con el wsconsume a partir de los wsdls

REM Ejecutado con java 11 y jboss EAP7
wsconsume -k  -w http://localhost:9080/regweb3/ws/v3/RegWebInfo?wsdl -s src/main/java -n -p es.caib.regweb3.ws.api.v3 -v -b ./bindings/bindings.xjc http://localhost:9080/regweb3/ws/v3/RegWebInfo?wsdl
wsconsume -k  -w http://localhost:9080/regweb3/ws/v3/RegWebPersonas?wsdl -s src/main/java -n -p es.caib.regweb3.ws.api.v3 -v -b ./bindings/bindings.xjc http://localhost:9080/regweb3/ws/v3/RegWebPersonas?wsdl
wsconsume -k  -w http://localhost:9080/regweb3/ws/v3/RegWebRegistroEntrada?wsdl -s src/main/java -n -p es.caib.regweb3.ws.api.v3 -v -b ./bindings/bindings.xjc http://localhost:9080/regweb3/ws/v3/RegWebRegistroEntrada?wsdl
wsconsume -k  -w http://localhost:9080/regweb3/ws/v3/RegWebRegistroSalida?wsdl -s src/main/java -n -p es.caib.regweb3.ws.api.v3 -v -b ./bindings/bindings.xjc http://localhost:9080/regweb3/ws/v3/RegWebRegistroSalida?wsdl
wsconsume -k  -w http://localhost:9080/regweb3/ws/v3/RegWebAsientoRegistral?wsdl -s src/main/java -n -p es.caib.regweb3.ws.api.v3 -v -b ./bindings/bindings.xjc http://localhost:9080/regweb3/ws/v3/RegWebAsientoRegistral?wsdl


