package es.caib.regweb3.model.utils;

import es.caib.regweb3.model.AnexoSir;

/**
 * Created by earrivi on 19/05/2017.
 *
 * Clase que representa un Anexo Sir completo con su documento y su firma
 */
public class AnexoSirFull {

    private AnexoSir documento;
    private AnexoSir firma;
    private boolean tieneFirma;

    public AnexoSirFull() {

    }

    public AnexoSirFull(AnexoSir documento, AnexoSir firma) {
        this.documento = documento;
        this.firma = firma;
    }

    public AnexoSir getDocumento() {
        return documento;
    }

    public void setDocumento(AnexoSir documento) {
        this.documento = documento;
    }

    public AnexoSir getFirma() {
        return firma;
    }

    public void setFirma(AnexoSir firma) {
        this.firma = firma;
    }

    public boolean getTieneFirma() {
        return tieneFirma;
    }

    public void setTieneFirma(boolean tieneFirma) {
        this.tieneFirma = tieneFirma;
    }
}
