import org.json.JSONException;
import org.json.JSONObject;

/**
 * 封装一下android常用的那个json
 */
public class CSON {
    private JSONObject js = null;
	private String rawJsonStr=null;

    public String getRawJsonStr() {
        return rawJsonStr;
    }

    public CSON(String jsonStr) {
        try {
             rawJsonStr=jsonStr;
			js = new JSONObject(jsonStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public CSON(JSONObject obj) {
        try {
            js = obj;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public <T> T getObjectType(String k) {
        try {
            return (T) js.get(k);
        } catch (Exception e) {
            return null;
        }
    }

//    public <T> T getObjectType(String k, Class<T> clazz) {
//        try {
//            return (T) js.get(k);
//        } catch (Exception e) {
//            return null;
//        }
//    }

    public <T> T getSpecificType(String k, Class<T> clazz) {
        try {
            Object o = js.get(k);
            return clazz.cast(o);
        } catch (Exception e) {
            return null;
        }
    }

    public int getInt(String k) {
        try {
            return js.getInt(k);
        } catch (Exception e) {
            return Integer.MIN_VALUE;
        }
    }

    public Double getDouble(String k) {
        try {
            return js.getDouble(k);
        } catch (Exception e) {
            return Double.MIN_VALUE;
        }
    }

    public Long getLong(String k) {
        try {
            return js.getLong(k);
        } catch (Exception e) {
            return Long.MIN_VALUE;
        }
    }

    public Boolean getBool(String k) {
        try {
            return js.getBoolean(k);
        } catch (Exception e) {
            return false;
        }
    }


//    public String getString(String k) {
//        try {
//            return js.getString(k);
//        } catch (Exception e) {
//            return null;
//        }
//    }


}