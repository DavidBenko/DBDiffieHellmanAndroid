# DBDiffieHellmanAndroid
Elliptic Curve Diffie Hellman Key Exchange over HTTP on Android

## Example Use
```java
public class MainActivity extends ActionBarActivity implements DiffieHellmanProvider {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        /*
         * Do ECDH Exchange Here
         */

        DiffieHellman dh = new DiffieHellman(this); // Create DiffieHellman, passing in a provider
        dh.keyExchange(new DHCallback() {
            @Override
            public void onComplete(String key, String salt) throws GeneralSecurityException {
                // Exchange is complete - do something with key and salt
                // Server can compute the same key using this salt and same number of rounds
                // Default is PBKDF2, 20000 rounds, using Hmac SHA-1
                
                Log.d("DH", "Final Key: " + key + "\nSalt: "+salt);
            }
        });
    }
    
    /*
     * Conform to DiffieHellmanProvider interface
     */

    public HttpHost keyExchangeHost(){
        return new HttpHost("http://sample.com",8080);
    }
    public HttpRequest keyExchangeRequest(String ourPublicKey){
    
        // Example request, replace with your implementation
    
        try {
            HttpPost request = new HttpPost();
            request.setURI(new URI("http://sample.com:8080"));
            request.addHeader("Content-Type", "application/json");
            request.addHeader("Accept", "application/json");
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("public_key", ourPublicKey);
            String payload = new JSONObject(map).toString();
            StringEntity params = new StringEntity(payload);
            request.setEntity(params);
            return request;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    public String parseKeyExchangeResponse(HttpResponse response){
        
        // Example response parsing, replace with your implementation
        
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            StringBuilder builder = new StringBuilder();
            for (String line = null; (line = reader.readLine()) != null; ) {
                builder.append(line).append("\n");
            }
            JSONTokener tokener = new JSONTokener(builder.toString());
            JSONObject finalResult = new JSONObject(tokener);
            String key = (String)finalResult.get("public_key");
            return key;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    public void handleError(GeneralSecurityException e){
        // Handle errors
        e.printStackTrace();
    }
}
```
