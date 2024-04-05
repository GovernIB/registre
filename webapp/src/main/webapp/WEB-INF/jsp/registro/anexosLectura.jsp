<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

<%--CONFIGURACIONES SEGÚN EL TIPO DE REGISTRO--%>
<c:if test="${param.tipoRegistro == RegwebConstantes.REGISTRO_ENTRADA}">
    <c:set var="color" value="info"/>
</c:if>
<c:if test="${param.tipoRegistro == RegwebConstantes.REGISTRO_SALIDA}">
    <c:set var="color" value="danger"/>
</c:if>

<div class="col-xs-12">

    <div class="panel panel-${color}">
        <%--TÍTULO--%>
        <div class="panel-heading">
            <h3 class="panel-title">
                <i class="fa fa-pencil-square-o"></i> <strong><spring:message code="anexo.anexos"/></strong>:
                <c:if test="${registro.registroDetalle.tipoDocumentacionFisica==RegwebConstantes.TIPO_DOCFISICA_ACOMPANYA_DOC_REQUERIDA}">
                    <span class="text-vermell text14"><spring:message code="tipoDocumentacionFisica.${registro.registroDetalle.tipoDocumentacionFisica}"/></span>
                </c:if>
                <c:if test="${registro.registroDetalle.tipoDocumentacionFisica==RegwebConstantes.TIPO_DOCFISICA_ACOMPANYA_DOC_COMPLEMENTARIA}">
                    <span class="text-taronja text14"><spring:message code="tipoDocumentacionFisica.${registro.registroDetalle.tipoDocumentacionFisica}"/></span>
                </c:if>
                <c:if test="${registro.registroDetalle.tipoDocumentacionFisica==RegwebConstantes.TIPO_DOCFISICA_NO_ACOMPANYA_DOC}">
                    <span class="text-verd text14"><spring:message code="tipoDocumentacionFisica.${registro.registroDetalle.tipoDocumentacionFisica}"/></span>
                </c:if>
            </h3>
        </div>

        <div class="panel-body">
            <div class="table-responsive">
                <%--SIN ANEXOS--%>
                <c:if test="${!registro.registroDetalle.tieneAnexos}">
                    <div class="alert alert-grey ">
                        <spring:message code="regweb.listado.vacio"/><strong> <spring:message code="anexo.anexo"/></strong>
                    </div>
                </c:if>
                <!-- Averiguamos si el registro tiene los anexos purgados -->
                <c:set var="anexosPurgados" value="${registro.registroDetalle.anexosPurgado}"/>
                
                <%--Tiene Anexos--%>
                <c:if test="${registro.registroDetalle.tieneAnexos}">

                    <table id="anexos" class="table table-bordered table-hover table-striped">
                        <thead>
                            <tr>
                                <th><spring:message code="anexo.titulo"/></th>
                                <th><spring:message code="anexo.sir.validezDocumento"/></th>
                                <th><spring:message code="anexo.tipoDocumento.corto"/></th>
                                <c:if test="${registro.estado != RegwebConstantes.REGISTRO_OFICIO_ACEPTADO && !anexosPurgados}">
                                    <th class="center"><spring:message code="anexo.tamano"/></th>
                                </c:if>
                                <th class="center"><spring:message code="anexo.firma"/></th>
                                <th class="center"><spring:message code="regweb.acciones"/></th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:set var="tamanyTotal" value="0"/>

                            <c:forEach var="anexoFull" items="${anexos}">
                                <c:set var="tamanyAnexo" value="0"/>
                                <!-- No mostra el justificant ni ho conta pel tamany màxim -->
                                <c:if test="${!anexoFull.anexo.justificante && anexoFull.anexo.titulo != RegwebConstantes.FICHERO_REGISTROELECTRONICO}">
                                    <tr>
                                        <%--TÍTULO--%>
                                        <td>
                                            <a data-toggle="modal" href="#detalleAnexo" onclick="obtenerAnexo(${anexoFull.anexo.id},${param.idEntidad})">
                                                <c:if test="${anexoFull.anexo.titulo != anexoFull.anexo.tituloCorto}">
                                                    <p rel="popupAbajo" data-content="<c:out value="${anexoFull.anexo.titulo}" escapeXml="true"/>"
                                                       data-toggle="popover"><c:out value="${anexoFull.anexo.tituloCorto}" escapeXml="true"/></p>
                                                </c:if>
                                                <c:if test="${anexoFull.anexo.titulo == anexoFull.anexo.tituloCorto}">
                                                    <c:out value="${anexoFull.anexo.titulo}" escapeXml="true"/>
                                                </c:if>
                                            </a>
                                        </td>
                                        <%--VALIDEZ DOCUMENTO--%>
                                        <td><spring:message code="tipoValidezDocumento.${anexoFull.anexo.validezDocumento}"/></td>

                                        <%--TIPO DOCUMENTO--%>
                                        <td><spring:message code="tipoDocumento.0${anexoFull.anexo.tipoDocumento}"/></td>

                                        <%--TAMAÑO--%>
                                        <c:if test="${registro.estado != RegwebConstantes.REGISTRO_OFICIO_ACEPTADO && !anexoFull.anexo.purgado}">
                                            <td class="text-right">
                                                <c:if test="${anexoFull.anexo.modoFirma != RegwebConstantes.MODO_FIRMA_ANEXO_ATTACHED && !anexoFull.anexo.confidencial}">
                                                    <c:set var="tamanyAnexo" value="${anexoFull.docSize}"/>
                                                </c:if>
                                                <c:if test="${anexoFull.anexo.modoFirma == RegwebConstantes.MODO_FIRMA_ANEXO_ATTACHED && !anexoFull.anexo.confidencial}">
                                                    <c:set var="tamanyAnexo" value="${anexoFull.signSize}"/>
                                                </c:if>
                                                <c:if test="${anexoFull.anexo.confidencial}">
                                                    <c:set var="tamanyAnexo" value="${anexoFull.anexo.confidencialSize}"/>
                                                </c:if>

                                                ${tamanyAnexo } KB
                                                <c:set var="tamanyTotal" value="${tamanyTotal + tamanyAnexo }"/>
                                            </td>
                                        </c:if>

                                        <%--FIRMA DEL ANEXO--%>

                                        <%-- Anexo con Firma Detached --%>
                                        <c:if test="${anexoFull.anexo.modoFirma == RegwebConstantes.MODO_FIRMA_ANEXO_DETACHED && !anexoFull.anexo.confidencial}">
                                            <c:if test="${registro.estado != RegwebConstantes.REGISTRO_OFICIO_ACEPTADO && !anexoFull.anexo.purgado}">

                                                <td class="center">
                                                    <c:if test="${anexoFull.anexo.estadoFirma == RegwebConstantes.ANEXO_FIRMA_VALIDA}">
                                                        <c:set var="clase" value="btn btn-info btn-default btn-xs"/>
                                                        <c:set var="etiqueta" value="anexo.tipofirma.detached.valido"/>
                                                    </c:if>
                                                    <c:if test="${anexoFull.anexo.estadoFirma == RegwebConstantes.ANEXO_FIRMA_INVALIDA || anexoFull.anexo.estadoFirma == RegwebConstantes.ANEXO_FIRMA_ERROR}">
                                                        <c:set var="clase" value="btn btn-danger btn-default btn-xs"/>
                                                        <c:set var="etiqueta" value="anexo.tipofirma.detached.invalido"/>
                                                    </c:if>
                                                    <c:if test="${anexoFull.anexo.estadoFirma == RegwebConstantes.ANEXO_FIRMA_NOINFO}">
                                                        <c:set var="clase" value="btn btn-warning btn-default btn-xs"/>
                                                        <c:set var="etiqueta" value="anexo.tipofirma.detached.noinfo"/>
                                                    </c:if>

                                                    <c:if test="${registro.estado != RegwebConstantes.REGISTRO_OFICIO_ACEPTADO && !anexoFull.anexo.purgado}">
                                                        <a class="${clase}" href="<c:url value="/anexo/descargarFirmaDetached/${anexoFull.anexo.id}"/>"
                                                           target="_blank" title="<spring:message code="${etiqueta}"/>"><span class="fa fa-key"></span>
                                                        </a>
                                                    </c:if>

                                                </td>
                                            </c:if>

                                            <c:if test="${registro.estado == RegwebConstantes.REGISTRO_OFICIO_ACEPTADO || anexoFull.anexo.purgado}">
                                                <td class="center"> <%--FIRMA--%>
                                                    <c:if test="${anexoFull.anexo.estadoFirma == RegwebConstantes.ANEXO_FIRMA_VALIDA}">
                                                        <c:set var="clase" value="btn btn-info btn-default btn-xs disabled"/>
                                                    </c:if>
                                                    <c:if test="${anexoFull.anexo.estadoFirma == RegwebConstantes.ANEXO_FIRMA_INVALIDA || anexoFull.anexo.estadoFirma == RegwebConstantes.ANEXO_FIRMA_ERROR}">
                                                        <c:set var="clase" value="btn btn-danger btn-default btn-xs disabled"/>
                                                    </c:if>
                                                    <c:if test="${anexoFull.anexo.estadoFirma == RegwebConstantes.ANEXO_FIRMA_NOINFO}">
                                                        <c:set var="clase" value="btn btn-warning btn-default btn-xs disabled"/>
                                                    </c:if>

                                                    <a class="${clase}" href="" target="_blank" title="<spring:message code="anexo.eliminado"/>"><span class="fa fa-key"></span></a>
                                                </td>
                                            </c:if>
                                        </c:if>

                                        <%-- TIPO ANEXO SIN FIRMA--%>
                                        <c:if test="${anexoFull.anexo.modoFirma == RegwebConstantes.MODO_FIRMA_ANEXO_SINFIRMA && !anexoFull.anexo.confidencial}">

                                            <td class="center">
                                                <p rel="popupAbajo" data-content="<spring:message code="anexo.tipofirma.sinfirma"/>" data-toggle="popover"><span class="label label-default">No</span></p>
                                            </td>
                                        </c:if>

                                        <%--TIPO ANEXO FIRMA ATTACHED--%>
                                        <c:if test="${anexoFull.anexo.modoFirma == RegwebConstantes.MODO_FIRMA_ANEXO_ATTACHED && !anexoFull.anexo.confidencial}">

                                            <td class="center">
                                                <c:if test="${anexoFull.anexo.estadoFirma == RegwebConstantes.ANEXO_FIRMA_VALIDA}">
                                                    <p rel="popupAbajo"
                                                       data-content="<spring:message code="anexo.tipofirma.attached.valido"/>(<fmt:formatDate value="${anexoFull.anexo.fechaValidacion}" pattern="dd/MM/yyyy"/>)"
                                                       data-toggle="popover"><span class="label label-success"><span
                                                            class="fa fa-key"></span></span></p>
                                                </c:if>
                                                <c:if test="${anexoFull.anexo.estadoFirma == RegwebConstantes.ANEXO_FIRMA_INVALIDA || anexoFull.anexo.estadoFirma == RegwebConstantes.ANEXO_FIRMA_ERROR}">
                                                    <p rel="popupAbajo" data-content="${anexoFull.anexo.motivoNoValidacion}" data-toggle="popover"><span class="label label-danger"><span
                                                            class="fa fa-key"></span></span></p>
                                                </c:if>
                                                <c:if test="${anexoFull.anexo.estadoFirma == RegwebConstantes.ANEXO_FIRMA_NOINFO}">
                                                    <p rel="popupAbajo" data-content="<spring:message code="anexo.tipofirma.attached"/>"
                                                       data-toggle="popover"><span class="label label-success">Si</span></p>
                                                </c:if>
                                            </td>
                                        </c:if>
                                        <%--ANEXO CONFIDENCIAL--%>
                                        <c:if test="${anexoFull.anexo.confidencial}">
                                            <td class="center">
                                                <a class="btn btn-success btn-default btn-xs disabled" href="" target="_blank" title="<spring:message code="anexo.confidencial"/>">
                                                    <span class="fa fa-lock"></span>
                                                </a>
                                            </td>
                                            <c:if test="${anexoFull.anexo.modoFirma == RegwebConstantes.MODO_FIRMA_ANEXO_SINFIRMA}">
                                                <td class="center">
                                                    <p rel="popupAbajo" data-content="<spring:message code="anexo.tipofirma.sinfirma"/>" data-toggle="popover"><span class="label label-default">No</span></p>
                                                </td>
                                            </c:if>

                                            <c:if test="${anexoFull.anexo.modoFirma == RegwebConstantes.MODO_FIRMA_ANEXO_ATTACHED}">
                                                <td class="center">
                                                    <p rel="popupAbajo" data-content="<spring:message code="anexo.tipofirma.attached"/>" data-toggle="popover"><span class="label label-success">Si</span></p>
                                                </td>
                                            </c:if>

                                            <c:if test="${anexoFull.anexo.modoFirma == RegwebConstantes.MODO_FIRMA_ANEXO_DETACHED}">
                                                <td class="center">
                                                    <a class="btn btn-warning btn-default btn-xs disabled" href="" target="_blank" title="<spring:message code="anexo.confidencial"/>">
                                                        <span class="fa fa-lock"></span>
                                                    </a>
                                                </td>
                                            </c:if>
                                        </c:if>

                                        <%--BOTONERA ACCIONES--%>
                                        <td class="center">

                                            <c:if test="${!anexoFull.anexo.confidencial}">

                                                <c:if test="${registro.estado != RegwebConstantes.REGISTRO_OFICIO_ACEPTADO && !anexoFull.anexo.purgado}">

                                                    <%@ include file="/WEB-INF/jsp/registro/visorAnexo.jsp" %>

                                                    <a class="btn btn-success btn-default btn-xs" href="<c:url value="/anexo/descargar/${anexoFull.anexo.id}"/>"
                                                       target="_blank" title="<spring:message code="anexo.descargar"/>"><span class="fa fa-download"></span>
                                                    </a>
                                                </c:if>
                                                <c:if test="${registro.estado == RegwebConstantes.REGISTRO_OFICIO_ACEPTADO || anexoFull.anexo.purgado}">
                                                    <a class="btn btn-success btn-default btn-xs disabled" href=""
                                                       target="_blank" title="<spring:message code="anexo.eliminado"/>"><span class="fa fa-window-close"></span>
                                                    </a>
                                                </c:if>

                                            </c:if>
                                            <c:if test="${anexoFull.anexo.confidencial}">
                                                <a class="btn btn-success btn-default btn-xs disabled" href="" target="_blank" title="<spring:message code="anexo.confidencial"/>"><span class="fa fa-lock"></span></a>
                                            </c:if>
                                        </td>
                                    </tr>
                                </c:if>
                            </c:forEach>
                           <%-- Fila pel tamany Total dels annexes --%>
                            <c:if test="${registro.estado != RegwebConstantes.REGISTRO_OFICIO_ACEPTADO && !anexosPurgados}">
                                <tr>
                                    <td class="senseBorder text-right" colspan="4"><spring:message code="anexo.sumatotaltamany"/>:<b>${tamanyTotal} KB</b></td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                   
                </c:if>
            </div>
        </div>
    </div>
</div>
<%--Modal para ver los dtos de un Anexo--%>
<c:import url="../registro/detalleAnexo.jsp"/>
