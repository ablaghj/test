package fr.sihm.batch.demat.listener;

import java.io.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemReadListener;

/**
 * Created by blaghji-a on 21/01/2019.
 */
public class ZipItemReadListener implements ItemReadListener<File> {

    private static Logger LOGGER = LoggerFactory.getLogger(ZipItemReadListener.class);

    @Override
    public void beforeRead() {
    }

    @Override
    public void afterRead(File file) {
        LOGGER.debug("ItemReadListener apres lecture de la ligne : {}", file);
    }
    @Override
    public void onReadError(Exception e) {
        LOGGER.error("Erreur lors de la lecture : {}", e.getMessage());
    }
}