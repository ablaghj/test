package fr.sihm.batch.demat.listener;

import fr.sihm.batch.model.ribxml.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemReadListener;

/**
 * Created by blaghji-a on 14/01/2019.
 */

/**
 * Listener du reader de chaque objet du fichier xml
 */

public class RibItemReadListener implements ItemReadListener<Objects> {

    private static Logger LOGGER = LoggerFactory.getLogger(RibItemReadListener.class);

    @Override
    public void beforeRead() {
    }

    @Override
    public void afterRead(Objects objectXmlItem) {
        LOGGER.debug("ItemReadListener apres lecture de la ligne : {}", objectXmlItem.getObject());
    }
    @Override
    public void onReadError(Exception e) {
        LOGGER.error("Erreur lors de la lecture : {}", e.getMessage());
    }
}
