package fr.sihm.batch.demat.model;

import java.util.Date;

/**
 * Created by blaghji-a on 16/01/2019.
 */
public class HmDemandeDto {

    // Numéro du pli (type d'objet: hm_demande)
    private String numpli;
    // Date d'arrivée du pli (prestataire) (type d'objet: hm_demande)
    private Date datedemat;
    // Date d'arrivée du pli (HM) (type d'objet: hm_demande)
    private Date datehm;
    // Pli recommandé (type d'objet: hm_demande)
    private String recom;
    // hm_traiteauto (type d'objet: hm_demande)
    private String traiteauto;
    // hm_horsged (type d'objet: hm_demande)
    private String horsged;
    // Document maitre (type d'objet: hm_demande)
    private String docmaitre;

    public String getNumpli() {
        return numpli;
    }

    public void setNumpli(String numpli) {
        this.numpli = numpli;
    }

    public Date getDatedemat() {
        return datedemat;
    }

    public void setDatedemat(Date datedemat) {
        this.datedemat = datedemat;
    }

    public Date getDatehm() {
        return datehm;
    }

    public void setDatehm(Date datehm) {
        this.datehm = datehm;
    }

    public String getRecom() {
        return recom;
    }

    public void setRecom(String recom) {
        this.recom = recom;
    }

    public String getTraiteauto() {
        return traiteauto;
    }

    public void setTraiteauto(String traiteauto) {
        this.traiteauto = traiteauto;
    }

    public String getHorsged() {
        return horsged;
    }

    public void setHorsged(String horsged) {
        this.horsged = horsged;
    }

    public String getDocmaitre() {
        return docmaitre;
    }

    public void setDocmaitre(String docmaitre) {
        this.docmaitre = docmaitre;
    }
}
