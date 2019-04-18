在android系统中的配置文件可以通过adb shell getprop 来获取属性之
假设有一个属性 ro.boot.serialno=112233
那么我们可以读取这个属性，我尝试了很多方式
    String stbid = "";
        try {
            stbid = Settings.System.getString(getContentResolver(), "ro.boot.serialno");
            Debug.log4LX(getClass(), "stbid 1", "stbid=" + stbid);
        } catch (Exception ex) {
            Debug.log4LX(getClass(), "stbid 1", "end exception");
        }
        try {

            stbid = Settings.Secure.getString(getContentResolver(), "ro.boot.serialno");
            Debug.log4LX(getClass(), "stbid 2", "stbid=" + stbid);
        } catch (Exception ex) {
            Debug.log4LX(getClass(), "stbid 2", "end exception");
        }

        try {
            stbid =SystemPropertiesProxy.get(this,"ro.boot.serialno");
            Debug.log4LX(getClass(), "stbid 3", "stbid=" + stbid);
        } catch (Exception ex) {
            Debug.log4LX(getClass(), "stbid 3", "end exception");
        }

        try {
            stbid = SystemProperties.get("ro.boot.serialno");
                Debug.log4LX(getClass(), "stbid 4", "stbid=" + stbid);
        } catch (Exception ex) {
            Debug.log4LX(getClass(), "stbid 4", "end exception");
        }

        try {
            stbid = System.getProperty("ro.boot.serialno");
            Debug.log4LX(getClass(), "stbid 5", "stbid=" + stbid);
        } catch (Exception ex) {
            Debug.log4LX(getClass(), "stbid 5", "end exception");
        }

        try {
            String mac = System.getProperty("ro.mac");
            Debug.log4LX(getClass(), "mac 1", "mac=" + mac);
        } catch (Exception ex) {
            Debug.log4LX(getClass(), "mac 1", "end exception");
        }

        //--- 和上面的方法是一样的，主要是key去掉了 boot
        try {
            stbid = Settings.System.getString(getContentResolver(), "ro.serialno");
            Debug.log4LX(getClass(), "stbid 11", "stbid=" + stbid);
        } catch (Exception ex) {
            Debug.log4LX(getClass(), "stbid 11", "end exception");
        }
        try {

            stbid = Settings.Secure.getString(getContentResolver(), "ro.serialno");
            Debug.log4LX(getClass(), "stbid 12", "stbid=" + stbid);
        } catch (Exception ex) {
            Debug.log4LX(getClass(), "stbid 12", "end exception");
        }

        try {
            stbid =SystemPropertiesProxy.get(this,"ro.serialno");
            Debug.log4LX(getClass(), "stbid 13", "stbid=" + stbid);
        } catch (Exception ex) {
            Debug.log4LX(getClass(), "stbid 13", "end exception");
        }

        try {
            stbid = SystemProperties.get("ro.serialno");
            Debug.log4LX(getClass(), "stbid 14", "stbid=" + stbid);
        } catch (Exception ex) {
            Debug.log4LX(getClass(), "stbid 14", "end exception");
        }

        try {
            stbid = System.getProperty("ro.serialno");
            Debug.log4LX(getClass(), "stbid 15", "stbid=" + stbid);
        } catch (Exception ex) {
            Debug.log4LX(getClass(), "stbid 15", "end exception");
        }
		发现stbid 3 stbid 4 stbid 13 stbid 14，这几组是有效的能获取数据的。
		他们分别用了2个工具类，也就是说以后可以用这个两个类来获取，而且一起用，这样更安全啊
