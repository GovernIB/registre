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

                    <%--BOTONERA--%>
                    <c:if test="${tieneJustificante}"> <%--Si se ha generado el justificante, muestra el boton paras descargarlo --%>
                        <div class="panel-footer center">

                            <%-- Si no tiene urlValidación solo podrá descargar el original --%>
                            <c:if test="${!tieneUrlValidacion}">
                             <div class="btn-group"><button type="button" class="btn btn-success btn-sm" onclick="goTo('<c:url value="/anexo/descargarFirma/${idJustificante}/true"/>')"><span class="fa fa-download"></span> <spring:message code="justificante.boton"/></button></div>
                            </c:if>

                            <%-- Si tiene urlValidación se podrá descargar el original o con el csv incrustado --%>
                            <c:if test="${tieneUrlValidacion}">
                                <div class="btn-group">
                                    <button type="button" class="btn btn-success btn-sm dropdown-toggle" data-toggle="dropdown">
                                        <spring:message code="justificante.boton"/> <span class="caret"></span>
                                    </button>
                                    <ul class="dropdown-menu">
                                        <li class="submenu-complet"><a onclick="goTo('<c:url value="/anexo/descargarFirma/${idJustificante}/true"/>')" onmouseover="this.style.cursor='pointer';"><spring:message code="justificante.original"/></a></li>
                                        <li class="submenu-complet"><a onclick="goTo('<c:url value="/anexo/descargarFirma/${idJustificante}/false"/>')" onmouseover="this.style.cursor='pointer';"><spring:message code="justificante.concsv"/></a></li>
                                    </ul>
                                </div>
                            </c:if>
                        </div>
                    </c:if>

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
                                <c:import url="../trazabilidad/trazabilidadEntrada.jsp"/>
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

</div>


<c:import url="../modulos/pie.jsp"/>

<script type="text/javascript" src="<c:url value="/js/plantilla.js"/>"></script>

<script type="text/javascript">

    // Descarga el justificante si se ha generado manualmente
    window.onload = function descargaJustificante(){
        <c:if test="${param.justificante==true}">
            mensajeSuccess('#mensajes', '<spring:message code="justificante.generando.success" javaScriptEscape='true'/>');
            goTo('<c:url value="/anexo/descargarFirma/${idJustificante}/false"/>');
        </c:if>
    };

    // Muestra los datos del hitórico seleccionado y oculta el resto
    function comparaRegistros(idHistorico){

        <c:forEach var="historico" items="${historicos}">
            $('#'+${historico.id}).hide();
        </c:forEach>
        var elemento = '#'+idHistorico;
        $(elemento).show();
    }

</script>

</body>
</html>