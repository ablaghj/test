package fr.sihm.batch.demat.model;

import java.util.Date;

/**
 * Created by blaghji-a on 16/01/2019.
 */
public class HmCourrierDto {

    // Courrier (type d'objet: hm_courrier)
    private String objectName;
    // Titre (type d'objet: hm_courrier)
    private String title;
    // Domaine adhérent ou professionnel de santé (type d'objet: hm_courrier)
    private String domaine;
    // Type de document (type d'objet: hm_courrier)
    private String typeCourrier;
    // Date d'indexation (prestataire) (type d'objet: hm_courrier)
    private Date dateindex;
    // Titulaire du RIB (type d'objet: hm_courrier)
    private String titulaire;
    // Code IBAN (type d'objet: hm_courrier)
    private String iban;
    // Code BIC (type d'objet: hm_courrier)
    private String bic;
    // Nom du fichier (type d'objet: hm_courrier)
    private String filename;
    // Type du fichier
    private String mimetype;


    // Date de vidéocodage (HM) (type d'objet: hm_courrier)
    private Date datevideo;
    // Numéro adhérent (type d'objet: hm_courrier)
    private String numcp;
    // Nom adhérent (type d'objet: hm_courrier)
    private String nom;
    // Prénom adhérent (type d'objet: hm_courrier)
    private String prenom;
    // Date de naissance (type d'objet: hm_courrier)
    private Date datenaiss;
    // Numéro Contrat collectif (type d'objet: hm_courrier)
    private String numce;
    // Identifiant du PS
    private String numps;
    // Groupe d'assurés (type d'objet: hm_courrier)
    private String codegrp;
    // Numéro de SIRET (type d'objet: hm_courrier)
    private String siret;
    // Numéro de Régime Obligatoire (type d'objet: hm_courrier)
    private String numro;
    // PS conventionné réseau (type d'objet: hm_courrier)
    private String tphm;
    // Code ER (type d'objet: hm_courrier)
    private String CodeEr;
    // Référence d'archivage physique du document (type d'objet: hm_courrier)
    private String rarcd;
    // Login du vidéocodeur (type d'objet: hm_courrier)
    private String loginvideo;
    // Motif de rejet DEMAT (type d'objet: hm_courrier)
    private String rejetdemat;
    // Nom assuré (type d'objet: hm_courrier)
    private String nomassure;
    // Prénom assuré (type d'objet: hm_courrier)
    private String prenomassure;
    // Type de document détaillé (type d'objet: hm_courrier)
    private String typecourrierdetail;
    // Code postal (type d'objet: hm_courrier)
    private String codepostal;
    // Localité (type d'objet: hm_courrier)
    private String localite;
    // Date d'effet de l'adhésion (type d'objet: hm_courrier)
    private Date dateadhesion;
    // Numéro de facture (type d'objet: hm_courrier)
    private String facture;
    // Date de facture (type d'objet: hm_courrier)
    private Date datefacture;
    // Type de flux de paiement (type d'objet: hm_courrier)
    private String typefluxpaiement;
    // Raison sociale PS (type d'objet: hm_courrier)
    private String raisonsociale;
    // Code postal du PS (type d'objet: hm_courrier)
    private String codepostalps;
    // Type de document détaillé (type d'objet: hm_courrier)
    private String localiteps;
    // Numéro de bordereau (type d'objet: hm_courrier)
    private String numbordereau;
    // Code spécialité (type d'objet: hm_courrier)
    private String specialite;
    // Bordereau (oui/non) (type d'objet: hm_courrier)
    private String bordereau;
    // Commentaire (type d'objet: hm_courrier)
    private String comment;
    // Mode de paiement (type d'objet: hm_courrier)
    private String modepaiement;
    // Code jour de Prélèvement (type d'objet: hm_courrier)
    private String jourprel;
    // Code CG (type d'objet: hm_courrier)
    private String codecg;


    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDomaine() {
        return domaine;
    }

    public void setDomaine(String domaine) {
        this.domaine = domaine;
    }

    public String getTypeCourrier() {
        return typeCourrier;
    }

    public void setTypeCourrier(String typeCourrier) {
        this.typeCourrier = typeCourrier;
    }

    public Date getDateindex() {
        return dateindex;
    }

    public void setDateindex(Date dateindex) {
        this.dateindex = dateindex;
    }

    public String getTitulaire() {
        return titulaire;
    }

