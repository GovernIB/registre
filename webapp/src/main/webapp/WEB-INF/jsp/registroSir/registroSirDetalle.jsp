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

        <!-- Miga de pan -->
        <div class="row">
            <div class="col-xs-12">
                <ol class="breadcrumb">
                    <c:import url="../modulos/migadepan.jsp"/>
                    <c:if test="${loginInfo.rolActivo.nombre == 'RWE_USUARI'}">
                        <li><a href="<c:url value="/registroSir/list"/>"><i class="fa fa-list"></i> <spring:message code="registroSir.listado"/></a></li>
                    </c:if>
                    <c:if test="${loginInfo.rolActivo.nombre == 'RWE_ADMIN'}">
                        <li><a href="<c:url value="/sir/monitorRecibidos"/>"><i class="fa fa-list"></i> <spring:message code="registroSir.buscador.recibidos"/></a></li>
                    </c:if>

                    <li class="active"><i class="fa fa-pencil-square-o"></i> <spring:message code="registroSir.registroSir"/> ${registroSir.numeroRegistro}</li>
                </ol>
            </div>
        </div><!-- Fin miga de pan -->
        <c:url value="/registroSir/aceptar/${registroSir.id}" var="urlAceptar" scope="request"/>

        <div class="row">

            <div class="col-xs-4">

                <div class="panel panel-primary">
                    <div class="panel-heading">
                        <h3 class="panel-title"><i class="fa fa-file-o"></i>
                            <strong> <spring:message code="registroSir.registroSir"/> ${registroSir.numeroRegistro}</strong>
                        </h3>
                    </div>
                    <div class="panel-body">

                        <%--DETALLE REGISTRO SIR--%>
                        <c:import url="detalleRegistroSir.jsp"/>

                    </div>

                    <form:form modelAttribute="registrarForm" action="${urlAceptar}" method="post" cssClass="form-horizontal">



                    <%-- Se muestra la Botonera si el RegistroSir está pendiente de procesar--%>
                    <c:if test="${registroSir.estado == 'RECIBIDO' && loginInfo.rolActivo.nombre == 'RWE_USUARI'}">

                        <%--Formulari per completar dades del registre--%>
                        <div class="panel-footer">

                            <div class="panel-heading senseMargesLaterals">
                                <h3 class="panel-title center">
                                    <strong><spring:message code="registro.completar"/></strong>
                                </h3>
                            </div>

                            <%--Libro único--%>
                            <form:hidden path="idLibro" value="${libro.id}"/>

                            <%--Distribuir--%>
                            <form:hidden path="distribuir"/>

                            <%--Si hay al menos un Libro, podemos aceptar el RegistroSir--%>
                            <c:if test="${not empty libro}">

                                <%--Idioma--%>
                                <div class="form-group col-xs-12">
                                    <div class="col-xs-5 pull-left etiqueta_regweb control-label textEsq">
                                        <label for="idIdioma" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.idioma"/>" data-toggle="popover"><span class="text-danger">*</span> <spring:message code="registroEntrada.idioma"/></label>
                                    </div>
                                    <div class="col-xs-7 no-pad-right" id="idioma">
                                        <form:select path="idIdioma" cssClass="chosen-select">
                                            <c:forEach items="${idiomas}" var="idioma">
                                                <c:if test="${idioma == RegwebConstantes.IDIOMA_CASTELLANO_ID}">
                                                    <form:option value="${idioma}" selected="selected"><spring:message
                                                            code="idioma.${idioma}"/></form:option>
                                                </c:if>
                                                <c:if test="${idioma != RegwebConstantes.IDIOMA_CASTELLANO_ID}">
                                                    <form:option value="${idioma}"><spring:message
                                                            code="idioma.${idioma}"/></form:option>
                                                </c:if>
                                            </c:forEach>
                                        </form:select>
                                        <span class="errors"></span>
                                    </div>
                                </div>

                                <%--Si la unidad tramitación destino está extinguida, lo informamos--%>
                                <c:if test="${extinguido}">
                                    <div class="form-group col-xs-12 center">
                                        <span class="text-vermell">
                                            <strong>
                                                <spring:message code="registroSir.organismo.destino.extinguido" arguments="${registroSir.decodificacionUnidadTramitacionDestino}"/>
                                            </strong>
                                        </span>
                                    </div>
                                </c:if>

                                <%--Organismo destino--%>
                                <div class="form-group col-xs-12">
                                    <div class="col-xs-5 pull-left etiqueta_regweb control-label textEsq">
                                        <label for="idOrganismoDestino" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.destino"/>" data-toggle="popover"><span class="text-danger">*</span> <spring:message code="registroEntrada.organismoDestino"/></label>
                                    </div>
                                    <div class="col-xs-7">
                                        <form:select path="idOrganismoDestino" cssClass="chosen-select" items="${organismosOficinaActiva}" itemValue="id" itemLabel="denominacion"/>
                                    </div>
                                </div>

                                <%--Código SIA--%>
                                <div class="form-group col-xs-12">
                                    <div class="col-xs-5 pull-left etiqueta_regweb control-label textEsq">
                                        <label for="codigoSia" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.codigoSIA"/>" data-toggle="popover"> <spring:message code="registroEntrada.codigoSIA"/></label>
                                    </div>
                                    <div class="col-xs-7" id="codigoSia">
                                        <form:input path="codigoSia" maxlength="20" cssClass="form-control"/> <span class="errors"></span>
                                    </div>
                                </div>

                                <div class="row">
                                    <div class="col-xs-12 list-group-item-heading">
                                        <c:if test="${registroSir.documentacionFisica!=RegwebConstantes.TIPO_DOCFISICA_NO_ACOMPANYA_DOC}">
                                            <button type="button" onclick='javascript:confirm("javascript:aceptarRegistroSir(false)","<spring:message code="regweb.confirmar.registroSIR" htmlEscape="true"/>")' href="javascript:void(0);" class="btn btn-primary btn-sm btn-block">
                                                <spring:message code="registroSir.aceptar"/>
                                            </button>
                                            <button type="button" onclick='javascript:confirm("javascript:aceptarRegistroSir(true)","<spring:message code="regweb.confirmar.registroSIR" htmlEscape="true"/>")' href="javascript:void(0);" class="btn btn-success btn-sm btn-block">
                                                <spring:message code="registroSir.aceptar.distribuir"/>
                                            </button>
                                        </c:if>
                                        <c:if test="${registroSir.documentacionFisica==RegwebConstantes.TIPO_DOCFISICA_NO_ACOMPANYA_DOC}">
                                            <button type="button" class="btn btn-primary btn-sm btn-block" onclick="aceptarRegistroSir(false)">
                                                <spring:message code="registroSir.aceptar"/>
                                            </button>
                                            <button type="button" class="btn btn-success btn-sm btn-block" onclick="aceptarRegistroSir(true)">
                                                <spring:message code="registroSir.aceptar.distribuir"/>
                                            </button>
                                        </c:if>
                                    </div>
                                </div>
                                <c:set var="errorObligatori"><spring:message code="error.valor.requerido"/></c:set>
                                <input id="error" type="hidden" value="${errorObligatori}"/>

                            </c:if>

                            <%--Si no hay Libros no podremos aceptar el RegistroSir--%>
                            <c:if test="${empty libro}">
                                <spring:message code="registroSir.libro.noexiste"/> <c:if test="${not empty registroSir.decodificacionUnidadTramitacionDestino}"> ${registroSir.decodificacionUnidadTramitacionDestino}</c:if>.
                                <spring:message code="registroSir.noaceptar"/>
                            </c:if>

                        </div>

                        <div class="panel-footer center">

                            <%--Botón Reenviar--%>
                            <c:if test="${puedeReenviar}">
                                <div class="btn-group">
                                    <a class="btn btn-warning btn-sm" href="<c:url value="/registroSir/${registroSir.id}/reenviar"/>" role="button"><spring:message code="registroSir.estado.reenviar"/></a>
                                </div>
                            </c:if>

                            <%--Rechazar--%>
                            <div class="btn-group">
                                <a data-toggle="modal" role="button" href="#rechazoModal"
                                   onclick="limpiarModalRechazo();"
                                   class="btn btn-danger btn-sm"><spring:message code="registroSir.estado.rechazar"/></a>
                            </div>
                        </div>
                    </c:if>
                </div>

            </div>

            <div class="col-xs-8 col-xs-offset">
                <c:import url="../modulos/mensajes.jsp"/>
            </div>



            <div class="col-xs-8">

                <ul class="navPrimary navPrimary-tabs" id="myTab">

                    <li><a href="#general" data-toggle="tab"><i class="fa fa-file-o"></i> General</a></li>
                    <c:if test="${not empty trazabilidades}">
                        <li><a href="#trazabilidad" data-toggle="tab"><i class="fa fa-clock-o fa-fw"></i> <spring:message code="registroEntrada.trazabilidad"/></a></li>
                    </c:if>
                    <c:if test="${not empty historicos && registro.estado != RegwebConstantes.REGISTRO_RESERVA}">
                        <li><a href="#modificaciones" data-toggle="tab"><i class="fa fa-pencil-square-o"></i> <spring:message code="regweb.modificaciones"/></a></li>
                    </c:if>

                </ul>

                <div id="contenido" class="tab-content contentPrimary">

                    <div class="tab-pane" id="general">

                        <!-- ANEXOS -->
                        <c:import url="anexosSir.jsp"/>

                        <%--INTERESADOS--%>
                        <c:import url="interesadosSir.jsp"/>

                        </form:form>

                        <%--EXPONE - SOLICITA--%>
                        <c:if test="${not empty registroSir.expone || not empty registroSir.solicita}">
                            <div class="col-xs-12 pull-right">
                                <div class="panel panel-primary">

                                    <div class="panel-heading">
                                        <h3 class="panel-title"><i class="fa fa-file-text-o"></i> <strong><spring:message code="registroDetalle.expone.solicita.titulo"/></strong></h3>
                                    </div>

                                    <div class="panel-body">
                                        <c:if test="${not empty registroSir.expone}">
                                            <p><strong><i class="fa fa-hand-o-right"></i> <spring:message code="registroDetalle.expone"/>:</strong> ${registroSir.expone}</p>
                                        </c:if>

                                        <c:if test="${ not empty registroSir.solicita}">
                                            <p><strong><i class="fa fa-hand-o-right"></i> <spring:message code="registroDetalle.solicita"/>:</strong> ${registroSir.solicita}</p>
                                        </c:if>
                                    </div>
                                </div>

                            </div>
                        </c:if>

                    </div>

                    <%--TRAZABILIDAD--%>
                    <c:if test="${not empty trazabilidades}">

                        <div class="tab-pane" id="trazabilidad">

                            <c:import url="../trazabilidad/trazabilidadSir.jsp">
                                <c:param name="adminEntidad" value="false"/>
                            </c:import>

                        </div>

                    </c:if>

                </div>
            </div>


        </div><!-- /div.row-->


        <%--Modal Rechazo--%>
        <div id="rechazoModal" class="modal fade">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">x</button>
                        <h3><spring:message code="registroSir.rechazar"/> ${registroSir.numeroRegistro}</h3>
                    </div>

                    <div class="modal-body">
                        <c:url value="/registroSir/rechazar/${registroSir.id}" var="urlRechazar" scope="request"/>
                        <form:form modelAttribute="rechazarForm" method="post" action="${urlRechazar}" cssClass="form-horizontal">
                            <div class="panel panel-primary">

                                <div class="panel-heading">
                                    <h3 class="panel-title"><i class="fa fa-pencil-square-o"></i>
                                        <strong><spring:message code="registroSir.rechazo.motivo"/></strong></h3>
                                </div>

                                <div class="panel-body">
                                    <div class="form-group col-xs-12">
                                        <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                            <label for="observacionesRechazoSir" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.observaciones.rechazo"/>" data-toggle="popover"><span class="text-danger">*</span> <spring:message code="registroEntrada.observaciones"/></label>
                                        </div>
                                        <div class="col-xs-8" id="observacionesRechazoSir">
                                            <form:textarea path="observacionesRechazo" rows="5" cssClass="form-control" maxlength="80"/> <span class="errors"></span>
                                        </div>
                                    </div>

                                </div> <!-- /.panel body -->
                            </div>
                            <!-- /.panel panel-primary -->
                            <div class="form-actions">
                                <input type="submit" value="<spring:message code="registroSir.estado.rechazar"/>"
                                       class="btn btn-danger btn-sm"
                                       onclick="return rechazarRegistroSir()">
                            </div>
                        </form:form>

                    </div>
                    <div class="modal-footer">
                        <button class="btn" data-dismiss="modal" aria-hidden="true"><spring:message code="regweb.cerrar"/></button>
                    </div>
                </div>
            </div>
        </div>

    </div>
