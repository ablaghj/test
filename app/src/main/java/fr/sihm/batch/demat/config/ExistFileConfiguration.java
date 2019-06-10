package fr.sihm.batch.demat.config;

import fr.hm.fwk.batch.config.BatchProperties;
import fr.hm.fwk.batch.directory.EnableHmDirectoryBatchProcessing;
import fr.sihm.batch.demat.listener.RibItemReadListener;
import fr.sihm.batch.demat.listener.RibItemWriteListener;
import fr.sihm.batch.demat.listener.ZipItemReadListener;
import fr.sihm.batch.demat.model.RibxmlDto;
import fr.sihm.batch.demat.processor.RibItemProcessor;
import fr.sihm.batch.demat.processor.ZipItemProcessor;
import fr.sihm.batch.demat.service.BatchBpmService;
import fr.sihm.batch.demat.service.DocumentService;
import fr.sihm.batch.model.ribxml.Objects;
import fr.sihm.mhmeservices.bpm.metier.factory.BpmBusinessSoapRequestFactory;
import fr.sihm.mhmeservices.bpm.metier.factory.BpmServiceFactory;
import fr.sihm.mhmeservices.bpm.metier.factory.DemandeSpecifiqueFactory;
import fr.sihm.mhmeservices.bpm.metier.factory.impl.BpmBusinessSoapRequestFactoryImpl;
import fr.sihm.mhmeservices.bpm.metier.factory.impl.BpmServiceFactoryImpl;
import fr.sihm.mhmeservices.bpm.metier.factory.impl.DemandeSpecifiqueFactoryImpl;
import fr.sihm.mhmeservices.bpm.service.BpmService;
import fr.sihm.mhmeservices.ws.BpmBusinessService;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PreDestroy;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.FlowJobBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
@EnableHmDirectoryBatchProcessing
/**
 * Classe définissant un job lors de l'existance de fichier .zip dans un répertoire.
 */
