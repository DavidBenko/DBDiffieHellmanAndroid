package davidbenko.diffiehellmantestandroid.dbdiffiehellman;

import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;

/**
 * Created by davidbenko on 2/5/15.
 */
class DHRequest{

    private HttpRequest _request;
    private HttpHost _host;
    private DHRequestCallback _callback;

    public DHRequest(HttpHost host, HttpRequest request, DHRequestCallback callback){
        _host = host;
        _request = request;
        _callback = callback;
    }

    public HttpHost getHost(){
        return _host;
    }
    public HttpRequest getRequest(){
        return _request;
    }
    public void callback(HttpResponse response){
        _callback.onComplete(response);
    }
}
