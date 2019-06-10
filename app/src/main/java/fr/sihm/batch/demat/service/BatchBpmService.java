package fr.sihm.batch.demat.service;

import com.tibco.n2.common.datamodel.DataModel;
import fr.sihm.mhmeservices.bpm.dto.AssureDTO;
import fr.sihm.mhmeservices.bpm.dto.DemandeurDTO;
import fr.sihm.mhmeservices.bpm.dto.RibDTO;
import fr.sihm.mhmeservices.bpm.exception.BpmFactoryException;
import fr.sihm.mhmeservices.bpm.exception.BpmServiceException;
import fr.sihm.mhmeservices.bpm.metier.enums.DemandeBpmType;
import fr.sihm.mhmeservices.bpm.metier.enums.ModificationRibType;
import fr.sihm.mhmeservices.bpm.metier.enums.StatutActuelRibType;
import fr.sihm.mhmeservices.bpm.metier.factory.DemandeSpecifiqueFactory;
import fr.sihm.mhmeservices.bpm.service.BpmService;
import fr.sihm.mhmeservices.ws.bpm.model.modifrib.DemandeModifRIB;
import fr.sihm.mhmeservices.ws.bpm.model.modifrib.DocumentList;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.xml.bind.JAXBElement;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BatchBpmService {

    public static final String BATCH_DEMAT = "DEMAT";
    private static Logger LOGGER_TRAITEMENT = LoggerFactory.getLogger("fr.batch");

    @Value("#{environment['ficeram.file.name']}")
    private String file;

    @Autowired
    private BpmService bpmService;

    @Autowired
    private DemandeSpecifiqueFactory demandeSpecifiqueFactory;


    /**
     * Appel de webservice BPM pour la modification du RIB
     * @param demandeurDTO
     * @param assureDTO
     * @param dateEffet
     * @param statutActuel
     * @param modificationType
     * @param ribDTO
     * @param dateMandatSEPA
     * @throws BpmServiceException
     */
    public void callModifRIB(DemandeurDTO demandeurDTO, AssureDTO assureDTO, Date dateEffet, StatutActuelRibType statutActuel, ModificationRibType modificationType, RibDTO ribDTO, Date dateMandatSEPA, DocumentList documentList) throws BpmServiceException {
        // creation de la demande specifique au format String
        DemandeModifRIB demandeSpecifique = demandeSpecifiqueFactory.createDemandeModifRIB(statutActuel, modificationType, ribDTO, dateMandatSEPA, new ArrayList<>(), documentList);
        JAXBElement<DemandeModifRIB> demandeSpecifiqueElement = demandeSpecifiqueFactory.createDemandeModifRIBElement(demandeSpecifique);
        String strDemandeSpecifique = bpmService.marshalDemandeSpecifique(demandeSpecifiqueElement, DemandeModifRIB.class);

        DataModel xmlPayload = bpmService.callCreationDemande(DemandeBpmType.RIB, demandeurDTO, assureDTO, toCalendar(dateEffet), strDemandeSpecifique, BATCH_DEMAT,false, null);

        // verification du message d'erreur
        bpmService.checkMessageErreur(xmlPayload.getOutputs());
        String idDemandeBpm = bpmService.getIdDemandeBpm(xmlPayload.getOutputs());
        LOGGER_TRAITEMENT.info("\tDemande  de modification de RIB créée avec l'identifiant suivant:\t" + idDemandeBpm);
    }


    public XMLGregorianCalendar getXmlGregorianCalendar(Date date) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            XMLGregorianCalendar xmlDate = DatatypeFactory.newInstance().newXMLGregorianCalendar();
            xmlDate.setYear(calendar.get(Calendar.YEAR));
            xmlDate.setMonth(calendar.get(Calendar.MONTH) + 1);
            xmlDate.setDay(calendar.get(Calendar.DAY_OF_MONTH));
            return xmlDate;
        } catch (DatatypeConfigurationException e) {
            throw new BpmFactoryException("Erreur lors de la creation de date.", e);
        }
    }

    public static Calendar toCalendar(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }


}
