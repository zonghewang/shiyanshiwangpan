package tk.zhla.citsoft.pan.parse.entity;

import java.io.Serializable;

public class FilePathEntity implements Serializable {
	private String name;
	private int aid;
	private int cid;
	private int pid;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAid() {
		return aid;
	}

	public void setAid(int aid) {
		this.aid = aid;
	}

	public int getCid() {
		return cid;
	}

	public void setCid(int cid) {
		this.cid = cid;
	}

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public FilePathEntity() {
		super();
		// TODO Auto-generated constructor stub
	}

	public FilePathEntity(String name, int aid, int cid, int pid) {
		super();
		this.name = name;
		this.aid = aid;
		this.cid = cid;
		this.pid = pid;
	}

}
