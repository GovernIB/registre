<%@ page import="es.caib.regweb3.persistence.utils.PropiedadGlobalUtil" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

<c:if test="${param.tipoRegistro == RegwebConstantes.REGISTRO_ENTRADA}">
    <c:set value="divider-info" var="divider"/>
</c:if>
<c:if test="${param.tipoRegistro == RegwebConstantes.REGISTRO_SALIDA}">
    <c:set value="divider-danger" var="divider"/>
</c:if>

<dt><i class="fa fa-home"></i> <spring:message code="oficina.oficina"/>: </dt> <dd> ${registro.oficina.denominacion}</dd>
<c:if test="${not empty registro.fecha}">
	<dt><i class="fa fa-clock-o"></i> <spring:message code="regweb.fecha"/>: </dt> <dd> <fmt:formatDate value="${registro.fecha}" pattern="dd/MM/yyyy HH:mm:ss"/></dd>
</c:if>
<%--
<dt><i class="fa fa-book"></i> <spring:message code="libro.libro"/>: </dt> <dd> ${registro.libro.nombre}</dd>
 --%>
<c:if test="${param.tipoRegistro == RegwebConstantes.REGISTRO_ENTRADA}">
    <c:if test="${not empty registro.destino}"> <dt><i class="fa fa-institution"></i> <spring:message code="registroEntrada.organismoDestino"/>: </dt> <dd>${registro.destino.denominacion} - ${registro.destino.codigo} <c:if test="${registro.destino.estado.codigoEstadoEntidad != RegwebConstantes.ESTADO_ENTIDAD_VIGENTE}"><span class="label label-danger"><spring:message code="unidad.estado.${registro.destino.estado.codigoEstadoEntidad}" /></span></c:if> </dd></c:if>
    <c:if test="${not empty registro.destinoExternoCodigo}">

        <dt><i class="fa fa-institution"></i> <spring:message code="registroEntrada.organismoDestino"/>: </dt>
        <dd> <div id="estadoExterno">${registro.destinoExternoDenominacion} - ${registro.destinoExternoCodigo} </div>
        </dd>
        <!-- Funcionalidad para consultar via rest a dir3caib el estado de un organismo externo y se muestra en el div "estadoExterno"-->
        <script type="text/javascript">
            /**
             * Obtiene el estado de un organismo externo
             * @param url donde hacer la petición ajax
             * @param id
             * @param elemento
             * @return Texto con la traducción del elmento solicitado
             */
            function obtenerEstadoOrganismoExterno(url, codigo,elemento){
                jQuery.ajax({
                    url: url,
                    data: { codigo: codigo },
                    type: 'GET',
                    success: function(result) {
                        var html;
                        if(result != 'V') {
                            html ='<c:out value="${registro.destinoExternoDenominacion}" escapeXml="true"/>' +' - '+ '${registro.destinoExternoCodigo}' + ' - <span class="label label-danger">' + tradestado['estado.' + result] + '</span>';
                        }
                        $('#'+elemento).html(html);
                    }
                }) ;
            }


            $(document).ready(function(){
                var urlCompleta= '<%=PropiedadGlobalUtil.getDir3CaibServer()%>'+ '/rest/GET/unidad/estado';
                obtenerEstadoOrganismoExterno(urlCompleta, '${registro.destinoExternoCodigo}','estadoExterno');
            });
        </script>
    </c:if>
</c:if>
<c:if test="${param.tipoRegistro == RegwebConstantes.REGISTRO_SALIDA}">
    <dt><i class="fa fa-institution"></i> <spring:message code="registroSalida.origen"/>: </dt> <dd>${registro.origen.denominacion} - ${registro.origen.codigo} <c:if test="${registro.origen.estado.codigoEstadoEntidad != RegwebConstantes.ESTADO_ENTIDAD_VIGENTE}"><span class="label label-danger"><spring:message code="unidad.estado.${registro.origen.estado.codigoEstadoEntidad}" /></span></c:if></dd>
