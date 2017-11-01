package calibrage.payzanagent.controls;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

import calibrage.payzanagent.commonUtil.fontUtil;

/**
 * Created by Calibrage19 on 05-10-2017.
 */

public class CommonTextView extends TextView {

    public CommonTextView(Context context) {
        super(context);
        ApplyCustomFont(context);
    }

    public CommonTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        ApplyCustomFont(context);
    }

    public CommonTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        ApplyCustomFont(context);
    }

    public CommonTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        ApplyCustomFont(context);
    }

    private void ApplyCustomFont(Context context) {
        Typeface customFont = fontUtil.gettypeFace(context, "Font-Regular.ttf");
        setTypeface(customFont);
    }


}
