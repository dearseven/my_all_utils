
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ThumbnailUtils
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore.Images.Thumbnails.MINI_KIND
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import cc.m2u.intelliv.myutils.IDoSomeThing
import com.vrv.base.R
import com.vrv.base.utils.MyThreadPool

import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.lang.ref.WeakReference
import java.util.*
import kotlin.collections.HashSet

class CyanFileExplorerAdapter : RecyclerView.Adapter<CyanFileExplorerViewHolder> {
    var files: ArrayList<Map<String, Any>>
    var weakCtx: WeakReference<Context>
    var inflater: LayoutInflater

    var selectedSet: HashSet<Map<String, Any>>

    var folderClick: IFolderClick

    var pathStack: Stack<String>
    var rootPath: String

    var isOnlyDirectory = false
    var pickMaxCount = 0

    val myThreadPool: MyThreadPool by lazy {
        MyThreadPool.getNewThreadPoll()
    }

    val myCache: SimpleCacheMine<String, Bitmap> by lazy {
        SimpleCacheMine<String, Bitmap>(30);
    }

    companion object {
        val handler = object : Handler() {}
    }

    constructor(ctx: Context, _isOnlyDirectory: Boolean, _pickMaxCount: Int) {
        this.isOnlyDirectory = _isOnlyDirectory
        this.pickMaxCount = _pickMaxCount

        pathStack = Stack<String>()
        rootPath = Environment.getExternalStorageDirectory().absolutePath

        pathStack.push(rootPath)
        this.files = GetFilesUtils.getInstance().getSonNode(rootPath)
                ?: ArrayList<Map<String, Any>>()

        if (isOnlyDirectory) {
            this.files = this.files.filter { it[GetFilesUtils.FILE_INFO_ISFOLDER] as Boolean } as ArrayList<Map<String, Any>>
        }
//        this.files.sortedBy {
//            var path = it[GetFilesUtils.FILE_INFO_PATH]
//            when (path) {
//                is String -> {
//                    path as String
//                }
//                is File -> {
//                    var _path = path.absolutePath
//                    path=_path
//                }
//            }
//            (path as String).toCharArray()[0]
//        }
        this.files.sortBy {
            var name = it[GetFilesUtils.FILE_INFO_NAME] as String
            name.toCharArray()[0]
        }
        //ArrayList<Map<String, Any>>()
        weakCtx = WeakReference(ctx)
        weakCtx.get()?.also {
            it as CyanFileExplorerActivity
            it.titleTv.setText(rootPath)
        }
        inflater = LayoutInflater.from(weakCtx.get())
        selectedSet = HashSet<Map<String, Any>>()
        this.folderClick = object : IFolderClick {
            override fun openFolder(path: String) {
                //设置路径
                weakCtx.get()?.also {
                    it as CyanFileExplorerActivity
                    it.titleTv.setText(path)
                }

                var list = GetFilesUtils.getInstance().getSonNode(path)
                if (isOnlyDirectory) {
                    list = list.filter { it[GetFilesUtils.FILE_INFO_ISFOLDER] as Boolean } as ArrayList<Map<String, Any>>
                }
                list?.apply {
                    //如果这个目录可以进去，则把路径入栈
                    pathStack.push(path)

                    this as ArrayList<Map<String, Object>>

                    val previousSize = files.size
                    (files as ArrayList<Map<String, Object>>).clear()
                    notifyItemRangeRemoved(0, previousSize);

                    this.forEach { (files as ArrayList<Map<String, Object>>).add(it) }
                    files.sortBy {
                        var name = it[GetFilesUtils.FILE_INFO_NAME] as String
                        name.toCharArray()[0]
                    }
                    notifyItemRangeChanged(0, files.size)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CyanFileExplorerViewHolder {
        var v = inflater.inflate(R.layout.cyan_file_explorer_item_demo, parent, false)
        var vh = CyanFileExplorerViewHolder(v)
        return vh
    }

    //用户点击back可以通过这里拦截，当返回true时，表示可以直接activity的back操作了
    fun popAndCanBack(): Boolean {
        var item = pathStack.pop()//当前的路径先直接出栈
        if (item.equals(rootPath)) {
            return true;
        }
        //取父路径
        item = pathStack.pop()
        folderClick.openFolder(item)
        return false;
    }

    override fun getItemCount(): Int = files.size

    override fun onBindViewHolder(holder: CyanFileExplorerViewHolder, position: Int) {
        val item = files[position]
        var path = item[GetFilesUtils.FILE_INFO_PATH]
        when (path) {
            is String -> {
                path as String
            }
            is File -> {
                var _path = path.absolutePath
                // val mimetype = MimetypeUtils.instance.getMimeType(path)
            }
        }

        if (item[GetFilesUtils.FILE_INFO_ISFOLDER] as Boolean) {
            holder.tv1.setImageResource(R.drawable.raptor_ic_folder)
            holder.tv2.setTag(R.id.content_id_1, path)
            holder.tv2.setOnClickListener {
                selectedSet.clear()
                val activity = (weakCtx.get() as CyanFileExplorerActivity)
                activity.okBtn.text = "${activity.getString(R.string.select_ok)}"
                folderClick.openFolder(it.getTag(R.id.content_id_1) as String)
            }
            holder.tv3.visibility = View.GONE
        } else {
            //holder.tv1.text = ""
            path as File
            holder.tv1.setImageResource(MimetypeUtils.instance.fromMimeTypeGetIcon(MimetypeUtils.instance.getMimeType(path)))
            MimetypeUtils.instance.getMimeType(path).apply {
                if (this.contains("image")) {
                    myThreadPool.execute {
                        revitionImageSize(path.absolutePath, dpToPx(36f, weakCtx.get()!!), dpToPx(36f, weakCtx.get()!!), object : IDoSomeThing<Bitmap>() {
                            override fun doSomeThing(t: Bitmap?) {
                                t?.also {
                                    handler.post {
                                        holder.tv1.setImageBitmap(t)
                                    }

                                }
                            }
                        })
                    }
                } else if (this.contains("video")) {
                    myThreadPool.execute {
                        getVideoThumbnailUtils(path.absolutePath, dpToPx(36f, weakCtx.get()!!), dpToPx(36f, weakCtx.get()!!), object : IDoSomeThing<Bitmap>() {
                            override fun doSomeThing(t: Bitmap?) {
                                t?.also {
                                    handler.post {
                                        holder.tv1.setImageBitmap(t)
                                    }
                                }
                            }
                        })
                    }
                }
            }

            holder.tv2.setOnClickListener { }
            holder.tv3.visibility = View.VISIBLE
        }

        holder.tv2.text = item[GetFilesUtils.FILE_INFO_NAME] as String

        if (selectedSet.contains(item)) {
            holder.tv3.setImageResource(R.drawable.raptor_select_icon_yes)
        } else {
            holder.tv3.setImageResource(R.drawable.raptor_select_icon_no)
        }
        //holder.tv3.setTag(R.id.content_id_1, path)
        holder.tv3.setTag(R.id.content_id_2, position)
        holder.tv3.setTag(R.id.content_id_3, item)
        holder.tv3.setOnClickListener {
            val index = it.getTag(R.id.content_id_2) as Int
            //val path = it.getTag(R.id.content_id_1) as String
            val item = it.getTag(R.id.content_id_3) as Map<String, Any>

            if (pickMaxCount == 1) {
                if (selectedSet.isEmpty()) {
                    addSelect(item)
                } else {
                    //要找到移除的是谁
                    var needRemoveId = -1
                    selectedSet.forEach {
                        for (i in 0..files.size - 1) {
                            if (files[i][GetFilesUtils.FILE_INFO_NAME]!!.equals(it[GetFilesUtils.FILE_INFO_NAME])) {
                                needRemoveId = i
                                break
                            }
                        }
                    }
                    clearSelected()
                    //修改item
                    if (needRemoveId != -1) {
                        notifyItemChanged(needRemoveId)
                    }
                    addSelect(item)
                }
            } else {
                val size = selectedSet.size
                if (size >= pickMaxCount && !selectedSet.contains(item)) {
                    Toast.makeText(weakCtx.get(), "${weakCtx.get()?.getString(R.string.max_can_pick)}$pickMaxCount", Toast.LENGTH_SHORT).show()
                } else {
                    if (selectedSet.contains(item)) {
                        removeSelected(item)
                    } else {
                        addSelect(item)
                    }
                }
            }
            notifyItemChanged(index)
        }
    }

    private fun clearSelected() {
        selectedSet.clear()
        val activity = (weakCtx.get() as CyanFileExplorerActivity)
        activity.okBtn.setBackgroundResource(R.drawable.cyan_file_explorer_ok_btn_normal)
        activity.okBtn.text = "(${selectedSet.size}) ${activity.getString(R.string.select_ok)}"
        activity.okBtn.setOnClickListener { }
    }

    private fun removeSelected(item: Map<String, Any>) {
        val activity = (weakCtx.get() as CyanFileExplorerActivity)
        selectedSet.remove(item)
        if (selectedSet.isEmpty()) {
            activity.okBtn.setBackgroundResource(R.drawable.cyan_file_explorer_ok_btn_normal)
            activity.okBtn.setOnClickListener { }
        } else {
            activity.okBtn.setBackgroundResource(R.drawable.cyan_file_explorer_ok_btn_selected)
            addOkClickEvent()
        }
        activity.okBtn.text = "(${selectedSet.size}) ${activity.getString(R.string.select_ok)}"
    }


    private fun addSelect(item: Map<String, Any>) {
        val activity = (weakCtx.get() as CyanFileExplorerActivity)
        selectedSet.add(item)
        activity.okBtn.setBackgroundResource(R.drawable.cyan_file_explorer_ok_btn_selected)
        activity.okBtn.text = "(${selectedSet.size}) ${activity.getString(R.string.select_ok)}"
        addOkClickEvent()
    }

    private fun addOkClickEvent() {
        val activity = (weakCtx.get() as CyanFileExplorerActivity)
        activity.okBtn.setOnClickListener {
            activity.setResult(Activity.RESULT_OK, Intent().apply {
                this.putExtras(Bundle().also {
                    val list = ArrayList<Map<String, Any>>(selectedSet.size + 1)
                    selectedSet.forEach { i ->
                        list.add(i)
                    }
                    it.putSerializable(CyanFileExplorerActivity.DATA_KEY, list)
                })
            })
            activity.finish()
        }
    }

    //暂时没用上
    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }


    /**
     * 根据指定的图像路径和大小来获取图片缩略图
     *
     * @param path      图像的路径
     * @param maxWidth  指定输出图像的宽度
     * @param maxHeight 指定输出图像的高度
     * @return 生成的缩略图
     */
    @Throws(IOException::class)
    fun revitionImageSize(path: String?, maxWidth: Int, maxHeight: Int, dst: IDoSomeThing<Bitmap>) {
        if (myCache.get(path!!) != null) {
            dst.doSomeThing(myCache.get(path!!))
        } else {
            var bitmap: Bitmap? = null
            try {
                var `in` = BufferedInputStream(FileInputStream(
                        File(path)))
                val options: BitmapFactory.Options = BitmapFactory.Options()
                options.inJustDecodeBounds = true
                BitmapFactory.decodeStream(`in`, null, options)
                `in`.close()
                var i = 0
                while (true) {
                    if (options.outWidth shr i <= maxWidth
                            && options.outHeight shr i <= maxHeight) {
                        `in` = BufferedInputStream(
                                FileInputStream(File(path)))
                        options.inSampleSize = Math.pow(2.0, i.toDouble()).toInt()
                        options.inJustDecodeBounds = false
                        bitmap = BitmapFactory.decodeStream(`in`, null, options)
                        break
                    }
                    i += 1
                }
            } catch (e: Exception) {
                dst.doSomeThing(null)
            }
            myCache.put(path!!, bitmap!!)
            dst.doSomeThing(bitmap)
        }
    }


    /**
     * 根据指定的图像路径和大小来获取图片缩略图
     *
     * @param path      图像的路径
     * @param maxWidth  指定输出图像的宽度
     * @param maxHeight 指定输出图像的高度
     * @return 生成的缩略图
     */
    @Throws(IOException::class)
    fun getVideoThumbnailUtils(path: String?, maxWidth: Int, maxHeight: Int, dst: IDoSomeThing<Bitmap>) {
        if (myCache.get(path!!) != null) {
            dst.doSomeThing(myCache.get(path!!))
        } else {
            var bitmap: Bitmap? = null
            try {
                bitmap = ThumbnailUtils.createVideoThumbnail(path, MINI_KIND)
            } catch (e: Exception) {
                dst.doSomeThing(null)
            }
            myCache.put(path!!, bitmap!!)
            dst.doSomeThing(bitmap)
        }
    }

    fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics).toInt()
    }


}