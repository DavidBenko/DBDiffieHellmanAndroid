package davidbenko.diffiehellmantestandroid.dbdiffiehellman;

import org.apache.http.HttpResponse;

/**
 * Created by davidbenko on 2/5/15.
 */
interface DHRequestCallback{
    void onComplete(HttpResponse response);
}
