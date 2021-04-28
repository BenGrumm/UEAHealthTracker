package controllers.Server;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

public class HTTPResponse {

    private int responseCode;
    private Map<String, List<String>> headers;

    public HTTPResponse(int responseCode, Map<String, List<String>> headers) {
        this.responseCode = responseCode;
        this.headers = headers;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public Map<String, List<String>> getHeader() {
        return headers;
    }

    public void setHeader(Map<String, List<String>> header) {
        this.headers = header;
    }

}
