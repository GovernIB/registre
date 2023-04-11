<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

<!DOCTYPE html>
<html lang="ca">
<head>
    <title><spring:message code="regweb.titulo"/></title>
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
                    <c:import url="../modulos/migadepan.jsp"/>
                    <li class="active"><i class="fa fa-list-ul"></i> <spring:message code="informe.organismos"/></li>
                </ol>
            </div>
        </div><!-- /.row -->

        <c:import url="../modulos/mensajes.jsp"/>

        <!-- BUSCADOR -->
            <div class="row">

                <div class="col-xs-12">

                    <div class="panel panel-warning">
                        <div class="panel-heading">
                            <h3 class="panel-title"><i class="fa fa-search"></i> <strong><spring:message code="informe.organismos"/></strong> </h3>
                        </div>
                        <div class="panel-body">
                            <form:form modelAttribute="informeOrganismoBusquedaForm" method="post" cssClass="form-horizontal" name="informeOrganismoBusquedaForm" onsubmit="return validaFormulario(this)">

                                <div class="col-xs-12">
                                    <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                        <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                            <label for="tipo" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.tipoLibro.libroRegistro"/>" data-toggle="popover"><span class="text-danger">*</span> <spring:message code="informe.tipoLibro"/></label>
                                        </div>
                                        <div class="col-xs-8">
                                            <form:select path="tipo" cssClass="chosen-select" onchange="actualizarOrganismos(this.selectedIndex)">
                                                <form:option value="1" default="default"><spring:message code="registroSir.tipoRegistro.0"/></form:option>
                                                <form:option value="2"><spring:message code="registroSir.tipoRegistro.1"/></form:option>
                                            </form:select>
                                        </div>
                                    </div>
                                    <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                        <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                            <label for="formato" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.formato.libroRegistro"/>" data-toggle="popover"><span class="text-danger">*</span> <spring:message code="regweb.formato"/></label>
                                        </div>
                                        <div class="col-xs-8">
                                            <form:select path="formato" cssClass="chosen-select">
                                                <form:option value="pdf" default="default"><spring:message code="regweb.formato.pdf" /></form:option>
                                                <form:option value="excel"><spring:message code="regweb.formato.excel"/></form:option>
                                            </form:select>
                                        </div>
                                    </div>
                                </div>

                                <div class="col-xs-12">
                                    <div class="form-group col-xs-6 espaiLinies senseMargeLat organismos1">
                                        <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                            <label for="idOrganismo" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.libro.libroRegistro"/>" data-toggle="popover"><span class="text-danger">*</span> <spring:message code="organismo.organismos"/></label>
                                        </div>
                                        <div class="col-xs-8" id="organismosDiv">
                                            <c:if test="${fn:length(organismosConsulta) eq 1}">
                                                <form:select path="idOrganismo" items="${organismosConsulta}" itemValue="id" itemLabel="denominacion" cssClass="chosen-select" />
                                            </c:if>
                                            <c:if test="${fn:length(organismosConsulta) gt 1}">
                                                <spring:message code="informe.organismo.select" var="varOrganismosConsulta"/>
                                                <form:select data-placeholder="${varOrganismosConsulta}" path="idOrganismo" items="${organismosConsulta}" itemValue="id" itemLabel="denominacion" cssClass="chosen-select" />
                                            </c:if>
                                            <span id="organismosErrors"></span>
                                        </div>
                                    </div>
                                    <div class="form-group col-xs-6 espaiLinies senseMargeLat campos1">
                                        <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                            <label for="campos" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.campos.libroRegistro"/>" data-toggle="popover"><span class="text-danger">*</span> <spring:message code="regweb.campos"/></label>
                                        </div>
                                        <div class="col-xs-8" id="campos">
                                            <spring:message code="informe.campos" var="varCampos"/>
                                            <form:select data-placeholder="${varCampos}" multiple="true" cssClass="chosen-select" id="campos" path="campos" name="campos">
                                                <form:option value="llibr" selected="selected"><spring:message code="registroEntrada.libro"/></form:option>
                                                <form:option value="ofici" selected="selected"><spring:message code="registroEntrada.oficina"/></form:option>
                                                <form:option value="anyRe" selected="selected"><spring:message code="registroEntrada.anyRegistro"/></form:option>
                                                <form:option value="data" selected="selected"><spring:message code="registroEntrada.dataRegistre"/></form:option>
                                                <form:option value="numRe" selected="selected"><spring:message code="registroEntrada.numeroRegistro"/></form:option>
                                                <form:option value="extra" selected="selected"><spring:message code="registroEntrada.extracto"/></form:option>
                                                <%--<form:option value="tipAs" selected="selected"><spring:message code="registroEntrada.tipoAsunto"/></form:option>--%>
                                                <form:option value="nomIn" selected="selected"><spring:message code="registroEntrada.interesados"/></form:option>
                                                <form:option value="orgOr" selected="selected"><spring:message code="registroEntrada.oficinaOrigen"/></form:option>
                                                <form:option value="numOr" selected="selected"><spring:message code="registroEntrada.numeroRegistroOrigen"/></form:option>
                                                <form:option value="datOr" selected="selected"><spring:message code="registroEntrada.dataOrigen"/></form:option>
                                                <form:option value="orgDe" selected="selected"><spring:message code="registroEntrada.destinoOrigen"/></form:option>
                                                <form:option value="docFi" selected="selected"><spring:message code="registroEntrada.documentacionFisica"/></form:option>
                                                <form:option value="idiom" selected="selected"><spring:message code="registroEntrada.idioma"/></form:option>
                                                <form:option value="obser" selected="selected"><spring:message code="registroEntrada.observaciones"/></form:option>
                                                <form:option value="estat" selected="selected"><spring:message code="registroEntrada.estado"/></form:option>
                                                <form:option value="exped" selected="selected"><spring:message code="registroEntrada.expediente"/></form:option>
                                                <form:option value="codAs"><spring:message code="registroEntrada.codigoAsunto"/></form:option>
                                                <form:option value="refEx"><spring:message code="registroEntrada.referenciaExterna"/></form:option>
                                                <form:option value="trans"><spring:message code="registroEntrada.transporte"/></form:option>
                                                <form:option value="numTr"><spring:message code="registroEntrada.numTransporte"/></form:option>
                                                <form:option value="intMa"><spring:message code="interesado.email"/></form:option>
                                                <form:option value="aplic"><spring:message code="registroEntrada.aplicacion"/></form:option>
                                            </form:select>
                                            <span id="camposErrors"></span>
                                        </div>
                                    </div>
                                </div>

                                <div class="col-xs-12">
                                    <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                        <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                            <label for="fechaInicio" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.inicio.libroRegistro"/>" data-toggle="popover"><span class="text-danger">*</span> <spring:message code="informe.fechaInicio"/></label>
                                        </div>
                                        <div class="col-xs-8" id="fechaInicio">
                                            <div class="input-group date no-pad-right">
                                                <form:input type="text" cssClass="form-control" path="fechaInicio" maxlength="10" placeholder="dd/mm/yyyy" name="fechaInicio"/>
                                                <span class="input-group-addon"><span class="fa fa-calendar"></span></span>
                                            </div>
                                            <span class="errors"></span>
                                        </div>
                                    </div>
                                    <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                        <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                            <label for="fechaFin" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.fin.libroRegistro"/>" data-toggle="popover"><span class="text-danger">*</span> <spring:message code="informe.fechaFin"/></label>
                                        </div>
                                        <div class="col-xs-8" id="fechaFin">
                                            <div class="input-group date no-pad-right">
                                                <form:input type="text" cssClass="form-control" path="fechaFin" maxlength="10" placeholder="dd/mm/yyyy" name="fechaFin"/>
                                                <span class="input-group-addon"><span class="fa fa-calendar"></span></span>
                                            </div>
                                            <span class="errors"></span>
                                        </div>
                                    </div>
                                </div>

                                <%--Comprueba si debe mostrar las opciones desplegadas o no--%>
                                <c:if test="${empty informeOrganismoBusquedaForm.interessatDoc &&
                                empty informeOrganismoBusquedaForm.interessatNom &&
                                empty informeOrganismoBusquedaForm.organDestinatari &&
                                empty informeOrganismoBusquedaForm.observaciones &&
                                empty informeOrganismoBusquedaForm.usuario && !informeOrganismoBusquedaForm.anexos}">
                                  <div id="demo" class="collapse">
                                </c:if>
                                <c:if test="${not empty informeOrganismoBusquedaForm.interessatDoc ||
                                not empty informeOrganismoBusquedaForm.interessatNom ||
                                not empty informeOrganismoBusquedaForm.organDestinatari ||
                                not empty informeOrganismoBusquedaForm.observaciones ||
                                not empty informeOrganismoBusquedaForm.usuario || informeOrganismoBusquedaForm.anexos}">
                                  <div id="demo" class="collapse in">
                                </c:if>

                                    <div class="col-xs-12">
                                        <div class="col-xs-6 espaiLinies">
                                            <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                                <label for="numeroRegistroFormateado" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.numero.libroRegistro"/>" data-toggle="popover"><spring:message code="registroEntrada.numeroRegistro"/></label>
                                            </div>
                                            <div class="col-xs-8">
                                                <form:input path="numeroRegistroFormateado" cssClass="form-control"/> <form:errors path="numeroRegistroFormateado" cssClass="help-block" element="span"/>
                                            </div>
                                        </div>
                                        <div class="col-xs-6 espaiLinies">
                                            <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                                <label for="extracto" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.extracto.libroRegistro"/>" data-toggle="popover"><spring:message code="registroEntrada.extracto"/></label>
                                            </div>
                                            <div class="col-xs-8">
                                                <form:input path="extracto" cssClass="form-control" maxlength="200" /> <form:errors path="extracto" cssClass="help-block" element="span"/>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="col-xs-12">
                                        <div class="col-xs-6 espaiLinies">
                                            <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                                <label for="interessatNom" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.nombreInteresado.libroRegistro"/>" data-toggle="popover"><spring:message code="registroEntrada.nombreInteresado"/></label>
                                            </div>
                                            <div class="col-xs-8">
                                                <form:input  path="interessatNom" cssClass="form-control" maxlength="255"/>
                                                <form:errors path="interessatNom" cssClass="help-block" element="span"/>
                                            </div>
                                        </div>
                                        <div class="col-xs-6 espaiLinies">
                                            <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                                <label for="interessatLli1" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.apellido1.libroRegistro"/>" data-toggle="popover"><spring:message code="interesado.apellido1"/></label>
                                            </div>
                                            <div class="col-xs-8">
                                                <form:input path="interessatLli1" cssClass="form-control" maxlength="255"/>
                                                <form:errors path="interessatLli1" cssClass="help-block" element="span"/>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="col-xs-12">
                                        <div class="col-xs-6 espaiLinies">
                                            <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                                <label for="interessatLli2" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.apellido2.libroRegistro"/>" data-toggle="popover"><spring:message code="interesado.apellido2"/></label>
                                            </div>
                                            <div class="col-xs-8">
                                                <form:input path="interessatLli2" cssClass="form-control" maxlength="255"/>
                                                <form:errors path="interessatLli2" cssClass="help-block" element="span"/>
                                            </div>
                                        </div>
                                        <div class="col-xs-6 espaiLinies">
                                            <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                                <label for="interessatDoc" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.documento.libroRegistro"/>" data-toggle="popover"><spring:message code="registroEntrada.docInteresado"/></label>
                                            </div>
                                            <div class="col-xs-8">
                                                <form:input  path="interessatDoc" cssClass="form-control" maxlength="17"/>
                                                <form:errors path="interessatDoc" cssClass="help-block" element="span"/>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="col-xs-12">
                                        <div class="col-xs-6 espaiLinies">
                                            <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                                <label for="estado" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.estado.libroRegistro"/>" data-toggle="popover"><spring:message code="registroEntrada.estado"/></label>
                                            </div>
                                            <div class="col-xs-8">
                                                <form:select path="estado" cssClass="chosen-select">
                                                    <form:option value="-1" label="..."/>
                                                    <c:forEach var="estado" items="${estados}">
                                                        <form:option value="${estado}"><spring:message code="registro.estado.${estado}"/></form:option>
                                                    </c:forEach>
                                                </form:select>
                                            </div>
                                        </div>

                                        <div class="col-xs-6 espaiLinies">
                                            <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                                <label for="anexos" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.anexos.libroRegistro"/>" data-toggle="popover"><spring:message code="registroEntrada.anexos"/></label>
                                            </div>
                                            <div class="col-xs-8">
                                                <form:checkbox path="anexos"/>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="col-xs-12">
                                        <div class="col-xs-6 espaiLinies">
                                            <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                                <label for="idOficina" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.oficina.libroRegistro"/>" data-toggle="popover"><spring:message code="registro.oficinaRegistro"/></label>
                                            </div>
                                            <div class="col-xs-8">
                                                <form:select path="idOficina" cssClass="chosen-select">
                                                    <form:option value="-1" label="..."/>
                                                    <c:forEach var="oficinaRegistro" items="${oficinasRegistro}">
                                                        <form:option value="${oficinaRegistro.id}">${oficinaRegistro.denominacion}</form:option>
                                                    </c:forEach>
                                                </form:select>
                                            </div>
                                        </div>

                                        <div class="col-xs-6 espaiLinies">
                                            <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq" id="orgDest">
                                                <label for="organDestinatari" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.destino.libroRegistro"/>" data-toggle="popover"><spring:message code="registroEntrada.organDestinatari"/></label>
                                            </div>
                                            <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq hidden" id="orgOrig">
                                                <label for="organDestinatari" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.destino.libroRegistro"/>" data-toggle="popover"><spring:message code="registroEntrada.organismoOrigen"/></label>
                                            </div>
                                            <div class="col-xs-6">
                                                <form:select path="organDestinatari" cssClass="chosen-select" onchange="actualizarOrganDestinatariNom(${organismo.denominacion})">
                                                    <form:option value="" label="..."/>
                                                    <c:forEach items="${organosDestino}" var="organismo">
                                                        <option value="${organismo.codigo}" <c:if test="${registroEntradaBusqueda.organDestinatari == organismo.codigo}">selected="selected"</c:if>>${organismo.denominacion}</option>
                                                    </c:forEach>
                                                </form:select>
                                                <form:errors path="organDestinatari" cssClass="help-block" element="span"/>
                                                <form:hidden path="organDestinatariNom"/>
                                            </div>
                                            <div class="col-xs-2 padLateral5" id="busDest" >
                                                <a data-toggle="modal" role="button" href="#modalBuscadorlistaRegEntrada"
                                                   onclick="inicializarBuscador('#codNivelAdministracionlistaRegEntrada','#codComunidadAutonomalistaRegEntrada','#provincialistaRegEntrada','#localidadlistaRegEntrada',${RegwebConstantes.nivelAdminAutonomica}, ${RegwebConstantes.comunidadBaleares}, 'listaRegEntrada' );"
                                                   class="btn btn-warning btn-sm"><spring:message code="regweb.buscar"/></a>
                                            </div>

                                            <div class="col-xs-2 padLateral5 hidden" id="busOrig">
                                                <a data-toggle="modal" role="button" href="#modalBuscadorlistaRegEntrada"
                                                   onclick="inicializarBuscador('#codNivelAdministracionlistaRegEntrada','#codComunidadAutonomalistaRegEntrada','#provincialistaRegEntrada','#localidadlistaRegEntrada',${RegwebConstantes.nivelAdminAutonomica}, ${RegwebConstantes.comunidadBaleares}, 'listaRegEntrada' );"
                                                   class="btn btn-warning btn-sm"><spring:message code="regweb.buscar"/></a>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="col-xs-12">
                                        <div class="col-xs-6 espaiLinies">
                                            <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                                <label for="observaciones" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.observaciones.libroRegistro"/>" data-toggle="popover"><spring:message code="registroEntrada.observaciones"/></label>
                                            </div>
                                            <div class="col-xs-8">
                                                <form:input path="observaciones" class="form-control" type="text" value=""/>
                                            </div>
                                        </div>
                                        <div class="col-xs-6 espaiLinies">
                                            <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                                <label for="usuario" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.usuario.libroRegistro"/>" data-toggle="popover"><spring:message code="usuario.usuario"/></label>
                                            </div>
                                            <div class="col-xs-8">
                                                <form:select path="usuario" class="chosen-select">
                                                    <form:option value="">...</form:option>
                                                    <c:forEach items="${usuariosEntidad}" var="usuarioEntidad">
                                                        <option value="${usuarioEntidad.usuario.identificador}" <c:if test="${usuario == usuarioEntidad.usuario.identificador}">selected="selected"</c:if>>${usuarioEntidad.usuario.identificador}</option>
                                                    </c:forEach>
                                                </form:select>
                                            </div>
                                        </div>
                                    </div>

                                </div>
                                <div class="col-xs-12 pad-bottom15 mesOpcions">
                                    <a class="btn btn-warning btn-xs pull-right masOpciones-success" data-toggle="collapse" data-target="#demo">
                                            <%--Comprueba si debe mostrar mas opciones o menos--%>
                                        <c:if test="${empty registroEntradaBusqueda.registroEntrada.oficina.id && empty registroEntradaBusqueda.interessatDoc && empty registroEntradaBusqueda.interessatNom && empty registroEntradaBusqueda.organDestinatari && empty registroEntradaBusqueda.observaciones && empty registroEntradaBusqueda.usuario && !registroEntradaBusqueda.anexos}">
                                            <span class="fa fa-plus"></span> <spring:message code="regweb.busquedaAvanzada"/>
                                        </c:if>
                                        <c:if test="${not empty registroEntradaBusqueda.registroEntrada.oficina.id || not empty registroEntradaBusqueda.interessatDoc || not empty registroEntradaBusqueda.interessatNom || not empty registroEntradaBusqueda.organDestinatari || not empty registroEntradaBusqueda.observaciones || not empty registroEntradaBusqueda.usuario || registroEntradaBusqueda.anexos}">
                                            <span class="fa fa-minus"></span> <spring:message code="regweb.busquedaAvanzada"/>
                                        </c:if>
                                    </a>
                                </div>


                                <div class="form-group col-xs-12">
                                    <button type="submit" class="btn btn-warning btn-sm boto-informe"><spring:message code="regweb.buscar"/></button>
                                </div>

                            </form:form>
                    </div>
                </div>
            </div>
        </div>
    <!-- FIN BUSCADOR-->

    <!-- Importamos el codigo jsp del modal del formulario para realizar la búsqueda de organismos Destino o Origen
        Mediante el archivo "busquedaorganismo.js" se implementa dicha búsqueda -->


        <c:import url="../registro/buscadorOrganismosOficinasREPestanas.jsp">
            <c:param name="tipo" value="listaRegEntrada"/>
        </c:import>


    <%--<c:if test="${tipo == 2}">
        <c:import url="../registro/buscadorOrganismosOficinasREPestanas.jsp">
            &lt;%&ndash;<c:param name="tipo" value="listaRegSalida"/>&ndash;%&gt;
        </c:import>
    </c:if>--%>


