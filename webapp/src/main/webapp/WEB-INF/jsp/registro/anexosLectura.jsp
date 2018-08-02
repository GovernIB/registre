<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

<%--CONFIGURACIONES SEGÚN EL TIPO DE REGISTRO--%>
<c:if test="${param.tipoRegistro == 'entrada'}">
    <c:set var="color" value="info"/>
</c:if>
<c:if test="${param.tipoRegistro == 'salida'}">
    <c:set var="color" value="danger"/>
</c:if>

<div class="col-xs-12">

    <div class="panel panel-${color}">

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
                <%--No tiene Anexos--%>
                <c:if test="${empty registro.registroDetalle.anexos}">
                    <div class="alert alert-grey ">
                        <spring:message code="regweb.listado.vacio"/><strong> <spring:message
                            code="anexo.anexo"/></strong>
                    </div>
                </c:if>
                <%--Tiene Anexos--%>
                <c:if test="${not empty registro.registroDetalle.anexos && fn:length(registro.registroDetalle.anexos) >= 1}">
                    <c:if test="${fn:length(registro.registroDetalle.anexos) == 1 && registro.registroDetalle.tieneJustificante}">
                        <div class="alert alert-grey ">
                            <spring:message code="regweb.listado.vacio"/><strong> <spring:message
                                code="anexo.anexo"/></strong>
                        </div>
                    </c:if>
                    <c:if test="${(fn:length(registro.registroDetalle.anexos) == 1 && !registro.registroDetalle.tieneJustificante) ||
                            fn:length(registro.registroDetalle.anexos) > 1 }">

                        <table id="anexos" class="table table-bordered table-hover table-striped">
                            <colgroup>
                                <col>
                                <col>
                                <c:if test="${registro.estado != RegwebConstantes.REGISTRO_OFICIO_ACEPTADO}">
                                    <col>
                                </c:if>
                                <col>
                                <col>
                            </colgroup>
                            <thead>
                            <tr>
                                <th><spring:message code="anexo.titulo"/></th>
                                <th><spring:message code="anexo.tipoDocumento"/></th>
                                <c:if test="${registro.estado != RegwebConstantes.REGISTRO_OFICIO_ACEPTADO}">
                                    <th><spring:message code="anexo.tamano"/></th>
                                </c:if>
                                <th class="center"><spring:message code="anexo.anexo"/></th>
                                <th class="center">Firma</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach var="anexo" items="${registro.registroDetalle.anexos}">
                                <c:set var="tamanyAnexo" value="0"/>
                                <!-- No mostra el justificant ni ho conta pel tamany màxim -->
                                <c:if test="${!anexo.justificante}">
                                    <tr>
                                        <td>
                                            <a data-toggle="modal" href="#detalleAnexo"
                                               onclick="obtenerAnexo(${anexo.id},${param.idEntidad})">
                                                <c:if test="${anexo.titulo != anexo.tituloCorto}">
                                                    <p rel="ayuda"
                                                       data-content="<c:out value="${anexo.titulo}" escapeXml="true"/>"
                                                       data-toggle="popover"><c:out value="${anexo.tituloCorto}"
                                                                                    escapeXml="true"/></p>
                                                </c:if>
                                                <c:if test="${anexo.titulo == anexo.tituloCorto}">
                                                    <c:out value="${anexo.titulo}" escapeXml="true"/>
                                                </c:if>
                                            </a>
                                        </td>
                                        <td>
                                            <spring:message code="tipoDocumento.0${anexo.tipoDocumento}"/>
                                        </td>
                                        <td>
                                            <c:if test="${registro.estado != RegwebConstantes.REGISTRO_OFICIO_ACEPTADO }">
                                                <c:if test="${anexo.modoFirma != RegwebConstantes.MODO_FIRMA_ANEXO_ATTACHED}">
                                                    <c:set var="tamanyAnexo"
                                                           value="${reg:getSizeOfDocumentCustody(anexo.custodiaID,param.idEntidad)}"/>
                                                </c:if>
                                                <c:if test="${anexo.modoFirma == RegwebConstantes.MODO_FIRMA_ANEXO_ATTACHED}">
                                                    <c:set var="tamanyAnexo"
                                                           value="${reg:getSizeOfSignatureCustody(anexo.custodiaID,param.idEntidad)}"/>
                                                </c:if>
                                            </c:if>
                                                ${tamanyAnexo } KB
                                        </td>
                                        <c:if test="${anexo.modoFirma == RegwebConstantes.MODO_FIRMA_ANEXO_DETACHED}">
                                            <td class="center">
                                                <c:if test="${registro.estado != RegwebConstantes.REGISTRO_OFICIO_ACEPTADO}">
                                                    <a class="btn btn-success btn-default btn-sm"
                                                       href="<c:url value="/anexo/descargarDocumento/${anexo.id}"/>"
                                                       target="_blank"
                                                       title="<spring:message code="anexo.descargar"/>">
                                                        <span class="fa fa-download"></span>
                                                    </a>
                                                </c:if>
                                            </td>
                                            <td class="center">
                                                <c:if test="${registro.estado != RegwebConstantes.REGISTRO_OFICIO_ACEPTADO}">

                                                    <c:if test="${anexo.firmaValida}">
                                                        <c:set var="clase" value="btn btn-info btn-default btn-sm"/>
                                                        <c:set var="etiqueta" value="anexo.tipofirma.detached.valido"/>
                                                    </c:if>
                                                    <c:if test="${!anexo.firmaValida}">
                                                        <c:set var="clase" value="btn btn-danger btn-default btn-sm"/>
                                                        <c:set var="etiqueta" value="anexo.tipofirma.detached.invalido"/>
                                                    </c:if>
                                                    <a class="${clase}"
                                                       href="<c:url value="/anexo/descargarFirma/${anexo.id}/true"/>"
                                                       target="_blank"
                                                       title="<spring:message code="${etiqueta}"/>">
                                                        <span class="fa fa-key"></span>
                                                    </a>
                                                </c:if>
                                            </td>
                                        </c:if>
                                        <c:if test="${anexo.modoFirma == RegwebConstantes.MODO_FIRMA_ANEXO_SINFIRMA}">
                                            <td class="center">
                                                <c:if test="${registro.estado != RegwebConstantes.REGISTRO_OFICIO_ACEPTADO}">
                                                    <a class="btn btn-success btn-default btn-sm"
                                                       href="<c:url value="/anexo/descargarDocumento/${anexo.id}"/>"
                                                       target="_blank"
                                                       title="<spring:message code="anexo.descargar"/>">
                                                        <span class="fa fa-download"></span>
                                                    </a>
                                                </c:if>
                                            </td>
                                            <td class="center"><p rel="ayuda"
                                                                  data-content="<spring:message code="anexo.tipofirma.sinfirma"/>"
                                                                  data-toggle="popover"><span
                                                    class="label label-default">No</span></p></td>
                                        </c:if>
                                        <c:if test="${anexo.modoFirma == RegwebConstantes.MODO_FIRMA_ANEXO_ATTACHED}">
                                            <td class="center">
                                                <c:if test="${registro.estado != RegwebConstantes.REGISTRO_OFICIO_ACEPTADO}">
                                                    <a class="btn btn-success btn-default btn-sm"
                                                       href="<c:url value="/anexo/descargarFirma/${anexo.id}/true"/>"
                                                       target="_blank"
                                                       title="<spring:message code="anexo.descargar"/>">
                                                        <span class="fa fa-download"></span></a>
                                                </c:if>
                                            </td>
                                            <td class="center">
                                                <c:if test="${anexo.estadoFirma == RegwebConstantes.ANEXO_FIRMA_VALIDA}">
                                                    <p rel="ayuda"
                                                       data-content="<spring:message code="anexo.tipofirma.attached.valido"/>(<fmt:formatDate value="${anexo.fechaValidacion}" pattern="dd/MM/yyyy"/>)"
                                                       data-toggle="popover"><span class="label label-success"><span
                                                            class="fa fa-key"></span></span></p>
                                                </c:if>
                                                <c:if test="${anexo.estadoFirma == RegwebConstantes.ANEXO_FIRMA_INVALIDA || anexo.estadoFirma == RegwebConstantes.ANEXO_FIRMA_ERROR}">
                                                    <p rel="ayuda" data-content="${anexo.motivoNoValidacion}"
                                                       data-toggle="popover"><span class="label label-danger"><span
                                                            class="fa fa-key"></span></span></p>
                                                </c:if>
                                                <c:if test="${anexo.estadoFirma == RegwebConstantes.ANEXO_FIRMA_NOINFO}">
                                                    <p rel="ayuda"
                                                       data-content="<spring:message code="anexo.tipofirma.attached"/>"
                                                       data-toggle="popover"><span
                                                            class="label label-success">Si</span></p>
                                                </c:if>
                                            </td>
                                        </c:if>
                                    </tr>
                                </c:if>
                            </c:forEach>
                            </tbody>
                        </table>
                    </c:if>
                </c:if>
            </div>
        </div>
    </div>
</div>
<%--Modal para ver los dtos de un Anexo--%>
<c:import url="../registro/detalleAnexo.jsp"/>
