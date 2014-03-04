package org.celllife.ivr.application.utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class JsonUtils {

    public Map<String, String> extractJsonVariables(String s) throws JSONException {

        JSONObject jObject = new JSONObject(s);
        JSONObject menu = jObject.getJSONObject("response");

        Map<String, String> map = new HashMap<String, String>();
        Iterator iterator = menu.keys();
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            String value = menu.getString(key);
            map.put(key, value);
        }

        return map;

    }

}
