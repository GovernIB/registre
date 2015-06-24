<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>
<un:useConstants var="RegwebConstantes" className="es.caib.regweb3.utils.RegwebConstantes"/>

  <c:forEach var="interesado" items="${registro.registroDetalle.interesados}">
    <c:if test="${!interesado.isRepresentante}">
      <c:choose>
        <c:when test="${interesado.tipo == RegwebConstantes.TIPO_INTERESADO_ADMINISTRACION}">
          var nombre = '<c:out value="${interesado.nombre}" escapeXml="true"/>';
          nombre = nombre.replace(/\"/g,'&quot;');
          nombre = nombre.replace(/'/g, "\\'");
          addOrganismoInteresadoHtml('${interesado.codigoDir3}',nombre,'<spring:message code="interesado.administracion"/>','${registro.registroDetalle.id}');
        </c:when>
        <c:when test="${interesado.tipo == RegwebConstantes.TIPO_INTERESADO_PERSONA_FISICA}">
          addInteresadoRepresentanteHtml('${interesado.id}','<c:out value="${interesado.nombrePersonaFisica}" escapeXml="true"/>','<spring:message code="persona.fisica"/>' ,'${interesado.representante.id}','${registro.registroDetalle.id}');
        </c:when>
        <c:when test="${interesado.tipo == RegwebConstantes.TIPO_INTERESADO_PERSONA_JURIDICA}">
          addInteresadoRepresentanteHtml('${interesado.id}','<c:out value="${interesado.nombrePersonaJuridica}" escapeXml="true"/>','<spring:message code="persona.juridica"/>' ,'${interesado.representante.id}','${registro.registroDetalle.id}');
        </c:when>
      </c:choose>
    </c:if>
  </c:forEach>


