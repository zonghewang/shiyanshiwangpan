package tk.zhla.citsoft.pan.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLHelper extends SQLiteOpenHelper {
	public MySQLHelper(Context context, String name,int v) {
		super(context, name, null, v);
	}
	

	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE download_finish (_id INTEGER PRIMARY KEY AUTOINCREMENT,aid INTEGER,pid INTEGER,n TEXT,m TEXT,ms INTEGER,pc TEXT,t TEXT,fid INTEGER,u TEXT,s TEXT,sha1 TEXT, state  INTEGER , time Text , flag INTEGER,  spare TEXT , spare1 TEXT , spare2 TEXT ,spare3 TEXT ,path text )");
		db.execSQL("CREATE TABLE download_inter ( _id INTEGER PRIMARY KEY AUTOINCREMENT,aid INTEGER,pid INTEGER,n TEXT,m TEXT,ms INTEGER,pc TEXT,t TEXT,fid INTEGER,u TEXT,s TEXT,sha1 TEXT, state  INTEGER , time Text , flag INTEGER,  spare TEXT , spare1 TEXT , spare2 TEXT ,spare3 TEXT,path text )");
		db.execSQL("CREATE TABLE upload_finish (_id INTEGER PRIMARY KEY AUTOINCREMENT,aid INTEGER,pid INTEGER,n TEXT,m TEXT,ms INTEGER,pc TEXT,t TEXT,fid INTEGER,u TEXT,s TEXT,sha1 TEXT,state  INTEGER , time Text , flag INTEGER,  spare TEXT , spare1 TEXT , spare2 TEXT ,spare3 TEXT,path text )");
		db.execSQL("CREATE TABLE upload_inter (_id INTEGER PRIMARY KEY AUTOINCREMENT,aid INTEGER,pid INTEGER,n TEXT,m TEXT,ms INTEGER,pc TEXT,t TEXT,fid INTEGER,u TEXT,s TEXT,sha1 TEXT,state  INTEGER , time Text , flag INTEGER,  spare TEXT , spare1 TEXT , spare2 TEXT ,spare3 TEXT,path text )");
	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}
}
