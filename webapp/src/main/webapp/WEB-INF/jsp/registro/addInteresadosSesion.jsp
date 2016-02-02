<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>
<un:useConstants var="RegwebConstantes" className="es.caib.regweb3.utils.RegwebConstantes"/>

<c:if test="${param.variable == RegwebConstantes.SESSION_INTERESADOS_ENTRADA}">
  <c:set var="interesados" value="${sessionScope[RegwebConstantes.SESSION_INTERESADOS_ENTRADA]}" />
</c:if>

<c:if test="${param.variable == RegwebConstantes.SESSION_INTERESADOS_SALIDA}">
  <c:set var="interesados" value="${sessionScope[RegwebConstantes.SESSION_INTERESADOS_SALIDA]}" />
</c:if>


<c:if test="${empty registro.id && not empty interesados}">

  <c:forEach var="interesado" items="${interesados}">
    <c:if test="${!interesado.isRepresentante}">

      <c:if test="${interesado.tipo == RegwebConstantes.TIPO_INTERESADO_ADMINISTRACION}">
        var nombre = "${interesado.nombreOrganismo}";
        nombre = nombre.replace(/\"/g,'&quot;');
        addOrganismoInteresadoHtml('${interesado.codigoDir3}',nombre,'<spring:message code="interesado.administracion"/>','${registro.registroDetalle.id}');
      </c:if>
      <c:if test="${interesado.tipo == RegwebConstantes.TIPO_INTERESADO_PERSONA_FISICA}">
        addInteresadoRepresentanteHtml('${interesado.id}','${interesado.nombrePersonaFisica}','<spring:message code="persona.fisica.corto"/>','${interesado.representante.id}','${interesado.representante.nombreCompleto}','${registro.registroDetalle.id}');
      </c:if>

      <c:if test="${interesado.tipo == RegwebConstantes.TIPO_INTERESADO_PERSONA_JURIDICA}">
        addInteresadoRepresentanteHtml('${interesado.id}','${interesado.nombrePersonaJuridica}','<spring:message code="persona.juridica.corto"/>','${interesado.representante.id}','${interesado.representante.nombreCompleto}','${registro.registroDetalle.id}');
      </c:if>
    </c:if>
  </c:forEach>

</c:if>
