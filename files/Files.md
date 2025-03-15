1 . Txt File 

Let's walk through the entire process step by step. In this example, we'll work with the base path `secrets/` (as we agreed), and store a simple text file in the path `secrets/files/txt`.

---

### **Step 1: Create a File with "Hello World" Content**

Navigate to our working directory (current path `~/Desktop/software/Vault/files`), and create a text file:

```bash
echo "Hello World" > hello.txt
```

This command creates a file named `hello.txt` with the content "Hello World".

---

### **Step 2: Add the File Content to Vault**

we can store the file's content as a secret by reading it and using the `@` syntax with the Vault CLI. Here, we'll store it under the key `content` in the path `secrets/files/txt`:

```bash
vault kv put secrets/files/txt content=@hello.txt
```

- **What this does:**  
  Vault reads the content of `hello.txt` and stores it under the key `content` at the path `secrets/files/txt`.  
  > *Note: we don't need to create the `files/txt` hierarchy manually—Vault automatically handles it when we put data in that path.*

---

### **Step 3: Read the Secret from Vault**

To verify that the secret has been stored, retrieve it with:

```bash
vault kv get secrets/files/txt
```

- **Output:**  
  This will show we the key-value pairs stored at that path (e.g., `content` with the value "Hello World").

If we want to extract just the file content to a local file, we can do:

```bash
vault kv get -field=content secrets/files/txt > retrieved.txt
```

This writes the retrieved content into a file named `retrieved.txt`.

---

### **Step 4: Update the Secret with New File Content**

Now, let's change the file's content. For example, update it to "Hello Vault". First, modify the file:

```bash
echo "Hello Vault" > hello.txt
```

Then update the secret in Vault:

```bash
vault kv put secrets/files/txt content=@hello.txt
```

- **What this does:**  
  It replaces the previous value with the new content from `hello.txt`.

we can verify the update by retrieving the secret again:

```bash
vault kv get secrets/files/txt
```

Or extract just the updated content:

```bash
vault kv get -field=content secrets/files/txt
```

---

### **Step 5: Delete the Secret**

When we're ready to delete the secret from Vault, run:

```bash
vault kv delete secrets/files/txt
```

- **What this does:**  
  It removes the secret data stored at `secrets/files/txt`. The logical path still exists, but the secret data is cleared.

---

### **Summary**

1. **Create File:**  
   - `echo "Hello World" > hello.txt`
2. **Store File in Vault:**  
   - `vault kv put secrets/files/txt content=@hello.txt`
3. **Read the Secret:**  
   - `vault kv get secrets/files/txt`  
   - (Optionally extract with `-field=content`)
4. **Update the Secret:**  
   - Update file: `echo "Hello Vault" > hello.txt`  
   - Update in Vault: `vault kv put secrets/files/txt content=@hello.txt`
5. **Delete the Secret:**  
   - `vault kv delete secrets/files/txt`

These steps demonstrate creating a file, adding it as a secret, reading it, updating it, and finally deleting it—all under the `secrets/` base path.



```
┌──(ashfaq㉿kali-vm)-[~/Desktop/software/Vault/files]
└─$ echo "Hello World" > hello.txt

                                                                                                                                                                                                                                            
┌──(ashfaq㉿kali-vm)-[~/Desktop/software/Vault/files]
└─$ ls
hello.txt
                                                                                                                                                                                                                                            
┌──(ashfaq㉿kali-vm)-[~/Desktop/software/Vault/files]
└─$ cat hello.txt 
Hello World
                                                                                                                                                                                                                                            
┌──(ashfaq㉿kali-vm)-[~/Desktop/software/Vault/files]
└─$ vault kv put secrets/files/txt content=@hello.txt

Success! Data written to: secrets/files/txt
                                                                                                                                                                                                                                            
┌──(ashfaq㉿kali-vm)-[~/Desktop/software/Vault/files]
└─$ vault kv get secrets/files/txt

===== Data =====
Key        Value
---        -----
content    Hello World
                                                                                                                                                                                                                                            
┌──(ashfaq㉿kali-vm)-[~/Desktop/software/Vault/files]
└─$ vault kv get -field=content secrets/files/txt > retrieved.txt

                                                                                                                                                                                                                                            
┌──(ashfaq㉿kali-vm)-[~/Desktop/software/Vault/files]
└─$ ls
hello.txt  retrieved.txt
                                                                                                                                                                                                                                            
┌──(ashfaq㉿kali-vm)-[~/Desktop/software/Vault/files]
└─$ cat retrieved.txt 
Hello World
                                                                                                                                                                                                                                            
┌──(ashfaq㉿kali-vm)-[~/Desktop/software/Vault/files]
└─$ echo "Hello Vault" > hello.txt

                                                                                                                                                                                                                                            
┌──(ashfaq㉿kali-vm)-[~/Desktop/software/Vault/files]
└─$ vault kv put secrets/files/txt content=@hello.txt            

Success! Data written to: secrets/files/txt
                                                                                                                                                                                                                                            
┌──(ashfaq㉿kali-vm)-[~/Desktop/software/Vault/files]
└─$ vault kv get secrets/files/txt                               

===== Data =====
Key        Value
---        -----
content    Hello Vault
                                                                                                                                                                                                                                            
┌──(ashfaq㉿kali-vm)-[~/Desktop/software/Vault/files]
└─$ vault kv delete secrets/files/txt

Success! Data deleted (if it existed) at: secrets/files/txt
                                                                                                                                                                                                                                            
┌──(ashfaq㉿kali-vm)-[~/Desktop/software/Vault/files]
└─$ vault kv get secrets/files/txt   

No value found at secrets/files/txt
                                                                                                                                                                                                                                            
┌──(ashfaq㉿kali-vm)-[~/Desktop/software/Vault/files]
└─$ 


```