</c:if>
<c:set var="docFisica" value="${registro.registroDetalle.tipoDocumentacionFisica}"/>

<c:if test="${(docFisica==RegwebConstantes.TIPO_DOCFISICA_ACOMPANYA_DOC_REQUERIDA || docFisica==RegwebConstantes.TIPO_DOCFISICA_ACOMPANYA_DOC_COMPLEMENTARIA) && not empty registro.registroDetalle.direccionPostalDestino}">
<dt><i class="fa fa-address-card"></i> <spring:message code="registroEntrada.organismoDestino.direccion"/>: </dt>
<dd>${registro.registroDetalle.direccionPostalDestino}</dd>
</c:if>
<hr class="${divider}">
<c:if test="${not empty registro.registroDetalle.extracto}"><dt><i class="fa fa-file-text-o"></i> <spring:message code="registroEntrada.extracto"/>: </dt> <dd> <c:out value="${registro.registroDetalle.extracto}" escapeXml="true"/></dd></c:if>
<c:if test="${not empty registro.registroDetalle.reserva}"><dt><i class="fa fa-file-text-o"></i> <spring:message code="registroEntrada.reserva"/>: </dt> <dd> ${registro.registroDetalle.reserva}</dd></c:if>
<c:if test="${not empty registro.registroDetalle.tipoDocumentacionFisica}"><dt><i class="fa fa-file-text-o"></i> <spring:message code="registroEntrada.tipoDocumentacionFisica"/>: </dt> <dd>
    <!-- Pone el color que corresponde con el el Tipo de documentacion elegido -->
    <c:if test="${registro.registroDetalle.tipoDocumentacionFisica==RegwebConstantes.TIPO_DOCFISICA_ACOMPANYA_DOC_REQUERIDA}">
        <span class="text-vermell">
    </c:if>
    <c:if test="${registro.registroDetalle.tipoDocumentacionFisica==RegwebConstantes.TIPO_DOCFISICA_ACOMPANYA_DOC_COMPLEMENTARIA}">
        <span class="text-taronja">
    </c:if>
    <c:if test="${registro.registroDetalle.tipoDocumentacionFisica==RegwebConstantes.TIPO_DOCFISICA_NO_ACOMPANYA_DOC}">
        <span class="text-verd">
    </c:if>
    <spring:message code="tipoDocumentacionFisica.${registro.registroDetalle.tipoDocumentacionFisica}" /></span>
