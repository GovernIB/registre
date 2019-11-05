<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

<!DOCTYPE html>
<html lang="ca">
<head>
    <title><spring:message code="regweb.titulo"/></title>
    <c:import url="../modulos/imports.jsp"/>
</head>

<body>

<c:import url="../modulos/menu.jsp"/>

<div class="row-fluid container main">

    <div class="well well-white">

        <div class="row">
            <div class="col-xs-12">
                <ol class="breadcrumb">
                    <c:import url="../modulos/migadepan.jsp"/>
                    <li><a href="<c:url value="/organismo/list"/>" ><i class="fa fa-home"></i> <spring:message code="organismo.organismos"/></a></li>
                    <li class="active"><i class="fa fa-list-ul"></i> <spring:message code="organismo.usuarios"/></li>
                </ol>
            </div>
        </div><!-- /.row -->

        <c:import url="../modulos/mensajes.jsp"/>

        <div class="row">
            <div class="col-xs-12">

                <div class="panel panel-warning">

                    <div class="panel-heading">
                        <a class="btn btn-warning btn-xs pull-right" href="<c:url value="/usuario/existeUsuario"/>" role="button"><span class="fa fa-plus"></span> <spring:message code="usuario.nuevo"/></a>
                        <h3 class="panel-title"><i class="fa fa-list"></i> <strong><spring:message code="usuario.buscador"/> de ${entidad.nombre}</strong></h3>
                    </div>

                    <div class="panel-body">
                        <form:form modelAttribute="usuarioEntidadBusqueda" method="post" cssClass="form-horizontal">
                            <form:hidden path="pageNumber"/>

                                <div class="col-xs-12">
                                    <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                        <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                            <form:label path="usuarioEntidad.usuario.identificador"><spring:message code="usuario.identificador"/></form:label>
                                        </div>
                                        <div class="col-xs-8">
                                            <form:input path="usuarioEntidad.usuario.identificador" cssClass="form-control"/>
                                        </div>
                                    </div>
                                    <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                        <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                            <form:label path="usuarioEntidad.usuario.nombre"><spring:message code="regweb.nombre"/></form:label>
                                        </div>
                                        <div class="col-xs-8">
                                            <form:input path="usuarioEntidad.usuario.nombre" cssClass="form-control"/>
                                        </div>
                                    </div>
                                </div>

                                <div class="col-xs-12">
                                    <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                        <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                            <form:label path="usuarioEntidad.usuario.apellido1"><spring:message code="usuario.apellido1"/></form:label>
                                        </div>
                                        <div class="col-xs-8">
                                            <form:input path="usuarioEntidad.usuario.apellido1" cssClass="form-control"/>
                                        </div>
                                    </div>
                                    <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                        <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                            <form:label path="usuarioEntidad.usuario.apellido2"><spring:message code="usuario.apellido2"/></form:label>
                                        </div>
                                        <div class="col-xs-8">
                                            <form:input path="usuarioEntidad.usuario.apellido2" cssClass="form-control"/>
                                        </div>
                                    </div>
                                </div>

                                <div class="col-xs-12">
                                    <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                        <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                            <form:label path="usuarioEntidad.usuario.documento"><spring:message code="usuario.documento"/></form:label>
                                        </div>
                                        <div class="col-xs-8">
                                            <form:input path="usuarioEntidad.usuario.documento" cssClass="form-control"/>
                                        </div>
                                    </div>
                                    <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                        <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                            <form:label path="usuarioEntidad.usuario.tipoUsuario"><spring:message code="usuario.tipoUsuario"/></form:label>
                                        </div>
                                        <div class="col-xs-8">
                                            <form:select path="usuarioEntidad.usuario.tipoUsuario" cssClass="chosen-select">
                                                <form:option value="0" default="default">...</form:option>
                                                <form:option value="1"><spring:message code="usuario.tipo.1"/></form:option>
                                                <form:option value="2"><spring:message code="usuario.tipo.2"/></form:option>
                                            </form:select>
                                        </div>
                                    </div>
                                </div>

                                <div class="col-xs-12">
                                    <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                        <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                            <label for="libro"><spring:message code="registroEntrada.libro"/></label>
                                        </div>
                                        <div class="col-xs-8">
                                            <form:select path="libro"  cssClass="chosen-select">
                                                <form:option path="libro" value="-1" selected="selected">...</form:option>
                                                <form:options path="libro" items="${libros}" itemValue="id" itemLabel="nombre"/>
                                            </form:select>
                                        </div>
                                    </div>
                                </div>


                                <div class="form-group col-xs-12">
                                    <input type="submit" value="<spring:message code="regweb.buscar"/>" class="btn btn-warning btn-sm"/>
                                    <input type="reset" value="<spring:message code="regweb.restablecer"/>" class="btn btn-sm"/>
                                    <c:if test="${not empty paginacion.listado}">
                                        <a class="btn btn-success btn-sm pull-right" onclick="exportarUsuarios('<c:url value="/entidad/exportarUsuarios"/>','${usuarioEntidadBusqueda.usuarioEntidad.usuario.identificador}','${usuarioEntidadBusqueda.usuarioEntidad.usuario.nombre}','${usuarioEntidadBusqueda.usuarioEntidad.usuario.apellido1}','${usuarioEntidadBusqueda.usuarioEntidad.usuario.apellido2}','${usuarioEntidadBusqueda.usuarioEntidad.usuario.documento}','${usuarioEntidadBusqueda.usuarioEntidad.usuario.tipoUsuario}','${usuarioEntidadBusqueda.libro.id}')" title="<spring:message code="usuario.exportar.busqueda"/>"><spring:message code="usuario.exportar"/></a>
                                    </c:if>
                                </div>


                        </form:form>

                        <c:if test="${paginacion != null}">

                            <div class="row">
                                <div class="col-xs-12">

                                    <c:if test="${empty paginacion.listado}">
                                        <div class="alert alert-grey alert-dismissable">
                                            <button type="button" class="close" data-dismiss="alert">&times;</button>
                                            <spring:message code="regweb.listado.vacio"/> <strong><spring:message code="usuario.usuario"/></strong>
                                        </div>
                                    </c:if>

                                    <c:if test="${not empty paginacion.listado}">
                                        <div class="alert-grey">
                                            <c:if test="${paginacion.totalResults == 1}">
                                                <spring:message code="regweb.resultado"/> <strong>${paginacion.totalResults}</strong> <spring:message code="usuario.usuario"/>
                                            </c:if>
                                            <c:if test="${paginacion.totalResults > 1}">
                                                <spring:message code="regweb.resultados"/> <strong>${paginacion.totalResults}</strong> <spring:message code="usuario.usuarios"/>
                                            </c:if>

                                            <p class="pull-right"><spring:message code="regweb.pagina"/> <strong>${paginacion.currentIndex}</strong> de ${paginacion.totalPages}</p>
                                        </div>

                                        <div class="table-responsive">
                                            <table class="table table-bordered table-hover table-striped">
                                                <colgroup>
                                                    <col>
                                                    <col>
                                                    <col>
                                                    <col>
                                                    <col>
                                                    <col width="101">
                                                </colgroup>
                                                <thead>
                                                <tr>
                                                    <th><spring:message code="regweb.nombre"/></th>
                                                    <th><spring:message code="usuario.email"/></th>
                                                    <th><spring:message code="usuario.identificador"/></th>
                                                    <th><spring:message code="usuario.tipo.corto"/></th>
                                                    <th><spring:message code="usuario.roles"/></th>
                                                    <th class="center"><spring:message code="regweb.acciones"/></th>
                                                </tr>
                                                </thead>

                                                <tbody>
                                                <c:forEach var="usuarioEntidad" items="${paginacion.listado}">
                                                    <tr>
                                                        <td>${usuarioEntidad.nombreCompleto}</td>
                                                        <td>${usuarioEntidad.usuario.email}</td>
                                                        <td>${usuarioEntidad.usuario.identificador}</td>
                                                        <td>
                                                            <c:if test="${usuarioEntidad.usuario.tipoUsuario == 1}">
                                                                <span class="label label-success"><spring:message code="usuario.tipo.1"/></span>
                                                            </c:if>
                                                            <c:if test="${usuarioEntidad.usuario.tipoUsuario == 2}">
                                                                <span class="label label-danger"><spring:message code="usuario.tipo.2"/></span>
                                                            </c:if>
                                                        </td>
                                                        <td>
                                                            <c:if test="${usuarioEntidad.usuario.rwe_superadmin}">
                                                                <span class="label label-success" rel="popupAbajo" data-content="<strong><spring:message code="rol.1.nombre"/>:</strong> <spring:message code="rol.1.descripcion"/>" data-toggle="popover"><i class="fa fa-institution"></i></span>
                                                            </c:if>
                                                            <c:if test="${usuarioEntidad.usuario.rwe_admin}">
                                                                <span class="label label-success" rel="popupAbajo" data-content="<strong><spring:message code="rol.2.nombre"/>:</strong> <spring:message code="rol.2.descripcion"/>" data-toggle="popover"><i class="fa fa-home"></i></span>
                                                            </c:if>
                                                            <c:if test="${usuarioEntidad.usuario.rwe_usuari}">
                                                                <span class="label label-success" rel="popupAbajo" data-content="<strong><spring:message code="rol.3.nombre"/>:</strong> <spring:message code="rol.3.descripcion"/>" data-toggle="popover"><i class="fa fa-file-o"></i></span>
                                                            </c:if>
                                                            <c:if test="${usuarioEntidad.usuario.rwe_ws_entrada}">
                                                                <span class="label label-success" rel="popupAbajo" data-content="<strong><spring:message code="rol.4.nombre"/>:</strong> <spring:message code="rol.4.descripcion"/>" data-toggle="popover"><i class="fa fa-mail-reply"></i></span>
                                                            </c:if>
                                                            <c:if test="${usuarioEntidad.usuario.rwe_ws_salida}">
                                                                <span class="label label-success" rel="popupAbajo" data-content="<strong><spring:message code="rol.5.nombre"/>:</strong> <spring:message code="rol.5.descripcion"/>" data-toggle="popover"><i class="fa fa-mail-forward"></i></span>
                                                            </c:if>
                                                            <c:if test="${usuarioEntidad.usuario.rwe_ws_ciudadano}">
                                                                <span class="label label-success" rel="popupAbajo" data-content="<strong><spring:message code="rol.6.nombre"/>:</strong> <spring:message code="rol.6.descripcion"/>" data-toggle="popover"><i class="fa fa-user"></i></span>
                                                            </c:if>
                                                        </td>
                                                        <td class="center">
                                                            <c:if test="${usuarioEntidad.usuario.rwe_usuari == true || usuarioEntidad.usuario.rwe_ws_entrada == true
                                                            || usuarioEntidad.usuario.rwe_ws_salida == true || usuarioEntidad.usuario.rwe_ws_ciudadano == true}">
                                                                <a class="btn btn-warning btn-sm" href="<c:url value="/entidad/permisos/${usuarioEntidad.id}"/>" title="<spring:message code="usuario.modificar.permisos"/>"><span class="fa fa-key"></span></a>
                                                            </c:if>
                                                            <c:if test="${usuarioEntidad.usuario.rwe_usuari == false && usuarioEntidad.usuario.rwe_ws_entrada == false
                                                            && usuarioEntidad.usuario.rwe_ws_salida == false && usuarioEntidad.usuario.rwe_ws_ciudadano == false}">
                                                                <a class="btn btn-warning disabled btn-sm" title="<spring:message code="usuario.asignar.permisos.denegado"/> "><span class="fa fa-key"></span></a>
                                                            </c:if>

                                                            <a class="btn btn-danger btn-sm" onclick='javascript:confirm("<c:url value="/entidad/permisos/${usuarioEntidad.id}/delete"/>","<spring:message code="regweb.confirmar.eliminacion" htmlEscape="true"/>")' href="javascript:void(0);" title="<spring:message code="regweb.eliminar"/>"><span class="fa fa-eraser"></span></a>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                                </tbody>
                                            </table>

                                            <!-- Paginacion -->
                                            <c:import url="../modulos/paginacionBusqueda.jsp">
                                                <c:param name="entidad" value="usuarioEntidad"/>
                                            </c:import>

                                        </div>

                                    </c:if>

                                </div>
                            </div>

                        </c:if>

                    </div>

                </div>
            </div>

        </div>




    </div>
</div> <!-- /container -->

<c:import url="../modulos/pie.jsp"/>

<script type="text/javascript">
    /**
     * Script per exportar els usuaris consultats a l'Excel
     * @param url
     * @param identificador
     * @param nombre
     * @param apellido1
     * @param apellido2
     * @param documento
     * @param tipo
     * @param libro
     */
    function exportarUsuarios(url,identificador,nombre,apellido1,apellido2,documento,tipo,libro){
        if(tipo != ''){
            url = url + "?tipo="+tipo;
        }
        if(identificador != ''){
            url = url + "&identificador="+identificador;
        }
        if(nombre != ''){
            url = url + "&nombre="+nombre;
        }
        if(apellido1 != ''){
            url = url + "&apellido1="+apellido1;
        }
        if(apellido2 != ''){
            url = url + "&apellido2="+apellido2;
        }
        if(documento != ''){
            url = url + "&documento="+documento;
        }
        if(libro != ''){
            url = url + "&idLibro="+libro;
        }
        goTo(url);
    }
</script>


</body>
</html>