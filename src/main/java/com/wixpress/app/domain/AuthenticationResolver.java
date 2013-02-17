package com.wixpress.app.domain;

import org.apache.commons.codec.binary.Base64;
import org.codehaus.jackson.map.ObjectMapper;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * Created by : doron
 * Since: 7/1/12
 */

public class AuthenticationResolver
{
    private static final String SIGN_ALGORITHM = "HMACSHA256";
    private final ObjectMapper objectMapper;
    private final Base64 base64 = new Base64(256, new byte[0], true);

    public AuthenticationResolver(ObjectMapper objectMapper)
    {
        this.objectMapper = objectMapper;
    }

    public String signInstance(String secret, WixSignedInstance wixSignedInstance)
    {
        SignedParameterGenerator parameterGenerator = new SignedParameterGenerator(secret);

        return parameterGenerator.marshal(wixSignedInstance);
    }

    public WixSignedInstance unsignInstance(String secret, String signedParameter)
    {
        SignedParameterGenerator parameterGenerator = new SignedParameterGenerator(secret);

        return parameterGenerator.unmarshal(signedParameter, WixSignedInstance.class);
    }

    private class SignedParameterGenerator
    {
        private final Mac mac;

        private SignedParameterGenerator(String secret)
        {
            try {
                SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes(), SIGN_ALGORITHM);

                mac = Mac.getInstance(SIGN_ALGORITHM);
                mac.init(secretKeySpec);

            } catch (NoSuchAlgorithmException e)
            {
                throw new AuthenticationResolverMarshallerException("failed creating SignedParameterMarshaller : [" + e.getMessage() + "]");
            } catch (InvalidKeyException e)
            {
                throw new AuthenticationResolverMarshallerException("failed creating SignedParameterMarshaller due to : [" + e.getMessage() + "]");
            }
        }

        private String marshal(Object payload)
        {
            try
            {
                String payloadAsJson = objectMapper.writeValueAsString(payload);
                byte[] payloadEncodedBase64 = base64.encode(payloadAsJson.getBytes());

                byte[] sig = base64.encode(mac.doFinal(payloadEncodedBase64));

                return String.format("%s.%s", new String(sig), new String(payloadEncodedBase64));

            } catch (IOException e) {
                throw new AuthenticationResolverMarshallerException("failed writing payload [" + payload + "] as json");
            }
        }

        private <T> T unmarshal(String value, Class<T> valueClass)
        {
            int idx = value.indexOf(".");
            byte[] sig = base64.decode(value.substring(0, idx).getBytes());

            String rawPayload = value.substring(idx+1);

            String payload = new String(base64.decode(rawPayload));

            try {
                byte[] mySig = mac.doFinal(rawPayload.getBytes());

                if (!Arrays.equals(mySig, sig))
                {
                    throw new InvalidSignatureException("Request signed-instance signature invalid. Are you using the right secret?");
                } else
                {
                    return objectMapper.readValue(payload, valueClass);
                }
            } catch (IOException e)
            {
                throw new AuthenticationResolverMarshallerException("failed writing payload [" + payload + "] as json");
            }
        }
    }
}
