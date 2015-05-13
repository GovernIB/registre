<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>
<un:useConstants var="RegwebConstantes" className="es.caib.regweb.utils.RegwebConstantes"/>

<c:if test="${empty registro.id && not empty interesados}">

  <c:forEach var="interesado" items="${interesados}">
    <c:if test="${!interesado.isRepresentante}">

      <c:if test="${interesado.tipo == RegwebConstantes.TIPO_INTERESADO_ADMINISTRACION}">  /*Organismo*/
        var nombre = "${interesado.nombre}";
        nombre = nombre.replace(/\"/g,'&quot;');
        addOrganismoInteresadoHtml('${interesado.codigoDir3}',nombre,'<spring:message code="interesado.administracion"/>','${registro.registroDetalle.id}');
      </c:if>
      <c:if test="${interesado.tipo == RegwebConstantes.TIPO_INTERESADO_PERSONA_FISICA}">  /*Fisica*/
        addInteresadoRepresentanteHtml('${interesado.id}','${interesado.nombrePersonaFisica}','<spring:message code="persona.fisica.corto"/>','${interesado.representante.id}','${registro.registroDetalle.id}');
      </c:if>

      <c:if test="${interesado.tipo == RegwebConstantes.TIPO_INTERESADO_PERSONA_JURIDICA}">  /*Juridica*/
        addInteresadoRepresentanteHtml('${interesado.id}','${interesado.nombrePersonaJuridica}','<spring:message code="persona.juridica.corto"/>','${interesado.representante.id}','${registro.registroDetalle.id}');
      </c:if>
    </c:if>
  </c:forEach>

</c:if>
