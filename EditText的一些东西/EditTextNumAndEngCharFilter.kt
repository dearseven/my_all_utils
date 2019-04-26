
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import kotlinx.android.synthetic.main.activity_update_my_password.*
import java.util.regex.Pattern
import java.util.regex.PatternSyntaxException

/**
 * 过滤edittext，只能输入英语和数字
 * addTextChangedListener(~~)
 * Created by wx on 2017/9/6.
 */
class EditTextNumAndEngCharFilter : TextWatcher {
    var et: EditText? = null

    constructor(et: EditText) {
        this.et = et
    }

    override fun afterTextChanged(s: Editable?) {
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

    @Throws(PatternSyntaxException::class)
    fun stringFilter(str: String): String {
        // 只允许字母、数字和汉字
        //val regEx = "[^a-zA-Z0-9\u4E00-\u9FA5]"//正则表达式

        // 只允许字母、数字
        val regEx = "[^a-zA-Z0-9]"//正则表达式
        val p = Pattern.compile(regEx)
        val m = p.matcher(str)
        return m.replaceAll("").trim()
    }
}