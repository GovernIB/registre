<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

<div class="col-xs-12">

    <ul class="timeline">

        <c:forEach var="trazabilidad" items="${trazabilidades}" varStatus="status">

            <%-- Registro que ha salido via SIR --%>
            <c:if test="${trazabilidad.tipo == RegwebConstantes.TRAZABILIDAD_OFICIO_SIR}">

                <%--OFICIO REMISION--%>
                <li>
                    <c:set var="oficioRemision" value="${trazabilidad.oficioRemision}" scope="request"/>
                    <c:set var="registroSalida" value="${trazabilidad.registroSalida}" scope="request"/>

                    <%-- Tipo anotación y Decodificación (Posteriormente se añadió esta info en Oficio Remisión, de ahí está comprobación)--%>
                    <c:if test="${empty trazabilidad.oficioRemision.tipoAnotacion}">
                        <c:set var="decodificacionTipoAnotacion" value="${trazabilidad.registroEntradaOrigen.registroDetalle.decodificacionTipoAnotacion}" scope="request"/>
                        <c:set var="tipoAnotacion" value="${trazabilidad.registroEntradaOrigen.registroDetalle.tipoAnotacion}" scope="request"/>
                    </c:if>
                    <c:if test="${not empty trazabilidad.oficioRemision.tipoAnotacion}">
                        <c:set var="decodificacionTipoAnotacion" value="${trazabilidad.oficioRemision.decodificacionTipoAnotacion}" scope="request"/>
                        <c:set var="tipoAnotacion" value="${trazabilidad.oficioRemision.tipoAnotacion}" scope="request"/>
                    </c:if>

                    <c:set var="maxReintentos" value="${maxReintentos}" scope="request"/>
                    <c:import url="../trazabilidad/oficioRemision.jsp"/>
                </li>

                <%--OFICIO ACEPTADO, REENVIADO O RECHAZADO--%>
                <c:if test="${trazabilidad.oficioRemision.estado == RegwebConstantes.OFICIO_ACEPTADO || trazabilidad.oficioRemision.estado == RegwebConstantes.OFICIO_SIR_RECHAZADO || trazabilidad.oficioRemision.estado == RegwebConstantes.OFICIO_SIR_DEVUELTO}">

                    <li class="timeline-inverted">
                        <c:set var="oficioRemision" value="${trazabilidad.oficioRemision}" scope="request"/>
                        <c:set var="registroEnviado" value="${trazabilidad.registroEntradaOrigen}" scope="request"/>
                        <c:import url="../trazabilidad/asientoDestino.jsp"/>
                    </li>

                </c:if>

            </c:if>

            <%--Recibido via SIR--%>
            <c:if test="${trazabilidad.tipo == RegwebConstantes.TRAZABILIDAD_RECIBIDO_SIR}">

                <%--REGISTRO SIR--%>
                <li class="timeline-inverted">
                    <c:set var="registroSir" value="${trazabilidad.registroSir}" scope="request"/>
                    <c:import url="../trazabilidad/registroSir.jsp"/>
                </li>

                <%--REGISTRO ENTRADA DESTINO--%>
                <c:if test="${trazabilidad.registroEntradaDestino != null}">
                    <li>
                        <c:set var="registroEntrada" value="${trazabilidad.registroEntradaDestino}" scope="request"/>
                        <fmt:message key="registroEntrada.aceptado" var="titulo" scope="request"/>
                        <c:import url="../trazabilidad/registroEntrada.jsp">
                            <c:param name="activo" value="true"/>
                        </c:import>
                    </li>
                </c:if>

            </c:if>

            <%-- Oficio Remision Interno o Externo --%>
            <c:if test="${trazabilidad.tipo == RegwebConstantes.TRAZABILIDAD_OFICIO}">

                <%-- Tipo Oficio Remision Entrada --%>
                <c:if test="${trazabilidad.oficioRemision.tipoOficioRemision == RegwebConstantes.TIPO_OFICIO_REMISION_ENTRADA}">

                    <c:if test="${registro.id == trazabilidad.registroEntradaOrigen.id}">

                        <%--OFICIO REMISION--%>
                        <li>
                            <c:set var="registroSalida" value="${trazabilidad.registroSalida}" scope="request"/>
                            <c:set var="oficioRemision" value="${trazabilidad.oficioRemision}" scope="request"/>
                            <c:import url="../trazabilidad/oficioRemision.jsp"/>
                        </li>

                        <%--REGISTRO ENTRADA DESTINO--%>
                        <c:if test="${trazabilidad.registroEntradaDestino != null}">
                            <li class="timeline-inverted">
                                <c:set var="registroEntrada" value="${trazabilidad.registroEntradaDestino}" scope="request"/>
                                <fmt:message key="registroEntrada.aceptado" var="titulo" scope="request"/>
                                <c:import url="../trazabilidad/registroEntrada.jsp"/>
                            </li>
                        </c:if>
                    </c:if>

                    <c:if test="${registro.id == trazabilidad.registroEntradaDestino.id}">

                        <%--REGISTRO ENTRADA ORIGEN--%>
                        <c:if test="${trazabilidad.oficioRemision.tipoOficioRemision == RegwebConstantes.TIPO_OFICIO_REMISION_ENTRADA}">
                            <li class="timeline-inverted">
                                <c:set var="registroEntrada" value="${trazabilidad.registroEntradaOrigen}" scope="request"/>
                                <fmt:message key="registroEntrada.origen" var="titulo" scope="request"/>
                                <c:import url="../trazabilidad/registroEntrada.jsp">
                                    <c:param name="activo" value="false"/>
                                </c:import>
                            </li>
                        </c:if>

                        <%--OFICIO REMISION--%>
                        <li class="timeline-inverted">
                            <c:set var="oficioRemision" value="${trazabilidad.oficioRemision}" scope="request"/>
                            <c:set var="registroSalida" value="${trazabilidad.registroSalida}" scope="request"/>
                            <c:import url="../trazabilidad/oficioRemision.jsp"/>
                        </li>

                        <%--REGISTRO ENTRADA--%>
                        <li>
                            <c:set var="registroEntrada" value="${registro}" scope="request"/>
                            <fmt:message key="registroEntrada.aceptado" var="titulo" scope="request"/>
                            <c:import url="../trazabilidad/registroEntrada.jsp">
                                <c:param name="activo" value="true"/>
                            </c:import>

                        </li>

                    </c:if>

                </c:if>

                <%-- Tipo Oficio Remision Salida --%>
                <c:if test="${trazabilidad.oficioRemision.tipoOficioRemision == RegwebConstantes.TIPO_OFICIO_REMISION_SALIDA}">

                    <%--REGISTRO SALIDA--%>
                    <li class="timeline-inverted">
                        <c:set var="registroSalida" value="${trazabilidad.registroSalida}" scope="request"/>
                        <fmt:message key="registroSalida.origen.registro" var="titulo" scope="request"/>
                        <c:import url="../trazabilidad/registroSalida.jsp"/>
                    </li>

                    <%--OFICIO REMISION--%>
                    <li class="timeline-inverted">
                        <c:set var="oficioRemision" value="${trazabilidad.oficioRemision}" scope="request"/>
                        <c:set var="registroSalida" value="${trazabilidad.registroSalida}" scope="request"/>
                        <c:import url="../trazabilidad/oficioRemision.jsp"/>
                    </li>

                    <%--REGISTRO ENTRADA--%>
                    <li>
                        <c:set var="registroEntrada" value="${registro}" scope="request"/>
                        <fmt:message key="registroEntrada.aceptado" var="titulo" scope="request"/>
                        <c:import url="../trazabilidad/registroEntrada.jsp">
                            <c:param name="activo" value="true"/>
                        </c:import>
                    </li>

                </c:if>

            </c:if>

            <%-- RECTIFICACIÓN --%>
            <c:if test="${trazabilidad.tipo == RegwebConstantes.TRAZABILIDAD_RECTIFICACION_ENTRADA}">

                <c:if test="${registro.id == trazabilidad.registroEntradaDestino.id}">

                    <%--RECTIFICADO--%>
                    <li class="timeline-inverted">
                        <c:set var="registroEntrada" value="${trazabilidad.registroEntradaOrigen}" scope="request"/>
                        <fmt:message key="registroEntrada.rectificado" var="titulo" scope="request"/>
                        <c:import url="../trazabilidad/registroEntrada.jsp">
                            <c:param name="activo" value="false"/>
                        </c:import>
                    </li>

                    <%--REGISTRO ENTRADA--%>
                    <li>
                        <c:set var="registroEntrada" value="${registro}" scope="request"/>
                        <fmt:message key="registroEntrada.aceptado" var="titulo" scope="request"/>
                        <c:import url="../trazabilidad/registroEntrada.jsp">
                            <c:param name="activo" value="true"/>
                        </c:import>
                    </li>

                </c:if>

                <%--RECTIFICADO--%>
                <c:if test="${registro.id == trazabilidad.registroEntradaOrigen.id}">
                    <li>
                        <c:set var="registroEntrada" value="${trazabilidad.registroEntradaDestino}" scope="request"/>
                        <fmt:message key="registroEntrada.rectificado" var="titulo" scope="request"/>
                        <c:import url="../trazabilidad/registroEntrada.jsp"/>
                    </li>
                </c:if>

            </c:if>

            <%-- DISTRIBUCIÓN --%>
            <c:if test="${trazabilidad.tipo == RegwebConstantes.TRAZABILIDAD_DISTRIBUCION}">

                <c:if test="${registro.id == trazabilidad.registroEntradaOrigen.id}">
                    <li>
                        <div class="timeline-badge primary"><i class="fa fa-sign-out"></i></div>
                        <div class="timeline-panel">
                            <div class="timeline-heading">
                                <h4 class="timeline-title">
                                    <a href="<c:url value="/registroEntrada/${trazabilidad.registroEntradaOrigen.id}/detalle"/>"><spring:message code="registroEntrada.distribuido"/></a>
                                </h4>
                                <p>
                                    <small class="text-muted"><i class="fa fa-clock-o"></i> <strong><spring:message code="registroEntrada.distribuir.fecha"/>:</strong> <fmt:formatDate value="${trazabilidad.fecha}" pattern="dd/MM/yyyy HH:mm:ss"/></small><br>
                                    <small class="text-muted"><i class="fa fa-barcode"></i> <strong><spring:message code="registroEntrada.numeroRegistro"/>:</strong> ${trazabilidad.registroEntradaOrigen.numeroRegistroFormateado}</small>
                                </p>
                            </div>
                            <div class="timeline-body">
                                <p><small><i class="fa fa-home"></i> <strong><spring:message code="registroEntrada.oficina"/>:</strong> ${trazabilidad.registroEntradaOrigen.oficina.denominacion}</small></p>
                                <p><small><i class="fa fa-bookmark"></i> <strong><spring:message code="registroEntrada.estado"/>:</strong>
                                    <c:import url="../registro/estadosRegistro.jsp">
                                        <c:param name="estado" value="${trazabilidad.registroEntradaOrigen.estado}"/>
                                    </c:import>
                                </small></p>
                            </div>
                        </div>
                    </li>
                </c:if>

            </c:if>

        </c:forEach>

    </ul>

</div>
