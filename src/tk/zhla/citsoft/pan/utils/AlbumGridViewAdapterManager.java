package tk.zhla.citsoft.pan.utils;

import java.util.ArrayList;
import java.util.List;

import tk.zhla.citsoft.pan.myclass.Album;
import tk.zhla.citsoft.pan.ui.adapter.AlbumGridAdapter;

import android.content.Context;
import android.os.Handler;


public class AlbumGridViewAdapterManager {

	private AlbumGridAdapter adapter = null;

	private List<Album> path = new ArrayList<Album>();

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			System.out.println("AlbumGridViewAdapterManager-----Handler");
			path = (List<Album>) msg.obj;
			
			adapter.setAlbums(path);
			adapter.notifyDataSetChanged();
		};

	};
	
	

	public AlbumGridViewAdapterManager(Context context) {

		new LocalImageFind(handler, context);
		adapter = new AlbumGridAdapter(context, path);
	}

	public AlbumGridAdapter getAdapter() {
		return adapter;
	}
}
