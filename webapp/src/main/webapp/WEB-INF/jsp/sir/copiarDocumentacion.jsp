<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

<!DOCTYPE html>
<html lang="ca">
    <head>
        <title><spring:message code="regweb.titulo"/></title>
        <c:import url="../modulos/imports.jsp"/>
    </head>

<body>

<c:import url="../modulos/menu.jsp"/>

<div class="row-fluid container main">

    <div class="well well-white">

        <div class="row">
            <div class="col-xs-12">
                <ol class="breadcrumb">
                    <c:import url="../modulos/migadepan.jsp"/>
                    <li class="active"><i class="fa fa-list-ul"></i> <strong><spring:message code="registroSir.registrosSir"/></strong></li>
                </ol>
            </div>
        </div><!-- /.row -->

        <div id="mensajes"></div>

        <c:import url="../modulos/mensajes.jsp"/>

        <!-- BUSCADOR -->

        <div class="row">

            <div class="col-xs-12">

                <div class="panel panel-warning">

                    <div class="panel-heading">
                        <h3 class="panel-title"><i class="fa fa-search"></i><strong> Procesar y generar documentación de Registros sir</strong> </h3>
                    </div>

                    <form:form modelAttribute="erteBusquedaForm" method="post" cssClass="form-horizontal">
                        <form:hidden path="pageNumber"/>

                        <div class="panel-body">

                            <div class="col-xs-12">
                                <div class="col-xs-6 espaiLinies">
                                    <div class="col-xs-4 pull-left etiqueta_regweb">
                                        <label for="registroSir.codigoEntidadRegistral" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.oficina.busqueda"/>" data-toggle="popover"><spring:message code="registro.oficinaRegistro"/></label>
                                    </div>
                                    <div class="col-xs-8">
                                        <form:select items="${oficinasSir}" itemLabel="${denominacion}" itemValue="${codigo}" path="registroSir.codigoEntidadRegistral" cssClass="chosen-select"/>
                                    </div>
                                </div>

                            </div>

                            <div class="col-xs-12">

                                <div class="col-xs-6 espaiLinies">
                                    <div class="col-xs-4 pull-left etiqueta_regweb">
                                        <label for="estado" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.estadoSir.busqueda"/>" data-toggle="popover"><spring:message code="registroSir.estado"/></label>
                                    </div>
                                    <div class="col-xs-8">
                                        <form:select path="estado" cssClass="chosen-select">
                                            <form:option value="" label="..."/>
                                            <c:forEach var="estado" items="${estados}">
                                                <form:option value="${estado.value}"><spring:message code="registroSir.estado.${estado.value}"/></form:option>
                                            </c:forEach>
                                        </form:select>
                                    </div>
                                </div>

                                <div class="col-xs-6 espaiLinies">
                                    <div class="col-xs-4 pull-left etiqueta_regweb">
                                        <label for="registroSir.aplicacion" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.aplicacion.busqueda"/>" data-toggle="popover"><spring:message code="registroSir.aplicacion"/></label>
                                    </div>
                                    <div class="col-xs-8">
                                        <form:input path="registroSir.aplicacion" maxlength="4" cssClass="form-control"/>
                                    </div>
                                </div>
                            </div>

                            <div class="col-xs-12">
                                <div class="col-xs-6 espaiLinies">
                                    <div class="col-xs-4 pull-left etiqueta_regweb">
                                        <label for="fechaInicio" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.inicioEnviadoSir.busqueda"/>" data-toggle="popover"><spring:message code="regweb.fechainicio"/></label>
                                    </div>
                                    <div class="col-xs-8" id="fechaInicio">
                                        <div class="input-group date no-pad-right">
                                            <form:input path="fechaInicio" type="text" cssClass="form-control"  maxlength="10" placeholder="dd/mm/yyyy" name="fechaInicio"/>
                                            <span class="input-group-addon"><span class="fa fa-calendar"></span></span>
                                        </div>
                                    </div>
                                </div>

                                <div class="col-xs-6 espaiLinies">
                                    <div class="col-xs-4 pull-left etiqueta_regweb">
                                        <label for="fechaFin" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.finEnviadoSir.busqueda"/>" data-toggle="popover"><spring:message code="regweb.fechafin"/></label>
                                    </div>
                                    <div class="col-xs-8" id="fechaFin">
                                        <div class="input-group date no-pad-right">
                                            <form:input path="fechaFin" type="text" cssClass="form-control"  maxlength="10" placeholder="dd/mm/yyyy" name="fechaFin"/>
                                            <span class="input-group-addon"><span class="fa fa-calendar"></span></span>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-xs-6 espaiLinies">
                                    <div class="col-xs-4 pull-left etiqueta_regweb">
                                        <label for="total" rel="popupAbajo" data-content="" data-toggle="popover">Total</label>
                                    </div>
                                    <div class="col-xs-8" id="total">
                                        <div class="input-group date no-pad-right">
                                            <form:input path="total" type="text" cssClass="form-control"  maxlength="10" name="total"/>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="form-group col-xs-12">
                                <button type="submit" class="btn btn-warning btn-sm"><spring:message code="regweb.buscar"/></button>
                            </div>
                    </form:form>

                </div>
            </div>
        </div>
    </div>

</div>
</div> <!-- /container -->

<c:import url="../modulos/pie.jsp"/>


<script type="text/javascript">

    var urlEnviarACK = '<c:url value="/sir/enviarACK"/>';
    var urlEnviarConfirmacion = '<c:url value="/sir/enviarConfirmacion"/>';
    var tradsMensajeControl = [];
    var tradsSir = [];
    tradsMensajeControl['mensajeControl.ACK.enviado.ok'] = "<spring:message code='mensajeControl.ACK.enviado.ok' javaScriptEscape='true' />";
    tradsMensajeControl['mensajeControl.ACK.enviado.error'] = "<spring:message code='mensajeControl.ACK.enviado.error' javaScriptEscape='true' />";
    tradsMensajeControl['mensajeControl.confirmacion.enviado.ok'] = "<spring:message code='mensajeControl.confirmacion.enviado.ok' javaScriptEscape='true' />";
    tradsMensajeControl['mensajeControl.confirmacion.enviado.error'] = "<spring:message code='mensajeControl.confirmacion.enviado.error' javaScriptEscape='true' />";
    tradsSir['registroSir.reiniciar.ok'] = "<spring:message code='registroSir.reiniciar.ok' javaScriptEscape='true' />";
    tradsSir['registroSir.reiniciar.error'] = "<spring:message code='registroSir.reiniciar.error' javaScriptEscape='true' />";
</script>

<script type="text/javascript" src="<c:url value="/js/sir.js"/>"></script>

</body>
</html>