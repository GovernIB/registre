package es.caib.regweb3.ws.model;

import java.io.Serializable;
import java.util.List;

public class ResultadoBusquedaWs<T> implements Serializable {

    private Integer totalResults;
    private Integer pageNumber;
    private List<T> results;

    public ResultadoBusquedaWs() {
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Integer getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }

    public List<T> getResults() {
        return results;
    }

    public void setResults(List<T> results) {
        this.results = results;
    }
}
