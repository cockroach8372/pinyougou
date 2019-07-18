import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Demo {
    public static void main(String[] args) {
        ArrayList<Map> list = new ArrayList<>();
        Map map=new HashMap();
        map.put("name","zhangsan");
        map.put("age",13);
        map.put("names","lisi");
        map.put("ages",14);
        list.add(map);
        System.out.println(map);
        System.out.println(list);
    }
}
