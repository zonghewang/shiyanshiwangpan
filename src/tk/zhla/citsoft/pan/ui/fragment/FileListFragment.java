package tk.zhla.citsoft.pan.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import tk.zhla.citsoft.pan.R;
import tk.zhla.citsoft.pan.db.DBManager;
import tk.zhla.citsoft.pan.net.RequestParam;
import tk.zhla.citsoft.pan.parse.DeleteFileParse;
import tk.zhla.citsoft.pan.parse.FileFindListParse;
import tk.zhla.citsoft.pan.parse.NewDirParse;
import tk.zhla.citsoft.pan.parse.entity.DeleteFileEntity;
import tk.zhla.citsoft.pan.parse.entity.FileDataDBEntity;
import tk.zhla.citsoft.pan.parse.entity.FileDataEntity;
import tk.zhla.citsoft.pan.parse.entity.FileDataFatherEntity;
import tk.zhla.citsoft.pan.parse.entity.FileDirDataEntity;
import tk.zhla.citsoft.pan.parse.entity.FileFindListEntity;
import tk.zhla.citsoft.pan.parse.entity.FilePathEntity;
import tk.zhla.citsoft.pan.parse.entity.FilesListEntity;
import tk.zhla.citsoft.pan.parse.entity.NewDirEntity;
import tk.zhla.citsoft.pan.share.ShareUtils;
import tk.zhla.citsoft.pan.ui.DownloadED;
import tk.zhla.citsoft.pan.ui.MainFragmentActivity;
import tk.zhla.citsoft.pan.ui.PhotoShowActivity;
import tk.zhla.citsoft.pan.ui.UpLoadActivity;
import tk.zhla.citsoft.pan.ui.adapter.FileListViewAdapter;
import tk.zhla.citsoft.pan.ui.fragment.popupwindow.MenuMore;
import tk.zhla.citsoft.pan.ui.fragment.popupwindow.PopMenu;
import tk.zhla.citsoft.pan.ui.myviews.FileDirLocationBar;
import tk.zhla.citsoft.pan.ui.myviews.FileDirLocationBar.OnFilePathChange;
import tk.zhla.citsoft.pan.utils.FileOpenUtils;
import tk.zhla.citsoft.pan.utils.ToastUtils;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.State;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

public class FileListFragment extends BaseFragment implements OnClickListener {

	private FilesListEntity entity ;
	
	private PullToRefreshListView listView ;
	
	private FileListViewAdapter adapter = null;
	
	private int offset = 0;
	
	public int isPop = 0;
	
	//最后一个后，是否在加载
	private boolean lastting =  false;
	
	private PopMenu popMenu ;
	
	private View upLoad;
	private View share;
	private View download;
	private View more_dis;
	
	private tk.zhla.citsoft.pan.ui.fragment.popupwindow.MenuMore menuMore;
	private TextView nor;
	private TextView dis;
	private boolean menuMoreShow = false;
	

	
	//文件导航
	private FileDirLocationBar bar2 = null;
	private ViewGroup group;
	private HorizontalScrollView horizontalScrollView;
	@Override
	public View createView(LayoutInflater inflater) {
		return inflater.inflate(R.layout.filelist_fragment_layout, null);
	}
	private DBManager dbManager;
	
	@Override
	public void onResume() {
		super.onResume();
		if(dbManager.getDownloadingFiles().size()>0||dbManager.getUpLoadingFiles().size()>0){
			transtr.setVisibility(View.VISIBLE);
		}
	
	}
	
	private View transtr ;
	
	
	
	@Override
	public void initViews() {
		dbManager = new DBManager(getActivity());
		dbManager.open();
		transtr = getView().findViewById(R.id.jd_dist_layout_download_view);
		getView().findViewById(R.id.jd_dist_down_gong).setOnClickListener(this);
		transtr.setOnClickListener(this);
		listView = (PullToRefreshListView) getView().findViewById(R.id.xListView);
		group = (ViewGroup) getView().findViewById(R.id.jd_locationID);
		 horizontalScrollView = (HorizontalScrollView) getView()
				.findViewById(R.id.jd_hor);
		 upLoad = getView().findViewById(R.id.net_d_upload);
		download = getView().findViewById(R.id.net_d_download);
		share = getView().findViewById(R.id.net_d_share);
		more_dis = getView().findViewById(R.id.net_d_more_dis);
		menuMore = new MenuMore(this);

		menuMore.setOnDismissListener(new OnDismissListener() {
			public void onDismiss() {
				nor.setVisibility(View.VISIBLE);
				dis.setVisibility(View.GONE);
				menuMoreShow = false;
			}
		});
		upLoad.setOnClickListener(this);
		download.setOnClickListener(this);
		share.setOnClickListener(this);
		more_dis.setOnClickListener(this);
		 popMenu = new PopMenu(this,(MainFragmentActivity) getActivity(), getView());
		 nor = (TextView) getView().findViewById(R.id.net_d_more_btn_nor);
			dis = (TextView) getView().findViewById(R.id.net_d_more_btn_dis);
		 nor.setOnClickListener(this);
	}
	
