/*
 * RegistroSeleccionado.java
 *
 * Created on 13 de octubre de 2004, 14:48
 */

package es.caib.regweb3.logic.helper;

import java.io.Serializable;

/**
 * Classe per desar un registre seleccionat
 *
 * @author FJMARTINEZ
 * @version 1.0
 */

public class RegistroSeleccionado implements Comparable<RegistroSeleccionado>, Serializable {

    /**
     * Creates a new instance of RegistroSeleccionado
     */
    public RegistroSeleccionado() {
    }

    public String anoEntrada, numeroEntrada, oficina, descripcionOficina, fechaES,
            data, descripcionRemitente, descripcionOrganismoDestinatario, descripcionDocumento,
            descripcionIdiomaDocumento, registroAnulado, extracto, descripcionGeografico, oficio,
            oficinaFisica, descripcionOficinaFisica;

    private int ano;
    private int numero;
    private int numeroDocumentosRegistro060 = 1;
    private boolean tieneDocsElectronicos = false;

    public void setAnoEntrada(String anoEntrada) {
        this.anoEntrada = anoEntrada;
        ano = Integer.parseInt(anoEntrada);
    }

    public void setNumeroEntrada(String numeroEntrada) {
        this.numeroEntrada = numeroEntrada;
        numero = Integer.parseInt(numeroEntrada);
    }

    public void setOficina(String oficina) {
        this.oficina = oficina;
    }

    public void setOficinaFisica(String oficinaFisica) {
        this.oficinaFisica = oficinaFisica;
    }

    public void setExtracto(String extracto) {
        this.extracto = extracto;
    }

    public void setDescripcionOficina(String descripcionOficina) {
        this.descripcionOficina = descripcionOficina;
    }

    public void setDescripcionOficinaFisica(String descripcionOficinaFisica) {
        this.descripcionOficinaFisica = descripcionOficinaFisica;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setOficio(String oficio) {
        this.oficio = oficio;
    }

    public void setFechaES(String fechaES) {
        this.fechaES = fechaES;
    }

    public void setDescripcionRemitente(String descripcionRemitente) {
        this.descripcionRemitente = descripcionRemitente;
    }

    public void setDescripcionOrganismoDestinatario(String descripcionOrganismoDestinatario) {
        this.descripcionOrganismoDestinatario = descripcionOrganismoDestinatario;
    }

    public void setDescripcionDocumento(String descripcionDocumento) {
        this.descripcionDocumento = descripcionDocumento;
    }

    public void setDescripcionIdiomaDocumento(String descripcionIdiomaDocumento) {
        this.descripcionIdiomaDocumento = descripcionIdiomaDocumento;
    }

    public void setRegistroAnulado(String registroAnulado) {
        this.registroAnulado = registroAnulado;
    }

    public void setDescripcionGeografico(String descripcionGeografico) {
        this.descripcionGeografico = descripcionGeografico;
    }


    public String getExtracto() {
        return extracto;
    }

    public String getAnoEntrada() {
        return anoEntrada;
    }

    public String getNumeroEntrada() {
        return numeroEntrada;
    }

    public String getOficina() {
        return oficina;
    }

    public String getOficinaFisica() {
        return oficinaFisica;
    }

    public String getOficio() {
        return oficio;
    }

    public String getDescripcionOficina() {
        return descripcionOficina;
    }

    public String getDescripcionOficinaFisica() {
        return descripcionOficinaFisica;
    }

    public String getDescripcionGeografico() {
        return descripcionGeografico;
    }

    public String getData() {
        return data;
    }

    public String getFechaES() {
        return fechaES;
    }

    public String getDescripcionRemitente() {
        return descripcionRemitente;
    }

    public String getDescripcionOrganismoDestinatario() {
        return descripcionOrganismoDestinatario;
    }

    public String getDescripcionDocumento() {
        return descripcionDocumento;
    }

    public String getDescripcionIdiomaDocumento() {
        return descripcionIdiomaDocumento;
    }

    public String getRegistroAnulado() {
        return registroAnulado;
    }

    public int getAno() {
        return ano;
    }

    public int getNumero() {
        return numero;
    }

    public int compareTo(RegistroSeleccionado o) {

        if (!(o instanceof RegistroSeleccionado)) {
            throw new ClassCastException();
        }

        RegistroSeleccionado reg = (RegistroSeleccionado) o;
        int resultado = ano - reg.getAno();
        if (resultado == 0) {
            return numero - reg.getNumero();
        } else {
            return numero;
        }
    }

    public int getNumeroDocumentosRegistro060() {
        return numeroDocumentosRegistro060;
    }

    public void setNumeroDocumentosRegistro060(int numeroDocumentosRegistro060) {
        this.numeroDocumentosRegistro060 = numeroDocumentosRegistro060;
    }

    /**
     * @return the tieneDocsElectronicos
     */
    public boolean getTieneDocsElectronicos() {
        return tieneDocsElectronicos;
    }

    /**
     * @param tieneDocsElectronicos the tieneDocsElectronicos to set
     */
    public void setTieneDocsElectronicos(boolean tieneDocsElectronicos) {
        this.tieneDocsElectronicos = tieneDocsElectronicos;
    }


    public void print() {
        System.out.println("anoEntrada:" + anoEntrada);
        System.out.println("numeroEntrada:" + numeroEntrada);
        System.out.println("oficina:" + oficina);
        System.out.println("descripcionOficina:" + descripcionOficina);
        System.out.println("fechaES:" + fechaES);
        System.out.println("data:" + data);
        System.out.println("descripcionRemitente:" + descripcionRemitente);
        System.out.println("descripcionOrganismoDestinatario:" + descripcionOrganismoDestinatario);
        System.out.println("descripcionDocumento:" + descripcionDocumento);
        System.out.println("descripcionIdiomaDocumento:" + descripcionIdiomaDocumento);
        System.out.println("registroAnulado:" + registroAnulado);
        System.out.println("extracto:" + extracto);
        System.out.println("descripcionGeografico:" + descripcionGeografico);
        System.out.println("oficio:" + oficio);
        System.out.println("oficinaFisica:" + oficinaFisica);
        System.out.println("descripcionOficinaFisica:" + descripcionOficinaFisica);
        System.out.println("ano:" + ano);
        System.out.println("numero:" + numero);
        System.out.println("numeroDocumentosRegistro060:" + numeroDocumentosRegistro060);
        System.out.println("tieneDocsElectronicos:" + tieneDocsElectronicos);
    }


}
