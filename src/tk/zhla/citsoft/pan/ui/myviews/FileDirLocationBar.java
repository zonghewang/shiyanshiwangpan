package tk.zhla.citsoft.pan.ui.myviews;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import tk.zhla.citsoft.pan.R;
import tk.zhla.citsoft.pan.parse.entity.FilePathEntity;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.HorizontalScrollView;


public class FileDirLocationBar implements OnClickListener {

	

	List<Button> buttons = new ArrayList<Button>();

	private Context context = null;

	private ViewGroup viewGroup = null;

	private HorizontalScrollView hor = null;

	

	public boolean isRootDir = true;
	
	//文件路径
	private List<FilePathEntity> filePathEntities ;

	/**
	 * @param context
	 *            所在的上下文
	 * @param viewGroup
	 *            所使用的布局
	 * @param horizontalScrollView
	 *            需要一个horizontalScrollView装载viewGroup
	 * 
	 * 
	 *            需要的布局: <HorizontalScrollView android:id="@+id/hor"
	 *            android:layout_width="match_parent"
	 *            android:layout_height="40dp" android:scrollbars="none" >
	 * 
	 *            <FrameLayout android:id="@+id/locationID"
	 *            android:layout_width="wrap_content"
	 *            android:layout_height="40dp" android:orientation="horizontal"
	 *            > </FrameLayout> </HorizontalScrollView>
	 */
	public FileDirLocationBar(Context context, ViewGroup viewGroup,
			HorizontalScrollView horizontalScrollView,List<FilePathEntity> filePathEntities) {

		this.context = context;

		
		this.viewGroup = viewGroup;
		
		viewGroup.removeAllViews();
		
		this.hor = horizontalScrollView;

		this.filePathEntities = filePathEntities;

		for (int i = 0; i < filePathEntities.size(); i++) {
			Message msg = new Message();
			msg.obj = filePathEntities.get(i);
			msg.what = i;
			handler.sendMessageDelayed(msg, i*50);
		}
	}
	
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			if(msg.what!=-1){
				addButton((FilePathEntity)msg.obj);
				handler.sendEmptyMessageDelayed(-1, 30);
			}else {
				hor.scrollTo(buttons.get(buttons.size()-1).getRight(), 0);
			}
		};
	};
	
	public void addBtn(FilePathEntity item){
		Message msg = new Message();
		msg.obj = item;
		msg.what = 1;
		handler.sendMessage(msg);
	}
	
	
	private void addButton(FilePathEntity item) {
		// int index = item.getPath().lastIndexOf("/") + 1;
		// String displayName = item.getPath().substring(index);
		
		Button b = createNewButton(item.getName());
		b.setOnClickListener(this);
		addButton2View(b);
		buttons.add(b);
		b.setTag(item);
	}

	
	
	private void addButton2View(Button b) {
		FrameLayout.LayoutParams params = new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
				Gravity.LEFT);
		if(buttons.size()==0){
			params.leftMargin = -50;
		}else {
			params.leftMargin = buttons.get(buttons.size()-1).getRight() - 50;
		}

		b.setLayoutParams(params);
		viewGroup.addView(b,0);
	}

	
	private Button createNewButton(String title) {

		Button b = new Button(context);
		b.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
		b.setSingleLine();
		b.setText(title);
		b.setBackgroundResource(R.drawable.location_btn_last);
		return b;
	}

	@Override
	public void onClick(View v) {
		FilePathEntity item = (FilePathEntity) v.getTag();
		if(onFilePathChange!=null){
			onFilePathChange.filePathChange(item.getCid());
		}
		int position = -1;
		for (int i = 0; i < buttons.size(); i++) {
			if(v==buttons.get(i)){
				position = i;
			}
		}
		List<Button> canrms = new ArrayList<Button>();
		for (int i = position+1; i < buttons.size(); i++) {
			canrms.add(buttons.get(i));
		}
		
		for (int i = 0; i < canrms.size(); i++) {
			buttons.remove(canrms.get(i));
			viewGroup.removeView(canrms.get(i));
		}
	}
	
	private OnFilePathChange onFilePathChange = null;

	public void setOnFilePathChange(OnFilePathChange onFilePathChange) {
		this.onFilePathChange = onFilePathChange;
	}

	public interface OnFilePathChange {
		public void filePathChange(int cid);
	}

	

}
