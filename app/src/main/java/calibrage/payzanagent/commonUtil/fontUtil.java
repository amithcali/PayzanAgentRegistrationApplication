package calibrage.payzanagent.commonUtil;

import android.content.Context;
import android.graphics.Typeface;

import java.util.HashMap;

/**
 * Created by Calibrage19 on 05-10-2017.
 */

public class fontUtil {
    public  static HashMap<String,Typeface> fontcahce=new HashMap<>();

    public static Typeface gettypeFace(Context context, String FontName) {


        Typeface typeface = fontcahce.get(FontName);
        if(typeface == null)
        {
            typeface= Typeface.createFromAsset(context.getAssets(), FontName);
            fontcahce.put(FontName,typeface);

        }



        return typeface;
    }


}
