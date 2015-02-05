package davidbenko.diffiehellmantestandroid.dbdiffiehellman;

import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;

import java.security.GeneralSecurityException;

/**
 * Created by davidbenko on 2/5/15.
 */
public interface DiffieHellmanProvider {
    public HttpHost keyExchangeHost();
    public HttpRequest keyExchangeRequest(String ourPublicKey);
    public String parseKeyExchangeResponse(HttpResponse response);
    public void handleError(GeneralSecurityException e);
}
