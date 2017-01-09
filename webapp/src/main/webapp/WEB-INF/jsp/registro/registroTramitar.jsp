<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

<c:if test="${param.tipoRegistro == 'registroEntrada'}">
    <c:url value="/registroEntrada/${registro.id}/enviarDestinatarios" var="urlDistribuir"/>
    <c:url value="/registroEntrada/${registro.id}/detalle" var="urlDetalle"/>
</c:if>

<%--Modal distribuir--%>
<div id="distribuirModal" class="modal fade bs-example-modal-lg">
    <spring:message code="registro.destinatarios.vacio"/>
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <h3><spring:message code="regweb.distribuir"/></h3>
            </div>

            <div class="modal-body">
                <div class="panel panel-info">

                    <div class="panel-heading">
                        <h3 class="panel-title"><i class="fa fa-pencil-square-o"></i> <strong>
                            <spring:message code="registro.destinatarios.listado"/></strong></h3>
                    </div>

                    <div class="panel-body">
                        <div class="row">
                            <div class="form-group col-xs-12 pad-left">
                                <div class="col-xs-3 pull-left etiqueta_regweb control-label text-right">
                                    <label><span class="text-danger">*</span> <spring:message code="registro.destinos"/></label>
                                </div>
                                <!-- Div donde se pinta el select multiple de los destinatarios posibles y propuestos (distribuir.js) - funcion distribuir() -->
                                <div class="col-xs-9" id="divdestinatarios">
                                    <span id="destinatariosErrors"></span>
                                </div>
                            </div>
                        </div>

                        <div class="row">
                            <div class="form-group col-xs-12 pad-left">
                                <div class="col-xs-3 pull-left etiqueta_regweb control-label text-right">
                                    <label><spring:message code="registroEntrada.observaciones"/></label>
                                </div>
                                <div class="col-xs-9">
                                    <textarea id="observtramit" name="observtramit" class="form-control"></textarea>
                                </div>
                            </div>
                        </div>

                    </div>
                </div>
                <div class="form-actions">
                    <input type="submit" class="btn btn-warning btn-sm" value="<spring:message
                        code="regweb.enviar"/>" onclick="enviarDestinatarios()">
                    <input type="button" value="Cancelar" class="btn btn-default btn-sm" data-dismiss="modal">
                </div>

            </div>
            <div class="modal-footer">
                <button class="btn" data-dismiss="modal" aria-hidden="true"><spring:message
                        code="regweb.cerrar"/></button>
            </div>
        </div>
    </div>
</div>



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