---
title: API Reference

language_tabs: # must be one of https://git.io/vQNgJ
  - shell
  - python
  - java

toc_footers:
  - <a href='https://github.com/lord/slate'>Sign Up for a Developer Key</a>


search: true
---




# BBOXX REST API

## Introduction

The BBOXX Pulse API allows developers to integrate third-party applications with BBOXX. This documentation provides the technical information about integrating and configuring a payment system to send payment information to BBOXX. Companies can use this API to allow real-time mobile money transactions to be processed in Pulse. 

[Click Here](pdf/BBOXX%20Mobile%20Money%20Payment%20Process%20Flowchart.pdf) for more information about the BBOXX's Mobile Money process.


## Authentication

```python
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

```

```java
// How to Generate the "Authorization" header in Java

import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.security.InvalidKeyException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64; 

public class GenerateSignature{
    
    protected static String generateSignature(String sharedSecret, String payload){
        byte[] raw_signature = null;
        try {
            Mac mac = Mac.getInstance("HmacSHA1");
            SecretKeySpec secret = new SecretKeySpec(sharedSecret.getBytes(Charset.forName("UTF-8")),
            "HmacSHA1");
            mac.init(secret);
            raw_signature = mac.doFinal(payload.getBytes(Charset.forName("UTF-8")));
        } catch( NoSuchAlgorithmException e) {
            System.out.print("Response: " + e.getMessage());
        } catch (InvalidKeyException e) {
            System.out.print("Response: " + e.getMessage());
        }
    return Base64.encodeBase64String(raw_signature);
    }

    public static void main(String[] args){
        String payload = "";

        // Example Request Data
        String body = "{\"name\":\"Bob\",\"amount\":32}";
        String messageId = "a2536ff7-886a-432d-a5ae-45ed9d12b016";
        String providerId = "12345";
        String customerId = "123";
        
        // Payload must be contructed in this order
        payload = body + messageId + providerId + customerId;
        
        String signature = generateSignature("supersecurekey",payload);
        
        String authHeader = "Authorization: " + customerId + ":" + signature;
        System.out.print(authHeader);
    }
}
```

```shell
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
```

Authentication is achieved by signing every HTTP request made to BBOXX.

The HTTP header “Authorization” is be used to transport the signature this security. The “Authorization” header consists of a `customerId` and the `signature` seperated by a colon `:` example: `Authorization: <customerId>:<signature>`

The `customerId` is a unqiue identifier that represents the customer who made the payment.

The `signature` is generated using the HMAC-SHA1 hashing mechanism.

Steps to create the `Authorization` header:

1. First, combine the following 4 fields into one large `payload` (*NOTE* The order is important): 
    - The Request body 
    - The `messageID` field
    - The `providerId` field
    - The `customerId` field
2. Then make sure the `payload` is in "UTF-8" format.
3. Then create the `raw_signature` by applying the HMAC SHA1 algorithm to the `payload`
4. Then create the `signature` by Base64 encoding the `raw_signature`
5. Then create the “Authorization” header by combining the `customerId` and the `signature` seperated by a colon `:`


## [GET] Customer Details 


### Description
Retrieve customer payment information. 


The GET request will be sent to the following URL:
https://payments-test.bboxx.co.uk/pulseapi/mm/v2/providers/{provider_id}/customers/{customer_id}

### Headers
```bundle exec middleman server```
```
Content-Type:application/json
Authorization:12345:XXXXXXXXXXXX=
MessageID:XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX
```
Name | description 
---------- | ------- 
Content-Type | If a request payload is sent, it should always be a JSON string 
Authorization | The customerId combined with the signature. (See Authentication section)
MessageID | A globaly unique identifier for every request. Generate using UUID4 to ensure uniqueness

### URL Parameters

Fields | description 
---------- | ------- 
provider_id |  The Mobile Operator provider ID that initiated the request. This is the same for all requests. 
customer_id |  A unqiue identifier that represents the customer who made the payment.


### Request Payload
Refer to Authentication above for description of header

