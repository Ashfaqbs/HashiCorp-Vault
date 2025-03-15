# sampleapp-policy.hcl

# Grant full CRUD and list permissions on secrets/SampleApp
path "secrets/SampleApp/*" {
  capabilities = ["create", "read", "update", "delete", "list"]
}





#Error making API request.


#URL: GET http://127.0.0.1:8200/v1/sys/internal/ui/mounts/secrets/SampleApp/config
#Code: 403. Errors:


# preflight capability check returned 403, please ensure client's policies grant access to path "secrets/SampleApp/config/"
# this error can be seen so we need to mount the base path as secret 

#┌──(ashfaq㉿kali-vm)-[~/Desktop/software/Vault/roles]
#└─$ vault secrets enable -path=secrets kv

# Success! Enabled the kv secrets engine at: secrets/