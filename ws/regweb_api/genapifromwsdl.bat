
set THEHOST=localhost:8080

wget http://%THEHOST%/regweb/ws/v3/RegWebHelloWorld?wsdl  -O src/main/resources/wsdl/RegWebHelloWorld.wsdl
call wsconsume -k http://%THEHOST%/regweb/ws/v3/RegWebHelloWorld?wsdl -s src/main/java -n -p es.caib.regweb.ws.api.v3

wget http://%THEHOST%/regweb/ws/v3/RegWebHelloWorldWithSecurity?wsdl  -O src/main/resources/wsdl/RegWebHelloWorldWithSecurity.wsdl
call wsconsume -k http://%THEHOST%/regweb/ws/v3/RegWebHelloWorldWithSecurity?wsdl -s src/main/java -n -p es.caib.regweb.ws.api.v3

wget http://%THEHOST%/regweb/ws/v3/RegWebPersonas?wsdl  -O src/main/resources/wsdl/RegWebPersonas.wsdl
call wsconsume -k http://%THEHOST%/regweb/ws/v3/RegWebPersonas?wsdl -s src/main/java -n -p es.caib.regweb.ws.api.v3

wget http://%THEHOST%/regweb/ws/v3/RegWebRegistroEntrada?wsdl  -O src/main/resources/wsdl/RegWebRegistroEntrada.wsdl
call wsconsume -k http://%THEHOST%/regweb/ws/v3/RegWebRegistroEntrada?wsdl -s src/main/java -n -p es.caib.regweb.ws.api.v3

wget http://%THEHOST%/regweb/ws/v3/RegWebRegistroSalida?wsdl  -O src/main/resources/wsdl/RegWebRegistroSalida.wsdl
call wsconsume -k http://%THEHOST%/regweb/ws/v3/RegWebRegistroSalida?wsdl -s src/main/java -n -p es.caib.regweb.ws.api.v3

wget http://%THEHOST%/regweb/ws/v3/RegWebInfo?wsdl  -O src/main/resources/wsdl/RegWebInfo.wsdl
call wsconsume -k http://%THEHOST%/regweb/ws/v3/RegWebInfo?wsdl -s src/main/java -n -p es.caib.regweb.ws.api.v3
