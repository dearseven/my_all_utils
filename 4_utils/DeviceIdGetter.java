
import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

/**
 * 获取安卓的设备号<br/>
 * 1 DEVICE_ID
 * 这是Android系统为开发者提供的用于标识手机设备的串号，也是各种方法中普适性较高的，可以说几乎所有的设备都可以返回这个串号，并且唯一性良好。
 * 获取DEVICE_ID需要READ_PHONE_STATE权限<br/>
 * <p>
 * 2 Sim Serial Number
 * 装有SIM卡的设备可以获得，对于CDMA设备，返回的是一个空值<br></>
 * <p>
 * 3 ANDROID_ID
 * 在设备首次启动时，系统会随机生成一个64位的数字，并把这个数字以16进制字符串的形式保存下来，这个16进制的字符串就是ANDROID_ID，当设备被wipe后该值会被重置
 * <br></>
 * <p>
 * 4 Serial Number
 * <p>
 * Created by wx on 2017/7/10.
 */
public class DeviceIdGetter {
    protected static final String PREFS_FILE = "_device_id.xml";
    protected static final String PREFS_DEVICE_ID = "_device_id";

    protected static UUID uuid;

    public DeviceIdGetter(Context context) {
        if (uuid == null) {
            synchronized (DeviceIdGetter.class) {
                if (uuid == null) {
                    final SharedPreferences prefs = context.getSharedPreferences(PREFS_FILE, 0);
                    final String id = prefs.getString(PREFS_DEVICE_ID, null);
                    if (id != null) {
                        uuid = UUID.fromString(id);
                    } else {
                        final String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
                        try {
                            if (!"9774d56d682e549c".equals(androidId)) {
                                uuid = UUID.nameUUIDFromBytes(androidId.getBytes("utf8"));
                            } else {
                                final String deviceId = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
                                uuid = deviceId != null ? UUID.nameUUIDFromBytes(deviceId.getBytes("utf8")) : UUID.randomUUID();
                            }
                        } catch (UnsupportedEncodingException e) {
                            throw new RuntimeException(e);
                        }
                        prefs.edit().putString(PREFS_DEVICE_ID, uuid.toString()).commit();
                    }
                }
            }
        }
    }


    public UUID getDeviceUuid() {
        return uuid;
    }
}
