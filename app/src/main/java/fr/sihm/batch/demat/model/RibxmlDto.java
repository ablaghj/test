package fr.sihm.batch.demat.model;

/**
 * Created by blaghji-a on 10/01/2019.
 */
public class RibxmlDto {

    private String identifiant;
    private HmCourrierDto courrier;
    private HmDemandeDto demande;

    public HmCourrierDto getCourrier() {
        return courrier;
    }

    public void setCourrier(HmCourrierDto courrier) {
        this.courrier = courrier;
    }

    public HmDemandeDto getDemande() {
        return demande;
    }

    public void setDemande(HmDemandeDto demande) {
        this.demande = demande;
    }

    public String getIdentifiant() {
        return identifiant;
    }

    public void setIdentifiant(String identifiant) {
        this.identifiant = identifiant;
    }
}