2. Cacert




Let's follow a similar process for a CA certificate file. For this demonstration, we'll create a dummy certificate file, store it in Vault, retrieve it, update it, and finally delete it. Here’s how to do it step by step:

---

### **Step 1: Create a Dummy CA Certificate File**

In our working directory (e.g., `~/Desktop/software/Vault/files`), create a file called `cacert.pem` with some sample certificate content. For example:

```bash
cat <<EOF > cacert.pem
-----BEGIN CERTIFICATE-----
MIIDdzCCAl+gAwIBAgIJANUE5aD9sA6QMA0GCSqGSIb3DQEBCwUAMFIxCzAJBgNV
BAYTAkFVMRMwEQYDVQQIDApxdWVlbnNsYW5kMREwDwYDVQQHDAhSZW1vdmUxEjAQ
BgNVBAoMCVRlc3QgQ28wHhcNMjAwMTAxMDAwMDAwWhcNMzAwMTAxMDAwMDAwWjBS
MQswCQYDVQQGEwJBVTETMBEGA1UECAwKcXVlZW5zbGFuZDERMA8GA1UEBwwIU2F2
ZXR0YTEUMBIGA1UECgwLVGVzdCBDbzCCASIwDQYJKoZIhvcNAQEBBQADggEPADCC
AQoCggEBAK1uM7VhlUO4tqB+7YFQuJVcYqKlyEZB5mXFd2rjxJY2e29L1UqA/HU6
eZpP0q7DyKll0Z0X2uZ8D6I+6dZAGN4FQfYvHcKOLOy0H2Mv6jz/7XG6qE9Blg3L
D1A4zJyxDq1H7pjdv8A0v3y+XKkAwP1zV7iz9VjN71FswE/uj3n5aFdpjRd+g/oE
sxj3pZ+v/o1oxTSdR3nQJbZ+KeF9UdjkC0DP7ZHPz3zqm6c7+1YOP3FdJwPvMEmX
Lz8k6gZ0E5qFQ+FQ+zJLbwxe4nURw2D5TzLS4s+2V+0S1Qb4/m7D2x8Q0yJXwd/Q
6J5I+rwCmjAplZyPQbAgdvBMbrunQI8CAwEAAaNQME4wHQYDVR0OBBYEFAaNzjP2
aQWap6XSeO3/d0EtJXGUMB8GA1UdIwQYMBaAFAaNzjP2aQWap6XSeO3/d0EtJXGU
MAwGA1UdEwQFMAMBAf8wDQYJKoZIhvcNAQELBQADggEBAFqxP90qm4r0dIXW5JEE
q7G9c3l3dZuEk+g9GJj/fYHBqvBg/CvK4L9Yq5b4Dk2/eC+n9Vqj3N1Vot2gSXgw
ZgHs3H8F+36+PltIhy4Ne+klA+gXq0z/Xru39J7Dzl2YcRav8jeEUMtM9/bpnv9S
2Jh+z6sCzC6P6dPxJ6c5pZ2JXSkQ2DzLwWX5Rx1Q3Oz9k5qP1xF7i8tK2pRfKzvE
I7o1lzy4WTGpV4p+ZK5xBw0cJrM3oOMM2iUJQIyIlH+kFJyWvfxK0r9G4hv/3pT0
-----END CERTIFICATE-----
EOF
```

This command creates a file called `cacert.pem` containing sample certificate content.

---

### **Step 2: Store the CA Certificate in Vault**

