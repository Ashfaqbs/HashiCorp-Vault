


┌──(ashfaq㉿kali-vm)-[~]
└─$ curl -fsSL https://releases.hashicorp.com/vault/1.13.1/vault_1.13.1_linux_amd64.zip -o vault.zip

                                                    
┌──(ashfaq㉿kali-vm)-[~]
└─$ mv vault.zip Desktop/software 

──(ashfaq㉿kali-vm)-[~/Desktop/software]
└─$ sudo apt install unzip

unzip is already the newest version (6.0-28).
unzip set to manually installed.
Summary:
  Upgrading: 0, Installing: 0, Removing: 0, Not Upgrading: 2283
                                                                                                                                                                                                            
┌──(ashfaq㉿kali-vm)-[~/Desktop/software]
└─$ unzip vault.zip

Archive:  vault.zip
  inflating: vault                   
                                                                                                                                                                                                                     
┌──(ashfaq㉿kali-vm)-[~/Desktop/software]
└─$ ls
vault  vault.zip
                                                                                                                                                                                                                     
┌──(ashfaq㉿kali-vm)-[~/Desktop/software]
└─$ sudo mv vault /usr/local/bin/

                                                                                                                                                                                                                     
┌──(ashfaq㉿kali-vm)-[~/Desktop/software]
└─$ vault --version

Vault v1.13.1 (4472e4a3fbcc984b7e3dc48f5a8283f3efe6f282), built 2023-03-23T12:51:35Z
                                                                                                                                    
┌──(ashfaq㉿kali-vm)-[~/Desktop/software]
└─$ docker run -d --name=vault -e 'VAULT_DEV_ROOT_TOKEN_ID=root' -p 8200:8200 hashicorp/vault:latest

ee741d5f23a50ff8dc88c7c8a5b1b2a607d32a0635969897f9d5d432b9765ae2
                                                                                                                                                                                                          
┌──(ashfaq㉿kali-vm)-[~/Desktop/software]
└─$ export VAULT_ADDR='http://127.0.0.1:8200'
                                                                                                                                                                                                                     
┌──(ashfaq㉿kali-vm)-[~/Desktop/software]
└─$ export VAULT_TOKEN='root'
                                                                                                                                                                                                                     
┌──(ashfaq㉿kali-vm)-[~/Desktop/software]
└─$ vault status

Key             Value
---             -----
Seal Type       shamir
Initialized     true
Sealed          false
Total Shares    1
Threshold       1
Version         1.19.0
Build Date      2025-03-04T12:36:40Z
Storage Type    inmem
Cluster Name    vault-cluster-c344b9d2
Cluster ID      31362d5b-23d0-39b0-c0d4-ffc3f660a77d
HA Enabled      false
                                                                                                                                                                                                                     
┌──(ashfaq㉿kali-vm)-[~/Desktop/software]
└─$ vault kv put secret/my-secret key1=value1 key2=value2

==== Secret Path ====
secret/data/my-secret

======= Metadata =======
Key                Value
---                -----
created_time       2025-03-13T16:46:15.265054202Z
custom_metadata    <nil>
deletion_time      n/a
destroyed          false
version            1
                                                                                                                                                                                                                     
┌──(ashfaq㉿kali-vm)-[~/Desktop/software]
└─$ vault kv get secret/my-secret

==== Secret Path ====
secret/data/my-secret

======= Metadata =======
Key                Value
---                -----
created_time       2025-03-13T16:46:15.265054202Z
custom_metadata    <nil>
deletion_time      n/a
destroyed          false
version            1

==== Data ====
Key     Value
---     -----
key1    value1
key2    value2
                                                                                                                                                                                                                     
┌──(ashfaq㉿kali-vm)-[~/Desktop/software]
└─$ vault kv put secret/my-secret key1=new_value1 key3=value3

==== Secret Path ====
secret/data/my-secret

======= Metadata =======
Key                Value
---                -----
created_time       2025-03-13T16:46:29.695989182Z
custom_metadata    <nil>
deletion_time      n/a
destroyed          false
version            2
                                                                                                                                                                                                                     
┌──(ashfaq㉿kali-vm)-[~/Desktop/software]
└─$ vault kv delete secret/my-secret

Success! Data deleted (if it existed) at: secret/data/my-secret
                                                                                                                                                                                                                     
┌──(ashfaq㉿kali-vm)-[~/Desktop/software]
└─$ vault kv list secret/

Keys
----
my-secret














- Windows
- run the vault image 

### 1. **Set the environment variables**

If we're using **Command Prompt** (`cmd`), we can use the following command to set the environment variables:

```bash
set VAULT_ADDR=http://127.0.0.1:8200
set VAULT_TOKEN=root
```

If we're using **PowerShell**, we need to use `$env:` instead:

```bash
$env:VAULT_ADDR="http://127.0.0.1:8200"
$env:VAULT_TOKEN="root"
```

### 2. **Check Vault Status**

After setting the environment variables, we can try running the `vault status` command again. For **Command Prompt** (`cmd`), just run:

```bash
vault status
```

In **PowerShell**, it will work the same way as long as the environment variables are set.

- download the vault exe 