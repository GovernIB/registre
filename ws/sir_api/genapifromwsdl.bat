set hostnameMINHAP=localhost:8380

wget http://%hostnameMINHAP%/services/WS_SIR6_A.wsdl?wsdl -O src/main/resources/wsdl/axis/WS_SIR6_A.wsdl
java -cp %AXISCLASSPATH% org.apache.axis.wsdl.WSDL2Java http://%hostnameMINHAP%/services/WS_SIR6_A.wsdl?wsdl -o src/main/java -p es.caib.regweb3.ws.sir.api.wssir6a

wget http://%hostnameMINHAP%/services/WS_SIR6_B.wsdl?wsdl -O src/main/resources/wsdl/axis/WS_SIR6_B.wsdl
java -cp %AXISCLASSPATH% org.apache.axis.wsdl.WSDL2Java http://%hostnameMINHAP%/services/WS_SIR6_B.wsdl?wsdl -o src/main/java -p es.caib.regweb3.ws.sir.api.wssir6b

wget http://%hostnameMINHAP%/services/WS_SIR7.wsdl?wsdl -O src/main/resources/wsdl/WS_SIR7.wsdl
java -cp %AXISCLASSPATH% org.apache.axis.wsdl.WSDL2Java http://%hostnameMINHAP%/services/WS_SIR7.wsdl?wsdl -o src/main/java -p es.caib.regweb3.ws.sir.api.wssir7

REM wget http://%hostnameMINHAP%/services/WS_SIR7.wsdl?wsdl -O src/main/resources/wsdl/WS_SIR7.wsdl
REM call wsconsume -k http://%hostnameMINHAP%/services/WS_SIR7.wsdl?wsdl -s src/main/java -n -p es.caib.regweb3.ws.sir.api.wssir7



