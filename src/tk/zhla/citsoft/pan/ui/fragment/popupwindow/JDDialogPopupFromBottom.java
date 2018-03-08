package tk.zhla.citsoft.pan.ui.fragment.popupwindow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import tk.zhla.citsoft.pan.R;
import tk.zhla.citsoft.pan.parse.entity.FileDataEntity;
import tk.zhla.citsoft.pan.parse.entity.FileDataFatherEntity;
import tk.zhla.citsoft.pan.parse.entity.FileDirDataEntity;
import tk.zhla.citsoft.pan.ui.fragment.FileListFragment;
import tk.zhla.citsoft.pan.utils.FindType;
import tk.zhla.citsoft.pan.utils.TimeAndSizeUtil;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Display;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class JDDialogPopupFromBottom extends Dialog {

	public JDDialogPopupFromBottom(FileListFragment fragment) {
		super(fragment.getActivity());
		for (int i = 0; i < fragment.getFilesListEntity().getFatherEntities()
				.size(); i++) {
			if (fragment.getFilesListEntity().getFatherEntities().get(i).isChecked) {
				items.add(fragment.getFilesListEntity().getFatherEntities()
						.get(i));
			}
		}
		this.fragment = fragment;

		view = View
				.inflate(fragment.getActivity(), R.layout.jd_pop_clear, null);

		lv = (ListView) view.findViewById(R.id.delete_lv);
		MyAdapter adapter = new MyAdapter();

		lv.setAdapter(adapter);

		setContentView(view);

		delete = (View) findViewById(R.id.settingConfirmClear);

		cancel = (Button) findViewById(R.id.settingCancelClear);
		delete.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				JDDialogPopupFromBottom.this.fragment.delete();
				dismiss();
			}
		});
		cancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				dismiss();
			}
		});

		init();
	}

	private View view = null;

	private View delete = null;

	private Button cancel = null;

	private ListView lv;

	DisplayImageOptions options;

	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

	protected ImageLoader imageLoader = ImageLoader.getInstance();

	List<FileDataFatherEntity> items = new ArrayList<FileDataFatherEntity>();

	private FileListFragment fragment;

	class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			if (items.size() > 4) {
				return 1;
			}
			return items.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = View.inflate(fragment.getActivity(),
					R.layout.delete_item, null);
			ImageView imv = (ImageView) convertView
					.findViewById(R.id.delete_item_image);
			TextView name = (TextView) convertView
					.findViewById(R.id.delete_item_name);
			TextView time = (TextView) convertView
					.findViewById(R.id.delete_item_time);
			TextView size = (TextView) convertView
					.findViewById(R.id.delete_item_size);
			if (items.size() > 4) {
				name.setText(items.get(items.size() - 1).getN() + " 等"
						+ items.size() + "项");
				return convertView;
			}
			FileDataFatherEntity entity = items.get(position);

			if (entity instanceof FileDirDataEntity) {
				// 文件夹
				imv.setImageResource(FindType.findDir(entity.getN()));
			} else {
				// 文件
				FileDataEntity entity2 = (FileDataEntity) entity;
				if (TextUtils.isEmpty(entity2.getU())) {
					imageLoader.displayImage(
							"drawable://" + FindType.findImage(entity2.getN()),
							imv, options, animateFirstListener);
				} else {
					imageLoader.displayImage(entity2.getU().trim() + "&tsha1="
							+ entity2.getShal() + "_100_100", imv, options,
							animateFirstListener);
				}
				size.setText(TimeAndSizeUtil.getSize(entity2.getS()+""));
			}

			name.setText(items.get(position).getN());

			time.setText(TimeAndSizeUtil.getTime(items.get(position).getT()));
			return convertView;
		}

	}

	private static class AnimateFirstDisplayListener extends
			SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections
				.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {
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

	private void init() {

		Window window = getWindow();
		window.setGravity(Gravity.BOTTOM);
		window.setWindowAnimations(R.style.mystyle); // 添加动画
		show();
		WindowManager windowManager = ((Activity) fragment.getActivity())
				.getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		WindowManager.LayoutParams params = getWindow().getAttributes();
		params.width = (int) (display.getWidth());
		getWindow().setAttributes(params);
		window.setBackgroundDrawableResource(R.drawable.background_dialog);

	}



}
