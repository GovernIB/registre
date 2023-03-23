<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

<!DOCTYPE html>
<html lang="ca">
<head>
    <title><spring:message code="registroEntrada.buscador"/></title>
    <c:import url="../modulos/imports.jsp"/>
    <script type="text/javascript" src="<c:url value="/js/busquedaorganismo.js"/>"></script>
</head>

<body>

<c:import url="../modulos/menu.jsp"/>

<div class="row-fluid container main">

    <div class="well well-white">

        <div class="row">
            <div class="col-xs-12">
                <ol class="breadcrumb">
                    <c:import url="../modulos/migadepan.jsp">
                        <c:param name="avisos" value="${loginInfo.mostrarAvisos}"/> <%--Importamos el menú de avisos--%>
                    </c:import>
                    <li class="active"><i class="fa fa-list-ul"></i> <strong><spring:message code="registroEntrada.buscador"/></strong></li>
                </ol>
            </div>
        </div><!-- /.row -->

        <c:import url="../modulos/mensajes.jsp"/>

        <!-- BUSCADOR -->

        <div class="row">

            <div class="col-xs-12">

                <div class="panel panel-info">

                    <div class="panel-heading">
                        <a class="btn btn-info btn-xs pull-right" href="<c:url value="/registroEntrada/new"/>" role="button"><span class="fa fa-plus"></span> <spring:message code="registroEntrada.nuevo"/></a>
                        <h3 class="panel-title">
                        	<i class="fa fa-search"></i><strong>&nbsp;
                        	<spring:message code="registroEntrada.buscador"/></strong>
                        </h3>
                    </div>

                    <c:url value="/registroEntrada/busqueda" var="urlBusqueda" scope="request"/>
                    <!--  con esta opcion tambien funciona  pero depende de  javascript onsubmit="document.charset = 'ISO-8859-1'"-->
                     <form:form modelAttribute="registroEntradaBusqueda" action="${urlBusqueda}"  method="get" cssClass="form-horizontal">

                        <form:hidden path="pageNumber"/>

                        <div class="panel-body">
                        
                        <div class="col-xs-12">

                            <div class="col-xs-6 espaiLinies">
                                <div class="col-xs-4 pull-left etiqueta_regweb">
                                    <label for="idOrganismo" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.organismo.busqueda"/>" data-toggle="popover"><spring:message code="organismo.organismo"/></label>
                                </div>
                                <div class="col-xs-8">
                                    <form:select path="idOrganismo" cssClass="chosen-select" onchange="actualizarOficinas()">
                                        <form:option value="" label="..."/>
                                        <c:forEach var="organismo" items="${organismosConsultaEntrada}">
                                            <form:option value="${organismo.id}">${organismo.denominacion}</form:option>
                                        </c:forEach>
                                    </form:select>
                                </div>
                            </div>

                            <div class="col-xs-6 espaiLinies">
                                <div class="col-xs-4 pull-left etiqueta_regweb">
                                    <label for="registroEntrada.estado" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.estado.busqueda"/>" data-toggle="popover"><spring:message code="registroEntrada.estado"/></label>
                                </div>
                                <div class="col-xs-8">
                                    <form:select path="registroEntrada.estado" cssClass="chosen-select">
                                        <form:option value="" label="..."/>
                                        <c:forEach var="estado" items="${estados}">
                                            <form:option value="${estado}"><spring:message code="registro.estado.${estado}"/></form:option>
                                        </c:forEach>
                                    </form:select>
                                </div>
                            </div>
                        </div>

                        <div class="col-xs-12">
                            <div class="col-xs-6 espaiLinies">
                                <div class="col-xs-4 pull-left etiqueta_regweb">
                                    <label for="registroEntrada.oficina.id" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.oficina.busqueda"/>" data-toggle="popover" disabled="true"><spring:message code="registro.oficinaRegistro"/></label>
                                </div>
                                <div class="col-xs-8">
                                    <form:select path="registroEntrada.oficina.id" cssClass="chosen-select" disabled="true"/>
                                </div>
                            </div>

                            <div class="col-xs-6 espaiLinies">
                                <div class="col-xs-4 pull-left etiqueta_regweb">
                                    <label for="organDestinatari" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.destino.busqueda"/>" data-toggle="popover"><spring:message code="registroEntrada.organDestinatari"/></label>
                                </div>
                                <div class="col-xs-6">
                                    <form:select path="organDestinatari" cssClass="chosen-select">
                                        <form:option value="" label="..."/>
                                        <c:forEach items="${organosDestino}" var="organismo">
                                            <option value="${organismo.codigo}" <c:if test="${registroEntradaBusqueda.organDestinatari == organismo.codigo}">selected="selected"</c:if>>${organismo.denominacion}</option>
                                        </c:forEach>
                                    </form:select>
                                    <form:errors path="organDestinatari" cssClass="help-block" element="span"/>
                                    <form:hidden path="organDestinatariNom"/>
                                </div>
                                <div class="col-xs-2 boto-mesOpcions">
                                    <a data-toggle="modal" role="button" href="#modalBuscadorlistaRegEntrada"
                                       onclick="inicializarBuscador('#codNivelAdministracionlistaRegEntrada','#codComunidadAutonomalistaRegEntrada','#provincialistaRegEntrada','#localidadlistaRegEntrada',${RegwebConstantes.nivelAdminAutonomica}, ${RegwebConstantes.comunidadBaleares}, 'listaRegEntrada' );"
                                       class="btn btn-info btn-sm"><spring:message code="regweb.buscar"/></a>
                                </div>
                            </div>
                            
						</div>
						<div class="col-xs-12">
                            
                            <div class="col-xs-6 espaiLinies">
                                <div class="col-xs-4 pull-left etiqueta_regweb">
                                    <label for="registroEntrada.numeroRegistroFormateado" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.numero.busqueda"/>" data-toggle="popover"><spring:message code="registroEntrada.numeroRegistro"/></label>
                                </div>
                                <div class="col-xs-8">
                                    <form:input path="registroEntrada.numeroRegistroFormateado" cssClass="form-control"/> <form:errors path="registroEntrada.numeroRegistroFormateado" cssClass="help-block" element="span"/>
                                </div>
                            </div>
                            <div class="col-xs-6 espaiLinies">
                                <div class="col-xs-4 pull-left etiqueta_regweb">
                                    <label for="registroEntrada.registroDetalle.extracto" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.extracto.busqueda"/>" data-toggle="popover"><spring:message code="registroEntrada.extracto"/></label>
                                </div>
                                <div class="col-xs-8">
                                    <form:input path="registroEntrada.registroDetalle.extracto" cssClass="form-control" maxlength="200" /> <form:errors path="registroEntrada.registroDetalle.extracto" cssClass="help-block" element="span"/>
                                </div>
                            </div>
                            
						</div>
						<div class="col-xs-12">
                            
                            <div class="col-xs-6 espaiLinies">
                                <div class="col-xs-4 pull-left etiqueta_regweb">
                                    <label for="fechaInicio" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.inicio.busqueda"/>" data-toggle="popover"><span class="text-danger">*</span> <spring:message code="informe.fechaInicio"/></label>
                                </div>
                                <div class="col-xs-8" id="fechaInicio">
                                    <div class="input-group date no-pad-right">
                                        <form:input path="fechaInicio" type="text" cssClass="form-control"  maxlength="10" placeholder="dd/mm/yyyy" name="fechaInicio"/>
                                        <span class="input-group-addon"><span class="fa fa-calendar"></span></span>
                                    </div>
                                    <form:errors path="fechaInicio" cssClass="help-block" element="span"/>

                                </div>
                            </div>
                            <div class="col-xs-6 espaiLinies">
                                <div class="col-xs-4 pull-left etiqueta_regweb">
                                    <label for="fechaFin" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.fin.busqueda"/>" data-toggle="popover"><span class="text-danger">*</span> <spring:message code="informe.fechaFin"/></label>
                                </div>
                                <div class="col-xs-8" id="fechaFin">
                                    <div class="input-group date no-pad-right">
                                        <form:input type="text" cssClass="form-control" path="fechaFin" maxlength="10" placeholder="dd/mm/yyyy" name="fechaFin"/>
                                        <span class="input-group-addon"><span class="fa fa-calendar"></span></span>
                                    </div>
                                    <form:errors path="fechaFin" cssClass="help-block" element="span"/>

                                </div>
                            </div>
                            
                         </div>

                        <%--Comprueba si debe mostrar las opciones desplegadas o no--%>
                        <c:if test="${empty registroEntradaBusqueda.registroEntrada.registroDetalle.tipoDocumentacionFisica &&
                            empty registroEntradaBusqueda.interessatDoc && empty registroEntradaBusqueda.interessatNom &&
                            empty registroEntradaBusqueda.interessatLli1 && empty registroEntradaBusqueda.interessatLli2 &&
                            empty registroEntradaBusqueda.organDestinatari && empty registroEntradaBusqueda.registroEntrada.registroDetalle.codigoSia &&
                            empty registroEntradaBusqueda.idUsuario && !registroEntradaBusqueda.registroEntrada.registroDetalle.presencial}">
                            <div id="demo" class="collapse">
                        </c:if>
                        <c:if test="${not empty registroEntradaBusqueda.registroEntrada.registroDetalle.tipoDocumentacionFisica ||
                            not empty registroEntradaBusqueda.interessatDoc || not empty registroEntradaBusqueda.interessatNom ||
                            not empty registroEntradaBusqueda.interessatLli1 || not empty registroEntradaBusqueda.interessatLli2 ||
                            not empty registroEntradaBusqueda.organDestinatari || not empty registroEntradaBusqueda.registroEntrada.registroDetalle.codigoSia ||
                            not empty registroEntradaBusqueda.idUsuario || registroEntradaBusqueda.registroEntrada.registroDetalle.presencial}">
                            <div id="demo" class="collapse in">
                        </c:if>

                            <div class="col-xs-12">
                                <div class="col-xs-6 espaiLinies">
                                    <div class="col-xs-4 pull-left etiqueta_regweb">
                                        <label for="interessatNom" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.nombre.busqueda"/>" data-toggle="popover"><spring:message code="registroEntrada.nombreInteresado"/></label>
                                    </div>
                                    <div class="col-xs-8">
                                        <form:input  path="interessatNom" cssClass="form-control" maxlength="255"/>
                                        <form:errors path="interessatNom" cssClass="help-block" element="span"/>
                                    </div>
                                </div>
                                <div class="col-xs-6 espaiLinies">
                                    <div class="col-xs-4 pull-left etiqueta_regweb">
                                        <label for="interessatLli1" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.apellido1.busqueda"/>" data-toggle="popover"><spring:message code="interesado.apellido1"/></label>
                                    </div>
                                    <div class="col-xs-8">
                                        <form:input path="interessatLli1" cssClass="form-control" maxlength="255"/>
                                        <form:errors path="interessatLli1" cssClass="help-block" element="span"/>
                                    </div>
                                </div>
                            </div>

                            <div class="col-xs-12">
                                <div class="col-xs-6 espaiLinies">
                                    <div class="col-xs-4 pull-left etiqueta_regweb">
                                        <label for="interessatLli2" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.apellido2.busqueda"/>" data-toggle="popover"><spring:message code="interesado.apellido2"/></label>
                                    </div>
                                    <div class="col-xs-8">
                                        <form:input path="interessatLli2" cssClass="form-control" maxlength="255"/>
                                        <form:errors path="interessatLli2" cssClass="help-block" element="span"/>
                                    </div>
                                </div>
                                <div class="col-xs-6 espaiLinies">
                                    <div class="col-xs-4 pull-left etiqueta_regweb">
                                        <label for="interessatDoc" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.documento.busqueda"/>" data-toggle="popover"><spring:message code="registroEntrada.docInteresado"/></label>
                                    </div>
                                    <div class="col-xs-8">
                                        <form:input  path="interessatDoc" cssClass="form-control" maxlength="17"/>
                                        <form:errors path="interessatDoc" cssClass="help-block" element="span"/>
                                    </div>
                                </div>
                            </div>

                            <div class="col-xs-12">
                                <div class="col-xs-6 espaiLinies">
                                    <div class="col-xs-4 pull-left etiqueta_regweb">
                                        <label for="registroEntrada.registroDetalle.tipoDocumentacionFisica" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.docFisica"/>" data-toggle="popover"><spring:message code="registroEntrada.tipoDocumentacionFisica"/></label>
                                    </div>
                                    <div class="col-xs-8">
                                        <form:select path="registroEntrada.registroDetalle.tipoDocumentacionFisica" cssClass="chosen-select">
                                            <form:option value="" label="..."/>
                                            <c:forEach var="tipoDocumentacion" items="${tiposDocumentacionFisica}">
                                                <form:option value="${tipoDocumentacion}"><spring:message code="tipoDocumentacionFisica.${tipoDocumentacion}"/></form:option>
                                            </c:forEach>
                                        </form:select>
                                    </div>
                                </div>
                                <%--<div class="col-xs-6 espaiLinies">
                                    <div class="col-xs-4 pull-left etiqueta_regweb">
                                        <label for="observaciones" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.observaciones.busqueda"/>" data-toggle="popover"><spring:message code="registroEntrada.observaciones"/></label>
                                    </div>
                                    <div class="col-xs-8">
                                        <form:input path="observaciones" class="form-control" type="text" value=""/>
                                    </div>
                                </div>--%>
                                <div class="col-xs-6 espaiLinies">
                                    <div class="col-xs-4 pull-left etiqueta_regweb">
                                        <label for="idUsuario" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.usuario.busqueda"/>" data-toggle="popover"><spring:message code="usuario.usuario"/></label>
                                    </div>
                                    <div class="col-xs-8">
                                        <form:select path="idUsuario" class="chosen-select">
                                            <form:option value="">...</form:option>
                                            <c:forEach items="${usuariosEntidad}" var="usuarioEntidad">
                                                <option value="${usuarioEntidad.id}" <c:if test="${registroEntradaBusqueda.idUsuario == usuarioEntidad.id}">selected="selected"</c:if>>${usuarioEntidad.usuario.identificador}</option>
                                            </c:forEach>
                                        </form:select>
                                    </div>
                                </div>
                            </div>

                            <div class="col-xs-12">
                                <div class="col-xs-6 espaiLinies">
                                    <div class="col-xs-4 pull-left etiqueta_regweb">
                                        <label for="registroEntrada.registroDetalle.presencial" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.presencial"/>" data-toggle="popover"><spring:message code="registro.presencial"/></label>
                                    </div>
                                    <div class="col-xs-8">
                                        <form:select path="registroEntrada.registroDetalle.presencial" cssClass="chosen-select">
                                            <form:option value="" label="..."/>
                                            <form:option value="true"><spring:message code="regweb.si"/></form:option>
                                            <form:option value="false"><spring:message code="regweb.no"/></form:option>
                                        </form:select>
                                    </div>
                                </div>
                                <div class="col-xs-6 espaiLinies">
                                    <div class="col-xs-4 pull-left etiqueta_regweb">
                                        <label for="registroEntrada.registroDetalle.codigoSia" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.codigoSIA"/>" data-toggle="popover"><spring:message code="registroEntrada.codigoSIA"/></label>
                                    </div>
                                    <div class="col-xs-8">
                                        <form:input path="registroEntrada.registroDetalle.codigoSia" cssClass="form-control"/> <form:errors path="registroEntrada.registroDetalle.codigoSia" cssClass="help-block" element="span"/>
                                    </div>
                                </div>
                            </div>

                        </div>
                        <div class="col-xs-12 pad-bottom15 mesOpcions">
                            <a class="btn btn-info btn-xs pull-right masOpciones-info" data-toggle="collapse" data-target="#demo">
                                <%--Comprueba si debe mostrar mas opciones o menos--%>
                                <c:if test="${empty registroEntradaBusqueda.interessatDoc && empty registroEntradaBusqueda.interessatNom
                                && empty registroEntradaBusqueda.organDestinatari && empty registroEntradaBusqueda.observaciones && empty registroEntradaBusqueda.idUsuario &&
                                !registroEntradaBusqueda.registroEntrada.registroDetalle.presencial}">
                                    <span class="fa fa-plus"></span> <spring:message code="regweb.busquedaAvanzada"/>
                                </c:if>
                                <c:if test="${not empty registroEntradaBusqueda.interessatDoc || not empty registroEntradaBusqueda.interessatNom
                                || not empty registroEntradaBusqueda.organDestinatari || not empty registroEntradaBusqueda.observaciones || not empty registroEntradaBusqueda.idUsuario ||
                                registroEntradaBusqueda.registroEntrada.registroDetalle.presencial}">
                                    <span class="fa fa-minus"></span> <spring:message code="regweb.busquedaAvanzada"/>
                                </c:if>
                            </a>
                        </div>


					 	<div class="row">

                            <div class="form-group col-xs-12">
                                <div class="col-xs-1 boto-panel center">
                                    <button type="submit" class="btn btn-warning btn-sm" style="margin-left: 15px;">
                                        <spring:message code="regweb.buscar"/>
                                    </button>
                                </div>
                            </div>

						</div>

                    </form:form>

                            <c:if test="${paginacion != null}">

                                <div class="form-group col-xs-12">

                                        <c:if test="${empty paginacion.listado}">
                                            <div class="alert alert-grey alert-dismissable">
                                                <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                                                <spring:message code="regweb.busqueda.vacio"/> <strong><spring:message code="registroEntrada.registroEntrada"/></strong>
                                            </div>
                                        </c:if>

                                        <c:if test="${not empty paginacion.listado}">

                                            <div class="alert-grey">
                                                <c:if test="${paginacion.totalResults == 1}">
                                                    <spring:message code="regweb.resultado"/> <strong>${paginacion.totalResults}</strong> <spring:message code="registroEntrada.registroEntrada"/>
                                                </c:if>
                                                <c:if test="${paginacion.totalResults > 1}">
                                                    <spring:message code="regweb.resultados"/> <strong>${paginacion.totalResults}</strong> <spring:message code="registroEntrada.registroEntradas"/>
                                                </c:if>

                                                <%--Select de "Ir a página"--%>
                                                <c:import url="../modulos/paginas.jsp"/>
                                            </div>


                                        <div class="table-responsive">

                                                <table class="table table-bordered table-hover table-striped tablesorter">
                                                    <colgroup>
                                                        <col width="80">
                                                        <col>
                                                        <col width="80">
                                                        <col>
                                                        <col>
                                                        <col>
                                                        <col>
                                                        <col>
                                                        <col>
                                                        <col width="125">
                                                    </colgroup>
                                                    <thead>
                                                        <tr>
                                                            <th class="center"><spring:message code="regweb.numero"/></th>
                                                            <th class="center"><spring:message code="registroEntrada.fecha"/></th>
                                                            <th class="center"><spring:message code="registroEntrada.usuario"/></th>
                                                            <th class="center"><spring:message code="registroEntrada.oficina"/></th>
                                                            <th class="center"><spring:message code="organismo.destino.corto"/></th>
                                                            <c:if test="${registroEntradaBusqueda.registroEntrada.estado == 2}">
                                                                <th class="center"><spring:message code="registroEntrada.reserva"/></th>
                                                            </c:if>
                                                            <c:if test="${registroEntradaBusqueda.registroEntrada.estado != 2}">
                                                                <th class="center"><spring:message code="registroEntrada.extracto"/></th>
                                                            </c:if>
                                                            <th class="center"><spring:message code="registroEntrada.estado"/></th>
                                                            <th class="center">Doc.</th>
                                                            <th class="center"><spring:message code="registro.presencial"/></th>
                                                            <th class="center"><spring:message code="regweb.acciones"/></th>
                                                        </tr>
                                                    </thead>

                                                    <tbody>
                                                        <c:forEach var="registro" items="${paginacion.listado}" varStatus="status">
                                                            <tr>
                                                                <td>${registro.numeroRegistroFormateado}</td>
                                                                <td class="center"><fmt:formatDate value="${registro.fecha}" pattern="dd/MM/yyyy"/></td>
                                                                <td class="center">${registro.usuario.usuario.identificador}</td>
                                                                <td class="center"><label class="no-bold" rel="popupAbajo" data-content="${registro.oficina.denominacion}" data-toggle="popover">${registro.oficina.codigo}</label></td>
                                                                <td>${(empty registro.destino)? registro.destinoExternoDenominacion : registro.destino.denominacion}</td>
                                                                <c:if test="${registro.estado == RegwebConstantes.REGISTRO_RESERVA}">
                                                                <td>
                                                                    <c:if test="${fn:length(registro.registroDetalle.reserva) <= 40}">
                                                                        ${registro.registroDetalle.reserva}
                                                                    </c:if>
                                                                    <c:if test="${fn:length(registro.registroDetalle.reserva) > 40}">
                                                                        <p rel="popupArriba" data-content="${registro.registroDetalle.reserva}" data-toggle="popover">${registro.registroDetalle.reservaCorto}</p>
                                                                    </c:if>
                                                                </td>
                                                                </c:if>
                                                                <c:if test="${registro.estado != RegwebConstantes.REGISTRO_RESERVA}">
                                                                    <td>
                                                                        <c:if test="${fn:length(registro.registroDetalle.extracto) <= 40}">
                                                                            <c:out value="${registro.registroDetalle.extracto}" escapeXml="true"/>
                                                                        </c:if>
                                                                        <c:if test="${fn:length(registro.registroDetalle.extracto) > 40}">
                                                                            <p rel="popupArriba" data-content="<c:out value="${registro.registroDetalle.extracto}" escapeXml="true"/>" data-toggle="popover"><c:out value="${registro.registroDetalle.extractoCorto}" escapeXml="true"/></p>
                                                                        </c:if>
                                                                    </td>
                                                                </c:if>
                                                                <td class="center">
                                                                    <c:import url="../registro/estadosRegistro.jsp">
                                                                        <c:param name="estado" value="${registro.estado}"/>
                                                                        <c:param name="decodificacionTipoAnotacion" value="${registro.registroDetalle.decodificacionTipoAnotacion}"/>
                                                                    </c:import>
                                                                </td>
                                                                <td class="center">
                                                                    <c:if test="${registro.registroDetalle.tipoDocumentacionFisica == RegwebConstantes.TIPO_DOCFISICA_NO_ACOMPANYA_DOC}">
                                                                        <i class="fa fa-print text-verd" title="<spring:message code="tipoDocumentacionFisica.${registro.registroDetalle.tipoDocumentacionFisica}"/>"></i>
                                                                    </c:if>
                                                                    <c:if test="${registro.registroDetalle.tipoDocumentacionFisica == RegwebConstantes.TIPO_DOCFISICA_ACOMPANYA_DOC_REQUERIDA}">
                                                                        <i class="fa fa-file-text text-vermell" title="<spring:message code="tipoDocumentacionFisica.${registro.registroDetalle.tipoDocumentacionFisica}"/>"></i>
                                                                    </c:if>
                                                                    <c:if test="${registro.registroDetalle.tipoDocumentacionFisica == RegwebConstantes.TIPO_DOCFISICA_ACOMPANYA_DOC_COMPLEMENTARIA}">
                                                                        <i class="fa fa-clipboard text-taronja" title="<spring:message code="tipoDocumentacionFisica.${registro.registroDetalle.tipoDocumentacionFisica}"/>"></i>
                                                                    </c:if>
                                                                </td>
                                                                <td class="center">
                                                                    <c:if test="${registro.registroDetalle.presencial}">
                                                                        <span class="label label-success"><spring:message code="regweb.si"/></span>
                                                                    </c:if>
                                                                    <c:if test="${not registro.registroDetalle.presencial}">
                                                                        <span class="label label-danger"><spring:message code="regweb.no"/></span>
                                                                    </c:if>
                                                                </td>
                                                                <td class="center">
                                                                    <%--Botón detalle--%>
                                                                    <a class="btn btn-info btn-sm" href="<c:url value="/registroEntrada/${registro.id}/detalle"/>" title="<spring:message code="registroEntrada.detalle"/>"><span class="fa fa-eye"></span></a>

                                                                    <%--Botón editar--%>
                                                                    <c:if test="${(registro.estado == RegwebConstantes.REGISTRO_VALIDO || registro.estado == RegwebConstantes.REGISTRO_RESERVA) && puedeEditar}">
                                                                        <a class="btn btn-warning btn-sm" href="<c:url value="/registroEntrada/${registro.id}/edit"/>" title="<spring:message code="regweb.editar"/>"><span class="fa fa-pencil"></span></a>
                                                                    </c:if>

                                                                    <%--Botón anular--%>
                                                                    <c:if test="${(registro.estado == RegwebConstantes.REGISTRO_VALIDO || registro.estado == RegwebConstantes.REGISTRO_RESERVA || registro.estado == RegwebConstantes.REGISTRO_PENDIENTE_VISAR) && puedeEditar}">
                                                                        <a data-toggle="modal" role="button" href="#anularModal" onclick="limpiarModalAnulacion(${registro.id});" class="btn btn-danger btn-sm"><span class="fa fa-thumbs-o-down"></span></a>
                                                                    </c:if>

                                                                    <%--Botón activar--%>
                                                                    <c:if test="${registro.estado == RegwebConstantes.REGISTRO_ANULADO && puedeEditar}">  <%--Anulado--%>
                                                                        <a class="btn btn-primary btn-sm" onclick='javascript:confirm("<c:url value="/registroEntrada/${registro.id}/activar"/>","<spring:message code="regweb.confirmar.activar" htmlEscape="true"/>")' href="javascript:void(0);" title="<spring:message code="regweb.activar"/>"><span class="fa fa-thumbs-o-up"></span></a>
                                                                    </c:if>

                                                                </td>
                                                            </tr>
                                                        </c:forEach>
                                                    </tbody>
                                                </table>


                                            <!-- Paginacion -->
                                            <c:import url="../modulos/paginacionBusqueda.jsp">
                                                <c:param name="entidad" value="registroEntrada"/>
                                            </c:import>

                                    </div>

                                    </c:if>

                                </div>

                            </c:if>


                        </div>
                </div>
            </div>
        </div>

        <!-- FIN BUSCADOR -->

        <!-- Importamos el codigo jsp del modal del formulario para realizar la búsqueda de organismos Destino
             Mediante el archivo "busquedaorganismo.js" se implementa dicha búsqueda -->
        <c:import url="../registro/buscadorOrganismosOficinasREPestanas.jsp">
            <c:param name="tipo" value="listaRegEntrada"/>
        </c:import>

        <%--Modal ANULAR--%>
        <c:import url="../registro/anular.jsp">
            <c:param name="tipoRegistro" value="${RegwebConstantes.REGISTRO_ENTRADA}"/>
        </c:import>


    </div>
