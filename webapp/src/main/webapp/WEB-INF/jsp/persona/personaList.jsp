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
                    <li class="active"><i class="fa fa-list-ul"></i> <spring:message code="persona.personas"/></li>
                </ol>
            </div>
        </div><!-- /.row -->

        <c:import url="../modulos/mensajes.jsp"/>

        <div class="row">
            <div class="col-xs-12">
                <div class="panel panel-warning">

                    <div class="panel-heading">
                        <a class="btn btn-warning btn-xs pull-right" href="<c:url value="/persona/new"/>" role="button"><span class="fa fa-plus"></span> <spring:message code="persona.nuevo"/></a>
                        <h3 class="panel-title"><i class="fa fa-search"></i><strong><spring:message code="persona.buscador"/></strong> </h3>
                    </div>

                    <div class="panel-body">
                        <form:form modelAttribute="personaBusqueda" method="post" cssClass="form-horizontal">
                            <form:hidden path="pageNumber"/>


                            <div class="col-xs-12">
                                <div class="col-xs-6 espaiLinies">
                                    <div class="col-xs-4 pull-left etiqueta_regweb">
                                        <label for="persona.nombre" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.nombrePersona.busqueda"/>" data-toggle="popover"><spring:message code="regweb.nombre"/></label>
                                    </div>
                                    <div class="col-xs-8">
                                        <form:input path="persona.nombre" cssClass="form-control"/>
                                    </div>
                                </div>
                                <div class="col-xs-6 espaiLinies">
                                    <div class="col-xs-4 pull-left etiqueta_regweb">
                                        <label for="persona.apellido1" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.apellido1Persona.busqueda"/>" data-toggle="popover"><spring:message code="persona.apellido1"/></label>
                                    </div>
                                    <div class="col-xs-8">
                                        <form:input path="persona.apellido1" cssClass="form-control"/>
                                    </div>
                                </div>
                            </div>

                            <div class="col-xs-12">
                                <div class="col-xs-6 espaiLinies">
                                    <div class="col-xs-4 pull-left etiqueta_regweb">
                                        <label for="persona.apellido2" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.apellido2Persona.busqueda"/>" data-toggle="popover"><spring:message code="persona.apellido2"/></label>
                                    </div>
                                    <div class="col-xs-8">
                                        <form:input path="persona.apellido2" cssClass="form-control"/>
                                    </div>
                                </div>
                                <div class="col-xs-6 espaiLinies">
                                    <div class="col-xs-4 pull-left etiqueta_regweb">
                                        <label for="persona.documento" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.documentoPersona.busqueda"/>" data-toggle="popover"><spring:message code="persona.documento"/></label>
                                    </div>
                                    <div class="col-xs-8">
                                        <form:input path="persona.documento" cssClass="form-control"/>
                                    </div>
                                </div>
                            </div>

                            <div class="col-xs-12">
                                <div class="col-xs-6 espaiLinies">
                                    <div class="col-xs-4 pull-left etiqueta_regweb">
                                        <label for="persona.tipo" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.tipoPersona.busqueda"/>" data-toggle="popover"><spring:message code="persona.tipoPersona"/></label>
                                    </div>
                                    <div class="col-xs-8">
                                        <form:select path="persona.tipo" cssClass="chosen-select">
                                            <form:option value="-1" default="default">...</form:option>
                                            <c:forEach items="${tiposPersona}" var="tmp">
                                                <form:option value="${tmp}" > <spring:message code="persona.tipo.${tmp}"/></form:option>
                                            </c:forEach>
                                        </form:select>
                                    </div>
                                </div>
                            </div>

                            <div class="form-group col-xs-12">
                                <input type="submit" value="<spring:message code="regweb.buscar"/>" class="btn btn-warning btn-sm"/>
                                <input type="reset" value="<spring:message code="regweb.restablecer"/>" class="btn btn-sm"/>
                                <c:if test="${not empty paginacion.listado}">
                                    <a class="btn btn-success btn-sm pull-right" onclick="exportar('<c:url value="/persona/exportarPersonas"/>','${personaBusquedaForm.persona.tipo}','${personaBusquedaForm.persona.nombre}','${personaBusquedaForm.persona.apellido1}','${personaBusquedaForm.persona.apellido2}','${personaBusquedaForm.persona.documento}')" title="<spring:message code="persona.exportar.busqueda"/>"><spring:message code="persona.exportar"/></a>
                                </c:if>
                            </div>
                        </form:form>

                        <c:if test="${paginacion != null}">

                            <div class="row">
                                <div class="col-xs-12">

                                    <c:if test="${empty paginacion.listado}">
                                        <div class="alert alert-grey alert-dismissable">
                                            <button type="button" class="close" data-dismiss="alert">&times;</button>
                                            <spring:message code="regweb.busqueda.vacio"/> <strong><spring:message code="persona.persona"/></strong>
                                        </div>
                                    </c:if>

                                    <c:if test="${not empty paginacion.listado}">
                                        <div class="alert-grey">
                                            <c:if test="${paginacion.totalResults == 1}">
                                                <spring:message code="regweb.resultado"/> <strong>${paginacion.totalResults}</strong> <spring:message code="persona.persona"/>
                                            </c:if>
                                            <c:if test="${paginacion.totalResults > 1}">
                                                <spring:message code="regweb.resultados"/> <strong>${paginacion.totalResults}</strong> <spring:message code="persona.personas"/>
                                            </c:if>

                                            <p class="pull-right"><spring:message code="regweb.pagina"/> <strong>${paginacion.currentIndex}</strong> de ${paginacion.totalPages}</p>
                                        </div>

                                        <div class="table-responsive">
                                            <table class="table table-bordered table-hover table-striped">
                                                <colgroup>
                                                    <col>
                                                    <col>
                                                    <col>
                                                    <col width="101">
                                                </colgroup>
                                                <thead>
                                                <tr>
                                                    <th><spring:message code="regweb.nombre"/></th>
                                                    <th><spring:message code="persona.documento"/></th>
                                                    <th><spring:message code="persona.tipoPersona"/></th>
                                                    <th class="center"><spring:message code="regweb.acciones"/></th>
                                                </tr>
                                                </thead>

                                                <tbody>
                                                <c:forEach var="persona" items="${paginacion.listado}">
                                                    <tr>
                                                        <td>
                                                            <c:if test="${persona.tipo == 2}"> ${persona.nombrePersonaFisica} </c:if>
                                                            <c:if test="${persona.tipo == 3}"> ${persona.nombrePersonaJuridica} </c:if>
                                                        </td>
                                                        <td>${persona.documento}</td>
                                                        <td>
                                                            <c:if test="${persona.tipo == 2}">
                                                                <span class="label label-success"><spring:message code="persona.tipo.${persona.tipo}"/></span>
                                                            </c:if>
                                                            <c:if test="${persona.tipo == 3}">
                                                                <span class="label label-warning"><spring:message code="persona.tipo.${persona.tipo}"/></span>
                                                            </c:if>
                                                        </td>
                                                        <td class="center">
                                                            <a class="btn btn-warning btn-sm" href="<c:url value="/persona/${persona.id}/edit"/>" title="<spring:message code="regweb.editar"/>"><span class="fa fa-pencil"></span></a>
                                                            <a class="btn btn-danger btn-sm" onclick='javascript:confirm("<c:url value="/persona/${persona.id}/delete"/>","<spring:message code="regweb.confirmar.eliminacion" htmlEscape="true"/>")' href="javascript:void(0);" title="<spring:message code="regweb.eliminar"/>"><span class="fa fa-eraser"></span></a>
                                                        </td>
                                                    </tr>
                                                </c:forEach>

                                                </tbody>
                                            </table>

                                            <!-- Paginacion -->
                                            <c:import url="../modulos/paginacionBusqueda.jsp">
                                                <c:param name="entidad" value="persona"/>
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
     * Script per exportar les persones consultades a l'Excel
     * @param url
     * @param tipo
     * @param nombre
     * @param apellido1
     * @param apellido2
     * @param documento
     */
    function exportar(url,tipo,nombre,apellido1,apellido2,documento){
        if(tipo != ''){
            url = url + "?tipo="+tipo;
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
        goTo(url);
    }
</script>


</body>
</html>