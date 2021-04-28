package controllers.Server;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

public class HTTPJSONResponse extends HTTPResponse{

    private JSONObject body;

    public HTTPJSONResponse(int responseCode, Map<String, List<String>> headers, JSONObject body) {
        super(responseCode, headers);
        this.body = body;
    }

    public JSONObject getBody() {
        return body;
    }

    public void setBody(JSONObject body) {
        this.body = body;
    }
}
