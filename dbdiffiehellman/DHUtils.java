package davidbenko.diffiehellmantestandroid.dbdiffiehellman;

import android.util.Base64;

import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.X509EncodedKeySpec;
import java.util.Random;

/**
 * Created by davidbenko on 2/5/15.
 */
final class DHUtils {

    final protected static char[] HEX_ARRAY = "0123456789abcdef".toCharArray();

    private DHUtils(){}

    static PublicKey stringToPubkey(String str, String keyType) throws GeneralSecurityException {
        String temp = str.replace("-----BEGIN PUBLIC KEY-----\n", "").replace("\n-----END PUBLIC KEY-----", "");
        byte[] decodedBytes = Base64.decode(temp, 0);
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(decodedBytes);
        KeyFactory factory = KeyFactory.getInstance(keyType);
        PublicKey publicKey = factory.generatePublic(publicKeySpec);
        return publicKey;
    }

    static String pubkeyToString(PublicKey pubKey){
        byte[] bytes = pubKey.getEncoded();
        String pubKeyData = "-----BEGIN PUBLIC KEY-----\n" + Base64.encodeToString(bytes,0) + "-----END PUBLIC KEY-----";
        return pubKeyData;
    }

    static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    static byte[] generateSalt(int length){
        final Random r = new SecureRandom();
        byte[] salt = new byte[length];
        r.nextBytes(salt);
        return salt;
    }
}
