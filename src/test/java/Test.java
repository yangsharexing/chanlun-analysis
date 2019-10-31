import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Test {
	public static void main(String[] args) {

		Map<String, String> map = new HashMap<String, String>();
		map.put("234", "1111");
//		map.remove("234");
		map.put("234", "222");
		System.out.println(map);

	}

}
