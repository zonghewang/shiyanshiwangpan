package tk.zhla.citsoft.pan.utils;

public class Logs {
	public static boolean debugging = true;
	public static void log(Object b){
		if(debugging){
			System.out.println(b);
		}
	}
}