</div>
</div> <!-- /container -->

<c:import url="../modulos/pie.jsp"/>


<!-- Modifica los Libros según el tipo de Registro elegido -->
<script type="text/javascript">

function actualizarOrganismos(tipo){
    <c:url var="obtenerOrganismos" value="/informe/obtenerOrganismos" />


    actualizarOrganismosTodos('${obtenerOrganismos}','#organismos',$('#tipo option:selected').val(),$('#organismos option:selected').val(),true);
    // Mostram el camp segons el tipus de registre que cercam
    if(tipo===0){
        $("#orgDest").removeClass("hidden");
        $("#orgOrig").addClass("hidden");
        $("#busDest").removeClass("hidden");
        $("#busOrig").addClass("hidden");
        $("#titulolistaRegEntrada").html("<spring:message code="organismo.buscador.listaRegEntrada"/>");
    }
    if(tipo===1){
        $("#orgDest").addClass("hidden");
        $("#orgOrig").removeClass("hidden");
        $("#busDest").addClass("hidden");
        $("#busOrig").removeClass("hidden");
        $("#titulolistaRegEntrada").html("<spring:message code="organismo.buscador.listaRegSalida"/>");

    }
}

</script>

<!-- VALIDADOR DE FORMULARI -->
<script type="text/javascript">

