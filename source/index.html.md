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

The  HTTP(S) POST Notification API is designed to help developers integrate third-party applications with BBOXX Mobile Money-system. This guide provides the technical information about integrating and configuring a system by  using RESTful HTTP(S) POST to recieve notification of Mobile Money transaction information. Companies can use this API to allow real-time mobile money transactions to be processed via Pulse. This document describes the HTTP POST parameters and the format of the expected response.
Synchronous or Near Real-Time Transactions.


## Authentication



 To authenticate the user and ensuring data integrity, a secure Message Authentication Code (HMAC) is used.
Technically, the HTTP header “Authorization” will be used for this additional security. The header will include both a
client identifier and the hash value, e.g.:


```python

Authorization: clientXYZ:uCMfSzkjue+HSDygYB5aEg==

```

The hash mechanism uses is  HMAC-SHA1. HMAC allows you to both authenticate the
client and verify the integrity of the request message. 
 HMAC is used as follows:


The client combines the JSON message , the message ID , path variables and the shared key, applies the HMAC function and retrieves the hash output.


The client adds the “Authorization” header to the request <br>
The client sends the request <br>
The server receives the messages and extracts the “Authorization” header <br>
Based on the client id the server finds the shared secret <br>
The server accepts the request if the two hash values are matching <br>
In case the hash values don’t match a HTTP 403 (Forbidden) response should be sent. <br>
Note: the resulting hash value should be encoded in the Base 64 encoding scheme. <br>




```python

        import hmac
        from hashlib import sha1

        dataencoded = body + MessageId + originId + CustomerId
        hashed = hmac.new(signature, dataencoded.encode('UTF-8'), sha1)
        responseIntegrity = hashed.digest().encode("base64").rstrip('\n')
        header_auth = CustomerId+':'+responseIntegrity

```

```java

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64; 
import org.apache.commons.io.IOUtils; 

response.setHeader("ResponseIntegrity", encodeHmac(“sharedSecret”,
                  jsonResponseFromServiceProviderToHub));
…

protected static String encodeHmac(String sharedSecret, String message){
 byte[] digest = null;
 try {
     Mac mac = Mac.getInstance("HmacSHA1");
     SecretKeySpec secret = new SecretKeySpec(sharedSecret.getBytes(Charset.forName("UTF-8")),
    "HmacSHA1");
     mac.init(secret);
     digest = mac.doFinal(message.getBytes(Charset.forName("UTF-8")));
     } catch( NoSuchAlgorithmException e) {
        System.out.println("Response: " + e.getMessage());
 } catch (InvalidKeyException e) {
    System.out.println("Response: " + e.getMessage());
 }
    return Base64.encodeBase64String(digest);
 }

```

Name | description 
---------- | ------- 
body | body content of the Request
MessageId | The value of the identifier is of the following format GUID, where each digit is a hexadecimal value.
originId | Unique identifier of the Mobile Money Provider
CustomerId |Unique identifier of the customer  






## Headers

The following header is mandatory for every message exchanged with the REST API :


```python


Headers = {' Authorization':'12000:fq/LZ0n8YxOp0tC3NLaj6GbPFE8=', 
          'SMSSupport:Y' ,
          'MessageID':'74e46da2-41ff-8bba-f529-930acbffdb4c',
          'MessageTimestamp':'20161029113022'} 

```

Name | description 
---------- | ------- 
Content-Type | If a request payload is sent, it should always be a JSON string 
Authorization | Hash to authentication the requesting system 
MessageTimestamp |Message timestamp  



## HTTP POST


### Description
This endpoint allows for a payment to be created. 


The POST request will be sent to the following URL:
https://apierp.bboxx.co.uk/mm/1.0/payments/payment/<Providerid>/customers/<CustomerId>/payments


Name | description 
---------- | ------- 
ProviderId |  Uniquely identifies the Mobile Operator that initiated the request
CustomerId |  Uniquely defines a customer in the SP system. Depending on the provider this can be an account number, email address, internal identifier





