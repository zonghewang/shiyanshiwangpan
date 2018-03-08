package tk.zhla.citsoft.pan.ui.adapter;

import java.util.List;

import tk.zhla.citsoft.pan.R;
import tk.zhla.citsoft.pan.net.ExecRunable;
import tk.zhla.citsoft.pan.parse.entity.FileDataEntity;
import tk.zhla.citsoft.pan.parse.entity.FileDownLoadLinkedEnitity;
import tk.zhla.citsoft.pan.ui.PhotoShowActivity;
import tk.zhla.citsoft.pan.ui.fragment.popupwindow.PhotoPopMenuShow;
import tk.zhla.citsoft.pan.utils.FileDownLoadLinkedUtil;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class PhotoViewPager_Adapter extends PagerAdapter  {

	DisplayImageOptions options;
	private PhotoPopMenuShow menuShow = null;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	private PhotoShowActivity context;
	ImageView imageView = null;

	LayoutInflater inflater;
	
	
	

	public PhotoViewPager_Adapter(
			PhotoShowActivity context, PhotoPopMenuShow menuShow) {
		super();
		this.context = context;
		this.menuShow = menuShow;
		options = new DisplayImageOptions.Builder()
				.cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		inflater = ((Activity) context).getLayoutInflater();

	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);

	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		View imageLayout = inflater.inflate(R.layout.photo_show_image,
				container, false);
		assert imageLayout != null;
		final ImageView imageView = (ImageView) imageLayout.findViewById(R.id.image);
		imageView.setVisibility(View.GONE);
		final ProgressBar spinner = (ProgressBar) imageLayout
				.findViewById(R.id.loading);
		FileDataEntity pe = (FileDataEntity) context.getEntity().getFatherEntities().get(position);
	
	
		if(pe.picPath!=null){
			imageLoader.displayImage(
					pe.picPath+"&tsha1="+pe.getShal(), imageView,
					options, new SimpleImageLoadingListener() {
						@Override
						public void onLoadingStarted(String imageUri, View view) {
							spinner.setVisibility(View.VISIBLE);
						}

						@Override
						public void onLoadingFailed(String imageUri, View view,
								FailReason failReason) {
							String message = null;
							switch (failReason.getType()) {
							case IO_ERROR:
								message = "���粻����";
								break;
							case DECODING_ERROR:
								message = "ͼƬ����ʧ��";
								break;
							case NETWORK_DENIED:
								message = "���粻����";
								break;
							case OUT_OF_MEMORY:
								message = "�ף��뻬��������";
								break;
							case UNKNOWN:
								message = "ͼƬ����ʧ��";
								break;
							}
							Toast.makeText(context, message, Toast.LENGTH_SHORT)
									.show();

							spinner.setVisibility(View.VISIBLE);
						}

						@Override
						public void onLoadingComplete(String imageUri,
								View view, Bitmap loadedImage) {
							spinner.setVisibility(View.GONE);
							imageView.setVisibility(View.VISIBLE);
							 
						}
					});
		}else {
		
		imageLoader.displayImage(
					pe.getU()+"&tsha1="+pe.getShal()+"_100_100", imageView,
					options, new SimpleImageLoadingListener() {
						@Override
						public void onLoadingStarted(String imageUri, View view) {
							spinner.setVisibility(View.VISIBLE);
						}

						@Override
						public void onLoadingFailed(String imageUri, View view,
								FailReason failReason) {
							String message = null;
							switch (failReason.getType()) {
							case IO_ERROR:
								message = "���粻����";
								break;
							case DECODING_ERROR:
								message = "ͼƬ����ʧ��";
								break;
							case NETWORK_DENIED:
								message = "���粻����";
								break;
							case OUT_OF_MEMORY:
								message = "�ף��뻬��������";
								break;
							case UNKNOWN:
								message = "ͼƬ����ʧ��";
								break;
							}
							Toast.makeText(context, message, Toast.LENGTH_SHORT)
									.show();

							spinner.setVisibility(View.VISIBLE);
						}

						@Override
						public void onLoadingComplete(String imageUri,
								View view, Bitmap loadedImage) {
							spinner.setVisibility(View.GONE);
							imageView.setVisibility(View.VISIBLE);
							 
						}
					});
			downImage(pe, imageView, spinner);
		}
		menuShow.showPopupWindows(imageLayout);
		container.addView(imageLayout, 0);
		
		return imageLayout;
	}
	
	public class TEMP {
		public FileDownLoadLinkedEnitity enitity;
		public ImageView iv;
		public ProgressBar spinner;
		public FileDataEntity dataEntity;
	}
	
	public void downImage(final FileDataEntity entity ,final ImageView iv,final ProgressBar spinner){
		ExecRunable.execRun(new Runnable() {
			public void run() {
				FileDownLoadLinkedEnitity linkedEnitity = FileDownLoadLinkedUtil.getFileLinked(context, entity.getFid());
				TEMP t = new TEMP();
				t.enitity = linkedEnitity;
				if(linkedEnitity!=null)
				entity.picPath = linkedEnitity.getUrl();
				t.iv = iv;
				t.spinner = spinner;
				Message msg = new Message();
				msg.obj  = t;
				if(linkedEnitity!=null)
				msg.what = 1;
				t.dataEntity = entity;
				handler.sendMessage(msg);
			}
		});
	}
	public Handler  handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case -1:
				
				break;
			case 1:
				
				final TEMP t = (TEMP) msg.obj;
				FileDownLoadLinkedEnitity pe = t.enitity;
				if(t.iv!=null)
				imageLoader.displayImage(
						pe.getUrl()+"&tsha1="+t.dataEntity.getShal(), t.iv,
						options, new SimpleImageLoadingListener() {
							@Override
							public void onLoadingStarted(String imageUri, View view) {
								t.spinner.setVisibility(View.VISIBLE);
							}

							@Override
							public void onLoadingFailed(String imageUri, View view,
									FailReason failReason) {
								String message = null;
								switch (failReason.getType()) {
								case IO_ERROR:
									message = "���粻����";
									break;
								case DECODING_ERROR:
									message = "ͼƬ����ʧ��";
									break;
								case NETWORK_DENIED:
									message = "���粻����";
									break;
								case OUT_OF_MEMORY:
									message = "�ף��뻬��������";
									break;
								case UNKNOWN:
									message = "ͼƬ����ʧ��";
									break;
								}
								Toast.makeText(context, message, Toast.LENGTH_SHORT)
										.show();

								t.spinner.setVisibility(View.VISIBLE);
							}

							@Override
							public void onLoadingComplete(String imageUri,
									View view, Bitmap loadedImage) {
								t.spinner.setVisibility(View.GONE);
								t.iv.setVisibility(View.VISIBLE);
								 
							}
						});
				break;
			}
		};
	};

    
    
 
	@Override
	public int getCount() {
		return context.getEntity().getCount();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0.equals(arg1);
	}



}
