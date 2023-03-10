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
                     <c:import url="../modulos/migadepan.jsp">
                         <c:param name="avisos" value="false"/>
                     </c:import>
                    <li class="active"><i class="fa fa-pencil-square-o"></i> <spring:message code="registroEntrada.registroEntrada"/> ${registro.numeroRegistroFormateado}</li>
                 </ol>
             </div>
        </div><!-- Fin miga de pan -->

        <div id="mensajes"></div>

        <div class="row">

            <div class="col-xs-12" id="mensajesdetalle">
                <c:import url="../modulos/mensajes.jsp"/>
            </div>

            <!-- Panel Lateral -->
            <div class="col-xs-4">

                <div class="panel panel-info">
                    <div class="panel-heading">
                        <h3 class="panel-title"><i class="fa fa-file-o"></i>
                            <strong> <spring:message code="registroEntrada.registroEntrada"/> ${registro.numeroRegistroFormateado}</strong>
                        </h3>
                    </div>
                    <div class="panel-body">

                        <%--DETALLE REGISTRO--%>
                        <dl class="detalle_registro">
                            <c:import url="../registro/detalleRegistro.jsp">
                                <c:param name="tipoRegistro" value="${RegwebConstantes.REGISTRO_ENTRADA}"/>
                            </c:import>
                        </dl>

                    </div>

                    <%--BOTONERA JUSTIFICANTE--%>
                    <div class="panel-footer center">
                        <c:if test="${registro.estado != RegwebConstantes.REGISTRO_RESERVA}">

                            <%--Si no se ha generado el justificante y el registro no está ANULADO, muestra el boton para generarlo --%>
                            <c:if test="${idJustificante == null && registro.estado != RegwebConstantes.REGISTRO_ANULADO}">
                                <div class="btn-group">
                                    <button type="button" class="btn btn-warning btn-sm dropdown-toggle" data-toggle="dropdown">
                                        <spring:message code="justificante.boton"/> <span class="caret"></span>
                                    </button>
                                    <ul class="dropdown-menu">
                                        <li class="submenu-complet"><a onclick="crearJustificante('<c:url value="/adminEntidad/registroEntrada/${idRegistro}/justificante/ca"/>')" onmouseover="this.style.cursor='pointer';"><spring:message code="regweb.catalan"/></a></li>
                                        <li class="submenu-complet"><a onclick="crearJustificante('<c:url value="/adminEntidad/registroEntrada/${idRegistro}/justificante/es"/>')" onmouseover="this.style.cursor='pointer';"><spring:message code="regweb.castellano"/></a></li>
                                    </ul>
                                </div>
                            </c:if>
                        </c:if>

                        <%--Si se ha generado el justificante, muestra el boton paras descargarlo --%>
                        <c:if test="${tieneJustificante}">
                            <%-- Si no tiene urlValidación solo podrá descargar el original --%>
                            <c:if test="${!tieneUrlValidacion}">
                                <div class="btn-group"><button type="button" class="btn btn-success btn-sm" onclick="goTo('<c:url value="/anexo/descargarJustificante/${idJustificante}/true"/>')"><span class="fa fa-download"></span> <spring:message code="justificante.boton"/></button></div>
                            </c:if>

                            <%-- Si tiene urlValidación se podrá descargar el original o con el csv incrustado --%>
                            <c:if test="${tieneUrlValidacion}">
                                <div class="btn-group">
                                    <button type="button" class="btn btn-success btn-sm dropdown-toggle" data-toggle="dropdown">
                                        <spring:message code="justificante.boton"/> <span class="caret"></span>
                                    </button>
                                    <ul class="dropdown-menu">
                                        <li class="submenu-complet"><a onclick="goTo('<c:url value="/anexo/descargarJustificante/${idJustificante}/true"/>')" onmouseover="this.style.cursor='pointer';"><spring:message code="justificante.original"/></a></li>
                                        <li class="submenu-complet"><a onclick="goTo('<c:url value="/anexo/descargarJustificante/${idJustificante}/false"/>')" onmouseover="this.style.cursor='pointer';"><spring:message code="justificante.concsv"/></a></li>
                                    </ul>
                                </div>
                            </c:if>
                        </c:if>

                        <%--Botón Anular--%>
                        <c:if test="${registro.estado != RegwebConstantes.REGISTRO_ANULADO && registro.estado != RegwebConstantes.REGISTRO_RECTIFICADO}">
                            <div class="btn-group">
                                <a data-toggle="modal" role="button" href="#anularModal" onclick="limpiarModalAnulacion(${registro.id});" class="btn btn-danger btn-sm"><spring:message code="regweb.anular"/></a>
                            </div>
                        </c:if>
                    </div>

                    <%--BOTONERA --%>
                    <div class="panel-footer center">
                        <form:form modelAttribute="integracion" action="${pageContext.request.contextPath}/integracion/busqueda" method="post" cssClass="form-horizontal" target="_blank">
                            <form:hidden path="texto"/>
                        </form:form>

                        <%--Botón integraciones--%>
                        <div class="btn-group">
                            <button type="button" onclick="buscarIntegraciones('${registro.numeroRegistroFormateado}')" class="btn btn-warning btn-sm btn-block">
                                <spring:message code="integracion.integraciones"/>
                            </button>
                        </div>

                        <%--Botón detalle Intercambio SIR--%>
                        <c:if test="${not empty registro.registroDetalle.identificadorIntercambio}">
                            <div class="btn-group"><button type="button" onclick="goToNewPage('<c:url value="/sir/${registro.registroDetalle.identificadorIntercambio}/detalle"/>')" class="btn btn-primary btn-sm"><spring:message code="idIntercambio.detalle"/></button></div>
                        </c:if>

                        <%--Botón Re-Distribuir--%>
                        <c:if test="${registro.estado == RegwebConstantes.REGISTRO_DISTRIBUIDO && registro.registroDetalle.anexosPurgado == false}">
                            <div class="btn-group"><button type="button" onclick="reDistribuir('<c:url value="/adminEntidad/registroEntrada/${registro.id}/reDistribuir"/>')" class="btn btn-primary btn-sm"><spring:message code="registroEntrada.redistribuir"/></button></div>
                        </c:if>
                        <%--Botón Marcar Distribuido--%>
                        <c:if test="${registro.estado == RegwebConstantes.REGISTRO_DISTRIBUYENDO && tieneJustificanteCustodiado}">
                            <div class="btn-group"><button type="button" onclick="goTo('<c:url value="/adminEntidad/registroEntrada/${registro.id}/marcarDistribuido"/>')" class="btn btn-primary btn-sm"><spring:message code="registroEntrada.marcarDistribuido"/></button></div>
                        </c:if>
                    </div>
                </div>

            </div>
            <!-- Fin Panel Lateral -->

            <c:if test="${registro.estado != RegwebConstantes.REGISTRO_RESERVA}">

                <div class="col-xs-8">

                    <ul class="navInfo navInfo-tabs" id="myTab">

                        <li><a href="#general" data-toggle="tab"><i class="fa fa-file-o"></i> General</a></li>
                        <c:if test="${not empty trazabilidades}">
                            <li><a href="#trazabilidad" data-toggle="tab"><i class="fa fa-clock-o fa-fw"></i> <spring:message code="registroEntrada.trazabilidad"/></a></li>
                        </c:if>
                        <c:if test="${not empty historicos && registro.estado != RegwebConstantes.REGISTRO_RESERVA}">
                            <li><a href="#modificaciones" data-toggle="tab"><i class="fa fa-pencil-square-o"></i> <spring:message code="regweb.modificaciones"/></a></li>
                        </c:if>
                        <c:if test="${tieneJustificante}">
                            <li><a href="#justificante" data-toggle="tab"><i class="fa fa-file-text-o"></i> <spring:message code="justificante.boton"/></a></li>
                        </c:if>
                    </ul>

                    <div id="contenido" class="tab-content contentInfo">

                        <div class="tab-pane" id="general">

                            <%--ANEXOS --%>
                            <c:if test="${registro.registroDetalle.tipoDocumentacionFisica != RegwebConstantes.TIPO_DOCFISICA_ACOMPANYA_DOC_REQUERIDA || registro.registroDetalle.tieneAnexos}">

                               <%--ANEXOS SOLO LECTURA--%>
                               <c:import url="../registro/anexosLectura.jsp">
                                   <c:param name="tipoRegistro" value="${RegwebConstantes.REGISTRO_ENTRADA}"/>
                                   <c:param name="idEntidad" value="${registro.oficina.organismoResponsable.entidad.id}"/>
                               </c:import>

                             </c:if>

                            <%--INTERESADOS SOLO LECTURA--%>
                            <c:import url="../registro/interesadosLectura.jsp">
                                <c:param name="tipoRegistro" value="${RegwebConstantes.REGISTRO_ENTRADA}"/>
                            </c:import>

                            <%--EXPONE - SOLICITA--%>
                            <c:if test="${not empty registro.registroDetalle.expone || not empty registro.registroDetalle.solicita}">
                                <c:import url="../registro/exponeSolicita.jsp">
                                    <c:param name="tipoRegistro" value="${RegwebConstantes.REGISTRO_ENTRADA}"/>
                                </c:import>
                            </c:if>
                        </div>

                        <%--TRAZABILIDAD--%>
                        <c:if test="${not empty trazabilidades}">
                            <div class="tab-pane" id="trazabilidad">
                                <c:import url="../trazabilidad/trazabilidadEntrada.jsp">
                                    <c:param name="adminEntidad" value="true"/>
                                </c:import>
                            </div>
                        </c:if>

                        <!-- MODIFICACIONES REGISTRO -->
                        <c:if test="${not empty historicos && registro.estado != RegwebConstantes.REGISTRO_RESERVA}">
                            <div class="tab-pane" id="modificaciones">
                                <c:import url="../registro/modificaciones.jsp">
                                    <c:param name="tipoRegistro" value="${RegwebConstantes.REGISTRO_ENTRADA}"/>
                                </c:import>
                            </div>
                        </c:if>

                        <%--JUSTIFICANTE--%>
                        <c:if test="${tieneJustificante}">
                            <div class="tab-pane" id="justificante">
                                <div class="col-xs-12">
                                    <dl class="detalle_registro">
                                        <c:if test="${not registro.registroDetalle.justificante.custodiado}">
                                            <dt><i class="fa fa-home"></i> Custodiado: </dt> <dd> <span class="label label-danger"><spring:message code="regweb.no"/></span></dd>
                                            <dt><i class="fa fa-home"></i> Perfil custodia: </dt> <dd> <spring:message code="perfilCustodia.${registro.registroDetalle.justificante.perfilCustodia}"/></dd>
                                            <dt><i class="fa fa-home"></i> CustodyId: </dt> <dd> ${registro.registroDetalle.justificante.custodiaID}</dd>
                                        </c:if>
                                        <c:if test="${registro.registroDetalle.justificante.custodiado}">
                                            <dt><i class="fa fa-home"></i> Custodiado: </dt> <dd> <span class="label label-success"><spring:message code="regweb.si"/></span></dd>
                                            <dt><i class="fa fa-home"></i> Perfil custodia: </dt> <dd> <spring:message code="perfilCustodia.${registro.registroDetalle.justificante.perfilCustodia}"/></dd>
                                            <dt><i class="fa fa-home"></i> Csv: </dt> <dd> ${registro.registroDetalle.justificante.csv}</dd>
                                            <dt><i class="fa fa-home"></i> CustodyId: </dt> <dd> ${registro.registroDetalle.justificante.custodiaID}</dd>
                                            <dt><i class="fa fa-home"></i> Expediente: </dt> <dd> ${registro.registroDetalle.justificante.expedienteID}</dd>
                                        </c:if>
                                        <dt><i class="fa fa-home"></i> idAnexo: </dt> <dd> ${registro.registroDetalle.justificante.id}</dd>
                                    </dl>
                                </div>
                            </div>
                        </c:if>
                    </div>
                </div>
            </c:if>

        </div>
    </div>

    <%--Modal ANULAR--%>
    <c:import url="../registro/anular.jsp">
        <c:param name="tipoRegistro" value="${RegwebConstantes.REGISTRO_ENTRADA}"/>
    </c:import>

