package fr.sihm.batch.demat.config;

import fr.sihm.batch.demat.model.DocumentResponse;
import fr.sihm.batch.demat.model.RibxmlDto;
import fr.sihm.batch.demat.service.BatchBpmService;
import fr.sihm.batch.demat.service.DocumentService;
import fr.sihm.mhmeservices.bpm.dto.AssureDTO;
import fr.sihm.mhmeservices.bpm.dto.DemandeurDTO;
import fr.sihm.mhmeservices.bpm.dto.IbanDTO;
import fr.sihm.mhmeservices.bpm.dto.RibDTO;
import fr.sihm.mhmeservices.bpm.metier.enums.CiviliteType;
import fr.sihm.mhmeservices.bpm.metier.enums.ModificationRibType;
import fr.sihm.mhmeservices.bpm.metier.enums.StatutActuelRibType;
import fr.sihm.mhmeservices.bpm.metier.factory.DemandeSpecifiqueFactory;
import fr.sihm.mhmeservices.ws.bpm.model.modifrib.Document;
import fr.sihm.mhmeservices.ws.bpm.model.modifrib.DocumentList;
import fr.sihm.mhmeservices.ws.bpm.model.modifrib.ObjectFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by blaghji-a on 14/01/2019.
 */

/**
 * ItemWriter permettant de créer des demandes de modification de RIB vers ACDC
 */
@StepScope
@Component
public class RibItemWriter implements ItemWriter<List<RibxmlDto>> {

    private static Logger LOGGER = LoggerFactory.getLogger("fr.batch");
    public static final String TIRET = "-";
    public static final String ZERO = "0";
    public static final String CODE_DEVISE_EURO = "EUR";


    @Autowired
    private DocumentService documentService;
    @Autowired
    private BatchBpmService batchBpmService;
    @Autowired
    private DemandeSpecifiqueFactory demandeSpecifiqueFactory;

    @Value("#{environment['rejet.directory']}")
    private String rejetDirectory;

    public static Integer nbrDemandes = 0;

