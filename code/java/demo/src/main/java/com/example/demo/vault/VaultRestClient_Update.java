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

public class VaultRestClient_Update {
    private static final String VAULT_URL = "http://127.0.0.1:8200/v1/secrets/data/SampleApp/config";
   // private static final String VAULT_TOKEN = System.getenv("VAULT_TOKEN");
   private static final String VAULT_TOKEN = "hvs.CAESIDER77nE1mCBPEItTWJAa8BaADGxRHoqSwYdl2Npjec1Gh4KHGh2cy5qdXR4VFJmZkxacTFESHlnSDk3cTVGbzg";
 
   private static final RestTemplate restTemplate = new RestTemplate();
    private static final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) {
        try {
            // Update newKey's value
            updateSecret("newKey", "updatedValue");

            // Print all secrets to verify
            printAllSecrets();

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Function to update a secret by key
    public static void updateSecret(String key, String newValue) throws Exception {
        // Step 1: Read existing secrets
        Map<String, String> secrets = getAllSecrets();

        // Step 2: Check if key exists
        if (!secrets.containsKey(key)) {
            System.out.println("Key '" + key + "' not found. Nothing to update.");
            return;
        }

        // Step 3: Update the value
        secrets.put(key, newValue);
        System.out.println("Updated '" + key + "' to '" + newValue + "'.");

        // Step 4: Write updated secrets back to Vault
        ObjectNode payload = mapper.createObjectNode();
        secrets.forEach(payload::put);

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

        System.out.println("Update Response Status: " + response.getStatusCode());
        System.out.println("Update Response Body: " + response.getBody());
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
        if (secrets.isEmpty()) {
            System.out.println("No secrets found.");
        } else {
            secrets.forEach((key, val) -> 
                System.out.println(key + ": " + val)
            );
        }
    }
}
/*
Code op
 ┌──(ashfaq㉿kali-vm)-[~/Desktop/software/Vault]
└─$  cd /home/ashfaq/Desktop/software/Vault ; /usr/bin/env /usr/lib/jvm/java-17-openjdk-amd64/bin/java @/tmp/cp_2ym4n5ovr4mw8thuzlmuu78zb.argfile com.example.demo.vault.VaultRes
tClient 
Picked up _JAVA_OPTIONS: -Dawt.useSystemAAFontSettings=on -Dswing.aatext=true
Updated 'newKey' to 'updatedValue'.
Update Response Status: 204 NO_CONTENT
Update Response Body: null

All secrets:
password: samplepass
newKey: updatedValue
API: sampleAPI
username: sampleuser
 */