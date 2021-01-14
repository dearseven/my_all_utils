
import android.content.Context;
import android.graphics.Canvas;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.TextView;

public class BoldTextView extends android.support.v7.widget.AppCompatTextView {
   public BoldTextView(Context context) {
        super(context);
        TextPaint tp = getPaint();
        tp.setFakeBoldText(true);
    }

    public BoldTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TextPaint tp = getPaint();
        tp.setFakeBoldText(true);

    }

    public BoldTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TextPaint tp = getPaint();
        tp.setFakeBoldText(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        TextPaint tp = getPaint();
//        tp.setFakeBoldText(true);
    }
}
