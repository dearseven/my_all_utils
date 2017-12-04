
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 语言切换工具
 */
public class LanguageUtils {

    /**
     * 手动修改适配的语种（不按照系统语言来适配），要记得重启应用
     * 其实如果不是要手动修改语言配置，用跟随系统的话，android本身的国际化就完美适配了
     *
     * @param ctx
     */
    public static void changeStringLang(Context ctx) {
        if (langs == null) {
            initLangs();
        }
        //DLog.log(LanguageUtils.class, ",当前系统类型：" + getLocalString(getCurrentLocale(ctx), false));
        //首先赋值一个默认类型
        Locale locale = DEFAULT_LOCALE;
        for (_Language lang : langs) {
            if (getLocalString(lang.locale, false).equals(getLocalString(getCurrentLocale(ctx), false))) {
                //如果这个适配列表里有当前系统的语言，则不处理
                locale = lang.locale;
                break;
            }
        }
        //ctx
        Resources resources = ctx.getResources();
        Configuration config = resources.getConfiguration();

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.locale = locale;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(locale);
        }
//        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            configuration.setLocales(null);
//        }

        Locale.setDefault(locale);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocale(locale);
        } else {
            config.locale = locale;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            ctx = ctx.createConfigurationContext(config);
        } else {
            ctx.getResources().updateConfiguration(config, ctx.getResources().getDisplayMetrics());
        }
        //!!!!要重启应用
    }

    /**
     * 修改APP配置里的语言信息
     *
     * @param ctx
     */
    public static void updateConfigsLangType(Context ctx) {
        if (langs == null) {
            initLangs();
        }
        _Language _lan = null;
        //首先把默认的语言赋值给_lan
        for (_Language lang : langs) {
            if (getLocalString(DEFAULT_LOCALE, false).equals(getLocalString(lang.locale, false))) {
                _lan = lang;
            }
        }
        //如果当前系统的语言在配置好的列表中，则修改_lan,不然就用上面的默认值
        Locale sysLocale = getCurrentLocale(ctx);
        for (_Language lang : langs) {
            if (getLocalString(sysLocale, false).equals(getLocalString(lang.locale, false))) {
                //如果这个适配列表里有当前系统的语言，则不处理
                _lan = lang;
                break;
            }
        }
        Configs.LANGUAGE_TYPE = _lan.id;
        DLog.log(LanguageUtils.class, "APP LANGTYPE=" + Configs.LANGUAGE_TYPE);
    }


    /**
     * 默认是英文
     */
    private static final Locale DEFAULT_LOCALE = Locale.ENGLISH;

    /**
     * 保存已经适配的语言种类
     */
    private static List<_Language> langs = null;

    private static void initLangs() {
        langs = new ArrayList<_Language>();
        //langs.add(new _Language(1, "跟随系统", Locale.getDefault()));
        // langs.add(new _Language(2, "简体中文", Locale.SIMPLIFIED_CHINESE));
        // langs.add(new _Language(3, "英语", Locale.ENGLISH));

        langs.add(new _Language(1, "简体中文", Locale.SIMPLIFIED_CHINESE));
        langs.add(new _Language(2, "英语", Locale.ENGLISH));
    }

    private static class _Language {
        public int id;
        public String name;
        public Locale locale;

        public _Language(int i, String name, Locale locale) {
            this.id = i;
            this.name = name;
            this.locale = locale;
        }

        @Override
        public String toString() {
            return "_Language{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", locale=" + locale +
                    '}';
        }
    }


    /**
     * 获取当前的系统locale
     *
     * @param ctx
     * @return
     */
    private static Locale getCurrentLocale(Context ctx) {
        Locale locale = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = ctx.getResources().getConfiguration().getLocales().get(0);
        } else {
            locale = ctx.getResources().getConfiguration().locale;
        }
        //或者仅仅使用 locale = Locale.getDefault(); 不需要考虑接口 deprecated(弃用)问题
        return locale;
    }

    /**
     * 通过local获取系统语言名,其实按道理要带国家和地区信息的比如中文就有zh-rCN，zh-HK之类的区分,但是如果不做的那么精细也可以不带
     *
     * @param locale
     * @param withCountry，带不带国家或地区信息
     * @return
     */
    private static String getLocalString(Locale locale, boolean withCountry) {
        String lang = null;
        if (withCountry)
            lang = locale.getLanguage() + "-" + locale.getCountry();
        else
            lang = locale.getLanguage();
        return lang;
    }
}
