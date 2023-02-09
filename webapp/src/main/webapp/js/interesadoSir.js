/**
 * Carga los datos de un InteresadoSir para mostrarlos en un modal
 * @param idInteresadoSir
 */
function obtenerInteresadoSir(idInteresadoSir){

    // Eliminamos el contenido del formulario y los mensajes de error
    limpiarInteresadoDetalleSir();

    //Obtenemos los datos de la Persona a editar
    $.ajax({
        url: urlCargarInteresadoSir,
        data: { idInteresadoSir: idInteresadoSir },
        type: "GET",
        dataType: 'json',
        contentType: 'application/json',

        success: function(result) {

            $('#representante').hide();

            // Interesado
            $('#interesadoTitulo').html(result.nombreCompleto);
            $('#nombreInteresado').html(result.nombreInteresado);
            $('#primerApellidoInteresado').html(result.primerApellidoInteresado);
            $('#segundoApellidoInteresado').html(result.segundoApellidoInteresado);
            if(result.tipoDocumentoIdentificacionInteresado != null && result.tipoDocumentoIdentificacionInteresado != '-1'){
                $("#tipoDocumentoIdentificacionInteresado").html(tradsinteresado['tipoDocumentoIdentificacion.'+result.tipoDocumentoIdentificacionInteresado]);
                $('#documentoIdentificacionInteresado').html(result.documentoIdentificacionInteresado);
            }

            if(result.codigoPaisInteresado != null){$("#codigoPaisInteresado").html(result.codigoPaisInteresado);}
            if(result.codigoProvinciaInteresado != null){
                $("#codigoProvinciaInteresado").html(result.codigoProvinciaInteresado);

                if(result.codigoMunicipioInteresado != null){
                    $("#codigoMunicipioInteresado").html(result.codigoMunicipioInteresado);

                }
            }

            $('#direccionInteresado').html(result.direccionInteresado);
            $('#razonSocialInteresado').html(result.razonSocialInteresado);
            $('#codigoDirectorioUnificadosInteresado').html(result.codigoDirectorioUnificadosInteresado);
            $('#direccionElectronicaHabilitadaInteresado').html(result.direccionElectronicaHabilitadaInteresado);
            $('#correoElectronicoInteresado').html(result.correoElectronicoInteresado);
            $('#codigoPostalInteresado').html(result.codigoPostalInteresado);
            $('#telefonoInteresado').html(result.telefonoInteresado);
            $('#telefonoMovilInteresado').html(result.telefonoMovilInteresado);
            if (result.canalPreferenteComunicacionInteresado != null) {
                $("#canalPreferenteComunicacionInteresado").html(tradsinteresado['canalNotificacion.' + result.canalPreferenteComunicacionInteresado]);

            }
            $('#observaciones').html(result.observaciones);

            //SICRES4
            $('#receptorNotificacionesInteresado').html(tradsinteresado[result.receptorNotificacionesInteresado]);
            $('#avisoNotificacionSMSInteresado').html(tradsinteresado[result.avisoNotificacionSMSInteresado]);
            $('#avisoCorreoElectronicoInteresado').html(tradsinteresado[result.avisoCorreoElectronicoInteresado]);

            // Si tiene representante
            if (result.representante) {
                $('#nombreRepresentante').html(result.nombreRepresentante);
                $('#primerApellidoRepresentante').html(result.primerApellidoRepresentante);
                $('#segundoApellidoRepresentante').html(result.segundoApellidoRepresentante);
                if (result.tipoDocumentoIdentificacionRepresentante != null && result.tipoDocumentoIdentificacionRepresentante != '-1') {
                    $("#tipoDocumentoIdentificacionRepresentante").html(tradsinteresado['tipoDocumentoIdentificacion.' + result.tipoDocumentoIdentificacionRepresentante]);
                    $('#documentoIdentificacionRepresentante').html(result.documentoIdentificacionRepresentante);
                }

                if(result.codigoPaisRepresentante != null){$("#codigoPaisRepresentante").html(result.codigoPaisRepresentante);}
                if(result.codigoProvinciaRepresentante != null){
                    $("#codigoProvinciaRepresentante").html(result.codigoProvinciaRepresentante);

                    if(result.codigoMunicipioRepresentante != null){
                        $("#codigoMunicipioRepresentante").html(result.codigoMunicipioRepresentante);

                    }
                }

                $('#direccionRepresentante').html(result.direccionRepresentante);
                $('#razonSocialRepresentante').html(result.razonSocialRepresentante);
                $('#codigoDirectorioUnificadosRepresentante').html(result.codigoDirectorioUnificadosRepresentante);
                $('#direccionElectronicaHabilitadaRepresentante').html(result.direccionElectronicaHabilitadaRepresentante);
                $('#correoElectronicoRepresentante').html(result.correoElectronicoRepresentante);
                $('#codigoPostalRepresentante').html(result.codigoPostalRepresentante);
                $('#telefonoRepresentante').html(result.telefonoRepresentante);
                $('#telefonoMovilRepresentante').html(result.telefonoMovilRepresentante);
                if (result.canalPreferenteComunicacionRepresentante != null) {
                    $("#canalPreferenteComunicacionRepresentante").html(tradsinteresado['canalNotificacion.' + result.canalPreferenteComunicacionRepresentante]);
                }

                //SICRES4

                $('#receptorNotificacionesRepresentante').html(tradsinteresado[result.receptorNotificacionesRepresentante]);
                $('#avisoNotificacionSMSRepresentante').html(tradsinteresado[result.avisoNotificacionSMSRepresentante]);
                $('#avisoCorreoElectronicoRepresentante').html(tradsinteresado[result.avisoCorreoElectronicoRepresentante]);

                $('#representante').show();
            }

        }
    });
}