</div> <!-- /container -->

<c:import url="../modulos/pie.jsp"/>

<!-- Cambia la imagen de la búsqueda avanzada-->
<script type="text/javascript">

    // Gestión de los cambios de Oficina
    $(window).load(function() {
        actualizarOficinas();
    });

    function actualizarOficinas(){
        <c:url var="obtenerOficinasEntrada" value="/rest/obtenerOficinasEntrada" />
        actualizarSelect('${obtenerOficinasEntrada}','#registroEntrada\\.oficina\\.id',$('#idOrganismo option:selected').val(),'${registroEntradaBusqueda.registroEntrada.oficina.id}',true,true);
    }

    // Posicionamos el ratón en el campo indicado al cargar el modal
    $('#modalBuscadorlistaRegEntrada').on('shown.bs.modal', function () {
        $('#denominacionlistaRegEntrada').focus();
    });

    var traduccion = new Array();
    traduccion['regweb.busquedaAvanzada'] = "<spring:message code='regweb.busquedaAvanzada' javaScriptEscape='true' />";

    $(function(){
        $("#demo").on("hide.bs.collapse", function(){
            $(".masOpciones-info").html('<span class="fa fa-plus"></span> ' + traduccion['regweb.busquedaAvanzada']);
        });
        $("#demo").on("show.bs.collapse", function(){
            $(".masOpciones-info").html('<span class="fa fa-minus"></span> ' + traduccion['regweb.busquedaAvanzada']);
        });
    });
</script>

<!-- Añade el color del libro activo o no activo -->
<script type="text/javascript">
    // Añade el color al libro al iniciar la pagina
    document.addEventListener("DOMContentLoaded", function() {
        $("#llibre_chosen").find("span").addClass($("#llibre option:selected").attr('class'));
    }, false);

    //Ejecuta cambio de colores al cambiar el libro del select
    $(document).on('change', '#llibre', function(event) {
        $("#llibre_chosen").find("span").removeClass("rojo");
        $("#llibre_chosen").find("span").addClass($("#llibre option:selected").attr('class'));
    });
</script>

</body>
</html>