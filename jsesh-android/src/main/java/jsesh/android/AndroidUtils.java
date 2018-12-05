package jsesh.android;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;

public class AndroidUtils {

    public static Activity getActivity(Context context) {
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity)context;
            }
            context = ((ContextWrapper)context).getBaseContext();
        }
        return null;
    }

}
