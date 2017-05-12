<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

<div class="col-xs-8 pull-right">

    <div class="panel panel-primary">

        <div class="panel-heading">
            <h3 class="panel-title"><i class="fa fa-pencil-square-o"></i> <strong><spring:message
                    code="anexo.anexos"/></strong>: <spring:message
                    code="tipoDocumentacionFisica.${registroSir.documentacionFisica}"/>
            </h3>
        </div>

        <div class="panel-body">
            <div class="col-xs-12">
                <div id="anexosdiv" class="table-responsive">

                    <c:if test="${empty registroSir.anexos}">
                        <div class="alert alert-warning alert-dismissable">
                            <strong><spring:message code="regweb.listado.vacio"/> <spring:message code="anexo.anexo"/></strong>
                        </div>
                    </c:if>

                    <c:if test="${not empty registroSir.anexos}">
                        <table id="anexos" class="table table-bordered table-hover table-striped">
                            <colgroup>
                                <col>
                                <col>
                                <c:if test="${registroSir.estado != 'ACEPTADO'}">
                                    <col>
                                    <col>
                                    <col>
                                </c:if>
                                <col width="50">
                            </colgroup>
                            <thead>
                            <tr>
                                <th><spring:message code="anexo.titulo"/></th>
                                <th><spring:message code="anexo.sir.tipoDocumento"/></th>
                                <th><spring:message code="anexo.tamano"/></th>
                                <c:if test="${registroSir.estado != 'ACEPTADO'}">
                                    <th><spring:message code="anexo.sir.validezDocumento"/></th>
                                    <th><spring:message code="anexo.origen"/></th>
                                    <th><spring:message code="anexo.tipoDocumental"/></th>
                                </c:if>
                                <th><spring:message code="regweb.acciones"/></th>
                            </tr>
                            </thead>

                            <tbody>

                            <c:forEach var="anexo" items="${registroSir.anexos}" varStatus="status">
                                <tr id="anexo${anexo.id}">
                                    <td>
                                        <c:if test="${anexo.nombreFichero != anexo.nombreFicheroCorto}">
                                            <p rel="valorPropiedad" data-content="${anexo.nombreFichero}" data-toggle="popover">${anexo.nombreFicheroCorto}</p>
                                        </c:if>
                                        <c:if test="${anexo.nombreFichero == anexo.nombreFicheroCorto}">
                                            ${anexo.nombreFichero}
                                        </c:if>
                                    </td>
                                    <td><spring:message code="tipoDocumento.${anexo.tipoDocumento}"/></td>
                                    <td>${anexo.tamano} KB</td>


                                        <%-- Gestionamos los campos NTI que no vienen informados por SICRES.
                                             Si el anexo es "FICHERO INTERNO" se deshabilitan los selects de los campos NTI
                                             Es el caso 4: DOCUMENTO CON FIRMA DETACHED y los documentos que son firmas
                                             se marcan como FICHERO INTERNO y los campos NTI solo se aplican al documento que no es la firma
                                        --%>
                                    <c:if test="${registroSir.estado != 'ACEPTADO'}">
                                        <c:if test="${empty anexo.validezDocumento}">
                                            <td>
                                                    <%--Si s'ha de posar valor per validez Documento--%>
                                                <select id="camposNTIs[${status.index}].idValidezDocumento"
                                                        name="camposNTIs[${status.index}].idValidezDocumento"
                                                        class="chosen-select"
                                                        <c:if test="${anexo.tipoDocumento == RegwebConstantes.CODIGO_SICRES_BY_TIPO_DOCUMENTO[RegwebConstantes.TIPO_DOCUMENTO_FICHERO_TECNICO] || registroSir.estado == 'ACEPTADO'}">disabled</c:if> >
                                                        <c:forEach items="${tiposValidezDocumento}" var="validezDocumento">
                                                            <option value="${validezDocumento}"><spring:message
                                                                    code="tipoValidezDocumento.${validezDocumento}"/></option>
                                                        </c:forEach>
                                                </select>
                                            </td>
                                        </c:if>

                                        <c:if test="${not empty anexo.validezDocumento}">
                                            <c:set var="validez" value="${anexo.validezDocumento}" scope="request"/>
                                            <td><spring:message
                                                    code="tipoValidezDocumento.${RegwebConstantes.TIPOVALIDEZDOCUMENTO_BY_CODIGO_SICRES[anexo.validezDocumento]}"/></td>
                                        </c:if>
                                        <td>
                                            <select id="camposNTIs[${status.index}].idOrigen"
                                                    name="camposNTIs[${status.index}].idOrigen" class="chosen-select"
                                                    <c:if test="${anexo.tipoDocumento == RegwebConstantes.CODIGO_SICRES_BY_TIPO_DOCUMENTO[RegwebConstantes.TIPO_DOCUMENTO_FICHERO_TECNICO] }">disabled</c:if>>
                                                    <option value="0"><spring:message
                                                            code="anexo.origen.ciudadano"/></option>
                                                    <option value="1" selected="selected"><spring:message
                                                            code="anexo.origen.administracion"/></option>
                                            </select>
                                        </td>
                                        <td>
                                            <select id="camposNTIs[${status.index}].idTipoDocumental"
                                                    name="camposNTIs[${status.index}].idTipoDocumental"
                                                    class="chosen-select"
                                                    <c:if test="${anexo.tipoDocumento == RegwebConstantes.CODIGO_SICRES_BY_TIPO_DOCUMENTO[RegwebConstantes.TIPO_DOCUMENTO_FICHERO_TECNICO] }">disabled</c:if>>
                                                    <option value="">...</option>
                                                    <c:forEach items="${tiposDocumentales}" var="tipoDocumental">
                                                        <option value="${tipoDocumental.codigoNTI}"><i:trad
                                                                value="${tipoDocumental}" property="nombre"/></option>
                                                    </c:forEach>
                                            </select>

                                        </td>
                                    </c:if>
                                    <input type="hidden"
                                           id="camposNTIs[${status.index}].id"
                                           name="camposNTIs[${status.index}].id"
                                           value="${anexo.id}"/>
                                    <td class="center"><a class="btn btn-success btn-default btn-sm"
                                                          href="<c:url value="/archivo/${anexo.anexo.id}"/>"
                                                          target="_blank"
                                                          title="<spring:message code="anexo.descargar"/>"><span
                                            class="fa fa-download"></span></a></td>
                                </tr>

                            </c:forEach>

                            </tbody>
                        </table>
                    </c:if>
                </div>
            </div>
        </div>
    </div>

</div>
