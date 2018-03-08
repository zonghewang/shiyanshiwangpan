package tk.zhla.citsoft.pan.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import tk.zhla.citsoft.pan.R;
import tk.zhla.citsoft.pan.parse.entity.FileDataEntity;
import tk.zhla.citsoft.pan.parse.entity.FilesListEntity;
import tk.zhla.citsoft.pan.ui.PhotoShowActivity;
import tk.zhla.citsoft.pan.ui.adapter.CameraListGrid_Adapter;
import tk.zhla.citsoft.pan.ui.fragment.popupwindow.CameraPopMenu;
import tk.zhla.citsoft.pan.ui.fragment.popupwindow.CameraPopMenuDelete;
import tk.zhla.citsoft.pan.ui.fragment.popupwindow.CameraPopMenuDown;
import tk.zhla.citsoft.pan.ui.fragment.popupwindow.CameraPopMenuShare;
import tk.zhla.citsoft.pan.utils.DateUtils;
import tk.zhla.citsoft.pan.utils.ToastUtils;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.State;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

public class PhotoListFragment extends BaseFragment  implements OnClickListener{

	private FilesListEntity entity;

	private View noEmptyView;

	private View enptyView;
	
	private CameraPopMenu menu = null;
	private LinearLayout send, download, delete;
	private CameraPopMenuDown menuDown = null;
	private CameraPopMenuDelete menuDelete = null;
	private CameraPopMenuShare menuShare = null;

	private PullToRefreshListView listView;

	private CameraListGrid_Adapter adapter;

	// 最后一个后，是否在加载
	private boolean lastting = false;

	private int offset = 0;
	
	private boolean isPop = false;

	@Override
	public View createView(LayoutInflater inflater) {
		// TODO Auto-generated method stub
		downFile();
		return inflater.inflate(R.layout.camera_main, null);
	}

	@Override
	public void initViews() {
		noEmptyView = getView().findViewById(R.id.camera_main_noempty);
		enptyView = getView().findViewById(R.id.camera_main_empty);
		listView = (PullToRefreshListView) getView().findViewById(
				R.id.xListView);
		
		download = (LinearLayout) getView().findViewById(
				R.id.camera_main_bn_down);
		send = (LinearLayout) getView().findViewById(R.id.camera_main_bn_send);
		delete = (LinearLayout) getView().findViewById(R.id.camera_main_bn_del);
		download.setOnClickListener(this);
		delete.setOnClickListener(this);
		send.setOnClickListener(this);
		
		menuDown = new CameraPopMenuDown(getActivity(), this);
		menuDelete = new CameraPopMenuDelete(getActivity(), this);
		menuShare = new CameraPopMenuShare(getActivity(), 
				getActivity(), this);

		menu = new CameraPopMenu(getActivity(), getActivity(), this);
	}

	
	
	
	public List<FileDataEntity> getEntity() {
		List<FileDataEntity> entities = new ArrayList<FileDataEntity>();
		if(entity!=null)
		for (int i = 0; i < entity.getFatherEntities().size(); i++) {
			entities.add((FileDataEntity) entity.getFatherEntities().get(i));
		}
		
		return entities;
	}

