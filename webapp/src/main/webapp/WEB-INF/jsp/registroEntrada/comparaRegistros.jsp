<%@ page import="es.caib.regweb3.model.RegistroEntrada" %>
<%@ page import="es.caib.regweb3.persistence.utils.RegistroUtils" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

<div id="modalCompararRegistros" class="modal fade" >

    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                <h3><spring:message code="registro.comparar"/></h3>
            </div>

            <div class="modal-body" >
                <div class="row">
                   <!-- Registro actual -->
                   <div class="col-xs-6">

                       <div class="panel panel-info">

                           <div class="panel-heading">
                               <h3 class="panel-title"><i class="fa fa-pencil-square"></i> <strong><spring:message code="registro.actual"/></strong></h3>
                           </div>

                           <div class="panel-body">

                               <div class="form-group col-xs-12">
                                   <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                                       <label><spring:message code="registroEntrada.extracto"/></label>
                                   </div>
                                   <div class="col-xs-8">${registro.registroDetalle.extracto}</div>
                               </div>

                               <div class="form-group col-xs-12">
                                   <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                                       <label><spring:message code="registroEntrada.documentacionFisica"/></label>
                                   </div>
                                   <div class="col-xs-8">
                                       <c:if test="${not empty registro.registroDetalle.tipoDocumentacionFisica}">
                                           <!-- Pone el color que corresponde con el el Tipo de documentacion elegido -->
                                            <c:if test="${registro.registroDetalle.tipoDocumentacionFisica==RegwebConstantes.TIPO_DOCFISICA_ACOMPANYA_DOC_REQUERIDA}">
                                                <p class="text-vermell"><spring:message code="tipoDocumentacionFisica.${registro.registroDetalle.tipoDocumentacionFisica}" /></p>
                                            </c:if>
                                            <c:if test="${registro.registroDetalle.tipoDocumentacionFisica==RegwebConstantes.TIPO_DOCFISICA_ACOMPANYA_DOC_COMPLEMENTARIA}">
                                               <p class="text-taronja"><spring:message code="tipoDocumentacionFisica.${registro.registroDetalle.tipoDocumentacionFisica}" /></p>
                                            </c:if>
                                            <c:if test="${registro.registroDetalle.tipoDocumentacionFisica==RegwebConstantes.TIPO_DOCFISICA_NO_ACOMPANYA_DOC}">
                                               <p class="text-verd"><spring:message code="tipoDocumentacionFisica.${registro.registroDetalle.tipoDocumentacionFisica}" /></p>
                                            </c:if>
                                       </c:if>
                                   </div>
                               </div>

                               <div class="form-group col-xs-12">
                                   <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                                       <label><spring:message code="registroEntrada.organismoDestino"/></label>
                                   </div>
                                   <div class="col-xs-8">
                                       <c:if test="${not empty registro.destino}">${registro.destino.denominacion}</c:if>
                                       <c:if test="${empty registro.destino}">${registro.destinoExternoDenominacion}</c:if>
                                   </div>
                               </div>

                               <c:if test="${not empty registro.registroDetalle.tipoAsunto}">
                                   <div class="form-group col-xs-12">
                                       <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                                           <label><spring:message code="registroEntrada.tipoAsunto"/></label>
                                       </div>
                                       <div class="col-xs-8">
                                           <c:if test="${not empty registro.registroDetalle.tipoAsunto}">
                                               <td>${registro.registroDetalle.tipoAsunto.traducciones[pageContext.response.locale.language].nombre}</td>
                                           </c:if>
                                       </div>
                                   </div>
                               </c:if>

                               <c:if test="${not empty registro.registroDetalle.idioma}">
                                   <div class="form-group col-xs-12">
                                       <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                                           <label><spring:message code="registroEntrada.idioma"/></label>
                                       </div>
                                       <div class="col-xs-8">
                                           <spring:message code="idioma.${registro.registroDetalle.idioma}"/>
                                       </div>
                                   </div>
                               </c:if>

                               <c:if test="${not empty registro.registroDetalle.codigoAsunto}">
                                  <div class="form-group col-xs-12">
                                      <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                                          <label><spring:message code="registroEntrada.codigoAsunto"/></label>
                                      </div>
                                      <div class="col-xs-8">
                                              ${registro.registroDetalle.codigoAsunto.traducciones[pageContext.response.locale.language].nombre}
                                      </div>
                                  </div>
                               </c:if>

                              <div class="form-group col-xs-12">
                                  <c:if test="${not empty registro.registroDetalle.referenciaExterna}">
                                      <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                                          <label><spring:message code="registroEntrada.referenciaExterna"/></label>
                                      </div>
                                      <div class="col-xs-4">${registro.registroDetalle.referenciaExterna}</div>
                                  </c:if>

                                  <c:if test="${not empty registro.registroDetalle.expediente}">
                                      <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                                          <label><spring:message code="registroEntrada.expediente"/></label>
                                      </div>
                                      <div class="col-xs-4">${registro.registroDetalle.expediente}</div>
                                  </c:if>

                              </div>
                               <c:if test="${not empty registro.registroDetalle.transporte}">
                                   <div class="form-group col-xs-12">
                                       <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                                           <label><spring:message code="registroEntrada.transporte"/></label>
                                       </div>
                                       <div class="col-xs-6">
                                           <spring:message code="transporte.0${registro.registroDetalle.transporte}" />
                                       </div>
                                       <div class="col-xs-4">${registro.registroDetalle.numeroTransporte}</div>
                                   </div>
                               </c:if>

                               <c:if test="${not empty registro.registroDetalle.observaciones}">
                                   <div class="form-group col-xs-12">
                                       <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                                           <label><spring:message code="registroEntrada.observaciones"/></label>
                                       </div>
                                       <div class="col-xs-8">${registro.registroDetalle.observaciones}</div>
                                   </div>
                               </c:if>

                               <c:if test="${not empty registro.registroDetalle.oficinaOrigen || not empty registro.registroDetalle.oficinaOrigenExternoCodigo}">
                                   <div class="form-group col-xs-12">
                                       <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                                           <label><spring:message code="registroEntrada.oficinaOrigen"/></label>
                                       </div>
                                       <div class="col-xs-8">
                                           <c:if test="${not empty registro.registroDetalle.oficinaOrigen}">
                                               ${registro.registroDetalle.oficinaOrigen.denominacion}
                                           </c:if>
                                           <c:if test="${empty registro.registroDetalle.oficinaOrigen}">
                                               ${registro.registroDetalle.oficinaOrigenExternoDenominacion}
                                           </c:if>
                                       </div>
                                   </div>
                               </c:if>

                               <c:if test="${not empty registro.registroDetalle.numeroRegistroOrigen}">
                                   <div class="form-group col-xs-6">
                                       <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                                           <label><spring:message code="registroEntrada.numeroRegistroOrigen"/></label>
                                       </div>
                                       <div class="col-xs-8">${registro.registroDetalle.numeroRegistroOrigen}</div>
                                   </div>
                               </c:if>
                               <c:if test="${not empty registro.registroDetalle.fechaOrigen}">
                                   <div class="form-group col-xs-6 no-pad-right">
                                       <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                                           <label><spring:message code="registroEntrada.fecha"/></label>
                                       </div>
                                       <div class="col-xs-8 no-pad-right" >
                                           <fmt:formatDate value="${registro.registroDetalle.fechaOrigen}" pattern="dd/MM/yyyy HH:mm:ss"/>
                                       </div>

                                   </div>
                               </c:if>

                               <c:if test="${not empty registro.registroDetalle.interesados}">
                                   <div class="form-group  col-xs-12">
                                       <table class="table table-bordered table-hover table-striped">
                                           <thead>
                                               <tr>
                                                   <th><spring:message code="registroEntrada.interesado"/></th>
                                                   <th><spring:message code="interesado.tipoInteresado.corto"/></th>
                                                   <th><spring:message code="representante.representante"/></th>
                                               </tr>
                                           </thead>

                                           <tbody>
                                               <c:forEach var="interesado" items="${registro.registroDetalle.interesados}">
                                                   <tr>
                                                       <td>
                                                           <c:if test="${interesado.tipo == RegwebConstantes.TIPO_INTERESADO_ADMINISTRACION}">${interesado.nombreOrganismo} </c:if>
                                                           <c:if test="${interesado.tipo == RegwebConstantes.TIPO_INTERESADO_PERSONA_FISICA}">${interesado.nombrePersonaFisica} </c:if>
                                                           <c:if test="${interesado.tipo == RegwebConstantes.TIPO_INTERESADO_PERSONA_JURIDICA}">${interesado.nombrePersonaJuridica} </c:if>
                                                       </td>
                                                       <td>
                                                           <c:if test="${interesado.tipo == RegwebConstantes.TIPO_INTERESADO_ADMINISTRACION}"><spring:message code="organismo.organismo"/></c:if>
                                                           <c:if test="${interesado.tipo == RegwebConstantes.TIPO_INTERESADO_PERSONA_FISICA}"><spring:message code="persona.fisica"/></c:if>
                                                           <c:if test="${interesado.tipo == RegwebConstantes.TIPO_INTERESADO_PERSONA_JURIDICA}"><spring:message code="persona.juridica"/></c:if>
                                                       </td>
                                                       <td>
                                                           <c:if test="${interesado.isRepresentante}">
                                                               <span class="label label-success">Si, <spring:message code="representante.representante"/>: ${interesado.representado.nombreCompleto}</span>
                                                           </c:if>

                                                           <c:if test="${!interesado.isRepresentante}">
                                                               <span class="label label-danger">No</span>
                                                           </c:if>
                                                       </td>
                                                   </tr>
                                               </c:forEach>
                                           </tbody>
                                       </table>
                                   </div>
                               </c:if>
                           </div>
                       </div>
                   </div>

                   <!-- Registro anterior -->
                   <c:forEach var="historico" items="${historicos}" varStatus="status">

                       <c:if test="${not empty historico.registroEntradaOriginal}">

                           <c:set var="reOriginal" value="${historico.registroEntradaOriginal}" scope="page"/>

                           <%
                               RegistroEntrada reOrig  = RegistroUtils.desSerilizarREXml((String) pageContext.getAttribute("reOriginal"));
                               pageContext.setAttribute("reOriginal",reOrig);
                           %>

                           <div class="col-xs-6" id="${historico.id}">

                               <div class="panel panel-warning">

                                   <div class="panel-heading">
                                       <h3 class="panel-title"><i class="fa fa-pencil-square-o"></i> <strong><spring:message code="registro.anterior"/></strong></h3>
                                   </div>

                                   <div class="panel-body">

                                       <div class="form-group  col-xs-12">
                                           <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                                               <label><spring:message code="registroEntrada.libro"/></label>
                                           </div>
                                           <div class="col-xs-8">${reOriginal.libro.nombre}</div>
                                       </div>

                                       <div class="form-group col-xs-12">
                                           <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                                               <label><spring:message code="registroEntrada.extracto"/></label>
                                           </div>
                                           <div class="col-xs-8">${reOriginal.registroDetalle.extracto}</div>
                                       </div>

                                       <c:if test="${not empty reOriginal.registroDetalle.tipoDocumentacionFisica}">
                                           <div class="form-group col-xs-12">
                                               <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                                                   <label><spring:message code="registroEntrada.documentacionFisica"/></label>
                                               </div>
                                               <!-- Pone el color que corresponde con el el Tipo de documentacion elegido -->
                                               <c:if test="${reOriginal.registroDetalle.tipoDocumentacionFisica==RegwebConstantes.TIPO_DOCFISICA_ACOMPANYA_DOC_REQUERIDA}">
                                                   <div class="col-xs-8 text-vermell" id="tipoDocumentacionFisica_${status.count}_${reOriginal.registroDetalle.tipoDocumentacionFisica}">
                                                       <spring:message code="tipoDocumentacionFisica.${reOriginal.registroDetalle.tipoDocumentacionFisica}"/>
                                                   </div>
                                               </c:if>
                                               <c:if test="${reOriginal.registroDetalle.tipoDocumentacionFisica==RegwebConstantes.TIPO_DOCFISICA_ACOMPANYA_DOC_COMPLEMENTARIA}">
                                                   <div class="col-xs-8 text-taronja" id="tipoDocumentacionFisica_${status.count}_${reOriginal.registroDetalle.tipoDocumentacionFisica}">
                                                       <spring:message code="tipoDocumentacionFisica.${reOriginal.registroDetalle.tipoDocumentacionFisica}"/>
                                                   </div>
                                               </c:if>
                                               <c:if test="${reOriginal.registroDetalle.tipoDocumentacionFisica==RegwebConstantes.TIPO_DOCFISICA_NO_ACOMPANYA_DOC}">
                                                   <div class="col-xs-8 text-verd" id="tipoDocumentacionFisica_${status.count}_${reOriginal.registroDetalle.tipoDocumentacionFisica}">
                                                       <spring:message code="tipoDocumentacionFisica.${reOriginal.registroDetalle.tipoDocumentacionFisica}"/>
                                                   </div>
                                               </c:if>
                                           </div>
                                       </c:if>

                                       <div class="form-group col-xs-12">
                                           <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                                               <label><spring:message code="registroEntrada.organismoDestino"/></label>
                                           </div>
                                           <div class="col-xs-8">
                                               <c:if test="${not empty reOriginal.destino}">${reOriginal.destino.denominacion}</c:if>
                                               <c:if test="${empty reOriginal.destino}">${reOriginal.destinoExternoDenominacion}</c:if>
                                           </div>

                                       </div>
                                       <c:if test="${not empty reOriginal.registroDetalle.tipoAsunto}">
                                           <div class="form-group col-xs-12">
                                               <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                                                   <label><spring:message code="registroEntrada.tipoAsunto"/></label>
                                               </div>
                                               <div class="col-xs-8" id="tipoAsunto_${status.count}_${reOriginal.registroDetalle.tipoAsunto.id}">
                                                   <script type="text/javascript">
                                                       $(document).ready(function(){
                                                           obtenerElementoTraducido('<c:url value="/rest/obtenerTipoAsunto"/>', '${reOriginal.registroDetalle.tipoAsunto.id}', 'tipoAsunto_${status.count}_${reOriginal.registroDetalle.tipoAsunto.id}');
                                                       });
                                                   </script>
                                               </div>
                                           </div>
                                       </c:if>

                                       <c:if test="${not empty reOriginal.registroDetalle.idioma && not empty reOriginal.registroDetalle.idioma}">
                                           <div class="form-group col-xs-12">
                                               <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                                                   <label><spring:message code="registroEntrada.idioma"/></label>
                                               </div>
                                               <div class="col-xs-8"><spring:message code="idioma.${reOriginal.registroDetalle.idioma}"/></div>
                                           </div>
                                       </c:if>

                                       <c:if test="${not empty reOriginal.registroDetalle.codigoAsunto}">
                                           <div class="form-group col-xs-12">
                                               <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                                                   <label><spring:message code="registroEntrada.codigoAsunto"/></label>
                                               </div>
                                               <div class="col-xs-8" id="codigoAsunto_${status.count}_${reOriginal.registroDetalle.codigoAsunto.id}">
                                                   <script type="text/javascript">
                                                       $(document).ready(function(){
                                                           obtenerElementoTraducido('<c:url value="/rest/obtenerCodigoAsunto"/>', '${reOriginal.registroDetalle.codigoAsunto.id}', 'codigoAsunto_${status.count}_${reOriginal.registroDetalle.codigoAsunto.id}');
                                                       });
                                                   </script>
                                               </div>
                                           </div>
                                       </c:if>

                                       <div class="form-group col-xs-12">
                                           <c:if test="${not empty reOriginal.registroDetalle.referenciaExterna}">
                                               <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                                                   <label><spring:message code="registroEntrada.referenciaExterna"/></label>
                                               </div>
                                               <div class="col-xs-4">${reOriginal.registroDetalle.referenciaExterna}</div>
                                           </c:if>


                                       <c:if test="${not empty reOriginal.registroDetalle.expediente}">
                                           <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                                            <label><spring:message code="registroEntrada.expediente"/></label>
                                           </div>
                                           <div class="col-xs-4">${reOriginal.registroDetalle.expediente}</div>
                                       </c:if>

                                       </div>

                                       <c:if test="${not empty reOriginal.registroDetalle.transporte}">
                                           <div class="form-group col-xs-12">
                                               <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                                                   <label><spring:message code="registroEntrada.transporte"/></label>
                                               </div>
                                               <div class="col-xs-6" id="transporte_${status.count}_${reOriginal.registroDetalle.transporte}">
                                                   <spring:message code="transporte.0${reOriginal.registroDetalle.transporte}" />
                                               </div>
                                               <div class="col-xs-4">${reOriginal.registroDetalle.numeroTransporte}</div>
                                           </div>
                                       </c:if>

                                       <c:if test="${not empty reOriginal.registroDetalle.observaciones}">
                                           <div class="form-group col-xs-12">
                                               <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                                                   <label><spring:message code="registroEntrada.observaciones"/></label>
                                               </div>
                                               <div class="col-xs-8">${reOriginal.registroDetalle.observaciones}</div>
                                           </div>
                                       </c:if>

                                       <c:if test="${not empty reOriginal.registroDetalle.oficinaOrigen || not empty reOriginal.registroDetalle.oficinaOrigenExternoCodigo}">
                                           <div class="form-group col-xs-12">
                                               <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                                                   <label><spring:message code="registroEntrada.oficinaOrigen"/></label>
                                               </div>
                                               <div class="col-xs-8">
                                                   <c:if test="${not empty reOriginal.registroDetalle.oficinaOrigen}">
                                                       ${reOriginal.registroDetalle.oficinaOrigen.denominacion}
                                                   </c:if>
                                                   <c:if test="${empty reOriginal.registroDetalle.oficinaOrigen}">
                                                       ${reOriginal.registroDetalle.oficinaOrigenExternoDenominacion}
                                                   </c:if>
                                               </div>
                                           </div>
                                       </c:if>

                                       <c:if test="${not empty reOriginal.registroDetalle.numeroRegistroOrigen}">
                                           <div class="form-group col-xs-6">
                                               <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                                                   <label><spring:message code="registroEntrada.numeroRegistroOrigen"/></label>
                                               </div>
                                               <div class="col-xs-8">${reOriginal.registroDetalle.numeroRegistroOrigen}</div>
                                           </div>
                                       </c:if>

                                       <c:if test="${not empty reOriginal.registroDetalle.fechaOrigen}">
                                           <div class="form-group col-xs-6 no-pad-right">
                                               <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                                                   <label><spring:message code="registroEntrada.fecha"/></label>
                                               </div>
                                               <div class="col-xs-8 no-pad-right" >
                                                   <fmt:formatDate value="${reOriginal.registroDetalle.fechaOrigen}" pattern="dd/MM/yyyy HH:mm:ss"/>
                                               </div>

                                           </div>
                                       </c:if>

                                       <c:if test="${not empty reOriginal.registroDetalle.interesados}">
                                           <div class="form-group  col-xs-12">
                                               <table class="table table-bordered table-hover table-striped">
                                                   <thead>
                                                       <tr>
                                                           <th><spring:message code="registroEntrada.interesado"/></th>
                                                           <th><spring:message code="interesado.tipoInteresado.corto"/></th>
                                                           <th><spring:message code="representante.representante"/></th>
                                                       </tr>
                                                   </thead>
                                                   <tbody>
                                                       <c:forEach var="interesado" items="${reOriginal.registroDetalle.interesados}">
                                                           <tr>
                                                               <td>
                                                                   <c:if test="${interesado.tipo == RegwebConstantes.TIPO_INTERESADO_ADMINISTRACION}">${interesado.nombreOrganismo} </c:if>
                                                                   <c:if test="${interesado.tipo == RegwebConstantes.TIPO_INTERESADO_PERSONA_FISICA}">${interesado.nombrePersonaFisica} </c:if>
                                                                   <c:if test="${interesado.tipo == RegwebConstantes.TIPO_INTERESADO_PERSONA_JURIDICA}">${interesado.nombrePersonaJuridica} </c:if>
                                                               </td>
                                                               <td>
                                                                   <c:if test="${interesado.tipo == RegwebConstantes.TIPO_INTERESADO_ADMINISTRACION}"><spring:message code="organismo.organismo"/></c:if>
                                                                   <c:if test="${interesado.tipo == RegwebConstantes.TIPO_INTERESADO_PERSONA_FISICA}"><spring:message code="persona.fisica"/></c:if>
                                                                   <c:if test="${interesado.tipo == RegwebConstantes.TIPO_INTERESADO_PERSONA_JURIDICA}"><spring:message code="persona.juridica"/></c:if>
                                                               </td>
                                                               <td>
                                                                   <c:if test="${interesado.isRepresentante}">
                                                                       <span class="label label-success">Si, <spring:message code="representante.representante"/>: ${interesado.representado.nombreCompleto}</span>
                                                                   </c:if>

                                                                   <c:if test="${!interesado.isRepresentante}">
                                                                       <span class="label label-danger">No</span>
                                                                   </c:if>
                                                               </td>
                                                           </tr>
                                                       </c:forEach>
                                                   </tbody>
                                               </table>
                                           </div>
                                       </c:if>
                                   </div>
                               </div>
                           </div>
                       </c:if>
                   </c:forEach>
                </div>
            </div>
        </div>
    </div>
</div>
