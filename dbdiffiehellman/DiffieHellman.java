package davidbenko.diffiehellmantestandroid.dbdiffiehellman;

import android.util.Base64;
import android.util.Log;

import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PublicKey;

import javax.crypto.KeyAgreement;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;

/**
 * Created by davidbenko on 2/5/15.
 */

public class DiffieHellman {

    final String KEY_DERIVATION_ALGORITHM = "PBKDF2WithHmacSHA1";
    final int KEY_DERIVATION_ROUNDS = 20000;
    final int KEY_LENGTH = 256;
    final int SALT_LENGTH = 32;
    final int KEY_PAIR_BIT_SIZE = 256;
    final String KEY_FACTORY_TYPE = "EC";
    final String KEY_AGREEMENT_TYPE = "ECDH";

    private DiffieHellmanProvider _provider;

    private DiffieHellman(){}

    public DiffieHellman(DiffieHellmanProvider provider){
        setProvider(provider);
    }

    public void setProvider(DiffieHellmanProvider provider){
        _provider = provider;
    }

    /*
     * Key Exchange
     */

    public void keyExchange(final DHCallback callback) {
        try {
            final KeyPair ourKeys = generateEcKeys();
            HttpHost host = _provider.keyExchangeHost();
            HttpRequest request = _provider.keyExchangeRequest(DHUtils.pubkeyToString(ourKeys.getPublic()));
            DHRequest dhRequest = new DHRequest(host,request, new DHRequestCallback() {
                @Override
                public void onComplete(HttpResponse response) {
                    try {
                        PublicKey theirKey = DHUtils.stringToPubkey(_provider.parseKeyExchangeResponse(response), KEY_FACTORY_TYPE);
                        byte[] salt = DHUtils.generateSalt(SALT_LENGTH);
                        String secret = deriveSecretFromKeys(ourKeys, theirKey);
                        String key = deriveKeyFromSecret(secret, salt);
                        String saltString = Base64.encodeToString(salt,Base64.NO_WRAP);
                        callback.onComplete(key,saltString);
                    }
                    catch (GeneralSecurityException e){
                        _provider.handleError(e);
                    }
                }
            });
            new DHKeyExchangeTask().execute(dhRequest);
        }
        catch(GeneralSecurityException e){
            _provider.handleError(e);
        }
    }

    /*
     * Generate Keys
     */

    private KeyPair generateEcKeys() throws GeneralSecurityException{
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance(KEY_FACTORY_TYPE);
        keyGen.initialize(KEY_PAIR_BIT_SIZE);
        return keyGen.generateKeyPair();
    }

    /*
     * Derive Keys
     */

    private String deriveSecretFromKeys(KeyPair ourKeys, PublicKey theirKey) throws GeneralSecurityException{
        KeyAgreement keyAgreement = KeyAgreement.getInstance(KEY_AGREEMENT_TYPE);
        keyAgreement.init(ourKeys.getPrivate());
        keyAgreement.doPhase(theirKey, true);
        return DHUtils.bytesToHex(keyAgreement.generateSecret());
    }

    private String deriveKeyFromSecret(String key, byte[] salt) throws GeneralSecurityException{
        PBEKeySpec spec = new PBEKeySpec(key.toCharArray(), salt, KEY_DERIVATION_ROUNDS, KEY_LENGTH);
        SecretKeyFactory skf = SecretKeyFactory.getInstance(KEY_DERIVATION_ALGORITHM);
        return Base64.encodeToString(skf.generateSecret(spec).getEncoded(), Base64.NO_WRAP);
    }
}