</div>


<c:import url="../modulos/pie.jsp"/>

<script type="text/javascript" src="<c:url value="/js/integracion.js"/>"></script>

<script type="text/javascript">

    // Muestra los datos del hitórico seleccionado y oculta el resto
    function comparaRegistros(idHistorico){

        <c:forEach var="historico" items="${historicos}">
            $('#'+${historico.id}).hide();
        </c:forEach>
        var elemento = '#'+idHistorico;
        $(elemento).show();
    }

    /**
     * Genera el Justificante del Registro
     * @param url
     */
    function crearJustificante(url){

        $.ajax({
            url:url,
            type:'POST',
            beforeSend: function(objeto){
                waitingDialog.show('<spring:message code="justificante.generando" javaScriptEscape='true'/>', {dialogSize: 'm', progressType: 'info'});
            },
            success:function(respuesta){
                if(respuesta.status == 'SUCCESS'){
                    goTo('<c:url value="/adminEntidad/registroEntrada/${idRegistro}/detalle"/>');
                }else if(respuesta.status == 'FAIL') {
                    mensajeError('#mensajes', respuesta.error);
                    waitingDialog.hide();
                }
            }
        });
    }

    /**
     * Vuelve a Distribuir el Registro
     * @param url
     */
    function reDistribuir(url){

        $.ajax({
            url:url,
            type:'POST',
            beforeSend: function(objeto){
                waitingDialog.show('<spring:message code="registroEntrada.distribuyendo" javaScriptEscape='true'/>', {dialogSize: 'm', progressType: 'info'});
            },
            success:function(respuesta){
                if(respuesta.status == 'SUCCESS'){
                    mensajeSuccess('#mensajes', '<spring:message code="registroEntrada.distribuir.ok" javaScriptEscape='true'/>');
                }else if(respuesta.status == 'FAIL') {
                    mensajeError('#mensajes', respuesta.error);
                }
                waitingDialog.hide();
            }
        });
    }

</script>

</body>
</html>