</div> <!-- /container -->

<c:import url="../modulos/pie.jsp"/>

<!-- Importamos el codigo jsp del modal del formulario para realizar la busqueda de organismos Origen
                 Mediante el archivo "busquedaorganismo.js" se implementa dicha búsqueda -->
<c:import url="../registro/buscadorOrganismosOficinasREPestanas.jsp">
    <c:param name="tipo" value="OficinaSir"/>
</c:import>
<script type="text/javascript" src="<c:url value="/js/busquedaorganismo.js"/>"></script>

<script type="application/javascript">
    // Realiza el Registro de un registroSir
    function aceptarRegistroSir(isDistribuir) {
        $('#distribuir').val(isDistribuir);
        var libro = true;
        var idioma = true;
        var tipoAsunto = true;
        var codigoSia = true;
        var idLibro = $('#idLibro').val();
        var idIdioma = $('#idIdioma').val();
        var idTipoAsunto = $('#idTipoAsunto').val();
        var codigoSiaValue = $('#codigoSia').val();

        // Valida que haya un libro elegido
        if (!validaCampo(idLibro, 'libro')) {
            libro = false;
        }
        // Valida que haya un idioma elegido
        if (!validaCampo(idIdioma, 'idioma')) {
            idioma = false;
        }
        // Valida que haya un tipoAsunto elegido
        if (!validaSelect(idTipoAsunto, 'tipoAsunto')) {
            tipoAsunto = false;
        }
        // Valida que el código SIA sea numérico
        if(!validaEntero(codigoSiaValue, 'codigoSia')){
            codigoSia = false;
        }

        // Mira si todos los campos son correctos
        if ((libro) && (idioma) && (tipoAsunto) && (codigoSia)) {

            waitingDialog.show('<spring:message code="registroSir.aceptando" javaScriptEscape='true'/>', {dialogSize: 'm', progressType: 'primarycd'});
            doForm('#registrarForm');
            //return true;
        } else {
            return false;
        }
    }

    function rechazarRegistroSir() {
        var observaciones = $('#observacionesRechazo').val();

        if(observaciones == ""){
            var variable = "#observacionesRechazoSir span.errors";
            var formatoHtml = "<span id='observaciones.errors' class='help-block'><spring:message code="error.valor.requerido"/></span>";
            $(variable).html(formatoHtml);
            $(variable).parents(".form-group").addClass("has-error");
            return false;
        }else{
            doForm('#rechazarForm');
        }
    }

    function limpiarModalRechazo(){
        $('#observaciones').val("");
        var variable = "#observacionesRechazoSir span.errors";
        var formatoHtml = "<span id='observaciones.errors' class='help-block'></span>";
        $(variable).html(formatoHtml);
        $(variable).parents(".form-group").removeClass("has-error");
    }

</script>


</body>
</html>