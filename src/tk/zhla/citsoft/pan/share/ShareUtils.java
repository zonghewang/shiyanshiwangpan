package tk.zhla.citsoft.pan.share;

import java.util.HashSet;
import java.util.Set;

import tk.zhla.citsoft.pan.net.CookieUtil;
import tk.zhla.citsoft.pan.parse.entity.LoginEntity;

import com.google.gson.Gson;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class ShareUtils {
	private SharedPreferences sp = null;
	private Editor editor = null;
	public ShareUtils(Context context) {
		sp = context.getSharedPreferences("data",Context.MODE_PRIVATE);
		editor = sp.edit();
	}
	
	public void addDownPath(String path){
		Set<String> set = sp.getStringSet("downpath",new HashSet<String>());
		set.add(path);
		editor.putStringSet("path", set);
		editor.commit();
	}
	
	public Set<String> getDownPath(){
		return sp.getStringSet("downpath", new HashSet<String>());
	}
	
	public void removeDownPath(String path){
		Set<String> set = sp.getStringSet("downpath",new HashSet<String>());
		set.remove(path);
		editor.putStringSet("path", set);
		editor.commit();
	}
	
	public void setAutoPhotoBack(boolean flag){
		editor.putBoolean("autophotoback", flag);
		editor.commit();
	}
	
	public void addPath(String path){
		Set<String> set = sp.getStringSet("path",new HashSet<String>());
		set.add(path);
		editor.putStringSet("path", set);
		editor.commit();
	}
	
	public void clearPath(){
		editor.putStringSet("path", new HashSet<String>());
		editor.commit();
	}
	
	public boolean isExistPath(String path){
		Set<String> set = sp.getStringSet("path",new HashSet<String>());
		if(set.contains(path)){
			return true;
		}
		return false;
	}
	
	public boolean getAutoPhotoBack(){
		return sp.getBoolean("autophotoback", false);
	}
	
	public void setURL(String url){
		editor.putString("url", url);
		editor.commit();
	}
	
	public String getURL(){
		return sp.getString("url", "");
	}
	
	public void setLoginEntity(LoginEntity entity){
		Gson gson = new Gson();
		editor.putString("entity", gson.toJson(entity));
		editor.commit();
	}
	
	public LoginEntity getLoginEntity(){
		String e = sp.getString("entity","");
		Gson g = new Gson();
		return g.fromJson(e, LoginEntity.class);
	}
	
	public void setAutoLogin(boolean isAuto){
		editor.putBoolean("autoLogin", isAuto);
		editor.commit();
	}
	public boolean isAutoLogin(){
		return sp.getBoolean("autoLogin", false);
	}
	
	public CookieUtil getCookieUtil(){
		LoginEntity entity =  getLoginEntity();
		CookieUtil cookieUtil = new CookieUtil(Integer.parseInt(entity.getId()), entity.getToken());
		return cookieUtil;
	}
	
	public void setUsername(String name){
		editor.putString("username", name);
		editor.commit();
	}
	public void setPwd(String pwd){
		editor.putString("pwd", pwd);
		editor.commit();
	}
	
	public String getUsername(){
		return sp.getString("username", "");
	}
	
	public String getPwd(){
		return sp.getString("pwd", "");
	}
}
