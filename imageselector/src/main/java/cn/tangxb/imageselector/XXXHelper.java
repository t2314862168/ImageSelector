package cn.tangxb.imageselector;

import android.content.Context;
import android.widget.TextView;

/**
 * Created by tangxb on 2015/10/8.
 */
public class XXXHelper {
    private Context mCtx;
    private TextView mTextView;

    private static XXXHelper ourInstance = null;

    public static XXXHelper getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new XXXHelper(context);
        }
        return ourInstance;
    }

    public void setRetainedTextView(TextView tv){
        this.mTextView = tv;
        mTextView.setText(mCtx.getString(android.R.string.ok));
    }

    private XXXHelper() {
    }

    private XXXHelper(Context context) {
        this.mCtx = context;
    }
}
