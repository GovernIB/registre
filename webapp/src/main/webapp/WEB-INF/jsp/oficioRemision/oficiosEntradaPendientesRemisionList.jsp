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
                    <c:import url="../modulos/migadepan.jsp">
                        <c:param name="avisos" value="${loginInfo.mostrarAvisos}"/>
                    </c:import>
                    <li class="active"><i class="fa fa-list-ul"></i>
                        <strong>
                            <c:if test="${tipoEvento == 2}">
                                <spring:message code="registroEntrada.oficiosRemision.internos"/>
                            </c:if>
                            <c:if test="${tipoEvento == 3}">
                                <spring:message code="registroEntrada.oficiosRemision.externos"/>
                            </c:if>
                            <c:if test="${tipoEvento == 4}">
                                <spring:message code="registroEntrada.oficiosRemision.sir"/>
                            </c:if>
                        </strong>
                    </li>
                </ol>
            </div>
        </div><!-- /.row -->

        <c:import url="../modulos/mensajes.jsp"/>

        <!-- BUSCADOR -->

        <div class="row">

            <div class="col-xs-12">

                <div class="panel panel-success">

                    <div class="panel-heading">
                        <h3 class="panel-title"><i class="fa fa-search"></i>
                            <strong>
                                <spring:message code="registroEntrada.buscador.oficiosRemision"/>
                                <c:if test="${tipoEvento == 2}">
                                    <spring:message code="registroEntrada.oficiosRemision.internos"/>
                                </c:if>
                                <c:if test="${tipoEvento == 3}">
                                    <spring:message code="registroEntrada.oficiosRemision.externos"/>
                                </c:if>
                                <c:if test="${tipoEvento == 4}">
                                    <spring:message code="registroEntrada.oficiosRemision.sir"/>
                                </c:if>
                            </strong>
                        </h3>
                    </div>
                    <div class="panel-body">

                        <c:if test="${empty organismosDestino}">
                            <div class="row">
                                <div class="col-xs-12">

                                    <div class="alert alert-grey ">
                                        <spring:message code="regweb.busqueda.vacio"/> <strong><spring:message
                                            code="oficioRemision.oficioRemision"/></strong>
                                    </div>

                                </div>
                            </div>
                        </c:if>

                        <c:if test="${not empty organismosDestino}">
                            <form:form modelAttribute="registroEntradaBusqueda" method="post" cssClass="form-horizontal">
                                <form:hidden path="pageNumber"/>

                                <div class="col-xs-12">
                                    <div class="col-xs-6 espaiLinies">
                                        <div class="col-xs-4 pull-left etiqueta_regweb">
                                            <label for="registroEntrada.destino.codigo" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.destinoOficio.busqueda"/>" data-toggle="popover"><spring:message code="registroEntrada.organDestinatari"/></label>
                                        </div>
                                        <div class="col-xs-8">
                                            <form:select path="registroEntrada.destino.codigo" items="${organismosDestino}"
                                                         itemValue="codigo" itemLabel="denominacion"
                                                         cssClass="chosen-select"/>
                                        </div>
                                    </div>

                                    <div class="col-xs-6 espaiLinies">
                                        <div class="col-xs-4 pull-left etiqueta_regweb">
                                            <label for="anyo" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.anyoOficio.busqueda"/>" data-toggle="popover"><spring:message code="registroEntrada.anyRegistro"/></label>
                                        </div>
                                        <div class="col-xs-8">
                                            <form:select path="anyo" cssClass="chosen-select">
                                                <form:option value="" label="..."/>
                                                <c:forEach items="${anys}" var="anyo">
                                                    <form:option value="${anyo}">${anyo}</form:option>
                                                </c:forEach>
                                            </form:select>
                                        </div>
                                    </div>

                                </div>

                                <div class="form-group col-xs-12">
                                    <button type="submit" class="btn btn-warning btn-sm"><spring:message code="regweb.buscar"/></button>
                                </div>
                            </form:form>
                        </c:if>

                        <%--Búsqueda de Oficios de Remisión--%>
                        <c:if test="${paginacion != null}">

                            <div class="row">
                                <div class="col-xs-12">

                                    <c:if test="${paginacion.totalResults == 0}">
                                        <div class="alert alert-grey alert-dismissable">
                                            <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                                            <spring:message code="regweb.busqueda.vacio"/> <strong><spring:message
                                                code="registroEntrada.registroEntrada"/></strong>
                                        </div>
                                    </c:if>

                                    <c:if test="${paginacion.totalResults > 0}">
                                    <%--Organismo destinatario vigente o extinguido--%>
                                        <c:if test="${oficiosRemisionOrganismo.vigente}">
                                            <div class="alert alert-grey center">
                                                <strong>${oficiosRemisionOrganismo.organismo.denominacion}
                                                    (${oficiosRemisionOrganismo.organismo.estado.descripcionEstadoEntidad})</strong>
                                            </div>
                                        </c:if>
                                        <%--Destino extinguido, con sustituto--%>
                                        <c:if test="${oficiosRemisionOrganismo.vigente == false && not empty oficiosRemisionOrganismo.sustitutos}">
                                            <div class="alert alert-danger center">
                                                <c:if test="${not empty oficiosRemisionOrganismo.organismo.denominacion}">
                                                    <strong>${oficiosRemisionOrganismo.organismo.denominacion}
                                                        (${oficiosRemisionOrganismo.organismo.estado.descripcionEstadoEntidad})</strong>
                                                    <br> <br>
                                                </c:if>
                                                <strong><spring:message code="oficioRemision.organismoDestino.extinguido"/></strong>
                                            </div>
                                        </c:if>
                                        <%--Destino extinguido, sin sustituto--%>
                                        <c:if test="${oficiosRemisionOrganismo.vigente == false && empty oficiosRemisionOrganismo.sustitutos}">
                                            <div class="alert alert-danger center">
                                                <c:if test="${not empty oficiosRemisionOrganismo.organismo.denominacion}">
                                                    <strong>${oficiosRemisionOrganismo.organismo.denominacion}
                                                        (${oficiosRemisionOrganismo.organismo.estado.descripcionEstadoEntidad})</strong>
                                                    <br> <br>
                                                </c:if>
                                                <strong><spring:message code="oficioRemision.organismoDestino.extinguido.no.sustitutos"/></strong>
                                            </div>
                                        </c:if>

                                        <%--Oficinas destino--%>
                                        <c:if test="${oficiosRemisionOrganismo.externo == false && oficiosRemisionOrganismo.oficinas == false}">
                                            <div class="alert alert-danger center">
                                                <strong><spring:message code="oficioRemision.organismoDestino.oficinaRegistral.ninguna"/></strong>
                                            </div>
                                        </c:if>

                                        <%--Resultados totales--%>
                                        <div class="alert-grey">
                                            <c:if test="${paginacion.totalResults == 1}">
                                                <spring:message code="regweb.resultado"/>
                                                <strong>${paginacion.totalResults}</strong> <spring:message
                                                    code="registroEntrada.registroEntrada"/>
                                            </c:if>
                                            <c:if test="${paginacion.totalResults > 1}">
                                                <spring:message code="regweb.resultados"/>
                                                <strong>${paginacion.totalResults}</strong> <spring:message
                                                    code="registroEntrada.registroEntradas"/>
                                            </c:if>

                                            <p class="pull-right"><spring:message code="regweb.pagina"/>
                                                <strong>${paginacion.currentIndex}</strong> de ${paginacion.totalPages}
                                            </p>
                                        </div>

                                        <%--Resultados--%>
                                        <div class="table-responsive overVisible">

                                            <%--Url Formulario--%>
                                            <c:if test="${oficiosRemisionOrganismo.sir == false}">
                                                <c:url value="/oficioRemision/newEntrada" var="urlFormulario"/>
                                            </c:if>

                                            <c:if test="${oficiosRemisionOrganismo.sir == true}">
                                                <c:url value="/oficioRemision/sir" var="urlFormulario"/>
                                            </c:if>

                                            <form:form action="${urlFormulario}" id="oficio" modelAttribute="oficioRemisionForm" method="post" cssClass="form-horizontal">

                                                <input type="hidden" id="tipoOficioRemision" name="tipoOficioRemision" value="${oficioRemisionForm.tipoOficioRemision}"/>
                                                <c:if test="${!oficiosRemisionOrganismo.externo && oficiosRemisionOrganismo.vigente}">
                                                    <input type="hidden" id="idOrganismo" name="idOrganismo" value="${oficiosRemisionOrganismo.organismo.id}"/>
                                                </c:if>

                                                <c:if test="${oficiosRemisionOrganismo.externo && oficiosRemisionOrganismo.vigente}">
                                                    <input type="hidden" id="organismoExternoCodigo"
                                                           name="organismoExternoCodigo"
                                                           value="${oficiosRemisionOrganismo.organismo.codigo}"/>
                                                    <input type="hidden" id="organismoExternoDenominacion"
                                                           name="organismoExternoDenominacion"
                                                           value="${oficiosRemisionOrganismo.organismo.denominacion}"/>
                                                </c:if>

                                                <%--<input type="hidden" id="idLibro" name="idLibro" value="${registroEntradaBusqueda.registroEntrada.libro.id}"/>--%>

                                                <table class="table table-bordered table-hover table-striped tablesorter">
                                                    <colgroup>
                                                        <col>
                                                        <col>
                                                        <col>
                                                        <col>
                                                        <col>
                                                        <col>
                                                        <col width="50">
                                                    </colgroup>
                                                    <thead>
                                                        <tr>
                                                            <th style="text-align:center;cursor: pointer;" onclick="seleccionarTodo('oficio','${paginacion.totalResults}');"><i class="fa fa-check-square"></i></th>
                                                            <th><spring:message code="registroEntrada.numeroRegistro"/></th>
                                                            <th><spring:message code="registroEntrada.fecha"/></th>
                                                            <th><spring:message code="oficina.origen"/></th>
                                                            <th><spring:message code="registroEntrada.organismoDestino"/></th>
                                                            <th><spring:message code="registroEntrada.extracto"/></th>
                                                            <th class="center"><spring:message code="regweb.acciones"/></th>
                                                        </tr>
                                                    </thead>

                                                    <tbody>
                                                        <c:forEach var="registroEntrada" items="${paginacion.listado}" varStatus="indice">
                                                            <tr>
                                                                <td><form:checkbox id="oficio_id${indice.index}" path="registros[${indice.index}].id"
                                                                                   value="${registroEntrada.id}"/></td>
                                                                <td>${registroEntrada.numeroRegistroFormateado}</td>
                                                                <td><fmt:formatDate value="${registroEntrada.fecha}"
                                                                                    pattern="dd/MM/yyyy"/></td>
                                                                <td><label class="no-bold" rel="popupAbajo"
                                                                           data-content="${registroEntrada.oficina.codigo}"
                                                                           data-toggle="popover">${registroEntrada.oficina.denominacion}</label>
                                                                </td>
                                                                <td>
                                                                    <c:if test="${registroEntrada.destino != null}">
                                                                        ${registroEntrada.destino.denominacion}
                                                                    </c:if>
                                                                    <c:if test="${registroEntrada.destino == null}">
                                                                        ${registroEntrada.destinoExternoDenominacion}
                                                                    </c:if>
                                                                </td>
                                                                <td><c:out value="${registroEntrada.registroDetalle.extracto}" escapeXml="true"/></td>
                                                                <td class="center">
                                                                    <a class="btn btn-info btn-sm"
                                                                       href="<c:url value="/registroEntrada/${registroEntrada.id}/detalle"/>"
                                                                       title="<spring:message code="registroEntrada.detalle"/>"><span class="fa fa-eye"></span></a>
                                                                </td>
                                                            </tr>
                                                        </c:forEach>

                                                    </tbody>
                                                </table>


                                                <!-- Paginacion -->
                                                <c:import url="../modulos/paginacionBusqueda.jsp">
                                                    <c:param name="entidad" value="registroEntrada"/>
                                                </c:import>


                                            <!-- Oficio Remision Interno-->
                                            <c:if test="${oficiosRemisionOrganismo.externo == false}">

                                                    <%--Organismo vigente--%>
                                                    <c:if test="${oficiosRemisionOrganismo.vigente && oficiosRemisionOrganismo.oficinas}">
                                                        <div class="btn-group">
                                                            <button type="button" onclick="crearOficioRemision('<spring:message code="oficioRemision.generando.interno" javaScriptEscape='true'/>')" class="btn btn-sm btn-success dropdown-toggle"><spring:message code="oficioRemision.boton.crear.interno"/></button>
                                                        </div>
                                                    </c:if>

                                                    <%--Organismo extinguido, con sustitutos--%>
                                                    <c:if test="${oficiosRemisionOrganismo.vigente == false && fn:length(oficiosRemisionOrganismo.sustitutos) > 0 && oficiosRemisionOrganismo.oficinas}">
                                                        <div class="col-xs-12">
                                                            <div class="col-xs-6 espaiLinies">
                                                                <div class="col-xs-4 pull-left etiqueta_regweb">
                                                                    <label for="idOrganismo" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.denominacion.organismo"/>" data-toggle="popover"><spring:message code="registroEntrada.organismoDestino.sustituto"/></label>
                                                                </div>
                                                                <div class="col-xs-8">
                                                                    <form:select path="idOrganismo" items="${oficiosRemisionOrganismo.sustitutos}"
                                                                                 itemValue="id" itemLabel="denominacion"
                                                                                 cssClass="chosen-select"/>
                                                                </div>
                                                            </div>
                                                        </div>
                                                        <div class="btn-group">
                                                            <button type="button" onclick="crearOficioRemision('<spring:message code="oficioRemision.generando.interno" javaScriptEscape='true'/>')" class="btn btn-sm btn-success dropdown-toggle"><spring:message code="oficioRemision.boton.crear.interno"/></button>
                                                        </div>
                                                    </c:if>

                                                    <%--Organismo extinguido, sin sustitutos--%>
                                                    <c:if test="${(oficiosRemisionOrganismo.vigente == false && empty oficiosRemisionOrganismo.sustitutos) || oficiosRemisionOrganismo.oficinas == false}">
                                                        <button type="button" class="btn btn-sm btn-success disabled"><spring:message code="oficioRemision.boton.crear.interno"/></button>
                                                    </c:if>

                                            </c:if>

                                            <!-- Oficio Remision Externo y SIR-->
                                            <c:if test="${oficiosRemisionOrganismo.externo == true}">
                                                <div class="btn-group col-xs-12">

                                                    <!--Organismo externo extinguido, con 1 sustituto-->
                                                    <c:if test="${oficiosRemisionOrganismo.vigente == false && fn:length(oficiosRemisionOrganismo.sustitutos) == 1}">
                                                        <div class="col-xs-12">
                                                            <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                                                <label for="organismoExternoCodigo" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.denominacion.organismo"/>" data-toggle="popover"><span class="text-danger">*</span> <spring:message code="registroEntrada.organismoDestino.sustituto"/></label>
                                                            </div>
                                                            <div class="col-xs-8">
                                                                    ${(oficiosRemisionOrganismo.sustitutos[0]).denominacion}
                                                            </div>
                                                        </div>
                                                        <input type="hidden" id="organismoExternoCodigo" name="organismoExternoCodigo" value="${(oficiosRemisionOrganismo.sustitutos[0]).codigo}"/>
                                                        <input type="hidden" id="organismoExternoDenominacion" name="organismoExternoDenominacion" value="${(oficiosRemisionOrganismo.sustitutos[0]).denominacion}"/>
                                                    </c:if>

                                                    <!--Organismo externo extinguido, con sustitutos sir-->
                                                    <c:if test="${oficiosRemisionOrganismo.vigente == false && fn:length(oficiosRemisionOrganismo.sustitutos) > 1}">
                                                        <div class="col-xs-12">

                                                            <div class="col-xs-3 pull-left etiqueta_regweb control-label textEsq">
                                                                <label for="organismoExternoCodigo" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.denominacion.organismo"/>" data-toggle="popover"><span class="text-danger">*</span> <spring:message code="registroEntrada.organismoDestino.sustituto"/></label>
                                                            </div>
                                                            <div class="col-xs-9">
                                                                <!-- Organismo externo sir, cargaremos sus oficinas SIR-->
                                                                <c:if test="${oficiosRemisionOrganismo.sir}">
                                                                    <form:select path="organismoExternoCodigo" items="${oficiosRemisionOrganismo.sustitutos}"
                                                                                     itemValue="codigo" itemLabel="denominacion"
                                                                                     cssClass="chosen-select"
                                                                                     onchange="actualizarOficinasSIROficios()"/>
                                                                </c:if>
                                                                <!-- Organismo externo no sir-->
                                                                <c:if test="${!oficiosRemisionOrganismo.sir}">
                                                                    <form:select path="organismoExternoCodigo" items="${oficiosRemisionOrganismo.sustitutos}"
                                                                                 itemValue="codigo" itemLabel="denominacion"
                                                                                 cssClass="chosen-select"/>
                                                                </c:if>
                                                            </div>
                                                        </div>
                                                    </c:if>




                                                    <!-- Oficina Sir destinataria -->
                                                    <c:if test="${oficiosRemisionOrganismo.sir}">
                                                        <!-- Una sola oficina SIR -->
                                                        <c:if test="${fn:length(oficiosRemisionOrganismo.oficinasSIR) == 1}">

                                                            <div class="col-xs-12">
                                                                <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                                                    <label for="oficinaSIRCodigo" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.oficinaSir"/>" data-toggle="popover"><span class="text-danger">*</span> <spring:message code="oficioRemision.oficinaSir"/></label>
                                                                </div>
                                                                <div class="col-xs-8">
                                                                        ${(oficiosRemisionOrganismo.oficinasSIR[0]).denominacion}
                                                                </div>
                                                            </div>
                                                            <input type="hidden" id="oficinaSIRCodigo" name="oficinaSIRCodigo" value="${(oficiosRemisionOrganismo.oficinasSIR[0]).codigo}"/>
                                                        </c:if>

                                                        <!-- Más de 1 Oficina SIR -->
                                                        <c:if test="${fn:length(oficiosRemisionOrganismo.oficinasSIR) > 1}">
                                                            <div class="col-xs-12">
                                                                <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq" >
                                                                    <label for="oficinaSIRCodigo" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.oficinaSir"/>" data-toggle="popover"><span class="text-danger">*</span> <spring:message code="oficioRemision.oficinaSir"/></label>
                                                                </div>
                                                                <div class="col-xs-8">
                                                                    <form:select path="oficinaSIRCodigo" items="${oficiosRemisionOrganismo.oficinasSIR}"
                                                                                 itemLabel="denominacion"
                                                                                 itemValue="codigo"
                                                                                 class="chosen-select"/>
                                                                </div>
                                                            </div>
                                                        </c:if>
                                                        <!-- Div de oficinas SIR  que se monta via js cuando hay más de un organo sustituto y lo escogen-->
                                                        <div class="col-xs-12 btn-group" id="ofSIR">
                                                            <div class="col-xs-3 pull-left etiqueta_regweb control-label textEsq">
                                                                <label rel="popupAbajo" data-content="<spring:message code="registro.ayuda.oficinaSir"/>" data-toggle="popover"><span class="text-danger">*</span> <spring:message code="oficioRemision.oficinaSir"/></label>
                                                            </div>
                                                            <div id="oficinaSIR"><c:if test="${fn:length(oficiosRemisionOrganismo.oficinasSIR) == 0}">
                                                                <spring:message code="registroSir.error.sinoficinas"/>
                                                            </c:if></div>
                                                        </div>
                                                    </c:if>


                                                    <!--Organismo externo vigente, botonera -->
                                                    <c:if test="${oficiosRemisionOrganismo.vigente}">

                                                        <c:if test="${oficiosRemisionOrganismo.sir == false}">
                                                        <div class="col-xs-6 peu">
                                                            <button type="button" onclick="crearOficioRemision('<spring:message code="oficioRemision.generando.externo" javaScriptEscape='true'/>')" class="btn btn-sm btn-success dropdown-toggle">
                                                                <spring:message code="oficioRemision.boton.crear.externo"/>
                                                            </button>
                                                        </div>
                                                        </c:if>
                                                        <c:if test="${oficiosRemisionOrganismo.sir == true}">
                                                        <div class="col-xs-6 peu">
                                                            <button type="button" onclick="crearOficioRemision('<spring:message code="oficioRemision.generando.sir" javaScriptEscape='true'/>')" class="btn btn-sm btn-primary dropdown-toggle">
                                                                <spring:message code="oficioRemision.boton.crear.sir"/>
                                                            </button>
                                                        </div>
                                                        </c:if>
                                                    </c:if>

                                                    <!--Organismo externo extinguido, con sustitutos botonera-->
                                                    <c:if test="${oficiosRemisionOrganismo.vigente == false && not empty oficiosRemisionOrganismo.sustitutos}">

                                                        <c:if test="${oficiosRemisionOrganismo.sir == false}">
                                                        <div class="col-xs-6 peu">
                                                            <button type="button" onclick="crearOficioRemision('<spring:message code="oficioRemision.generando.externo" javaScriptEscape='true'/>')" class="btn btn-sm btn-success dropdown-toggle">
                                                                <spring:message code="oficioRemision.boton.crear.externo"/>
                                                            </button>
                                                        </div>
                                                        </c:if>
                                                        <c:if test="${oficiosRemisionOrganismo.sir == true}">
                                                        <div class="col-xs-6 peu">
                                                            <button type="button" onclick="crearOficioRemision('<spring:message code="oficioRemision.generando.sir" javaScriptEscape='true'/>')" class="btn btn-sm btn-primary dropdown-toggle">
                                                                <spring:message code="oficioRemision.boton.crear.sir"/>
                                                            </button>
                                                        </div>
                                                        </c:if>

                                                    </c:if>


                                                    <!--Organismo externo extinguido, sin sustitutos botonera-->
                                                    <c:if test="${oficiosRemisionOrganismo.vigente == false && empty oficiosRemisionOrganismo.sustitutos}">

                                                        <c:if test="${oficiosRemisionOrganismo.sir == false}">
                                                        <div class="col-xs-6 peu">
                                                            <button type="button" class="btn btn-sm btn-success disabled">
                                                                <spring:message code="oficioRemision.boton.crear.externo"/>
                                                            </button>
                                                        </div>
                                                        </c:if>

                                                        <c:if test="${oficiosRemisionOrganismo.sir == true}">
                                                        <div class="col-xs-6 peu">
                                                            <button type="button" class="btn btn-sm btn-primary disabled">
                                                                <spring:message code="oficioRemision.boton.crear.sir"/>
                                                            </button>
                                                        </div>
                                                        </c:if>

                                                    </c:if>

                                                </div>
                                            </c:if>

                                            </form:form>

                                        </div>

                                    </c:if>

                                </div>
                            </div>

                        </c:if>

                    </div>
                </div>
            </div>
        </div>

    </div>
