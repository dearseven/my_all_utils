
import android.content.Context;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.module.AppGlideModule;

import java.io.InputStream;

/**
 * 其实我真的不知道Glide4这么做的意思是什么
 * Created by wx on 2017/7/20.
 */
@GlideModule
public class MyAppGlideModule extends AppGlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        //重新设置内存限制,这里10M
        builder.setMemoryCache(new LruResourceCache(10*1024*1024));
    }
    /**
     * 为App注册一个自定义的String类型的BaseGlideUrlLoader
     *
     * @param context
     * @param registry
     */
    @Override
    public void registerComponents(Context context, Registry registry) {
        registry.append(String.class, InputStream.class, CustomBaseGlideUrlLoader.factory);
    }
    /**
     * 关闭扫描AndroidManifests.xml
     * @return
     */
    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }
}
