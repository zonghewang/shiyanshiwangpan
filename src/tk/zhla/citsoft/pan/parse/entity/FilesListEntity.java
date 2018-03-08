package tk.zhla.citsoft.pan.parse.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FilesListEntity  implements Serializable{
	private int count;
	private String order;
	private int uid;
	private boolean state;
	private String error = "";
	private String errno = "";
	private String time;
	private int offset;
	private int limit;
	private int aid;
	private int cid;
	private int is_asc;
	private int star;
	private int is_share;
	private int type;
	private List<FileDataFatherEntity> fatherEntities;
	private List<FilePathEntity> filePathEntities;
public FilesListEntity() {
	// TODO Auto-generated constructor stub
}
	/**
	 * ºÏ²¢
	 */
	public void merge(FilesListEntity entity){
		if(entity==null){
			return;
		}
		if(entity.getFatherEntities()!=null){
			this.fatherEntities.addAll(entity.getFatherEntities());
		}
//		if(entity.getFilePathEntities()!=null){
//			this.filePathEntities.addAll(entity.getFilePathEntities());
//		}
	}

	public List<FileDataFatherEntity> getFatherEntities() {
		return fatherEntities;
	}

	public void setFatherEntities(List<FileDataFatherEntity> fatherEntities) {
		this.fatherEntities = fatherEntities;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
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

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
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

	public int getIs_asc() {
		return is_asc;
	}

	public void setIs_asc(int is_asc) {
		this.is_asc = is_asc;
	}

	public int getStar() {
		return star;
	}

	public void setStar(int star) {
		this.star = star;
	}

	public int getIs_share() {
		return is_share;
	}

	public void setIs_share(int is_share) {
		this.is_share = is_share;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public List<FilePathEntity> getFilePathEntities() {
		return filePathEntities;
	}

	public void setFilePathEntities(List<FilePathEntity> filePathEntities) {
		this.filePathEntities = filePathEntities;
	}

	public FilesListEntity(int count, String order, int uid, boolean state,
			String error, String errno, String time, int offset, int limit,
			int aid, int cid, int is_asc, int star, int is_share, int type,
			List<FileDataFatherEntity> fatherEntities,
			List<FilePathEntity> filePathEntities) {
		super();
		this.count = count;
		this.order = order;
		this.uid = uid;
		this.state = state;
		this.error = error;
		this.errno = errno;
		this.time = time;
		this.offset = offset;
		this.limit = limit;
		this.aid = aid;
		this.cid = cid;
		this.is_asc = is_asc;
		this.star = star;
		this.is_share = is_share;
		this.type = type;
		this.fatherEntities = fatherEntities;
		this.filePathEntities = filePathEntities;
	}

	
	

	
}
