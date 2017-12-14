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
                    <li class="active"><i class="fa fa-list-ul"></i> <spring:message code="catalogoDir3.ultima.sincronizacion"/></li>
                </ol>
            </div>
        </div><!-- /.row -->

        <div class="row">
            <div class="col-xs-12">

                <div class="panel panel-warning">

                    <div class="panel-heading">
                        <h3 class="panel-title"><i class="fa fa-list"></i> <strong><spring:message code="catalogoDir3.ultima.sincronizacion"/></strong></h3>
                    </div>

                    <div class="panel-body">

                        <c:import url="../modulos/mensajes.jsp"/>

                        <c:if test="${empty descarga}">
                            <div class="alert alert-grey alert-dismissable">
                                <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                                <strong><spring:message code="catalogoDir3.sincronizar.vacio"/></strong>
                            </div>
                        </c:if>

                        <c:if test="${not empty descarga}">

                            <div class="table-responsive">

                                <table class="table table-bordered table-hover table-striped tablesorter">
                                    <colgroup>
                                        <col>

                                    </colgroup>
                                    <thead>
                                    <tr>
                                        <th><spring:message code="catalogoDir3.sincronizar.fecha"/></th>
                                    </tr>
                                    </thead>

                                    <tbody>
                                            <tr>
                                                <td><fmt:formatDate pattern="dd/MM/yyyy" value="${descarga.fechaImportacion}" /></td>
                                            </tr>
                                    </tbody>
                                </table>

                            </div>

                        </c:if>



                        <c:if test="${not empty descarga}">
                           <input type="button" id="actuali" value="<spring:message code="catalogoDir3.actualizar"/>"  class="btn btn-success btn-sm"/>
                        </c:if>
                        <c:if test="${empty descarga}">
                          <input type="button" id="sincro" value="<spring:message code="catalogoDir3.sincronizar"/>"  class="btn btn-success btn-sm"/>
                        </c:if>
                        <input type="button" value="<spring:message code="regweb.cancelar"/>" onclick="goTo('<c:url value="/inici"/>')" class="btn btn-sm">

                    </div><!-- /.panel body -->

                </div>
            </div>
        </div>


    </div>
</div> <!-- /container -->

<spring:message code="regweb.procesando" var="textoModal" scope="request"/>
<c:import url="../modalSincro.jsp">
    <c:param name="textoModal" value="${textoModal}"/>
</c:import>
<c:import url="../modulos/pie.jsp"/>

<script type="text/javascript">
    $(document).ready(function() {
        $('#sincro').click(function(){
            $.ajax({
                url:'<c:url value="/dir3/sincronizarCatalogo"/>',
                type:'GET',
                beforeSend: function(objeto){
                    $('#modalSincro').modal('show');
                },
                complete:function(){
                    $('#modalSincro').modal('hide');
                    goTo('<c:url value="/dir3/datosCatalogo"/>');
                }
            });
        });
        $('#actuali').click(function(){
            $.ajax({
                url:'<c:url value="/dir3/actualizarCatalogo"/>',
                type:'GET',
                beforeSend: function(objeto){
                    $('#modalSincro').modal('show');
                },
                complete:function(){
                    $('#modalSincro').modal('hide');
                    goTo('<c:url value="/dir3/datosCatalogo"/>');
                }
            });
        });
    });
</script>

</body>
</html>