```python


url = 'https://payments-test.bboxx.co.uk/pulseapi/mm/v2/providers/{provider_id}/customers/{customer_id}'

header ={'Content-Type':'application/json'
        'Authorization':'12000:fq/LZ0n8YxOp0tC3NLaj6GbPFE8=',
        'MessageID':'74e46dafsf-8bbadafdsfdsffdb4c',
        'Authorization':'{{Authorization}}'} 


post = requests.get(url=url, header= header)
   
```




### Response Payload 


Upon the successful processing of the GET request, a JSON encoded response will be return in the following format: 


```
{
    "minimum_payment": null,
    "down_payment": null,
    "last_payments": [
        {
            "amount": 2100,
            "reference": "AE34252UV",
            "timestamp": "2018-05-15T09:20:35Z"
        },
        {
            "amount": 5680,
            "reference": "ADFST5342",
            "timestamp": "2018-04-13T13:40:38Z"
        }
    ],
    "package": "BBOXX Standard Kit"
    "daily_rate": null,
    "full_name": "BOB SMITH",
    "expire_date": null,
    "phone_numbers": [
        {
            "phone_number": "0765345678"
        },
        {
            "phone_number": "0734646334"
        }
    ]
}


```

Parameter | description 
---------- | ------- 
minimum_payment <br><font color="DarkGray">_int_</font><br>| Amount required for switch on (NULL if customer has no equipment installed)
down_payment <br><font color="DarkGray">_int_</font><br>| Amount required to allow installation
last_payments <br><font color="DarkGray">_JSON Object_</font><br>| List of Customers last 3 payments, including currency, ammount, reference and timestamp 
package <br><font color ="DarkGray">_String_</font><br>| The package installed (currently hardcoded as "BBOXX Standard Kit")
daily_rate <br><font color="DarkGray">_double_</font><br>| Cost of single day of energy
full_name <br><font color="DarkGray">_String_</font><br>| Customers full name
expire_date <br><font color="DarkGray">_String_</font><br>| Date equipment will shut down if no further payments made
phone_numbers <br><font color="DarkGray">_int_</font><br>| List of customer phone numbers including prefered_phone option

## [POST] Add Customer Phone Number 


### Description
Add phone number to customer account. 


The POST request will be sent to the following URL:
https://payments-test.bboxx.co.uk/pulseapi/mm/v2/providers/{provider_id}/customers/{customer_id}/phones/{phone_number}

### Headers

```
Content-Type:application/json
Authorization:12345:XXXXXXXXXXXX=
MessageID:XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX
```
Name | description 
---------- | ------- 
Content-Type | If a request payload is sent, it should always be a JSON string 
Authorization | The customerId combined with the signature. (See Authentication section)
MessageID | A globaly unique identifier for every request. Generate using UUID4 to ensure uniqueness

### URL Parameters

Fields | description 
---------- | ------- 
provider_id |  The Mobile Operator provider ID that initiated the request. This is the same for all requests. 
customer_id |  A unqiue identifier that represents the customer who made the payment.
phone_number | The phone number that you would like to add to the customers account (excluding country code).

### Response Payload 


Upon the successful processing of the POST request, a JSON encoded response will be return in the following format: 


Parameter | description 
---------- | ------- 
result <br><font color="DarkGray">_String_</font><br>| Message confirming process complete
error <br><font color="DarkGray">_JSON Object_</font><br>| Error message object, including description, error_code and status_code

## [DELETE] Remove Customer Phone Number


### Description
Remove phone numbers from customer account


The DELETE request will be sent to the following URL:
https://payments-test.bboxx.co.uk/pulseapi/mm/v2/providers/{provider_id}/customers/{customer_id)/phones/{phone_number}

### Headers

```
Content-Type:application/json
Authorization:12345:XXXXXXXXXXXX=
MessageID:XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX
MessageTimestamp:yyyyMMddHHmmss
```

Name | description 
---------- | ------- 
Content-Type | If a request payload is sent, it should always be a JSON string 
Authorization | The customerId combined with the signature. (See Authentication section)
MessageID | A globaly unique identifier for every request. Generate using UUID4 to ensure uniqueness
MessageTimestamp | The timestamp of the payment in UTC timezone with the yyyyMMddHHmmss format

