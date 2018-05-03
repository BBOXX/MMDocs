# How to Generate the "Authorization" header in Python

import hmac
from hashlib import sha1

def generate_signature(shared_secret, payload):
    raw_signature = hmac.new(shared_secret, payload.encode('UTF-8'), sha1)
    signature = raw_signature.digest().encode("base64").rstrip('\n')
    return signature

# Example Request Data
customerId  = "123";
body        = "{\"name\":\"Bob\",\"amount\":32}"
messageId   = "a2536ff7-886a-432d-a5ae-45ed9d12b016"
providerId  = "12345"

# Payload must be contructed in this order
payload = body + messageId + providerId + customerId

# Generate Signature
signature = generate_signature("supersecurekey",payload)
# Construct Authorization Header
authHeader = "Authorization: " + customerId + ":" + signature;

print(authHeader)