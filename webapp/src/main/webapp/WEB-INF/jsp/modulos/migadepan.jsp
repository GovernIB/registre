<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

<c:if test="${loginInfo.rolActivo.nombre == 'RWE_SUPERADMIN'}">
    <li><a href="<c:url value="/inici"/>" ><i class="fa fa-power-off"></i> <spring:message code="regweb.inicio"/></a></li>
</c:if>
<c:if test="${loginInfo.rolActivo.nombre == 'RWE_ADMIN'}">
    <c:if test="${fn:length(loginInfo.entidades) >= 1}">
        <li><a href="<c:url value="/inici"/>"><i class="fa fa-institution"></i> ${loginInfo.entidadActiva.nombre}</a></li>
    </c:if>
    <c:if test="${fn:length(loginInfo.entidades) == 0}">
        <li><a href="<c:url value="/inici"/>" ><i class="fa fa-power-off"></i> <spring:message code="regweb.inicio"/></a></li>
    </c:if>
</c:if>

<c:if test="${loginInfo.rolActivo.nombre == 'RWE_USUARI'}">

    <%--Importamos el menú de avisos--%>
    <c:if test="${param.avisos == true}">
        <c:import url="/avisos"/>
    </c:if>

    <c:if test="${fn:length(loginInfo.oficinasAcceso) >= 1}">
        <li><a rel="popupAbajo" data-content="${loginInfo.oficinaActiva.codigo}" <c:if test="${loginInfo.oficinaActiva.sirEnvio || loginInfo.oficinaActiva.sirRecepcion}">class="azul"</c:if> href="<c:url value="/inici"/>"><i class="fa fa-home"></i> ${loginInfo.oficinaActiva.denominacion} <c:if test="${loginInfo.oficinaActiva.oamr}"><i class="fa fa-star"></i></c:if></a></li>
    </c:if>
    <c:if test="${fn:length(loginInfo.oficinasAcceso) == 0}">
        <li><a href="<c:url value="/inici"/>"><i class="fa fa-institution"></i> ${loginInfo.entidadActiva.nombre}</a></li>
        <li><i class="fa fa-home"></i> <spring:message code="error.oficina.ninguna"/></li>
    </c:if>

</c:if>