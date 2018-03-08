package tk.zhla.citsoft.pan.ui;

import java.io.File;
import java.util.List;

import tk.zhla.citsoft.pan.R;
import tk.zhla.citsoft.pan.db.DBManager;
import tk.zhla.citsoft.pan.parse.entity.FileDataDBEntity;
import tk.zhla.citsoft.pan.ui.adapter.DownAndUpAdapter;
import tk.zhla.citsoft.pan.ui.dialog.JDDialogDownedStop;
import tk.zhla.citsoft.pan.utils.FileOpenUtils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Shader.TileMode;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class DownloadED extends Activity implements OnClickListener {
	private View back;
	private Intent intent;
	private ListView lv;
	private DBManager sq;
	private DownAndUpAdapter adapter;
	private List<FileDataDBEntity> files;
	private View nullShow;

	private View stop;
	
	private TextView tv = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.download_ed);
		sq = new DBManager(this);
		tv = (TextView) findViewById(R.id.title);
		sq.open();
		getFile();
		setView();
	}
	
	public List<FileDataDBEntity> getFileDataDBEntities(){
		return files;
	}

	public void getFile() {
		int type = getIntent().getIntExtra("type", 0);
		switch (type) {
		case 0:
			tv.setText("上传/下载");
			files = sq.getDownloadingFiles();
			files.addAll(sq.getUpLoadingFiles());
			break;

		case 1:
			tv.setText("下载文件");
			files = sq.getDownloadedFiles();
			break;
		case 2:
			tv.setText("上传");
			files = sq.getUpLoadedFiles();
			break;
		}
	}

	public void refresh() {
		getFile();
		adapter = new DownAndUpAdapter(getApplicationContext(), files);
		lv.setAdapter(adapter);
	}

	public void setView() {
		stop = findViewById(R.id.trans_lay_stop);
		stop.setOnClickListener(this);
		nullShow = findViewById(R.id.downlond_null_show);
		lv = (ListView) findViewById(R.id.downlond_listview);
		adapter = new DownAndUpAdapter(getApplicationContext(), files);
		lv.setAdapter(adapter);
		if (files != null) {
			lv.setVisibility(View.VISIBLE);
			nullShow.setVisibility(View.GONE);
		} else {
			lv.setVisibility(View.GONE);
			nullShow.setVisibility(View.VISIBLE);
		}
		back = findViewById(R.id.downlond_back);
		back.setOnClickListener(this);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Toast.makeText(
						getApplicationContext(),
						files.get(arg2).getPath() + "vv"
								+ files.get(arg2).getN(), 0).show();
				if (files.get(arg2).getPath() != null) {
					File file = new File(files.get(arg2).getPath());
					
					if (file.exists()) {
						if(file.isFile()){
							try {
								Intent intent = FileOpenUtils.openFile(files.get(
										arg2).getPath());
								startActivity(intent);
							} catch (Exception e) {
								Toast.makeText(DownloadED.this, "文件类型不支持", 0)
										.show();
							}
						}else {
							File f1 = new File(file.getAbsolutePath()+"/"+files.get(arg2).getN());
							try {
								Intent intent = FileOpenUtils.openFile(f1.getAbsolutePath());
								startActivity(intent);
							} catch (Exception e) {
								Toast.makeText(DownloadED.this, "文件类型不支持", 0)
										.show();
							}
						}
					} else {
						Toast.makeText(DownloadED.this, "文件删除已删除", 0).show();
					}
				}

			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.downlond_back:
			DownloadED.this.finish();
			break;
		case R.id.trans_lay_stop:
			JDDialogDownedStop stop = new JDDialogDownedStop(this);
			stop.showMyDialog();

			break;

		default:
			break;
		}

	}
}
