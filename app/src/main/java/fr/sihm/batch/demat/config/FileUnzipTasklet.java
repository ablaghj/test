package fr.sihm.batch.demat.config;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

/**
 * Created by blaghji-a on 20/03/2019.
 */
public class FileUnzipTasklet implements Tasklet, InitializingBean {

    private static Logger LOGGER_TRAITEMENT = LoggerFactory.getLogger("fr.batch");

    @Value("#{environment['extract.directory']}")
    private String exctractDirectory;

    @Value("#{environment['hm.batch.directory.path']}")
    private String directoryFileName;

    @Override
    public void afterPropertiesSet() throws Exception {
    }

    @Override
    public RepeatStatus execute(StepContribution contribution,
                                ChunkContext chunkContext) throws Exception {
        Resource[] resources;
        ExistFileConfiguration.listPath = new ArrayList<>();
        String fileName = chunkContext.getStepContext().getStepExecution()
                .getJobParameters().getString("fileName");
        LOGGER_TRAITEMENT.info("######################## Traitement du fichier: " + fileName + "########################");
        String dirFileName = directoryFileName  + System.getProperty("file.separator")  + fileName;
        if(StringUtils.isNotBlank(dirFileName)){
            try {
                File parentFile = new File(dirFileName);
                File extractDirectoryFile = new File(FilenameUtils.removeExtension(exctractDirectory + System.getProperty("file.separator")  + parentFile.getName()));
                if (extractDirectoryFile.exists()){
                    FileUtils.deleteQuietly(extractDirectoryFile);
                }
                extractDirectoryFile.mkdir();
                ZipFile zipFile = new ZipFile(parentFile);
                zipFile.extractAll(extractDirectoryFile.getAbsolutePath());
                String zipFilesPattern = "file:" + extractDirectoryFile.getAbsolutePath() + System.getProperty("file.separator") + "*.zip";
                PathMatchingResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
                resources = resourcePatternResolver.getResources(zipFilesPattern.replace("\\", "/"));
                if(resources != null && resources.length > 0){
                    for (Resource resource : resources){
                        ExistFileConfiguration.listPath.add(resource.getFile().getAbsolutePath());
                    }
                }

            } catch (ZipException exception){
                LOGGER_TRAITEMENT.error("Une erreur s'est produite lors de l'extraction du zip parent: " + exception);
            } catch (IOException exception){
                LOGGER_TRAITEMENT.error("Une erreur s'est produite lors de la récupération des zips: " + exception);
            } catch (Exception ex){
                LOGGER_TRAITEMENT.error("Une erreur s'est produite: " + ex);
            }
        }
        return RepeatStatus.FINISHED;
    }

}