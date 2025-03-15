Let's try a different encryption approach using Vault's **Transit Secrets Engine**. The transit engine is designed specifically for encryption and decryption operations without storing the data permanently—it acts as an encryption-as-a-service.

Below is a step-by-step guide:

---

### **1. Create a New Text File**

First, create a new text file with some content. For example, navigate to our working directory and create a file named `newfile.txt`:

```bash
echo "This is a test for transit encryption" > newfile.txt
```

---

### **2. Enable the Transit Secrets Engine**

If we haven’t already enabled the transit engine, do so by running:

```bash
vault secrets enable transit
```

This command mounts the transit engine at the default path `transit/`.

---

### **3. Create an Encryption Key**

Before we can encrypt data, we need to create a key. Create a key named `mykey`:

```bash
vault write -f transit/keys/mykey
```

This key will be used to encrypt and decrypt data.

---

### **4. Encrypt the File's Content**

Since the transit engine expects a base64-encoded plaintext, we'll need to encode our file's content first. we can then pass that encoded string to the encrypt command.

For example, to encrypt the content of `newfile.txt`:

```bash
vault write transit/encrypt/mykey plaintext=$(base64 -w 0 newfile.txt)
```

- **Explanation:**  
  - `base64 -w 0 newfile.txt` encodes the content of `newfile.txt` in one continuous line.
  - The output of the base64 command is passed as the value for `plaintext` in the Vault command.
  
The command returns a ciphertext, which is the encrypted form of our file content.

---

### **5. Decrypt the Ciphertext**

To verify that the encryption worked correctly, we can decrypt the ciphertext. Suppose the encryption command returned something like:

```json
{
  "data": {
    "ciphertext": "vault:v1:..."
  }
}
```

Copy the ciphertext value and run:

```bash
vault write transit/decrypt/mykey ciphertext="vault:v1:..."
```

The decrypted output will be a base64-encoded string. To convert it back to the original text, we can pipe it through a base64 decode:

```bash
vault write transit/decrypt/mykey ciphertext="vault:v1:..." -format=json | jq -r '.data.plaintext' | base64 --decode
```

> **Note:** This example assumes we have `jq` installed for JSON parsing.

---

### **Summary of the Process**

1. **Create a New File:**
   ```bash
   echo "This is a test for transit encryption" > newfile.txt
   ```
2. **Enable Transit Engine:**
   ```bash
   vault secrets enable transit
   ```
3. **Create an Encryption Key:**
   ```bash
   vault write -f transit/keys/mykey
   ```
4. **Encrypt the File Content:**
   ```bash
   vault write transit/encrypt/mykey plaintext=$(base64 -w 0 newfile.txt)
   ```
5. **Decrypt to Verify (using `jq` for JSON parsing):**
   ```bash
   vault write transit/decrypt/mykey ciphertext="vault:v1:..." -format=json | jq -r '.data.plaintext' | base64 --decode
   ```

This process shows we how to use a different encryption method (transit engine) to encrypt and decrypt file content. Let me know if we'd like to go over any of these steps in more detail or if we have further questions!











┌──(ashfaq㉿kali-vm)-[~/Desktop/software/Vault/files]
└─$ echo "This is a test for transit encryption" > newfile.txt

                                                                                                                                                                                                                                            
┌──(ashfaq㉿kali-vm)-[~/Desktop/software/Vault/files]
└─$ ls
Files.md  cacert.pem  hello.txt  newfile.txt  retrieved.txt  retrieved_cacert.pem
                                                                                                                                                                                                                                            
┌──(ashfaq㉿kali-vm)-[~/Desktop/software/Vault/files]
└─$ vault secrets enable transit

Success! Enabled the transit secrets engine at: transit/
                                                                                                                                                                                                                                            
┌──(ashfaq㉿kali-vm)-[~/Desktop/software/Vault/files]
└─$ vault write -f transit/keys/mykey

Key                       Value
---                       -----
allow_plaintext_backup    false
auto_rotate_period        0s
deletion_allowed          false
derived                   false
exportable                false
imported_key              false
keys                      map[1:1741961294]
latest_version            1
min_available_version     0
min_decryption_version    1
min_encryption_version    0
name                      mykey
supports_decryption       true
supports_derivation       true
supports_encryption       true
supports_signing          false
type                      aes256-gcm96
                                                                                                                                                                                                                                            
┌──(ashfaq㉿kali-vm)-[~/Desktop/software/Vault/files]
└─$ vault write transit/encrypt/mykey plaintext=$(base64 -w 0 newfile.txt)

Key            Value
---            -----
ciphertext     vault:v1:cFHUzPNEW8celR2YYSxYQbkI8W19aJhExFqth9U9kumT74oFxKwOh9VKRP3A3DZXnCEue93n/ITlKxR7sxXetmfu
key_version    1
                                                                                                                                                                                                                                                                                                                     
┌──(ashfaq㉿kali-vm)-[~/Desktop/software/Vault/files]
└─$ vault write transit/decrypt/mykey ciphertext="vault:v1:cFHUzPNEW8celR2YYSxYQbkI8W19aJhExFqth9U9kumT74oFxKwOh9VKRP3A3DZXnCEue93n/ITlKxR7sxXetmfu"

Key          Value
---          -----
plaintext    VGhpcyBpcyBhIHRlc3QgZm9yIHRyYW5zaXQgZW5jcnlwdGlvbgo=
                                                                                                                                                                                                                                            
┌──(ashfaq㉿kali-vm)-[~/Desktop/software/Vault/files]
└─$ 

