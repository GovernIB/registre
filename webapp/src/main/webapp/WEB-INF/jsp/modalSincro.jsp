<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

<div id="modalSincro" class="modal fade bs-example-modal-lg">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h3><spring:message code="regweb.procesando"/></h3>
            </div>
            <div class="modal-body">
                <div class="col-xs-4 centrat" id="carga">
                    <img src="<c:url value="/img/712.GIF"/>" width="60" height="60"/>
                </div>
            </div>
        </div>
    </div>
</div>
<!-- /.modal -->