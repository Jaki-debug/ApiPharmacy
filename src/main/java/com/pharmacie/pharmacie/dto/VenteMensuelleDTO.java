package com.pharmacie.pharmacie.dto;

import java.math.BigDecimal;

public class VenteMensuelleDTO {
    private String mois;
    private BigDecimal total;

    public VenteMensuelleDTO(String mois, BigDecimal total) {
        this.mois = mois;
        this.total = total;
    }

    public String getMois() {
        return mois;
    }

    public void setMois(String mois) {
        this.mois = mois;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}
