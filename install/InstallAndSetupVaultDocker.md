docker pull hashicorp/vault:latest


docker run -d --name=vault -e 'VAULT_DEV_ROOT_TOKEN_ID=root' -p 8200:8200 hashicorp/vault:latest


stopped ... 








┌──(ashfaq㉿kali-vm)-[~]
└─$ docker ps -a
CONTAINER ID   IMAGE                                 COMMAND                  CREATED        STATUS                      PORTS     NAMES
ee741d5f23a5   hashicorp/vault:latest                "docker-entrypoint.s…"   12 hours ago   Exited (0) 11 hours ago               vault
c539985b8e7b   gcr.io/k8s-minikube/kicbase:v0.0.46   "/usr/local/bin/entr…"   12 hours ago   Exited (130) 12 hours ago             minikube
                                                                                                                                                                                                                    
┌──(ashfaq㉿kali-vm)-[~]
└─$ docker start ee
ee
                                                                                                                                                                                                                    
┌──(ashfaq㉿kali-vm)-[~]
└─$ export VAULT_ADDR=http://127.0.0.1:8200

                                                                                                                                                                                                                    
┌──(ashfaq㉿kali-vm)-[~]
└─$ export VAULT_TOKEN=root

                                                                                                                                                                                                                    
┌──(ashfaq㉿kali-vm)-[~]
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
Cluster Name    vault-cluster-04cdc6e1
Cluster ID      d56aac86-ad83-0fc8-a9f9-3dc2be8f8bb2
HA Enabled      false
