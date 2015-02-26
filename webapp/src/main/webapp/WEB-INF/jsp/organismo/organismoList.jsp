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
                    <li><a href="<c:url value="/inici"/>" ><i class="fa fa-power-off"></i> <spring:message code="regweb.inicio"/></a></li>
                    <li><a href="<c:url value="/entidad/${entidad.id}/edit"/>" ><i class="fa fa-globe"></i> ${entidad.nombre}</a></li>
                    <li class="active"><i class="fa fa-list-ul"></i> <spring:message code="organismo.organismos"/></li>
                </ol>
            </div>
        </div><!-- /.row -->

        <div class="row">
            <div class="col-xs-12">

                <div class="panel panel-success">

                    <div class="panel-heading">
                        <a class="btn btn-success btn-xs pull-right" href="<c:url value="/organismo/arbolList"/>" role="button"><i class="fa fa-sitemap"></i> <spring:message code="organismo.arbol"/></a>
                        <h3 class="panel-title"><i class="fa fa-list"></i> <strong><spring:message code="organismo.buscador"/> ${entidad.nombre}</strong></h3>
                    </div>

                    <c:import url="../modulos/mensajes.jsp"/>

                    <div class="panel-body">

                        <form:form modelAttribute="organismoBusqueda" method="post" cssClass="form-horizontal">
                            <form:hidden path="pageNumber"/>

                            <div class="form-group col-xs-6">
                                <div class="col-xs-4 pull-left align-right"><spring:message code="regweb.nombre"/></div>
                                <div class="col-xs-8">
                                    <form:input path="organismo.denominacion" cssClass="form-control"/>
                                </div>
                            </div>

                            <div class="form-group col-xs-6">
                                <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                                    <form:label path="organismo.estado.id"><spring:message code="organismo.estado"/></form:label>
                                </div>
                                <div class="col-xs-8">

                                    <form:select path="organismo.estado.id"  cssClass="chosen-select">
                                        <%--<form:option value="-1">...</form:option>--%>
                                        <form:options items="${estados}" itemValue="id" itemLabel="descripcionEstadoEntidad"/>
                                    </form:select>
                                    <form:errors path="organismo.estado.id" cssClass="help-block" element="span"/>
                                </div>
                            </div>

                            <div class="form-group col-xs-12">
                               <input type="submit" value="<spring:message code="regweb.buscar"/>" class="btn btn-warning btn-sm"/>
                               <input type="reset" value="<spring:message code="regweb.restablecer"/>" class="btn btn-sm"/>
                            </div>
                        </form:form>

                        <c:if test="${paginacion != null}">

                            <div class="row">
                                <div class="col-xs-12">

                                    <c:if test="${paginacion != null && empty paginacion.listado}">
                                        <div class="alert alert-warning alert-dismissable">
                                            <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                                            <spring:message code="regweb.listado.vacio"/> <strong><spring:message code="organismo.organismo"/></strong>
                                        </div>
                                    </c:if>

                                    <c:if test="${not empty paginacion.listado}">

                                        <div class="alert-grey">
                                            <c:if test="${paginacion.totalResults == 1}">
                                                <spring:message code="regweb.resultado"/> <strong>${paginacion.totalResults}</strong> <spring:message code="organismo.organismo"/>
                                            </c:if>
                                            <c:if test="${paginacion.totalResults > 1}">
                                                <spring:message code="regweb.resultados"/> <strong>${paginacion.totalResults}</strong> <spring:message code="organismo.organismos"/>
                                            </c:if>
                                            <p class="pull-right"><spring:message code="regweb.pagina"/> <strong>${paginacion.currentIndex}</strong> de ${paginacion.totalPages}</p>
                                        </div>

                                        <div class="table-responsive">
                                            <table class="table table-bordered table-hover table-striped">
                                                <thead>
                                                <tr>
                                                    <th><spring:message code="organismo.organismo"/></th>
                                                    <th><spring:message code="organismo.estado"/></th>
                                                    <th width="50"><spring:message code="regweb.acciones"/></th>
                                                </tr>
                                                </thead>

                                                <tbody>
                                                <c:forEach var="organismo" items="${paginacion.listado}">
                                                    <tr>
                                                        <td>${organismo.denominacion}</td>
                                                        <td>${organismo.estado.descripcionEstadoEntidad}</td>
                                                        <td>
                                                            <c:if test="${organismo.estado.codigoEstadoEntidad == 'V'}">
                                                                <a class="btn btn-warning btn-sm" href="<c:url value="/libro/${organismo.id}/libros"/>" title="<spring:message code="organismo.libros"/>"><span class="fa fa-book"></span></a>
                                                            </c:if>
                                                            <c:if test="${organismo.estado.codigoEstadoEntidad != 'V'}">
                                                                <a class="btn btn-warning btn-sm disabled" title="<spring:message code="organismo.libros.no"/>"><span class="fa fa-book"></span></a>
                                                            </c:if>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                                </tbody>
                                            </table>

                                            <!-- Paginacion -->
                                            <c:import url="../modulos/paginacionBusqueda.jsp">
                                                <c:param name="entidad" value="organismo"/>
                                            </c:import>

                                        </div>
                                    </c:if>

                                </div>
                            </div>

                        </c:if>
                    </div>

                </div> <!--/.panel success-->

            </div>
        </div> <!-- /.row-->


        <%--Botonera--%>
        <c:if test="${rolAutentidado.nombre == 'RWE_ADMIN'}">
            <c:if test="${empty descarga}">
                <button type="button" id="sincro" class="btn btn-success btn-sm"><spring:message code="entidad.sincronizar"/></button>
            </c:if>
            <c:if test="${not empty descarga}">
                <button type="button" id="actuali" class="btn btn-success btn-sm"><spring:message code="entidad.actualizar"/></button>
            </c:if>

        </c:if>

        <div id="modalSincro" class="modal fade bs-example-modal-lg" >
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h3><spring:message code="organismo.sincronizando"/></h3>
                    </div>
                    <div class="modal-body" >
                        <div class="col-xs-4 centrat" id="carga">
                            <img src="<c:url value="/img/712.GIF"/>" width="60" height="60"/>
                        </div>
                    </div>
                </div>
            </div>
        </div> <!-- /.modal -->
    </div>
</div> <!-- /container -->

<c:import url="../modulos/pie.jsp"/>

<script type="text/javascript">
    $(document).ready(function() {
        $('#sincro').click(function(){
            $.ajax({
                url:'<c:url value="/entidad/${entidad.id}/sincronizar"/>',
                type:'GET',
                beforeSend: function(objeto){
                    $('#modalSincro').modal('show');
                },
                complete:function(){
                    $('#modalSincro').modal('hide');
                    goTo('<c:url value="/organismo/list"/>');
                }
            });


        });
        $('#actuali').click(function(){
            $.ajax({
                url:'<c:url value="/entidad/${entidad.id}/actualizar"/>',
                type:'GET',
                beforeSend: function(objeto){
                    $('#modalSincro').modal('show');
                },
                complete:function(){
                    $('#modalSincro').modal('hide');
                    goTo('<c:url value="/entidad/pendientesprocesar"/>');
                }
            });


        });
    });
</script>


</body>
</html>