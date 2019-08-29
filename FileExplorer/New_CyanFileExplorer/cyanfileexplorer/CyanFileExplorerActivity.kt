

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cyaninject.annotions.CyanView
import com.cyaninject.main.CyanInjector
import com.nextcloud.talk.R

class CyanFileExplorerActivity : AppCompatActivity() {
    companion object {
        val DATA_KEY = "CyanFileExplorerActivity_result_data"
        //
        val KEY_IS_ONLY_DIRECTORY = "IS_ONLY_DIRECTORY"
        //
        val KEY_PICK_MAX_COUNT = "PICK_MAX_COUNT"
    }

    //是否只选择目录
    var isOnlyDirectory = false
    //可以最大勾选的数量
    var pickMaxCount = 9

    @CyanView(R.id.cyanFileExplorer_recyclerview)
    lateinit var recyclerView: RecyclerView
    @CyanView(R.id.cyanFileExplorer_ok_btn)
    lateinit var okBtn: AppCompatButton


//    lateinit var fileUtils: GetFilesUtils
//    lateinit var fileList: List<Map<String, Any>>;

    lateinit var adapter: CyanFileExplorerAdapter;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cyan_file_explorer)
        CyanInjector.injectActivity(this)
        //
        isOnlyDirectory = intent.extras.getBoolean(KEY_IS_ONLY_DIRECTORY, isOnlyDirectory)
        pickMaxCount = intent.extras.getInt(KEY_PICK_MAX_COUNT, pickMaxCount)

//        fileUtils = GetFilesUtils.getInstance()

        recyclerView?.also {
            it.layoutManager = LinearLayoutManager(this@CyanFileExplorerActivity, RecyclerView.VERTICAL, false)
            it.addItemDecoration(CyanFileExplorerDecoration(this@CyanFileExplorerActivity, CyanFileExplorerDecoration.VERTICAL_LIST))
            it.itemAnimator = DefaultItemAnimator()
            adapter = CyanFileExplorerAdapter(this@CyanFileExplorerActivity, isOnlyDirectory, pickMaxCount)
            it.adapter = adapter
        }
    }


    override fun onBackPressed() {
        if (adapter.popAndCanBack()) {
            //super.onBackPressed()
            setResult(Activity.RESULT_OK, Intent().apply {
                this.putExtras(Bundle().also {
                    val list = ArrayList<Map<String, Any>>(adapter.selectedSet.size + 1);
                    adapter.selectedSet.forEach { i ->
                        list.add(i)
                    }
                    it.putSerializable(DATA_KEY, list)
                })
            })
            finish()
        }
    }

}
