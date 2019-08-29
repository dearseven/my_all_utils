import android.content.Context
import android.os.Environment
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.cyan.Dlog
import com.nextcloud.talk.R
import java.io.File
import java.lang.ref.WeakReference
import java.util.*
import kotlin.collections.HashSet

class CyanFileExplorerAdapter : RecyclerView.Adapter<CyanFileExplorerViewHolder> {
    lateinit var files: ArrayList<Map<String, Any>>
    lateinit var weakCtx: WeakReference<Context>
    lateinit var inflater: LayoutInflater

    lateinit var selectedSet: HashSet<Map<String, Any>>

    lateinit var folderClick: IFolderClick

    lateinit var pathStack: Stack<String>
    lateinit var rootPath: String

    var isOnlyDirectory = false
    var pickMaxCount = 0

//    constructor(files: List<Map<String, Any>>, ctx: Context) {
//    pathStack = Stack()
//        this.files = files
//        weakCtx = WeakReference(ctx)
//        inflater = LayoutInflater.from(weakCtx.get())
//        selectedSet = HashSet<String>()
//        this.folderClick = object : IFolderClick {
//            override fun openFolder(path: String) {
//                Dlog.log(javaClass, "openFolder=${path}")
//                val list = GetFilesUtils.getInstance().getSonNode(path)
//                Dlog.log(javaClass, "list=${list}")
//                list?.apply {
//                    //如果这个目录可以进去，则把路径入栈
//                    pathStack.push(path)
//
//                    this as java.util.ArrayList<Map<String, Object>>
//
//                    val previousSize = files.size
//                    (files as java.util.ArrayList<Map<String, Object>>).clear()
//                    notifyItemRangeRemoved(0, previousSize);
//
//                    this.forEach { (files as java.util.ArrayList<Map<String, Object>>).add(it) }
//                    notifyItemRangeChanged(0, files.size)
//                }
//            }
//        }
//
//    }

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
        //ArrayList<Map<String, Any>>()
        weakCtx = WeakReference(ctx)
        inflater = LayoutInflater.from(weakCtx.get())
        selectedSet = HashSet<Map<String, Any>>()
        this.folderClick = object : IFolderClick {
            override fun openFolder(path: String) {
                var list = GetFilesUtils.getInstance().getSonNode(path)
                if (isOnlyDirectory) {
                    list = list.filter { it[GetFilesUtils.FILE_INFO_ISFOLDER] as Boolean } as ArrayList<Map<String, Any>>
                }
                list?.apply {
                    //如果这个目录可以进去，则把路径入栈
                    pathStack.push(path)

                    this as java.util.ArrayList<Map<String, Object>>

                    val previousSize = files.size
                    (files as java.util.ArrayList<Map<String, Object>>).clear()
                    notifyItemRangeRemoved(0, previousSize);

                    this.forEach { (files as java.util.ArrayList<Map<String, Object>>).add(it) }
                    notifyItemRangeChanged(0, files.size)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CyanFileExplorerViewHolder {
        var v = inflater!!.inflate(R.layout.cyan_file_explorer_item_demo, parent, false)
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
        var path = item[GetFilesUtils.FILE_INFO_PATH];
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
                folderClick.openFolder(it.getTag(R.id.content_id_1) as String)
            }
        } else {
            //holder.tv1.text = ""
            path as File
            holder.tv1.setImageResource(MimetypeUtils.instance.fromMimeTypeGetIcon(MimetypeUtils.instance.getMimeType(path)))
            holder.tv2.setOnClickListener { }
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
                    selectedSet.add(item)
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
                    selectedSet.clear()
                    //修改item
                    if (needRemoveId != -1) {
                        notifyItemChanged(needRemoveId)
                    }
                    selectedSet.add(item)
                }
            } else {
                val size = selectedSet.size
                if (size >= pickMaxCount && !selectedSet.contains(item)) {
                    Toast.makeText(weakCtx.get(), "${weakCtx.get()?.getString(R.string.max_can_pick)}$pickMaxCount", Toast.LENGTH_SHORT).show()
                } else {
                    if (selectedSet.contains(item)) {
                        selectedSet.remove(item)
                    } else {
                        selectedSet.add(item)
                    }
                }
            }
            notifyItemChanged(index)
        }
    }

    //暂时没用上
    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

}