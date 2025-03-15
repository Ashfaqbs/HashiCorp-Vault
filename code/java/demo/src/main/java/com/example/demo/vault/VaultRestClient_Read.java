package com.example.demo.vault;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class VaultRestClient_Read {
    private static final String VAULT_URL = "http://127.0.0.1:8200/v1/secrets/data/SampleApp/config";
    //private static final String VAULT_TOKEN = System.getenv("VAULT_TOKEN");
    private static final String VAULT_TOKEN = "hvs.CAESIDER77nE1mCBPEItTWJAa8BaADGxRHoqSwYdl2Npjec1Gh4KHGh2cy5qdXR4VFJmZkxacTFESHlnSDk3cTVGbzg";

    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Vault-Token", VAULT_TOKEN);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            System.out.println("Using Vault Token: " + VAULT_TOKEN);
            System.out.println("Querying URL: " + VAULT_URL);

            ResponseEntity<String> response = restTemplate.exchange(
                VAULT_URL,
                HttpMethod.GET,
                entity,
                String.class
            );

            System.out.println("Response Status: " + response.getStatusCode());
            System.out.println("Raw Response: " + response.getBody());

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());
            JsonNode data = root.path("data"); // Only one "data" level

            System.out.println("Parsed data node: " + data.toString());

            // 1. Get only username
            String username = data.path("username").asText();
            System.out.println("Username only: " + username);

            // 2. Get all secrets
            System.out.println("\nAll secrets:");
            data.fields().forEachRemaining(entry -> {
                System.out.println(entry.getKey() + ": " + entry.getValue().asText());
            });

        } catch (Exception e) {
            System.err.println("Error connecting to Vault: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

/*
 Code OP
 ──(ashfaq㉿kali-vm)-[~/Desktop/software/Vault]
└─$  cd /home/ashfaq/Desktop/software/Vault ; /usr/bin/env /usr/lib/jvm/java-17-openjdk-amd64/bin/java @/tmp/cp_2ym4n5ovr4mw8thuzlmuu78zb.argfile com.example.demo.vault.VaultRes
tClient 
Picked up _JAVA_OPTIONS: -Dawt.useSystemAAFontSettings=on -Dswing.aatext=true
Raw response: {"request_id":"44d67afa-fb29-b0f8-b3b2-dd5c4b7f375b","lease_id":"","renewable":false,"lease_duration":2764800,"data":{"API":"sampleAPI","password":"samplepass","username":"sampleuser"},"wrap_info":null,"warnings":null,"auth":null,"mount_type":"kv"}

Username only: 

All secrets:
                                                                                                                                                                                 
┌──(ashfaq㉿kali-vm)-[~/Desktop/software/Vault]
└─$  cd /home/ashfaq/Desktop/software/Vault ; /usr/bin/env /usr/lib/jvm/java-17-openjdk-amd64/bin/java @/tmp/cp_2ym4n5ovr4mw8thuzlmuu78zb.argfile com.example.demo.vault.VaultRes
tClient 
Picked up _JAVA_OPTIONS: -Dawt.useSystemAAFontSettings=on -Dswing.aatext=true
Using Vault Token: hvs.CAESIDER77nE1mCBPEItTWJAa8BaADGxRHoqSwYdl2Npjec1Gh4KHGh2cy5qdXR4VFJmZkxacTFESHlnSDk3cTVGbzg
Querying URL: http://127.0.0.1:8200/v1/secrets/data/SampleApp/config
Response Status: 200 OK
Raw Response: {"request_id":"8131de0c-431a-e93e-15e8-a0f233ff77ee","lease_id":"","renewable":false,"lease_duration":2764800,"data":{"API":"sampleAPI","password":"samplepass","username":"sampleuser"},"wrap_info":null,"warnings":null,"auth":null,"mount_type":"kv"}

Parsed data node: {"API":"sampleAPI","password":"samplepass","username":"sampleuser"}
Username only: sampleuser

All secrets:
API: sampleAPI
password: samplepass
username: sampleuser


Note :
Vault setup might be using KV v1
and not the typical KV v2 structure

 */