	public FilesListEntity getFilesListEntity(){
		return entity;
	}
	
	
	public void refresh(){
		listView.setState(State.REFRESHING, true);
		lianwang(getFileList(-1, 0, entity.getCid(),sortName,ascOrDesc), new OnLianWangFinishLisenter<FilesListEntity>() {
			public void onError(int errorCode) {
				listView.onRefreshComplete();
			}
			public void onFinish(FilesListEntity t) {
				if(t==null){
					return;
				}
				if(entity.getCid()==t.getCid()){
					//初始化
					entity = t;
					adapter.notifyDataSetChanged();
					listView.onRefreshComplete();
				}
			}
		});
	}

	@Override
	public void setLogic() {
		listView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				if(isSearch){
					lianwang(getRearchParam(), new OnLianWangFinishLisenter<FileFindListEntity>() {

						@Override
						public void onFinish(FileFindListEntity t) {
							if(t==null){
								return;
							}
							entity = t.getFilesListEntity();
							adapter.notifyDataSetChanged();
						}

						@Override
						public void onError(int errorCode) {
							ToastUtils.toast(getActivity(), "网络异常");
						}
					});
					return;
				}
				lianwang(getFileList(-1, 0, entity==null?0:entity.getCid(),sortName,ascOrDesc), new OnLianWangFinishLisenter<FilesListEntity>() {
					public void onError(int errorCode) {
						listView.onRefreshComplete();
					}
					public void onFinish(FilesListEntity t) {
						if(t==null){
							return;
						}
						//初始化
						entity = t;
						adapter.notifyDataSetChanged();
						//setBar();
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
				if(isSearch){
					if(entity.getCount()==entity.getFatherEntities().size()){
						lastting = false;
						return;
					}
					lianwang(getRearchParam(), new OnLianWangFinishLisenter<FileFindListEntity>() {

						@Override
						public void onFinish(FileFindListEntity t) {
							if(t==null){
								return;
							}
							entity.merge(t.getFilesListEntity());
							adapter.notifyDataSetChanged();
							lastting = false;
						}

						@Override
						public void onError(int errorCode) {
							ToastUtils.toast(getActivity(), "网络异常");
							lastting = false;
						}
					});
					return;
				}
				
				
				if(entity!=null){
					offset = entity.getFatherEntities().size();
				}
				lianwang(getFileList(-1, offset, entity.getCid(),sortName,ascOrDesc), new OnLianWangFinishLisenter<FilesListEntity>() {
					public void onError(int errorCode) {
						lastting = false;
					}
					public void onFinish(FilesListEntity t) {
						if(t==null){
							return;
						}
						entity.merge(t);
						adapter.notifyDataSetChanged();
						lastting = false;
						
					}
				});
			}
		});
	}
	

