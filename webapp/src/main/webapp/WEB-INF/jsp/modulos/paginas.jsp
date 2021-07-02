<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<c:if test="${paginacion.totalPages > 1}">
    <span class="pull-right">
        <spring:message code="regweb.pagina"/>
        <select id="irPagina" class="input-mini">
            <c:forEach var="indice" begin="1" end="${paginacion.totalPages}" step="1">
              <option value="${indice}" <c:if test="${indice == paginacion.currentIndex}">selected="selected"</c:if>>${indice}</option>
            </c:forEach>
        </select> de ${paginacion.totalPages}
    </span>
</c:if>

<c:if test="${paginacion.totalPages == 1}">
  <p class="pull-right"><spring:message code="regweb.pagina"/> <strong>${paginacion.currentIndex}</strong> de ${paginacion.totalPages}</p>
</c:if>