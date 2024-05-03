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

                    <%--BOTONERA--%>

                    <%--Botones Justificante y Sello--%>
                    <c:if test="${registro.estado != RegwebConstantes.REGISTRO_PENDIENTE_VISAR && registro.estado != RegwebConstantes.REGISTRO_ANULADO}">
                        <div class="panel-footer center">

                            <%--Si no és una Reserva de Número, se muestras las opciones del Justificante --%>
                            <c:if test="${registro.estado != RegwebConstantes.REGISTRO_RESERVA}">

                                <%--Si no se ha generado el justificante y el registro no está ANULADO, muestra el boton para generarlo --%>
                                <c:if test="${!tieneJustificante && registro.estado != RegwebConstantes.REGISTRO_ANULADO && permisoEditar}">
                                    <div class="btn-group">
                                        <button type="button" class="btn btn-warning btn-sm dropdown-toggle" data-toggle="dropdown">
                                            <spring:message code="justificante.boton"/> <span class="caret"></span>
                                        </button>
                                        <ul class="dropdown-menu">
                                            <li class="submenu-complet"><a onclick="crearJustificante('<c:url value="/registroEntrada/${idRegistro}/justificante/ca"/>')" onmouseover="this.style.cursor='pointer';"><spring:message code="regweb.catalan"/></a></li>
                                            <li class="submenu-complet"><a onclick="crearJustificante('<c:url value="/registroEntrada/${idRegistro}/justificante/es"/>')" onmouseover="this.style.cursor='pointer';"><spring:message code="regweb.castellano"/></a></li>
                                        </ul>
                                    </div>
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
                            </c:if>
                            <%-- Botón de sello --%>
                            <div class="btn-group"><button type="button" data-toggle="modal" data-target="#selloModal" class="btn btn-warning btn-sm"><spring:message code="sello.imprimir"/></button></div>

                        </div>
                    </c:if>

                    <%--Botón Distribuir y Oficio Remision--%>
                    <c:if test="${registro.estado == RegwebConstantes.REGISTRO_VALIDO && permisoEditar}">

                        <%--ES UNA DISTRIBUCIÓN--%>
                        <c:if test="${(registro.evento == RegwebConstantes.EVENTO_DISTRIBUIR && permisoDistribuir && not empty registro.registroDetalle.interesados)}">
                            <div class="panel-footer center">

                                <c:if test="${(registro.registroDetalle.tipoDocumentacionFisica == RegwebConstantes.TIPO_DOCFISICA_NO_ACOMPANYA_DOC || registro.registroDetalle.tipoDocumentacionFisica == RegwebConstantes.TIPO_DOCFISICA_ACOMPANYA_DOC_COMPLEMENTARIA)
                                  && fn:length(registro.registroDetalle.anexos) == 0}">
                                    <button type="button" onclick='mensajeInformativo("<spring:message code="distribuir.advertencia" htmlEscape="true"/>","<spring:message code="distribuir.tipoDoc.anexos" htmlEscape="true"/>")'
                                            class="btn btn-success btn-sm btn-block"><spring:message code="regweb.distribuir"/></button>
                                </c:if>

                                <c:if test="${registro.registroDetalle.tipoDocumentacionFisica == RegwebConstantes.TIPO_DOCFISICA_ACOMPANYA_DOC_REQUERIDA || fn:length(registro.registroDetalle.anexos) > 0}">
                                    <button type="button" onclick='confirmDistribuir("<spring:message code="regweb.confirmar.distribuir" htmlEscape="true"/>", ${pluginDistribucionEmail})'
                                            class="btn btn-success btn-sm btn-block"><spring:message code="regweb.distribuir"/></button>
                                </c:if>

                            </div>
                        </c:if>

                        <%--ES UN OFICIO DE REMISIÓN--%>
                        <c:if test="${registro.evento != RegwebConstantes.EVENTO_DISTRIBUIR}">

                            <div class="panel-footer center">

                                <%--OFICIO DE REMISIÓN EXTERNO--%>
                                <c:if test="${registro.evento == RegwebConstantes.EVENTO_OFICIO_EXTERNO}">
                                    <button type="button" onclick="goTo('<c:url value="/oficioRemision/entradasPendientesRemision/3"/>')" class="btn btn-success btn-sm btn-block">
                                        <spring:message code="oficioRemision.boton.crear.externo"/>
                                    </button>
                                </c:if>

                                <%--OFICIO DE REMISIÓN SIR--%>
                                <c:if test="${registro.evento == RegwebConstantes.EVENTO_OFICIO_SIR && loginInfo.oficinaActiva.sirEnvio}">

                                    <c:if test="${empty erroresAnexosSir}">

                                        <!-- Si es Documentación en papel (ROJO) -->
                                        <c:if test="${registro.registroDetalle.tipoDocumentacionFisica == RegwebConstantes.TIPO_DOCFISICA_ACOMPANYA_DOC_REQUERIDA}">
                                            <button type="button" onclick='javascript:confirmEnvioSinAnexos("<c:url value="/registroEntrada/${registro.id}/enviarSir"/>","<spring:message code="regweb.confirmar.SIR.rojo" htmlEscape="true"/>", "<spring:message code="regweb.informacion.importante" htmlEscape="true"/>","<spring:message code="regweb.seguir" htmlEscape="true"/>","<spring:message code="regweb.seguir.no" htmlEscape="true"/>",${!tieneJustificante})' href="javascript:void(0);" class="btn btn-primary btn-sm btn-block">
                                                <spring:message code="registroEntrada.enviar.sir"/>
                                            </button>
                                        </c:if>

                                        <!-- Si es Documentación en papel y digitalizada (AMARILLO) -->
                                        <c:if test="${registro.registroDetalle.tipoDocumentacionFisica == RegwebConstantes.TIPO_DOCFISICA_ACOMPANYA_DOC_COMPLEMENTARIA}">
                                            <c:if test="${fn:length(anexos)==0 && tieneJustificante==false}">
                                                <button type="button" onclick='javascript:confirmEnvioSinAnexos("<c:url value="/registroEntrada/${registro.id}/enviarSir"/>","<spring:message code="regweb.confirmar.SIR.amarillo" htmlEscape="true"/>", "<spring:message code="regweb.anexos.vacio" htmlEscape="true"/>","<spring:message code="regweb.enviar" htmlEscape="true"/>","<spring:message code="regweb.anexos.añadir" htmlEscape="true"/>",true)' href="javascript:void(0);" class="btn btn-primary btn-sm btn-block">
                                                    <spring:message code="registroEntrada.enviar.sir"/>
                                                </button>
                                            </c:if>
                                            <c:if test="${fn:length(anexos)!=0 || tieneJustificante==true}">
                                                <button type="button" onclick="goTo('<c:url value="/registroEntrada/${registro.id}/enviarSir"/>')" class="btn btn-success btn-sm btn-block">
                                                    <spring:message code="registroEntrada.enviar.sir"/>
                                                </button>
                                            </c:if>
                                        </c:if>

                                        <!-- Si es Documentación digitalizada (VERDE) -->
                                        <c:if test="${registro.registroDetalle.tipoDocumentacionFisica == RegwebConstantes.TIPO_DOCFISICA_NO_ACOMPANYA_DOC}">
                                            <c:if test="${fn:length(anexos) == 0 && tieneJustificante==false}">
                                                <button type="button" onclick='javascript:confirmEnvioSinAnexos("<c:url value="/registroEntrada/${registro.id}/enviarSir"/>","<spring:message code="regweb.confirmar.SIR.verde" htmlEscape="true"/>", "<spring:message code="regweb.anexos.vacio" htmlEscape="true"/>","<spring:message code="regweb.enviar" htmlEscape="true"/>","<spring:message code="regweb.anexos.añadir" htmlEscape="true"/>", true)' href="javascript:void(0);" class="btn btn-primary btn-sm btn-block">
                                                    <spring:message code="registroEntrada.enviar.sir"/>
                                                </button>
                                            </c:if>
                                            <c:if test="${fn:length(anexos) != 0 || tieneJustificante==true}">
                                                <button type="button" onclick="goTo('<c:url value="/registroEntrada/${registro.id}/enviarSir"/>')" class="btn btn-primary btn-sm btn-block">
                                                    <spring:message code="registroEntrada.enviar.sir"/>
                                                </button>
                                            </c:if>
                                        </c:if>

                                    </c:if>

                                    <c:if test="${not empty erroresAnexosSir}">
                                        <button type="button" class="btn btn-success btn-sm btn-block disabled">
                                            <spring:message code="registroEntrada.enviar.sir"/>
                                        </button>
                                    </c:if>
                                </c:if>

                                <%--OFICIO DE REMISIÓN SIR, PERO NO ESTÁ ACTIVA LA OFICINA ACTIVA--%>
                                <c:if test="${registro.evento == RegwebConstantes.EVENTO_OFICIO_SIR && !loginInfo.oficinaActiva.sirEnvio}">
                                    <p class="text-danger">El <strong>${(empty registro.destino)? registro.destinoExternoDenominacion : registro.destino.denominacion}</strong> <spring:message code="oficioRemision.noSIR.1"/> ${loginInfo.oficinaActiva.denominacion} <spring:message code="oficioRemision.noSIR.2"/></p>
                                    <button type="button" onclick="goTo('<c:url value="/oficioRemision/entradasPendientesRemision/3"/>')" class="btn btn-success btn-sm btn-block">
                                        <spring:message code="oficioRemision.boton.crear.externo"/>
                                    </button>
                                </c:if>

                            </div>
                        </c:if>

                    </c:if>


                    <div class="panel-footer center">

                        <%--Botón nuevo registro--%>
                        <c:if test="${registro.estado != RegwebConstantes.REGISTRO_RESERVA}">
                            <div class="btn-group"><button type="button" onclick="goTo('<c:url value="/registroEntrada/new"/>')" class="btn btn-info btn-sm"><spring:message code="registro.boton.nuevo"/></button></div>
                        </c:if>

                        <%--Botón nueva reserva--%>
                        <c:if test="${registro.estado == RegwebConstantes.REGISTRO_RESERVA}">
                            <div class="btn-group"><button type="button" onclick="goTo('<c:url value="/registroEntrada/reserva"/>')" class="btn btn-info btn-sm"><spring:message code="registro.boton.nuevo.reserva"/></button></div>
                        </c:if>

                        <%--Botón Editar Registro--%>
                        <c:if test="${(registro.estado == RegwebConstantes.REGISTRO_VALIDO || registro.estado == RegwebConstantes.REGISTRO_RESERVA) && registro.registroDetalle.presencial && permisoEditar && !tieneJustificante}">
                            <div class="btn-group"><button type="button" onclick="goTo('<c:url value="/registroEntrada/${registro.id}/edit"/>')" class="btn btn-warning btn-sm"><spring:message code="registro.boton.editar"/></button></div>
                        </c:if>

                        <%--Botón Activar--%>
                        <c:if test="${registro.estado == RegwebConstantes.REGISTRO_ANULADO && permisoEditar}">
                            <div class="btn-group"><button type="button" onclick='javascript:confirm("<c:url value="/registroEntrada/${registro.id}/activar"/>","<spring:message code="regweb.confirmar.activar" htmlEscape="true"/>")' class="btn btn-primary btn-sm"><spring:message code="regweb.activar"/></button></div>
                        </c:if>

                        <%--Botón Visar--%>
                        <c:if test="${registro.estado == RegwebConstantes.REGISTRO_PENDIENTE_VISAR && isResponsableOrganismo}">
                            <div class="btn-group"><button type="button" onclick='javascript:confirm("<c:url value="/registroEntrada/${registro.id}/visar"/>","<spring:message code="regweb.confirmar.visar" htmlEscape="true"/>")' class="btn btn-success btn-sm"><spring:message code="regweb.visar"/></button></div>
                        </c:if>

                        <%--Botón Anular--%>
                        <c:if test="${(registro.estado == RegwebConstantes.REGISTRO_VALIDO || registro.estado == RegwebConstantes.REGISTRO_RESERVA || registro.estado == RegwebConstantes.REGISTRO_PENDIENTE_VISAR) && permisoEditar}">
                            <div class="btn-group">
                                <a data-toggle="modal" role="button" href="#anularModal" onclick="limpiarModalAnulacion(${registro.id});" class="btn btn-danger btn-sm"><spring:message code="regweb.anular"/></a>
                            </div>
                        </c:if>

                        <%--Botón reenviar--%>
                        <c:if test="${(registro.estado == RegwebConstantes.REGISTRO_RECHAZADO || registro.estado == RegwebConstantes.REGISTRO_REENVIADO) && permisoEditar}">
                            <div class="btn-group"><button type="button" onclick='javascript:goTo("<c:url value="/registroEntrada/${registro.id}/reenviar"/>")' class="btn btn-success btn-sm"><spring:message code="registro.boton.reenviar"/></button></div>
                        </c:if>

                        <%--Botón rectificar--%>
                        <c:if test="${(registro.estado == RegwebConstantes.REGISTRO_ANULADO || registro.estado == RegwebConstantes.REGISTRO_RECHAZADO) && permisoEditar}">
                            <div class="btn-group"><button type="button" onclick='javascript:confirm("<c:url value="/registroEntrada/${registro.id}/rectificar"/>","<spring:message code="regweb.confirmar.rectificar" htmlEscape="true"/>")' class="btn btn-danger btn-sm"><spring:message code="registro.boton.rectificar"/></button></div>
                        </c:if>

                    </div>

                </div>

            </div>
            <!-- Fin Panel Lateral izquierdo -->

            <!-- Inicio pestañas (General, Trazabiliad, Modificaciones) -->

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
                    </ul>

                    <div id="contenido" class="tab-content contentInfo">

                        <div class="tab-pane" id="general">

                            <!-- ANEXOS -->
                            <c:if test="${registro.registroDetalle.tipoDocumentacionFisica != RegwebConstantes.TIPO_DOCFISICA_ACOMPANYA_DOC_REQUERIDA || registro.registroDetalle.tieneAnexos}">
                                <!-- ANEXOS COMPLETO-->
                                <c:if test="${anexosEditar}">
                                    <c:import url="../registro/anexos.jsp">
                                        <c:param name="tipoRegistro" value="${RegwebConstantes.REGISTRO_ENTRADA}"/>
                                    </c:import>
                                </c:if>

                                <%--ANEXOS SOLO LECTURA--%>
                                <c:if test="${not anexosEditar}">
                                   <c:import url="../registro/anexosLectura.jsp">
                                       <c:param name="tipoRegistro" value="${RegwebConstantes.REGISTRO_ENTRADA}"/>
                                       <c:param name="idEntidad" value="${registro.entidad.id}"/>
                                   </c:import>
                                </c:if>
                            </c:if>

                            <%--INTERESADOS--%>
                            <c:if test="${interesadosEditar}">
                                <c:import url="../registro/interesados.jsp">
                                    <c:param name="tipoRegistro" value="${RegwebConstantes.REGISTRO_ENTRADA}"/>
                                    <c:param name="idRegistroDetalle" value="${registro.registroDetalle.id}"/>
                                </c:import>
                            </c:if>

                            <%--INTERESADOS SOLO LECTURA--%>
                            <c:if test="${not interesadosEditar}">
                                <c:import url="../registro/interesadosLectura.jsp">
                                    <c:param name="tipoRegistro" value="${RegwebConstantes.REGISTRO_ENTRADA}"/>
                                </c:import>
                            </c:if>

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
                                    <c:param name="adminEntidad" value="false"/>
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
                    </div>
                </div>
            </c:if>
        </div>
    </div><!-- /div.row-->

    <%--Modal ANULAR--%>
    <c:import url="../registro/anular.jsp">
        <c:param name="tipoRegistro" value="${RegwebConstantes.REGISTRO_ENTRADA}"/>
    </c:import>

    <%--SELLO --%>
    <c:import url="../registro/sello.jsp">
        <c:param name="tipoRegistro" value="${RegwebConstantes.REGISTRO_ENTRADA}"/>
    </c:import>

    <%--Modal de distribución vial mail --%>
    <c:import url="../registro/registroDistribuir.jsp">
        <c:param name="aceptarRegistroSir" value="false"/>
    </c:import>

