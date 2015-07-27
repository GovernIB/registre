/*
 Funciones especficas para la búsqueda de organismo y oficinas en un registro de entrada.
 En función del tipoOrganismo indicado, se realizará búsqueda de organismos u oficinas.
 */


// Muestra la informacion de los Popover de la lista de Organismos
$('a[rel=popover]').popover();

mostraInformacio = function() {
    var zIndices = new Array();
    $('div.popover, div.modal').each( function() {
        zIndices.push($(this).attr('class') + '; z-index: ' + $(this).css('z-index'));
    });
    var html = zIndices.join('<br>');
    $('.show-z-index').html(html);
};

$('body').on('hover', 'a[rel=popover]', mostraInformacio);


/*
 Función que pinta una tabla con los resultados obtenidos de la búsqueda.
 @param tipoOrganismo indica desde donde se realiza la búsqueda para poder asignar
 los resultados al select que corresponde.
 */

function organismoBusqueda(tipoOrganismo, urlServidor,idRegistroDetalle){
	
      // obtenemos los valores del formulario para realizar la búsqueda.
      var denominacion = $('#denominacion'+tipoOrganismo).val();
      var codigoOrganismo= $('#codigoOrganismo'+tipoOrganismo).val();
      var codNivelAdministracion= $('#codNivelAdministracion'+tipoOrganismo).val();
      var codComunidadAutonoma= $('#codComunidadAutonoma'+tipoOrganismo).val();
      // indica si queremos obtener aquellos organismos que son unidad Raiz
      var unidadRaiz=false;
      // indica que queremos obtener de la búsqueda de organismos aquellos que tienen oficinas
      //de registro
      var conOficinas= true;


      // Variables configurables en función del tipo de organismo indicado.
      var idSelect= '';
      var idDenominacion= '';
      var url = '';
      /* REGISTRO ENTRADA */
      // Caso de organismo Destino
      if(tipoOrganismo == 'OrganismoDestino'){
        idSelect = "#destino\\\\.codigo";
        idDenominacion = "#destino\\\\.denominacion";
        url = urlServidor+"/rest/busqueda/organismos";
      }
      // Caso de oficina origen
      if(tipoOrganismo == 'OficinaOrigen'){
        idSelect = "#registroDetalle\\\\.oficinaOrigen\\\\.codigo";
        idDenominacion = "#registroDetalle\\\\.oficinaOrigen\\\\.denominacion";
        url = urlServidor+"/rest/busqueda/oficinas";
      }
      // Caso administracion interesado
      if(tipoOrganismo == 'OrganismoInteresado'){
        conOficinas = false;
        unidadRaiz= $('#unidadRaiz'+tipoOrganismo).prop('checked');
        idSelect = "#registroDetalle\\\\.organismoInteresado\\\\.codigo";
        idDenominacion = "#registroDetalle\\\\.organismoInteresado\\\\.denominacion";
        url = urlServidor+"/rest/busqueda/organismos";
      }
      /* REGISTRO SALIDA */
      // Caso Organismo Origen de registro salida
      if(tipoOrganismo == 'OrganismoOrigen'){
    	  idSelect = "#origen\\\\.codigo";
    	  idDenominacion = "#origen\\\\.denominacion";
    	  url = urlServidor+"/rest/busqueda/organismos";
      }

      if (tipoOrganismo == 'listaRegEntrada') {
          idSelect = "#organDestinatari";
          idDenominacion = "#organDestinatariNom";
          url = urlServidor+"/rest/busqueda/organismos";
      }
      
      if (tipoOrganismo == 'listaRegSalida') {
          idSelect = "#organOrigen";
          idDenominacion = "#organOrigenNom";
          url = urlServidor+"/rest/busqueda/organismos";
      }

      // Inicializamos el div del resultado de busqueda.
      $('#resultadosbusqueda'+tipoOrganismo).empty();
      $('#resultadosbusqueda'+tipoOrganismo).html('');
      $('#resultadosbusqueda'+tipoOrganismo).attr("display:none");


      // Definimos la tabla que contendrá los resultados.
      var idTablaResultados = "tresultadosbusqueda"+ tipoOrganismo;
      var table = $('<table id="'+idTablaResultados+'"></table>').addClass('paginated table table-bordered table-hover table-striped tablesorter ');
      table.append('<colgroup><col><col width="50"></colgroup>');


      table.append('<thead><tr><th>'+tradorganismo['organismo.denominacion']+'</th><th>'+tradorganismo['regweb3.acciones']+'</th></tr></thead><tbody></tbody>');

      //Mostram la imatge de reload
      $('#reloadorg'+tipoOrganismo).show();
      // realizamos la busqueda en dir3 mediante REST.
      $.ajax({
            async: false,
            crossDomain: true,
            url: url,
            type: 'GET',
            dataType: 'json',
            data: { codigo: codigoOrganismo, denominacion: denominacion, codNivelAdministracion: codNivelAdministracion, codComunidadAutonoma: codComunidadAutonoma, conOficinas: conOficinas, unidadRaiz:unidadRaiz },
            success: function(result) {

               $('#resultadosbusqueda'+tipoOrganismo).css('display', 'block');
               var len = result.length;
               for ( var i = 0; i < len; i++) {
                  var denominacion = '';
                  var codigo = '';


                 // Sustituimos comillas simples y dobles para evitar problemas en la llamada
                 // a la función posterior
                 denominacion = result[i].denominacion.replace(/\"/g,'&quot;');
                 denominacion = denominacion.replace(/'/g, "\\'");
                 codigo = result[i].codigo;

                 var title = $('#organismo_raiz').val()+": "+result[i].unidadRaiz+" || "+$('#organismo_superior').val()+": "+result[i].unidadSuperior;

                 // definimos el contenido de la tabla en función de los resultados de la busqueda.
                 if(tipoOrganismo == 'OrganismoInteresado'){

                     var linea ="<tr><td style=\"text-align:left;\"><label rel=\"popover\" class=\"no-bold text-gris\" style=\"cursor: pointer;\" title=\""+title+"\">"+result[i].denominacion+"</label></td><td class=\"center\"><input type=\"button\" class=\"btn btn-sm\" value=\"Seleccionar\" onclick=\"addAdministracionInteresadosModal('"+codigo+"','"+denominacion+"','Administración','"+tipoOrganismo+"','"+idRegistroDetalle+"')\"/></td></tr>";

                 }else{

                     var linea ="<tr><td style=\"text-align:left;\"><label rel=\"popover\" class=\"no-bold text-gris\" style=\"cursor: pointer;\" title=\""+title+"\">"+result[i].denominacion+"</label></td><td class=\"center\"><input type=\"button\" class=\"btn btn-sm\" value=\"Seleccionar\" onclick=\"asignarOrganismo('"+codigo+"','"+denominacion+"','"+idSelect+"','"+idDenominacion+"','"+tipoOrganismo+"')\"/></td></tr>";

                 }

                 table.append(linea);
               }
                // Ocultamos imagen reload
                $('#reloadorg'+tipoOrganismo).hide();
                // Se muestra solo si hay resultados
                if(len != 0){
                    $('#resultadosbusqueda'+tipoOrganismo).attr("display:block");
                    $('#resultadosbusqueda'+tipoOrganismo).append(table);
                }else if(len==0){
                    //$('#resultadosbusqueda'+tipoOrganismo).attr("display:none");
                    $('#resultadosbusqueda'+tipoOrganismo).html('<br/><div class="alert alert-warning" style="text-align:left;">No se han encontrado resultados</div>');
                }

                // mostramos el listado paginado.
                $('#tresultadosbusqueda'+tipoOrganismo,'td').each(function(i) {
                           $(this).text(i+1);
                });

                $('#tresultadosbusqueda'+tipoOrganismo).each(function() {

                           var currentPage = 0;
                           var numPerPage = 10;
                           var $table =  $(this);
                           $table.bind('repaginate', function() {
                               $table.find('tbody tr').hide().slice(currentPage * numPerPage, (currentPage + 1) * numPerPage).show();
                           });
                           $table.trigger('repaginate');
                           var numRows = $table.find('tbody tr').length;
                           var numPages = Math.ceil(numRows / numPerPage);
                         //  var $pager = $('<div class="pager"></div>');
                           var $pager = $('<ul class="pagination pagination-sm"></ul>');
                           for (var page = 0; page < numPages; page++) {
                               var numero = page + 1;
                               $('<li><a href="javascript:void(0);">'+numero+'</a></li>').bind('click', {
                                   newPage: page
                               }, function(event) {
                                   currentPage = event.data['newPage'];
                                   $table.trigger('repaginate');
                                   $(this).addClass('active').siblings().removeClass('active');
                               }).appendTo($pager);
                           }
                           $pager.insertBefore($table).find('li:first').addClass('active');

                       });

            }


      }) ;

}


/* Función que limpia el formulario de búsqueda y vacia la tabla de resultados de la búsqueda
* @param tipoOrganismo, coletilla para los id de los formularios, ya que se importa el mismo jsp varias veces
* */
function limpiarFormularioBusqueda(tipoOrganismo){
      clearForm("#organismoREBusquedaForm"+tipoOrganismo);
      $('#resultadosbusqueda'+tipoOrganismo).empty();
      $('#resultadosbusqueda'+tipoOrganismo).html('');
      $('#resultadosbusqueda'+tipoOrganismo).attr("display:none");
      $('#reloadorg'+tipoOrganismo).hide();
}


function inicializarBuscador(selectNivelAdministracion, selectComunidadAutonoma, idNivelAdministracion, idComunidadAutonoma, tipoOrganismo){
	
         $('#reloadorg'+tipoOrganismo).hide();
         $(selectNivelAdministracion).val(idNivelAdministracion);
         $(selectNivelAdministracion).trigger("chosen:updated");
         $(selectComunidadAutonoma).val(idComunidadAutonoma);
         $(selectComunidadAutonoma).trigger("chosen:updated");

    }

/* Función que asigna el valor seleccionado de la búsqueda al select correspondiente
* y cierra el modal de la búsqueda */
function asignarOrganismo(codigo, denominacion, idSelect, idDenominacion,tipoOrganismo){
    var anadir = true;

    var idModal = "#modalBuscador"+ tipoOrganismo;
    /* Miramos si el organismo ya existe en el select, si existe lo seleccionamos,
    si no lo indicamos con la variable anadir para añadirlo posteriormente */
	$(idSelect +" option").each(function() {

        if($(this).val() == codigo){
			anadir = false;
			$(this).prop("selected", true);
		}

    });
	// Si no existe lo añadimos al select
	if(anadir){
		var html= '';
		var selected = 'selected="selected"';
		html += '<option '+selected+' value="' + codigo + '">'
							+ denominacion + '</option>';

		$(idSelect).append(html);	
		$(idDenominacion).val(denominacion);
    }
	$(idSelect).trigger("chosen:updated");
	$(idModal).modal('hide');
    limpiarFormularioBusqueda(tipoOrganismo);
}

/**
 * Añade una nueva fila con la Persona Juridica a la tabla de interesados
 * @param codigoDir3
 * @param denominacion
 * @param tipo
 * @param representante
 * @param tipoOrganismo
 */
function addAdministracionInteresadosModal(codigoDir3, denominacion,tipo,tipoOrganismo,idRegistroDetalle){

    var idModal = "#modalBuscador"+ tipoOrganismo;

    $.ajax({
        url: urlAddOrganismoInteresado,
        type: 'GET',
        dataType: 'json',
        data: { codigoDir3: codigoDir3, denominacion:denominacion, idRegistroDetalle:idRegistroDetalle },
        contentType: 'application/json',

        success: function(result) {
            addOrganismoInteresadoHtml(codigoDir3, denominacion,tipo,idRegistroDetalle);
        }
    });

    $(idModal).modal('hide');

}

/* función que busca via rest los organimos raiz de la comunidad indicada y los carga en el select indicado
 **/
function buscarOrganismosRaizComunidad(urlServidor,idSelect,valorSelected,todos, codComunidadAutonoma){
    var url = urlServidor+"/rest/busqueda/organismos";

    $.ajax({
        async: false,
        crossDomain: true,
        url: url,
        type: 'GET',
        dataType: 'json',
        data: { codigo:'', denominacion: '', codNivelAdministracion: '', codComunidadAutonoma: codComunidadAutonoma, conOficinas:false, unidadRaiz:true },
        success: function(result) {
            if(todos){html = '<option value="-1">...</option>';}
            var len = result.length;
            var selected='';
            for ( var i = 0; i < len; i++) {
                selected='';
                if(valorSelected != null && result[i].codigo == valorSelected){
                    selected = 'selected="selected"';
                }
                html += '<option '+selected+' value="' + result[i].codigo + '">'
                    + result[i].denominacion + '</option>';
            }
            html += '</option>';

            if(len != 0){
                $(idSelect).html(html);
            }else if(len==0){
                $(idSelect).attr("disabled","disabled");
                var html='';
                $(idSelect).html(html);
            }
            $(idSelect).trigger("chosen:updated");
        }

    });
}
