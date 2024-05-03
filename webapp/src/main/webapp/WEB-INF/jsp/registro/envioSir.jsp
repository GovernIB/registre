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

<c:if test="${tipoRegistro == RegwebConstantes.REGISTRO_ENTRADA}">
    <c:set var="color" value="info"/>
</c:if>
<c:if test="${tipoRegistro == RegwebConstantes.REGISTRO_SALIDA}">
    <c:set var="color" value="danger"/>
</c:if>

<div class="row-fluid container main">

    <div class="well well-white">

        <!-- Miga de pan -->
        <div class="row">
            <div class="col-xs-12">
                <ol class="breadcrumb">
                    <c:import url="../modulos/migadepan.jsp"/>
                    <li class="active"><i class="fa fa-pencil-square-o"></i>
                        <c:if test="${tipoRegistro == RegwebConstantes.REGISTRO_ENTRADA}">
                            <spring:message code="registroEntrada.registroEntrada"/> ${registro.numeroRegistroFormateado}
                        </c:if>
                        <c:if test="${tipoRegistro == RegwebConstantes.REGISTRO_SALIDA}">
                            <spring:message code="registroSalida.registroSalida"/> ${registro.numeroRegistroFormateado}
                        </c:if>
                    </li>
                </ol>
            </div>
        </div><!-- Fin miga de pan -->

        <c:import url="../modulos/mensajes.jsp"/>
        <div id="mensajes"></div>

        <div class="row">

            <div class="col-xs-12">

                <div class="panel panel-${color}">
                    <div class="panel-heading">
                        <h3 class="panel-title"><i class="fa fa-file-o"></i>
                            <c:if test="${tipoRegistro == RegwebConstantes.REGISTRO_ENTRADA}">
                                <strong>SIR: <spring:message code="regweb.enviar"/> <spring:message code="registroEntrada.registroEntrada"/> ${registro.numeroRegistroFormateado} a ${destino.denominacion}</strong>
                            </c:if>
                            <c:if test="${tipoRegistro == RegwebConstantes.REGISTRO_SALIDA}">
                                <strong>SIR: <spring:message code="regweb.enviar"/> <spring:message code="registroSalida.registroSalida"/> ${registro.numeroRegistroFormateado} a ${destino.denominacion}</strong>
                            </c:if>
                        </h3>
                    </div>
                    <div class="panel-body">
                        <div class="row">

                            <form:form modelAttribute="envioSirForm" method="post" cssClass="form-horizontal">
                                <div class="col-lg-6">
                                    <div class="panel panel-default">
                                        <div class="panel-heading">
                                            <strong><spring:message code="oficina.origen"/>: ${registro.oficina.denominacion}</strong>
                                        </div>
                                        <div class="panel-body">
                                            <p><strong><i class="fa fa-home"></i> <spring:message code="registroEntrada.numeroRegistro"/>:</strong> ${registro.numeroRegistroFormateado}</p>
                                            <p><strong><i class="fa fa-clock-o"></i> <spring:message code="regweb.fecha"/>:</strong> <fmt:formatDate value="${registro.fecha}" pattern="dd/MM/yyyy HH:mm:ss"/></p>
                                        </div>
                                    </div>
                                </div>

                                <div class="col-lg-6">
                                    <div class="panel panel-default">
                                        <div class="panel-heading">
                                            <strong><spring:message code="oficina.destino"/></strong>
                                        </div>
                                        <div class="panel-body">
                                            <p><strong><i class="fa fa-institution"></i> <spring:message code="registroEntrada.organismoDestino"/>:</strong> ${destino.denominacion} <c:if test="${destino.codigoEstadoEntidad != RegwebConstantes.ESTADO_ENTIDAD_VIGENTE}"><span class="label label-danger"><spring:message code="unidad.estado.${destino.codigoEstadoEntidad}" /></span></c:if></p>

                                            <input type="hidden" id="idRegistro" name="idRegistro" value="${registro.id}"/>

                                            <!-- Gestión de sustitutos -->
                                            <!-- Si hay sustitutos es que el organismo al que va dirigido está extinguido -->
                                            <c:if test="${fn:length(sustitutos) > 1}">
                                                <p><span class="text-vermell ">
                                                        <strong><spring:message code="registroSir.organismo.destino.extinguido"/> ${destino.denominacion}</strong>
                                                </span></p>
                                            </c:if>

                                            <!-- Sustitutos -->
                                            <c:if test="${fn:length(sustitutos) == 1}">
                                                <p><strong><i class="fa fa-home"></i> <spring:message code="registroEntrada.organismoDestino.sustituto"/>:</strong> ${sustitutos[0].denominacion}</p>
                                                <input type="hidden" id="destinoSIRCodigo" name="destinoSIRCodigo" value="${sustitutos[0].codigo}"/>
                                            </c:if>
                                            <c:if test="${fn:length(sustitutos) > 1}">
                                                <div class="form-group">
                                                    <div class="col-xs-3"><strong><i class="fa fa-home"></i> <spring:message code="registroEntrada.organismoDestino.sustituto"/>:</strong></div>
                                                    <div class="col-xs-9">

                                                        <form:select path="destinoSIRCodigo" items="${sustitutos}"
                                                                     itemLabel="denominacion"
                                                                     itemValue="codigo"
                                                                     class="form-control"
                                                                     onchange="actualizarOficinasSIR()"/>
                                                    </div>
                                                </div>
                                            </c:if>

                                            <!-- Oficina Sir destinataria -->
                                            <!-- Una sola oficina SIR -->
                                            <c:if test="${fn:length(oficinasSIR) == 1}">
                                                   <p><strong><i class="fa fa-home"></i> <spring:message code="oficina.destino"/>:</strong> ${oficinasSIR[0].denominacion} (${oficinasSIR[0].codigo})</p>
                                                   <input type="hidden" id="oficinaSIRCodigo" name="oficinaSIRCodigo" value="${oficinasSIR[0].codigo}"/>
                                            </c:if>
                                            <!-- Más de 1 Oficina SIR -->
                                            <c:if test="${fn:length(oficinasSIR) > 1}">
                                                <div class="form-group">
                                                    <div class="col-xs-3"><strong><i class="fa fa-home"></i> <spring:message code="oficina.destino"/>:</strong></div>
                                                    <div class="col-xs-9">
                                                        <form:select path="oficinaSIRCodigo" items="${oficinasSIR}"
                                                                     itemLabel="denominacion"
                                                                     itemValue="codigo"
                                                                     class="form-control"/>
                                                    </div>
                                                </div>
                                            </c:if>

                                            <!-- Div de oficinas SIR  que se monta via js cuando hay más de un órgano sustituto y lo escogen-->
                                            <div class="form-group" id="ofSIR">
                                                <div class="col-xs-3" ><strong><i class="fa fa-home"></i> <spring:message code="oficina.destino"/>:</strong></div>
                                                <div id="oficinaSIR">
                                                    <c:if test="${fn:length(oficinasSIR) == 0}">
                                                        <spring:message code="registroSir.error.sinoficinas"/>
                                                    </c:if>
                                                </div>
                                            </div>
                                            <!-- Fin Gestión sustitutos -->
                                        </div>
                                    </div>
                                </div>

                                <div class="form-actions col-xs-12">
                                    <input type="button" id="enviar" value="<spring:message code="regweb.enviar"/>" class="btn btn-${color} btn-sm" <c:if test="${fn:length(oficinasSIR)==0}">disabled</c:if>/>

                                    <c:if test="${tipoRegistro == RegwebConstantes.REGISTRO_ENTRADA}">
                                        <input type="button" value="<spring:message code="regweb.cancelar"/>" onclick="goTo('<c:url value="/registroEntrada/${registro.id}/detalle"/>')" class="btn btn-sm"/>
                                    </c:if>
                                    <c:if test="${tipoRegistro == RegwebConstantes.REGISTRO_SALIDA}">
                                        <input type="button" value="<spring:message code="regweb.cancelar"/>" onclick="goTo('<c:url value="/registroSalida/${registro.id}/detalle"/>')" class="btn btn-sm"/>
                                    </c:if>
                                </div>
                            </form:form>
                        </div>
                    </div>
                </div>
            </div>

            <div class="col-xs-8 col-xs-offset">
                <c:import url="../modulos/mensajes.jsp"/>
            </div>
        </div><!-- /div.row-->
    </div>