### URL Parameters
Name | description 
---------- | ------- 
providerId |  The Mobile Operator provider ID that initiated the request. This is the same for all requests. 
customerId |  A unqiue identifier that represents the customer who made the payment. Depending on the provider this can be an account number, email address, internal identifier
phone_number | The phone number that you would like to remove from the customers account (excluding country code).

### Response Payload 


Upon the successful processing of the DELETE request, a JSON encoded response will be return in the following format: 


```

 {"result": "PHONE_NUMBER_REMOVED"}
 
 or
 {
  "error": {
    "description": "Unable to find a customer with the given cuustomer_id.",
    "error_code": "CUSTOMER_NOT_FOUND",
    "status_code": 404
   }
 }


```

Parameter | description 
---------- | ------- 
result <br><font color="DarkGray">_String_</font><br>| Message confirming process complete
error <br><font color="DarkGray">_JSON Object_</font><br>| Error message object, including description, error_code and status_code

## [POST] Payment


### Description
This endpoint allows for a payment to be created. 


The POST request will be sent to the following URL:

#### Version 1
https://payments-test.bboxx.co.uk/pulseapi/mm/1.0/payments/payment/{providerId}/customers/{customerId}/payments

#### Version 2 
https://payments-test.bboxx.co.uk/pulseapi/mm/v2/providers/{providerId}/customers/{customerId}/payments

### Headers

```
Content-Type:application/json
Authorization:12345:XXXXXXXXXXXX=
MessageID:XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX
MessageTimestamp:yyyyMMddHHmmss
```

Name | description 
---------- | ------- 
Content-Type | If a request payload is sent, it should always be a JSON string 
Authorization | The customerId combined with the signature. (See Authentication section)
MessageID | A globaly unique identifier for every request. Generate using UUID4 to ensure uniqueness
MessageTimestamp | The timestamp of the payment in UTC timezone with the yyyyMMddHHmmss format

### URL Parameters
Name | description 
---------- | ------- 
providerId |  The Mobile Operator provider ID that initiated the request. This is the same for all requests. 
customerId |  A unqiue identifier that represents the customer who made the payment. Depending on the provider this can be an account number, email address, internal identifier


### Request Payload
Following table provides information of each JSON field of the HTTP request body:

```shell

curl -H "Accept: application/json" -H "Content-Type: application/json" 
 -H "Authorization:243858606953:9HkobKxzuAiK4j9bHbi80HDMG+Y=" -H "SMSSupport:Y"
 -H "MessageID:74e46da-41ff-8bba-f529-930acbffdb4c", -H "MessageTimestamp:20161029113022" 
 -X GET -d '{"transactionId" : "123647","reference" : "bill-0001","operator" : "Mobile_provider","subscriber" : "243858606953","countryCode" : "CD","transaction_type": "PayBill","first_name": "Jhon","account_number":"45678","last_name":  "Doe","amountTargetValue":  78.79,"amountTargetCurrency":  "GBP","exchangeRate":  1.38,"currency" : "USD","amount" : 100.00 }'
https://payments-test.bboxx.co.uk/pulseapi/mm/1.0/payments/payment/343755867/customers/243858606953/payments


```

```python


url = 'https://payments-test.bboxx.co.uk/pulseapi/mm/1.0/payments/payment/200/customers/12000/payments'

Headers ={' Authorization':'12000:fq/LZ0n8YxOp0tC3NLaj6GbPFE8=',
        'SMSSupport:Y' ,
        'MessageID':'74e46da2-41ff-8bba-f529-930acbffdb4c',
        'MessageTimestamp':'20161029113022'} 

body = '{"transactionId" : "123647",
        "reference" : "4526",
        "operator" : "Mobile_provider", 
        "subscriber" : "250786474859", 
        "countryCode" : "RW", 
        "currency" : "RWF", 
        "transaction_type": "PayBill"
        "first_name": "Jhon",
        "account_number":"45678",
        "last_name": "Doe",
        "amount" : 200.0 }' 

post = requests.post(url=url, header= header, body= body)
   
```


