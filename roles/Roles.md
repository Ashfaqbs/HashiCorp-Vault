We were using root role now we will use a role only authorized for specific path defined in hcl file

Yes, we can do that by creating a policy that restricts access to only a specific path (in this case, `secrets/SampleApp`) and then generating a token tied to that policy. In Vault, rather than “users” in a traditional sense, we define policies and then create tokens (or use another auth method) that have those policies attached. This way, we can ensure that this new token (representing our SampleApp) only has permissions for the `secrets/SampleApp` path.

Let’s walk through the steps:

---

### 1. **Create a Policy for SampleApp**

Create a policy file (e.g., `sampleapp-policy.hcl`) with the following content:

```hcl
# sampleapp-policy.hcl

# Grant full CRUD and list permissions on secrets/SampleApp
path "secrets/SampleApp/*" {
  capabilities = ["create", "read", "update", "delete", "list"]
}
```

- **What this does:**  
  This policy allows any token associated with it to perform create, read, update, delete, and list operations on any key under `secrets/SampleApp/`.

---

### 2. **Write the Policy to Vault**

Use the root token (or another token with admin rights) to write the policy into Vault:

```bash
vault policy write sampleapp sampleapp-policy.hcl
```

- **Result:**  
  Vault registers a new policy named `sampleapp` with the defined permissions.

---

### 3. **Create a Token for SampleApp**

Now, generate a token that has only the `sampleapp` policy attached. This token will represent the new user or application:

```bash
vault token create -policy="sampleapp" -orphan
```

- **Note:**  
  The `-orphan` flag ensures that the token does not have a parent token, making it independent.

- **Result:**  
  Vault will output a new token. Copy this token—it will look something like `s.xxxxxx...`.

---

### 4. **Test the New Token**

To simulate the new user or SampleApp, export the new token and test access:

```bash
export VAULT_TOKEN=<new-sampleapp-token>
```

Now, try accessing `secrets/SampleApp`:

```bash
vault kv put secrets/SampleApp/config username="sampleuser" password="samplepass" API="sampleAPI"
vault kv get secrets/SampleApp/config
```

- **Expected Outcome:**  
  These commands should work because the token has permissions for `secrets/SampleApp/*`.

- **Test Access to Other Paths:**  
  If we try accessing a path outside of `secrets/SampleApp`, such as `secrets/env`, it should be denied:

```bash
vault kv get secrets/env/SIT
```

we should receive a permission denied error, confirming that the policy is correctly scoped.

---

### Summary

1. **Create Policy File:** Define permissions for `secrets/SampleApp/*`.
2. **Write Policy:** Use `vault policy write sampleapp sampleapp-policy.hcl`.
3. **Create Token:** Generate a token with the `sampleapp` policy.
4. **Test:** Export the token and verify that it has access only to `secrets/SampleApp`.

This approach gives us SampleApp token only the required permissions, ensuring that it cannot interfere with other parts of our Vault.












┌──(ashfaq㉿kali-vm)-[~/Desktop/software/Vault/roles]
└─$ vault policy write sampleapp sampleapp-policy.hcl

Success! Uploaded policy: sampleapp
                                                                                                                                                                                                                                     
┌──(ashfaq㉿kali-vm)-[~/Desktop/software/Vault/roles]
└─$ vault token create -policy="sampleapp" -orphan

Key                  Value
---                  -----
token                hvs.CAESIHIBWpkNLePpTBZ4ud_jcoGHbPSRauzuXmWBl2j9SnJ_Gh4KHGh2cy5MdTN3bDBWV1N0SGdWdEliMWpTdE4ycXc
token_accessor       Vuq1xRBXOc58nWU59w1U7muS
token_duration       768h
token_renewable      true
token_policies       ["default" "sampleapp"]
identity_policies    []
policies             ["default" "sampleapp"]
                                                                                                                                                                                                                                     
┌──(ashfaq㉿kali-vm)-[~/Desktop/software/Vault/roles]
└─$ export VAULT_TOKEN=hvs.CAESIHIBWpkNLePpTBZ4ud_jcoGHbPSRauzuXmWBl2j9SnJ_Gh4KHGh2cy5MdTN3bDBWV1N0SGdWdEliMWpTdE4ycXc 

-earlier we used root now we are using token specified for the .hcl config file                                                                                                                                                                                                                                        
┌──(ashfaq㉿kali-vm)-[~/Desktop/software/Vault/roles]
└─$ vault kv put secrets/SampleApp/config username="sampleuser" password="samplepass" API="sampleAPI"
Success! Data written to: secrets/SampleApp/config
                                                                                                                                                                                                                                     
┌──(ashfaq㉿kali-vm)-[~/Desktop/software/Vault/roles]
└─$ vault kv get secrets/SampleApp/config
====== Data ======
Key         Value
---         -----
API         sampleAPI
password    samplepass
username    sampleuser
                                                                                                                                                                                                                                     
┌──(ashfaq㉿kali-vm)-[~/Desktop/software/Vault/roles]
└─$ vault kv get secrets/env/SIT

Error reading secrets/env/SIT: Error making API request.

URL: GET http://127.0.0.1:8200/v1/secrets/env/SIT
Code: 403. Errors:

* 1 error occurred:
        * permission denied


                                                                                                                                                                                                                                     
┌──(ashfaq㉿kali-vm)-[~/Desktop/software/Vault/roles]
└─$ 
                                                                            