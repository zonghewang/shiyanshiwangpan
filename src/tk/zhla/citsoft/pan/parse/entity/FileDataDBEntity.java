package tk.zhla.citsoft.pan.parse.entity;

public class FileDataDBEntity extends FileDataEntity {
	// flag INTEGER,  
	//spare TEXT ,
	//spare1 TEXT , spare2 TEXT 
	//spare3 TEXT
	/**
	 * 0 ,加入下载列表    加入上传列表
	 * 1 ,开始下载        上传开始
	 * 2, 下载失败      上传失败
	 */
	private int flag;
	private String spare ;//文件已经下载的大小
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
	 * 文件下载失败的原因
	 */
	private String spare2;
	
	/**
	 * 下载到本地的路径
	 */
	private String path;
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
	
	
}
