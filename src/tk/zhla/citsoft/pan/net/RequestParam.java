package tk.zhla.citsoft.pan.net;

import java.util.List;

import org.apache.http.NameValuePair;

import tk.zhla.citsoft.pan.parse.Parse;


public class RequestParam {
	public String url ;
	
	public List<NameValuePair> pairs;
	
	
	public static final int GET = 0;
	
	public static final int POST = 1;
	
	public int method = GET;
	
	public Parse parse;
}
