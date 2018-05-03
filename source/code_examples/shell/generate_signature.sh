#  How to Generate the "Authorization" header in BASH

body="{\"name\":\"Bob\",\"amount\":32}"
messageId="a2536ff7-886a-432d-a5ae-45ed9d12b016"
providerId="12345"
customerId="123"

function generate_signature {
  sharedSecret="$1"
  payload="$2"
  echo -n "$payload" | openssl dgst -binary -sha1 -hmac "$sharedSecret" | openssl base64
}

signature=`generate_signature supersecurekey $body$messageId$providerId$customerId`
authHeader="Authorization: $customerId:$signature"
echo $authHeader