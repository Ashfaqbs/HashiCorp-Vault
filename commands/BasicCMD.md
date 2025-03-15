┌──(ashfaq㉿kali-vm)-[~/Desktop/software/Vault/roles]
└─$ vault version

Vault v1.13.1 (4472e4a3fbcc984b7e3dc48f5a8283f3efe6f282), built 2023-03-23T12:51:35Z
                                                                                                                                                                                                                    
┌──(ashfaq㉿kali-vm)-[~/Desktop/software/Vault/roles]
└─$ curl http://127.0.0.1:8200/v1/sys/health

{"initialized":true,"sealed":false,"standby":false,"performance_standby":false,"replication_performance_mode":"disabled","replication_dr_mode":"disabled","server_time_utc":1741969629,"version":"1.19.0","enterprise":false,"cluster_name":"vault-cluster-4b448dc4","cluster_id":"c70ad736-692a-288d-a0f3-3aa811350feb","echo_duration_ms":0,"clock_skew_ms":0,"replication_primary_canary_age_ms":0}
                                                                                                                                                                                                                    
┌


Let's walk through a complete example using the base path `secrets/` with the root token. We'll assume Vault is running in dev mode and we're authenticated as root.

> **Note:** With the KV secrets engine, we don't have to create the inner paths (like `secrets/dev`) manually. When we write a secret to a new path (e.g., `secrets/dev/some-secret`), Vault automatically handles the inner "folders."

---

### 1. **Enable the KV Secrets Engine at the Base Path**

If we haven't already mounted the KV engine at `secrets/`, do so now:

```bash
vault secrets enable -path=secrets kv
```

This mounts the KV engine at `secrets/`, which will be our base path for storing secrets.

---

### 2. **Add a Secret to `secrets/dev`**

we can directly write a secret to a subpath. For example, to add database credentials for the DEV environment:

```bash
vault kv put secrets/dev/database username="devuser" password="devpass"
```

- **What happens?**  
  we didn't explicitly create `secrets/dev` beforehand; Vault automatically treats it as part of the path hierarchy and stores the data.

---

### 3. **Retrieve the Secret**

To read the stored secret from `secrets/dev/database`:

```bash
vault kv get secrets/dev/database
```

This command returns the key-value pairs we stored (e.g., `username` and `password`).

---

### 4. **Update the Secret**

To update (or add new keys) to the same secret, run:

```bash
vault kv put secrets/dev/database username="devuser" password="newdevpass" host="localhost"
```

- **What happens?**  
  The `username` remains (or is updated if changed), the `password` is updated, and a new key `host` is added.

---

### 5. **List All Keys Under `secrets/dev`**

To list the keys (subpaths) under `secrets/dev`:

```bash
vault kv list secrets/dev
```

This will show we the keys (or subfolders) directly under `secrets/dev`.  
> **Note:** The `list` command only shows the immediate children, not a recursive view.

---

### 6. **Delete the Secret**

If we want to delete the secret at `secrets/dev/database`, run:

```bash
vault kv delete secrets/dev/database
```

This removes the secret data at that specific path.  
> **Note:** The path itself (`secrets/dev/`) still exists conceptually, but without any stored secrets until we add new ones.

---

### Summary

- **No Need for Manual Path Creation:**  
  we can directly use paths like `secrets/dev/database`, and Vault will treat `dev` as part of the hierarchy automatically.

- **CRUD Operations:**  
  - **Put:** Add/update secrets using `vault kv put`.
  - **Get:** Retrieve secrets with `vault kv get`.
  - **List:** Use `vault kv list` to see keys/subpaths.
  - **Delete:** Remove secrets with `vault kv delete`.

By following these steps, we can test and validate all operations on one path (`secrets/dev`). Once we’re comfortable, the same pattern applies to other environments like `secrets/prod` or `secrets/sit`.

                                                                                                                                                                                                                                -Rough      
┌──(ashfaq㉿kali-vm)-[~/Desktop/software/Vault]
└─$ vault secrets enable -path=secrets kv

Success! Enabled the kv secrets engine at: secrets/
                                                                                                                -Add                                                                                                                      
┌──(ashfaq㉿kali-vm)-[~/Desktop/software/Vault]
└─$ vault kv put secrets/dev/database username="devuser" password="devpass"

Success! Data written to: secrets/dev/database
                                                                                                                -Get

┌──(ashfaq㉿kali-vm)-[~/Desktop/software/Vault]
└─$ vault kv get secrets/dev/database
====== Data ======
Key         Value
---         -----
password    devpass
username    devuser
                                                                                                                -Update                                                                                                                           
┌──(ashfaq㉿kali-vm)-[~/Desktop/software/Vault]
└─$ vault  kv put secrets/dev/database username="devuser" password="newdevpass"
Success! Data written to: secrets/dev/database
                                                                                                                                                                                                                                            
┌──(ashfaq㉿kali-vm)-[~/Desktop/software/Vault]
└─$ vault kv get secrets/dev/database                                          
====== Data ======
Key         Value
---         -----
password    newdevpass
username    devuser

                                                                                                                -Update Again the old value

┌──(ashfaq㉿kali-vm)-[~/Desktop/software/Vault]
└─$ vault kv put secrets/dev/database username="devuser" password="devpass"

Success! Data written to: secrets/dev/database
                                                                                                                                                                                                                                            
┌──(ashfaq㉿kali-vm)-[~/Desktop/software/Vault]
└─$ vault kv get secrets/dev/database                                      
====== Data ======
Key         Value
---         -----
password    devpass
username    devuser
                                                                                                                

-Delete and verify                                                                                             