Fields | Description 
----------| ------- 
transactionId <br><font color="DarkGray">_string_</font><br><font color="Red">required</font> | Unique identifier of the payment as generated by the MMO
subscriber <br><font color="DarkGray">_string_</font><br><font color="Red">required</font> | Unique identifier of the end customer in the MMO’s system. This is usually the MSISDN
reference <br><font color="DarkGray">_string_</font><br><font color="Red">required</font> | The reference number as assigned to the transaction by the mobile money provider  
first_name <br><font color="DarkGray">_string_</font><br><font color="Red">required</font> | The first name of the subscriber 
last_name <br><font color="DarkGray">_string_</font><br><font color="Red">required</font> | The last name of the subscriber 
amount <br><font color="DarkGray">_int_</font><br><font color="Red">required</font> | The amount to be paid in local currency
currency <br><font color="DarkGray">_string_</font><br><font color="Red">required</font> | The local currency reference
amountTargetValue <br><font color="DarkGray">_int_</font>| Contains the amount to be paid in target currency
amountTargetCurrency <br><font color="DarkGray">_int_</font>| Contains the target currency reference 
exchangeRate <br><font color="DarkGray">_int_</font>| Exchange rate between both currencies (if applicable)


### Response Payload 


Upon the successful processing of the POST request, a JSON encoded response will be return in the following format: 


```

 #### Version 1
{
  "status": "ACCEPTED",
  "first_name": "James",
  "last_name": "Bond",
  "amountTargetCurrency": 0,
  "reference": "17",
  "providerTransactionId": 1642621,
  "exchangeRate": 0,
  "subscriber": "76299583",
  "currency": "CFA",
  "amount": 7500,
  "transactionId": "12341",
  "smsMessage": "Dear Customer the payment was accepted.",
  "amountTargetValue": 0
}
 
#### Version 2 
{
  "result": "PAYMENT_ACCEPTED"
}


```

#### Version 1

Parameter | description 
---------- | ------- 
status <br><font color="DarkGray">_String_</font><br>| The status of the payment: ACCEPTED
first_name <br><font color="DarkGray">_String_</font><br>| First name of the person who made the payment
last_name <br><font color="DarkGray">_String_</font><br>| Last name of the person who made the payment
reference <br><font color="DarkGray">_String_</font><br>| The unique reference of the payment 
countryCode <br><font color="DarkGray">_String_</font><br>| Country code in format (ISO 3166-1 alpha-2)
providerTransactionId <br><font color="DarkGray">_String_</font><br>| The  provider’s reference ID of the payment 
exchangeRate <br><font color="DarkGray">_Float_</font><br>| The exchange rate sent in the payment 
subscriber <br><font color="DarkGray">_String_</font><br>| The subscriber
currency <br><font color="DarkGray">_String_</font><br>| The 3 letter currency code used in the payment
amount <br><font color="DarkGray">_Float_</font><br>| The amount of the payment
transactionId <br><font color="DarkGray">_String_</font><br>| The transaction id of the payment
smsMessage <br><font color="DarkGray">_String_</font><br>| UNUSED field which can be use to send an SMS to the customer
amountTargetValue <br><font color="DarkGray">_Float_</font><br>| The amount of the payment after currency conversion

#### Version 2

Parameter | description 
---------- | ------- 
result <br><font color="DarkGray">_String_</font><br>| The status of the payment. Should be: PAYMENT_ACCEPTED




## [GET] Payment

```shell

curl -H "Accept: application/json" -H "Content-Type: application/json" 
 -H "Authorization:243858606953:9HkobKxzuAiK4j9bHbi80HDMG+Y=" 
 -H "SMSSupport:Y"
 -H "MessageID:74e46da-41ff-8bba-f529-930acbffdb4c", 
 -H "MessageTimestamp:20161029113022" 
https://payments-test.bboxx.co.uk/pulseapi/mm/1.0/payments/payment/343755867/customers/243858606953/payments
    
```


```python
import requests

url =  'https://payments-test.bboxx.co.uk/pulseapi/mm/1.0/payments/payment/200/customers/12000/payments'

Headers={' Authorization':'12000:fq/LZ0n8YxOp0tC3NLaj6GbPFE8=', 'SMSSupport:Y' ,
          'MessageID':'74e46da2-41ff-8bba-f529-930acbffdb4c','MessageTimestamp':'20161029113022'} 

post = requests.get(url=url, header=header)
```

### Description
The GET request will bring you the content of a historical transaction

