
wsimport -verbose -Xnocompile -p es.caib.regweb3.ws.sir.wssir8b -wsdllocation http://localhost:8080/regweb3/ws/sir/v3/WS_SIR8_B?wsdl -d src/main/java  WS_SIR8_B.wsdl

wsimport -verbose -Xnocompile -p es.caib.regweb3.ws.sir.wssir9 -wsdllocation http://localhost:8080/regweb3/ws/sir/v3/WS_SIR9?wsdl -d src/main/java  WS_SIR9.wsdl

REM java -cp %AXISCLASSPATH% org.apache.axis.wsdl.WSDL2Java src/main/resources/wsdl/WS_SIR8_B.wsdl -o src/main/java -p es.caib.regweb3.ws.sir.wssir8b
REM  call wsconsume -k src/main/resources/wsdl/WS_SIR9.wsdl -s src/main/java -n -p es.caib.regweb3.ws.sir.wssir9

