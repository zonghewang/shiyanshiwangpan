package tk.zhla.citsoft.pan.parse.entity;

public class FileDataDBEntity extends FileDataEntity {
	// flag INTEGER,  
	//spare TEXT ,
	//spare1 TEXT , spare2 TEXT 
	//spare3 TEXT
	/**
	 * 0 ,���������б�    �����ϴ��б�
	 * 1 ,��ʼ����        �ϴ���ʼ
	 * 2, ����ʧ��      �ϴ�ʧ��
	 */
	private int flag;
	private String spare ;//�ļ��Ѿ����صĴ�С
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public String getSpare() {
		return spare;
	}
	public String getSpare2() {
		return spare2;
	}
	public void setSpare2(String spare2) {
		this.spare2 = spare2;
	}
	public void setSpare(String spare) {
		this.spare = spare;
	}
	/**
	 * �ļ�����ʧ�ܵ�ԭ��
	 */
	private String spare2;
	
	/**
	 * ���ص����ص�·��
	 */
	private String path;
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
	
	
}
