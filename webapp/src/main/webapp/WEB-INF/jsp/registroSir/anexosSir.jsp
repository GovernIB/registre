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
                        <table id="anexos" class="table table-bordered table-hover table-striped">
                            <thead>
                                <tr>
                                    <th><spring:message code="anexo.titulo"/></th>
                                    <c:if test="${registroSir.estado == 'RECIBIDO'}">
                                        <th><spring:message code="anexo.sir.validezDocumento"/></th>
                                        <th><spring:message code="anexo.origen"/></th>
                                        <th><spring:message code="anexo.tipoDocumental"/></th>
                                    </c:if>
                                    <th class="center" width="95"><spring:message code="regweb.acciones"/></th>
                                </tr>
                            </thead>

                            <tbody>

                            <c:forEach var="anexo" items="${anexosSirFull}" varStatus="status">

                                <tr id="anexo${anexo.documento.id}">
                                    <td class="ajustTamanySir">
                                        <a data-toggle="modal" href="#detalleAnexoSir${anexo.documento.id}">
                                            <c:if test="${anexo.documento.nombreFichero != anexo.documento.nombreFicheroCorto}">
                                                <p rel="popupAbajo" data-content="${anexo.documento.nombreFichero}" data-toggle="popover">${anexo.documento.nombreFicheroCorto}</p>
                                            </c:if>
                                            <c:if test="${anexo.documento.nombreFichero == anexo.documento.nombreFicheroCorto}">
                                                ${anexo.documento.nombreFichero}
                                            </c:if>
                                        </a>
                                    </td>

                                        <%-- Gestionamos los campos NTI que no vienen informados por SICRES.
                                             Si el anexo es "FICHERO INTERNO" se deshabilitan los selects de los campos NTI
                                             Es el caso 4: DOCUMENTO CON FIRMA DETACHED y los documentos que son firmas
                                             se marcan como FICHERO INTERNO y los campos NTI solo se aplican al documento que no es la firma
                                        --%>

                                    <c:if test="${registroSir.estado == 'RECIBIDO'}">
                                        <c:if test="${empty anexo.documento.validezDocumento}">
                                            <td class="ajustTamanySir">
                                                    <%--Si s'ha de posar valor per validez Documento--%>
                                                <select id="camposNTIs[${status.index}].idValidezDocumento" name="camposNTIs[${status.index}].idValidezDocumento" class="chosen-select"
                                                        <c:if test="${anexo.documento.tipoDocumento == RegwebConstantes.CODIGO_SICRES_BY_TIPO_DOCUMENTO[RegwebConstantes.TIPO_DOCUMENTO_FICHERO_TECNICO]}">disabled</c:if> >
                                                        <c:forEach items="${tiposValidezDocumento}" var="validezDocumento">
                                                            <option value="${validezDocumento}"><spring:message code="tipoValidezDocumento.${validezDocumento}"/></option>
                                                        </c:forEach>
                                                </select>
                                            </td>
                                        </c:if>

                                        <c:if test="${not empty anexo.documento.validezDocumento}">
                                            <c:set var="validez" value="${anexo.documento.validezDocumento}" scope="request"/>
                                            <td class="ajustTamanySir"><spring:message code="tipoValidezDocumento.${RegwebConstantes.TIPOVALIDEZDOCUMENTO_BY_CODIGO_SICRES[anexo.documento.validezDocumento]}"/></td>
                                        </c:if>

                                        <td class="ajustTamanySir">
                                            <select id="camposNTIs[${status.index}].idOrigen" name="camposNTIs[${status.index}].idOrigen" class="chosen-select"
                                                    <c:if test="${anexo.documento.tipoDocumento == RegwebConstantes.CODIGO_SICRES_BY_TIPO_DOCUMENTO[RegwebConstantes.TIPO_DOCUMENTO_FICHERO_TECNICO] }">disabled</c:if>>
                                                <option value="0"><spring:message code="anexo.origen.ciudadano"/></option>
                                                <option value="1" selected="selected"><spring:message code="anexo.origen.administracion"/></option>
                                            </select>
                                        </td>

                                        <td class="ajustTamanySir">
                                            <select id="camposNTIs[${status.index}].idTipoDocumental" name="camposNTIs[${status.index}].idTipoDocumental" class="chosen-select"
                                                    <c:if test="${anexo.documento.tipoDocumento == RegwebConstantes.CODIGO_SICRES_BY_TIPO_DOCUMENTO[RegwebConstantes.TIPO_DOCUMENTO_FICHERO_TECNICO] }">disabled</c:if>>
                                                <option value="">...</option>
                                                <c:forEach items="${tiposDocumentales}" var="tipoDocumental">
                                                    <option value="${tipoDocumental.codigoNTI}">${tipoDocumental.traducciones[pageContext.response.locale.language].nombre}</option>
                                                </c:forEach>
                                            </select>
                                        </td>
                                    </c:if>

                                   <%--BOTONERA ANEXO--%>

                                    <td class="center">
                                        <%--Anexos sin purgar--%>
                                        <c:if test="${not anexo.documento.purgado}">
                                            <%--Visor Anexo--%>
                                            <c:if test="${anexo.documento.tipoMIME == RegwebConstantes.MIME_PDF}">

                                                <a data-toggle="modal" class="btn btn-info btn-default btn-xs" href="#visorAnexo${anexo.documento.anexo.id}"
                                                   title="<spring:message code="anexo.visualizar"/>"><span class="fa fa-search"></span></a>

                                                <div id="visorAnexo${anexo.documento.anexo.id}" class="modal fade" role="dialog">
                                                    <div class="modal-dialog modal-lg">
                                                        <div class="modal-content">
                                                            <div class="modal-header">
                                                                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">x</button>
                                                                <h3 class="modal-title"><spring:message code="anexo.visualizar"/>: ${anexo.documento.nombreFichero}</h3>
                                                            </div>
                                                            <div class="modal-body">
                                                                <object type="${anexo.documento.tipoMIME}" data="<c:url value="/archivo/${anexo.documento.anexo.id}/false"/>" width="100%" height="700"></object>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>

                                            </c:if>
                                            <%--Descarga Anexo--%>
                                            <a class="btn btn-success btn-xs" href="<c:url value="/archivo/${anexo.documento.anexo.id}"/>" target="_blank" title="<spring:message code="anexo.descargar"/>"><span class="fa fa-download"></span></a>
                                            <%--Descarga Firma--%>
                                            <c:if test="${not empty anexo.firma}">
                                                <a class="btn btn-info btn-xs" href="<c:url value="/archivo/${anexo.firma.anexo.id}"/>" target="_blank" title="<spring:message code="anexo.tipofirma.detached"/>"><span class="fa fa-key"></span></a>
                                            </c:if>
                                        </c:if>

                                        <%--Anexos purgados--%>
                                        <c:if test="${anexo.documento.purgado}">
                                            <a href="javascript:void(0);" class="btn btn-success disabled btn-xs" title="<spring:message code="registroSir.anexo.eliminado"/>"><span class="fa fa-download"></span></a>
                                            <c:if test="${not empty anexo.firma}">
                                                <a href="javascript:void(0);" class="btn btn-info disabled btn-xs" title="<spring:message code="registroSir.anexo.eliminado"/>"><span class="fa fa-key"></span></a>
                                            </c:if>
                                        </c:if>

                                        <%--Información de la firma (sin firma o attached)--%>
                                        <c:if test="${empty anexo.firma && anexo.tieneFirma}">
                                            <span class="label label-success" rel="popupAbajo" data-content="<spring:message code="anexo.tipofirma.attached"/>" data-toggle="popover"><i class="fa fa-key"></i></span>
                                        </c:if>
                                        <c:if test="${empty anexo.firma && !anexo.tieneFirma}">
                                            <span class="label label-danger" rel="popupAbajo" data-content="<spring:message code="anexo.tipofirma.sinfirma"/>" data-toggle="popover"><i class="fa fa-key"></i></span>
                                        </c:if>
                                    </td>

                                    <input type="hidden" id="camposNTIs[${status.index}].id" name="camposNTIs[${status.index}].id" value="${anexo.documento.id}"/>
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