</div> <!-- /container -->

<c:import url="../modulos/pie.jsp"/>

<script type="text/javascript" src="<c:url value="/js/organismosaprocesar.js"/>"></script>
<script type="text/javascript">

    $(document).ready(function() {
        //Ocultamos el div ofSIR que se monta via js cuando hay sustitutos
        $('#ofSIR').hide();

        $('#enviar').click(function(){

            var url;
            var urlDetalle;

            if(${tipoRegistro == RegwebConstantes.REGISTRO_ENTRADA}){
                url = '<c:url value="/registroEntrada/${registro.id}/enviarSir"/>';
                urlDetalle ='<c:url value="/registroEntrada/${registro.id}/detalle"/>';
            }
            if(${tipoRegistro == RegwebConstantes.REGISTRO_SALIDA}){
                url = '<c:url value="/registroSalida/${registro.id}/enviarSir"/>';
                urlDetalle ='<c:url value="/registroSalida/${registro.id}/detalle"/>';
            }
            
            $.ajax({
                url: url,
                type:'POST',
                data: {oficinaSIRCodigo: $("#oficinaSIRCodigo").val()},
                beforeSend: function(objeto){
                    waitingDialog.show('<spring:message code="registroSir.enviando" javaScriptEscape='true'/>', {dialogSize: 'm', progressType: 'primary'});
                },
                success:function(respuesta){

                    if(respuesta.status == 'SUCCESS'){
                        goTo(urlDetalle);

                    }else{
                        if(respuesta.status=='FAIL') {
                            mostrarMensajeError('#mensajes', respuesta.error);
                            waitingDialog.hide();
                        }
                    }
                }
            });
        });
    });

    /** Mensajes para la funcionalidad para cargar las oficinas SIR del destino indicado*/
    var mensaje = "<spring:message code='registroSir.error.sinoficinas' javaScriptEscape='true' />";
    var mensaje2 = "<spring:message code='registroSir.obtener.oficinasSIR' javaScriptEscape='true' />";

    /**
     * Función para actualizar las oficinas SIR desde el select de los sustitutos cuando el destino está extinguido
     */
    function actualizarOficinasSIR(){
        <c:url var="obtenerOficinasSIR" value="/rest/obtenerOficinasSIR" />
        var codigoDestinoSIR = $('#destinoSIRCodigo option:selected').val();
        if(codigoDestinoSIR !== '-1'){
            //En función del codigoDestinoSIR indicado cargamos sus Oficinas SIR
            cargarOficinasSIR('${obtenerOficinasSIR}',codigoDestinoSIR,'oficinaSIRCodigo','0',false, mensaje,mensaje2,'form-control','#enviar');

        }
    }
</script>

</body>
</html>