	@Override
	public void setLogic() {
		listView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				lianwang(getFileList(2, 0, 0,USER_PTIME,ASC),
						new OnLianWangFinishLisenter<FilesListEntity>() {
							public void onError(int errorCode) {
								listView.onRefreshComplete();
							}

							public void onFinish(FilesListEntity t) {
								// 初始化
								entity = t;
								list = getTileAndFilesListEntity();
								adapter.notifyDataSetChanged();
								listView.onRefreshComplete();
							}
						});
			}
		});
		listView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

			@Override
			public void onLastItemVisible() {
				if (lastting) {

					return;
				}
				lastting = true;
				if (entity != null) {
					offset = entity.getFatherEntities().size();
				}
				lianwang(getFileList(2, offset, 0,USER_PTIME,ASC),
						new OnLianWangFinishLisenter<FilesListEntity>() {
							public void onError(int errorCode) {
								lastting = false;
							}

							public void onFinish(FilesListEntity t) {
								entity.merge(t);
								list = getTileAndFilesListEntity();
								adapter.notifyDataSetChanged();
								lastting = false;
								showBackGroud();

							}
						});
			}
		});
	}
	
	public void showBackGroud(){
		if(entity==null||entity.getFatherEntities()==null||entity.getFatherEntities().size()==0){
			enptyView.setVisibility(View.VISIBLE);
			noEmptyView.setVisibility(View.GONE);
		}else {
			enptyView.setVisibility(View.GONE);
			noEmptyView.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void setAdapter() {
		
		adapter = new CameraListGrid_Adapter(this);
		listView.setAdapter(adapter);
		listView.setOnScrollListener(new PauseOnScrollListener(ImageLoader.getInstance(), true, true));
		lianwang(getFileList(2, 0, 0,USER_PTIME,ASC),
				new OnLianWangFinishLisenter<FilesListEntity>() {
					public void onError(int errorCode) {
					}

					public void onFinish(FilesListEntity t) {
						// 初始化
						entity = t;
						list = getTileAndFilesListEntity();
						adapter.notifyDataSetChanged();
						showBackGroud();
						
					}
		});
	}

	public FilesListEntity getFilesListEntity() {
		return entity;
	}

	private List list = new ArrayList();
	public List getList() {
		return list;
	}
	
	@Override
	public void onResume() {
		
		
		super.onResume();
	}
	
	public List getTileAndFilesListEntity() {
		List list = new ArrayList();
		String lastTime = null;
		if(getFilesListEntity()==null){
			return list;
		}
		List<FileDataEntity> entities = new ArrayList<FileDataEntity>();
		int size = 0;
		for (int i = 0; i < getFilesListEntity().getFatherEntities().size(); i++) {
			FileDataEntity entity = (FileDataEntity) getFilesListEntity()
					.getFatherEntities().get(i);
			if (i == 0) {
				lastTime = DateUtils.parse2Date(entity.getT());
				list.add(lastTime);
				entities.add(entity);
				size=1;
			} else {
				if (DateUtils.parse2Date(entity.getT()).equals(
						lastTime)) {
					entities.add(entity);
					size++;
					if(size==3||i==getFilesListEntity().getFatherEntities().size()-1){
						list.add(entities);
						entities = new ArrayList<FileDataEntity>();
						size=0;
					}
				} else {
					if(size>0){
						list.add(entities);
						entities = new ArrayList<FileDataEntity>();
					}
					lastTime = DateUtils.parse2Date(entity.getT());
					list.add(lastTime);
					entities.add(entity);
					size=1;
				}
			}
		}
		return list;

	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		//ToastUtils.toast(getActivity(), requestCode+"-----"+resultCode);
		if(requestCode==-1){
			if(resultCode==1){
				refreshWithDataChange();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public boolean onBack() {
		if (PhotoListFragment.this.menuDown.havePop()) {
			PhotoListFragment.this.menuDown.dis();
			return false;
		}
		if (PhotoListFragment.this.menuDelete.havePop()) {
			PhotoListFragment.this.menuDelete.dis();
			return false;
			
		}
		if (PhotoListFragment.this.menuShare.havePop()) {
			PhotoListFragment.this.menuShare.dis();
			return false;
		}
		if(menu.havePop()){
			menu.dis();
			return false;
		}
		return true;
	}
	
	public void refresh(){
		adapter.notifyDataSetChanged();
	}
	
	public void refreshWithDataChange(){
		listView.setState(State.REFRESHING, true);
		lianwang(getFileList(2, 0, 0,USER_PTIME,ASC),
				new OnLianWangFinishLisenter<FilesListEntity>() {
					public void onError(int errorCode) {
					}

					public void onFinish(FilesListEntity t) {
						// 初始化
						entity = t;
						list = getTileAndFilesListEntity();
						adapter.notifyDataSetChanged();
						showBackGroud();
					}
		});
	}

	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.camera_main_bn_down:
			if (!PhotoListFragment.this.menuDown.havePop()) {
				PhotoListFragment.this.menuDown.showPopupWindows(v);
				adapter.setChecked(true);
				adapter.notifyDataSetChanged();
			}
			break;
		case R.id.camera_main_bn_del:
			if (!PhotoListFragment.this.menuDelete.havePop()) {
				PhotoListFragment.this.menuDelete.showPopupWindows(v);
				adapter.setChecked(true);
				adapter.notifyDataSetChanged();
			}
			break;
		case R.id.camera_main_bn_send:
			if (!PhotoListFragment.this.menuShare.havePop()) {
				PhotoListFragment.this.menuShare.showPopupWindows(v);
				adapter.setChecked(true);
				adapter.notifyDataSetChanged();
			}
			break;
		default:
			break;
		}
	}
	
	public void dis(){
		isChecked = false;
		if(entity==null){
			return;
		}
		adapter.setChecked(false);
		setCheckedAll(false);
		adapter.refreshChecked();
	}
	public void setCheckedAll(boolean flag){
		if(entity!=null)
			for (int i = 0; i < entity.getFatherEntities().size(); i++) {
				FileDataEntity entity1 = (FileDataEntity) entity.getFatherEntities().get(i);
				entity1.isChecked = flag;
		}
	}

	@Override
	public void pageChange() {
		int count = 0;
		if (PhotoListFragment.this.menuDown.havePop()) {
			PhotoListFragment.this.menuDown.dis();
			count ++;
		}
		if (PhotoListFragment.this.menuDelete.havePop()) {
			PhotoListFragment.this.menuDelete.dis();
			count++;
			
		}
		if (PhotoListFragment.this.menuShare.havePop()) {
			PhotoListFragment.this.menuShare.dis();
			count++;
		}
		if(menu.havePop()){
			menu.dis();
			count++;
		}
		if(count==0){
			dis();
		}
	}
	
	public void showMenu(){
		if(!menu.havePop()){
			menu.showPopupWindows(download);
		}
	}
	
	public void check(FileDataEntity entity,boolean isChecked){
		entity.isChecked = isChecked;
		if (PhotoListFragment.this.menuDown.havePop()) {
			menuDown.check();
			return;
		}
		if (PhotoListFragment.this.menuDelete.havePop()) {
			menuDelete.check();
			return;
			
		}
		if (PhotoListFragment.this.menuShare.havePop()) {
			menuShare.check();
			return;
		}
		if(getCheckedFileDataEntity().size()==0){
			this.isChecked = false;
			dis();
			if(menu.havePop()){
				menu.dis();
			}
		}
		
	}

	
	public List<FileDataEntity> getCheckedFileDataEntity(){
		List<FileDataEntity> dataEntities = new ArrayList<FileDataEntity>();
		if(entity!=null)
		for (int i = 0; i < entity.getFatherEntities().size(); i++) {
			FileDataEntity entity1 = (FileDataEntity) entity.getFatherEntities().get(i);
			if(entity1.isChecked){
				dataEntities.add(entity1);
			}
		}
		return dataEntities;
	}
	public boolean isChecked = false;
	
}
