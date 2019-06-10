package fr.sihm.batch.demat.processor;

import fr.sihm.batch.demat.config.RibConstants;
import fr.sihm.batch.demat.model.HmCourrierDto;
import fr.sihm.batch.demat.model.RibxmlDto;
import fr.sihm.batch.model.ribxml.Objects;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.datatype.XMLGregorianCalendar;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

/**
 * Created by blaghji-a on 10/01/2019.
 */
@Component
public class RibItemProcessor implements ItemProcessor<Objects, List<RibxmlDto>>{


    private static final Logger LOGGER = LoggerFactory.getLogger(RibItemProcessor.class);
    private static Logger LOGGER_TRAITEMENT = LoggerFactory.getLogger("fr.batch");

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static List<RibxmlDto> ribxmlDtoList = new ArrayList<>();
    @Override
    public List<RibxmlDto> process(Objects objectXmlItem) throws Exception {
        try {
            Map <String, Map<String, String>> objectsValuesFirstFile = getObjectsValuesFromFile(objectXmlItem);
            if(ribxmlDtoList.isEmpty()){
                // Traitement du premier fichier (*.xml)
                for (Objects.Object object : objectXmlItem.getObject()){
                    if("hm_courrier".equalsIgnoreCase(object.getType())){
                        RibxmlDto ribxmlDto = new RibxmlDto();
                        HmCourrierDto courrierDto = new HmCourrierDto();
                        Map<String, String> valuesFromFirstFile =  objectsValuesFirstFile.get(object.getIdentifier());
                        if(StringUtils.isNotBlank(valuesFromFirstFile.get(RibConstants.HM_DATEVIDEO_ATTRIBUTE))){
                            courrierDto.setDatevideo(sdf.parse(valuesFromFirstFile.get(RibConstants.HM_DATEVIDEO_ATTRIBUTE)));
                        }
                        if(StringUtils.isNotBlank(valuesFromFirstFile.get(RibConstants.HM_NUMCP_ATTRIBUTE))){
                            courrierDto.setNumcp(valuesFromFirstFile.get(RibConstants.HM_NUMCP_ATTRIBUTE));
                        }
                        if(StringUtils.isNotBlank(valuesFromFirstFile.get(RibConstants.HM_NOM_ATTRIBUTE))){
                            courrierDto.setNom(valuesFromFirstFile.get(RibConstants.HM_NOM_ATTRIBUTE));
                        }
                        if(StringUtils.isNotBlank(valuesFromFirstFile.get(RibConstants.HM_PRENOM_ATTRIBUTE))){
                            courrierDto.setPrenom(valuesFromFirstFile.get(RibConstants.HM_PRENOM_ATTRIBUTE));
                        }
                        if(StringUtils.isNotBlank(valuesFromFirstFile.get(RibConstants.HM_DATENAISS_ATTRIBUTE))){
                            courrierDto.setDatenaiss(sdf.parse(valuesFromFirstFile.get(RibConstants.HM_DATENAISS_ATTRIBUTE)));
                        }
                        if(StringUtils.isNotBlank(valuesFromFirstFile.get(RibConstants.HM_NUMCE_ATTRIBUTE))){
                            courrierDto.setNumce(valuesFromFirstFile.get(RibConstants.HM_NUMCE_ATTRIBUTE));
                        }
                        if(StringUtils.isNotBlank(valuesFromFirstFile.get(RibConstants.HM_NUMPS_ATTRIBUTE))){
                            courrierDto.setNumps(valuesFromFirstFile.get(RibConstants.HM_NUMPS_ATTRIBUTE));
                        }
                        if(StringUtils.isNotBlank(valuesFromFirstFile.get(RibConstants.HM_CODEGRP_ATTRIBUTE))){
                            courrierDto.setCodegrp(valuesFromFirstFile.get(RibConstants.HM_CODEGRP_ATTRIBUTE));
                        }
                        if(StringUtils.isNotBlank(valuesFromFirstFile.get(RibConstants.HM_SIRET_ATTRIBUTE))){
                            courrierDto.setSiret(valuesFromFirstFile.get(RibConstants.HM_SIRET_ATTRIBUTE));
                        }
                        if(StringUtils.isNotBlank(valuesFromFirstFile.get(RibConstants.HM_NUMRO_ATTRIBUTE))){
                            courrierDto.setNumro(valuesFromFirstFile.get(RibConstants.HM_NUMRO_ATTRIBUTE));
                        }
                        if(StringUtils.isNotBlank(valuesFromFirstFile.get(RibConstants.HM_TPHM_ATTRIBUTE))){
                            courrierDto.setTphm(valuesFromFirstFile.get(RibConstants.HM_TPHM_ATTRIBUTE));
                        }
                        if(StringUtils.isNotBlank(valuesFromFirstFile.get(RibConstants.HM_ER_ATTRIBUTE))){
                            courrierDto.setCodeEr(valuesFromFirstFile.get(RibConstants.HM_ER_ATTRIBUTE));
                        }
                        if(StringUtils.isNotBlank(valuesFromFirstFile.get(RibConstants.HM_RARCD_ATTRIBUTE))){
                            courrierDto.setRarcd(valuesFromFirstFile.get(RibConstants.HM_RARCD_ATTRIBUTE));
                        }
                        if(StringUtils.isNotBlank(valuesFromFirstFile.get(RibConstants.HM_LOGINVIDEO_ATTRIBUTE))){
                            courrierDto.setLoginvideo(valuesFromFirstFile.get(RibConstants.HM_LOGINVIDEO_ATTRIBUTE));
                        }
                        if(StringUtils.isNotBlank(valuesFromFirstFile.get(RibConstants.HM_REJETDEMAT_ATTRIBUTE))){
                            courrierDto.setRejetdemat(valuesFromFirstFile.get(RibConstants.HM_REJETDEMAT_ATTRIBUTE));
                        }
                        if(StringUtils.isNotBlank(valuesFromFirstFile.get(RibConstants.HM_NOMASSURE_ATTRIBUTE))){
                            courrierDto.setNomassure(valuesFromFirstFile.get(RibConstants.HM_NOMASSURE_ATTRIBUTE));
                        }
                        if(StringUtils.isNotBlank(valuesFromFirstFile.get(RibConstants.HM_PRENOMASSURE_ATTRIBUTE))){
                            courrierDto.setPrenomassure(valuesFromFirstFile.get(RibConstants.HM_PRENOMASSURE_ATTRIBUTE));
                        }
                        if(StringUtils.isNotBlank(valuesFromFirstFile.get(RibConstants.HM_TYPECOURRIERDETAIL_ATTRIBUTE))){
                            courrierDto.setTypecourrierdetail(valuesFromFirstFile.get(RibConstants.HM_TYPECOURRIERDETAIL_ATTRIBUTE));
                        }
                        if(StringUtils.isNotBlank(valuesFromFirstFile.get(RibConstants.HM_CODEPOSTAL_ATTRIBUTE))){
                            courrierDto.setCodepostal(valuesFromFirstFile.get(RibConstants.HM_CODEPOSTAL_ATTRIBUTE));
                        }
                        if(StringUtils.isNotBlank(valuesFromFirstFile.get(RibConstants.HM_LOCALITE_ATTRIBUTE))){
                            courrierDto.setLocalite(valuesFromFirstFile.get(RibConstants.HM_LOCALITE_ATTRIBUTE));
                        }
                        if(StringUtils.isNotBlank(valuesFromFirstFile.get(RibConstants.HM_DATEADHESION_ATTRIBUTE))){
                            courrierDto.setDateadhesion(sdf.parse(valuesFromFirstFile.get(RibConstants.HM_DATEADHESION_ATTRIBUTE)));
                        }
                        if(StringUtils.isNotBlank(valuesFromFirstFile.get(RibConstants.HM_FACTURE_ATTRIBUTE))){
                            courrierDto.setFacture(valuesFromFirstFile.get(RibConstants.HM_FACTURE_ATTRIBUTE));
                        }
                        if(StringUtils.isNotBlank(valuesFromFirstFile.get(RibConstants.HM_DATE_FACTURE_ATTRIBUTE))){
                            courrierDto.setDatefacture(sdf.parse(valuesFromFirstFile.get(RibConstants.HM_DATE_FACTURE_ATTRIBUTE)));
                        }
                        if(StringUtils.isNotBlank(valuesFromFirstFile.get(RibConstants.HM_TYPE_FLUX_PAIEMENT_ATTRIBUTE))){
                            courrierDto.setTypefluxpaiement(valuesFromFirstFile.get(RibConstants.HM_TYPE_FLUX_PAIEMENT_ATTRIBUTE));
                        }
                        if(StringUtils.isNotBlank(valuesFromFirstFile.get(RibConstants.HM_RAISON_SOCIALE_ATTRIBUTE))){
                            courrierDto.setRaisonsociale(valuesFromFirstFile.get(RibConstants.HM_RAISON_SOCIALE_ATTRIBUTE));
                        }
                        if(StringUtils.isNotBlank(valuesFromFirstFile.get(RibConstants.HM_CODE_POSTAL_PS_ATTRIBUTE))){
                            courrierDto.setCodepostalps(valuesFromFirstFile.get(RibConstants.HM_CODE_POSTAL_PS_ATTRIBUTE));
                        }
                        if(StringUtils.isNotBlank(valuesFromFirstFile.get(RibConstants.HM_LOCALITE_PS_ATTRIBUTE))){
                            courrierDto.setLocaliteps(valuesFromFirstFile.get(RibConstants.HM_LOCALITE_PS_ATTRIBUTE));
                        }
                        if(StringUtils.isNotBlank(valuesFromFirstFile.get(RibConstants.HM_NUM_BORDEREAU_ATTRIBUTE))){
                            courrierDto.setNumbordereau(valuesFromFirstFile.get(RibConstants.HM_NUM_BORDEREAU_ATTRIBUTE));
                        }
                        if(StringUtils.isNotBlank(valuesFromFirstFile.get(RibConstants.HM_SPECIALITE_ATTRIBUTE))){
                            courrierDto.setSpecialite(valuesFromFirstFile.get(RibConstants.HM_SPECIALITE_ATTRIBUTE));
                        }
                        if(StringUtils.isNotBlank(valuesFromFirstFile.get(RibConstants.HM_BORDEREAU_ATTRIBUTE))){
                            courrierDto.setBordereau(valuesFromFirstFile.get(RibConstants.HM_BORDEREAU_ATTRIBUTE));
                        }
                        if(StringUtils.isNotBlank(valuesFromFirstFile.get(RibConstants.HM_COMMENT_ATTRIBUTE))){
                            courrierDto.setComment(valuesFromFirstFile.get(RibConstants.HM_COMMENT_ATTRIBUTE));
                        }
                        if(StringUtils.isNotBlank(valuesFromFirstFile.get(RibConstants.HM_MODEPAIEMENT_ATTRIBUTE))){
                            courrierDto.setModepaiement(valuesFromFirstFile.get(RibConstants.HM_MODEPAIEMENT_ATTRIBUTE));
                        }
                        if(StringUtils.isNotBlank(valuesFromFirstFile.get(RibConstants.HM_JOURPRE_ATTRIBUTE))){
                            courrierDto.setJourprel(valuesFromFirstFile.get(RibConstants.HM_JOURPRE_ATTRIBUTE));
                        }
                        if(StringUtils.isNotBlank(valuesFromFirstFile.get(RibConstants.HM_CG_ATTRIBUTE))){
                            courrierDto.setCodecg(valuesFromFirstFile.get(RibConstants.HM_CG_ATTRIBUTE));
                        }
                        if(object.getFeatures() != null && object.getFeatures().getFeature() != null && object.getFeatures().getFeature().getContentspec() != null && object.getFeatures().getFeature().getContentspec().getPrimarycontent() != null){
                            courrierDto.setFilename(object.getFeatures().getFeature().getContentspec().getPrimarycontent().getFilename());
                            courrierDto.setMimetype(object.getFeatures().getFeature().getContentspec().getPrimarycontent().getMimetype());
                        }
                        ribxmlDto.setCourrier(courrierDto);
                        ribxmlDto.setIdentifiant(object.getIdentifier());
                        ribxmlDtoList.add(ribxmlDto);
                    }
                }
            } else {
                // Traitement du deuxième fichier (*BPM.xml)
                Map <String, Map<String, String>> objectsValuesSecondFile = getObjectsValuesFromFile(objectXmlItem);
                for (RibxmlDto ribxmlDto : ribxmlDtoList){
                    Map<String, String> valuesFromSecondFile =  objectsValuesSecondFile.get(ribxmlDto.getIdentifiant());
                    if (valuesFromSecondFile != null) {
                        if(StringUtils.isNotBlank(valuesFromSecondFile.get(RibConstants.OBJECT_NAME_ATTRIBUTE))){
                            ribxmlDto.getCourrier().setObjectName(valuesFromSecondFile.get(RibConstants.OBJECT_NAME_ATTRIBUTE));
                        }
                        if(StringUtils.isNotBlank(valuesFromSecondFile.get(RibConstants.TITLE_ATTRIBUTE))){
                            ribxmlDto.getCourrier().setTitle(valuesFromSecondFile.get(RibConstants.TITLE_ATTRIBUTE));
                        }
                        if(StringUtils.isNotBlank(valuesFromSecondFile.get(RibConstants.HM_DOMAINE_ATTRIBUTE))){
                            ribxmlDto.getCourrier().setDomaine(valuesFromSecondFile.get(RibConstants.HM_DOMAINE_ATTRIBUTE));
                        }
                        if(StringUtils.isNotBlank(valuesFromSecondFile.get(RibConstants.HM_TYPECOURRIER_ATTRIBUTE))){
                            ribxmlDto.getCourrier().setTypeCourrier(valuesFromSecondFile.get(RibConstants.HM_TYPECOURRIER_ATTRIBUTE));
                        }
                        if(StringUtils.isNotBlank(valuesFromSecondFile.get(RibConstants.HM_DATEINDEX_ATTRIBUTE))){
                            ribxmlDto.getCourrier().setDateindex(sdf.parse(valuesFromSecondFile.get(RibConstants.HM_DATEINDEX_ATTRIBUTE)));
                        }
                        if(StringUtils.isNotBlank(valuesFromSecondFile.get(RibConstants.HM_TITULAIRE_ATTRIBUTE))){
                            ribxmlDto.getCourrier().setTitulaire(valuesFromSecondFile.get(RibConstants.HM_TITULAIRE_ATTRIBUTE));
                        }
                        if(StringUtils.isNotBlank(valuesFromSecondFile.get(RibConstants.HM_IBAN_ATTRIBUTE))){
                            ribxmlDto.getCourrier().setIban(valuesFromSecondFile.get(RibConstants.HM_IBAN_ATTRIBUTE));
                        }
                        if(StringUtils.isNotBlank(valuesFromSecondFile.get(RibConstants.HM_BIC_ATTRIBUTE))){
                            ribxmlDto.getCourrier().setBic(valuesFromSecondFile.get(RibConstants.HM_BIC_ATTRIBUTE));
                        }
                    }
                }
            }
        } catch (Exception e){
            LOGGER_TRAITEMENT.error("Exception: "+ e + ribxmlDtoList);
        } finally {
            LOGGER.debug("END: transformation le la ligne {}", objectXmlItem);
        }
        return ribxmlDtoList;
    }