</div> <!-- /container -->

<c:import url="../modulos/pie.jsp"/>

<script type="text/javascript">

    $(document).ready(function() {
        //Ocultamos el div ofSIR que se monta via js cuando hay más de 1 sustituto
        $('#ofSIR').hide();
    });

    /**
     * Genera el Oficio de Remisón
     */
    function crearOficioRemision(mensaje){
        waitingDialog.show(mensaje, {dialogSize: 'm', progressType: 'success'});
        doForm('#oficio');

    }


    //Selecciona todos los oficios de remision de un organismo
    function seleccionarTodo(nomOficio, filas) {
        var len = parseInt(filas);
        var nombre = "#" + nomOficio + "_id0";
        //Selecciona todos los checks o los deselecciona todos a la vez
        if (len > 0) {
            if ($(nombre).prop('checked')) {
                for (var i = 0; i < len; i++) {
                    nombre = "#" + nomOficio + "_id" + i;
                    $(nombre).prop('checked', false);
                }
            } else {
                for (var i = 0; i < len; i++) {
                    nombre = "#" + nomOficio + "_id" + i;
                    $(nombre).prop('checked', true);
                }
            }
        }
    }

    /** Mensajes para la funcionalidad de cargar las oficinas SIR del destino indicado*/
    var mensaje = "<spring:message code='registroSir.error.sinoficinas' javaScriptEscape='true' />";
    var mensaje2 = "<spring:message code='registroSir.obtener.oficinasSIR' javaScriptEscape='true' />";

    /**
     * Función para actualizar las oficinas SIR desde el select de los sustitutos cuando el destino está extinguido
     */
    function actualizarOficinasSIROficios(){
        <c:url var="obtenerOficinasSIR" value="/rest/obtenerOficinasSIR" />
        var codigoDestinoSIR = $('#organismoExternoCodigo option:selected').val();
        if(codigoDestinoSIR !== '-1'){
            cargarOficinasSIR('${obtenerOficinasSIR}',codigoDestinoSIR,'oficinaSIRCodigo','0',false, mensaje,mensaje2,'chosen-select col-xs-12 selectSirDest','');
        }
    }
</script>

</body>
</html>