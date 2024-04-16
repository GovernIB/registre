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
                    <li><a href="<c:url value="/inici"/>"><i class="fa fa-institution"></i> ${loginInfo.entidadActiva.nombre}</a></li>
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
                        <a class="btn btn-warning btn-xs pull-right margin-left10" href="<c:url value="/organismo/arbol"/>" role="button"><span class="fa fa-sitemap"></span> <spring:message code="organismo.organigrama"/></a>
                        <h3 class="panel-title"><i class="fa fa-search"></i> <strong><spring:message code="organismo.buscador.entidad"/> ${entidad.nombre} (${entidad.codigoDir3})</strong></h3>
                    </div>

                    <div class="panel-body">
                        <form:form modelAttribute="organismoBusqueda" method="post" cssClass="form-horizontal">
                            <form:hidden path="pageNumber" value="1"/>

                            <div class="col-xs-12">
                                <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                    <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                        <form:label path="organismo.denominacion"><spring:message code="regweb.nombre"/></form:label>
                                    </div>
                                    <div class="col-xs-8">
                                        <form:input path="organismo.denominacion" cssClass="form-control"/>
                                    </div>
                                </div>

                                <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                    <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                        <form:label path="organismo.codigo"><spring:message code="organismo.codigo"/></form:label>
                                    </div>
                                    <div class="col-xs-8">
                                        <form:input path="organismo.codigo" cssClass="form-control"/>
                                    </div>
                                </div>
                            </div>

                            <div class="col-xs-12">
                                <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                    <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                        <form:label path="organismo.estado.id"><spring:message code="organismo.estado"/></form:label>
                                    </div>
                                    <div class="col-xs-8">

                                        <form:select path="organismo.estado.id"  cssClass="chosen-select">
                                            <form:options items="${estados}" itemValue="id" itemLabel="descripcionEstadoEntidad"/>
                                        </form:select>
                                        <form:errors path="organismo.estado.id" cssClass="help-block" element="span"/>
                                    </div>
                                </div>
                                <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                    <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                         <form:label path="organismo.permiteUsuarios"><spring:message code="organismo.permiteUsuarios"/></form:label>
                                    </div>
                                    <div class="col-xs-8">
                                        <form:select path="organismo.permiteUsuarios" cssClass="chosen-select">
                                            <form:option value="" label="..."/>
                                            <form:option value="true"><spring:message code="regweb.si"/></form:option>
                                            <form:option value="false"><spring:message code="regweb.no"/></form:option>
                                        </form:select>
                                    </div>
                                </div>
                            </div>
                            <div class="col-xs-12">
                                <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                    <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                        <form:label path="organismo.externo"><spring:message code="organismo.externo"/></form:label>
                                    </div>
                                    <div class="col-xs-8">
                                        <form:select path="organismo.externo" cssClass="chosen-select">
                                            <form:option value="" label="..."/>
                                            <form:option value="true"><spring:message code="regweb.si"/></form:option>
                                            <form:option value="false"><spring:message code="regweb.no"/></form:option>
                                        </form:select>
                                    </div>
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

                                        <table class="table table-bordered table-hover table-striped">
                                            <thead>
                                                <tr>
                                                    <th><spring:message code="organismo.organismo"/></th>
                                                    <th><spring:message code="entidad.codigoDir3"/></th>
                                                    <th><spring:message code="organismo.organismoSuperior"/></th>
                                                    <th>EDP</th>
                                                    <th class="center"><spring:message code="organismo.estado"/></th>
                                                    <th width="140" class="center"><spring:message code="regweb.acciones"/></th>
                                                </tr>
                                            </thead>

                                            <tbody>
                                                <c:forEach var="organismo" items="${paginacion.listado}">
                                                    <tr>
                                                        <td>${organismo.denominacion}</td>
                                                        <td>${organismo.codigo}</td>
                                                        <td>
                                                            <c:if test="${not empty organismo.organismoSuperior}">
                                                                ${organismo.organismoSuperior.denominacion}
                                                            </c:if>
                                                            <c:if test="${empty organismo.organismoSuperior}">
                                                                ${organismo.organismoRaiz.denominacion}
                                                            </c:if>
                                                        </td>
                                                        <td>
                                                            <c:if test="${organismo.edp == true}">
                                                                <span class="label label-success">Si</span>
                                                            </c:if>
                                                            <c:if test="${organismo.edp == false}">
                                                                <span class="label label-danger">No</span>
                                                            </c:if>
                                                        </td>
                                                        <td class="center">
                                                            <c:choose>
                                                                <c:when test="${organismo.estado.codigoEstadoEntidad == RegwebConstantes.ESTADO_ENTIDAD_ANULADO}">
                                                                    <span class="label label-danger"><spring:message code="unidad.estado.${organismo.estado.codigoEstadoEntidad}" /></span>
                                                                </c:when>
                                                                <c:when test="${organismo.estado.codigoEstadoEntidad == RegwebConstantes.ESTADO_ENTIDAD_EXTINGUIDO}">
                                                                    <span class="label label-warning"><spring:message code="unidad.estado.${organismo.estado.codigoEstadoEntidad}" /></span>
                                                                </c:when>
                                                                <c:when test="${organismo.estado.codigoEstadoEntidad == RegwebConstantes.ESTADO_ENTIDAD_TRANSITORIO}">
                                                                    <span class="label label-default"><spring:message code="unidad.estado.${organismo.estado.codigoEstadoEntidad}" /></span>
                                                                </c:when>
                                                                <c:when test="${organismo.estado.codigoEstadoEntidad == RegwebConstantes.ESTADO_ENTIDAD_VIGENTE}">
                                                                    <span class="label label-success"><spring:message code="unidad.estado.${organismo.estado.codigoEstadoEntidad}" /></span>
                                                                </c:when>
                                                            </c:choose>
                                                        </td>
                                                        <td class="center">
                                                            <c:if test="${organismo.permiteUsuarios}">

                                                                <div class="btn-group">
                                                                    <button type="button" class="btn btn-primary btn-sm dropdown-toggle" data-toggle="dropdown">
                                                                        <i class="fa fa-users"></i> <span class="caret"></span>
                                                                    </button>
                                                                    <ul class="dropdown-menu">
                                                                        <li class="submenu-complet"><a href="<c:url value="/organismo/${organismo.id}/usuarios"/>" target="_blank"><span class="fa fa-eye"></span> <spring:message code="organismo.usuarios"/></a></li>
                                                                        <li class="submenu-complet"><a href="javascript:void(0);" onclick='javascript:confirm("<c:url value="/organismo/${organismo.id}/desactivarUsuarios"/>","<spring:message code="organismo.usuarios.desactivar" htmlEscape="true"/>")'><span class="fa fa-close"></span> <spring:message code="organismo.permiteUsuarios.desactivar"/></a></li>
                                                                    </ul>
                                                                </div>

                                                            </c:if>
                                                            <c:if test="${not organismo.permiteUsuarios}">
                                                                <a class="btn btn-primary btn-sm" onclick='javascript:confirm("<c:url value="/organismo/${organismo.id}/activarUsuarios"/>","<spring:message code="organismo.usuarios.activar" htmlEscape="true"/>")' href="javascript:void(0);" title="<spring:message code="organismo.usuarios.activar"/>"><span class="fa fa-check"></span></a>
                                                            </c:if>

                                                            <c:if test="${organismo.externo}">
                                                                <a class="btn btn-success btn-sm" href="javascript:void(0);" onclick='javascript:confirm("<c:url value="/organismo/${organismo.id}/desactivarExterno"/>","<spring:message code="organismo.externo.desactivar" htmlEscape="true"/>")' title="<spring:message code="organismo.externo.desactivar"/>"><span class="fa fa-institution"></span></a>
                                                            </c:if>
                                                            <c:if test="${not organismo.externo}">
                                                                <a class="btn btn-danger btn-sm" href="javascript:void(0);" onclick='javascript:confirm("<c:url value="/organismo/${organismo.id}/activarExterno"/>","<spring:message code="organismo.externo.activar" htmlEscape="true"/>")' title="<spring:message code="organismo.externo.activar"/>"><span class="fa fa-ban"></span></a>
                                                            </c:if>
                                                            <a class="btn btn-warning btn-sm" href="<c:url value="/organismo/${organismo.id}/detalle"/>" target="_blank" title="<spring:message code="organismo.detalle"/>"><span class="fa fa-eye"></span></a>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                            </tbody>
                                        </table>

                                        <!-- Paginacion -->
                                        <c:import url="../modulos/paginacionBusqueda.jsp">
                                            <c:param name="entidad" value="organismo"/>
                                        </c:import>

                                    </c:if>
                                </div>
                            </div>
                        </c:if>
                    </div>
                </div> <!--/.panel success-->
            </div>
        </div> <!-- /.row-->


        <%--Botonera--%>
        <c:if test="${loginInfo.rolActivo.nombre == 'RWE_ADMIN'}">
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

<spring:message code="regweb.procesando" var="textoModal" scope="request"/>
<c:import url="../modalSincro.jsp">
    <c:param name="textoModal" value="${textoModal}"/>
</c:import>
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
                        waitingDialog.show('<spring:message code="organismo.organigrama.sincronizando" javaScriptEscape='true'/>', {dialogSize: 'm', progressType: 'success'});
                    },
                    success:function(respuesta){

                        if(respuesta.status == 'SUCCESS'){
                            goTo('<c:url value="/organismo/list"/>');
                        }else{
                            if(respuesta.status=='FAIL') {
                                mostrarMensajeError('#mensajes', respuesta.error);
                                waitingDialog.hide();
                            }
                        }
                    }
                });
            });
        });

        $('#actuali').click(function(){

            confirmModal.modal("show");
            confirmModal.find("#okButton").click(function(event) {
                confirmModal.modal("hide");

                goTo('<c:url value="/entidad/${entidad.id}/actualizar"/>');
            });
        });
    });
</script>


</body>
</html>