<%--Detalle de cada anexo sir--%>
<c:forEach var="anexoSir" items="${anexosSirFull}" varStatus="status">

    <div id="detalleAnexoSir${anexoSir.documento.id}" class="modal fade">

        <div class="modal-dialog modal-lg" id="formularioAnexo">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">x</button>
                    <h3>${anexoSir.documento.nombreFichero}</h3>
                </div>

                <div class="modal-body">

                    <div class="form-group col-xs-6">
                        <div class="col-xs-5 pull-left etiqueta_regweb control-label">
                            <label><spring:message code="anexo.titulo"/></label>
                        </div>
                        <div class="col-xs-7">${anexoSir.documento.nombreFichero}</div>
                    </div>

                    <c:if test="${not empty anexoSir.documento.validezDocumento}">
                        <div class="form-group col-xs-6">
                            <div class="col-xs-5 pull-left etiqueta_regweb control-label">
                                <label ><spring:message code="anexo.validezDocumento"/></label>
                            </div>
                            <div class="col-xs-7"><spring:message code="tipoValidezDocumento.${RegwebConstantes.TIPOVALIDEZDOCUMENTO_BY_CODIGO_SICRES[anexoSir.documento.validezDocumento]}"/></div>
                        </div>
                    </c:if>

                    <div class="form-group col-xs-6">
                        <div class="col-xs-5 pull-left etiqueta_regweb control-label">
                            <label><spring:message code="anexo.tipoDocumento"/></label>
                        </div>
                        <div class="col-xs-7"><spring:message code="tipoDocumento.${anexoSir.documento.tipoDocumento}"/></div>
                    </div>

                    <div class="form-group col-xs-6">
                        <div class="col-xs-5 pull-left etiqueta_regweb control-label">
                            <label><spring:message code="anexo.tamano"/></label>
                        </div>
                        <div class="col-xs-7">${anexoSir.documento.tamano} KB</div>
                    </div>

                    <div class="form-group col-xs-6">
                        <div class="col-xs-5 pull-left etiqueta_regweb control-label">
                            <label><spring:message code="anexo.observaciones"/></label>
                        </div>
                        <div class="col-xs-7">${anexoSir.documento.observaciones}</div>
                    </div>

                    <div class="form-group col-xs-6">
                        <div class="col-xs-5 pull-left etiqueta_regweb control-label">
                            <label><spring:message code="anexo.mime"/></label>
                        </div>
                        <div class="col-xs-7">${anexoSir.documento.tipoMIME}</div>
                    </div>

                    <div class="form-group col-xs-6">
                        <div class="col-xs-5 pull-left etiqueta_regweb control-label">
                            <label><spring:message code="anexoSir.identificadorFichero"/></label>
                        </div>
                        <div class="col-xs-7">${anexoSir.documento.identificadorFichero}</div>
                    </div>

                    <div class="clearfix"></div>

                </div>
                <div class="modal-footer">
                    <button class="btn btn-sm" data-dismiss="modal" aria-hidden="true">
                        <spring:message code="regweb.cerrar"/></button>
                </div>

            </div>
        </div>
    </div>


</c:forEach>
