<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<c:if test="${empty reprosUsuario}">
    <li class="submenu-complet"><a href="<c:url value="/registroEntrada/new"/>"><spring:message
            code="registroEntrada.registroEntrada"/></a></li>
</c:if>

<c:if test="${not empty reprosUsuario}">
    <li class="dropdown-submenu-left toggle-left">
        <a href="<c:url value="/registroEntrada/new"/>"><i class="fa fa-chevron-left"></i> <spring:message
                code="registroEntrada.registroEntrada"/></a>
        <ul class="dropdown-menu">
            <c:forEach items="${reprosUsuario}" var="repro">
                <li><a href="<c:url value="/registroEntrada/new/${repro.id}"/>">${repro.nombre}</a></li>
            </c:forEach>
        </ul>
    </li>
</c:if>
