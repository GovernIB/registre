<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

<div class="col-xs-12 pull-right">

    <div class="panel panel-primary">

        <div class="panel-heading">

            <h3 class="panel-title">
                <i class="fa fa-pencil-square-o"></i> <strong><spring:message code="anexo.anexos"/></strong>:
                <c:if test="${registroSir.documentacionFisica==RegwebConstantes.TIPO_DOCFISICA_ACOMPANYA_DOC_REQUERIDA}">
                    <span class="text-vermell text14"><spring:message code="tipoDocumentacionFisica.${registroSir.documentacionFisica}"/></span>
                </c:if>
                <c:if test="${registroSir.documentacionFisica==RegwebConstantes.TIPO_DOCFISICA_ACOMPANYA_DOC_COMPLEMENTARIA}">
                    <span class="text-taronja text14"><spring:message code="tipoDocumentacionFisica.${registroSir.documentacionFisica}"/></span>
                </c:if>
                <c:if test="${registroSir.documentacionFisica==RegwebConstantes.TIPO_DOCFISICA_NO_ACOMPANYA_DOC}">
                    <span class="text-verd text14"><spring:message code="tipoDocumentacionFisica.${registroSir.documentacionFisica}"/></span>
                </c:if>
            </h3>
        </div>

        <div class="panel-body senseMargesLaterals">
            <div class="col-xs-12 padLateral5">
                <div id="anexosdiv" class="table-responsive sin-scroll">

                    <c:if test="${empty anexosSirFull}">
                        <div class="alert alert-grey alert-dismissable">
                            <strong><spring:message code="regweb.listado.vacio"/> <spring:message code="anexo.anexo"/></strong>
                        </div>
                    </c:if>

                    <c:if test="${not empty anexosSirFull}">
                        <table id="anexos" class="table table-bordered table-hover table-striped tablePad2">
                            <colgroup>
                                <col>
                                <col>
                                <%----%>
                                <c:if test="${registroSir.estado == 'RECIBIDO_CONFIRMADO'}">
                                    <col>
                                    <col>
                                    <col>
                                </c:if>
                                 
                                <col>
                                <col>
                                <col>
                            </colgroup>
                            <thead>
                            <tr>
                                <th><spring:message code="anexo.titulo"/></th>
                                <th><spring:message code="anexo.sir.tipoDocumento"/></th>
                                <%--<th><spring:message code="anexo.tamano"/></th>--%>
                                
                                <c:if test="${registroSir.estado == 'RECIBIDO_CONFIRMADO'}">
                                    <th><spring:message code="anexo.sir.validezDocumento"/></th>
                                    <%--
                                    <th><spring:message code="anexo.origen"/></th>
                                    <th><spring:message code="anexo.tipoDocumental"/></th>
                                	--%>
                                </c:if>
                                <th class="center">Doc</th>
                                <th class="center">Firma</th>
                                <th></th>
                            </tr>
                            </thead>

                            <tbody>

                            <c:forEach var="anexo" items="${anexosSirFull}" varStatus="status">
                            	<c:url value="/registroSir/${registroSir.id}/${anexo.documento.id}/actualizar" var="urlAceptar" scope="request"/>
                    			<form:form modelAttribute="registrarForm" action="${urlAceptar}" method="post" cssClass="form-horizontal">
                                <tr id="anexo${anexo.documento.id}">
                                    <td class="ajustTamanySir">
                                        <c:if test="${anexo.documento.nombreFichero != anexo.documento.nombreFicheroCorto}">
                                            <p rel="popupAbajo" data-content="${anexo.documento.nombreFichero}" data-toggle="popover">${anexo.documento.nombreFicheroCorto}</p>
                                        </c:if>
                                        <c:if test="${anexo.documento.nombreFichero == anexo.documento.nombreFicheroCorto}">
                                            ${anexo.documento.nombreFichero}
                                        </c:if>
                                    </td>
                                    <td class="ajustTamanySir"><spring:message code="tipoDocumento.${anexo.documento.tipoDocumento}"/></td>
                                    <%--<td>${anexo.documento.tamano} KB</td>--%>

                                        <%-- Gestionamos los campos NTI que no vienen informados por SICRES.
                                             Si el anexo es "FICHERO INTERNO" se deshabilitan los selects de los campos NTI
                                             Es el caso 4: DOCUMENTO CON FIRMA DETACHED y los documentos que son firmas
                                             se marcan como FICHERO INTERNO y los campos NTI solo se aplican al documento que no es la firma
                                        --%>
									
                                    <c:if test="${registroSir.estado == 'RECIBIDO_CONFIRMADO'}">
                                        <c:if test="${empty anexo.documento.validezDocumento}">
                                            <td class="ajustTamanySir">
                                           
                                                    <%--Si s'ha de posar valor per validez Documento--%>
                                                    <%----%>
                                                <select id="camposNTIs[${status.index}].idValidezDocumento"
                                                        name="camposNTIs[${status.index}].idValidezDocumento"
                                                        class="chosen-select"
                                                        <c:if test="${anexo.documento.tipoDocumento == RegwebConstantes.CODIGO_SICRES_BY_TIPO_DOCUMENTO[RegwebConstantes.TIPO_DOCUMENTO_FICHERO_TECNICO]}">disabled</c:if> >
                                                        <c:forEach items="${tiposValidezDocumento}" var="validezDocumento">
                                                            <option value="${validezDocumento}"><spring:message
                                                                    code="tipoValidezDocumento.${validezDocumento}"/></option>
                                                        </c:forEach>
                                                </select>
                                            </td>
                                        </c:if>

                                        <c:if test="${not empty anexo.documento.validezDocumento}">
                                            <c:set var="validez" value="${anexo.documento.validezDocumento}" scope="request"/>
                                            <td class="ajustTamanySir"><spring:message code="tipoValidezDocumento.${RegwebConstantes.TIPOVALIDEZDOCUMENTO_BY_CODIGO_SICRES[anexo.documento.validezDocumento]}"/></td>
                                        </c:if>

										<%-- 
                                        <td class="ajustTamanySir">
                                            <select id="camposNTIs[${status.index}].idOrigen"
                                                    name="camposNTIs[${status.index}].idOrigen" class="chosen-select"
                                                    <c:if test="${anexo.documento.tipoDocumento == RegwebConstantes.CODIGO_SICRES_BY_TIPO_DOCUMENTO[RegwebConstantes.TIPO_DOCUMENTO_FICHERO_TECNICO] }">disabled</c:if>>
                                                <option value="0"><spring:message
                                                        code="anexo.origen.ciudadano"/></option>
                                                <option value="1" selected="selected"><spring:message
                                                        code="anexo.origen.administracion"/></option>
                                            </select>
                                        </td>

                                        <td class="ajustTamanySir">
                                            <select id="camposNTIs[${status.index}].idTipoDocumental"
                                                    name="camposNTIs[${status.index}].idTipoDocumental"
                                                    class="chosen-select"
                                                    <c:if test="${anexo.documento.tipoDocumento == RegwebConstantes.CODIGO_SICRES_BY_TIPO_DOCUMENTO[RegwebConstantes.TIPO_DOCUMENTO_FICHERO_TECNICO] }">disabled</c:if>>
                                                <option value="">...</option>
                                                <c:forEach items="${tiposDocumentales}" var="tipoDocumental">
                                                    <option value="${tipoDocumental.codigoNTI}"><i:trad
                                                            value="${tipoDocumental}" property="nombre"/></option>
                                                </c:forEach>
                                            </select>
                                        </td>
                                         --%>
                                    </c:if>
                                   
									<%-- --%>
                                    <c:if test="${not anexo.documento.purgado}">
                                        <td class="center ajustTamanySir">
                                            <a class="btn btn-success btn-sm"
                                                     href="<c:url value="/archivo/${anexo.documento.anexo.id}"/>"
                                                     target="_blank" title="<spring:message code="anexo.descargar"/>"><span
                                                    class="fa fa-download"></span></a>
                                        </td>
                                    </c:if>

                                    <c:if test="${anexo.documento.purgado}">
                                        <td class="center ajustTamanySir">
                                            <a href="javascript:void(0);" class="btn btn-success disabled btn-sm" title="<spring:message code="registroSir.anexo.eliminado"/>"><span class="fa fa-download"></span></a>
                                        </td>
                                    </c:if>

                                    <td class="center ajustTamanySir">
                                        <c:if test="${not empty anexo.firma}">

                                            <c:if test="${not anexo.documento.purgado}">
                                                <a class="btn btn-info btn-default btn-sm"
                                                   href="<c:url value="/archivo/${anexo.firma.anexo.id}"/>"
                                                   target="_blank"
                                                   title="<spring:message code="anexo.tipofirma.detached"/>"><span class="fa fa-download"></span></a>
                                            </c:if>

                                            <c:if test="${anexo.documento.purgado}">
                                                <a href="javascript:void(0);" class="btn btn-info disabled btn-sm" title="<spring:message code="registroSir.anexo.eliminado"/>"><span class="fa fa-download"></span></a>
                                            </c:if>


                                        </c:if>
                                        <c:if test="${empty anexo.firma && anexo.tieneFirma}">
                                            <p rel="popupAbajo" data-content="<spring:message code="anexo.tipofirma.attached"/>" data-toggle="popover"><span class="label label-success">Si</span></p>
                                        </c:if>
                                        <c:if test="${empty anexo.firma && !anexo.tieneFirma}">
                                            <span class="label label-danger">No</span>
                                        </c:if>
                                    </td>
                                    <%--
									<c:if test="${registroSir.estado == 'RECIBIDO_CONFIRMADO'}">
										<input type="submit" value="<spring:message code="registroSir.aceptar"/>" class="btn btn-success btn-sm">
									</c:if>
									--%>
                                    <input type="hidden"
                                           id="camposNTIs[${status.index}].id"
                                           name="camposNTIs[${status.index}].id"
                                           value="${anexo.documento.id}"/>
                                </tr>
								</form:form>
                            </c:forEach>

                            </tbody>
                        </table>
                    </c:if>
                </div>
            </div>
        </div>
    </div>

</div>