The GET request will be sent to the following URL:
https://payments-test.bboxx.co.uk/pulseapi/1.0/payments/payment/{providerId}/customers/{customerId}/transaction/{transactionId}/payments
### Headers

```
Content-Type:application/json
Authorization:12345:XXXXXXXXXXXX=
MessageID:XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX
MessageTimestamp:yyyyMMddHHmmss
```

Name | description 
---------- | ------- 
Content-Type | If a request payload is sent, it should always be a JSON string 
Authorization | The customerId combined with the signature. (See Authentication section)
MessageID | A globaly unique identifier for every request. Generate using UUID4 to ensure uniqueness
MessageTimestamp | The timestamp of the payment in UTC timezone with the yyyyMMddHHmmss format

### URL Parameters 

Name | description 
---------- | ------- 
providerId | If a request payload is sent, it should always be a JSON string 
customerId | Hash to authentication the requesting system 
transactionId | The unique reference for the payment that should be retreieved 



### Response Payload 


Upon the successful processing of the POST request, a JSON encoded response will be return in the following format: 



```

 {
  "status": "RECEIVED",
  "transactionId": "ryAJfphyxTd1bIqR3bT0",
  "transactionTimestamp": "2016-11-02T14:59:23Z"
}


```


Parameter | description 
---------- | ------- 
status <br><font color="DarkGray">_String_</font><br>| The status of the payment
transactionId <br><font color="DarkGray">_String_</font><br>| The unique reference for the payment  
transactionTimestamp <br><font color="DarkGray">_String_</font><br>| The datetime of the payment in UTC ISO format. 




##  Errors code 


The Bboxx Mobile Money API uses the following error codes:




Code Error | description | Exemple
---------- | ------------| --------
4000 | Provider information wrong  | your application is not register in the system please check your account. 
4001 | Customer not found | The provided CustomerID was not found in the system.
4002 | The description message | Transaction cannot be completed as multiple customers have been found for the provided customerID.
4003 | Invalid amount  | Unable to validate this transaction as the amount specified is not valid e.g: zero negative or invalid format amount.
4004 |  Forbidden operation | The customer identified by the customerId is not allowed to perform this operation.
4005 | Invalid Reference  | The reference provided in the message is invalid. i.e: The reference is unknown.
4006 | Provider information wrong  | The provided currency is not valid.
4007 |  Operation not succeed  | Invalid message format.
4008 | Field value length too large | Field value exceeded the maximum length allowed.
4009 |  Transaction ID not found   | The transaction ID provided was not found. 
40010|  Duplicate Transaction ID | The Customer for provided customerID was not found in system.








# BBOXX SOAP API

## Introduction

The BBOXX SOAP API is based on open standards known collectively as web services, which include the Simple Object Access Protocol (SOAP), Web Services Definition Language (WSDL), and the XML Schema Definition language (XSD). A wide range of development tools on a variety of platforms support web services.

Like many web services, BBOXX SOAP API is a combination of client-side and server-side schemas.


## Authentication

When you call BBOXX SOAP API, you must authenticate each request by using a username and password provided using "BASIC Authentication". 

### How Basic Authentication Works

```shell

 curl -H "Content-Type: text/xml" -u MobileProvider:3drvJjs9#324Qfa 
 -X POST -d @Payment.xml -i  https://payments-test.bboxx.co.uk/pulseapi/mm/XMLPay

    
```  

In basic authentication, the client requests a URL that requires authentication. The server requests the client (or user agent) to authenticate itself by sending a **401-Not Authorized** code. The client, in return, sends back the same request but with login credentials as a base64 encoded string in the format `username:password`. This string is sent in the `Authorization` header field as the following:

```Authorization: Basic {base64_encode(username:password)}```

So if the username is tutsplus and the password is 123456, the following header field would be sent with the request:

```Authorization: Basic dHV0c3BsdXM6MTIzNDU2```

Since the base64 encoded string can easily be decoded, this method is highly insecure to be used on an open network. Hence this method should only be used for debugging and development purposes when the connection between the server and the client is trusted.


## Request


#### Description
This endpoint allows for a payment to be created. 


The POST request will be sent to the following URL:
https://payments-test.bboxx.co.uk/pulseapi/mm/XMLPay



