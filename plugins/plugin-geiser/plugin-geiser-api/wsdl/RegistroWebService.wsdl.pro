<?xml version='1.0' encoding='UTF-8'?><wsdl:definitions xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/" xmlns:tns="http://ws.rgeco.geiser.minhap.gob.es/" xmlns:soap="http://schemas.xmlsoap.org/wsdl/soap/" xmlns:ns2="http://schemas.xmlsoap.org/soap/http" xmlns:ns1="http://registro.ws.rgeco.geiser.minhap.gob.es/" name="RegistroWebService" targetNamespace="http://ws.rgeco.geiser.minhap.gob.es/">
  <wsdl:import location="https://appint.seap.minhap.es/rgeco/services/RegistroWebService?wsdl=IRegistroWebService.wsdl" namespace="http://registro.ws.rgeco.geiser.minhap.gob.es/">
    </wsdl:import>
  <wsdl:binding name="RegistroWebServiceSoapBinding" type="ns1:IRegistroWebService">
    <soap:binding style="document" transport="http://schemas.xmlsoap.org/soap/http"/>
    <wsdl:operation name="registrarV2">
      <soap:operation soapAction="" style="document"/>
      <wsdl:input name="registrarV2">
        <soap:body use="literal"/>
      </wsdl:input>
      <wsdl:output name="registrarV2Response">
        <soap:body use="literal"/>
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="rechazar">
      <soap:operation soapAction="" style="document"/>
      <wsdl:input name="rechazar">
        <soap:body use="literal"/>
      </wsdl:input>
      <wsdl:output name="rechazarResponse">
        <soap:body use="literal"/>
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="iterar">
      <soap:operation soapAction="" style="document"/>
      <wsdl:input name="iterar">
        <soap:body use="literal"/>
      </wsdl:input>
      <wsdl:output name="iterarResponse">
        <soap:body use="literal"/>
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="consultarV2">
      <soap:operation soapAction="" style="document"/>
      <wsdl:input name="consultarV2">
        <soap:body use="literal"/>
      </wsdl:input>
      <wsdl:output name="consultarV2Response">
        <soap:body use="literal"/>
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="registrarEnviarHastaUnidad">
      <soap:operation soapAction="" style="document"/>
      <wsdl:input name="registrarEnviarHastaUnidad">
        <soap:body use="literal"/>
      </wsdl:input>
      <wsdl:output name="registrarEnviarHastaUnidadResponse">
        <soap:body use="literal"/>
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="buscarEstadoTramitacionV2">
      <soap:operation soapAction="" style="document"/>
      <wsdl:input name="buscarEstadoTramitacionV2">
        <soap:body use="literal"/>
      </wsdl:input>
      <wsdl:output name="buscarEstadoTramitacionV2Response">
        <soap:body use="literal"/>
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="buscarV2">
      <soap:operation soapAction="" style="document"/>
      <wsdl:input name="buscarV2">
        <soap:body use="literal"/>
      </wsdl:input>
      <wsdl:output name="buscarV2Response">
        <soap:body use="literal"/>
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="registrar">
      <soap:operation soapAction="" style="document"/>
      <wsdl:input name="registrar">
        <soap:body use="literal"/>
      </wsdl:input>
      <wsdl:output name="registrarResponse">
        <soap:body use="literal"/>
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="confirmar">
      <soap:operation soapAction="" style="document"/>
      <wsdl:input name="confirmar">
        <soap:body use="literal"/>
      </wsdl:input>
      <wsdl:output name="confirmarResponse">
        <soap:body use="literal"/>
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="registrarEnviarHastaUnidadV2">
      <soap:operation soapAction="" style="document"/>
      <wsdl:input name="registrarEnviarHastaUnidadV2">
        <soap:body use="literal"/>
      </wsdl:input>
      <wsdl:output name="registrarEnviarHastaUnidadV2Response">
        <soap:body use="literal"/>
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="consultar">
      <soap:operation soapAction="" style="document"/>
      <wsdl:input name="consultar">
        <soap:body use="literal"/>
      </wsdl:input>
      <wsdl:output name="consultarResponse">
        <soap:body use="literal"/>
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="buscarEstadoTramitacion">
      <soap:operation soapAction="" style="document"/>
      <wsdl:input name="buscarEstadoTramitacion">
        <soap:body use="literal"/>
      </wsdl:input>
      <wsdl:output name="buscarEstadoTramitacionResponse">
        <soap:body use="literal"/>
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="iterarV2">
      <soap:operation soapAction="" style="document"/>
      <wsdl:input name="iterarV2">
        <soap:body use="literal"/>
      </wsdl:input>
      <wsdl:output name="iterarV2Response">
        <soap:body use="literal"/>
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="buscar">
      <soap:operation soapAction="" style="document"/>
      <wsdl:input name="buscar">
        <soap:body use="literal"/>
      </wsdl:input>
      <wsdl:output name="buscarResponse">
        <soap:body use="literal"/>
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="registrarEnviar">
      <soap:operation soapAction="" style="document"/>
      <wsdl:input name="registrarEnviar">
        <soap:body use="literal"/>
      </wsdl:input>
      <wsdl:output name="registrarEnviarResponse">
        <soap:body use="literal"/>
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="registrarEnviarV2">
      <soap:operation soapAction="" style="document"/>
      <wsdl:input name="registrarEnviarV2">
        <soap:body use="literal"/>
      </wsdl:input>
      <wsdl:output name="registrarEnviarV2Response">
        <soap:body use="literal"/>
      </wsdl:output>
    </wsdl:operation>
  </wsdl:binding>
  <wsdl:service name="RegistroWebService">
    <wsdl:port binding="tns:RegistroWebServiceSoapBinding" name="RegistroWebServicePort">
      <soap:address location="https://appint.seap.minhap.es/rgeco/services/RegistroWebService"/>
    </wsdl:port>
  </wsdl:service>
</wsdl:definitions>