	@Override
	public void setAdapter() {
		lianwang(getFileList(-1, 0, 0,sortName,ascOrDesc), new OnLianWangFinishLisenter<FilesListEntity>() {
			public void onError(int errorCode) {
			}
			public void onFinish(FilesListEntity t) {
				if(t==null){
					return;
				}
				//初始化
				entity = t;
				setBar();
				adapter.notifyDataSetChanged();
			}
		});
		adapter = new FileListViewAdapter(this);
		listView.setAdapter(adapter);
		listView.setOnScrollListener(new PauseOnScrollListener(ImageLoader.getInstance(), false, false));
	}
	
	
	
	
	public void onItemClick(int postion){
		//先看是文件还是文件夹
		FileDataFatherEntity e =  entity.getFatherEntities().get(postion);
		
		if(e instanceof FileDirDataEntity){
			if(isSearch){
				lianwang(getFileList(-1, 0, ((FileDirDataEntity) e).getCid(),sortName,ascOrDesc), new OnLianWangFinishLisenter<FilesListEntity>() {
					public void onError(int errorCode) {
					}
					public void onFinish(FilesListEntity t) {
						if(t==null){
							return;
						}
						//初始化
						entity = t;
						setBar();
						adapter.notifyDataSetChanged();
					}
				});
				isSearch = false;
				
				return;
			}
			addBarItem((FileDirDataEntity) e);
			entity.setCid(((FileDirDataEntity) e).getCid());
			entity.setFatherEntities(new ArrayList<FileDataFatherEntity>());
			adapter.notifyDataSetChanged();
			//文件夹
//			lianwang(getFileList(-1, 0, ((FileDirDataEntity) e).getCid(),sortName,ascOrDesc), new OnLianWangFinishLisenter<FilesListEntity>() {
//				public void onError(int errorCode) {
//				}
//				public void onFinish(FilesListEntity t) {
//					if(t==null){
//						return;
//					}
//					//初始化
//					entity = t;
////					setBar();
//					adapter.notifyDataSetChanged();
//				}
//			});
			refresh();
		}else {
			FileDataEntity dataEntity = (FileDataEntity) e;
			FileDataDBEntity dataDBEntity =  dbManager.getDownloadedFile(dataEntity.getFid());
			if(dataDBEntity!=null){
				try{
				Intent i = FileOpenUtils.openFile(dataDBEntity.getPath()+"/"+dataDBEntity.getN());
				startActivity(i);
				}catch (Exception d){
					ToastUtils.toast(getActivity(), "本机无法打开此文件");
				}
				return;
			}
			FileDataDBEntity dataDBEntity2 = dbManager.getDownloadingFile(dataEntity.getFid());
			if(dataDBEntity2!=null){
				ToastUtils.toast(getActivity(), "文件正在下载中");
				return;
			}
			String end = dataEntity.getN().substring(dataEntity.getN().lastIndexOf(".") + 1,
					dataEntity.getN().length()).toLowerCase();
			 if (end.equalsIgnoreCase("jpg") || end.equalsIgnoreCase("gif") || end.equalsIgnoreCase("png")
						|| end.equalsIgnoreCase("jpeg") || end.equalsIgnoreCase("bmp")){
				 Intent	i= new Intent(getActivity(), PhotoShowActivity.class);
				 i.putExtra("entity", dataEntity);
				 startActivity(i);
			 }else{
				 ToastUtils.toast(getActivity(), "文件没有下载，点击选择下载");
			 }
			
			
			
		}
	}
	
	public void setBar(){
		bar2 = new FileDirLocationBar(getActivity(),group,horizontalScrollView,entity.getFilePathEntities());
		bar2.setOnFilePathChange(new OnFilePathChange() {
			public void filePathChange(int cid) {
				if(cid==-1){
					return;
				}
				
				if(cid==entity.getCid()){
					if(isSearch){
						//文件夹
						lianwang(getFileList(-1, 0, cid,sortName,ascOrDesc), new OnLianWangFinishLisenter<FilesListEntity>() {
							public void onError(int errorCode) {
							}
							public void onFinish(FilesListEntity t) {
								//初始化
								entity = t;
								//setBar();
								adapter.notifyDataSetChanged();
							}
						});
					}
					
				}else {
					//文件夹
					lianwang(getFileList(-1, 0, cid,sortName,ascOrDesc), new OnLianWangFinishLisenter<FilesListEntity>() {
						public void onError(int errorCode) {
						}
						public void onFinish(FilesListEntity t) {
							//初始化
							entity = t;
							//setBar();
							adapter.notifyDataSetChanged();
						}
					});
				}
				isSearch = false;
				
			}
		});
	}
	
