package wc.c.libbase.okhttp;

public class HeadItem {
    public static int WRITE_TO_HEAD=0;
    public static int WRITE_TO_COOKIE=1;
    public static int WRITE_TO_BOTTH=2;

    public int writeTo=WRITE_TO_HEAD;
    public String key;
    public String value;

    public HeadItem(int writeTo,String key,String value){
        this.writeTo=writeTo;
        this.key=key;
        this.value=value;
    }
}