public class ExistFileConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExistFileConfiguration.class);
    private static Logger LOGGER_TRAITEMENT = LoggerFactory.getLogger("fr.batch");

    @Value("#{environment['extract.directory']}")
    private String exctractDirectory;

    @Value("#{environment['bpm.businessService.ws.endpoint.url']}")
    private String bpmServerUrl;

    @Value("#{environment['bpm.businessService.ws.username']}")
    private String bpmServerUsername;

    @Value("#{environment['bpm.businessService.ws.password']}")
    private String bpmServerPassword;

    @Value("#{environment['archive.directory']}")
    private String archiveDirectory;


    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    private final BatchProperties batchProperties;

    public static String currentFile = StringUtils.EMPTY;

    public static List <String> listPath = new ArrayList<>();


    public ExistFileConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory, BatchProperties batchProperties){
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.batchProperties = batchProperties;
    }


    @PreDestroy
    public void destroy() throws NoSuchJobException {
    }


    @StepScope
    @Bean
    public DirectoryItemReader directoryItemReader() {
        if(!listPath.isEmpty()){
            currentFile = listPath.remove(0).replace("\\", "/");
        }
        return new DirectoryItemReader("file:" + currentFile);
    }


    @StepScope
    @Bean
    public StaxEventItemReader<Objects> xmlSecondFileItemReader(@Value("#{jobParameters['fileName']}") String fileName){
        Resource[] resources = getResourcesXml("*BPM.xml", fileName);
        return xmlFileItemReader(resources[0]);
    }


    @StepScope
    @Bean
    public StaxEventItemReader<Objects> xmlFirstFileItemReader(@Value("#{jobParameters['fileName']}") String fileName) {
        Resource[] allResourcesXml = getResourcesXml("*.xml", fileName);
        Resource[] resources = new Resource[allResourcesXml.length];
        int index = 0;
        for(Resource resourceXml: allResourcesXml){
            if (!isResourceBPM(resourceXml, fileName)){
                resources[index] = resourceXml;
                index++;
            }
        }
        return xmlFileItemReader(resources[0]);
    }


    @StepScope
    @Bean
    public ItemProcessor<Objects, List<RibxmlDto>> processorRibFirstFile() {
        LOGGER.debug("definition du bean processorRib.");
        RibItemProcessor.ribxmlDtoList.clear();
        return new RibItemProcessor();
    }


    @StepScope
    @Bean
    public ItemProcessor<Objects, List<RibxmlDto>> processorRibSecondFile() {
        LOGGER.debug("definition du bean processorRib.");
        return new RibItemProcessor();
    }


    @StepScope
    @Bean
    public ItemProcessor<File, List<File>> processorFirstZipFiles() {
        LOGGER.debug("definition du bean processor des zips.");
        return new ZipItemProcessor();
    }


    @StepScope
    @Bean
    public ItemProcessor<File, List<File>> processorZipFiles() {
        LOGGER.debug("definition du bean processor des zips.");
        return new ZipItemProcessor();
    }


    @StepScope
    @Bean
    public ItemWriter<List<RibxmlDto>> writerRib() {
        LOGGER.debug("definition du bean writer.");
        return new RibItemWriter();
    }


    @StepScope
    @Bean
    public ItemWriter<List<File>> writerZipFiles() {
        LOGGER.debug("definition du bean writer.");
        return new ZipItemWriter();
    }


    /**
     * Job de batch acdc-demat
     */
    @Bean
    public Job jobExistingFile(@Qualifier("stepUnzipParentFile") Step stepUnzipParent,
                               @Qualifier("stepUnzipFiles") Step stepUnzipFile, @Qualifier("stepReadFirstFileXml") Step stepReadFirstFile, @Qualifier("stepReadSecondFileXml") Step stepReadSecondFile, @Qualifier("stepDeleteFiles") Step stepDelete) {

        LOGGER.debug("definition du bean");
        JobBuilder jobBuilder = jobBuilderFactory.get("existingFile").incrementer(new RunIdIncrementer());
        StepDecider decider = new StepDecider();
        Flow flow = new FlowBuilder<Flow>("stepflow")
                .start(stepUnzipFile).next(stepReadFirstFile).next(stepReadSecondFile).next(decider).on("PURSUE").to(stepUnzipFile)
                .from(decider).on("DONE").end("COMPLETED").build();

        FlowJobBuilder builder = jobBuilder
                .flow(stepUnzipParent)
                .next(flow)
                .next(stepDelete)
                .end();
        return builder.build();
    }


    @Bean
    public Step stepDeleteFiles() {
        return stepBuilderFactory.get("deleteFileStep")
                .tasklet((contribution, chunkContext) -> {
                    // Déplacement du fichier parent dans le répertoire d'archive
                    String fileName = (String)chunkContext.getStepContext().getJobParameters().get("fileName");
                    String fileUrl = batchProperties.getDirectory().getPath() + System.getProperty("file.separator") + fileName;
                    File parentFile = new File(fileName);
                    String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                    File archiveDirectoryFile = new File(archiveDirectory + System.getProperty("file.separator") + FilenameUtils.removeExtension(parentFile.getName()) + "_" + timestamp);
                    if (archiveDirectoryFile.exists()){
                        FileUtils.deleteQuietly(archiveDirectoryFile);
                    }
                    FileUtils.moveFileToDirectory(new File(fileUrl), archiveDirectoryFile, true);
                    // Suppression du répertoire temp

                    File extractDirectoryFile = new File(FilenameUtils.removeExtension(exctractDirectory + System.getProperty("file.separator")  + parentFile.getName()));
                    if (extractDirectoryFile.exists()){
                        FileUtils.deleteQuietly(extractDirectoryFile);
                    }
                    return RepeatStatus.FINISHED;
                }).build();
    }


    @Bean
    public Step stepUnzipParentFile() {
        LOGGER.debug("definition du bean stepUnzipParentFile");
        return stepBuilderFactory.get(Constants.STEP_UNZIP_FILE_PARENT).tasklet(unzipTasklet()) .build();
    }


    @StepScope
    @Bean
    public FileUnzipTasklet unzipTasklet() {
        LOGGER.debug("definition du bean unzipTasklet.");
        return new FileUnzipTasklet();
    }


    @Bean
    public Step stepUnzipFiles() {
        LOGGER.debug("definition du bean stepUnzipFiles");
        return stepBuilderFactory.get(Constants.STEP_UNZIP_FILE).<File, List<File>>chunk(1).reader(directoryItemReader()).processor(processorZipFiles()).writer(writerZipFiles()).listener(new ZipItemReadListener()).build();
    }


    @Bean
    public Step stepReadFirstFileXml() {
        return stepBuilderFactory.get(Constants.STEP_READ_SECOND_FILE_XML).<Objects, List<RibxmlDto>>chunk(1).reader(xmlFirstFileItemReader(null)).processor(processorRibFirstFile()).listener(new RibItemReadListener()).listener(new RibItemWriteListener()).build();
    }


    @Bean
    public Step stepReadSecondFileXml() {
        LOGGER.debug("definition du bean stepReadFirstFileXml");
        return stepBuilderFactory.get(Constants.STEP_READ_FIRST_FILE_XML).<Objects, List<RibxmlDto>>chunk(1).reader(xmlSecondFileItemReader(null)).processor(processorRibSecondFile()).writer(writerRib()).listener(new RibItemReadListener()).build();

    }


    @Bean
    public BpmBusinessSoapRequestFactory bpmBusinessSoapRequestFactory() {
        return new BpmBusinessSoapRequestFactoryImpl();
    }


    @Bean
    public DocumentService documentService() {
        return new DocumentService();
    }


    @Bean
    public BpmServiceFactory bpmServiceFactory() {
        BpmServiceFactoryImpl bpmServiceFactory = new BpmServiceFactoryImpl();
        bpmServiceFactory.setBpmRequestFactory(bpmBusinessSoapRequestFactory());
        return bpmServiceFactory;
    }


    @Bean
    public BpmBusinessService bpmBusinessService() {
        if(LOGGER.isDebugEnabled()){
            LOGGER.debug("bpm.businessService.ws.endpoint.url : {}", bpmServerUrl);
            LOGGER.debug("bpm.businessService.ws.username     : {}", bpmServerUsername);
            LOGGER.debug("bpm.businessService.ws.password     : {}", StringUtils.replaceAll(bpmServerPassword, "[a-z]", "*"));
        }
        BpmBusinessService bpmBusinessService = new BpmBusinessService(bpmServerUrl);
        bpmBusinessService.setEndPointUrl(bpmServerUrl);
        bpmBusinessService.setUsername(bpmServerUsername);
        bpmBusinessService.setPassword(bpmServerPassword);
        return bpmBusinessService;
    }


    @Bean
    public BpmService bpmService() {
        BpmService bpmService = new BpmService();
        bpmService.setBpmBusinessService(bpmBusinessService());
        bpmService.setBpmServiceFactory(bpmServiceFactory());
        return bpmService;
    }


    @Bean
    public BatchBpmService batchBpmService() {
        return new BatchBpmService();
    }


    @Bean
    public DemandeSpecifiqueFactory demandeSpecifiqueFactory() {
        return new DemandeSpecifiqueFactoryImpl();
    }


    private Resource[] getResourcesXml(String postFix, String fileName){
        try {
            File dirFilesExtracted = new File(FilenameUtils.removeExtension(exctractDirectory + System.getProperty("file.separator")  + new File(fileName).getName()));
            File currentDirectory = new File(FilenameUtils.removeExtension(currentFile));
            String filesNamePattern = dirFilesExtracted.getAbsolutePath() + System.getProperty("file.separator") + currentDirectory.getName() + System.getProperty("file.separator") + postFix;
            PathMatchingResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver(this.getClass().getClassLoader());
            return resourcePatternResolver.getResources("file:" + filesNamePattern.replace("\\", "/"));
        } catch (IOException e){
            LOGGER_TRAITEMENT.error("Une erreur s'est produite: " + e);
        }
        return null;
    }


    private StaxEventItemReader<Objects> xmlFileItemReader(Resource resource){
        StaxEventItemReader<Objects> xmlFileReader = new StaxEventItemReader<>();
        try {
            FileSystemResource fileSystemResource = new FileSystemResource(resource.getFile());
            xmlFileReader.setResource(fileSystemResource);
            xmlFileReader.setFragmentRootElementName("objects");
            Jaxb2Marshaller objectMarshaller = new Jaxb2Marshaller();
            objectMarshaller.setClassesToBeBound(Objects.class);
            xmlFileReader.setUnmarshaller(objectMarshaller);
        } catch (IOException e){
            LOGGER_TRAITEMENT.error("Une erreur s'est produite: " + e);
        }
        return xmlFileReader;
    }


    private boolean isResourceBPM (Resource resource, String fileName){
        boolean isResourceBPM = false;
        try {
            Resource[] resources = getResourcesXml("*BPM.xml", fileName);
            for (Resource resourceBPM :  resources){
                if(resourceBPM.getFile().getAbsolutePath().equals(resource.getFile().getAbsolutePath())){
                    isResourceBPM = true;
                }
            }
        } catch (IOException e){
            LOGGER_TRAITEMENT.error("Une erreur s'est produite lors de la récupération des fichiers xml: " + e);
        }
        return isResourceBPM;
    }

}
