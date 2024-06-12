package es.caib.regweb3.persistence.validator;

import es.caib.regweb3.model.CatPais;
import es.caib.regweb3.model.Interesado;
import es.caib.regweb3.persistence.ejb.CatPaisLocal;
import es.caib.regweb3.persistence.ejb.InteresadoLocal;
import es.caib.regweb3.persistence.ejb.PersonaLocal;
import es.caib.regweb3.utils.DocumentoUtils;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.StringUtils;
import es.caib.regweb3.utils.Validacion;
import org.fundaciobit.genapp.common.i18n.I18NArgumentString;
import org.fundaciobit.genapp.common.validation.IValidatorResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by Fundació BIT. Gestiona las Validaciones del formulario para crear
 * o editar una {@link es.caib.regweb3.model.Interesado}
 *
 * @author earrivi
 * @author anadal
 * Date: 11/02/14
 */

public class InteresadoValidator<T> extends AbstractRegWebValidator<T> {

    protected final Logger log = LoggerFactory.getLogger(getClass());


    public void validate(IValidatorResult<T> errors, T __target__, boolean __isNou__,
                         InteresadoLocal interesadoEjb, PersonaLocal personaEjb, CatPaisLocal catPaisEjb) {

        Interesado interesado = (Interesado) __target__;

        // Validaciones si es Interesado Física

        if (interesado.getTipo() == null) {
            rejectIfEmptyOrWhitespace(errors, __target__, "tipo", "error.valor.requerido");
        } else {

            if (interesado.getTipo().equals(RegwebConstantes.TIPO_INTERESADO_PERSONA_FISICA)) {

                rejectIfEmptyOrWhitespace(errors, __target__, "apellido1",
                        "error.valor.requerido");

                if (!hasFieldErrors(errors, "apellido1") && (interesado.getApellido1().length() > 30)) {
                    rejectValue(errors, "apellido1", "error.valor.maxlenght");
                }

                rejectIfEmptyOrWhitespace(errors, __target__, "nombre", "error.valor.requerido");

                if (!hasFieldErrors(errors, "nombre") && (interesado.getNombre().length() > 30)) {
                    rejectValue(errors, "nombre", "error.valor.maxlenght");
                }

                if (!isNullOrEmpty(interesado.getApellido2()) && (interesado.getApellido2().length() > 30)) {
                    rejectValue(errors, "apellido2", "error.valor.maxlenght");
                }

            }

            // Validaciones si es Interesado Jurídica
            if (interesado.getTipo().equals(RegwebConstantes.TIPO_INTERESADO_PERSONA_JURIDICA)) {
                rejectIfEmptyOrWhitespace(errors, __target__, "razonSocial", "error.valor.requerido");
                if (interesado.getCanal() != null && (interesado.getCanal().equals(RegwebConstantes.CANAL_DIRECCION_POSTAL))){
                    rejectValue(errors, "canal", "error.canal.direccion.electronica");
                }
            }

            // Validaciones si es ADMINISTRACION
            if (interesado.getTipo().equals(RegwebConstantes.TIPO_INTERESADO_ADMINISTRACION)) {
                rejectIfEmptyOrWhitespace(errors, __target__, "razonSocial", "error.valor.requerido");
            }
        }

        // Gestionamos las validaciones según el Canal escogido
        if (interesado.getCanal() == null) {
            //rejectIfEmptyOrWhitespace(errors, __target__, "canal", "error.valor.requerido","El camp és obligatori");
        } else {

            if (interesado.getCanal().equals(RegwebConstantes.CANAL_DIRECCION_POSTAL)) {

                rejectIfEmptyOrWhitespace(errors, __target__, "direccion", "error.valor.requerido");

                if (interesado.getPais() == null || interesado.getPais().getId() == null || interesado.getPais().getId() == -1) {
                    rejectValue(errors, "pais.id", "error.valor.requerido");
                } else {

                    try {

                        log.info("Buscando país: " + interesado.getPais().getId() + " - " + interesado.getPais().getCodigoPais() + " - " + interesado.getPais().getDescripcionPais());
                        log.info("Localidad: " + interesado.getLocalidad() + " - " + interesado.getNombre());
                        log.info("Cp: " + interesado.getCp() + " - " + interesado.getNombre());
                        CatPais pais = catPaisEjb.findById(interesado.getPais().getId());

                        // Validaciones si el país seleccionado es ESPAÑA
                        if (pais.getCodigoPais().equals(RegwebConstantes.PAIS_ESPANYA)) {

                            if (interesado.getProvincia() == null || interesado.getProvincia().getId() == null || interesado.getProvincia().getId() == -1) {
                                rejectValue(errors, "provincia.id", "error.valor.requerido");
                            }
                            if (interesado.getLocalidad() == null || interesado.getLocalidad().getId() == null || interesado.getLocalidad().getId() == -1) {
                                rejectValue(errors, "localidad.id", "error.valor.requerido");
                            }
                            if (StringUtils.isEmpty(interesado.getCp())) {
                                rejectIfEmptyOrWhitespace(errors, __target__, "cp", "error.valor.requerido");
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            } else if (interesado.getCanal().equals(RegwebConstantes.CANAL_DIRECCION_ELECTRONICA)) {

                rejectIfEmptyOrWhitespace(errors, __target__, "direccionElectronica", "error.valor.requerido");
            }
        }

        // CÓDIGO POSTAL
        if (interesado.getCp() != null && interesado.getCp().length() > 0) {
            if (interesado.getCp().length() == 5) {
                for (int i = 0; i < interesado.getCp().length(); i++) {
                    if (!Character.isDigit(interesado.getCp().charAt(i))) {
                        rejectValue(errors, "cp", "error.cp.formato");
                        break;
                    }
                }
            } else {
                rejectValue(errors, "cp", "error.cp.largo");
            }
        }


        //SICRES4
        Long tipoDocumento = interesado.getTipoDocumentoIdentificacion();
        if(tipoDocumento == null){
            rejectValue(errors, "tipoDocumentoIdentificacion", "error.valor.requerido");
        }else{
            if(tipoDocumento == RegwebConstantes.TIPODOCUMENTOID_CIF_ID && isNullOrEmpty(interesado.getRazonSocial())){
                rejectValue(errors, "razonSocial", "error.valor.requerido");
            }
            rejectIfEmptyOrWhitespace(errors, __target__, "documento", "error.valor.requerido");
        }


        if(tipoDocumento != null && StringUtils.isNotEmpty(interesado.getDocumento())) {

            String documento = interesado.getDocumento().toUpperCase();

            Validacion validacionDocumento;
            try {
                validacionDocumento = DocumentoUtils.comprobarDocumento(documento, tipoDocumento);
            } catch (Exception e) {
                e.printStackTrace();
                validacionDocumento = new Validacion(Boolean.FALSE, "error.documento", "El document es erroni");
            }

            //Si el formato es correcto busca que no exista ya en el sistema
            if(validacionDocumento.getValido()){

                if(__isNou__){ // Si viene de la capa Web, Comprueba que el documento no exista en la bbdd
                    boolean existe = false;
                    try {

                        if (interesado.getId() == null) {
                            existe = personaEjb.existeDocumentoNew(interesado.getDocumento().toUpperCase(), interesado.getEntidad());
                        }

                    } catch (Exception e) {
                        log.info("Error comprobando si persona ya existe: ", e);
                        existe = true;
                    }

                    if (existe) {
                        rejectValue(errors, "documento", "error.document.existe");
                    }
                }

            } else {
                rejectValue(errors, "documento", validacionDocumento.getCodigoError(), new I18NArgumentString(documento));
            }

        }else if(StringUtils.isNotEmpty(interesado.getDocumento())){
            rejectValue(errors, "tipoDocumentoIdentificacion", "error.valor.requerido");
        }


        //DIRECCIÓN
        if (!isNullOrEmpty(interesado.getDireccion()) && (interesado.getDireccion().length() > 160)) {
            rejectValue(errors, "direccion", "error.valor.maxlenght");
        }

        //EMAIL
        if (!isNullOrEmpty(interesado.getEmail()) && interesado.getEmail().length() > 160) {
            rejectValue(errors, "email", "error.valor.maxlenght");
        }
        //SICRES4
        if(interesado.getAvisoCorreoElectronico() && isNullOrEmpty(interesado.getEmail())){
            rejectValue(errors, "email", "error.valor.requerido");
        }

        //TELÉFONO
        if (!isNullOrEmpty(interesado.getTelefono()) && interesado.getTelefono().length() > 20) {
            rejectValue(errors, "telefono", "error.valor.maxlenght");
        }
        if(interesado.getAvisoNotificacionSMS() && isNullOrEmpty(interesado.getTelefonoMovil())){
            rejectValue(errors, "telefonoMovil", "error.valor.requerido");
        }

        //RAZÓN SOCIAL
        if (!isNullOrEmpty(interesado.getRazonSocial()) && interesado.getRazonSocial().length() > 80) {
            rejectValue(errors, "razonSocial", "error.valor.maxlenght");
        }

        //OBSERVACIONES
        if (!isNullOrEmpty(interesado.getObservaciones()) && interesado.getObservaciones().length() > 160) {
            rejectValue(errors, "observaciones", "error.valor.maxlenght");
        }


    }


    @Override
    public String prefixFieldName() {
        return "interesado.";
    }

    @Override
    public String adjustFieldName(String fieldName) {
        if (fieldName == null) {
            return null;
        } else {
            if (fieldName.endsWith(".id")) {
                return fieldName.substring(0, fieldName.lastIndexOf('.'));
            }
        }
        return fieldName;
    }

}