Now, store the content of this certificate file in Vault under the path `secrets/files/cacert`. Use the `@` syntax to load file content:

```bash
vault kv put secrets/files/cacert content=@cacert.pem
```

- **What happens:**  
  Vault reads the content of `cacert.pem` and stores it as the value for the key `content` at `secrets/files/cacert`.

---

### **Step 3: Retrieve the CA Certificate from Vault**

To verify that the certificate was stored correctly, retrieve it with:

```bash
vault kv get secrets/files/cacert
```

This command will display the stored key-value pair, including the certificate content.

If we want to extract just the certificate content to a file:

```bash
vault kv get -field=content secrets/files/cacert > retrieved_cacert.pem
```

This writes the certificate content from Vault into `retrieved_cacert.pem`.

---

### **Step 4: Update the CA Certificate**

Let’s simulate an update. Modify `cacert.pem` to have new content. For example, we can replace part of the certificate or simply change a text line:

```bash
echo "-----BEGIN CERTIFICATE-----
MIIDdzCCAl+gAwIBAgIJANUE5aD9sA6QMA0GCSqGSIb3DQEBCwUAMFIxCzAJBgNV
... (updated content) ...
-----END CERTIFICATE-----" > cacert.pem
```

Now update the secret in Vault:

```bash
vault kv put secrets/files/cacert content=@cacert.pem
```

we can verify the update by retrieving the secret again:

```bash
vault kv get -field=content secrets/files/cacert
```

---

### **Step 5: Delete the CA Certificate Secret**

When we’re ready to delete the stored certificate, run:

```bash
vault kv delete secrets/files/cacert
```

This command removes the secret stored at that path.

---

### **Summary**

1. **Create a Dummy CA Certificate File:**  
   - Create `cacert.pem` with sample certificate content.
2. **Store the Certificate in Vault:**  
   - `vault kv put secrets/files/cacert content=@cacert.pem`
3. **Retrieve the Certificate:**  
   - `vault kv get secrets/files/cacert`  
   - Or extract with `vault kv get -field=content secrets/files/cacert > retrieved_cacert.pem`
4. **Update the Certificate:**  
   - Modify `cacert.pem` and update via `vault kv put secrets/files/cacert content=@cacert.pem`
5. **Delete the Certificate:**  
   - `vault kv delete secrets/files/cacert`

These steps let we test storing, updating, reading, and deleting a CA certificate in Vault.





┌──(ashfaq㉿kali-vm)-[~/Desktop/software/Vault/files]
└─$ cat <<EOF > cacert.pem
-----BEGIN CERTIFICATE-----
MIIDdzCCAl+gAwIBAgIJANUE5aD9sA6QMA0GCSqGSIb3DQEBCwUAMFIxCzAJBgNV
BAYTAkFVMRMwEQYDVQQIDApxdWVlbnNsYW5kMREwDwYDVQQHDAhSZW1vdmUxEjAQ
BgNVBAoMCVRlc3QgQ28wHhcNMjAwMTAxMDAwMDAwWhcNMzAwMTAxMDAwMDAwWjBS
MQswCQYDVQQGEwJBVTETMBEGA1UECAwKcXVlZW5zbGFuZDERMA8GA1UEBwwIU2F2
ZXR0YTEUMBIGA1UECgwLVGVzdCBDbzCCASIwDQYJKoZIhvcNAQEBBQADggEPADCC
AQoCggEBAK1uM7VhlUO4tqB+7YFQuJVcYqKlyEZB5mXFd2rjxJY2e29L1UqA/HU6
eZpP0q7DyKll0Z0X2uZ8D6I+6dZAGN4FQfYvHcKOLOy0H2Mv6jz/7XG6qE9Blg3L
D1A4zJyxDq1H7pjdv8A0v3y+XKkAwP1zV7iz9VjN71FswE/uj3n5aFdpjRd+g/oE
sxj3pZ+v/o1oxTSdR3nQJbZ+KeF9UdjkC0DP7ZHPz3zqm6c7+1YOP3FdJwPvMEmX
Lz8k6gZ0E5qFQ+FQ+zJLbwxe4nURw2D5TzLS4s+2V+0S1Qb4/m7D2x8Q0yJXwd/Q
6J5I+rwCmjAplZyPQbAgdvBMbrunQI8CAwEAAaNQME4wHQYDVR0OBBYEFAaNzjP2
aQWap6XSeO3/d0EtJXGUMB8GA1UdIwQYMBaAFAaNzjP2aQWap6XSeO3/d0EtJXGU
MAwGA1UdEwQFMAMBAf8wDQYJKoZIhvcNAQELBQADggEBAFqxP90qm4r0dIXW5JEE
q7G9c3l3dZuEk+g9GJj/fYHBqvBg/CvK4L9Yq5b4Dk2/eC+n9Vqj3N1Vot2gSXgw
ZgHs3H8F+36+PltIhy4Ne+klA+gXq0z/Xru39J7Dzl2YcRav8jeEUMtM9/bpnv9S
2Jh+z6sCzC6P6dPxJ6c5pZ2JXSkQ2DzLwWX5Rx1Q3Oz9k5qP1xF7i8tK2pRfKzvE
I7o1lzy4WTGpV4p+ZK5xBw0cJrM3oOMM2iUJQIyIlH+kFJyWvfxK0r9G4hv/3pT0
-----END CERTIFICATE-----
EOF

                                                                                                                                                                                                                                            
┌──(ashfaq㉿kali-vm)-[~/Desktop/software/Vault/files]
└─$ ls
cacert.pem  hello.txt  retrieved.txt
                                                                                                                                                                                                                                            