//Valida los libros seleccionados (libros, nombre del libro)
function organismosSeleccionados(select, camp) {
    var variable = '';
    var htmlBuit = '';
    // Valor de los libros
    var value = $(select).val();
    var numOrganismos = 0;
    if (value!=null && value!=""){
        // Número de los organismos en el select
        numOrganismos = value.length;
    }
    // Si hay menos de un libro seleccionado, retorna error
    if (numOrganismos<1){
        variable = "#" + camp + " span#organismosErrors";
        htmlBuit = "<span id='organismosErrors' class='help-block'>És obligatori elegir al manco 1 organisme</span>";
        $(variable).html(htmlBuit);
        $(variable).parents(".organismos1").addClass("has-error");
        $('ul.chosen-choices').css('border-color','#a94442');
        return false;
    }else{
        variable = "#" + camp + " span:contains('elegir')";
        $(variable).removeClass("help-block");
        $(variable).parents(".organismos1").removeClass("has-error");
        htmlBuit = "<span id='organismosErrors'></span>";
        $(variable).html(htmlBuit);
        $('ul.chosen-choices').css('border-color','#aaa');
        return true;
    }
}

//Valida los campos seleccionados (campos, nombre del campo)
function camposSeleccionados(select, camp) {
var variable = '';
var htmlBuit = '';
// Valor de los campos
var value = $(select).val();
var numCampos = 0;
if (value!=null && value!=""){
    // Número de los campos en el select
    numCampos = value.length;
}
// Si hay menos de dos campos seleccionados, retorna error
if (numCampos<2){
    variable = "#" + camp + " span#camposErrors";
    htmlBuit = "<span id='camposErrors' class='help-block'>És obligatori elegir almanco 2 camps</span>";
    $(variable).html(htmlBuit);
    $(variable).parents(".campos1").addClass("has-error");
    $('ul.chosen-choices').css('border-color','#a94442');
    return false;
}else{
    variable = "#" + camp + " span:contains('elegir')";
    $(variable).removeClass("help-block");
    $(variable).parents(".campos1").removeClass("has-error");
    htmlBuit = "<span id='camposErrors'></span>";
    $(variable).html(htmlBuit);
    $('ul.chosen-choices').css('border-color','#aaa');
    return true;
}
}

