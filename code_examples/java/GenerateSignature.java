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