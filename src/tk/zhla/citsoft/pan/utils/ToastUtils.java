package tk.zhla.citsoft.pan.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtils {
	public static void toast(Context context,String str){
		Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
	}
}