// Valida el formuario si las fechas Inicio y Fin son correctas, hay almenos 2 campos seleccionados, hay un Libro seleccionado
function validaFormulario(form) {
    var fechaInicio = true;
    var fechaFin = true;
    var organismos = true;
    var campos = true;
    var fechas = true;
    // Valida el formato de Fecha de Inicio
    if (!validaFecha(form.fechaInicio, 'fechaInicio')) {
        fechaInicio = false;
    }
    // Valida el formato de Fecha de Fin
    if (!validaFecha(form.fechaFin, 'fechaFin')) {
        fechaFin = false;
    }
    // Si las Fechas son correctas, Valida el Fecha Inicio y Fecha Fin menor o igual que fecha actual, Fecha Inicio menor o igual que Fecha Fin
    if((fechaInicio)&&(fechaFin)){
        if (!validaFechasConjuntas(form.fechaInicio, form.fechaFin, 'fechaInicio', 'fechaFin')) {
            fechas = false;
        }
    }
    // Valida los libros seleccionados
    if (!organismosSeleccionados(form.idOrganismo, 'organismosDiv')){
        organismos = false;
    }
    // Valida los campos seleccionados
    if (!camposSeleccionados(form.campos, 'campos')){
        campos = false;
    }
    // Si todos los campos son correctos, hace el submit
    return (fechaInicio) && (fechaFin) && (organismos) && (campos) && (fechas);
}

