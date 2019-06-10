package fr.sihm.batch.demat.listener;

import fr.sihm.batch.demat.model.RibxmlDto;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemWriteListener;

/**
 * Created by blaghji-a on 14/01/2019.
 */

/**
 * Listener du writer des donnees formatees d'une ligne du fichier FiceramDto
 */
public class RibItemWriteListener implements ItemWriteListener<RibxmlDto> {

    private static Logger LOGGER = LoggerFactory.getLogger(RibItemWriteListener.class);

    @Override
    public void beforeWrite(List<? extends RibxmlDto> list) {
        LOGGER.debug("ItemWriteListener avant ecriture");
    }

    @Override
    public void afterWrite(List<? extends RibxmlDto> list) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("ItemWriteListener apres ecriture (nb elements = {})", list.size());
            for (RibxmlDto ribxmlDto : list) {
                LOGGER.debug("\t- de la ligne : {} {}", ribxmlDto);
            }
        }
    }

    @Override
    public void onWriteError(Exception e, List<? extends RibxmlDto> list) {
        LOGGER.warn("Erreur lors de l'ecriture : {}", e.getMessage());
    }
}
