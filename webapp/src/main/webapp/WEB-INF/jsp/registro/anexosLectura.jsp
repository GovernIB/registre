<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

<%--CONFIGURACIONES SEGÚN EL TIPO DE REGISTRO--%>
<c:if test="${param.tipoRegistro == 'entrada'}">
    <c:set var="color" value="info"/>
</c:if>
<c:if test="${param.tipoRegistro == 'salida'}">
    <c:set var="color" value="danger"/>
</c:if>

<div class="col-xs-8 pull-right">

    <div class="panel panel-${color}">

        <div class="panel-heading">
            <h3 class="panel-title">
                <i class="fa fa-file"></i> <strong><spring:message code="anexo.anexos"/></strong>
            </h3>
        </div>

        <div class="panel-body">

                <div class="table-responsive">

                    <c:if test="${empty anexos}">
                        <div class="alert alert-grey ">
                            <spring:message code="regweb.listado.vacio"/><strong> <spring:message code="anexo.anexo"/></strong>
                        </div>
                    </c:if>

                    <c:if test="${not empty anexos}">
                        <table id="interesados" class="table table-bordered table-hover table-striped">
                            <colgroup>
                                <col>
                                <col>
                                <col>
                                <col>
                                <col>
                            </colgroup>
                            <thead>
                            <tr>
                                <th><spring:message code="anexo.titulo"/></th>
                                <th><spring:message code="anexo.tipoDocumento"/></th>
                                <th><spring:message code="anexo.tamano"/></th>
                                <th class="center">Doc</th>
                                <th class="center">Firma</th>
                            </tr>
                            </thead>

                            <tbody>
                            <c:forEach var="anexo" items="${anexos}">

                                <!-- No mostra el justificant ni ho conta pel tamany màxim -->
                                <c:if test="${!anexo.justificante}">
                                    <tr>

                                        <td>
                                            <a data-toggle="modal" href="#detalleAnexo" onclick="obtenerAnexo(${anexo.id})">${anexo.titulo}</a>
                                        </td>


                                        <td>
                                            <spring:message code="tipoDocumento.0${anexo.tipoDocumento}"/>
                                        </td>
                                        <!-- TODO mostrar el tamanyo desde custodia -->
                                        <td>
                                            <c:if test="${anexo.modoFirma != RegwebConstantes.MODO_FIRMA_ANEXO_ATTACHED}">
                                                <c:set var="tamanyAnexo" value="${reg:getSizeOfDocumentCustody(anexo.custodiaID)}" />
                                            </c:if>
                                            <c:if test="${anexo.modoFirma == RegwebConstantes.MODO_FIRMA_ANEXO_ATTACHED}">
                                                <c:set var="tamanyAnexo" value="${reg:getSizeOfSignatureCustody(anexo.custodiaID)}" />
                                            </c:if>
                                            ${tamanyAnexo } KB
                                        </td>

                                        <c:if test="${anexo.modoFirma == RegwebConstantes.MODO_FIRMA_ANEXO_DETACHED}">
                                            <td><a class="btn btn-success btn-default btn-sm"
                                                   href="<c:url value="/anexo/descargarDocumento/${anexo.id}"/>"
                                                   target="_blank" title="<spring:message code="anexo.descargar"/>"><span
                                                    class="fa fa-download"></span></a></td>
                                            <td><a class="btn btn-info btn-default btn-sm"
                                                   href="<c:url value="/anexo/descargarFirma/${anexo.id}"/>"
                                                   target="_blank" title="<spring:message code="anexo.descargar"/>"><span
                                                    class="fa fa-download"></span></a></td>
                                        </c:if>
                                        <c:if test="${anexo.modoFirma == RegwebConstantes.MODO_FIRMA_ANEXO_SINFIRMA}">
                                            <td><a class="btn btn-success btn-default btn-sm"
                                                   href="<c:url value="/anexo/descargarDocumento/${anexo.id}"/>"
                                                   target="_blank" title="<spring:message code="anexo.descargar"/>"><span
                                                    class="fa fa-download"></span></a></td>
                                            <td><span class="label label-danger">No</span></td>
                                        </c:if>
                                        <c:if test="${anexo.modoFirma == RegwebConstantes.MODO_FIRMA_ANEXO_ATTACHED}">

                                            <td><a class="btn btn-success btn-default btn-sm"
                                                   href="<c:url value="/anexo/descargarFirma/${anexo.id}"/>"
                                                   target="_blank" title="<spring:message code="anexo.descargar"/>"><span
                                                    class="fa fa-download"></span></a></td>
                                            <td><span class="label label-success">Si</span></td>
                                        </c:if>
                                    </tr>
                                </c:if>
                            </c:forEach>
                            </tbody>
                        </table>
                    </c:if>
                </div>

        </div>
    </div>
</div>
<%--Modal para ver los dtos de un Interesado--%>
<c:import url="../registro/detalleAnexo.jsp"/>