#### Request Payload
Following table provides information of each XMl fields:


```xml

<?xml version="1.0" encoding="UTF-8"?>
<SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/">
<SOAP-ENV:header>
</SOAP-ENV:header>
    <SOAP-ENV:Body>
        <ns1 paymentDone>
        <countryCode>TG</countryCode>
        <transactionId>123647</transactionId> 
        <reference>4526</reference>
        <operator>BBOXX Telecom</operator>
        <subscriber>250786474859</subscriber>
        <transaction_type>PayBill</transaction_type>
        <first_name>Jhon</first_name> 
        <account_number>45678</account_number> 
        <last_name>Doe</last_name> 
        <amount>10000.00</amount> 
        <currency>CFA </currency>
        <amountTargetValue>18.60 </amountTargetValue>
        <amountTargetCurrency>USD</amountTargetCurrency>
        <exchangeRate>0.0019</exchangeRate>
        <freeField>JOE</freeField>
        </ns1 paymentDone>
    </SOAP-ENV:Body>
</SOAP-ENV:Envelope>


</xml>
      
```



Fields | Description 
----------| ------- 
transactionId <br><font color="DarkGray"></font> | Unique identifier of the payment as generated by the MMO
operator<br><font color="DarkGray"></font> | Unique identifier of the MMO
subscriber <br><font color="DarkGray"></font> | Unique identifier of the end customer in the MMO’s system. This is usually the MSISDN
reference <br><font color="DarkGray"></font>| The reference number as assigned to the transaction by the mobile money provider  
internal_transaction_id<br><font color="DarkGray"></font> | The internal ID of the transaction within the System. 
transaction_type <br><font color="DarkGray"></font>| The type of the transaction eg. Paybill, Buygoods etc, 
account_number <br><font color="DarkGray"></font> | The account number as entered by the subscriber. (optional – only for Paybill payments) 
first_name <br><font color="DarkGray"></font> | The first name of the subscriber 
last_name <br><font color="DarkGray"></font>| The last name of the subscriber 
amount <br><font color="DarkGray"></font>| The amount to be paid in local currency
amountTargetValue <br><font color="DarkGray"></font>| Contains the amount to be paid in target currency
amountTargetCurrency <br><font color="DarkGray"></font>| Contains the target currency reference 
exchangeRate <br><font color="DarkGray"></font>| Exchange rate between both currencies (if applicable)
currency <br><font color="DarkGray"></font> | The local currency reference
status <br><font color="DarkGray"></font> |  Status of the request : Accepted | Refused | Error




### Response Payload 


Upon the successful processing of the POST request, a XML response will be return : 


```xml

<?xml version="1.0" encoding="UTF-8"?>
  <SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/" >
    <SOAP-ENV:Body>
      <ns1:paymentDoneResponse>
      <date> 2017-02-28 16:30:46.023428</date>
      <code>200</code>
      <description>  Success </description>
      </ns1:paymentDoneResponse>
    </SOAP-ENV:Body>
  </SOAP-ENV:Envelope>

</xml>
      
```



Parameter | description 
---------- | ------- 
date| The transaction timestamp
code | The payment status code
description | The description message  




##  Errors code 


The Bboxx Mobile Money API uses the following error codes:



Code Error | description | Exemple
---------- | ------------| --------
4000 | Provider information wrong  | your application is not register in the system please check your account. 
4001 | Customer not found | The provided CustomerID was not found in the system.
4002 | The description message | Transaction cannot be completed as multiple customers have been found for the provided customerID.
4003 | Invalid amount  | Unable to validate this transaction as the amount specified is not valid e.g: zero negative or invalid format amount.
4004 |  Forbidden operation | The customer identified by the customerId is not allowed to perform this operation.
4005 | Invalid Reference  | The reference provided in the message is invalid. i.e: The reference is unknown.
4006 | Provider information wrong  | The provided currency is not valid.
4007 |  Operation not succeed  | Invalid message format.
4008 | Field value length too large | Field value exceeded the maximum length allowed.
4009 |  Transaction ID not found   | The transaction ID provided was not found. 
40010|  Duplicate Transaction ID | The Customer for provided customerID was not found in system.


