
import java.util.HashMap;
import java.util.Iterator;


public class TestRun {
	public static void main(String[] args) {
		String test = "testString";
		//简单运算，返回的是T，这里直接返回的是 testString__
		String result = new RunMap<String, Integer>(test).runSelf(new RunMap.F1<String>() {
			@Override
			public String apply(String t) {
				return "testString__";
			}
		});
		System.out.println(result);

		//执行的方法是runIt，返回的是R，这里返回的实际值a=a1&b=b1
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("a", "a1");
		map.put("b", "b1");
		String param = new RunMap<HashMap<String, String>, String>(map)
				.runIt(new RunMap.F2<HashMap<String, String>, String>() {

					@Override
					public String apply(HashMap<String, String> t) {
						StringBuffer sb = new StringBuffer();
						Iterator<String> it = t.keySet().iterator();
						boolean isFirst = true;
						while (it.hasNext()) {
							String key = it.next();
							String value = t.get(key);
							if (!isFirst) {
								sb.append("&");
							} else {
								isFirst = false;
							}
							sb.append(key).append("=").append(value);
						}
						return sb.toString();
					}
				});
		System.out.println(param);

		//执行的方法是runItMap，这里返回的是
		//RunMap [_t={a=a1, b=b1, c=c1, d=d1}, _r=a=a1&b=b1&c=c1&d=d1]
		map.put("c", "c1");
		map.put("d", "d1");
		RunMap<HashMap<String, String>, String> rm = new RunMap<HashMap<String, String>, String>(map)
				.runItMap(new RunMap.F2<HashMap<String, String>, String>() {

					@Override
					public String apply(HashMap<String, String> t) {
						StringBuffer sb = new StringBuffer();
						Iterator<String> it = t.keySet().iterator();
						boolean isFirst = true;
						while (it.hasNext()) {
							String key = it.next();
							String value = t.get(key);
							if (!isFirst) {
								sb.append("&");
							} else {
								isFirst = false;
							}
							sb.append(key).append("=").append(value);
						}
						return sb.toString();
					}
				});
		System.out.println(rm);

		//执行的方法是，runT2R，这里返回的是
		//[_t=a=a1&b=b1&c=c1&d=d1&e=e1&f=f1, _r=null]
		//把结果R返回到一个新的RunMap中，并且变成T
		map.put("e", "e1");
		map.put("f", "f1");
		RunMap<HashMap<String, String>, String> rm1 = new RunMap<HashMap<String, String>, String>(map)
				.runT2R(new RunMap.F2<HashMap<String, String>, String>() {

					@Override
					public String apply(HashMap<String, String> t) {
						StringBuffer sb = new StringBuffer();
						Iterator<String> it = t.keySet().iterator();
						boolean isFirst = true;
						while (it.hasNext()) {
							String key = it.next();
							String value = t.get(key);
							if (!isFirst) {
								sb.append("&");
							} else {
								isFirst = false;
							}
							sb.append(key).append("=").append(value);
						}
						return sb.toString();
					}
				});
		System.out.println(rm1);
	}
}
