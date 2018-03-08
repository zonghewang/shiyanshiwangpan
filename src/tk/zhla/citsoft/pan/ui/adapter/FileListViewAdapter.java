package tk.zhla.citsoft.pan.ui.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import tk.zhla.citsoft.pan.R;
import tk.zhla.citsoft.pan.db.DBManager;
import tk.zhla.citsoft.pan.parse.entity.FileDataEntity;
import tk.zhla.citsoft.pan.parse.entity.FileDataFatherEntity;
import tk.zhla.citsoft.pan.parse.entity.FileDirDataEntity;
import tk.zhla.citsoft.pan.ui.fragment.FileListFragment;
import tk.zhla.citsoft.pan.utils.FindType;
import tk.zhla.citsoft.pan.utils.TimeAndSizeUtil;

import android.graphics.Bitmap;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class FileListViewAdapter extends BaseAdapter {
	
	private FileListFragment fragment = null;
	
	private List<ViewHoder> hoders = new ArrayList<FileListViewAdapter.ViewHoder>();
	
	public void refreshChecked(){
		for (ViewHoder viewHoder : hoders) {
			viewHoder.check.setChecked(false);
		}
	}
	
	
	DisplayImageOptions options;

	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	private DBManager dbManager;

	
	public FileListViewAdapter(FileListFragment fragment) {
		dbManager = new DBManager(fragment.getActivity());
		dbManager.open();
		this.fragment = fragment;
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.ic_stub)
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.considerExifParams(true)
		.displayer(new RoundedBitmapDisplayer(20))
		.build();
		handler.sendEmptyMessageDelayed(1, 3000);
	}
	class   ViewHoder {
		public CheckBox check;
		public Button stop;
		public TextView name;
		public TextView sname;
		public TextView size;
		public TextView time;
		public ImageView image;
		public ImageView imageDow;
		public View file;
		public View f;
		public View btn;
		public View btn2;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(fragment.getFilesListEntity()!=null){
			return fragment.getFilesListEntity().getFatherEntities().size();
		}else {
			return 0;
		}
	}

	@Override
	public Object getItem(int position) {

		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHoder hoder;
		hoder = null;
		FileDataFatherEntity entity = fragment.getFilesListEntity().getFatherEntities().get(position);
		if (convertView == null) {
			hoder = new ViewHoder();
			hoders.add(hoder);
			convertView = View.inflate(fragment.getActivity(), R.layout.netdist_item, null);
			hoder.file = convertView.findViewById(R.id.dist_file);
			hoder.f = convertView.findViewById(R.id.dist_f);
			hoder.check = (CheckBox) convertView
					.findViewById(R.id.dist_item_btn_check);
			hoder.name = (TextView) convertView
					.findViewById(R.id.dist_item_name);
			hoder.image = (ImageView) convertView
					.findViewById(R.id.dist_item_image);
			hoder.imageDow = (ImageView) convertView
					.findViewById(R.id.dist_item_image_dow);
			hoder.btn = convertView.findViewById(R.id.dist_btn);
			hoder.btn2 = convertView.findViewById(R.id.dist_btn2);
			hoder.stop = (Button) convertView
					.findViewById(R.id.dist_item_btn_btn);
			hoder.sname = (TextView) convertView.findViewById(R.id.dist_item_n);
			hoder.size = (TextView) convertView.findViewById(R.id.dist_item_s);
			hoder.time = (TextView) convertView.findViewById(R.id.dist_item_t);
			
			convertView.setTag(hoder);
		} else {
			hoder = (ViewHoder) convertView.getTag();
		}
		hoder.stop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dbManager.deleteDownloadingFile((Integer)v.getTag());
				notifyDataSetChanged();
			}
		});
		
		
		
		hoder.check.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				fragment.checkFile(isChecked, position);
			}
		});
		hoder.check.setChecked(entity.isChecked);
	
		//判定是文件夹还是文件
		if(entity instanceof FileDirDataEntity){ 
			//文件夹
			hoder.image.setImageResource(FindType.findDir(entity.getN()));
			hoder.f.setVisibility(View.GONE);
			hoder.file.setVisibility(View.VISIBLE);
			hoder.name.setText(entity.getN());
			hoder.btn2.setVisibility(View.GONE);
			hoder.check.setVisibility(View.VISIBLE);
			hoder.imageDow.setVisibility(View.GONE);
		}else {
			//文件
			FileDataEntity entity2 = (FileDataEntity)entity;
			if(TextUtils.isEmpty(entity2.getU())){
				 imageLoader.displayImage("drawable://"
							+ FindType
							.findImage(entity2.getN()), hoder.image, options, animateFirstListener);
			}else {
				imageLoader.displayImage(entity2.getU().trim()+"&tsha1="+entity2.getShal()+"_100_100", hoder.image, options, animateFirstListener);
			}
			hoder.size.setText(TimeAndSizeUtil.getSize(entity2.getS()+""));
			hoder.f.setVisibility(View.VISIBLE);
			hoder.time.setText(TimeAndSizeUtil.getTime(entity2.getT()));
			hoder.sname.setText(entity.getN());
			hoder.file.setVisibility(View.GONE);
			if(dbManager.getDownloadingFile(entity2.getFid())!=null){
				System.out.println("下载中");
				hoder.btn2.setVisibility(View.VISIBLE);
				hoder.stop.setTag(entity2.getFid());
				hoder.check.setVisibility(View.GONE);
				hoder.imageDow.setImageResource(R.drawable.job_status_down);
				hoder.imageDow.setVisibility(View.VISIBLE);
			}else {
				hoder.btn2.setVisibility(View.GONE);
				hoder.check.setVisibility(View.VISIBLE);
				hoder.imageDow.setVisibility(View.GONE);
			}
			if(dbManager.getDownloadedFile(entity2.getFid())!=null){
				hoder.imageDow.setImageResource(R.drawable.photo_downloaded);
				hoder.imageDow.setVisibility(View.VISIBLE);
			}else {
				hoder.imageDow.setVisibility(View.GONE);
			}
		}
		//文件目录点击
		convertView.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				fragment.onItemClick(position);
			}
		});
		return convertView;
	}
	private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}
	
	public Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			FileListViewAdapter.this.notifyDataSetChanged();
			handler.sendEmptyMessageDelayed(1, 4000);
		};
	};

}
