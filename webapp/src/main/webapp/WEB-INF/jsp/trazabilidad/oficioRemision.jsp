<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

<div class="timeline-badge success"><i class="fa fa-envelope-o"></i></div>

<div class="timeline-panel">
    <%--Título--%>
    <div class="timeline-heading">
        <h4 class="timeline-title">
            <a href="<c:url value="/oficioRemision/${oficioRemision.id}/detalle"/>">
                <c:if test="${oficioRemision.sir == false}">
                    <c:if test="${not empty oficioRemision.organismoDestinatario}">
                        <spring:message code="oficioRemision.oficioRemision.interno"/>
                    </c:if>

                    <c:if test="${empty oficioRemision.organismoDestinatario}">
                        <spring:message code="oficioRemision.oficioRemision.externo"/>
                    </c:if>
                </c:if>

                <c:if test="${oficioRemision.sir == true}">
                    <spring:message code="oficioRemision.oficioRemision.sir"/>
                </c:if>
            </a>
        </h4>
        <p>
            <small class="text-muted"><i class="fa fa-clock-o"></i> <strong><spring:message code="oficioRemision.fecha"/>:</strong> <fmt:formatDate value="${oficioRemision.fecha}" pattern="dd/MM/yyyy HH:mm:ss"/></small><br>
        </p>
    </div>
    <div class="timeline-body">

        <%--Oficina origen--%>
        <p>
            <small><i class="fa fa-home"></i> <strong><spring:message code="oficioRemision.oficina"/>:</strong>
                ${oficioRemision.oficina.denominacion}
            </small>
        </p>

        <%--Organismo destino--%>
        <p>
            <small><i class="fa fa-exchange"></i> <strong><spring:message code="oficioRemision.organismoDestino"/>:</strong>
                <c:if test="${not empty oficioRemision.organismoDestinatario}">${oficioRemision.organismoDestinatario.denominacion} (${oficioRemision.organismoDestinatario.codigo})</c:if>
                <c:if test="${empty oficioRemision.organismoDestinatario}">${oficioRemision.destinoExternoDenominacion} (${oficioRemision.destinoExternoCodigo})</c:if>
            </small>
        </p>


        <%--OficioRemision SIR--%>
        <c:if test="${oficioRemision.sir == true}">

            <!--Oficina destino -->
            <c:if test="${not empty oficioRemision.decodificacionEntidadRegistralDestino}">
                <c:if test="${not empty oficioRemision.contactosEntidadRegistralDestino}">
                    <p id="contactosOficina" data-content="${oficioRemision.contactosEntidadRegistralDestino}" data-toggle="popover" style="cursor:help" rel="popupDerecha">
                    <small><i class="fa fa-exchange"></i> <strong><spring:message code="oficioRemision.oficinaSirDestino"/>:</strong> ${oficioRemision.decodificacionEntidadRegistralDestino} (${oficioRemision.codigoEntidadRegistralDestino})</small></p>
                </c:if>
                <c:if test="${empty oficioRemision.contactosEntidadRegistralDestino}">
                    <p><small><i class="fa fa-exchange"></i> <strong><spring:message code="oficioRemision.oficinaSirDestino"/>:</strong> ${oficioRemision.decodificacionEntidadRegistralDestino} (${oficioRemision.codigoEntidadRegistralDestino})</small></p>
                </c:if>
            </c:if>

            <%--Identificador intercambio--%>
            <c:if test="${loginInfo.rolActivo.nombre == 'RWE_ADMIN'}">
                <p>
                    <small><i class="fa fa-qrcode"></i> <strong><spring:message code="registroSir.identificadorIntercambio"/>:</strong>
                        <a target="_blank" href="<c:url value="/sir/${oficioRemision.identificadorIntercambio}/detalle"/>">${oficioRemision.identificadorIntercambio}</a>
                    </small>
                </p>
            </c:if>
            <c:if test="${loginInfo.rolActivo.nombre == 'RWE_USUARI'}">
                <p>
                    <small><i class="fa fa-qrcode"></i> <strong><spring:message code="registroSir.identificadorIntercambio"/>:</strong> ${oficioRemision.identificadorIntercambio}</small>
                </p>
            </c:if>

            <%--Tipo intercambio (Tipo anotación)  {Envío, Reenvío, Rechazo} --%>
            <p>
                <small><i class="fa fa-send"></i> <strong><spring:message code="registroSir.tipoIntercambio"/>: <spring:message code="registroSir.tipoAnotacion.${tipoAnotacion}"/></strong></small>
            </p>

            <%--Motivo intercambio (Descripción Tipo anotación) --%>
            <c:if test="${tipoAnotacion != RegwebConstantes.TIPO_ANOTACION_ENVIO}">
                <p>
                    <small><i class="fa fa-file-text-o"></i> <strong><spring:message code="registroSir.motivo"/>:</strong> ${decodificacionTipoAnotacion}</small>
                </p>
            </c:if>

            <%--Estado --%>
            <p>
                <small><i class="fa fa-bookmark"></i> <strong><spring:message code="oficioRemision.estado"/>:</strong>

                    <c:if test="${oficioRemision.estado == RegwebConstantes.OFICIO_ACEPTADO}"><span class="label label-success"></c:if>
                    <c:if test="${oficioRemision.estado == RegwebConstantes.OFICIO_SIR_ENVIADO}"><span class="label label-warning"></c:if>
                    <c:if test="${oficioRemision.estado == RegwebConstantes.OFICIO_SIR_ENVIADO_ACK}"><span class="label label-success"></c:if>
                    <c:if test="${oficioRemision.estado == RegwebConstantes.OFICIO_SIR_ENVIADO_ERROR}"><span class="label label-danger"></c:if>
                    <c:if test="${oficioRemision.estado == RegwebConstantes.OFICIO_SIR_REENVIADO}"><span class="label label-warning"></c:if>
                    <c:if test="${oficioRemision.estado == RegwebConstantes.OFICIO_SIR_REENVIADO_ACK}"><span class="label label-success"></c:if>
                    <c:if test="${oficioRemision.estado == RegwebConstantes.OFICIO_SIR_REENVIADO_ERROR}"><span class="label label-danger"></c:if>
                    <c:if test="${oficioRemision.estado == RegwebConstantes.OFICIO_SIR_RECHAZADO}"><span class="label label-danger"></c:if>
                    <c:if test="${oficioRemision.estado == RegwebConstantes.OFICIO_SIR_DEVUELTO}"><span class="label label-danger"></c:if>
                    <c:if test="${oficioRemision.estado == RegwebConstantes.OFICIO_ANULADO}"><span class="label label-danger"></c:if>

                    <spring:message code="oficioRemision.estado.${oficioRemision.estado}"/> - <fmt:formatDate value="${oficioRemision.fechaEstado}" pattern="dd/MM/yyyy HH:mm:ss"/>
                </span>
                </small>
            </p>

            <%--Código y descripción error--%>
            <c:if test="${oficioRemision.estado == RegwebConstantes.OFICIO_SIR_ENVIADO_ERROR || oficioRemision.estado == RegwebConstantes.OFICIO_SIR_REENVIADO_ERROR}">
                <p>
                    <small><i class="fa fa-comment"></i> <strong><spring:message code="registroSir.descripcionError"/>:</strong> ${oficioRemision.codigoError} - ${oficioRemision.descripcionError}</small>
                </p>
            </c:if>

            <%--Reintentos--%>
            <p>
                <c:if test="${oficioRemision.numeroReintentos > 0 && oficioRemision.numeroReintentos < maxReintentos}">
                    <small><i class="fa fa-retweet"></i> <strong><spring:message code="oficioRemision.reintentos"/>:</strong> ${oficioRemision.numeroReintentos}</small>
                </c:if>
                <c:if test="${oficioRemision.numeroReintentos == maxReintentos}">
                    <small><i class="fa fa-retweet"></i> <strong><spring:message code="oficioRemision.reintentos"/>:</strong> <spring:message code="oficioRemision.reintentos.max"/> (${maxReintentos})</small>
                </c:if>
            </p>

        </c:if>

        <%--OficioRemision normal--%>
        <c:if test="${oficioRemision.sir == false}">
            <p>
                <small><i class="fa fa-bookmark"></i> <strong><spring:message code="oficioRemision.estado"/>:</strong>
                    <c:if test="${oficioRemision.estado == RegwebConstantes.OFICIO_INTERNO_ENVIADO}"><span class="label label-success"></c:if>
                    <c:if test="${oficioRemision.estado == RegwebConstantes.OFICIO_EXTERNO_ENVIADO}"><span class="label label-success"></c:if>
                    <c:if test="${oficioRemision.estado == RegwebConstantes.OFICIO_ACEPTADO}"><span class="label label-success"></c:if>
                    <c:if test="${oficioRemision.estado == RegwebConstantes.OFICIO_ANULADO}"><span class="label label-danger"></c:if>

                    <spring:message code="oficioRemision.estado.${oficioRemision.estado}"/> - <fmt:formatDate value="${oficioRemision.fechaEstado}" pattern="dd/MM/yyyy HH:mm:ss"/>

                    </span>
                </small>
            </p>
        </c:if>

        <%--Registro Salida--%>
        <c:if test="${not empty registroSalida}">
            <p>
                <small><i class="fa fa-share"></i> <strong><spring:message code="registroSalida.registroSalida"/>:</strong>
                    <a target="_blank" href="<c:url value="/registroSalida/${registroSalida.id}/detalle"/>">${registroSalida.numeroRegistroFormateado}</a>
                </small>
            </p>
        </c:if>
    </div>
</div>