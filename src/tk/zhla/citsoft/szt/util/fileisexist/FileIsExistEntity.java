package tk.zhla.citsoft.szt.util.fileisexist;

public class FileIsExistEntity {
	
//	{"state":true,"error":"","errno":"","type":"file"}
//	State :   TrueΪ�ļ����ڣ� false Ϊ���ִ���
//	Error:  ���ļ�������ʱstateΪfalse��errorֵΪ FILE_NOT_EXIST, ����Ϊ��������ԭ��
//	Errno:  Ϊ������롣
//	Type:  ��state Ϊtrue ʱ����type�ֶΣ�fileΪ�ļ���dir ΪĿ¼

	
	private boolean state;
	private String error;
	private String errno;
	private String type;
	
	@Override
	public String toString() {
		return "FileIsExistEntity [state=" + state + ", error=" + error
				+ ", errno=" + errno + ", type=" + type + "]";
	}
	public boolean isState() {
		return state;
	}
	public void setState(boolean state) {
		this.state = state;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public String getErrno() {
		return errno;
	}
	public void setErrno(String errno) {
		this.errno = errno;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	

}
