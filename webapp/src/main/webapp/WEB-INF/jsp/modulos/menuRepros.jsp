<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%--Registro Entrada--%>
<c:if test="${tipoRegistro==1}">
    <c:if test="${empty reprosUsuario}">
        <li class="submenu-complet"><a href="<c:url value="/registroEntrada/new"/>"><i class="fa fa-file-o"></i> <spring:message code="registroEntrada.nuevo"/></a></li>
    </c:if>

    <c:if test="${not empty reprosUsuario}">
        <li class="dropdown-submenu-left toggle-left"><a href="<c:url value="/registroEntrada/new"/>"><i class="fa fa-chevron-left"></i> <i class="fa fa-file-o"></i> <spring:message code="registroEntrada.nuevo"/></a>
            <ul class="dropdown-menu scrollable-menu llistaReproMenu">
                <li class="pad_left-20"><i class="fa fa-briefcase"></i> <spring:message code="registroEntrada.nuevo.repro"/></li>
                <li class="divider"></li>
                <c:forEach items="${reprosUsuario}" var="repro">
                    <li class="llista"><a href="<c:url value="/registroEntrada/new/${repro.id}"/>" class="padLlista">${repro.nombre}</a></li>
                </c:forEach>
            </ul>
        </li>
    </c:if>
</c:if>

<%--Registro Salida--%>
<c:if test="${tipoRegistro==2}">
    <c:if test="${empty reprosUsuario}">
        <li class="submenu-complet"><a href="<c:url value="/registroSalida/new"/>"><i class="fa fa-file-o"></i> <spring:message code="registroSalida.nuevo"/></a></li>
    </c:if>

    <c:if test="${not empty reprosUsuario}">
        <li class="dropdown-submenu-left toggle-left"><a href="<c:url value="/registroSalida/new"/>"><i class="fa fa-chevron-left"></i> <i class="fa fa-file-o"></i> <spring:message code="registroSalida.nuevo"/></a>
            <ul class="dropdown-menu scrollable-menu llistaReproMenu">
                <li class="pad_left-20"><i class="fa fa-briefcase"></i> <spring:message code="registroEntrada.nuevo.repro"/></li>
                <li class="divider"></li>
                <c:forEach items="${reprosUsuario}" var="repro">
                    <li class="llista"><a href="<c:url value="/registroSalida/new/${repro.id}"/>" class="padLateral5">${repro.nombre}</a></li>
                </c:forEach>
            </ul>
        </li>
    </c:if>
</c:if>
