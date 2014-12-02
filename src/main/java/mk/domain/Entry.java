package mk.domain;

import javax.persistence.Entity;
import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.web.bind.annotation.RequestParam;

@Entity
public class Entry extends AbstractPersistable<Long> {
    
    private String yritys;
    private String osoite;
    private String kaupunki;
    private String postinumero;
    private String maa;
    private String puhelinnumero;
    private String sahkopostiosoite;
    private String viesti;

    public String getYritys() {
        return yritys;
    }

    public void setYritys(String yritys) {
        this.yritys = yritys;
    }

    public String getOsoite() {
        return osoite;
    }

    public void setOsoite(String osoite) {
        this.osoite = osoite;
    }

    public String getKaupunki() {
        return kaupunki;
    }

    public void setKaupunki(String kaupunki) {
        this.kaupunki = kaupunki;
    }

    public String getPostinumero() {
        return postinumero;
    }

    public void setPostinumero(String postinumero) {
        this.postinumero = postinumero;
    }

    public String getMaa() {
        return maa;
    }

    public void setMaa(String maa) {
        this.maa = maa;
    }

    public String getPuhelinnumero() {
        return puhelinnumero;
    }

    public void setPuhelinnumero(String puhelinnumero) {
        this.puhelinnumero = puhelinnumero;
    }

    public String getSahkopostiosoite() {
        return sahkopostiosoite;
    }

    public void setSahkopostiosoite(String sahkopostiosoite) {
        this.sahkopostiosoite = sahkopostiosoite;
    }

    public String getViesti() {
        return viesti;
    }

    public void setViesti(String viesti) {
        this.viesti = viesti;
    }
    
}