┌──(ashfaq㉿kali-vm)-[~/Desktop/software/Vault]
└─$ vault kv delete  secrets/dev/database
Success! Data deleted (if it existed) at: secrets/dev/database
                                                                                                                                                                                                                                            
┌──(ashfaq㉿kali-vm)-[~/Desktop/software/Vault]
└─$ vault kv get secrets/dev/database    
No value found at secrets/dev/database








# HashiCorp Vault: Key-Value Store Commands

### Overview:
In this document, we'll explain how to interact with Vault's Key-Value (KV) secrets engine by using commands that store, retrieve, and list secrets. We'll also clarify how paths and keys work in the Vault KV store, especially focusing on why `database` is considered a "key" and the distinction between it and the actual key-value pairs (like `username` and `password`).

---

### 1. **Command:** `vault kv get secrets/dev/database`
#### Description:
This command retrieves the data stored at the `secrets/dev/database` path.

#### Explanation:
- **`vault`**: Refers to the Vault CLI tool.
- **`kv`**: Specifies that we're working with the Key-Value secrets engine in Vault.
- **`get`**: Instructs Vault to fetch the data stored at the specified path.
- **`secrets/dev/database`**: This is the path in Vault where the secret is stored. It represents the location where our key-value pairs (like `username` and `password`) are saved.

#### Output:
Initially, if the data is not found:
```
No value found at secrets/dev/database
```

After we've stored the secret using the `vault kv put` command, it will display the key-value pairs stored at that path:
```
====== Data ======
Key         Value
---         -----
password    devpass
username    devuser
```

**Clarification**: In this case, `username` and `password` are **keys** under the `secrets/dev/database` path, and their respective values are `devuser` and `devpass`.

---

### 2. **Command:** `vault kv put secrets/dev/database username="devuser" password="devpass"`
#### Description:
This command stores or updates data at the `secrets/dev/database` path. The `username` and `password` are the keys being stored with their corresponding values.

#### Explanation:
- **`vault`**: Refers to the Vault CLI tool.
- **`kv`**: Specifies the Key-Value secrets engine.
- **`put`**: Instructs Vault to store data at the given path.
- **`secrets/dev/database`**: This is the path in Vault where we're saving the key-value pairs.
- **`username="devuser"` and `password="devpass"`**: These are the actual key-value pairs being stored at the `secrets/dev/database` path.

#### Output:
```
Success! Data written to: secrets/dev/database
```
This indicates that the data (the `username` and `password`) has been successfully written to the Vault.

---

### 3. **Command:** `vault kv list secrets/dev`
#### Description:
This command lists all the keys (or paths) under the `secrets/dev` path.

#### Explanation:
- **`vault`**: Refers to the Vault CLI tool.
- **`kv`**: Specifies the Key-Value secrets engine.
- **`list`**: Instructs Vault to list the keys stored at the given path.
- **`secrets/dev`**: This is the path in Vault under which we want to list keys.

#### Output:
```
Keys
----
database
```
Here, `database` is listed as a key under the `secrets/dev` path. It refers to the **path** where the `username` and `password` keys are stored, not the actual key-value pairs themselves.

---

### Key Clarification: Why is `database` also a Key?

In the context of Vault, **`database` is a key** in the Vault store, but it refers to the **path** where we've stored the key-value pairs. In this case, under the `secrets/dev` path, the `database` key holds the actual data that consists of the `username` and `password` keys with their respective values.

- **`database`** is a key (or "path") that organizes our secrets. 
- Under this path, there are actual **key-value pairs**:
  - `username` → `devuser`
  - `password` → `devpass`

In summary, Vault uses the concept of paths and keys:
- **Paths** are like directories or containers that hold secrets.
- **Keys** inside those paths are the actual data points stored, such as `username`, `password`, or any other data.

### To Summarize:

1. **`secrets/dev/database`** is a **path** (a key in Vault's terminology) under which data is stored.
2. **`username`** and **`password`** are **keys** inside the `secrets/dev/database` path, and their values are `devuser` and `devpass`.

---

### Conclusion:

With these commands, we can interact with Vault to securely store, retrieve, and list secrets. The `database` path acts as a key within Vault, and under that path, we can have multiple keys (such as `username` and `password`) with their associated values. This structure allows we to organize and manage secrets in a hierarchical manner.


---








┌──(ashfaq㉿kali-vm)-[~/Desktop/software/Vault]
└─$ vault kv get secrets/dev/database
No value found at secrets/dev/database
                                                                                                                            
┌──(ashfaq㉿kali-vm)-[~/Desktop/software/Vault]
└─$ vault kv delete  secrets/dev/database
Success! Data deleted (if it existed) at: secrets/dev/database
            
┌──(ashfaq㉿kali-vm)-[~/Desktop/software/Vault]
└─$ vault  kv get secrets/dev        
No value found at secrets/dev
            
┌──(ashfaq㉿kali-vm)-[~/Desktop/software/Vault]
└─$ vault kv get secrets/dev/database

No value found at secrets/dev/database
            
┌──(ashfaq㉿kali-vm)-[~/Desktop/software/Vault]
└─$ vault kv put secrets/dev/database username="devuser" password="devpass"

Success! Data written to: secrets/dev/database
            
┌──(ashfaq㉿kali-vm)-[~/Desktop/software/Vault]
└─$ vault  kv get secrets/dev/database
====== Data ======
Key         Value
---         -----
password    devpass
username    devuser
            
┌──(ashfaq㉿kali-vm)-[~/Desktop/software/Vault]
└─$ vault  kv get secrets/dev         
No value found at secrets/dev
                                                                                                                     
┌──(ashfaq㉿kali-vm)-[~/Desktop/software/Vault]
└─$ vault  kv list secrets/dev
Keys
----
database
                                                                                                                                                                                                                        