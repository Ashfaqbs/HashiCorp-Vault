package com.example.demo.vault;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class VaultRestClient_AddFile {
    private static final String PRIMARY_URL = "http://127.0.0.1:8200/v1/secrets/data/SampleApp/config/cacert";
    private static final String FALLBACK_URL = "http://127.0.0.1:8200/v1/secrets/data/SampleApp/config";
    //private static final String VAULT_TOKEN = System.getenv("VAULT_TOKEN");
    private static final String VAULT_TOKEN = "hvs.CAESIDER77nE1mCBPEItTWJAa8BaADGxRHoqSwYdl2Npjec1Gh4KHGh2cy5qdXR4VFJmZkxacTFESHlnSDk3cTVGbzg";

    private static final RestTemplate restTemplate = new RestTemplate();
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final String CACERT_FILE_PATH = "code/demo/src/main/java/com/example/demo/vault/cacert.pem"; // Adjust if needed

    public static void main(String[] args) {
        try {
            // Upload the cacert file
            uploadCaCert(CACERT_FILE_PATH);

            // Print all secrets from the primary path (or fallback if used)
            printAllSecrets(PRIMARY_URL);

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Function to upload the cacert file
    public static void uploadCaCert(String filePath) throws Exception {
        // Read the file content
        String certContent = new String(Files.readAllBytes(Paths.get(filePath)));
        System.out.println("Read cacert content (first 50 chars): " + certContent.substring(0, Math.min(50, certContent.length())));

        // Prepare the payload
        ObjectNode payload = mapper.createObjectNode();
        payload.put("cacert", certContent);

        // Try primary path
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Vault-Token", VAULT_TOKEN);
        headers.set("Content-Type", "application/json");
        HttpEntity<String> entity = new HttpEntity<>(mapper.writeValueAsString(payload), headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                PRIMARY_URL,
                HttpMethod.PUT,
                entity,
                String.class
            );
            System.out.println("Uploaded to " + PRIMARY_URL);
            System.out.println("Response Status: " + response.getStatusCode());
            System.out.println("Response Body: " + response.getBody());
        } catch (Exception e) {
            System.out.println("Failed to upload to " + PRIMARY_URL + ": " + e.getMessage());
            // Fallback to existing path
            Map<String, String> existingSecrets = getAllSecrets(FALLBACK_URL);
            existingSecrets.put("cacert", certContent);
            ObjectNode fallbackPayload = mapper.createObjectNode();
            existingSecrets.forEach(fallbackPayload::put);

            HttpEntity<String> fallbackEntity = new HttpEntity<>(mapper.writeValueAsString(fallbackPayload), headers);
            ResponseEntity<String> fallbackResponse = restTemplate.exchange(
                FALLBACK_URL,
                HttpMethod.PUT,
                fallbackEntity,
                String.class
            );
            System.out.println("Fell back to " + FALLBACK_URL);
            System.out.println("Fallback Response Status: " + fallbackResponse.getStatusCode());
            System.out.println("Fallback Response Body: " + fallbackResponse.getBody());
        }
    }

    // Function to get all secrets from a given URL
    public static Map<String, String> getAllSecrets(String url) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Vault-Token", VAULT_TOKEN);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            entity,
            String.class
        );

        JsonNode root = mapper.readTree(response.getBody());
        JsonNode data = root.path("data");

        Map<String, String> secrets = new HashMap<>();
        data.fields().forEachRemaining(entry -> 
            secrets.put(entry.getKey(), entry.getValue().asText())
        );

        return secrets;
    }

    // Function to print all secrets from a given URL
    public static void printAllSecrets(String url) throws Exception {
        Map<String, String> secrets = getAllSecrets(url);
        System.out.println("\nAll secrets at " + url + ":");
        if (secrets.isEmpty()) {
            System.out.println("No secrets found. Trying fallback...");
            secrets = getAllSecrets(FALLBACK_URL);
            System.out.println("\nAll secrets at " + FALLBACK_URL + ":");
        }
        secrets.forEach((key, val) -> 
            System.out.println(key + ": " + val)
        );
    }
}
/*
 Code op

 Picked up _JAVA_OPTIONS: -Dawt.useSystemAAFontSettings=on -Dswing.aatext=true
Read cacert content (first 50 chars): -----BEGIN CERTIFICATE-----
MIIDdzCCAl+gAwIBAgIJAN
Uploaded to http://127.0.0.1:8200/v1/secrets/data/SampleApp/config/cacert
Response Status: 204 NO_CONTENT
Response Body: null

All secrets at http://127.0.0.1:8200/v1/secrets/data/SampleApp/config/cacert:
cacert: -----BEGIN CERTIFICATE-----
MIIDdzCCAl+gAwIBAgIJANUE5aD9sA6QMA0GCSqGSIb3DQEBCwUAMFIxCzAJBgNV
... (updated content) ...
-----END CERTIFICATE-----

 */


 /*
  it was able to create a path cacert and add the content of the file there 


  ┌──(ashfaq㉿kali-vm)-[~/Desktop/software/Vault/roles]
└─$ vault kv list  secrets/data/SampleApp/config
Keys
----
cacert
 
                                                                                                                                                                                                                    
┌──(ashfaq㉿kali-vm)-[~/Desktop/software/Vault/roles]
└─$ vault kv get secrets/data/SampleApp/config/cacert
===== Data =====
Key       Value
---       -----
cacert    -----BEGIN CERTIFICATE-----
MIIDdzCCAl+gAwIBAgIJANUE5aD9sA6QMA0GCSqGSIb3DQEBCwUAMFIxCzAJBgNV
... (updated content) ...
-----END CERTIFICATE-----
                                                                                                                                                                                                                    
┌──(ashfaq㉿kali-vm)-[~/Desktop/software/Vault/roles]
└─$ 

  */