package fr.k2i.adbeback.webapp.util;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * User: dimitri
 * Date: 21/01/15
 * Time: 17:37
 * Goal:
 */
public class YamlUtils {


    public static Map<String,String> yamlToMap(InputStream in) {
        return yamlToMap( (Map<String,Object>) new Yaml().load(in),null);
    }

    private static Map<String,String> yamlToMap(Map<String,Object> in,String str) {
        Map<String,String> res = new HashMap<>();


        for (Map.Entry<String, Object> entry : in.entrySet()) {
            if (entry.getValue() instanceof HashMap) {
                HashMap value = (HashMap) entry.getValue();
                if(str==null){
                    res.putAll(yamlToMap(value, (String) entry.getKey()));
                }else{
                    res.putAll(yamlToMap(value, str+"."+(String) entry.getKey()));
                }
            }else if (entry.getValue() instanceof String){
                res.put(str+"."+entry.getKey(), (String) entry.getValue());
            }
        }

        return res;
    }
}
