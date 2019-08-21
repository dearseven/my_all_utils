
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cyan.Dlog
import com.cyaninject.annotions.CyanView
import com.cyaninject.main.CyanInjector
import com.nextcloud.talk.R
import java.io.File

class CyanFileExplorerActivity : AppCompatActivity() {
    companion object {
        val DATA_KEY = "CyanFileExplorerActivity_result_data"
    }

    @CyanView(R.id.cyanFileExplorer_recyclerview)
    lateinit var recyclerView: RecyclerView

//    lateinit var fileUtils: GetFilesUtils
//    lateinit var fileList: List<Map<String, Any>>;

    lateinit var adapter: CyanFileExplorerAdapter;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cyan_file_explorer)
        CyanInjector.injectActivity(this)

//        fileUtils = GetFilesUtils.getInstance()

        recyclerView?.also {
            it.layoutManager = LinearLayoutManager(this@CyanFileExplorerActivity, RecyclerView.VERTICAL, false)
            it.addItemDecoration(CyanFileExplorerDecoration(this@CyanFileExplorerActivity, CyanFileExplorerDecoration.VERTICAL_LIST))
            it.itemAnimator = DefaultItemAnimator()
            adapter = CyanFileExplorerAdapter(this@CyanFileExplorerActivity)
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