┌──(ashfaq㉿kali-vm)-[~/Desktop/software/Vault/files]
└─$ cat cacert.pem        
-----BEGIN CERTIFICATE-----
MIIDdzCCAl+gAwIBAgIJANUE5aD9sA6QMA0GCSqGSIb3DQEBCwUAMFIxCzAJBgNV
BAYTAkFVMRMwEQYDVQQIDApxdWVlbnNsYW5kMREwDwYDVQQHDAhSZW1vdmUxEjAQ
BgNVBAoMCVRlc3QgQ28wHhcNMjAwMTAxMDAwMDAwWhcNMzAwMTAxMDAwMDAwWjBS
MQswCQYDVQQGEwJBVTETMBEGA1UECAwKcXVlZW5zbGFuZDERMA8GA1UEBwwIU2F2
ZXR0YTEUMBIGA1UECgwLVGVzdCBDbzCCASIwDQYJKoZIhvcNAQEBBQADggEPADCC
AQoCggEBAK1uM7VhlUO4tqB+7YFQuJVcYqKlyEZB5mXFd2rjxJY2e29L1UqA/HU6
eZpP0q7DyKll0Z0X2uZ8D6I+6dZAGN4FQfYvHcKOLOy0H2Mv6jz/7XG6qE9Blg3L
D1A4zJyxDq1H7pjdv8A0v3y+XKkAwP1zV7iz9VjN71FswE/uj3n5aFdpjRd+g/oE
sxj3pZ+v/o1oxTSdR3nQJbZ+KeF9UdjkC0DP7ZHPz3zqm6c7+1YOP3FdJwPvMEmX
Lz8k6gZ0E5qFQ+FQ+zJLbwxe4nURw2D5TzLS4s+2V+0S1Qb4/m7D2x8Q0yJXwd/Q
6J5I+rwCmjAplZyPQbAgdvBMbrunQI8CAwEAAaNQME4wHQYDVR0OBBYEFAaNzjP2
aQWap6XSeO3/d0EtJXGUMB8GA1UdIwQYMBaAFAaNzjP2aQWap6XSeO3/d0EtJXGU
MAwGA1UdEwQFMAMBAf8wDQYJKoZIhvcNAQELBQADggEBAFqxP90qm4r0dIXW5JEE
q7G9c3l3dZuEk+g9GJj/fYHBqvBg/CvK4L9Yq5b4Dk2/eC+n9Vqj3N1Vot2gSXgw
ZgHs3H8F+36+PltIhy4Ne+klA+gXq0z/Xru39J7Dzl2YcRav8jeEUMtM9/bpnv9S
2Jh+z6sCzC6P6dPxJ6c5pZ2JXSkQ2DzLwWX5Rx1Q3Oz9k5qP1xF7i8tK2pRfKzvE
I7o1lzy4WTGpV4p+ZK5xBw0cJrM3oOMM2iUJQIyIlH+kFJyWvfxK0r9G4hv/3pT0
-----END CERTIFICATE-----
                                                                                                                                                                                                                                            
┌──(ashfaq㉿kali-vm)-[~/Desktop/software/Vault/files]
└─$ vault kv put secrets/files/cacert content=@cacert.pem

Success! Data written to: secrets/files/cacert
                                                                                                                                                                                                                                            
┌──(ashfaq㉿kali-vm)-[~/Desktop/software/Vault/files]
└─$ vault kv get secrets/files/cacert

