package com.example.demo.vault;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.HashMap;
import java.util.Map;

public class VaultRestClient_Add_Read {
    private static final String VAULT_URL = "http://127.0.0.1:8200/v1/secrets/data/SampleApp/config";
    //private static final String VAULT_TOKEN = System.getenv("VAULT_TOKEN");
    private static final String VAULT_TOKEN = "hvs.CAESIDER77nE1mCBPEItTWJAa8BaADGxRHoqSwYdl2Npjec1Gh4KHGh2cy5qdXR4VFJmZkxacTFESHlnSDk3cTVGbzg";
    private static final RestTemplate restTemplate = new RestTemplate();
    private static final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) {
        try {
            // Add a new secret
            addSecret("newKey", "newValue");

            // Print all secrets
            printAllSecrets();

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Function to add a new secret without removing existing ones
    public static void addSecret(String key, String value) throws Exception {
        // Step 1: Read existing secrets
        Map<String, String> existingSecrets = getAllSecrets();

        // Step 2: Add new secret to the map
        existingSecrets.put(key, value);

        // Step 3: Create JSON payload with all secrets
        ObjectNode payload = mapper.createObjectNode();
        existingSecrets.forEach(payload::put);

        // Step 4: Send PUT request to update secrets
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Vault-Token", VAULT_TOKEN);
        headers.set("Content-Type", "application/json");
        HttpEntity<String> entity = new HttpEntity<>(mapper.writeValueAsString(payload), headers);

        ResponseEntity<String> response = restTemplate.exchange(
            VAULT_URL,
            HttpMethod.PUT,
            entity,
            String.class
        );

        System.out.println("Add Secret Response Status: " + response.getStatusCode());
        System.out.println("Add Secret Response Body: " + response.getBody());
    }

    // Function to get all secrets as a Map
    public static Map<String, String> getAllSecrets() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Vault-Token", VAULT_TOKEN);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
            VAULT_URL,
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

    // Function to print all secrets
    public static void printAllSecrets() throws Exception {
        Map<String, String> secrets = getAllSecrets();
        System.out.println("\nAll secrets:");
        secrets.forEach((key, val) -> 
            System.out.println(key + ": " + val)
        );
    }
}


/*

Code output
 ┌──(ashfaq㉿kali-vm)-[~/Desktop/software/Vault]
└─$  cd /home/ashfaq/Desktop/software/Vault ; /usr/bin/env /usr/lib/jvm/java-17-openjdk-amd64/bin/java @/tmp/cp_2ym4n5ovr4mw8thuzlmuu78zb.argfile com.example.demo.vault.VaultRes
tClient 
Picked up _JAVA_OPTIONS: -Dawt.useSystemAAFontSettings=on -Dswing.aatext=true
Add Secret Response Status: 204 NO_CONTENT
Add Secret Response Body: null

All secrets:
password: samplepass
newKey: newValue
API: sampleAPI
username: sampleuser
                                                                                                                                                                                 
┌──(ashfaq㉿kali-vm)-[~/Desktop/software/Vault]
└─$ 
 */