package tk.zhla.citsoft.pan.db;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import tk.zhla.citsoft.pan.parse.entity.FileDataDBEntity;
import tk.zhla.citsoft.pan.share.ShareUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


public class DBManager {
	private SQLiteDatabase db;
	
	private Context context;
	
	public DBManager(Context context) {
		this.context = context;
	}
	
	public void open(){
		ShareUtils shareUtils = new ShareUtils(context);
		db = new MySQLHelper(context, "down_up"+shareUtils.getLoginEntity().getId()+".db", 1).getWritableDatabase();
	}
	public void close(){
		if(db!=null){
			if(db.isOpen()){
				db.close();
			}
		}
	}
	
	public void addDownloadingFile(FileDataDBEntity entity){
		ContentValues values = new ContentValues();
		values.put("aid", entity.getAid());
		values.put("pid", entity.getPid());
		values.put("fid", entity.getFid());
		values.put("m", entity.getM());
		values.put("n", entity.getN());
		values.put("pc", entity.getPc());
		values.put("t", entity.getT());
		values.put("u", entity.getU());
		values.put("s", entity.getS());
		values.put("sha1", entity.getShal());
		values.put("path", entity.getPath());
		values.put("spare", entity.getSpare());
		values.put("flag", entity.getFlag());
		values.put("spare2", entity.getSpare2());
		FileDataDBEntity userLocalFile2 = getDownloadingFile(entity.getFid());
		if (userLocalFile2  == null) {
			db.insert("download_inter", null, values);
		} else {
			try {
				updateDownloadingFile(entity);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void addUpLoadingFile(FileDataDBEntity entity){
		ContentValues values = new ContentValues();
		values.put("aid", entity.getAid());
		values.put("pid", entity.getPid());
		values.put("fid", entity.getFid());
		values.put("m", entity.getM());
		values.put("n", entity.getN());
		values.put("pc", entity.getPc());
		values.put("t", entity.getT());
		values.put("u", entity.getU());
		values.put("s", entity.getS());
		values.put("sha1", entity.getShal());
		values.put("path", entity.getPath());
		values.put("spare", entity.getSpare());
		values.put("flag", entity.getFlag());
		values.put("spare2", entity.getSpare2());
	//	FileDataDBEntity userLocalFile2 = getUpLoadingFile(entity.getFid());
		//if (userLocalFile2  == null) {
		db.insert("upload_inter", null, values);
//		} else {
//			updateUpLoadingFile(entity);
//		}
	}
	
	public void addDownloadedFile(FileDataDBEntity entity){
		ContentValues values = new ContentValues();
		values.put("aid", entity.getAid());
		values.put("pid", entity.getPid());
		values.put("fid", entity.getFid());
		values.put("m", entity.getM());
		values.put("n", entity.getN());
		values.put("pc", entity.getPc());
		values.put("t", entity.getT());
		values.put("u", entity.getU());
		values.put("s", entity.getS());
		values.put("sha1", entity.getShal());
		values.put("path", entity.getPath());
		values.put("spare", entity.getSpare());
		values.put("flag", entity.getFlag());
		values.put("spare2", entity.getSpare2());
		FileDataDBEntity userLocalFile2 = getDownloadedFile(entity.getFid());
		if (userLocalFile2  == null) {
			db.insert("download_finish", null, values);
		} else {
			updateDownloadedFile(entity);
		}
	}
	public void addUpLoadedFile(FileDataDBEntity entity){
		ContentValues values = new ContentValues();
		values.put("aid", entity.getAid());
		values.put("pid", entity.getPid());
		values.put("fid", entity.getFid());
		values.put("m", entity.getM());
		values.put("n", entity.getN());
		values.put("pc", entity.getPc());
		values.put("t", entity.getT());
		values.put("u", entity.getU());
		values.put("s", entity.getS());
		values.put("sha1", entity.getShal());
		values.put("path", entity.getPath());
		values.put("spare", entity.getSpare());
		values.put("flag", entity.getFlag());
		values.put("spare1", entity.getSpare());
		values.put("spare2", entity.getSpare2());
		FileDataDBEntity userLocalFile2 = getUpLoadedFile(entity.getFid());
		if (userLocalFile2  == null) {
			db.insert("upload_finish", null, values);
		} else {
			updateUpLoadedFile(entity);
		}
	}
	public void updateDownloadingFile(FileDataDBEntity entity) throws IOException{
		if(getDownloadingFile(entity.getFid())==null){
			throw new IOException();
		}
		ContentValues values = new ContentValues();
		values.put("aid", entity.getAid());
		values.put("pid", entity.getPid());
		values.put("fid", entity.getFid());
		values.put("m", entity.getM());
		values.put("n", entity.getN());
		values.put("pc", entity.getPc());
		values.put("t", entity.getT());
		values.put("u", entity.getU());
		values.put("s", entity.getS());
		values.put("sha1", entity.getShal());
		values.put("path", entity.getPath());
		values.put("spare", entity.getSpare());
		values.put("flag", entity.getFlag());
		values.put("spare1", entity.getSpare());
		values.put("spare2", entity.getSpare2());
		db.update("download_inter", values, "fid=?", new String[]{entity.getFid()+""});
		
	}
	
	public void updateUpLoadingFile(FileDataDBEntity entity){
		ContentValues values = new ContentValues();
		values.put("aid", entity.getAid());
		values.put("pid", entity.getPid());
		values.put("fid", entity.getFid());
		values.put("m", entity.getM());
		values.put("n", entity.getN());
		values.put("pc", entity.getPc());
		values.put("t", entity.getT());
		values.put("u", entity.getU());
		values.put("s", entity.getS());
		values.put("sha1", entity.getShal());
		values.put("path", entity.getPath());
		values.put("spare", entity.getSpare());
		values.put("flag", entity.getFlag());
		values.put("spare1", entity.getSpare());
		values.put("spare2", entity.getSpare2());
		db.update("upload_inter", values, "fid=?", new String[]{entity.getFid()+""});
	}
	
	public void updateDownloadedFile(FileDataDBEntity entity){
		ContentValues values = new ContentValues();
		values.put("aid", entity.getAid());
		values.put("pid", entity.getPid());
		values.put("fid", entity.getFid());
		values.put("m", entity.getM());
		values.put("n", entity.getN());
		values.put("pc", entity.getPc());
		values.put("t", entity.getT());
		values.put("u", entity.getU());
		values.put("s", entity.getS());
		values.put("sha1", entity.getShal());
		values.put("path", entity.getPath());
		values.put("spare", entity.getSpare());
		values.put("flag", entity.getFlag());
		values.put("spare1", entity.getSpare());
		values.put("spare2", entity.getSpare2());
		db.update("download_finish", values, "fid=?", new String[]{entity.getFid()+""});
	}
	public void updateUpLoadedFile(FileDataDBEntity entity){
		ContentValues values = new ContentValues();
		values.put("aid", entity.getAid());
		values.put("pid", entity.getPid());
		values.put("fid", entity.getFid());
		values.put("m", entity.getM());
		values.put("n", entity.getN());
		values.put("pc", entity.getPc());
		values.put("t", entity.getT());
		values.put("u", entity.getU());
		values.put("s", entity.getS());
		values.put("sha1", entity.getShal());
		values.put("path", entity.getPath());
		values.put("spare", entity.getSpare());
		values.put("flag", entity.getFlag());
		values.put("spare1", entity.getSpare());
		values.put("spare2", entity.getSpare2());
		db.update("upload_finish", values, "fid=?", new String[]{entity.getFid()+""});
	}
	
	public List<FileDataDBEntity> getDownloadingFiles(){
		
		return getFileDataDBEntities("download_inter");
	}
	
	public List<FileDataDBEntity> getFileDataDBEntities(String db1){
		Cursor cursor = db.rawQuery("select * from "+db1, null);
		List<FileDataDBEntity> lists = new ArrayList<FileDataDBEntity>();
		while (cursor.moveToNext()) {
			FileDataDBEntity file = new FileDataDBEntity();
			file.setAid(cursor.getInt(cursor.getColumnIndex("aid")));
			file.setFid(cursor.getInt(cursor.getColumnIndex("fid")));
			file.setPid(cursor.getInt(cursor.getColumnIndex("pid")));
			file.setS(Long.parseLong(cursor.getString(cursor.getColumnIndex("s"))));
			file.setU(cursor.getString(cursor.getColumnIndex("u")));
			file.setShal(cursor.getString(cursor.getColumnIndex("sha1")));
			file.setN(cursor.getString(cursor.getColumnIndex("n")));
			file.setM(Integer.parseInt(cursor.getString(cursor.getColumnIndex("m"))));
			file.setPc(cursor.getString(cursor.getColumnIndex("pc")));
			file.setT(cursor.getString(cursor.getColumnIndex("t")));
			file.setPath(cursor.getString(cursor.getColumnIndex("path")));
			file.setSpare(cursor.getString(cursor.getColumnIndex("spare")));
			file.setSpare2(cursor.getString(cursor.getColumnIndex("spare2")));
			file.setFlag(cursor.getInt(cursor.getColumnIndex("flag")));
			lists.add(file);
		}
		cursor.close();
		return lists;
	}
	
	public List<FileDataDBEntity> getUpLoadingFiles(){
		return getFileDataDBEntities("upload_inter");
	}
	
	public List<FileDataDBEntity> getDownloadedFiles(){
		return getFileDataDBEntities("download_finish");
	}
	public List<FileDataDBEntity> getUpLoadedFiles(){
		return getFileDataDBEntities("upload_finish");
	}
	
	public FileDataDBEntity getFileDataDBEntity(String db1,int fid){
		Cursor cursor = db.rawQuery("select * from "+db1 +" where fid="+fid, null);
		if (cursor.moveToNext()) {
			FileDataDBEntity file = new FileDataDBEntity();
			file.setAid(cursor.getInt(cursor.getColumnIndex("aid")));
			file.setFid(cursor.getInt(cursor.getColumnIndex("fid")));
			file.setPid(cursor.getInt(cursor.getColumnIndex("pid")));
			file.setS(Long.parseLong(cursor.getString(cursor.getColumnIndex("s"))));
			file.setU(cursor.getString(cursor.getColumnIndex("u")));
			file.setShal(cursor.getString(cursor.getColumnIndex("sha1")));
			file.setN(cursor.getString(cursor.getColumnIndex("n")));
			file.setM(Integer.parseInt(cursor.getString(cursor.getColumnIndex("m"))));
			file.setPc(cursor.getString(cursor.getColumnIndex("pc")));
			file.setT(cursor.getString(cursor.getColumnIndex("t")));
			file.setPath(cursor.getString(cursor.getColumnIndex("path")));
			file.setSpare(cursor.getString(cursor.getColumnIndex("spare")));
			file.setSpare2(cursor.getString(cursor.getColumnIndex("spare2")));
			file.setFlag(cursor.getInt(cursor.getColumnIndex("flag")));
			cursor.close();
			return file;
		}
		cursor.close();
		return null;
	}
	
	public FileDataDBEntity getDownloadingFile(int fid){
		return getFileDataDBEntity("download_inter", fid);
	}
	public FileDataDBEntity getUpLoadingFile(int fid){
		return getFileDataDBEntity("upload_inter", fid);
	}
	
	public FileDataDBEntity getDownloadedFile(int fid){
		return getFileDataDBEntity("download_finish", fid);
	}
	public FileDataDBEntity getUpLoadedFile(int fid){
		return getFileDataDBEntity("upload_finish", fid);
	}
	
	public void deleteDownloadingFile(int fid){
		deleteFileDataDBEntity("download_inter", fid);
	}
	public void deleteUpLoadingFile(int fid){
		deleteFileDataDBEntity("upload_inter", fid);
	}
	
	public void deleteDownloadedFile(int fid){
		deleteFileDataDBEntity("download_finish", fid);
		
	}
	public void deleteUpLoadedFile(int fid){
		deleteFileDataDBEntity("upload_finish", fid);
		
	}
	
	public void deleteFileDataDBEntity(String db1,int fid){
		db.delete(db1, "fid=?", new String[] { fid+"" });
	}
	

	
	
	
}
