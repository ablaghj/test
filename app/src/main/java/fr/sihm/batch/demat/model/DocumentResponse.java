package fr.sihm.batch.demat.model;

/**
 * Created by blaghji-a on 29/01/2019.
 */
public class DocumentResponse {

    private String idTechnique;
    private String nomFichier;

    public String getIdTechnique() {
        return idTechnique;
    }

    public void setIdTechnique(String idTechnique) {
        this.idTechnique = idTechnique;
    }

    public String getNomFichier() {
        return nomFichier;
    }

    public void setNomFichier(String nomFichier) {
        this.nomFichier = nomFichier;
    }
}