/**
 * Limpia el formulario de interesado y los posibles mensajes de error
 */
function limpiarInteresadoDetalleSir(){

    $('#interesadoTitulo').html('');

    $('#nombreInteresado').html('');
    $('#primerApellidoInteresado').html('');
    $('#segundoApellidoInteresado').html('');
    $('#tipoDocumentoIdentificacionInteresado').html('');
    $('#documentoIdentificacionInteresado').html('');
    $('#correoElectronicoInteresado').html('');
    $('#telefonoInteresado').html('');
    $('#canalPreferenteComunicacionInteresado').html('');
    $('#codigoPaisInteresado').html('');
    $('#codigoProvinciaInteresado').html('');
    $('#codigoMunicipioInteresado').html('');
    $('#direccionInteresado').html('');
    $('#codigoPostalInteresado').html('');
    $('#razonSocialInteresado').html('');
    $('#direccionElectronicaHabilitadaInteresado').html(''); //TODO deprecated
    $('#receptorNotificacionesInteresado').html('');
    $('#avisoNotificacionSMSInteresado').html('');
    $('#avisoCorreoElectronicoInteresado').html('');

    $('#nombreRepresentante').html('');
    $('#primerApellidoRepresentante').html('');
    $('#segundoApellidoRepresentante').html('');
    $('#tipoDocumentoIdentificacionRepresentante').html('');
    $('#documentoIdentificacionRepresentante').html('');
    $('#correoElectronicoRepresentante').html('');
    $('#telefonoRepresentante').html('');
    $('#canalPreferenteComunicacionRepresentante').html('');
    $('#codigoPaisRepresentante').html('');
    $('#codigoProvinciaRepresentante').html('');
    $('#codigoMunicipioRepresentante').html('');
    $('#direccionRepresentante').html('');
    $('#codigoPostalRepresentante').html('');
    $('#razonSocialRepresentante').html('');
    $('#direccionElectronicaHabilitadaRepresentante').html(''); //TODO deprecated
    $('#receptorNotificacionesRepresentante').html('');
    $('#avisoNotificacionSMSRepresentante').html('');
    $('#avisoCorreoElectronicoRepresentante').html('');

    $('#observaciones').html('');
}
