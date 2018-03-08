package tk.zhla.citsoft.pan.parse;

import tk.zhla.citsoft.pan.parse.entity.LoginEntity;
import android.telephony.gsm.GsmCellLocation;

import com.google.gson.Gson;

public class LoginParse {
	public LoginEntity parse(String s){
		Gson gson = new Gson();
		return gson.fromJson(s, LoginEntity.class);
	}
}
