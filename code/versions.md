
Why We Collect Existing Secrets First
our Vault setup seems to use a KV v1-like structure (based on the flat "data": {...} response). Here’s why we collect existing secrets before adding a new one:

    Vault KV v1 Stores Secrets as a Single Object per Path:
        At secrets/data/SampleApp/config, we've got one secret object:
        json

        {
          "data": {
            "password": "samplepass",
            "newKey": "newValue",
            "API": "sampleAPI",
            "username": "sampleuser"
          }
        }

        A PUT request to this path replaces the entire "data" object. If we send just {"newKey2": "newValue2"}, it overwrites everything, leaving:
        json

        {
          "data": {
            "newKey2": "newValue2"
          }
        }

    No Partial Updates in KV v1:
        KV v1 doesn’t support appending a single key-value pair. we have to send the full object, so we:
            Fetch the current secrets.
            Add the new key-value pair.
            Write it all back to avoid losing existing data.
    Example:
        Without collecting: Sending {"newKey2": "newValue2"} wipes out password, API, etc.
        With collecting: We keep password, API, etc., and add newKey2.

How Data Is Stored in KV v2 (New Addition)
Now, let’s look at KV v2, which our Vault could use (since we're on v1.19.0 and used secrets/data/ paths), even though our response looks v1-like. Here’s how it’s different:

    KV v2 Structure:
        KV v2 adds versioning and nests the actual secrets under "data"."data", with metadata alongside:
        json

        {
          "request_id": "some-id",
          "lease_id": "",
          "renewable": false,
          "lease_duration": 0,
          "data": {
            "data": {
              "password": "samplepass",
              "newKey": "newValue",
              "API": "sampleAPI",
              "username": "sampleuser"
            },
            "metadata": {
              "created_time": "2025-03-15T10:00:00Z",
              "deletion_time": "",
              "destroyed": false,
              "version": 1
            }
          },
          "wrap_info": null,
          "warnings": null,
          "auth": null
        }

        Key Difference: The secrets live in "data"."data", and "metadata" tracks versioning info.
    Versioning:
        Every time we update the secret at secrets/data/SampleApp/config, KV v2 keeps the old version and creates a new one:
            Version 1: {"password": "samplepass", "API": "sampleAPI"}
            Version 2: {"password": "samplepass", "API": "sampleAPI", "newKey": "newValue"}
        we can access old versions with vault kv get -version=1 secrets/data/SampleApp/config.
    CLI vs. API Behavior:
        The CLI command vault kv put secrets/data/SampleApp/config newKey=newValue looks like it just adds a key, but under the hood, it:
            Fetches the latest version.
            Merges the new key with existing ones.
            Writes a new version.
        The HTTP API (like we’re using) doesn’t auto-merge—we send the full "data" object we want for the new version.

Updating in KV v2 and Old Keys
Here’s why updating in KV v2 doesn’t always hinder old keys, and when it does:

    CLI Patch Command (Doesn’t Hinder Old Keys):
        KV v2 has a vault kv patch command that adds or updates specific keys without touching others:
        bash

        vault kv patch secrets/data/SampleApp/config newKey2=newValue2

        Before (version 1):
        json

        "data": {
          "data": {
            "password": "samplepass",
            "API": "sampleAPI"
          },
          "metadata": {"version": 1, ...}
        }

        After (version 2):
        json

        "data": {
          "data": {
            "password": "samplepass",
            "API": "sampleAPI",
            "newKey2": "newValue2"
          },
          "metadata": {"version": 2, ...}
        }

        The CLI handles the merge for we, keeping old keys intact.
    HTTP API PUT (Can Hinder Unless we Collect):
        With the API (e.g., PUT /v1/secrets/data/SampleApp/config), we send the full "data" object:
        json

        {
          "data": {
            "newKey2": "newValue2"
          }
        }

        Result (version 2):
        json

        "data": {
          "data": {
            "newKey2": "newValue2"
          },
          "metadata": {"version": 2, ...}
        }

        Old keys (password, API) are gone unless we:
            Fetch the current version first (like we do).
            Add newKey2 to it.
            Send the updated full object:
            json

            {
              "data": {
                "password": "samplepass",
                "API": "sampleAPI",
                "newKey2": "newValue2"
              }
            }

        So, even in v2, the API requires collecting existing secrets to preserve old keys—just like v1.
    Why It Doesn’t Hinder (With Proper Tools):
        KV v2’s versioning means old data isn’t lost—it’s stored as previous versions.
        The CLI’s patch command abstracts the merge, but the API doesn’t, so we mimic that behavior in code.

our Setup vs. v2

    our Response: {"data": {...}} (flat, v1-like).
    our Path: secrets/data/SampleApp/config (v2-style).
    Conclusion: we might be using a KV v2 mount with a v1-compatible response (Vault can do this if configured), or it’s just v1. Either way, the API behavior is the same: PUT replaces the whole object.

To confirm v2:
bash

vault kv metadata get secrets/data/SampleApp/config

    If it shows metadata (e.g., version), it’s v2.
    Update in v2 would still need collecting unless using patch-like logic.

Updated Python Example (v2 Aware)
Here’s how we’d add a secret in Python, handling v2 if needed:
python

import requests
import os

VAULT_URL = os.getenv("VAULT_URL")
VAULT_TOKEN = os.getenv("VAULT_TOKEN")
headers = {"X-Vault-Token": VAULT_TOKEN, "Content-Type": "application/json"}

def add_secret(key, value, is_v2=False):
    try:
        # Fetch current secrets
        response = requests.get(VAULT_URL, headers=headers)
        response.raise_for_status()
        data = response.json().get("data", {})
        secrets = data.get("data") if is_v2 else data  # v2 nests under "data"."data"

        # Add new secret
        secrets[key] = value
        
        # Prepare payload
        payload = {"data": secrets} if is_v2 else secrets
        response = requests.put(VAULT_URL, headers=headers, json=payload)
        response.raise_for_status()
        print(f"Added '{key}': '{value}'")
    except requests.RequestException as e:
        print(f"Error: {e}")

# Assuming our current setup (v1-like)
add_secret("newKey2", "newValue2", is_v2=False)

    v1: secrets is flat.
    v2: Wraps in {"data": secrets}.

Final Thoughts

    v1: Always collect to preserve keys—PUT replaces everything.
    v2: Collect for API PUT to preserve keys, or use CLI patch. Old versions are safe either way.
