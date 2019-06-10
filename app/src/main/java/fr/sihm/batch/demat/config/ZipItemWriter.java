package fr.sihm.batch.demat.config;

import java.io.File;
import java.util.List;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

/**
 * Created by blaghji-a on 21/01/2019.
 */
@Component
public class ZipItemWriter implements ItemWriter<List<File>> {
    @Override
    public void write(List<? extends List<File>> lotFiles) throws Exception {
    }
}
