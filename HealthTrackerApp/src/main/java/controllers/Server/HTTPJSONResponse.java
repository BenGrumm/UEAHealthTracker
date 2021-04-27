package controllers.Server;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

public class HTTPJSONResponse {

    private int responseCode;
    private Map<String, List<String>> headers;
    private JSONObject body;

    public HTTPJSONResponse(int responseCode, Map<String, List<String>> headers, JSONObject body) {
        this.responseCode = responseCode;
        this.headers = headers;
        this.body = body;
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

    public JSONObject getBody() {
        return body;
    }

    public void setBody(JSONObject body) {
        this.body = body;
    }
}
