package util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.servlet.ServletInputStream;
import java.lang.reflect.Type;
import java.util.Map;

public class Convert {
    public static String toString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    public static String[] toParamValues(ServletInputStream inputStream, String[] paramKeys) {
        String parameterString = Convert.toString(inputStream); //1 string, json
        Type mapType = new TypeToken<Map<String, String>>() {
        }.getType();
        Gson gson = new Gson();
        Map<String, String> jsonMap = gson.fromJson(parameterString, mapType);

        if (jsonMap == null) {
            return null;
        }
        String[] paramValues = new String[paramKeys.length];
        for (int i = 0; i < paramKeys.length; i++) {
            String paramKey = paramKeys[i];
            if (jsonMap.containsKey(paramKey)) {
                paramValues[i] = jsonMap.get(paramKey);
            }
        }
        return paramValues;
    }


}
