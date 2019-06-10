package fr.sihm.batch.demat.processor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import net.lingala.zip4j.core.ZipFile;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by blaghji-a on 21/01/2019.
 */
@Component
@Scope(value = "step")
public class ZipItemProcessor implements ItemProcessor<File, List<File>> {

    @Value("#{environment['extract.directory']}")
    private String exctractDirectory;

    @Value("#{jobParameters['fileName']}")
    private String fileName;



    @Override
    public List<File> process(File file) throws Exception {
        File parentFile = new File(fileName);
        File parentDirectoryFile = new File(FilenameUtils.removeExtension(exctractDirectory + System.getProperty("file.separator")  + parentFile.getName()));
        if (parentDirectoryFile.exists()){
            File subDirectoryFile = new File( FilenameUtils.removeExtension(parentDirectoryFile.getAbsolutePath() + System.getProperty("file.separator")  + file.getName()));

            if (subDirectoryFile.exists()){
                FileUtils.deleteQuietly(subDirectoryFile);
            }
            subDirectoryFile.mkdir();
            ZipFile zipFile = new ZipFile(file);
            zipFile.extractAll(subDirectoryFile.getAbsolutePath());
        }
        List<File> files = new ArrayList<>();
        files.add(file);
        return files;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
