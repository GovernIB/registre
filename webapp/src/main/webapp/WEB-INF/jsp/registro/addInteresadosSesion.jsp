<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>
<un:useConstants var="RegwebConstantes" className="es.caib.regweb3.utils.RegwebConstantes"/>

<c:if test="${param.variable == RegwebConstantes.SESSION_INTERESADOS_ENTRADA}">
    <c:set var="interesados" value="${sessionScope[RegwebConstantes.SESSION_INTERESADOS_ENTRADA]}"/>
</c:if>

<c:if test="${param.variable == RegwebConstantes.SESSION_INTERESADOS_SALIDA}">
    <c:set var="interesados" value="${sessionScope[RegwebConstantes.SESSION_INTERESADOS_SALIDA]}"/>
</c:if>


<c:if test="${empty registro.id && not empty interesados}">

    <c:forEach var="interesado" items="${interesados}">
        <c:if test="${!interesado.isRepresentante}">

            <c:if test="${interesado.tipo == RegwebConstantes.TIPO_INTERESADO_ADMINISTRACION}">
                var nombre = "${interesado.nombreOrganismo}";
                nombre = nombre.replace(/\"/g,'&quot;');
                addOrganismoInteresadoHtml('${interesado.codigoDir3}',nombre,'<spring:message
                    code="interesado.administracion"/>','${registro.registroDetalle.id}',false);
            </c:if>
            <c:if test="${interesado.tipo == RegwebConstantes.TIPO_INTERESADO_PERSONA_FISICA}">
                var interesado = "${interesado.nombrePersonaFisica}";
                interesado = interesado.replace(/\"/g,'&quot;');
                var representante = "${interesado.representante.nombreCompleto}";
                representante = representante.replace(/\"/g,'&quot;');
                addInteresadoRepresentanteHtml('${interesado.id}',interesado,'<spring:message
                    code="persona.fisica"/>','${interesado.representante.id}',representante,'${registro.registroDetalle.id}');
            </c:if>

            <c:if test="${interesado.tipo == RegwebConstantes.TIPO_INTERESADO_PERSONA_JURIDICA}">
                var interesado = "${interesado.nombrePersonaJuridica}";
                interesado = interesado.replace(/\"/g,'&quot;');
                var representante = "${interesado.representante.nombreCompleto}";
                representante = representante.replace(/\"/g,'&quot;');
                addInteresadoRepresentanteHtml('${interesado.id}',interesado,'<spring:message
                    code="persona.juridica"/>','${interesado.representante.id}',representante,'${registro.registroDetalle.id}');
            </c:if>
        </c:if>
    </c:forEach>

</c:if>
