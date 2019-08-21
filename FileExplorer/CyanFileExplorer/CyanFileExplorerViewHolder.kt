
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cyaninject.annotions.CyanView
import com.cyaninject.main.CyanInjector
import com.nextcloud.talk.R

class CyanFileExplorerViewHolder : RecyclerView.ViewHolder {
    @CyanView(R.id.cfeid_tv1)
    lateinit var tv1: TextView
    @CyanView(R.id.cfeid_tv2)
    lateinit var tv2: TextView
    @CyanView(R.id.cfeid_tv3)
    lateinit var tv3: TextView


    constructor(view: View) : super(view) {
        CyanInjector.injectView(this, view)
    }
}