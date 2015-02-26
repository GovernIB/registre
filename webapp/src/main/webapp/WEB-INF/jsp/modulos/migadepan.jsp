<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

<c:if test="${rolAutentidado.nombre == 'RWE_SUPERADMIN'}">
    <li><a href="<c:url value="/inici"/>" ><i class="fa fa-power-off"></i> <spring:message code="regweb.inicio"/></a></li>
</c:if>
<c:if test="${rolAutentidado.nombre == 'RWE_ADMIN'}">
    <c:if test="${fn:length(entidades) >= 1}">
        <li><a href="<c:url value="/inici"/>"><i class="fa fa-globe"></i> ${entidadActiva.nombre}</a></li>
    </c:if>
    <c:if test="${fn:length(entidades) == 0}">
        <li><a href="<c:url value="/inici"/>" ><i class="fa fa-power-off"></i> <spring:message code="regweb.inicio"/></a></li>
    </c:if>
</c:if>

<c:if test="${rolAutentidado.nombre == 'RWE_USUARI'}">
    <c:if test="${fn:length(oficinas) >= 1}">
        <li><a href="<c:url value="/inici"/>"><i class="fa fa-globe"></i> ${oficinaActiva.denominacion}</a></li>
    </c:if>
    <c:if test="${fn:length(oficinas) == 0}">
        <li><a href="<c:url value="/inici"/>"><i class="fa fa-globe"></i> <spring:message code="error.oficina.ninguna"/></a></li>
    </c:if>

    <%--Importamos el menú de avisos
    <c:import url="/avisos"/>--%>
</c:if>