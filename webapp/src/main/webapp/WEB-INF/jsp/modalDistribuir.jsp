<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

<div id="modalDistribDestinatarios" class="modal fade bs-example-modal-lg">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h3><spring:message code="regweb.procesando"/></h3>
            </div>
            <div class="modal-body">
                <div class="row">
                    <div class="col-xs-4 centrat" id="cargadist">
                        <img src="<c:url value="/img/712.GIF"/>" width="60" height="60"/>
                    </div>
                </div>
                <div class="row">
                    <div class="col-xs-12" id="divlistdestinatarios">
                        <spring:message code="regweb.distribuyendo"/>
                        <ul id="listadestin">
                        </ul>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
            </div>
        </div>
    </div>
</div>
<!-- /.modal -->

