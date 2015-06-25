package es.caib.regweb3.persistence.validator;

import es.caib.regweb3.model.CatPais;
import es.caib.regweb3.model.Interesado;
import es.caib.regweb3.persistence.ejb.CatPaisLocal;
import es.caib.regweb3.persistence.ejb.InteresadoLocal;
import es.caib.regweb3.persistence.ejb.PersonaLocal;
import es.caib.regweb3.utils.RegwebConstantes;
import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.validation.IValidatorResult;


/**
 * Created by Fundació BIT. Gestiona las Validaciones del formulario para crear
 * o editar una {@link es.caib.regweb3.model.Interesado}
 * 
 * @author earrivi
 * @author anadal 
 * Date: 11/02/14
 */

public class InteresadoValidator<T> extends AbstractRegWebValidator<T> {

  protected final Logger log = Logger.getLogger(getClass());

   
  public void validate(IValidatorResult<T> errors, T __target__, boolean __isNou__,
      InteresadoLocal interesadoEjb, PersonaLocal personaEjb, CatPaisLocal catPaisEjb) {

    Interesado interesado = (Interesado) __target__;

    // TODO Verificar que existeix
    // interesado.getTipoDocumentoIdentificacion().getId()

    // Validaciones si es Interesado Física

    if (interesado.getTipo() == null) {
      rejectIfEmptyOrWhitespace(errors, __target__,  "tipo",
          "error.valor.requerido", "El camp és obligatori");
    } else {
      if (interesado.getTipo() != null) {
        if (interesado.getTipo().equals(RegwebConstantes.TIPO_INTERESADO_PERSONA_FISICA)) {

        
          rejectIfEmptyOrWhitespace(errors, __target__, "apellido1",
              "error.valor.requerido", "El camp és obligatori");
        
          if (!hasFieldErrors(errors,"apellido1")) {
            if(interesado.getApellido1().length() > 30){
              rejectValue(errors, "apellido1","error.valor.maxlenght","Tamaño demasiado largo");
            }
          }

        
          rejectIfEmptyOrWhitespace(errors, __target__, "nombre", "error.valor.requerido",
              "El camp és obligatori");
         
          if (!hasFieldErrors(errors, "nombre")) {
            if(interesado.getNombre().length() > 30) {
              rejectValue(errors, "nombre","error.valor.maxlenght","Tamaño demasiado largo");
            }
          }
          
          if(!isNullOrEmpty(interesado.getApellido2())){
                if(interesado.getApellido2().length() > 30){
                  rejectValue(errors, "apellido2","error.valor.maxlenght","Tamaño demasiado largo");
              }
          }

        /*
        if ((interesado.getNombre() == null || interesado.getNombre().length() == 0)
            && (interesado.getApellido1() == null || interesado.getApellido1().length() == 0)) {
          rejectValue(errors, "nombre", "error.valor.requerido", "El camp és obligatori");
          rejectValue(errors, "apellido1", "error.valor.requerido", "El camp és obligatori");
        }
        */

        /*
         * rejectIfEmptyOrWhitespace(errors, __target__, "nombre",
         * "error.valor.requerido", "El camp és obligatori");
         * rejectIfEmptyOrWhitespace(errors, __target__, "apellido1",
         * "error.valor.requerido", "El camp és obligatori");
         */

      }

      // Validaciones si es Interesado Jurídica
      if (interesado.getTipo().equals(RegwebConstantes.TIPO_INTERESADO_PERSONA_JURIDICA)) {
        rejectIfEmptyOrWhitespace(errors, __target__, "razonSocial",
            "error.valor.requerido", "El camp és obligatori");
      }
      }
    }

    // Gestionamos las validaciones según el Canal escogido
    if (interesado.getCanal() == null) {
      //rejectIfEmptyOrWhitespace(errors, __target__, "canal", "error.valor.requerido","El camp és obligatori");
    } else {
      {

        if (interesado.getCanal().equals(RegwebConstantes.CANAL_DIRECCION_POSTAL)) {

          
          rejectIfEmptyOrWhitespace(errors, __target__, "direccion",
                "error.valor.requerido", "El camp és obligatori");
          

          // Validaciones si el país seleccionado es ESPAÑA
          if (interesado.getPais() == null || interesado.getPais().getId() == null || interesado.getPais().getId() == -1) {
            rejectValue(errors, "pais.id", "error.valor.requerido", "El camp és obligatori");
          } else {

            try {
              CatPais pais = catPaisEjb.findById(interesado.getPais().getId());

              if (pais.getCodigoPais().equals(RegwebConstantes.PAIS_ESPAÑA)) {

                if (interesado.getCp() == null || interesado.getCp().length() == 0) {

                  if (interesado.getProvincia() == null
                      || interesado.getProvincia().getId() == null) {
                    rejectValue(errors, "provincia.id", "error.valor.requerido",
                        "El camp és obligatori");
                  }
                }

                if (interesado.getProvincia() == null
                    || interesado.getProvincia().getId() == null) {
                  rejectIfEmptyOrWhitespace(errors, __target__, "cp", "error.valor.requerido",
                      "El camp és obligatori");
                }
              }

            } catch (Exception e) {
              e.printStackTrace();
            }

          }

        } else if (interesado.getCanal()
            .equals(RegwebConstantes.CANAL_DIRECCION_ELECTRONICA)) {

          rejectIfEmptyOrWhitespace(errors, __target__, "direccionElectronica",
              "error.valor.requerido", "El camp és obligatori");

        } else if (interesado.getCanal()
            .equals(RegwebConstantes.CANAL_COMPARECENCIA_ELECTRONICA)) {

        }
      }
    }

    // CÓDIGO POSTAL
    if (interesado.getCp() != null && interesado.getCp().length() > 0) {
      if (interesado.getCp().length() == 5) {
        for (int i = 0; i < interesado.getCp().length(); i++) {
          if (!Character.isDigit(interesado.getCp().charAt(i))) {
            rejectValue(errors, "cp", "error.cp.formato", "El codi postal ha de ser numèric");
            break;
          }
        }
      } else {
        rejectValue(errors, "cp", "error.cp.largo", "El codi postal no té 5 dígits");
      }
    }

    
    // DOCUMENTO (DNI, NIE, PASAPORTE)
    Long tipoDocumento = interesado.getTipoDocumentoIdentificacion();

    if (tipoDocumento != null) {
        //rejectIfEmptyOrWhitespace(errors, __target__, "documento", "error.valor.requerido", "El camp és obligatori");

      String documento = interesado.getDocumento().toUpperCase();

        Boolean formatoCorrecto = false;

        String letras = "TRWAGMYFPDXBNJZSQVHLCKE";
        int valor = 0;

        switch (tipoDocumento.intValue()) {

        case (int) RegwebConstantes.TIPODOCUMENTOID_NIF_ID: /* NIF (DNI) */
          if (documento.length() == 9) {
            String numeroNif = documento.substring(0, documento.length() - 1);
            Boolean nifcorrecte = true;
            for (int i = 0; i < numeroNif.length(); i++) {
              if (!Character.isDigit(numeroNif.charAt(i))) {
                rejectValue(errors, "documento", "error.dni.incorrecto",
                    "El dni no té format correcte (8 DIGITS + LLETRA)");
                nifcorrecte = false;
                break;
              }
            }
            if (nifcorrecte) {
              valor = Integer.parseInt(documento.substring(0, documento.length() - 1));
              formatoCorrecto = true;
            }
          } else {
            rejectValue(errors, "documento", "error.document.largo",
                "Llargària de document incorrecta");
          }

          if(formatoCorrecto){
            if (documento.endsWith("" + letras.charAt(valor % 23)) == false) {
              rejectValue(errors, "documento", "error.documento.formato","Lletra de document incorrecta");
            }
          }
          break;

        case (int) RegwebConstantes.TIPODOCUMENTOID_CIF_ID: /* CIF */
          if (documento.length() == 9) {
            String cif = documento.substring(1, documento.length() - 1);
            // Boolean nie1correcte = true;
            for (int i = 0; i < cif.length(); i++) {
              if (!Character.isDigit(cif.charAt(i))) {
                rejectValue(errors, "documento", "error.cif.incorrecto",
                    "El nie no té format correcte (LLETRA + 7 DIGITS + LLETRA)");
                // nie1correcte = false;
                break;
              }
            }
          } else {
            rejectValue(errors, "documento", "error.document.largo",
                "Llargària de document incorrecta");
          }
          formatoCorrecto = true;
          break;

        case (int) RegwebConstantes.TIPODOCUMENTOID_NIE_ID: /* NIE */
          if (documento.length() == 9) {

            if (documento.startsWith("X") || documento.startsWith("x")) {
              // Es un NIE
              String numeroNie1 = documento.substring(1, documento.length() - 1);
              Boolean nie1correcte = true;
              for (int i = 0; i < numeroNie1.length(); i++) {
                if (!Character.isDigit(numeroNie1.charAt(i))) {
                  rejectValue(errors, "documento", "error.nie.incorrecto",
                      "El nie no té format correcte (LLETRA + 7 DIGITS + LLETRA)");
                  nie1correcte = false;

                  break;
                }
              }
              if (nie1correcte) {
                valor = Integer.parseInt(documento.substring(1, documento.length() - 1));
                formatoCorrecto = true;
              }

            } else if (documento.startsWith("Y") || documento.startsWith("y")) {
              // Es un NIE
              String numeroNie2 = documento.substring(1, documento.length() - 1);
              Boolean nie2correcte = true;
              for (int i = 0; i < numeroNie2.length(); i++) {
                if (!Character.isDigit(numeroNie2.charAt(i))) {
                  rejectValue(errors, "documento", "error.nie.incorrecto",
                      "El nie no té format correcte (LLETRA + 7 DIGITS + LLETRA)");
                  nie2correcte = false;

                  break;
                }
              }
              if (nie2correcte) {
                valor = 10000000 + Integer.parseInt(documento.substring(1,
                    documento.length() - 1));
                formatoCorrecto = true;
              }

            } else if (documento.startsWith("Z") || documento.startsWith("z")) {
              // Es un NIE
              String numeroNie3 = documento.substring(1, documento.length() - 1);
              Boolean nie3correcte = true;
              for (int i = 0; i < numeroNie3.length(); i++) {
                if (!Character.isDigit(numeroNie3.charAt(i))) {
                  rejectValue(errors, "documento", "error.nie.incorrecto",
                      "El nie no té format correcte (LLETRA + 7 DIGITS + LLETRA)");
                  nie3correcte = false;

                  break;
                }
              }
              if (nie3correcte) {
                valor = 20000000 + Integer.parseInt(documento.substring(1,
                    documento.length() - 1));
                formatoCorrecto = true;
              }
            } else {
              rejectValue(errors, "documento", "error.nie.incorrecto",
                  "El nie no té format correcte (LLETRA + 7 DIGITS + LLETRA)");
            }

          } else {
            rejectValue(errors, "documento", "error.document.largo",
                "Llargària de document incorrecta");
          }

          if(formatoCorrecto){
            if (documento.endsWith("" + letras.charAt(valor % 23)) == false) {
              rejectValue(errors, "documento", "error.documento.formato","Lletra de document incorrecta");
            }
          }
          break;

        case (int) RegwebConstantes.TIPODOCUMENTOID_PASSAPORT_ID: /* PASAPORTE */

          break;
        }

        if (formatoCorrecto) {
            boolean existe = false;
            try {
              if (interesado.getId() == null) {
                  log.info("dentro existeDocumentoNew");
                existe = personaEjb.existeDocumentoNew(interesado.getDocumento(),interesado.getEntidad());
              }/* else {
                  log.info("dentro existeDocumentoEdit");
                existe = personaEjb.existeDocumentoEdit(interesado.getDocumento(),interesado.getId(),interesado.getEntidad());
              }*/

            } catch (Exception e) {
              log.error("Error comprobando si interesado ya existe: ", e);
              existe = true;
            }

            if (existe) {
              rejectValue(errors, "documento", "error.document.existe", "El document ja existeix");
            }
        }
    }
    
    
    //DIRECCIÓN
    if(!isNullOrEmpty(interesado.getDireccion())){
        if(interesado.getDireccion().length() > 160){
            rejectValue(errors, "direccion","error.valor.maxlenght","Tamaño demasiado largo");
        }
    }

    //EMAIL
    if(!isNullOrEmpty(interesado.getEmail()) && interesado.getEmail().length() > 160){
      rejectValue(errors, "email","error.valor.maxlenght","Tamaño demasiado largo");
    }

    //TELÉFONO
    if(!isNullOrEmpty(interesado.getTelefono()) && interesado.getTelefono().length() > 20) {
      rejectValue(errors, "telefono","error.valor.maxlenght","Tamaño demasiado largo");
    }

    //DireccionElectronica
    if(!isNullOrEmpty(interesado.getDireccionElectronica()) && interesado.getDireccionElectronica().length() > 160) {
        rejectValue(errors, "direccionElectronica","error.valor.maxlenght","Tamaño demasiado largo");
    }

    //RAZÓN SOCIAL
    if(!isNullOrEmpty(interesado.getRazonSocial()) && interesado.getRazonSocial().length() > 80) {
       rejectValue(errors, "razonSocial","error.valor.maxlenght","Tamaño demasiado largo");
    }

    //OBSERVACIONES
    if(!isNullOrEmpty(interesado.getObservaciones()) && interesado.getObservaciones().length() > 80) {
      rejectValue(errors, "observaciones","error.valor.maxlenght","Tamaño demasiado largo");
    }


  }


  @Override
  public String prefixFieldName() {
    return "interesado.";
  }

  @Override
  public String adjustFieldName(String fieldName) {
    if (fieldName == null ) {
      return null;
    } else {
      if (fieldName.endsWith(".id")) {
        return fieldName.substring(0, fieldName.lastIndexOf('.'));
      }
    }
    return fieldName;
  }

}
