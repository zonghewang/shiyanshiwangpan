package tk.zhla.citsoft.pan.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import tk.zhla.citsoft.pan.myclass.Album;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;


public class LocalImageFind {
	// public final static String ROOT_PATH = "mnt/";

	// File rootFile = null;

	private Handler handler = null;

	private Context context = null;

	private Map<String, List<String>> dir = new HashMap<String, List<String>>();

	private List<Album> albums = new ArrayList<Album>();

	public LocalImageFind(Handler handler, Context context) {
		super();
		this.handler = handler;
		this.context = context;
		getImages();
	}

	private void getImages() {

		new Thread(new Runnable() {

			public void run() {
				System.out.println("LocalImageFind");
				Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				ContentResolver mContentResolver = context.getContentResolver();

				// ֻ��ѯjpeg��png��ͼƬ
				Cursor mCursor = mContentResolver.query(mImageUri, null,
						MediaStore.Images.Media.MIME_TYPE + "=? or "
								+ MediaStore.Images.Media.MIME_TYPE + "=? or "
								+ MediaStore.Images.Media.MIME_TYPE + "=? or "
								+ MediaStore.Images.Media.MIME_TYPE + "=?",
						new String[] { "image/jpeg", "image/png", "image/gif",
								"image/bmp" },
						MediaStore.Images.Media.DATE_MODIFIED);

				if (mCursor == null) {
					return;
				}

				while (mCursor.moveToNext()) {
					// ��ȡͼƬ��·��
					String path = mCursor.getString(mCursor
							.getColumnIndex(MediaStore.Images.Media.DATA));

					// ��ȡ��ͼƬ�ĸ�·����
					String parentName = new File(path).getParentFile()
							.getName();

					// ���ݸ�·������ͼƬ���뵽mGruopMap��

					if (!dir.containsKey(parentName)) {
						List<String> chileList = new ArrayList<String>();
						chileList.add(path);
						dir.put(parentName, chileList);
					} else {
						dir.get(parentName).add(path);
					}
				}
				Set<String> tempSet = dir.keySet();
				Iterator<String> iterator = tempSet.iterator();

				while (iterator.hasNext()) {
					String path = iterator.next();
					List<String> images = dir.get(path);
					String firstImage = images.get(0);
					int imageCount = images.size();
					Album album = new Album();
					// if(path.startsWith("/storage/emulated/0")){
					// path.replaceFirst("/storage/emulated/0",
					// "/mnt/shell/emulated/0");
					// }
					album.setFilePath(path);
					album.setFirstImagePath(firstImage);
					album.setPhotoCount(imageCount);
					albums.add(album);
				}

				// ֪ͨHandlerɨ��ͼƬ���

				Message msg = handler.obtainMessage();
				msg.obj = albums;
				msg.what = 1000;
				msg.sendToTarget();
				mCursor.close();
			}
		}).start();

	}

}
