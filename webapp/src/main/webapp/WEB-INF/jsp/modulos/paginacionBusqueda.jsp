<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%--<script type="text/javascript">

    function exportar(formato){
        var formulario = '#'+'${param.entidad}Busqueda';
        $('#formato').val(formato);
        $(formulario).submit();
        $('#formato').val("");
    }

</script>

<div class="export">

    <div class="btn-group">
        <a class="btn dropdown-toggle" data-toggle="dropdown" href="javascript:void(0);">Exportar <span class="caret"></span></a>
        <ul class="dropdown-menu">
            <li><a href="javascript:void(0);" onclick="exportar('pdf')"><i class="icono-pdf"></i>Pdf</a></li>
            <li><a href="javascript:void(0);" onclick="exportar('excel')"><i class="icono-xls"></i>Excel</a></li>
        </ul>
    </div>

</div>--%>

<c:if test="${paginacion.totalPages > 1}">

    <script type="text/javascript">
        function postPagination(pageNumer){
            var formulario = '#'+'${param.entidad}Busqueda';
            $('#pageNumber').val(pageNumer);
            $(formulario).submit();
        }
    </script>

    <div class="row">
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
                        <li><a href="javascript:void(0);" title="Anterior" onclick="postPagination('${paginacion.currentIndex - 1}')">&laquo;</a></li>
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
                        <li><a href="javascript:void(0);" title="Següent" onclick="postPagination('${paginacion.currentIndex + 1}')">&raquo;</a></li>
                        <%--<li><a href="${lastUrl}">&gt;&gt;</a></li>--%>
                    </c:otherwise>
                </c:choose>
            </ul>
        </div>
    </div>

</c:if>