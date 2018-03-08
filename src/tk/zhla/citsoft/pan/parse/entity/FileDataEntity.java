package tk.zhla.citsoft.pan.parse.entity;

public class FileDataEntity extends FileDataFatherEntity {
	private int fid;
	private long s;
	private String u;
	private String shal;
	
	public String picPath;
	
	
	public int getFid() {
		return fid;
	}

	public void setFid(int fid) {
		this.fid = fid;
	}

	public long getS() {
		return s;
	}

	public void setS(long s) {
		this.s = s;
	}

	public String getU() {
		return u;
	}

	public void setU(String u) {
		this.u = u;
	}

	public String getShal() {
		return shal;
	}

	public void setShal(String shal) {
		this.shal = shal;
	}

	public FileDataEntity(int fid, int s, String u, String shal) {
		super();
		this.fid = fid;
		this.s = s;
		this.u = u;
		this.shal = shal;
	}

	public FileDataEntity() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "FileDataEntity [fid=" + fid + ", s=" + s + ", u=" + u
				+ ", shal=" + shal + "]";
	}
	
	public FileDataDBEntity getFileDataDBEntity(){
		FileDataDBEntity dbEntity = new FileDataDBEntity();
		dbEntity.setAid(getAid());
		dbEntity.setFid(getFid());
		dbEntity.setM(getM());
		dbEntity.setN(getN());
		dbEntity.setPc(getPc());
		dbEntity.setPid(getPid());
		dbEntity.setS(getS());
		dbEntity.setShal(getShal());
		dbEntity.setT(getT());
		dbEntity.setU(getU());
		return dbEntity;
	}

}