	public void addBarItem(FileDirDataEntity entity){
		FilePathEntity entity2 = new FilePathEntity(entity.getN(), entity.getAid(), entity.getCid(), entity.getPid());
		bar2.addBtn(entity2);
	}
	
	
	//checkbox点击  
	public void checkFile(boolean isChecked,int position){
		entity.getFatherEntities().get(position).isChecked = isChecked;
		if(isChecked){
			//执行选中的操作
			if(isPop==0){
				popMenu.showPopupWindows();
			}else {
				if(isPop==1){
					//下载
					popMenu.setTitle("已经选择"+getCheckedFileDataFatherEntities().size()+"个文件或者文件夹");
				}else {
					if(entity.getFatherEntities().get(position) instanceof FileDirDataEntity){
						//分享
						entity.getFatherEntities().get(position).isChecked = false;
						adapter.notifyDataSetChanged();
						ToastUtils.toast(getActivity(), "暂不支持文件夹分享");
						if(getCheckedFileDataFatherEntities().size()==0){
							isChecked = false;
						}
						return;
					}
					if(getCheckedFileDataFatherEntities().size()>1){
						entity.getFatherEntities().get(position).isChecked = false;
						adapter.notifyDataSetChanged();
						ToastUtils.toast(getActivity(), "暂时只支持一个文件分享");
						if(getCheckedFileDataFatherEntities().size()==0){
							isChecked = false;
						}
						return;
					}
					popMenu.setTitle("已经选择"+getCheckedFileDataFatherEntities().size()+"个文件");
					
				}
			}
		}else {
			if(getCheckedFileDataFatherEntities().size()==0){
				//执行没有选中的操作
				popMenu.dis();
			}else {
				if(isPop==1){
					//下载
					popMenu.setTitle("已经选择"+getCheckedFileDataFatherEntities().size()+"个文件或者文件夹");
				}else {
					//分享
					popMenu.setTitle("已经选择"+getCheckedFileDataFatherEntities().size()+"个文件");
					
				}
			}
			
		}
	}
	
	
	
