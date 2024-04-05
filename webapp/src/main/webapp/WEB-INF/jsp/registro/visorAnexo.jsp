<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

<c:if test="${anexoFull.docMime == RegwebConstantes.MIME_PDF || anexoFull.signMime == RegwebConstantes.MIME_PDF}">

    <a data-toggle="modal" class="btn btn-info btn-default btn-xs"
       href="#visorAnexo${anexoFull.anexo.id}"
       title="<spring:message code="anexo.visualizar"/>"><span class="fa fa-search"></span></a>

    <div id="visorAnexo${anexoFull.anexo.id}" class="modal fade" role="dialog">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">x</button>
                    <h3 class="modal-title"><spring:message code="anexo.visualizar"/>: ${anexoFull.anexo.tituloCorto}</h3>
                </div>
                <div class="modal-body">
                    <object type="${anexoFull.docMime}" data="<c:url value="/anexo/descargar/${anexoFull.anexo.id}/false"/>" width="100%" height="700"></object>
                </div>
            </div>
        </div>
    </div>
</c:if>