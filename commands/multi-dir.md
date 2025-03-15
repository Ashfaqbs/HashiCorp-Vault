Let's set up a hierarchical structure under the base path `secrets/` so that we  have a clear separation for each environment (SIT, DEV, PROD). In Vault's KV engine, we  don't have to manually create intermediate folders—simply writing to a new path will automatically create that hierarchy.

Below are the steps and commands for each environment:

---

### 1. **Store Secrets for SIT**

Run the following command to store three secrets (username, password, and API) for the SIT environment:

```bash
vault kv put secrets/env/SIT username="situser" password="sitpass" API="sitAPI"
```

- **Explanation:**  
  This command stores the key-value pairs under the path `secrets/env/SIT`. Even though we  haven't created `secrets/env/SIT` explicitly, Vault will automatically handle it.

---

### 2. **Store Secrets for DEV**

Similarly, store the secrets for the DEV environment:

```bash
vault kv put secrets/env/DEV username="devuser" password="devpass" API="devAPI"
```

- **Explanation:**  
  This command writes the keys `username`, `password`, and `API` to the path `secrets/env/DEV`.

---

### 3. **Store Secrets for PROD**

Finally, store the secrets for the PROD environment:

```bash
vault kv put secrets/env/PROD username="produser" password="prodpass" API="prodAPI"
```

- **Explanation:**  
  This command creates or updates the keys at the path `secrets/env/PROD` with the provided values.

---

### 4. **Verify the Stored Secrets**

we  can verify each environment's secrets using the `vault kv get` command. For example, to check the SIT environment:

```bash
vault kv get secrets/env/SIT
```

The output should display the stored keys and their corresponding values.

---

### Summary

- **No Manual Path Creation:**  
  Writing to `secrets/env/SIT`, `secrets/env/DEV`, or `secrets/env/PROD` automatically creates these hierarchical paths.
  
- **CRUD Operations:**  
  - **Create/Update:** Use `vault kv put` with the desired path and key-value pairs.
  - **Read:** Use `vault kv get` to retrieve secrets.
  - **Delete:** Later, if needed, we  can delete a secret with `vault kv delete secrets/env/SIT` (or DEV/PROD).

This step-by-step approach sets up we r environments and stores the required secrets. Let me know if we 'd like to proceed with further operations or if we  have any questions!


                                                                                                                                                                                                                                            
┌──(ashfaq㉿kali-vm)-[~/Desktop/software/Vault/files]
└─$ vault kv put secrets/env/SIT username="situser" password="sitpass" API="sitAPI"

Success! Data written to: secrets/env/SIT
                                                                                                                                                                                                                                            
┌──(ashfaq㉿kali-vm)-[~/Desktop/software/Vault/files]
└─$ vault kv get secrets/env/SIT                                                   
====== Data ======
Key         Value
---         -----
API         sitAPI
password    sitpass
username    situser
                                                                                                                                                                                                                                            
┌──(ashfaq㉿kali-vm)-[~/Desktop/software/Vault/files]
└─$ vault kv list secrets/env/SIT
No value found at secrets/env/SIT
                                                                                                                                                                                                                                            
┌──(ashfaq㉿kali-vm)-[~/Desktop/software/Vault/files]
└─$ vault kv list secrets/env    
Keys
----
SIT
                                                                                                                                                                                                                                            
┌──(ashfaq㉿kali-vm)-[~/Desktop/software/Vault/files]
└─$ vault kv put secrets/env/DEV username="devuser" password="devpass" API="devAPI"

Success! Data written to: secrets/env/DEV
                                                                                                                                                                                                                                            
┌──(ashfaq㉿kali-vm)-[~/Desktop/software/Vault/files]
└─$ vault kv put secrets/env/PROD username="produser" password="prodpass" API="prodAPI"

Success! Data written to: secrets/env/PROD
                                                                                                                                                                                                                                            
┌──(ashfaq㉿kali-vm)-[~/Desktop/software/Vault/files]
└─$ vault kv list secrets/env                                                          
Keys
----
DEV
PROD
SIT
                                                                                 

