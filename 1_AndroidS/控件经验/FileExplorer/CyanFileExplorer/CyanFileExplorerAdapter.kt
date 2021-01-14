
import android.content.Context
import android.os.Environment
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cyan.Dlog
import com.nextcloud.talk.R
import java.io.File
import java.lang.ref.WeakReference
import java.util.*
import kotlin.collections.HashSet

class CyanFileExplorerAdapter : RecyclerView.Adapter<CyanFileExplorerViewHolder> {
    lateinit var files: List<Map<String, Any>>
    lateinit var weakCtx: WeakReference<Context>
    lateinit var inflater: LayoutInflater

    lateinit var selectedSet: HashSet<Map<String, Any>>

    lateinit var folderClick: IFolderClick

    lateinit var pathStack: Stack<String>
    lateinit var rootPath: String

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

    constructor(ctx: Context) {
        pathStack = Stack<String>()
        rootPath = Environment.getExternalStorageDirectory().absolutePath
        pathStack.push(rootPath)
        this.files = GetFilesUtils.getInstance().getSonNode(rootPath)
                ?: ArrayList<Map<String, Any>>()
        //ArrayList<Map<String, Any>>()
        weakCtx = WeakReference(ctx)
        inflater = LayoutInflater.from(weakCtx.get())
        selectedSet = HashSet<Map<String, Any>>()
        this.folderClick = object : IFolderClick {
            override fun openFolder(path: String) {
                val list = GetFilesUtils.getInstance().getSonNode(path)
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
                path = path.absolutePath
            }
        }

        if (item[GetFilesUtils.FILE_INFO_ISFOLDER] as Boolean) {
            holder.tv1.text = "/"
            holder.tv2.setTag(R.id.content_id_1, path)
            holder.tv2.setOnClickListener {
                selectedSet.clear()
                folderClick.openFolder(it.getTag(R.id.content_id_1) as String)
            }
        } else {
            holder.tv1.text = ""
            holder.tv2.setOnClickListener { }
        }

        holder.tv2.text = item[GetFilesUtils.FILE_INFO_NAME] as String

        if (selectedSet.contains(item)) {
            holder.tv3.text = "已"
        } else {
            holder.tv3.text = "未"
        }
        //holder.tv3.setTag(R.id.content_id_1, path)
        holder.tv3.setTag(R.id.content_id_2, position)
        holder.tv3.setTag(R.id.content_id_3, item)
        holder.tv3.setOnClickListener {
            it as TextView
            val index = it.getTag(R.id.content_id_2) as Int
            //val path = it.getTag(R.id.content_id_1) as String
            val item=it.getTag(R.id.content_id_3) as Map<String,Any>

            if (selectedSet.contains(item)) {
                selectedSet.remove(item)
                //it.text = "未"
            } else {
                selectedSet.add(item)
                //it.text = "已"
            }
            notifyItemChanged(index)
        }
    }

    //暂时没用上
    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

}