### Request Payload
Following table provides information of each JSON field of the HTTP request body:

```shell

curl -H "Accept: application/json" -H "Content-Type: application/json" 
 -H "Authorization:243858606953:9HkobKxzuAiK4j9bHbi80HDMG+Y=" -H "SMSSupport:Y"
 -H "MessageID:74e46da-41ff-8bba-f529-930acbffdb4c", -H "MessageTimestamp:20161029113022" 
 -X GET -d '{"transactionId" : "123647","reference" : "bill-0001","operator" : "Mobile_provider","subscriber" : "243858606953","countryCode" : "CD","transaction_type": "PayBill","first_name": "Jhon","account_number":"45678","last_name":  "Doe","amountTargetValue":  78.79,"amountTargetCurrency":  "GBP","exchangeRate":  1.38,"currency" : "USD","amount" : 100.00 }'
https://apierp.bboxx.co.uk/mm/1.0/payments/payment/343755867/customers/243858606953/payments


```

```python


url = 'https://apierp.bboxx.co.uk/mm/1.0/payments/payment/200/customers/12000/payments'

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
transactionId <br><font color="DarkGray">_string_</font> | Unique identifier of the payment as generated by the MMO
operator<br><font color="DarkGray">_string_</font> | Unique identifier of the MMO
subscriber <br><font color="DarkGray">_string_</font> | Unique identifier of the end customer in the MMO’s system. This is usually the MSISDN
reference <br><font color="DarkGray">_string_</font>| The reference number as assigned to the transaction by the mobile money provider  
internal_transaction_id<br><font color="DarkGray">_string_</font> | The internal ID of the transaction within the System. 
transaction_type <br><font color="DarkGray">_string_</font>| The type of the transaction eg. Paybill, Buygoods etc, 
account_number <br><font color="DarkGray">_string_</font> | The account number as entered by the subscriber. (optional – only for Paybill payments) 
first_name <br><font color="DarkGray">_string_</font> | The first name of the subscriber 
last_name <br><font color="DarkGray">_string_</font>| The last name of the subscriber 
amount <br><font color="DarkGray">_int_</font>| Contains the amount to be paid in local currency
amountTargetValue <br><font color="DarkGray">_int_</font>| Contains the amount to be paid in target currency
amountTargetCurrency <br><font color="DarkGray">_int_</font>| Contains the target currency reference 
exchangeRate <br><font color="DarkGray">_int_</font>| Exchange rate between both currencies (if applicable)
currency <br><font color="DarkGray">_string_</font> | Currency in use 



### Response Payload 


Upon the successful processing of the POST request, a JSON encoded response will be return in the following format: 


```python

 {"providerTransactionId":"0123456",
      "status": "ACCEPTED",
      "Description": "Dear Customer the payment was accepted."}


```

Parameter | description 
---------- | ------- 
ProviderTransactionId | The  provider’s reference ID of the payment 
Status | The status of the payment: 
Description | Tne description message  




## HTTP GET


### Description
The GET request will bring you the content of a historical transaction


The GET request will be sent to the following URL:
https://apierp.bboxx.co.uk/mm/1.0/payments/payment/<Providerid>/customers/<CustomerId>/payments


```shell

curl -H "Accept: application/json" -H "Content-Type: application/json" 
 -H "Authorization:243858606953:9HkobKxzuAiK4j9bHbi80HDMG+Y=" 
 -H "SMSSupport:Y"
 -H "MessageID:74e46da-41ff-8bba-f529-930acbffdb4c", 
 -H "MessageTimestamp:20161029113022" 
https://apierp.bboxx.co.uk/mm/1.0/payments/payment/343755867/customers/243858606953/payments
    
```


```python
import requests

url =  'https://apierp.bboxx.co.uk/mm/1.0/payments/payment/200/customers/12000/payments'