    public void setTitulaire(String titulaire) {
        this.titulaire = titulaire;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getBic() {
        return bic;
    }

    public void setBic(String bic) {
        this.bic = bic;
    }

    public Date getDatevideo() {
        return datevideo;
    }

    public void setDatevideo(Date datevideo) {
        this.datevideo = datevideo;
    }

    public String getNumcp() {
        return numcp;
    }

    public void setNumcp(String numcp) {
        this.numcp = numcp;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public Date getDatenaiss() {
        return datenaiss;
    }

    public void setDatenaiss(Date datenaiss) {
        this.datenaiss = datenaiss;
    }

    public String getNumce() {
        return numce;
    }

    public void setNumce(String numce) {
        this.numce = numce;
    }

    public String getCodegrp() {
        return codegrp;
    }

    public void setCodegrp(String codegrp) {
        this.codegrp = codegrp;
    }

    public String getSiret() {
        return siret;
    }

    public void setSiret(String siret) {
        this.siret = siret;
    }

    public String getNumro() {
        return numro;
    }

    public void setNumro(String numro) {
        this.numro = numro;
    }

    public String getTphm() {
        return tphm;
    }

    public void setTphm(String tphm) {
        this.tphm = tphm;
    }

    public String getCodeEr() {
        return CodeEr;
    }

    public void setCodeEr(String codeEr) {
        CodeEr = codeEr;
    }

    public String getRarcd() {
        return rarcd;
    }

    public void setRarcd(String rarcd) {
        this.rarcd = rarcd;
    }

    public String getLoginvideo() {
        return loginvideo;
    }

    public void setLoginvideo(String loginvideo) {
        this.loginvideo = loginvideo;
    }

    public String getRejetdemat() {
        return rejetdemat;
    }

    public void setRejetdemat(String rejetdemat) {
        this.rejetdemat = rejetdemat;
    }

    public String getNomassure() {
        return nomassure;
    }

    public void setNomassure(String nomassure) {
        this.nomassure = nomassure;
    }

    public String getPrenomassure() {
        return prenomassure;
    }

    public void setPrenomassure(String prenomassure) {
        this.prenomassure = prenomassure;
    }

    public String getTypecourrierdetail() {
        return typecourrierdetail;
    }

    public void setTypecourrierdetail(String typecourrierdetail) {
        this.typecourrierdetail = typecourrierdetail;
    }

    public String getCodepostal() {
        return codepostal;
    }

    public void setCodepostal(String codepostal) {
        this.codepostal = codepostal;
    }

    public String getLocalite() {
        return localite;
    }

    public void setLocalite(String localite) {
        this.localite = localite;
    }

    public Date getDateadhesion() {
        return dateadhesion;
    }

    public void setDateadhesion(Date dateadhesion) {
        this.dateadhesion = dateadhesion;
    }

    public String getFacture() {
        return facture;
    }

    public void setFacture(String facture) {
        this.facture = facture;
    }

    public Date getDatefacture() {
        return datefacture;
    }

    public void setDatefacture(Date datefacture) {
        this.datefacture = datefacture;
    }

    public String getTypefluxpaiement() {
        return typefluxpaiement;
    }

    public void setTypefluxpaiement(String typefluxpaiement) {
        this.typefluxpaiement = typefluxpaiement;
    }

    public String getRaisonsociale() {
        return raisonsociale;
    }

    public void setRaisonsociale(String raisonsociale) {
        this.raisonsociale = raisonsociale;
    }

    public String getCodepostalps() {
        return codepostalps;
    }

    public void setCodepostalps(String codepostalps) {
        this.codepostalps = codepostalps;
    }

    public String getLocaliteps() {
        return localiteps;
    }

    public void setLocaliteps(String localiteps) {
        this.localiteps = localiteps;
    }

    public String getNumbordereau() {
        return numbordereau;
    }

    public void setNumbordereau(String numbordereau) {
        this.numbordereau = numbordereau;
    }

    public String getSpecialite() {
        return specialite;
    }

    public void setSpecialite(String specialite) {
        this.specialite = specialite;
    }

    public String getBordereau() {
        return bordereau;
    }

    public void setBordereau(String bordereau) {
        this.bordereau = bordereau;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getModepaiement() {
        return modepaiement;
    }

    public void setModepaiement(String modepaiement) {
        this.modepaiement = modepaiement;
    }

    public String getJourprel() {
        return jourprel;
    }

    public void setJourprel(String jourprel) {
        this.jourprel = jourprel;
    }

    public String getCodecg() {
        return codecg;
    }

    public void setCodecg(String codecg) {
        this.codecg = codecg;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getMimetype() {
        return mimetype;
    }

    public void setMimetype(String mimetype) {
        this.mimetype = mimetype;
    }

    public String getNumps() {
        return numps;
    }

    public void setNumps(String numps) {
        this.numps = numps;
    }
}
