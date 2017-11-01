package calibrage.payzanagent.controls;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.widget.Spinner;

/**
 * Created by Calibrage19 on 09-10-2017.
 */

public class CommonSpinner extends Spinner {
    public CommonSpinner(Context context) {
        super(context);
    }

    public CommonSpinner(Context context, int mode) {
        super(context, mode);
    }

    public CommonSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CommonSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CommonSpinner(Context context, AttributeSet attrs, int defStyleAttr, int mode) {
        super(context, attrs, defStyleAttr, mode);
    }

    public CommonSpinner(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes, int mode) {
        super(context, attrs, defStyleAttr, defStyleRes, mode);
    }

    public CommonSpinner(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes, int mode, Resources.Theme popupTheme) {
        super(context, attrs, defStyleAttr, defStyleRes, mode, popupTheme);
    }
}