    @Override
    public void write(List<? extends List<RibxmlDto>> list) throws Exception {
        for (List<RibxmlDto> ribxmlDto : list) {
            File currentZipFile = new File(ExistFileConfiguration.currentFile);
            File currentDirectory = new File(FilenameUtils.removeExtension(ExistFileConfiguration.currentFile));
            InputStream file = null;
            LOGGER.info("{} (nb elements à traiter= {} sous le répertoire= {})", currentZipFile.getName(), ribxmlDto.size(), currentDirectory.getAbsolutePath());
            for (RibxmlDto ribDtoXml : ribxmlDto){
                try {
                    //Appel au service infoArchive via api-demande
                    String pieceJointe = currentDirectory + System.getProperty("file.separator") + ribDtoXml.getCourrier().getFilename();
                    file = new FileInputStream(pieceJointe);
                    DocumentResponse documentResponse = documentService.injectDocument(pieceJointe, ribDtoXml.getCourrier().getMimetype(), file);
                    if(documentResponse != null){
                        String idDocument = documentService.getDataFromAutoSignedId(documentResponse.getIdTechnique() , DocumentService.BATCH_RIB);
                        //String idDocument = "080186a2803b38a8803b3987:ci:46";
                        //Appel au service ACDC
                        DemandeurDTO demandeurDTO = new DemandeurDTO();
                        if(StringUtils.isNotBlank(ribDtoXml.getCourrier().getNumce())){
                            demandeurDTO.setNumContratCollInfinite(ribDtoXml.getCourrier().getNumce());
                        } else {
                            // Contrat individuel
                            demandeurDTO.setNumContratCollInfinite("GSP");
                        }
                        demandeurDTO.setNumPersonGRC(ZERO);
                        if(StringUtils.isNotBlank(ribDtoXml.getCourrier().getNom())){
                            demandeurDTO.setNom(ribDtoXml.getCourrier().getNom());
                        } else {
                            demandeurDTO.setNom(TIRET);
                        }
                        if(StringUtils.isNotBlank(ribDtoXml.getCourrier().getPrenom())){
                            demandeurDTO.setPrenom(ribDtoXml.getCourrier().getPrenom());
                        }

                        if(StringUtils.isNotBlank(ribDtoXml.getCourrier().getNumro())){
                            demandeurDTO.setNumeroRO(ribDtoXml.getCourrier().getNumro());
                        } else {
                            demandeurDTO.setNumeroRO(ZERO);
                        }
                        demandeurDTO.setCivilite(CiviliteType.BATCH);
                        if(StringUtils.isNotBlank(ribDtoXml.getCourrier().getCodegrp())){
                            demandeurDTO.setCodeGroupeAssure(ribDtoXml.getCourrier().getCodegrp());
                        } else {
                            demandeurDTO.setCodeGroupeAssure(TIRET);
                        }
                        //Infos Assuré
                        AssureDTO assureDTO = new AssureDTO();
                        if(StringUtils.isNotBlank(ribDtoXml.getCourrier().getNomassure())){
                            assureDTO.setNom(ribDtoXml.getCourrier().getNomassure());
                        } else {
                            assureDTO.setNom(TIRET);
                        }
                        if(StringUtils.isNotBlank(ribDtoXml.getCourrier().getPrenomassure())){
                            assureDTO.setPrenom(ribDtoXml.getCourrier().getPrenomassure());
                        } else {
                            assureDTO.setPrenom(TIRET);
                        }
                        assureDTO.setCivilite(CiviliteType.BATCH);
                        if(StringUtils.isNotBlank(ribDtoXml.getCourrier().getNumcp())){
                            assureDTO.setNumContratIndivInfinite(ribDtoXml.getCourrier().getNumcp());
                        } else {
                            assureDTO.setNumContratIndivInfinite(ZERO);
                        }
                        RibDTO ribDTO = new RibDTO();
                        if(StringUtils.isNotBlank(ribDtoXml.getCourrier().getIban()) && StringUtils.isNotBlank(ribDtoXml.getCourrier().getBic())){
                            ribDTO = demandeSpecifiqueFactory.createRib(ribDtoXml.getCourrier().getIban(), ribDtoXml.getCourrier().getBic(), CODE_DEVISE_EURO, ribDtoXml.getCourrier().getTitulaire());
                        } else {
                            IbanDTO ibanDTO = new IbanDTO();
                            ibanDTO.setCodePays(TIRET);
                            ibanDTO.setCleIBAN(0);
                            ibanDTO.setCodeBanque(TIRET);
                            ibanDTO.setCodeGuichet(TIRET);
                            ibanDTO.setNumeroCompte(TIRET);
                            ibanDTO.setCleRIB(TIRET);
                            ribDTO.setIban(ibanDTO);
                            ribDTO.setBic(TIRET);
                            ribDTO.setCodeDevise(TIRET);
                            ribDTO.setTitulaireDuCompte(TIRET);
                        }
                        ObjectFactory ribObjectFactory = new ObjectFactory();
                        DocumentList documentList = ribObjectFactory.createDocumentList();
                        Document document = ribObjectFactory.createDocument();
                        document.setIdDocument(idDocument);
                        document.setDateEnvoie(batchBpmService.getXmlGregorianCalendar(new Date()));
                        document.setNomDocument(ribDtoXml.getCourrier().getFilename());
                        documentList.getDocument().add(document);
                        batchBpmService.callModifRIB(demandeurDTO, assureDTO, new Date(),  StatutActuelRibType.PAR_DEFAUT,  ModificationRibType.DEFAULT, ribDTO, new Date(), documentList);
                        nbrDemandes++;
                    }
                } catch (Exception e) {
                    LOGGER.error("\tUne erreur s'est produite, le fichier {} n'a pas pu être envoyé vers ACDC:\t"+ e +"):", ribDtoXml.getCourrier().getFilename());
                    String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                    File rejetDirectoryFile = new File(rejetDirectory + System.getProperty("file.separator") + FilenameUtils.removeExtension(currentZipFile.getName()) + "_" + timestamp);
                    if (rejetDirectoryFile.exists()){
                        FileUtils.deleteQuietly(rejetDirectoryFile);
                    }
                    rejetDirectoryFile.mkdir();
                    FileUtils.copyFileToDirectory(currentZipFile ,rejetDirectoryFile);

                } finally {
                    if(file != null){
                        file.close();
                    }
                }
            }
        }
    }



}
