package util;

import java.io.IOException;
import java.util.Properties;

/**
 * @Author: zerocoder
 * @Description:
 * @Date: 2021/3/2 19:20
 */

public class PropertiesUtil {
    private static Properties properties;
    static{

        properties = new Properties();
        try {
            properties.load(PropertiesUtil.class.getClassLoader().getResourceAsStream(Constants.ZOOKEEPER_CONFIG));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getValue(String key){
        return (String) properties.get(key);
    }
}