</dd>
</c:if>
<%--<c:if test="${not empty registro.registroDetalle.tipoAsunto}"><dt><i class="fa fa-thumb-tack"></i> <spring:message code="tipoAsunto.tipoAsunto"/>: </dt> <dd> <i:trad value="${registro.registroDetalle.tipoAsunto}" property="nombre"/></dd></c:if>--%>
<c:if test="${not empty registro.registroDetalle.idioma}"><dt><i class="fa fa-bullhorn"></i> <spring:message code="registroEntrada.idioma"/>:</dt> <dd> <spring:message code="idioma.${registro.registroDetalle.idioma}"/></dd></c:if>
<c:if test="${not empty registro.registroDetalle.codigoAsunto}"> <dt><i class="fa fa-thumb-tack"></i> <spring:message code="codigoAsunto.codigoAsunto"/>: </dt> <dd> <i:trad value="${registro.registroDetalle.codigoAsunto}" property="nombre"/></dd></c:if>
<c:if test="${not empty registro.registroDetalle.referenciaExterna}"> <dt><i class="fa fa-thumb-tack"></i> <spring:message code="registroEntrada.referenciaExterna"/>: </dt> <dd> ${registro.registroDetalle.referenciaExterna}</dd></c:if>
<c:if test="${not empty registro.registroDetalle.expediente}"> <dt><i class="fa fa-newspaper-o"></i> <spring:message code="registroEntrada.expediente"/>: </dt> <dd> ${registro.registroDetalle.expediente}</dd></c:if>
<c:if test="${not empty registro.registroDetalle.transporte || not empty registro.registroDetalle.numeroTransporte}"> <dt><i class="fa fa-bus"></i> <spring:message code="registroEntrada.transporte"/>: </dt></c:if>  <c:if test="${not empty registro.registroDetalle.transporte}"><dd> <spring:message code="transporte.0${registro.registroDetalle.transporte}"/></c:if>  <c:if test="${not empty registro.registroDetalle.numeroTransporte}">${registro.registroDetalle.numeroTransporte}</dd></c:if>
<c:if test="${not empty registro.registroDetalle.oficinaOrigen}"> <dt><i class="fa fa-home"></i> <spring:message code="registroEntrada.oficinaOrigen"/>: </dt> <dd> ${registro.registroDetalle.oficinaOrigen.denominacion} </dd></c:if>
<c:if test="${not empty registro.registroDetalle.oficinaOrigenExternoCodigo}"> <dt><i class="fa fa-home"></i> <spring:message code="registroEntrada.oficinaOrigen"/>: </dt> <dd> ${registro.registroDetalle.oficinaOrigenExternoDenominacion}</dd></c:if>
<c:if test="${not empty registro.registroDetalle.numeroRegistroOrigen}"> <dt><i class="fa fa-barcode"></i> <spring:message code="registroEntrada.numeroRegistroOrigen"/>: </dt> <dd> ${registro.registroDetalle.numeroRegistroOrigen}</dd></c:if>
<c:if test="${not empty registro.registroDetalle.fechaOrigen}"> <dt><i class="fa fa-clock-o"></i> <spring:message code="registroEntrada.fechaOrigen"/>: </dt> <dd> <fmt:formatDate value="${registro.registroDetalle.fechaOrigen}" pattern="dd/MM/yyyy HH:mm:ss"/></dd></c:if>
<c:if test="${not empty registro.registroDetalle.codigoSia}"> <dt><i class="fa fa-barcode"></i> <spring:message code="registroEntrada.codigoSIA"/>: </dt> <dd> ${registro.registroDetalle.codigoSia}</dd></c:if>
<c:if test="${not empty registro.registroDetalle.observaciones}"> <dt><i class="fa fa-file-text-o"></i> <spring:message code="registroEntrada.observaciones"/>: </dt> <dd> ${registro.registroDetalle.observaciones}</dd></c:if>
<hr class="${divider}">
<c:if test="${loginInfo.rolActivo.nombre == 'RWE_ADMIN'}">
    <c:if test="${not empty registro.usuario}">
        <dt><i class="fa fa-user"></i> <spring:message code="registroEntrada.usuario"/>: </dt> <dd> ${registro.usuario.usuario.nombreIdentificador}</dd>
    </c:if>
</c:if>
<c:if test="${registro.registroDetalle.presencial}">
    <dt><i class="fa fa-street-view"></i> <spring:message code="registro.presencial"/>: </dt> <dd> <span class="label label-success"><spring:message code="regweb.si"/></span></dd>
</c:if>
<c:if test="${not registro.registroDetalle.presencial}">
    <dt><i class="fa fa-street-view"></i> <spring:message code="registro.presencial"/>: </dt> <dd> <span class="label label-danger"><spring:message code="regweb.no" /></span></dd>
    <c:if test="${not empty registro.registroDetalle.aplicacionTelematica}">
        <dt><i class="fa fa-gears"></i> <spring:message code="registroEntrada.aplicacion"/>: </dt> <dd> ${registro.registroDetalle.aplicacionTelematica}</dd>
    </c:if>
</c:if>
<dt><i class="fa fa-bookmark"></i> <spring:message code="registroEntrada.estado"/>: </dt>
<dd class="eti-rechazo">
    <c:import url="../registro/estadosRegistro.jsp">
        <c:param name="estado" value="${registro.estado}"/>
        <c:param name="decodificacionTipoAnotacion" value="${registro.registroDetalle.decodificacionTipoAnotacion}"/>
    </c:import>
</dd>