</script>

<script type="text/javascript">
function actualizarOrganismosTodos(url, idSelect, seleccion, valorSelected, todos){
    var html = '';
    if(seleccion != '-1'){
        jQuery.ajax({
            url: url,
            type: 'GET',
            dataType: 'json',
            data: { id: seleccion },
            contentType: 'application/json',
            success: function(result) {
                if(todos){html = '';}
                var len = result.length;
                var selected='';
                for ( var i = 0; i < len; i++) {
                    selected='';
                    if(result.length == 1){
                        selected = 'selected="selected"';
                    }
                    html += '<option '+selected+' value="' + result[i].id + '">'
                    + result[i].denominacion + '</option>';
                }
                html += '</option>';

                if(len != 0){
                    $(idSelect).html(html);
                    $(idSelect).attr("disabled",false).trigger("chosen:updated");
                }else if(len==0){
                    var html='';
                    $(idSelect).html(html);
                    $(idSelect).attr("disabled",true).trigger("chosen:updated");
                }
            }
        });

    }
}
</script>

<!-- Cambia la imagen de la búsqueda avanzada-->
<script>
    var traduccion = new Array();
    traduccion['regweb.busquedaAvanzada'] = "<spring:message code='regweb.busquedaAvanzada' javaScriptEscape='true' />";

    $(function(){
        $("#demo").on("hide.bs.collapse", function(){
            $(".masOpciones-success").html('<span class="fa fa-plus"></span> ' + traduccion['regweb.busquedaAvanzada']);
        });
        $("#demo").on("show.bs.collapse", function(){
            $(".masOpciones-success").html('<span class="fa fa-minus"></span> ' + traduccion['regweb.busquedaAvanzada']);
        });
    });
</script>

<!-- Cambia el valor del Nombre del Organismo Destinatario-->
<script>
    function actualizarOrganDestinatariNom(denominacio){
        $("#organDestinatariNom").html(denominacio);
    }
</script>

</body>
</html>