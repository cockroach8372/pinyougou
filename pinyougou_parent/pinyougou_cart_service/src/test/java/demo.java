import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class demo {

    @Test
    public void test1() throws Exception{
        Map map =new HashMap();
        map.put("123","null");
        System.out.println(map);
    }
}