	public List<FileDataFatherEntity> getCheckedFileDataFatherEntities(){
		List<FileDataFatherEntity> dataEntities = new ArrayList<FileDataFatherEntity>();
		if(entity==null){
			return dataEntities;
		}
		for (int i = 0; i < entity.getFatherEntities().size(); i++) {
			if(entity.getFatherEntities().get(i).isChecked){
				dataEntities.add(entity.getFatherEntities().get(i));
			}
		}
		return dataEntities;
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.net_d_upload:
			Intent i = new Intent(getActivity(), UpLoadActivity.class);
			if(entity!=null){
				i.putExtra("entity", entity);
			}
			startActivity(i);
			break;
		case R.id.net_d_share:
			popMenu.showSharePopMenu();
			isPop = 2;
			break;
		case R.id.net_d_download:
			popMenu. showSharePopMenuDow();
			isPop = 1;
			break;
		case R.id.jd_dist_down_gong:
			transtr.setVisibility(View.GONE);
			break;

		case R.id.net_d_more_btn_nor:
			menuMore.show(more_dis);
			nor.setVisibility(View.GONE);
			dis.setVisibility(View.VISIBLE);
			menuMoreShow = true;
			break;
		case R.id.net_d_more_btn_dis:
			menuMore.dismiss();
			dis.setVisibility(View.GONE);
			nor.setVisibility(View.VISIBLE);
			break;

		case R.id.jd_dist_layout_download_view:
			Intent intent = new Intent(getActivity(), DownloadED.class);
			intent.putExtra("type", 0);
			startActivity(intent);
			break;

		default:
			break;
		}
	}
	
	
	
	
	private boolean isSearch = false;
	private String rearchName = "";
	
	public void search(String name){
		isSearch = true;
		rearchName = name;
		entity.getFatherEntities().clear();
		bar2.addBtn(new FilePathEntity("搜索 "+rearchName, -1, -1, -1));
		lianwang(getRearchParam(), new OnLianWangFinishLisenter<FileFindListEntity>() {

			@Override
			public void onFinish(FileFindListEntity t) {
				entity = t.getFilesListEntity();
				adapter.notifyDataSetChanged();
			}

			@Override
			public void onError(int errorCode) {
				ToastUtils.toast(getActivity(), "网络异常");
			}
		});
	}
	
	public RequestParam getRearchParam(){
		RequestParam parm = new RequestParam();
		ShareUtils shareUtils = new ShareUtils(getActivity());
		parm.url = shareUtils.getURL()+"/a1/index?ct=file&ac=search";
	
		parm.method = RequestParam.POST;
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		System.out.println(entity.getFatherEntities().size()+"");
		pairs.add(new BasicNameValuePair("offset", entity.getFatherEntities().size()+""));
		pairs.add(new BasicNameValuePair("limit", 100+""));
		pairs.add(new BasicNameValuePair("value", rearchName));
		parm.pairs = pairs;
		parm.parse = new FileFindListParse();
		
		return parm;
	}
	
	
	private String sortName = FILE_NAME;
	
	private int ascOrDesc = ASC;
	
	public void setSort(String sortName,int ascOrDesc){
		this.sortName = sortName;
		this.ascOrDesc = ascOrDesc;
		refresh();
	}

	public String getSortName() {
		return sortName;
	}


	public int getAscOrDesc() {
		return ascOrDesc;
	}

	
	
	public void createDir(String name){
		//第一步检查文件名是否存在
		//如果不存在，创建
		if(isSearch){
			ToastUtils.toast(getActivity(), "处于搜索状态，请切换到其他文件件在创建");
			return;
		}
		lianwang(getDirCreateParam(name), new OnLianWangFinishLisenter<NewDirEntity>() {

			@Override
			public void onFinish(NewDirEntity t) {
				if(t==null){
					ToastUtils.toast(getActivity(), "创建失败");
				}else{
						if("".equals(t.getError())){
							ToastUtils.toast(getActivity(), "创建成功");
							refresh();
						}else {
							ToastUtils.toast(getActivity(), t.getError());
						}
				}
			}

			@Override
			public void onError(int errorCode) {
				ToastUtils.toast(getActivity(), "网络异常");
			}
		});
	}
	
	public RequestParam getDirCreateParam(String name){
		RequestParam param = new RequestParam();
		ShareUtils shareUtils = new ShareUtils(getActivity());
		param.url = shareUtils.getURL()+"/a1/index?ct=dir&ac=add";
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		pairs.add(new BasicNameValuePair("cname", name));
		pairs.add(new BasicNameValuePair("pid", entity.getCid()+""));
		param.method = RequestParam.POST;
		param.pairs = pairs;
		param.parse = new NewDirParse();
		return param;
	}
	
	
	public void dis(){
		for (int i = 0; i < entity.getFatherEntities().size(); i++) {
			entity.getFatherEntities().get(i).isChecked = false;
		}
		popMenu.dis();
		adapter.notifyDataSetChanged();
	}
	
	public void checkAll(boolean checked){
		if(entity==null){
			return;
		}
		for (int i = 0; i < entity.getFatherEntities().size(); i++) {
			entity.getFatherEntities().get(i).isChecked = checked;
		}
		if(checked==false){
			adapter.refreshChecked();
		}else {
		adapter.notifyDataSetChanged();
		}
	}
	
	public void delete(){
		lianwang(getDeleteFileParam(), new OnLianWangFinishLisenter<DeleteFileEntity>() {

			@Override
			public void onFinish(DeleteFileEntity t) {
				if(t!=null){
					if("".equals(t.getError())){
						ToastUtils.toast(getActivity(), "删除成功");
					}else {
						ToastUtils.toast(getActivity(), t.getError());
					}
					refresh();
				}else {
					ToastUtils.toast(getActivity(), "网络错误");
				}
			}

			@Override
			public void onError(int errorCode) {
				ToastUtils.toast(getActivity(), "网络错误");
			}
		});
	}
	public RequestParam getDeleteFileParam(){
		List<FileDataFatherEntity> entities =getCheckedFileDataFatherEntities();
		RequestParam param = new RequestParam();
		ShareUtils shareUtils = new ShareUtils(getActivity());
		param.url = shareUtils.getURL()+"/a1/index?ct=file&ac=delete";
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		StringBuffer buf = new StringBuffer();
		for(int i=0;i<entities.size();i++){
			FileDataFatherEntity entity = entities.get(i);
			if(entity instanceof FileDirDataEntity){
				if(i==entities.size()-1){
					buf.append(((FileDirDataEntity)entity).getCid());
				}else {
					buf.append(((FileDirDataEntity)entity).getCid()+",");
				}
				
			}else {
				if(i==entities.size()-1){
					buf.append(((FileDataEntity)entity).getFid());
				}else {
					buf.append(((FileDataEntity)entity).getFid()+",");
				}
			}
		}
		System.out.println(buf.toString());
		param.method = RequestParam.POST;
		pairs.add(new BasicNameValuePair("fid", buf.toString()));
		param.pairs = pairs;
		param.parse = new DeleteFileParse();
		
		return param;
		
	}



	@Override
	public boolean onBack() {
		// TODO Auto-generated method stub
		if(isPop!=0||getCheckedFileDataFatherEntities().size()!=0){
			popMenu.dis();
			checkAll(false);
			return false;
		}
		
		return true;
	}
	
	public void down(){
		transtr.setVisibility(View.VISIBLE);
		for (int i = 0; i < getCheckedFileDataFatherEntities().size(); i++) {
			if(getCheckedFileDataFatherEntities().get(i) instanceof FileDataEntity){
				down(entity, (FileDataEntity) getCheckedFileDataFatherEntities().get(i));
			}else {
				down((FileDirDataEntity)getCheckedFileDataFatherEntities().get(i), 0);
			}
		}
	}

	@Override
	public void pageChange() {
		popMenu.dis();
		checkAll(false);
	}
	
}
