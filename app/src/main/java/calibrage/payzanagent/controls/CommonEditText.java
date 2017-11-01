package calibrage.payzanagent.controls;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

import calibrage.payzanagent.commonUtil.fontUtil;

/**
 * Created by Calibrage19 on 05-10-2017.
 */

public class CommonEditText extends EditText {
    public CommonEditText(Context context) {
        super(context);
        ApplyCustomFont(context);
    }

    public CommonEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        ApplyCustomFont(context);
    }

    public CommonEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        ApplyCustomFont(context);
    }

    public CommonEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        ApplyCustomFont(context);
    }
    private void ApplyCustomFont(Context context) {
        Typeface customFont = fontUtil.gettypeFace(context, "Font-Regular.ttf");
        setTypeface(customFont);
    }
}
