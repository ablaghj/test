package fr.sihm.batch.demat.config;

/**
 * Application constants.
 */
public final class Constants {
    public static final String STEP_UNZIP_FILE = "stepUnzipFile";
    public static final String STEP_UNZIP_FILE_PARENT = "stepUnzipFileParent";
    public static final String STEP_READ_FIRST_FILE_XML = "stepReadFirstFileXml";
    public static final String STEP_READ_SECOND_FILE_XML = "stepReadSecondFileXml";
    private Constants() {
        throw new IllegalStateException("Utility class designed to store constants only");
    }

}
