<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

<c:if test="${param.tipoRegistro == 'registroEntrada'}">
    <c:url value="/registroEntrada/${registro.id}/sello" var="urlSello"/>
    <c:url value="/registroEntrada/${registro.id}/enviardestinatarios" var="urlDistribuir"/>
    <c:url value="/registroEntrada/${registro.id}/detalle" var="urlDetalle"/>
</c:if>

<c:if test="${param.tipoRegistro == 'registroSalida'}">
    <c:url value="/registroSalida/${registro.id}/sello" var="urlSello"/>
    <%-- <c:url value="/registroSalida/${registro.id}/enviardestinatarios" var="urlDistribuir"/>
     <c:url value="/registroSalida/${registro.id}/detalle" var="urlDetalle"/>--%>
</c:if>

<%--Modal tramitar--%>
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
                            <div class="form-group col-xs-6 pad-left">
                                <div class="col-xs-6 pull-left etiqueta_regweb control-label">
                                    <label><span class="text-danger">*</span> <spring:message code="registro.destinos"/></label>
                                </div>
                                <!-- Div donde se pinta el select multiple de los destinatarios posibles y propuestos (distribuir.js) - funcion distribuir() -->
                                <div class="col-xs-6 no-pad-right" id="divdestinatarios">
                                    <span id="destinatariosErrors"></span>
                                </div>
                            </div>
                        </div>

                        <div class="row">
                            <div class="form-group col-xs-8">
                                <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                                    <label><spring:message code="registroEntrada.observaciones"/></label>
                                </div>
                                <div class="col-xs-8">
                                    <textarea id="observtramit" name="observtramit"></textarea>
                                </div>
                            </div>
                        </div>

                    </div>
                </div>
                <div class="form-actions">
                    <input type="submit" class="btn btn-warning btn-sm" value="<spring:message
                        code="regweb.enviar"/>" onclick="enviarDestinatarios('${urlDistribuir}','${urlDetalle}')">
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