<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<c:if test="${paginacion.totalPages > 1}">

    <script type="text/javascript">
        function postPagination(pageNumer){
            var formulario = '#'+'${param.entidad}Busqueda';
            $('#pageNumber').val(pageNumer);
            $(formulario).submit();
        }
    </script>

    <div class="col-xs-12">

        <c:url var="firstUrl" value="/${param.entidad}/list/1" />
        <c:url var="lastUrl" value="/${param.entidad}/list/${paginacion.totalPages}" />
        <c:url var="prevUrl" value="/${param.entidad}/list/${paginacion.currentIndex - 1}" />
        <c:url var="nextUrl" value="/${param.entidad}/list/${paginacion.currentIndex + 1}" />

        <ul class="pagination">
            <c:choose>
                <c:when test="${paginacion.currentIndex == 1}">
                </c:when>
                <c:otherwise>
                    <li><a title="<spring:message code="regweb.primero"/>" href="javascript:void(0);" onclick="postPagination('1')">&laquo;</a></li>
                    <li><a title="<spring:message code="regweb.anterior"/>" href="javascript:void(0);" onclick="postPagination('${paginacion.currentIndex - 1}')">&lsaquo;</a></li>
                </c:otherwise>
            </c:choose>

            <c:forEach var="i" begin="${paginacion.beginIndex}" end="${paginacion.endIndex}">
                <c:url var="pageUrl" value="/${param.entidad}/list/${i}" />
                <c:choose>
                    <c:when test="${i == paginacion.currentIndex}">
                        <li class="active"><a href="javascript:void(0);" onclick="postPagination('${i}')"><c:out value="${i}" /></a></li>
                    </c:when>
                    <c:otherwise>
                        <li><a href="javascript:void(0);" onclick="postPagination('${i}')"><c:out value="${i}" /></a></li>
                    </c:otherwise>
                </c:choose>
            </c:forEach>

            <c:choose>
                <c:when test="${paginacion.currentIndex == paginacion.totalPages}">
                </c:when>
                <c:otherwise>
                    <li><a href="javascript:void(0);" title="<spring:message code="regweb.siguiente"/>" onclick="postPagination('${paginacion.currentIndex + 1}')">&rsaquo;</a></li>
                    <li><a href="javascript:void(0);" title="<spring:message code="regweb.ultimo"/>" onclick="postPagination('${paginacion.totalPages}')">&raquo;</a></li>
                </c:otherwise>
            </c:choose>
        </ul>
    </div>

</c:if>