import requests
import os
import json
from dotenv import load_dotenv

load_dotenv() 

#VAULT_TOKEN = os.getenv("VAULT_TOKEN")  # Make sure VAULT_TOKEN is exported in your shell


VAULT_URL = os.environ["VAULT_URL"]
VAULT_TOKEN = os.environ["VAULT_TOKEN"]

# Headers for authentication
headers = {
    "X-Vault-Token": VAULT_TOKEN,
    "Content-Type": "application/json"
}

def get_secret_by_key(key):
    """Get the value of a specific key from Vault."""
    try:
        response = requests.get(VAULT_URL, headers=headers)
        response.raise_for_status()  # Raise an error for bad status codes
        
        # Parse the JSON response
        data = response.json().get("data", {})
        value = data.get(key)
        
        if value is not None:
            print(f"Value for '{key}': {value}")
        else:
            print(f"Key '{key}' not found.")
        return value
    
    except requests.RequestException as e:
        print(f"Error fetching secret: {e}")
        return None

def get_all_secrets():
    """Get all secrets from Vault."""
    try:
        response = requests.get(VAULT_URL, headers=headers)
        response.raise_for_status()
        
        # Parse the JSON response
        data = response.json().get("data", {})
        
        print("\nAll secrets:")
        if not data:
            print("No secrets found.")
        else:
            for key, value in data.items():
                print(f"{key}: {value}")
        return data
    
    except requests.RequestException as e:
        print(f"Error fetching secrets: {e}")
        return {}

if __name__ == "__main__":
    # Get a specific key (e.g., 'newKey')
    get_secret_by_key("newKey")
    
    # Get all secrets
    get_all_secrets()


    """
    OP 

                                                                                                                                                                                 
(.venv) ┌──(.venv)─(ashfaq㉿kali-vm)-[~/Desktop/software/Vault]
└─$ /home/ashfaq/Desktop/software/Vault/.venv/bin/python /home/ashfaq/Desktop/software/Vault/code/python/app.py
Key 'newKey' not found.

All secrets:
API: sampleAPI
password: samplepass
username: sampleuser
    
    
    
    """