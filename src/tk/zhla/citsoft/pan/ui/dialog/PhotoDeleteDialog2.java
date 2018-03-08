package tk.zhla.citsoft.pan.ui.dialog;

import java.util.ArrayList;
import java.util.List;

import tk.zhla.citsoft.pan.R;
import tk.zhla.citsoft.pan.net.DeleteRun;
import tk.zhla.citsoft.pan.parse.entity.DeleteFileEntity;
import tk.zhla.citsoft.pan.parse.entity.FileDataEntity;
import tk.zhla.citsoft.pan.parse.entity.FileDataFatherEntity;
import tk.zhla.citsoft.pan.ui.PhotoShowActivity;
import tk.zhla.citsoft.pan.utils.TimeAndSizeUtil;
import tk.zhla.citsoft.pan.utils.TimeUtils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
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
import android.widget.Toast;


public class PhotoDeleteDialog2 {
	private Dialog d = null;

	private View view = null;

	private View confirm = null;

	private Button cancel = null;

	private Context context = null;
	
	
	private FileDataEntity entity=null;

	private ListView lv;
	private List<FileDataEntity> pictures;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			
			if (msg.what==-1) {
				Toast.makeText(context, "删除失败", 0).show();
				return;
			}
			DeleteFileEntity entity = (DeleteFileEntity) msg.obj;
			if (entity.isState()) {
				Toast.makeText(context, "删除完成", 0).show();
				if (entity.getType() == 1) {
					//utils.deleteFilebyFid(entity.getFid());
				} 
				((PhotoShowActivity)context).refresh(entity.getFid()+"");
				
				
			} else {
				Toast.makeText(context, "删除失败", 0).show();
			}
			
			disDialog();
		}
	};
	public PhotoDeleteDialog2(Context context, FileDataFatherEntity entity) {
		super();
		
		this.context = context;
		this.entity=(FileDataEntity) entity;
		d = new Dialog(context);
		pictures=new ArrayList<FileDataEntity>();
		pictures.add((FileDataEntity) entity);
		view = View.inflate(context, R.layout.photo_delete, null);

		lv = (ListView) view.findViewById(R.id.photo_delete_lv);
		MyAdapter adapter = new MyAdapter();

		lv.setAdapter(adapter);

		d.setContentView(view);

		confirm = d.findViewById(R.id.photo_delete_lin);

		cancel = (Button) d.findViewById(R.id.photo_delete_cancel);

		init();
	}

	class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			if (pictures.size() > 3) {
				return 1;
			} else {
				return pictures.size();
			}

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
			convertView = View.inflate(context, R.layout.photo_delete_item,
					null);
			TextView textView = (TextView) view
					.findViewById(R.id.photo_delete_title);
			ImageView imv = (ImageView) convertView
					.findViewById(R.id.photo_delete_item_image);
			TextView name = (TextView) convertView
					.findViewById(R.id.photo_delete_item_name);
			TextView time = (TextView) convertView
					.findViewById(R.id.photo_delete_item_time);
			TextView sizes = (TextView) convertView
					.findViewById(R.id.photo_delete_item_sizes);
			TextView size = (TextView) convertView
					.findViewById(R.id.photo_delete_item_size);
			//loaderUtil.loadImage(pictures.get(position).getUrl(), imv);
			if (pictures.size() == 1) {
				textView.setText("确定要将这个文件删除？");
				name.setText(pictures.get(position).getN());
				sizes.setText("");
				size.setText(TimeAndSizeUtil.getSize(pictures.get(position).getS() + ""));
				time.setText(TimeUtils.getTime(Long.parseLong(pictures.get(position)
						.getT())));
			} else if (pictures.size() < 4 && pictures.size() > 1) {
				textView.setText("确定要将这" + pictures.size() + "项删除？");
				name.setText(pictures.get(position).getN());
				size.setText("");
				sizes.setText(TimeAndSizeUtil.getSize(pictures.get(position).getS() + ""));
				time.setText("");
			} else {
				textView.setText("确定要将这些文件删除？");
				name.setText(pictures.get(0).getN() + "等" + pictures.size()
						+ "项");
				sizes.setText("");
				size.setText("");
				time.setText("");
			}
			return convertView;
		}
	}

	private void init() {

		Window dialogWindow = d.getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		dialogWindow.setGravity(Gravity.LEFT | Gravity.BOTTOM);
		lp.width = LayoutParams.MATCH_PARENT;
		lp.height = LayoutParams.WRAP_CONTENT;
		// lp.x = 0;
		// lp.y = getWindowHeight();
		dialogWindow
				.setBackgroundDrawableResource(R.drawable.background_dialog);

		final int height = lp.height;

		confirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showDialog();
					String s = entity.getFid()+"";
					//添加删除事件
					DeleteRun deleteRun = new DeleteRun(1,entity
							.getFid(), 0, context, handler,
							entity.getPid());
					new Thread(deleteRun).start();
				dialogAnimation(d, view, height, getWindowHeight(), true);

			}
		});
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialogAnimation(d, view, height, getWindowHeight(), true);
			
			}
		});
		d.show();
		dialogAnimation(d, view, getWindowHeight(), height, false);

	}

	Dialog dialog = null;

	public void showDialog() {

		dialog = new Dialog(context);
		Window dialogWindow = dialog.getWindow();
		dialogWindow
				.setBackgroundDrawableResource(R.drawable.background_dialog);
		dialog.setContentView(R.layout.photo_delete_dialog);
		ImageView imageView = (ImageView) dialog
				.findViewById(R.id.photo_delete_diaog_image);
		imageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				disDialog();
			}
		});
		dialog.show();
	}

	public void disDialog() {
		if (dialog != null) {
			dialog.dismiss();
		}
	}

	private int getWindowHeight() {
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay()
				.getMetrics(dm);
		return dm.widthPixels;
	}

	private void dialogAnimation(final Dialog d, View v, int from, int to,
			boolean needDismiss) {

		Animation anim = new TranslateAnimation(0, 0, from, to);
		anim.setFillAfter(true);
		anim.setDuration(600);
		if (needDismiss) {
			anim.setAnimationListener(new AnimationListener() {

				public void onAnimationStart(Animation animation) {
				}

				public void onAnimationRepeat(Animation animation) {
				}

				public void onAnimationEnd(Animation animation) {
					d.dismiss();
				}
			});

		}

		v.startAnimation(anim);
	}

	private OnConfirmListener onConfirmListener = null;

	public interface OnConfirmListener {
		public void confirm();
	}

	public void setOnConfirmListener(OnConfirmListener onConfirmListener) {
		this.onConfirmListener = onConfirmListener;
	}

}
