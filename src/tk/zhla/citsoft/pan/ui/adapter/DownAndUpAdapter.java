package tk.zhla.citsoft.pan.ui.adapter;

import java.io.File;
import java.util.List;

import tk.zhla.citsoft.pan.R;
import tk.zhla.citsoft.pan.parse.entity.FileDataDBEntity;
import tk.zhla.citsoft.pan.utils.FindType;
import tk.zhla.citsoft.pan.utils.TimeAndSizeUtil;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

public class DownAndUpAdapter extends BaseAdapter {
	private List<FileDataDBEntity> files;
	private Context context;
	private ImageLoader util= ImageLoader.getInstance();

	public DownAndUpAdapter(Context context, List<FileDataDBEntity> files) {
		this.files = files;
		this.context = context;
	
	}

	public class ViewHoder {
		public ImageView imageView;
		public TextView name;
		public TextView size;
		public TextView time;
		public CheckBox box;

	}

	@Override
	public int getCount() {

		return files.size();

	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHoder hoder = null;
		if (convertView == null) {
			hoder = new ViewHoder();
			convertView = View.inflate(context,
					R.layout.jd_downlond_and_upload_item, null);
			hoder.imageView = (ImageView) convertView
					.findViewById(R.id.dowup_item_image);
			hoder.name = (TextView) convertView
					.findViewById(R.id.dowup_item_name);
			hoder.time = (TextView) convertView
					.findViewById(R.id.dowup_item_time);
			hoder.size = (TextView) convertView
					.findViewById(R.id.dowup_item_size);
			hoder.box = (CheckBox) convertView
					.findViewById(R.id.dowup_item_btn);
			hoder.box.setTag(position);
			convertView.setTag(hoder);
		} else {
			hoder = (ViewHoder) convertView.getTag();
		}
		hoder.box.setVisibility(View.GONE);
		//util.loadImage(files.get(position).getU(), hoder.imageView);
		
		 hoder.imageView
		 .setImageResource(FindType.findImage(files.get(position).getPath()));
		hoder.name.setText(files.get(position).getN());
		
			File f =  new File(files.get(position).getPath());
		if(f.isFile()){
			hoder.size.setText(TimeAndSizeUtil.getSize(f.length()+""));
			hoder.time.setText(TimeAndSizeUtil.getTime(f.lastModified()/1000+""));
		} else{
			hoder.size.setText(TimeAndSizeUtil.getSize(files.get(position).getS()+""));
			hoder.time.setText(TimeAndSizeUtil.getTime(files.get(position).getT()));
		}
		

		return convertView;
	}


	public void setFiles(List<FileDataDBEntity> files) {
		this.files = files;
	}
	
	
	
}
