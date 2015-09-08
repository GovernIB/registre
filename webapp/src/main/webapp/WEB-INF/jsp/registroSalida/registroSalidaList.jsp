<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>
<un:useConstants var="RegwebConstantes" className="es.caib.regweb3.utils.RegwebConstantes"/>

<!DOCTYPE html>
<html lang="ca">
<head>
    <title><spring:message code="registroSalida.buscador"/></title>
    <c:import url="../modulos/imports.jsp"/>
    <script type="text/javascript" src="<c:url value="/js/busquedaorganismo.js"/>"></script>
    <script type="text/javascript">
  		var tradorganismo = new Array();
  		tradorganismo['organismo.denominacion'] = "<spring:message code='organismo.denominacion' javaScriptEscape='true' />";
  		tradorganismo['regweb.acciones'] = "<spring:message code='regweb.acciones' javaScriptEscape='true' />";
        tradorganismo['organismo.superior'] = "<spring:message code='organismo.superior' javaScriptEscape='true' />";
  	</script>
</head>

<body>

    <c:import url="../modulos/menu.jsp"/>

    <div class="row-fluid container main">

    <div class="well well-white">

        <div class="row">
            <div class="col-xs-12">
                <ol class="breadcrumb">
                    <li><a href="<c:url value="/inici"/>"><i class="fa fa-globe"></i> ${oficinaActiva.denominacion}</a></li>
                    <li class="active"><i class="fa fa-list-ul"></i> <strong><spring:message code="registroSalida.buscador"/></strong></li>
                    <%--Importamos el menú de avisos--%>
                    <c:import url="/avisos"/>
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

                    <div class="row">

                        <div class="form-group col-xs-6">
                            <div class="col-xs-4"><span class="text-danger">*</span> <spring:message code="registroSalida.libro"/></div>
                            <div class="col-xs-8">
                                <form:select path="registroSalida.libro.id" items="${librosConsulta}" itemLabel="nombreCompleto" itemValue="id" cssClass="chosen-select"/>
                            </div>
                        </div>
                        <div class="form-group col-xs-6">
                            <div class="col-xs-4 "><spring:message code="registroSalida.estado"/></div>
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
                    <div class="row">

                        <div class="form-group col-xs-6">
                            <div class="col-xs-4 "><spring:message code="registroSalida.numeroRegistro"/></div>
                            <div class="col-xs-8">
                                <form:input path="registroSalida.numeroRegistroFormateado" cssClass="form-control"/> <form:errors path="registroSalida.numeroRegistroFormateado" cssClass="help-block" element="span"/>
                            </div>
                        </div>
                        <div class="form-group col-xs-6">
                            <div class="col-xs-4 "><spring:message code="registroSalida.extracto"/></div>
                            <div class="col-xs-8">
                                <form:input path="registroSalida.registroDetalle.extracto" cssClass="form-control" maxlength="200"/> <form:errors path="registroSalida.registroDetalle.extracto" cssClass="help-block" element="span"/>
                            </div>
                        </div>

                    </div>
                    <div class="row">

                        <div class="form-group col-xs-6">
                            <div class="col-xs-4"><span class="text-danger">*</span> <spring:message code="informe.fechaInicio"/></div>
                            <div class="col-xs-8" id="fechaInicio">
                                <div class="input-group date no-pad-right">
                                    <form:input path="fechaInicio" type="text" cssClass="form-control"  maxlength="10" placeholder="dd/mm/yyyy" name="fechaInicio"/>
                                    <span class="input-group-addon"><span class="fa fa-calendar"></span></span>
                                </div>
                                <form:errors path="fechaInicio" cssClass="help-block" element="span"/>

                            </div>
                        </div>
                        <div class="form-group col-xs-6">
                            <div class="col-xs-4"><span class="text-danger">*</span> <spring:message code="informe.fechaFin"/></div>
                            <div class="col-xs-8" id="fechaFin">
                                <div class="input-group date no-pad-right">
                                    <form:input type="text" cssClass="form-control" path="fechaFin" maxlength="10" placeholder="dd/mm/yyyy" name="fechaFin"/>
                                    <span class="input-group-addon"><span class="fa fa-calendar"></span></span>
                                </div>
                                <form:errors path="fechaFin" cssClass="help-block" element="span"/>

                            </div>
                        </div>

                    </div>


                    <c:if test="${empty registroSalidaBusqueda.registroSalida.oficina.id && empty registroSalidaBusqueda.interessatDoc && empty registroSalidaBusqueda.interessatNom && empty registroSalidaBusqueda.organOrigen && empty registroSalidaBusqueda.observaciones && empty registroSalidaBusqueda.usuario && !registroSalidaBusqueda.anexos}">
                        <div id="demo" class="collapse">
                    </c:if>
                    <c:if test="${not empty registroSalidaBusqueda.registroSalida.oficina.id || not empty registroSalidaBusqueda.interessatDoc || not empty registroSalidaBusqueda.interessatNom || not empty registroSalidaBusqueda.organOrigen || not empty registroSalidaBusqueda.observaciones || not empty registroSalidaBusqueda.usuario || registroSalidaBusqueda.anexos}">
                        <div id="demo" class="collapse in">
                    </c:if>

                        <div class="row">
                            <div class="form-group col-xs-6">
                                <div class="col-xs-4 "><spring:message code="registroEntrada.nombreInteresado"/></div>
                                <div class="col-xs-8">
                                    <form:input  path="interessatNom" cssClass="form-control" maxlength="255"/>
                                    <form:errors path="interessatNom" cssClass="help-block" element="span"/>
                                </div>
                            </div>
                            <div class="form-group col-xs-6">
                                <div class="col-xs-4 "><spring:message code="registroEntrada.docInteresado"/></div>
                                <div class="col-xs-8">
                                    <form:input  path="interessatDoc" cssClass="form-control" maxlength="17"/>
                                    <form:errors path="interessatDoc" cssClass="help-block" element="span"/>
                                </div>
                            </div>
                        </div>

                        <div class="row">
                            <div class="form-group col-xs-6">
                                <div class="col-xs-4 "><spring:message code="registro.oficinaRegistro"/></div>
                                <div class="col-xs-8">
                                    <form:select path="registroSalida.oficina.id" cssClass="chosen-select">
                                        <form:option value="" label="..."/>
                                        <c:forEach var="oficinaRegistro" items="${oficinasRegistro}">
                                            <form:option value="${oficinaRegistro.id}">${oficinaRegistro.denominacion}</form:option>
                                        </c:forEach>
                                    </form:select>
                                </div>
                            </div>
                            <div class="form-group col-xs-6">
                                <div class="col-xs-4"><spring:message code="registroEntrada.organOrigen"/></div>
                                <div class="col-xs-6">
                                    <form:select path="organOrigen" cssClass="chosen-select">
                                        <form:option value="" label="..."/>
                                        <c:forEach items="${organosOrigen}" var="organismo">
                                            <option value="${organismo.codigo}" <c:if test="${registroSalidaBusqueda.organOrigen == organismo.codigo}">selected="selected"</c:if>>${organismo.denominacion}</option>
                                        </c:forEach>
                                    </form:select>
                                    <form:errors path="organOrigen" cssClass="help-block" element="span"/>
                                    <form:hidden path="organOrigenNom"/>
                                </div>
                                <div class="col-xs-2 boto-panel">
                                    <a data-toggle="modal" role="button" href="#modalBuscadorlistaRegSalida" onclick="inicializarBuscador('#codNivelAdministracionlistaRegSalida','#codComunidadAutonomalistaRegSalida','${oficina.organismoResponsable.nivelAdministracion.codigoNivelAdministracion}', '${oficina.organismoResponsable.codAmbComunidad.codigoComunidad}', 'listaRegSalida');" class="btn btn-warning btn-sm"><spring:message code="regweb.buscar"/></a>
                                </div>
                            </div>
                        </div>

                        <div class="row">
                            <div class="form-group col-xs-6">
                                <div class="col-xs-4"><spring:message code="registroEntrada.observaciones"/></div>
                                <div class="col-xs-8">
                                    <form:input path="observaciones" class="form-control" type="text" value=""/>
                                </div>
                            </div>
                            <div class="form-group col-xs-6">
                                <div class="col-xs-4"><spring:message code="usuario.usuario"/></div>
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

                        <div class="row" style="display:none;">
                            <div class="form-group col-xs-6">
                                <div class="col-xs-4 "><spring:message code="registroEntrada.anexos"/></div>
                                <div class="col-xs-8">
                                    <form:checkbox path="anexos"/>
                                </div>
                            </div>
                            <div class="form-group col-xs-6"><div class="col-xs-12">&nbsp;</div></div>
                        </div>

                    </div>

                <div class="row pad-bottom15">
                    <a class="masOpciones-danger" data-toggle="collapse" data-target="#demo">
                        <%--Comprueba si debe mostrar mas opciones o menos--%>
                        <c:if test="${empty registroSalidaBusqueda.registroSalida.oficina.id && empty registroSalidaBusqueda.interessatDoc && empty registroSalidaBusqueda.interessatNom && empty registroSalidaBusqueda.organOrigen && empty registroSalidaBusqueda.observaciones && empty registroSalidaBusqueda.usuario && !registroSalidaBusqueda.anexos}">
                            <span class="fa fa-plus-square-o"></span> <spring:message code="regweb.busquedaAvanzada"/>
                        </c:if>
                        <c:if test="${not empty registroSalidaBusqueda.registroSalida.oficina.id || not empty registroSalidaBusqueda.interessatDoc || not empty registroSalidaBusqueda.interessatNom || not empty registroSalidaBusqueda.organOrigen || not empty registroSalidaBusqueda.observaciones || not empty registroSalidaBusqueda.usuario || registroSalidaBusqueda.anexos}">
                            <span class="fa fa-minus-square-o"></span> <spring:message code="regweb.busquedaAvanzada"/>
                        </c:if>
                    </a>
                </div>

                <div class="row">

                    <div class="form-group col-xs-12">
                        <div class="col-xs-1 boto-panel">
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
                                <div class="alert alert-warning alert-dismissable">
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
                                        <th><spring:message code="registroSalida.numeroRegistro"/></th>
                                        <th><spring:message code="registroSalida.fecha"/></th>
                                        <%--<th><spring:message code="registroSalida.libro.corto"/></th>--%>
                                        <th><spring:message code="registroSalida.usuario"/></th>
                                        <th><spring:message code="registroSalida.oficina"/></th>
                                        <th><spring:message code="registroSalida.origen"/></th>
                                        <c:if test="${registroSalidaBusqueda.registroSalida.estado == 2}">
                                            <th><spring:message code="registroEntrada.reserva"/></th>
                                        </c:if>
                                        <c:if test="${registroSalidaBusqueda.registroSalida.estado != 2}">
                                            <th><spring:message code="registroSalida.extracto"/></th>
                                        </c:if>
                                        <th><spring:message code="registroSalida.estado"/></th>
                                        <th><spring:message code="registroEntrada.interesados"/></th>
                                        <th><spring:message code="registroEntrada.anexos"/></th>

                                        <th class="center"><spring:message code="regweb.acciones"/></th>
                                    </tr>
                                    </thead>

                                    <tbody>
                                    <c:forEach var="registroSalida" items="${paginacion.listado}" varStatus="status">
                                        <tr>
                                            <td>${registroSalida.numeroRegistroFormateado}</td>
                                            <td><fmt:formatDate value="${registroSalida.fecha}" pattern="dd/MM/yyyy"/></td>
                                            <%--<td><label class="no-bold" rel="ayuda" data-content="${registroSalida.libro.nombre}" data-toggle="popover">${registroSalida.libro.codigo}</label></td>--%>
                                            <td>${registroSalida.usuario.usuario.identificador}</td>
                                            <td><label class="no-bold" rel="ayuda" data-content="${registroSalida.oficina.denominacion}" data-toggle="popover">${registroSalida.oficina.codigo}</label></td>
                                            <c:if test="${registroSalida.origen != null}">
                                                <td>${registroSalida.origen.denominacion}</td>
                                            </c:if>
                                            <c:if test="${registroSalida.origen == null}">
                                                <td>${registroSalida.origenExternoDenominacion}</td>
                                            </c:if>
                                            <td>${registroSalida.registroDetalle.extracto}</td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${registroSalida.estado == RegwebConstantes.ESTADO_VALIDO}">
                                                        <span class="label label-success"><spring:message code="registro.estado.${registroSalida.estado}" /></span>
                                                    </c:when>
                                                    <c:when test="${registroSalida.estado == RegwebConstantes.ESTADO_PENDIENTE}">
                                                        <span class="label label-warning"><spring:message code="registro.estado.${registroSalida.estado}" /></span>
                                                    </c:when>
                                                    <c:when test="${registroSalida.estado == RegwebConstantes.ESTADO_PENDIENTE_VISAR}">
                                                        <span class="label label-info"><spring:message code="registro.estado.${registroSalida.estado}" /></span>
                                                    </c:when>
                                                    <c:when test="${registroSalida.estado == RegwebConstantes.ESTADO_OFICIO_EXTERNO || registroSalida.estado == RegwebConstantes.ESTADO_OFICIO_INTERNO}">
                                                        <span class="label label-default"><spring:message code="registro.estado.${registroSalida.estado}" /></span>
                                                    </c:when>
                                                    <c:when test="${registroSalida.estado == RegwebConstantes.ESTADO_ENVIADO}">
                                                        <span class="label label-primary"><spring:message code="registro.estado.${registroSalida.estado}" /></span>
                                                    </c:when>
                                                    <c:when test="${registroSalida.estado == RegwebConstantes.ESTADO_TRAMITADO}">
                                                        <span class="label label-primary"><spring:message code="registro.estado.${registroSalida.estado}" /></span>
                                                    </c:when>
                                                    <c:when test="${registroSalida.estado == RegwebConstantes.ESTADO_ANULADO}">
                                                        <span class="label label-danger"><spring:message code="registro.estado.${registroSalida.estado}" /></span>
                                                    </c:when>

                                                </c:choose>
                                            </td>
                                            <c:if test="${registroSalida.registroDetalle.interesados != null}">
                                                <td class="center"><label class="no-bold" rel="ayuda" data-content="${registroSalida.registroDetalle.nombreInteresadosHtml}" data-toggle="popover">${fn:length(registroSalida.registroDetalle.interesados)}</label></td>
                                            </c:if>
                                            <c:if test="${registroSalida.registroDetalle.interesados == null}">
                                                <td class="center">0</td>
                                            </c:if>
                                            <c:if test="${registroSalida.registroDetalle.anexos != null}">
                                                <td class="center">${fn:length(registroSalida.registroDetalle.anexos)}</td>
                                            </c:if>
                                            <c:if test="${registroSalida.registroDetalle.anexos == null}">
                                                <td class="center">0</td>
                                            </c:if>

                                            <td class="center">
                                                <a class="btn btn-info btn-sm" href="<c:url value="/registroSalida/${registroSalida.id}/detalle"/>" title="<spring:message code="registroSalida.detalle"/>"><span class="fa fa-eye"></span></a>
                                                    <%--Acciones según el estado--%>
                                                    <%--Si no nos encontramos en la misma Oficia en la que se creó el Registro, no podemos hacer nada con el--%>
                                                <c:if test="${registroSalida.oficina.id == oficinaActiva.id}">
                                                    <c:choose>
                                                        <c:when test="${registroSalida.estado == RegwebConstantes.ESTADO_VALIDO && puedeEditar}">  <%--Válido--%>
                                                            <a class="btn btn-warning btn-sm" href="<c:url value="/registroSalida/${registroSalida.id}/edit"/>" title="<spring:message code="regweb.editar"/>"><span class="fa fa-pencil"></span></a>
                                                            <a class="btn btn-danger btn-sm" href="javascript:void(0);" onclick='javascript:confirm("<c:url value="/registroSalida/${registroSalida.id}/anular"/>","<spring:message code="regweb.confirmar.anular" htmlEscape="true"/>")' title="<spring:message code="regweb.anular"/>"><span class="fa fa-thumbs-o-down"></span></a>
                                                        </c:when>

                                                        <c:when test="${registroSalida.estado == RegwebConstantes.ESTADO_PENDIENTE_VISAR && isAdministradorLibro}">  <%--Pendiente de Visar--%>
                                                            <a class="btn btn-danger btn-sm" href="javascript:void(0);" onclick='javascript:confirm("<c:url value="/registroSalida/${registroSalida.id}/anular"/>","<spring:message code="regweb.confirmar.anular" htmlEscape="true"/>")' title="<spring:message code="regweb.anular"/>"><span class="fa fa-thumbs-o-down"></span></a>
                                                        </c:when>

                                                        <c:when test="${registroSalida.estado == RegwebConstantes.ESTADO_ANULADO && puedeEditar}">  <%--Anulado--%>
                                                            <a class="btn btn-primary btn-sm" onclick='javascript:confirm("<c:url value="/registroSalida/${registroSalida.id}/activar"/>","<spring:message code="regweb.confirmar.activar" htmlEscape="true"/>")' href="javascript:void(0);" title="<spring:message code="regweb.activar"/>"><span class="fa fa-thumbs-o-up"></span></a>
                                                        </c:when>

                                                    </c:choose>
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

                        </c:if>

                    </div>

                </c:if>


            </div>
            </div>
            </div>
        </div>

        <!-- Importamos el codigo jsp del modal del formulario para realizar la búsqueda de organismos Origen
             Mediante el archivo "busquedaorganismo.js" se implementa dicha búsqueda -->
        <c:import url="../registro/buscadorOrganismosOficinasRE.jsp">
            <c:param name="tipo" value="listaRegSalida"/>
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
                $(".masOpciones-danger").html('<span class="fa fa-plus-square-o"></span> ' + traduccion['regweb.busquedaAvanzada']);
            });
            $("#demo").on("show.bs.collapse", function(){
                $(".masOpciones-danger").html('<span class="fa fa-minus-square-o"></span> ' + traduccion['regweb.busquedaAvanzada']);
            });
        });
    </script>

</body>
</html>