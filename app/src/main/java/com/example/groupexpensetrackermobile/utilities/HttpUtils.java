package com.example.groupexpensetrackermobile.utilities;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class HttpUtils {
    private static final HttpUtils INSTANCE = new HttpUtils();

    private HttpUtils() {
    }

    public static HttpUtils getInstance() {
        return INSTANCE;
    }


    public JsonObjectRequest getCustomJsonObjectRequest(int method, String url, JSONObject postData,
                                                        Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener, String token) {
        return new JsonObjectRequest(method, url, postData, responseListener, errorListener) {
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                try {
                    String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));

                    JSONObject result = null;

                    if (jsonString != null && jsonString.length() > 0)
                        result = new JSONObject(jsonString);

                    return Response.success(result,
                            HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                } catch (JSONException je) {
                    return Response.error(new ParseError(je));
                }
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                if(token != null) {
                    headers.put("Authorization", "Bearer " + token);
                }
                return headers;
            }
        };
    }

    public JsonArrayRequest getCustomJsonArrayRequest(int method, String url, JSONArray postData,
                                                      Response.Listener<JSONArray> responseListener, Response.ErrorListener errorListener, String token) {
        return new JsonArrayRequest(method, url, postData, responseListener, errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                if(token != null) {
                    headers.put("Authorization", "Bearer " + token);
                }
                return headers;
            }
        };
    }

}