Headers={' Authorization':'12000:fq/LZ0n8YxOp0tC3NLaj6GbPFE8=', 'SMSSupport:Y' ,
          'MessageID':'74e46da2-41ff-8bba-f529-930acbffdb4c','MessageTimestamp':'20161029113022'} 

post = requests.get(url=url, header=header)
```



Name | description 
---------- | ------- 
ProviderId | If a request payload is sent, it should always be a JSON string 
CustomerId | Hash to authentication the requesting system 







### Response Payload


Upon the successful processing of the GET request, a JSON encoded response will be return in the following format:




```shell

 '{"transactionId" : "123647",
        "reference" : "4526",
        "operator" : "Mobile_provider", 
        "subscriber" : "250786474859", 
        "countryCode" : "RW", 
        "currency" : "RWF", 
        "transaction_type": "PayBill"
        "first_name": "Jhon",
        "account_number": "45678",
        "last_name": "Doe",
        "amount" : 200.0 }' 
   
```

```python

 '{"transactionId" : "123647",
        "reference" : "4526",
        "operator" : "Mobile_provider", 
        "subscriber" : "250786474859", 
        "countryCode" : "RW", 
        "currency" : "RWF", 
        "transaction_type": "PayBill"
        "first_name": "Jhon",
        "account_number": "45678",
        "last_name": "Doe",
        "amount" : 200.0 }' 

      
```


Fields | Description 
----------| ------- 
transactionId <br><font color="DarkGray">_string_</font> | Unique identifier of the payment as generated by the MMO
operator<br><font color="DarkGray">_string_</font> | Unique identifier of the MMO
subscriber <br><font color="DarkGray">_string_</font> | Unique identifier of the end customer in the MMO’s system. This is usually the MSISDN
reference <br><font color="DarkGray">_string_</font>| The reference number as assigned to the transaction by the mobile money provider  
internal_transaction_id<br><font color="DarkGray">_string_</font> | The internal ID of the transaction within the System. 
transaction_type <br><font color="DarkGray">_string_</font>| The type of the transaction eg. Paybill, Buygoods etc, 
account_number <br><font color="DarkGray">_string_</font> | The account number as entered by the subscriber. (optional – only for Paybill payments) 
first_name <br><font color="DarkGray">_string_</font> | The first name of the subscriber 
last_name <br><font color="DarkGray">_string_</font>| The last name of the subscriber 
amount <br><font color="DarkGray">_int_</font>| The amount to be paid in local currency
amountTargetValue <br><font color="DarkGray">_int_</font>| Contains the amount to be paid in target currency
amountTargetCurrency <br><font color="DarkGray">_int_</font>| Contains the target currency reference 
exchangeRate <br><font color="DarkGray">_int_</font>| Exchange rate between both currencies (if applicable)
currency <br><font color="DarkGray">_string_</font> | The local currency reference
status <br><font color="DarkGray">_string_</font> | 3 possible status of the request : Accepted | Refused | Error


### Response Payload 


Upon the successful processing of the POST request, a JSON encoded response will be return in the following format: 



```shell

 {"providerTransactionId":"0123456",
        "status": "ACCEPTED",
        "Description": "Dear Customer the payment was accepted."}


```


```python

 {"providerTransactionId":"0123456",
        "status": "ACCEPTED",
        "Description": "Dear Customer the payment was accepted."}


```



Parameter | description 
---------- | ------- 
ProviderTransactionId | The  provider’s reference ID of the payment 
Status | The status of the payment: 
Description | Tne description message  






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

When you call BBOXX SOAP API, you must authenticate each request by using a set of API credentials. BBOXX associates a set of API credentials with a specific Mobile Money account.



```shell

 curl -H "Content-Type: text/xml" -u MobileProvider:3drvJjs9#324Qfa 
 -X POST -d @Payment.xml -i  https://apierp.bboxx.co.uk/mm/XMLPay

    
```  


## Request


#### Description
This endpoint allows for a payment to be created. 


The POST request will be sent to the following URL:
https://apierp.bboxx.co.uk/mm/XMLPay



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