===== Data =====
Key        Value
---        -----
content    -----BEGIN CERTIFICATE-----
MIIDdzCCAl+gAwIBAgIJANUE5aD9sA6QMA0GCSqGSIb3DQEBCwUAMFIxCzAJBgNV
BAYTAkFVMRMwEQYDVQQIDApxdWVlbnNsYW5kMREwDwYDVQQHDAhSZW1vdmUxEjAQ
BgNVBAoMCVRlc3QgQ28wHhcNMjAwMTAxMDAwMDAwWhcNMzAwMTAxMDAwMDAwWjBS
MQswCQYDVQQGEwJBVTETMBEGA1UECAwKcXVlZW5zbGFuZDERMA8GA1UEBwwIU2F2
ZXR0YTEUMBIGA1UECgwLVGVzdCBDbzCCASIwDQYJKoZIhvcNAQEBBQADggEPADCC
AQoCggEBAK1uM7VhlUO4tqB+7YFQuJVcYqKlyEZB5mXFd2rjxJY2e29L1UqA/HU6
eZpP0q7DyKll0Z0X2uZ8D6I+6dZAGN4FQfYvHcKOLOy0H2Mv6jz/7XG6qE9Blg3L
D1A4zJyxDq1H7pjdv8A0v3y+XKkAwP1zV7iz9VjN71FswE/uj3n5aFdpjRd+g/oE
sxj3pZ+v/o1oxTSdR3nQJbZ+KeF9UdjkC0DP7ZHPz3zqm6c7+1YOP3FdJwPvMEmX
Lz8k6gZ0E5qFQ+FQ+zJLbwxe4nURw2D5TzLS4s+2V+0S1Qb4/m7D2x8Q0yJXwd/Q
6J5I+rwCmjAplZyPQbAgdvBMbrunQI8CAwEAAaNQME4wHQYDVR0OBBYEFAaNzjP2
aQWap6XSeO3/d0EtJXGUMB8GA1UdIwQYMBaAFAaNzjP2aQWap6XSeO3/d0EtJXGU
MAwGA1UdEwQFMAMBAf8wDQYJKoZIhvcNAQELBQADggEBAFqxP90qm4r0dIXW5JEE
q7G9c3l3dZuEk+g9GJj/fYHBqvBg/CvK4L9Yq5b4Dk2/eC+n9Vqj3N1Vot2gSXgw
ZgHs3H8F+36+PltIhy4Ne+klA+gXq0z/Xru39J7Dzl2YcRav8jeEUMtM9/bpnv9S
2Jh+z6sCzC6P6dPxJ6c5pZ2JXSkQ2DzLwWX5Rx1Q3Oz9k5qP1xF7i8tK2pRfKzvE
I7o1lzy4WTGpV4p+ZK5xBw0cJrM3oOMM2iUJQIyIlH+kFJyWvfxK0r9G4hv/3pT0
-----END CERTIFICATE-----
                                                                                                                                                                                                                                            
┌──(ashfaq㉿kali-vm)-[~/Desktop/software/Vault/files]
└─$ vault kv list  secrets/files  

Keys
----
cacert
                                                                                                                                                                                                                                            
┌──(ashfaq㉿kali-vm)-[~/Desktop/software/Vault/files]
└─$ vault kv get -field=content secrets/files/cacert > retrieved_cacert.pem

                                                                                                                                                                                                                                            
┌──(ashfaq㉿kali-vm)-[~/Desktop/software/Vault/files]
└─$ echo "-----BEGIN CERTIFICATE-----
MIIDdzCCAl+gAwIBAgIJANUE5aD9sA6QMA0GCSqGSIb3DQEBCwUAMFIxCzAJBgNV
... (updated content) ...
-----END CERTIFICATE-----" > cacert.pem

                                                                                                                                                                                                                                            
┌──(ashfaq㉿kali-vm)-[~/Desktop/software/Vault/files]
└─$ vault kv put secrets/files/cacert content=@cacert.pem

Success! Data written to: secrets/files/cacert
                                                                                                                                                                                                                                            
┌──(ashfaq㉿kali-vm)-[~/Desktop/software/Vault/files]
└─$ vault kv get -field=content secrets/files/cacert

-----BEGIN CERTIFICATE-----
MIIDdzCCAl+gAwIBAgIJANUE5aD9sA6QMA0GCSqGSIb3DQEBCwUAMFIxCzAJBgNV
... (updated content) ...
-----END CERTIFICATE-----

                                                                                                                                                                                                                                            
┌──(ashfaq㉿kali-vm)-[~/Desktop/software/Vault/files]
└─$ vault kv delete secrets/files/cacert

Success! Data deleted (if it existed) at: secrets/files/cacert
                                                                                                                                                                                                                                            
┌──(ashfaq㉿kali-vm)-[~/Desktop/software/Vault/files]
└─$ 
