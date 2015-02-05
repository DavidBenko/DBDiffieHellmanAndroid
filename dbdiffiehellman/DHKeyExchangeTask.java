package davidbenko.diffiehellmantestandroid.dbdiffiehellman;

import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * Created by davidbenko on 2/5/15.
 */
class DHKeyExchangeTask extends AsyncTask<DHRequest, Void, HttpResponse> {

    @Override
    protected HttpResponse doInBackground(DHRequest... requests) {
        if (requests.length > 0){
            try {
                DHRequest req = requests[0];
                HttpClient client = new DefaultHttpClient();
                HttpResponse response = client.execute(req.getHost(), req.getRequest());
                req.callback(response);
                return response;
            }
            catch(Exception e){
                e.printStackTrace();
                return null;
            }
        }
        else return null;
    }
}
