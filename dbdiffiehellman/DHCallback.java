package davidbenko.diffiehellmantestandroid.dbdiffiehellman;

import java.security.GeneralSecurityException;

/**
 * Created by davidbenko on 2/5/15.
 */
public interface DHCallback {
    void onComplete(String key, String salt) throws GeneralSecurityException;
}