</div>

<c:import url="../modulos/pie.jsp"/>

<script type="text/javascript">
    var urlDistribuir = '<c:url value="/registroEntrada/${registro.id}/distribuir"/>';
    var urlDetalle = '<c:url value="/registroEntrada/${registro.id}/detalle" />';

    <%-- Traducciones para distribuir.js --%>
    var traddistribuir = new Array();
    traddistribuir['distribuir.distribuyendo'] ="<spring:message code="registroEntrada.distribuyendo" javaScriptEscape="true"/>";

    var tradestado = new Array();
    tradestado['estado.E'] = "<spring:message code="unidad.estado.E" javaScriptEscape='true' />";
    tradestado['estado.V'] = "<spring:message code="unidad.estado.V" javaScriptEscape='true' />";
    tradestado['estado.A'] = "<spring:message code="unidad.estado.A" javaScriptEscape='true' />";
    tradestado['estado.T'] = "<spring:message code="unidad.estado.T" javaScriptEscape='true' />";

</script>

<script type="text/javascript" src="<c:url value="/js/busquedaorganismo.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/sello.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/plantilla.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/distribuir.js"/>"></script>

<script type="text/javascript">

    // Descarga el justificante si se ha generado manualmente
    window.onload = function descargaJustificante(){
        <c:if test="${param.justificante==true}">
            mensajeSuccess('#mensajes', '<spring:message code="justificante.generando.success" javaScriptEscape='true'/>');
            goTo('<c:url value="/anexo/descargarJustificante/${idJustificante}/false"/>');
        </c:if>
    };

    // Muestra mensaje informativo si el registro es Presencial de una persona jurídica
    $(document).ready(function () {
        <c:if test="${avisoRegistroPresencial}">
        mensajeInformativo("<spring:message code="aviso.registro.presencial.titulo" htmlEscape="true"/>","<spring:message code="aviso.registro.presencial" htmlEscape="true"/>")
        </c:if>
    });


    // Muestra los datos del hitórico seleccionado y oculta el resto
    function comparaRegistros(idHistorico){

        <c:forEach var="historico" items="${historicos}">
            $('#'+${historico.id}).hide();
        </c:forEach>
        var elemento = '#'+idHistorico;
        $(elemento).show();
    }

    function actualizarLocalidad(){
        <c:url var="obtenerLocalidades" value="/rest/obtenerLocalidades" />
        actualizarSelect('${obtenerLocalidades}','#localidad\\.id',$('#provincia\\.id option:selected').val(),$('#localidad\\.id option:selected').val(),false,false);
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
                    goTo('<c:url value="/registroEntrada/${idRegistro}/detalle?justificante=true"/>');
                }else if(respuesta.status == 'FAIL') {
                    mensajeError('#mensajes', respuesta.error);
                    waitingDialog.hide();
                }
            }
        });
    }

</script>

</body>
</html>