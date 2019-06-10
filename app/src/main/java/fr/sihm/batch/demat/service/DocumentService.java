package fr.sihm.batch.demat.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.sihm.batch.demat.model.DocumentResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import org.apache.commons.io.IOUtils;
import org.glassfish.jersey.internal.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * Created by blaghji-a on 29/01/2019.
 */
public class DocumentService {

    private static Logger LOGGER = LoggerFactory.getLogger(DocumentService.class);

    public static final String BATCH_RIB = "batch-demat";
    private static String SHA512 = "SHA-512";
    private static String SECRET = "ApiDemandeSecret";

    @Value("${security.ldapConfig.connectionName}")
    public String username;

    @Value("${security.ldapConfig.connectionPassword}")
    public String password;

    @Value("${demande.service.api.url.endpoint.base}")
    private String demandeServiceApiUrl;

    private RestTemplate restTemplate;

    /**
     * Injection d'un document vers infoArchive via api-demande
     * @return HttpHeaders
     */
    public DocumentResponse injectDocument(String pathFileName, String mimeType, InputStream file) throws IOException{
        DocumentResponse documentResponse = new DocumentResponse();
        HttpHeaders httpHeaders = createHeaders(new File(pathFileName).getName(), mimeType);
        HttpEntity<DocumentResponse> httpEntity = new HttpEntity(IOUtils.toByteArray(file), httpHeaders);
        restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = this.restTemplate.exchange(demandeServiceApiUrl + "/v1/creationPJDemande/" ,
                HttpMethod.POST, httpEntity, String.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        if (responseEntity.getBody() != null) {
            documentResponse = objectMapper.readValue(responseEntity.getBody(), new TypeReference<DocumentResponse>() {
            });
        }
        return documentResponse;
    }


    /**
     * Creation des header
     * @return HttpHeaders
     */
    public HttpHeaders createHeaders(String fileName, String mimeType) {
        HttpHeaders headers = new HttpHeaders();
        String plainCreds = username + ":" + password;
        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.encode(plainCredsBytes);
        String base64Creds = new String(base64CredsBytes);
        headers.add("Authorization", "Basic " + base64Creds);
        headers.add("Application", BATCH_RIB);
        headers.add("Content-Disposition", fileName);
        headers.add("Content-Type", mimeType);
        return headers;
    }

    /**
     * Fonction de décryptage de l'idTechnique
     * @return String
     */
    public String getDataFromAutoSignedId(String autoSignedId, String application) throws NoSuchAlgorithmException{
        String data = null;
        byte[] decodeAutoSignedId = java.util.Base64.getUrlDecoder().decode(autoSignedId);
        MessageDigest md = MessageDigest.getInstance(SHA512);
        int digestLength = md.getDigestLength();
        if (decodeAutoSignedId.length < digestLength) {
            LOGGER.error("DecodeAutoSignedId has not the good size");
        } else {
            data = new String(decodeAutoSignedId, digestLength, decodeAutoSignedId.length - digestLength);
            String dataToDigest = data + application + SECRET;
            byte[] digest = md.digest(dataToDigest.getBytes());
            byte[] digestSource = new byte[digest.length];
            System.arraycopy(decodeAutoSignedId, 0, digestSource, 0, digest.length);
            if (Arrays.equals(digest, digestSource)) {
                return data;
            } else {
                LOGGER.error("The data doesn't match the code application");
            }
        }
        return data;
    }
}
