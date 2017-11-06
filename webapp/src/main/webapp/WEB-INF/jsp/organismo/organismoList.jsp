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

                <c:import url="../modulos/mensajes.jsp"/>
                <div id="mensajes"></div>

                <div class="panel panel-warning">

                    <div class="panel-heading">
                        <a class="btn btn-warning btn-xs pull-right margin-left10" href="<c:url value="/organismo/arbolList"/>" role="button"><span class="fa fa-sitemap"></span> <spring:message code="organismo.organigrama"/></a>
                        <a class="btn btn-warning btn-xs pull-right" href="<c:url value="/entidad/librosCambiar"/>" role="button"><span class="fa fa-book"></span> <spring:message code="entidad.cambiarlibros"/></a>
                        <h3 class="panel-title"><i class="fa fa-list"></i> <strong><spring:message
                                code="organismo.buscador.entidad"/> ${entidad.nombre} (${entidad.codigoDir3})</strong></h3>
                    </div>

                    <div class="panel-body">
                        <form:form modelAttribute="organismoBusqueda" method="post" cssClass="form-horizontal">
                            <form:hidden path="pageNumber" value="1"/>

                            <div class="form-group col-xs-6">
                                <div class="col-xs-4 pull-left align-right"><spring:message code="regweb.nombre"/></div>
                                <div class="col-xs-8">
                                    <form:input path="organismo.denominacion" cssClass="form-control"/>
                                </div>
                            </div>

                            <div class="form-group col-xs-6">
                                <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                                    <form:label path="organismo.estado.id"><span class="text-danger">*</span> <spring:message code="organismo.estado"/></form:label>
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
                                        <div class="alert alert-grey alert-dismissable">
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
                                                    <th><spring:message code="entidad.codigoDir3"/></th>
                                                    <th class="center"><spring:message code="organismo.estado"/></th>
                                                    <th class="center"><spring:message code="organismo.libros"/></th>
                                                    <th width="100" class="center"><spring:message code="regweb.acciones"/></th>
                                                </tr>
                                                </thead>

                                                <tbody>
                                                <c:forEach var="organismo" items="${paginacion.listado}">
                                                    <tr>
                                                        <td>${organismo.denominacion}</td>
                                                        <td>${organismo.codigo}</td>
                                                        <td>${organismo.estado.descripcionEstadoEntidad}</td>
                                                        <td class="center">${fn:length(organismo.libros)}</td>
                                                        <td class="center">
                                                            <c:if test="${organismo.estado.codigoEstadoEntidad == RegwebConstantes.ESTADO_ENTIDAD_VIGENTE}">
                                                                <a class="btn btn-warning btn-sm" href="<c:url value="/libro/${organismo.id}/libros"/>" title="<spring:message code="organismo.libros"/>"><span class="fa fa-book"></span></a>
                                                            </c:if>
                                                            <c:if test="${organismo.estado.codigoEstadoEntidad != RegwebConstantes.ESTADO_ENTIDAD_VIGENTE}">
                                                                <a class="btn btn-warning btn-sm disabled" title="<spring:message code="organismo.libros.no"/>"><span class="fa fa-book"></span></a>
                                                            </c:if>
                                                            <a class="btn btn-warning btn-sm" href="<c:url value="/organismo/${organismo.id}/oficinas"/>" title="<spring:message code="organismo.oficinas"/>"><span class="fa fa-home"></span></a>
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
        <c:if test="${rolAutenticado.nombre == 'RWE_ADMIN'}">
            <c:if test="${empty descarga}">
                <button type="button" id="sincro" class="btn btn-success btn-sm"><spring:message code="entidad.sincronizar"/></button>
            </c:if>
            <c:if test="${not empty descarga}">
                <button type="button" id="actuali" class="btn btn-success btn-sm"><spring:message code="entidad.actualizar"/></button>
                <spring:message code="catalogoDir3.sincronizar.fecha"/>: <fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${descarga.fechaImportacion}" />
            </c:if>
        </c:if>

    </div>
</div> <!-- /container -->

<c:import url="../modalSincro.jsp"/>
<c:import url="../modulos/pie.jsp"/>

<script type="text/javascript" src="<c:url value="/js/organismosaprocesar.js"/>"></script>
<script type="text/javascript">
    var trads = new Array();
    trads['actualizacion.nook'] = "<spring:message code="regweb.actualizacion.nook" javaScriptEscape='true' />";
    trads['actualizacion.nopermitido'] = "<spring:message code="regweb.actualizacion.nopermitido" javaScriptEscape='true' />";


    $(document).ready(function() {
        var confirmModal =
                $("<div class=\"modal fade\">" +
                "<div class=\"modal-dialog\">" +
                "<div class=\"modal-content\">"+
                "<div class=\"modal-header\">" +
                "<button type=\"button\" class=\"close\" data-dismiss=\"modal\" aria-hidden=\"true\">&times;</button>" +
                "<h4 class=\"modal-title\"><spring:message code="regweb.confirmar" htmlEscape="true"/></h4>" +
                "</div>" +

                "<div class=\"modal-body\">" +
                "<p><spring:message code="catalogoDir3.confirmacion.actualizar" htmlEscape="true"/></p>" +
                "</div>" +

                "<div class=\"modal-footer\">" +
                "<button type=\"button\" id=\"noButton\" class=\"btn btn-default\" data-dismiss=\"modal\">No</button>"+
                "<button type=\"button\" id=\"okButton\" class=\"btn btn-danger\">Sí</button>"+
                "</div>" +
                "</div>" +
                "</div>" +
                "</div>");

        $('#sincro').click(function(){

            confirmModal.modal("show");
            confirmModal.find("#okButton").click(function(event) {
                confirmModal.modal("hide");

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

        });
        $('#actuali').click(function(){

            confirmModal.modal("show");
            confirmModal.find("#okButton").click(function(event) {
                confirmModal.modal("hide");

                $.ajax({
                    url:'<c:url value="/entidad/${entidad.id}/actualizar"/>',
                    type:'GET',
                    beforeSend: function(objeto){
                        $('#modalSincro').modal('show');
                    },
                    success:function(respuesta){

                        if(respuesta.status == 'SUCCESS'){
                            goTo('<c:url value="/entidad/pendientesprocesar"/>');
                        }else{
                            if(respuesta.status=='NOTALLOWED'){
                                $('#modalSincro').modal('hide');
                                mostrarMensajeError('#mensajes', trads['actualizacion.nopermitido']);
                            }
                            if(respuesta.status=='FAIL') {
                                $('#modalSincro').modal('hide');
                                mostrarMensajeError('#mensajes', trads['actualizacion.nook']);
                            }
                        }

                    }
                });

            });

        });
    });
</script>


</body>
</html>