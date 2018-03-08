package tk.zhla.citsoft.pan.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * @author liujiandong
 * 
 */
public class TimeAndSizeUtil {

	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
			"yyyy��MM��dd  HH:mm:ss");

	/**
	 * 
	 * @param m
	 *            ����� ʱ�� ��ֵ
	 * @return ���ص�Ϊ yyyy��MM��dd HH:mm:ss
	 */
	public static String getTime(String m) {
		try{
		if(m!=null){
			long l = Long.valueOf(m);
			Date date = new Date(l * 1000);
			return DATE_FORMAT.format(date);
		}
		}catch (Exception e){
			
		}
		return "";
	}

	/**
	 * 
	 * @param size
	 *            �����bite
	 * @return **.**k **.**m **.**G
	 */
	public static String getSize(String size) {
		
		try {
			long s = Long.valueOf(size);

			if (s < 1024 * 1024) {

				return ((int) ((((double) s) / (1024) * 100)) / 100.00 + "k");
			} else if (s < 1024 * 1024 * 1024) {

				return ((int) ((((double) s) / (1024 * 1024) * 100)) / 100.00 + "M");
			} else {

				return ((int) ((((double) s) / (1024 * 1024 * 1024) * 100)) / 100.00 + "G");
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";

	}
}
