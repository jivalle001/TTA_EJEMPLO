package eus.ehu.ejemplo;

import android.util.Base64;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jose on 13/01/17.
 */
public class RestClient {
    private final static String AUTH = "Authorization";
    private final String baseUrl;
    private final Map<String,String> properties = new HashMap<>();

    public RestClient(String baseUrl) {
        this.baseUrl=baseUrl;
    }

    public void setHttpBasicAuth(String user, String passwd) {
        String basicAuth = Base64.encodeToString(String.format("%s:%s",user,passwd).getBytes(),Base64.DEFAULT);
        properties.put(AUTH,String.format("Basic %s",basicAuth));
    }

    public String getAuthorization(){
        return properties.get(AUTH);
    }

    public void setAuthorization(String auth){
        properties.put(AUTH,auth);
    }
}
