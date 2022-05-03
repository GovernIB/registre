<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

<dl class="detalle_registro">

    <%--Oficina Inicio--%>
    <c:if test="${not empty registroSir.codigoEntidadRegistralInicio}">
        <%--Si es diferente a la de Origen--%>
        <c:if test="${registroSir.codigoEntidadRegistralInicio != registroSir.codigoEntidadRegistralOrigen}">
            <dt><i class="fa fa-home"></i> <spring:message code="registroSir.oficinaInicio"/>:
            </dt>
            <dd>
                <c:if test="${not empty registroSir.decodificacionEntidadRegistralInicio}">
                    ${registroSir.decodificacionEntidadRegistralInicio} -
                </c:if>
                    ${registroSir.codigoEntidadRegistralInicio}
            </dd>
            <hr class="divider-primary">
        </c:if>

    </c:if>

    <c:if test="${not empty registroSir.fechaRecepcion}">
        <dt><i class="fa fa-clock-o"></i> <spring:message code="registroSir.fechaRecepcion"/>: </dt>
        <dd><fmt:formatDate value="${registroSir.fechaRecepcion}" pattern="dd/MM/yyyy HH:mm:ss"/></dd>
    </c:if>

    <c:if test="${not empty registroSir.numeroRegistro}">
        <dt><i class="fa fa-barcode"></i> <spring:message code="registroSir.numeroRegistro"/>: </dt>
        <dd>${registroSir.numeroRegistro}</dd>
    </c:if>

    <c:if test="${not empty registroSir.fechaRegistro}">
        <dt><i class="fa fa-clock-o"></i> <spring:message code="registroSir.fechaRegistro"/>: </dt>
        <dd><fmt:formatDate value="${registroSir.fechaRegistro}" pattern="dd/MM/yyyy HH:mm:ss"/></dd>
    </c:if>

    <dt><i class="fa fa-file-o"></i> <spring:message code="registroSir.tipoRegistro"/>: </dt>
    <c:if test="${registroSir.tipoRegistro == 'ENTRADA'}">
        <dd><span class="label label-info"><spring:message code="registroSir.entrada"/></span></dd>
    </c:if>
    <c:if test="${registroSir.tipoRegistro == 'SALIDA'}">
        <dd><span class="label label-danger"><spring:message code="registroSir.salida"/></span></dd>
    </c:if>

    <hr class="divider-primary">

    <c:if test="${loginInfo.rolActivo.nombre == 'RWE_ADMIN'}">
        <%--Oficina Donde se recibió el registro originalmente--%>
        <dt><i class="fa fa-home"></i> <spring:message code="registroSir.oficinaOriginal"/>:
        </dt>
        <dd>
            <c:if test="${not empty registroSir.codigoEntidadRegistral}">
                ${registroSir.codigoEntidadRegistral} -
            </c:if>
        </dd>
        <hr class="divider-primary">
    </c:if>

    <%--Unidad Tramitación Origen--%>
    <c:if test="${not empty registroSir.codigoUnidadTramitacionOrigen}">
        <dt><i class="fa fa-institution"></i> <spring:message code="registroSir.unidadOrigen"/>:
        </dt>
        <dd>
            <c:if test="${not empty registroSir.decodificacionUnidadTramitacionOrigen}">
                ${registroSir.decodificacionUnidadTramitacionOrigen} -
            </c:if>
                ${registroSir.codigoUnidadTramitacionOrigen}
        </dd>
    </c:if>

    <%--Oficina Origen--%>
    <dt><i class="fa fa-home"></i> <spring:message code="registroSir.oficinaOrigen"/>:
    </dt>
    <dd>
        <c:if test="${not empty registroSir.decodificacionEntidadRegistralOrigen}">
            ${registroSir.decodificacionEntidadRegistralOrigen} -
        </c:if>
        ${registroSir.codigoEntidadRegistralOrigen}
    </dd>

    <hr class="divider-primary">

    <%--Unidad Tramitación Destino--%>
    <c:if test="${not empty registroSir.codigoUnidadTramitacionDestino}">
        <dt><i class="fa fa-institution"></i> <spring:message code="registroSir.unidadDestino"/>:
        </dt>
        <dd>
            <c:if test="${not empty registroSir.decodificacionUnidadTramitacionDestino}">
                ${registroSir.decodificacionUnidadTramitacionDestino} -
            </c:if>
                ${registroSir.codigoUnidadTramitacionDestino}
            <c:if test="${not empty estadoDestino and estadoDestino.codigoEstadoEntidad != RegwebConstantes.ESTADO_ENTIDAD_VIGENTE}"><span class="label label-danger"><spring:message code="unidad.estado.${estadoDestino.codigoEstadoEntidad}" /></span></c:if>
        </dd>
    </c:if>

    <%--Oficina Destino--%>
    <c:if test="${not empty registroSir.codigoEntidadRegistralDestino}">
        <dt><i class="fa fa-home"></i> <spring:message code="registroSir.oficinaDestino"/>:
        </dt>
        <dd>
            <c:if test="${not empty registroSir.decodificacionEntidadRegistralDestino}">
                ${registroSir.decodificacionEntidadRegistralDestino} -
            </c:if>
                ${registroSir.codigoEntidadRegistralDestino}
        </dd>
    </c:if>
    <hr class="divider-primary">

    <c:if test="${not empty registroSir.nombreUsuario}">
        <dt><i class="fa fa-user"></i> <spring:message code="usuario.usuario"/>: </dt>
        <dd> ${registroSir.nombreUsuario}</dd>
    </c:if>

    <c:if test="${not empty registroSir.contactoUsuario}">
        <dt><i class="fa fa-at"></i> <spring:message code="registroSir.contacto"/>: </dt>
        <dd> ${registroSir.contactoUsuario}</dd>
    </c:if>
    <c:if test="${not empty registroSir.identificadorIntercambio}">
        <dt><i class="fa fa-qrcode"></i> <spring:message code="registroSir.identificadorIntercambio"/>: </dt>
        <dd> ${registroSir.identificadorIntercambio}</dd>
    </c:if>

    <c:if test="${not empty registroSir.tipoAnotacion}">
        <dt><i class="fa fa-map-marker"></i> <spring:message code="registroSir.tipoAnotacion"/>: </dt>
        <dd>
            <c:if test="${registroSir.tipoAnotacion == '01'}">
                <span class="label label-warning"><spring:message
                        code="registroSir.tipoAnotacion.${registroSir.tipoAnotacion}"/></span>
            </c:if>
            <c:if test="${registroSir.tipoAnotacion == '02'}">
                <span class="label label-success"><spring:message
                        code="registroSir.tipoAnotacion.${registroSir.tipoAnotacion}"/></span>
            </c:if>
            <c:if test="${registroSir.tipoAnotacion == '03'}">
                <span class="label label-info"><spring:message
                        code="registroSir.tipoAnotacion.${registroSir.tipoAnotacion}"/></span>
            </c:if>
            <c:if test="${registroSir.tipoAnotacion == '04'}">
                <span class="label label-danger"><spring:message
                        code="registroSir.tipoAnotacion.${registroSir.tipoAnotacion}"/></span>
            </c:if>
        </dd>
    </c:if>
    <c:if test="${not empty registroSir.decodificacionTipoAnotacion}">
        <dt><i class="fa fa-newspaper-o"></i> <spring:message code="registroSir.descripcionTipoAnotacion"/>: </dt>
        <dd> ${registroSir.decodificacionTipoAnotacion}</dd>
    </c:if>
    <%--<c:if test="${not empty registroSir.indicadorPrueba}">
        <dt><i class="fa fa-tag"></i> <spring:message code="registroSir.indicadorPrueba"/>: </dt>
        <dd>
            <c:if test="${registroSir.indicadorPrueba == 'NORMAL'}">
                <span class="label label-success"><spring:message
                        code="registroSir.indicadorPrueba.normal"/></span>
            </c:if>
            <c:if test="${registroSir.indicadorPrueba == 'PRUEBA'}">
                <span class="label label-danger"><spring:message
                        code="registroSir.indicadorPrueba.prueba"/></span>
            </c:if>
        </dd>
    </c:if>--%>

    <c:if test="${not empty registroSir.documentacionFisica}">
        <dt><i class="fa fa-file"></i> <spring:message code="registroSir.tipoDocumentacionFisica"/>: </dt>
        <dd>
            <!-- Pone el color que corresponde con el el Tipo de documentacion elegido -->
            <c:if test="${registroSir.documentacionFisica==RegwebConstantes.TIPO_DOCFISICA_ACOMPANYA_DOC_REQUERIDA}">
                <span class="text-vermell"><spring:message code="tipoDocumentacionFisica.${registroSir.documentacionFisica}"/></span>
            </c:if>
            <c:if test="${registroSir.documentacionFisica==RegwebConstantes.TIPO_DOCFISICA_ACOMPANYA_DOC_COMPLEMENTARIA}">
                <span class="text-taronja"><spring:message code="tipoDocumentacionFisica.${registroSir.documentacionFisica}"/></span>
            </c:if>
            <c:if test="${registroSir.documentacionFisica==RegwebConstantes.TIPO_DOCFISICA_NO_ACOMPANYA_DOC}">
                <span class="text-verd"><spring:message code="tipoDocumentacionFisica.${registroSir.documentacionFisica}"/></span>
            </c:if>
        </dd>
    </c:if>

    <c:if test="${not empty registroSir.resumen}">
        <dt><i class="fa fa-file-text-o"></i> <spring:message code="registroSir.extracto"/>: </dt>
        <dd> ${registroSir.resumen}</dd>
    </c:if>

    <c:if test="${not empty registroSir.codigoAsunto}">
        <dt><i class="fa fa-thumb-tack"></i> <spring:message code="codigoAsunto.codigoAsunto"/>: </dt>
        <dd> ${registroSir.codigoAsunto}</dd>
    </c:if>

    <c:if test="${not empty registroSir.referenciaExterna}">
        <dt><i class="fa fa-thumb-tack"></i> <spring:message code="registroSir.referenciaExterna"/>: </dt>
        <dd> ${registroSir.referenciaExterna}</dd>
    </c:if>

    <c:if test="${not empty registroSir.numeroExpediente}">
        <dt><i class="fa fa-newspaper-o"></i> <spring:message
                code="registroSir.expediente"/>:
        </dt>
        <dd> ${registroSir.numeroExpediente}</dd>
    </c:if>

    <c:if test="${not empty registroSir.tipoTransporte}">
        <dt><i class="fa fa-bus"></i> <spring:message code="registroSir.transporte"/>:
        </dt>
        <dd><spring:message code="transporte.${registroSir.tipoTransporte}"/> ${registroSir.numeroTransporte}</dd>
    </c:if>

    <c:if test="${not empty registroSir.observacionesApunte}">
        <dt><i class="fa fa-file-text-o"></i> <spring:message code="registroSir.observaciones"/>: </dt>
        <dd> ${registroSir.observacionesApunte}</dd>
    </c:if>

    <c:if test="${not empty registroSir.aplicacion}">
        <dt><i class="fa fa-gears"></i> <spring:message code="registroSir.aplicacion"/>: </dt>
        <dd> ${registroSir.aplicacion}</dd>
    </c:if>

    <c:if test="${not empty registroSir.estado}">
        <dt><i class="fa fa-bookmark"></i> <spring:message code="registroSir.estado"/>: </dt>
        <dd class="eti-rechazo">
            <c:import url="/WEB-INF/jsp/registroSir/estadosRegistroSir.jsp" />
        </dd>
    </c:if>

    <c:if test="${not empty registroSir.fechaEstado}">
        <dt><i class="fa fa-clock-o"></i> <spring:message code="registroSir.fechaEstado"/>: </dt>
        <dd><fmt:formatDate value="${registroSir.fechaEstado}" pattern="dd/MM/yyyy HH:mm:ss"/></dd>
    </c:if>

    <c:if test="${registroSir.numeroReintentos > 0}">
        <dt><i class="fa fa-retweet"></i> <spring:message code="oficioRemision.reintentos"/>: </dt>
        <dd> ${registroSir.numeroReintentos}</dd>
    </c:if>

    <c:if test="${registroSir.estado == 'REENVIADO_Y_ERROR' || registroSir.estado == 'RECHAZADO_Y_ERROR'}">
        <c:if test="${not empty registroSir.codigoError}">
            <dt><i class="fa fa-bug"></i> <spring:message code="registroSir.codigoError"/>: </dt>
            <dd> ${registroSir.codigoError}</dd>
        </c:if>

        <c:if test="${not empty registroSir.descripcionError}">
            <dt><i class="fa fa-comment"></i> <spring:message code="registroSir.descripcionError"/>: </dt>
            <dd> ${registroSir.descripcionError}</dd>
        </c:if>
    </c:if>


</dl>