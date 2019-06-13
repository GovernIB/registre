/**
 * ResultadoBusquedaWs.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.caib.regweb3.ws.v3.apiaxis;

public class ResultadoBusquedaWs  implements java.io.Serializable {
    private java.lang.Integer pageNumber;

    private java.lang.Object[] results;

    private java.lang.Integer totalResults;

    public ResultadoBusquedaWs() {
    }

    public ResultadoBusquedaWs(
           java.lang.Integer pageNumber,
           java.lang.Object[] results,
           java.lang.Integer totalResults) {
           this.pageNumber = pageNumber;
           this.results = results;
           this.totalResults = totalResults;
    }


    /**
     * Gets the pageNumber value for this ResultadoBusquedaWs.
     * 
     * @return pageNumber
     */
    public java.lang.Integer getPageNumber() {
        return pageNumber;
    }


    /**
     * Sets the pageNumber value for this ResultadoBusquedaWs.
     * 
     * @param pageNumber
     */
    public void setPageNumber(java.lang.Integer pageNumber) {
        this.pageNumber = pageNumber;
    }


    /**
     * Gets the results value for this ResultadoBusquedaWs.
     * 
     * @return results
     */
    public java.lang.Object[] getResults() {
        return results;
    }


    /**
     * Sets the results value for this ResultadoBusquedaWs.
     * 
     * @param results
     */
    public void setResults(java.lang.Object[] results) {
        this.results = results;
    }

    public java.lang.Object getResults(int i) {
        return this.results[i];
    }

    public void setResults(int i, java.lang.Object _value) {
        this.results[i] = _value;
    }


    /**
     * Gets the totalResults value for this ResultadoBusquedaWs.
     * 
     * @return totalResults
     */
    public java.lang.Integer getTotalResults() {
        return totalResults;
    }


    /**
     * Sets the totalResults value for this ResultadoBusquedaWs.
     * 
     * @param totalResults
     */
    public void setTotalResults(java.lang.Integer totalResults) {
        this.totalResults = totalResults;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ResultadoBusquedaWs)) return false;
        ResultadoBusquedaWs other = (ResultadoBusquedaWs) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.pageNumber==null && other.getPageNumber()==null) || 
             (this.pageNumber!=null &&
              this.pageNumber.equals(other.getPageNumber()))) &&
            ((this.results==null && other.getResults()==null) || 
             (this.results!=null &&
              java.util.Arrays.equals(this.results, other.getResults()))) &&
            ((this.totalResults==null && other.getTotalResults()==null) || 
             (this.totalResults!=null &&
              this.totalResults.equals(other.getTotalResults())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getPageNumber() != null) {
            _hashCode += getPageNumber().hashCode();
        }
        if (getResults() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getResults());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getResults(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getTotalResults() != null) {
            _hashCode += getTotalResults().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ResultadoBusquedaWs.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "resultadoBusquedaWs"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("pageNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("", "pageNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("results");
        elemField.setXmlName(new javax.xml.namespace.QName("", "results"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "anyType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("totalResults");
        elemField.setXmlName(new javax.xml.namespace.QName("", "totalResults"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