    /**
     * Méthode de récupération des valeurs (hm_courrier Datainstances)
     * @param objectXmlItem
     * @return objects
     */
    private Map <String, Map<String, String>> getObjectsValuesFromFile(Objects objectXmlItem) {
        Map <String, Map<String, String>> objects = new HashMap<>() ;
        for (Objects.Object object : objectXmlItem.getObject()){
            HashMap <String, String> datainstances = new HashMap<>();
            if(!objects.containsKey(object.getIdentifier()) && "hm_courrier".equalsIgnoreCase(object.getType())) {
                for (Objects.Object.Datainstances.Datainstance datainstance : object.getDatainstances().getDatainstance()){
                    if(!datainstances.containsKey(datainstance.getName())) {
                        if(StringUtils.isNotBlank(datainstance.getDatavalue().getStringdata())){
                            datainstances.put(datainstance.getName(), datainstance.getDatavalue().getStringdata());
                        } else if(datainstance.getDatavalue().getDatedata() != null){
                            datainstances.put(datainstance.getName(), convertXmlGregorianToString(datainstance.getDatavalue().getDatedata()));
                        }
                    }
                }
                objects.put(object.getIdentifier(), datainstances);
            }
        }
        return objects;
    }



    public static String convertXmlGregorianToString(XMLGregorianCalendar xc)
    {
        GregorianCalendar gCalendar = xc.toGregorianCalendar();
        String dateString = sdf.format(gCalendar.getTime());
        return dateString;
    }


}
