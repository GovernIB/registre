<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

<%--Modal distribuir--%>
<div id="distribuirModal" class="modal fade">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">x</button>
                <h3><spring:message code="distribuir.email"/></h3>
            </div>

            <div class="modal-body">
                <div class="panel panel-info">

                    <div class="panel-heading">
                        <h3 class="panel-title">
                            <i class="fa fa-pencil-square-o"></i> <strong><spring:message code="registro.destinatarios.listado"/></strong>
                        </h3>
                    </div>

                    <div class="panel-body">
                        <form id="distribuirForm" method="post" cssClass="form-horizontal" >
                            <!--Listado de emails -->
                            <div class="row">
                                <div class="form-group col-xs-12">
                                    <div class="col-xs-4 pull-left etiqueta_regweb control-label text-right">
                                        <label><span class="text-danger"> * </span><spring:message code="registroEntrada.emails.lista"/></label>
                                    </div>
                                    <div class="col-xs-8" id="idEmails">
                                        <textarea id="emails" name="emails" class="form-control" maxlength="256">${param.distribucionEmailDefault}</textarea>
                                        <span class="errors"></span>
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="form-group col-xs-12">
                                    <div class="col-xs-4 pull-left etiqueta_regweb control-label text-right">
                                        <label><span class="text-danger"> * </span><spring:message code="registroEntrada.motivo"/></label>
                                    </div>
                                    <div class="col-xs-8" id="idMotivo">
                                        <textarea id="motivo" name="motivo" class="form-control" maxlength="80">${param.distribucionAsuntoDefault}</textarea>
                                        <span class="errors"></span>
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="form-group col-xs-12">
                                    <div class="col-xs-12">
                                        <input type="button" value="<spring:message code="regweb.distribuir"/>" class="btn btn-warning btn-sm" onclick="validarFormEmail(${param.aceptarRegistroSir})"/>
                                        <button class="btn btn-sm" data-dismiss="modal" aria-hidden="true"><spring:message code="regweb.cerrar"/></button>
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