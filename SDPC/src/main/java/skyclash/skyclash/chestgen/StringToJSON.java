package skyclash.skyclash.chestgen;

import org.json.simple.JSONArray;

public class StringToJSON {
    @SuppressWarnings("unchecked")
    public static JSONArray convert(String str) {
        str = str.replaceAll("]]","]");
        str = str.replaceAll("\\[\\[","[");
        str = str.replaceAll("],", "].");
        String[] strarray = str.split("\\.");
        JSONArray strjson = new JSONArray();
        for (String a: strarray) {
            a = a.replaceAll("\\[", "");
            a = a.replaceAll("]", "");
            String[] coords = a.split(",");
            JSONArray coordjson = new JSONArray();
            for (String b: coords) {
                if (!b.isEmpty()) {
                    coordjson.add(Integer.valueOf(b));
                }
            }
            if (!coordjson.isEmpty()) {
                strjson.add(coordjson);
            }
        }
        return strjson;
    }
}
