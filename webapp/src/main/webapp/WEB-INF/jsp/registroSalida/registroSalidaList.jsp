<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

<!DOCTYPE html>
<html lang="ca">
<head>
    <title><spring:message code="registroSalida.buscador"/></title>
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
                    <li class="active"><i class="fa fa-list-ul"></i> <strong><spring:message code="registroSalida.buscador"/></strong></li>
                </ol>
            </div>
        </div><!-- /.row -->

        <c:import url="../modulos/mensajes.jsp"/>

        <!-- BUSCADOR -->

        <div class="row">

            <div class="col-xs-12">

            <div class="panel panel-danger">

            <div class="panel-heading">
                <a class="btn btn-danger btn-xs pull-right" href="<c:url value="/registroSalida/new"/>" role="button"><span class="fa fa-plus"></span> <spring:message code="registroSalida.nuevo"/></a>
                <h3 class="panel-title">
                	<i class="fa fa-search"></i>&nbsp;
                	<strong><spring:message code="registroSalida.buscador"/></strong>
                </h3>
            </div>

            <c:url value="/registroSalida/busqueda" var="urlBusqueda"/>
            <form:form modelAttribute="registroSalidaBusqueda" action="${urlBusqueda}" method="get" cssClass="form-horizontal">
            <form:hidden path="pageNumber"/>

                <div class="panel-body">

                    <div class="col-xs-12">

                        <div class="col-xs-6 espaiLinies">
                            <div class="col-xs-4 pull-left etiqueta_regweb">
                                <label for="registroSalida.libro.id" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.libro.busqueda"/>" data-toggle="popover"><span class="text-danger">*</span> <spring:message code="registroSalida.libro"/></label>
                            </div>
                            <div class="col-xs-8">
                                <form:select path="registroSalida.libro.id" items="${librosConsulta}" itemLabel="nombreCompleto" itemValue="id" cssClass="chosen-select"/>
                            </div>
                        </div>
                        <div class="col-xs-6 espaiLinies">
                            <div class="col-xs-4 pull-left etiqueta_regweb">
                                <label for="registroSalida.estado" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.estado.busqueda"/>" data-toggle="popover"><spring:message code="registroSalida.estado"/></label>
                            </div>
                            <div class="col-xs-8">
                                <form:select path="registroSalida.estado" cssClass="chosen-select">
                                    <form:option value="" label="..."/>
                                    <c:forEach var="estado" items="${estados}">
                                        <form:option value="${estado}"><spring:message code="registro.estado.${estado}"/></form:option>
                                    </c:forEach>
                                </form:select>

                                 <form:errors path="registroSalida.estado" cssClass="help-block" element="span"/>
                            </div>
                        </div>

                    </div>
                    <div class="col-xs-12">

                        <div class="col-xs-6 espaiLinies">
                            <div class="col-xs-4 pull-left etiqueta_regweb">
                                <label for="registroSalida.numeroRegistroFormateado" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.numero.busqueda"/>" data-toggle="popover"><spring:message code="registroEntrada.numeroRegistro"/></label>
                            </div>
                            <div class="col-xs-8">
                                <form:input path="registroSalida.numeroRegistroFormateado" cssClass="form-control"/> <form:errors path="registroSalida.numeroRegistroFormateado" cssClass="help-block" element="span"/>
                            </div>
                        </div>
                        <div class="col-xs-6 espaiLinies">
                            <div class="col-xs-4 pull-left etiqueta_regweb">
                                <label for="registroSalida.registroDetalle.extracto" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.extracto.busqueda"/>" data-toggle="popover"><spring:message code="registroEntrada.extracto"/></label>
                            </div>
                            <div class="col-xs-8">
                                <form:input path="registroSalida.registroDetalle.extracto" cssClass="form-control" maxlength="200"/> <form:errors path="registroSalida.registroDetalle.extracto" cssClass="help-block" element="span"/>
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

                    <c:if test="${empty registroSalidaBusqueda.registroSalida.oficina.id &&
                    empty registroSalidaBusqueda.interessatDoc && empty registroSalidaBusqueda.interessatNom &&
                    empty registroSalidaBusqueda.interessatLli1 && empty registroSalidaBusqueda.interessatLli2 &&
                    empty registroSalidaBusqueda.organOrigen && empty registroSalidaBusqueda.observaciones &&
                    empty registroSalidaBusqueda.usuario && !registroSalidaBusqueda.anexos}">
                        <div id="demo" class="collapse">
                    </c:if>
                    <c:if test="${not empty registroSalidaBusqueda.registroSalida.oficina.id ||
                    not empty registroSalidaBusqueda.interessatDoc || not empty registroSalidaBusqueda.interessatNom ||
                    not empty registroSalidaBusqueda.interessatLli1 || not empty registroSalidaBusqueda.interessatLli2 ||
                    not empty registroSalidaBusqueda.organOrigen || not empty registroSalidaBusqueda.observaciones ||
                    not empty registroSalidaBusqueda.usuario || registroSalidaBusqueda.anexos}">
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
                                    <label for="registroSalida.oficina.id" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.oficina.busqueda"/>" data-toggle="popover"><spring:message code="registro.oficinaRegistro"/></label>
                                </div>
                                <div class="col-xs-8">
                                    <form:select path="registroSalida.oficina.id" cssClass="chosen-select">
                                        <form:option value="" label="..."/>
                                        <c:forEach var="oficinaRegistro" items="${oficinasRegistro}">
                                            <form:option value="${oficinaRegistro.id}">${oficinaRegistro.denominacion}</form:option>
                                        </c:forEach>
                                    </form:select>
                                </div>
                            </div>
                            <div class="col-xs-6 espaiLinies">
                                <div class="col-xs-4 pull-left etiqueta_regweb">
                                    <label for="organOrigen" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.origen.busqueda"/>" data-toggle="popover"><spring:message code="registroEntrada.organOrigen"/></label>
                                </div>
                                <div class="col-xs-8">
                                    <form:select path="organOrigen" cssClass="chosen-select">
                                        <form:option value="" label="..."/>
                                        <c:forEach items="${organosOrigen}" var="organismo">
                                            <option value="${organismo.codigo}" <c:if test="${registroSalidaBusqueda.organOrigen == organismo.codigo}">selected="selected"</c:if>>${organismo.denominacion}</option>
                                        </c:forEach>
                                    </form:select>
                                    <form:errors path="organOrigen" cssClass="help-block" element="span"/>
                                    <form:hidden path="organOrigenNom"/>
                                </div>
                            </div>
                        </div>

                        <div class="col-xs-12">
                            <div class="col-xs-6 espaiLinies">
                                <div class="col-xs-4 pull-left etiqueta_regweb">
                                    <label for="observaciones" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.observaciones.busqueda"/>" data-toggle="popover"><spring:message code="registroEntrada.observaciones"/></label>
                                </div>
                                <div class="col-xs-8">
                                    <form:input path="observaciones" class="form-control" type="text" value=""/>
                                </div>
                            </div>
                            <div class="col-xs-6 espaiLinies">
                                <div class="col-xs-4 pull-left etiqueta_regweb">
                                    <label for="usuario" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.usuario.busqueda"/>" data-toggle="popover"><spring:message code="usuario.usuario"/></label>
                                </div>
                                <div class="col-xs-8">
                                    <form:select path="usuario" class="chosen-select">
                                        <form:option value="">...</form:option>
                                        <c:forEach items="${usuariosEntidad}" var="usuarioEntidad">
                                            <option value="${usuarioEntidad.usuario.identificador}" <c:if test="${registroSalidaBusqueda.usuario == usuarioEntidad.usuario.identificador}">selected="selected"</c:if>>${usuarioEntidad.usuario.identificador}</option>
                                        </c:forEach>
                                    </form:select>
                                </div>
                            </div>
                        </div>

                        <div class="col-xs-12" style="display:none;">
                            <div class="col-xs-6 espaiLinies">
                                <div class="col-xs-4 pull-left etiqueta_regweb">
                                    <label for="anexos" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.anexos.busqueda"/>" data-toggle="popover"><spring:message code="registroEntrada.anexos"/></label>
                                </div>
                                <div class="col-xs-8">
                                    <form:checkbox path="anexos"/>
                                </div>
                            </div>
                            <div class="form-group col-xs-6"><div class="col-xs-12">&nbsp;</div></div>
                        </div>

                    </div>

                <div class="col-xs-12 pad-bottom15 mesOpcions">
                    <a class="btn btn-danger btn-xs pull-right masOpciones-danger" data-toggle="collapse" data-target="#demo">
                        <%--Comprueba si debe mostrar mas opciones o menos--%>
                        <c:if test="${empty registroSalidaBusqueda.registroSalida.oficina.id && empty registroSalidaBusqueda.interessatDoc && empty registroSalidaBusqueda.interessatNom && empty registroSalidaBusqueda.organOrigen && empty registroSalidaBusqueda.observaciones && empty registroSalidaBusqueda.usuario && !registroSalidaBusqueda.anexos}">
                            <span class="fa fa-plus"></span> <spring:message code="regweb.busquedaAvanzada"/>
                        </c:if>
                        <c:if test="${not empty registroSalidaBusqueda.registroSalida.oficina.id || not empty registroSalidaBusqueda.interessatDoc || not empty registroSalidaBusqueda.interessatNom || not empty registroSalidaBusqueda.organOrigen || not empty registroSalidaBusqueda.observaciones || not empty registroSalidaBusqueda.usuario || registroSalidaBusqueda.anexos}">
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

                    <div class="row">
                        <div class="col-xs-12">

                            <c:if test="${empty paginacion.listado}">
                                <div class="alert alert-grey alert-dismissable">
                                    <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                                    <spring:message code="regweb.busqueda.vacio"/> <strong><spring:message code="registroSalida.registroSalida"/></strong>
                                </div>
                            </c:if>

                            <c:if test="${not empty paginacion.listado}">

                                <div class="alert-grey">
                                    <c:if test="${paginacion.totalResults == 1}">
                                        <spring:message code="regweb.resultado"/> <strong>${paginacion.totalResults}</strong> <spring:message code="registroSalida.registroSalida"/>
                                    </c:if>
                                    <c:if test="${paginacion.totalResults > 1}">
                                        <spring:message code="regweb.resultados"/> <strong>${paginacion.totalResults}</strong> <spring:message code="registroSalida.registroSalidas"/>
                                    </c:if>

                                    <p class="pull-right"><spring:message code="regweb.pagina"/> <strong>${paginacion.currentIndex}</strong> de ${paginacion.totalPages}</p>
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
                                        <th class="center"><spring:message code="registroSalida.fecha"/></th>
                                        <th class="center"><spring:message code="registroSalida.usuario"/></th>
                                        <th class="center"><spring:message code="registroSalida.oficina"/></th>
                                        <th class="center"><spring:message code="registroSalida.origen"/></th>
                                        <th class="center"><spring:message code="registroSalida.extracto"/></th>
                                        <th class="center"><spring:message code="registroSalida.estado"/></th>
                                        <th class="center"><spring:message code="registroSalida.destinatarios"/></th>
                                        <th class="center"><spring:message code="registroEntrada.anexos"/></th>
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
                                            <c:if test="${registro.origen != null}">
                                                <td>${registro.origen.denominacion}</td>
                                            </c:if>
                                            <c:if test="${registro.origen == null}">
                                                <td>${registro.origenExternoDenominacion}</td>
                                            </c:if>
                                            <td>
                                                <c:if test="${fn:length(registro.registroDetalle.extracto) <= 40}">
                                                    <c:out value="${registro.registroDetalle.extracto}" escapeXml="true"/>
                                                </c:if>
                                                <c:if test="${fn:length(registro.registroDetalle.extracto) > 40}">
                                                    <p rel="popupArriba" data-content="<c:out value="${registro.registroDetalle.extracto}" escapeXml="true"/>" data-toggle="popover"><c:out value="${registro.registroDetalle.extractoCorto}" escapeXml="true"/></p>
                                                </c:if>
                                            </td>
                                            <td class="center">
                                                <c:import url="../registro/estadosRegistro.jsp">
                                                    <c:param name="estado" value="${registro.estado}"/>
                                                    <c:param name="decodificacionTipoAnotacion" value="${registro.registroDetalle.decodificacionTipoAnotacion}"/>
                                                </c:import>
                                            </td>
                                            <c:if test="${registro.registroDetalle.interesados != null}">
                                                <td class="center"><label class="no-bold representante" rel="popupAbajo"
                                                                          data-content="<c:out value="${registro.registroDetalle.nombreInteresadosHtml}" escapeXml="true"/>"
                                                                          data-toggle="popover"><c:out value="${registro.registroDetalle.totalInteresados}" escapeXml="true"/></label>
                                                </td>
                                            </c:if>
                                            <c:if test="${registro.registroDetalle.interesados == null}">
                                                <td class="center">0</td>
                                            </c:if>
                                            <c:if test="${registro.registroDetalle.anexos != null}">
                                                <c:if test="${registro.registroDetalle.tieneJustificante}"><td class="center">${fn:length(registro.registroDetalle.anexos)-1}</td></c:if>
                                                <c:if test="${!registro.registroDetalle.tieneJustificante}"><td class="center">${fn:length(registro.registroDetalle.anexos)}</td></c:if>
                                            </c:if>
                                            <c:if test="${registro.registroDetalle.anexos == null}">
                                                <td class="center">0</td>
                                            </c:if>

                                            <td class="center">
                                                <a class="btn btn-info btn-sm" href="<c:url value="/registroSalida/${registro.id}/detalle"/>" title="<spring:message code="registroSalida.detalle"/>"><span class="fa fa-eye"></span></a>
                                                    <%--Acciones según el estado--%>
                                                    <%--Si no nos encontramos en la misma Oficia en la que se creó el Registro o en su Oficina Responsable, no podemos hacer nada con el--%>
                                                <c:if test="${registro.oficina.id == loginInfo.oficinaActiva.id || registro.oficina.oficinaResponsable.id == loginInfo.oficinaActiva.id}">

                                                    <%--Botón editar--%>
                                                    <c:if test="${registro.estado == RegwebConstantes.REGISTRO_VALIDO && puedeEditar && !registro.registroDetalle.tieneJustificante}">
                                                        <a class="btn btn-warning btn-sm" href="<c:url value="/registroSalida/${registro.id}/edit"/>" title="<spring:message code="regweb.editar"/>"><span class="fa fa-pencil"></span></a>
                                                    </c:if>

                                                    <%--Botón anular--%>
                                                    <c:if test="${(registro.estado == RegwebConstantes.REGISTRO_VALIDO || registro.estado == RegwebConstantes.REGISTRO_PENDIENTE_VISAR) && puedeEditar}">
                                                        <%--<a class="btn btn-danger btn-sm" href="javascript:void(0);" onclick='javascript:confirm("<c:url value="/registroSalida/${registro.id}/anular"/>","<spring:message code="regweb.confirmar.anular" htmlEscape="true"/>")' title="<spring:message code="regweb.anular"/>"><span class="fa fa-thumbs-o-down"></span></a>--%>
                                                        <a data-toggle="modal" role="button" href="#anularModal" onclick="limpiarModalAnulacion(${registro.id});" class="btn btn-danger btn-sm"><span class="fa fa-thumbs-o-down"></span></a>
                                                    </c:if>

                                                    <%--Botón activar--%>
                                                    <c:if test="${registro.estado == RegwebConstantes.REGISTRO_ANULADO && puedeEditar}">
                                                        <a class="btn btn-primary btn-sm" onclick='javascript:confirm("<c:url value="/registroSalida/${registro.id}/activar"/>","<spring:message code="regweb.confirmar.activar" htmlEscape="true"/>")' href="javascript:void(0);" title="<spring:message code="regweb.activar"/>"><span class="fa fa-thumbs-o-up"></span></a>
                                                    </c:if>

                                                </c:if>


                                            </td>
                                        </tr>
                                    </c:forEach>
                                    </tbody>
                                </table>


                                <!-- Paginacion -->
                                <c:import url="../modulos/paginacionBusqueda.jsp">
                                    <c:param name="entidad" value="registroSalida"/>
                                </c:import>

                            </div>
                        </div>

                        </c:if>

                    </div>

                </c:if>


            </div>
            </div>
            </div>
        </div>

        <!-- Importamos el codigo jsp del modal del formulario para realizar la búsqueda de organismos Origen
             Mediante el archivo "busquedaorganismo.js" se implementa dicha búsqueda -->
        <c:import url="../registro/buscadorOrganismosOficinasREPestanas.jsp">
            <c:param name="tipo" value="listaRegSalida"/>
        </c:import>

        <%--Modal ANULAR--%>
        <c:import url="../registro/anular.jsp">
            <c:param name="tipoRegistro" value="salida"/>
        </c:import>

    </div>
    </div> <!-- /container -->

    <c:import url="../modulos/pie.jsp"/>

    <!-- Cambia la imagen de la búsqueda avanzada-->
    <script>

        var traduccion = new Array();
        traduccion['regweb.busquedaAvanzada'] = "<spring:message code='regweb.busquedaAvanzada' javaScriptEscape='true' />";

        $(function(){
            $("#demo").on("hide.bs.collapse", function(){
                $(".masOpciones-danger").html('<span class="fa fa-plus"></span> ' + traduccion['regweb.busquedaAvanzada']);
            });
            $("#demo").on("show.bs.collapse", function(){
                $(".masOpciones-danger").html('<span class="fa fa-minus"></span> ' + traduccion['regweb.busquedaAvanzada']);
            });
        });
    </script>

</body>
</html>