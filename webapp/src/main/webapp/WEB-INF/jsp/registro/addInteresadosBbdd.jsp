<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>


  <c:forEach var="interesado" items="${registro.registroDetalle.interesados}">
    <c:if test="${!interesado.isRepresentante}">
      <c:choose>
        <c:when test="${interesado.tipo == RegwebConstantes.TIPO_INTERESADO_ADMINISTRACION}">
          var nombre = '<c:out value="${interesado.nombreOrganismo}" escapeXml="true"/>';
          nombre = nombre.replace(/\"/g,'&quot;');
          nombre = nombre.replace(/'/g, "\\'");
          addOrganismoInteresadoHtml('${interesado.codigoDir3}',nombre,'<spring:message
                code="interesado.administracion"/>','${registro.registroDetalle.id}',false);
        </c:when>
        <c:when test="${interesado.tipo == RegwebConstantes.TIPO_INTERESADO_PERSONA_FISICA}">
          var interesado = '<c:out value="${interesado.nombrePersonaFisica}" escapeXml="true"/>';
          interesado = interesado.replace(/\"/g,'&quot;');
          var representante = "${interesado.representante.nombreCompleto}";
          representante = representante.replace(/\"/g,'&quot;');
          addInteresadoRepresentanteHtml('${interesado.id}',interesado,'<spring:message
                code="persona.fisica"/>' ,'${interesado.representante.id}',representante,'${registro.registroDetalle.id}');
        </c:when>
        <c:when test="${interesado.tipo == RegwebConstantes.TIPO_INTERESADO_PERSONA_JURIDICA}">
          var interesado = '<c:out value="${interesado.nombrePersonaJuridica}" escapeXml="true"/>';
          interesado = interesado.replace(/\"/g,'&quot;');
          var representante = "${interesado.representante.nombreCompleto}";
          representante = representante.replace(/\"/g,'&quot;');
          addInteresadoRepresentanteHtml('${interesado.id}',interesado,'<spring:message
                code="persona.juridica"/>' ,'${interesado.representante.id}',representante,'${registro.registroDetalle.id}');
        </c:when>
      </c:choose>
    </c:if>
  </c:forEach>


