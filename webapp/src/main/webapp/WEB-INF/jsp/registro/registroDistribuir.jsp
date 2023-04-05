<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

<%--Modal distribuir--%>
<div id="distribuirModal" class="modal fade">
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
                        <form id="distribuirForm" method="post"  cssClass="form-horizontal" >
                            <!--Listado de emails -->
                            <div class="row">
                                <div class="form-group col-xs-8">
                                    <div class="col-xs-3 pull-left etiqueta_regweb control-label text-right">
                                        <label><span class="text-danger"> * </span><spring:message code="registroEntrada.emails.lista"/></label>
                                    </div>
                                    <div class="col-xs-9" id="idEmails">
                                        <textarea id="emails" name="emails" class="form-control" maxlength="256"></textarea>
                                        <span class="errors"></span>
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="form-group col-xs-8">
                                    <div class="col-xs-3 pull-left etiqueta_regweb control-label text-right">
                                        <label><span class="text-danger"> * </span><spring:message code="registroEntrada.motivo"/></label>
                                    </div>
                                    <div class="col-xs-9" id="idMotivo">
                                        <textarea id="motivo" name="motivo" class="form-control" maxlength="80"></textarea>
                                        <span class="errors"></span>
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="form-group col-xs-12">
                                    <div class="col-xs-12">
                                        <input type="button" value="<spring:message code="regweb.distribuir"/>" class="btn btn-warning btn-sm" onclick="validarFormEmail(${param.aceptarRegistroSir})"/>

                                        <c:if test="${tipoRegistro == RegwebConstantes.REGISTRO_ENTRADA}">
                                            <input type="button" value="<spring:message code="regweb.cancelar"/>" onclick="goTo('<c:url value="/registroEntrada/${registro.id}/detalle"/>')" class="btn btn-sm"/>
                                        </c:if>
                                        <c:if test="${tipoRegistro == RegwebConstantes.REGISTRO_SALIDA}">
                                            <input type="button" value="<spring:message code="regweb.cancelar"/>" onclick="goTo('<c:url value="/registroSalida/${registro.id}/detalle"/>')" class="btn btn-sm"/>
                                        </c:if>

                                    </div>
                                </div>
                            </div>
                        </form>
                    </div>

                </div>

            </div>
        </div>
    </div>
</div>