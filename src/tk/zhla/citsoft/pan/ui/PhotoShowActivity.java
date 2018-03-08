package tk.zhla.citsoft.pan.ui;

import tk.zhla.citsoft.pan.R;
import tk.zhla.citsoft.pan.net.ExecRunable;
import tk.zhla.citsoft.pan.net.HttpUtils;
import tk.zhla.citsoft.pan.net.RequestParam;
import tk.zhla.citsoft.pan.parse.FileListParse;
import tk.zhla.citsoft.pan.parse.entity.FileDataEntity;
import tk.zhla.citsoft.pan.parse.entity.FilesListEntity;
import tk.zhla.citsoft.pan.share.ShareUtils;
import tk.zhla.citsoft.pan.ui.adapter.PhotoViewPager_Adapter;
import tk.zhla.citsoft.pan.ui.fragment.popupwindow.PhotoPopMenuShow;
import tk.zhla.citsoft.pan.utils.TimeUtils;
import tk.zhla.citsoft.pan.utils.ToastUtils;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;

import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXTextObject;
import com.tencent.mm.sdk.openapi.WXWebpageObject;
import com.tencent.mm.sdk.platformtools.BackwardSupportUtil.BitmapFactory;
import com.tencent.mm.sdk.platformtools.Util;

public class PhotoShowActivity extends Activity {
	private PhotoPopMenuShow menuShow = null;
	private ViewPager page = null;
	private int currpag = 0;
	
	private FileDataEntity dataEntity;
	
	private FilesListEntity entity;
	
	// ����
	public static IWXAPI api;

	private ProgressDialog pd;


	public boolean isDelete() {
		return delete;
	}
	public void setDelete(boolean delete) {
		this.delete = delete;
	}
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		delete =false;
		setContentView(R.layout.photo_show);
		Intent intent = getIntent();
		currpag = intent.getIntExtra("position", -1);
		dataEntity  = (FileDataEntity) intent.getSerializableExtra("entity");
		
		page = (ViewPager) findViewById(R.id.photo_show_viewpager);
	
		api = MainFragmentActivity.api;
		
		page.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				currpag = arg0;
				menuShow.setTitle(TimeUtils.getTime(Long.parseLong(entity.getFatherEntities().get(currpag).getT())*1000),
						(currpag + 1) + "/" + entity.getFatherEntities().size());
				for (int i = 0; i < entity.getFatherEntities().size(); i++) {
					entity.getFatherEntities().get(i).isChecked = false;
				} 
				entity.getFatherEntities().get(arg0).isChecked=true;
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
		
		pd = new ProgressDialog(this);
		pd.setTitle("���ڼ���");
		pd.show();
		
		//
		downFromWeb(0);
	}
	/**
	 * ʱ������
	 */
	public static final String USER_PTIME = "user_ptime";
	/**
	 * ��С����
	 */
	public static final String FILE_SIZE="file_size";
	/**
	 * �ļ���
	 */
	public static final String FILE_NAME = "file_name";
	
	/**
	 * ����
	 */
	public static final int DESC = 1;
	
	/**
	 * ����
	 */
	public static final int ASC = 0;
	
	public void downFromWeb(final int offset){
		ExecRunable.execRun(new Thread(){
			public void run() {
			ShareUtils utils = new ShareUtils(PhotoShowActivity.this);
			String url = utils.getURL()+"/a1/index?ct=list&aid=1&cid="+dataEntity.getPid()+"&o="+USER_PTIME+"&asc="+ASC+"&offset="+offset+"&limit=100&type="+2;
			
			try {
				
				String result = HttpUtils.GetStringForHttpGet(utils.getCookieUtil(), null, url, 1);
				FileListParse parse = new FileListParse();
				FilesListEntity entity = parse.parse(result);
				Message msg = new Message();
				if(offset==0){
				msg.what = 1;
				}else {
					msg.what = 2;
				}
				msg.obj = entity;
				handler.sendMessage(msg);
				
			} catch (Exception e) {
				e.printStackTrace();
				
				handler.sendEmptyMessage(-1);
			}
			
			};
		});
	}
	public RequestParam getFileList(int type,int offset,int cid,String order,int ascOrdesc){
		RequestParam param = new RequestParam();
		ShareUtils utils = new ShareUtils(this);
		if(type==-1){
			param.url = utils.getURL()+"/a1/index?ct=list&aid=1&cid="+cid+"&o="+order+"&asc="+ascOrdesc+"&offset="+offset+"&limit=100";
		}else {
			param.url = utils.getURL()+"/a1/index?ct=list&aid=1&cid="+cid+"&o="+order+"&asc="+ascOrdesc+"&offset="+offset+"&limit=100&type="+type;
		}
		param.method = RequestParam.GET;
		param.parse = new FileListParse();
		return param;
	}
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(PhotoShowActivity.this==null){
				return;
			}
			pd.dismiss();
			switch (msg.what) {
			case -1://����ʧ��
				
				break;
			case 1:
				//���سɹ�
				
				entity = (FilesListEntity) msg.obj;
				getCurrentPosition();
				if(currpag!=-1){
					setAdapter();
				}
				
				if(entity.getCount()>entity.getFatherEntities().size()){
					downFromWeb(entity.getFatherEntities().size());
				}
				
				break;
			case 2:
				if(adapter==null){
					if(getCurrentPosition()!=-1){
						setAdapter();
					}
				}
				entity.merge((FilesListEntity) msg.obj);
				if(entity.getCount()>entity.getFatherEntities().size()){
					downFromWeb(entity.getFatherEntities().size());
				}
				if(adapter!=null)
				adapter.notifyDataSetChanged();
			default:
				break;
			}
		};
	};
	
	private PhotoViewPager_Adapter adapter ;
	
	
	
	public FilesListEntity getEntity() {
		return entity;
	}
	
	public void setAdapter(){
		menuShow = new PhotoPopMenuShow(this);
		adapter = new PhotoViewPager_Adapter(this, menuShow);
		page.setAdapter(adapter);
		
		getCurrentPosition();
		page.setCurrentItem(currpag);
		menuShow.setTitle(TimeUtils.getTime(Long.parseLong(entity.getFatherEntities().get(currpag).getT())*1000),
				(currpag + 1) + "/" + entity.getCount());
	}
	
	public int getCurrentPosition(){
		for (int i = 0; i < entity.getFatherEntities().size(); i++) {
			FileDataEntity fileDataEntity = (FileDataEntity) entity.getFatherEntities().get(i);
			if(dataEntity.getFid()==fileDataEntity.getFid()){
				currpag=i;
			}
		}
		return currpag;
	}
	
	public static boolean delete = false;
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode==KeyEvent.KEYCODE_BACK) {
			if(delete){
				setResult(1);
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	public int getCurrpag() {
		return currpag;
	}
	
	public void refresh(String fid){
		for (int i = 0; i <entity.getFatherEntities().size(); i++) {
			if((((FileDataEntity)(entity.getFatherEntities().get(i))).getFid()+"").equals(fid)){
				currpag--;
				
				delete = true;
				entity.getFatherEntities().remove(i);
				entity.setCount(entity.getCount()-1);
				if(entity.getCount()==0){
					finish();
					return;
				}
				menuShow = new PhotoPopMenuShow(this);
				page.setAdapter(new PhotoViewPager_Adapter(this, menuShow));
				menuShow.setTitle(TimeUtils.getTime(Long.parseLong(entity.getFatherEntities().get(currpag).getT())*1000),
						(currpag + 1) + "/" + entity.getCount());
				page.setCurrentItem(currpag);
				
				
				return;
			}
		}
		
		
	}
	
	/** ������ϵ�� */
	public void sendSMS(String smsBody) {

		Uri smsToUri = Uri.parse("smsto:");

		Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);

		intent.putExtra("sms_body", smsBody);

		startActivity(intent);

	}
	/** �������� */
	public void send(String msg1, String name) {
		// ��ʼ��һ��WXTextObject����
		WXTextObject textObj = new WXTextObject();
		textObj.text = msg1;

		// ��WXTextObject�����ʼ��һ��WXMediaMessage����
		WXMediaMessage msg = new WXMediaMessage();
		msg.mediaObject = textObj;
		// �����ı����͵���Ϣʱ��title�ֶβ�������
		// msg.title = "Will be ignored";
		msg.description = name;
		// WXWebpageObject webpage = new WXWebpageObject();
		// webpage.webpageUrl = msg1;
		// WXMediaMessage msg = new WXMediaMessage(webpage);
		// msg.title = "" + name;
		// msg.description = "���Ǹ���������   " + msg1;
		// Bitmap thumb = BitmapFactory.decodeResource(getResources(),
		// R.drawable.ic_launcher);
		// msg.thumbData = Util.bmpToByteArray(thumb, true);
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = buildTransaction("webpage");
		req.message = msg;
		req.scene = SendMessageToWX.Req.WXSceneSession;
		api.sendReq(req);
	}

	private String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis())
				: type + System.currentTimeMillis();
	}

	/** ��������Ȧ */
	public void sendFriends(String msg1, String name) {
		// WXWebpageObject webpage = new WXWebpageObject();
		// webpage.webpageUrl = msg1;
		// WXMediaMessage msg = new WXMediaMessage(webpage);
		// msg.title = "";
		// msg.description = "����һ����������   " + msg1;
		// Bitmap thumb = BitmapFactory.decodeResource(getResources(),
		// R.drawable.ic_launcher);
		// msg.thumbData = Util.bmpToByteArray(thumb, true);
		WXTextObject textObj = new WXTextObject();
		textObj.text = msg1;

		// ��WXTextObject�����ʼ��һ��WXMediaMessage����
		WXMediaMessage msg = new WXMediaMessage();
		msg.mediaObject = textObj;
		// �����ı����͵���Ϣʱ��title�ֶβ�������
		// msg.title = "Will be ignored";
		msg.description = name;
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = buildTransaction("webpage");
		req.message = msg;
		req.scene = SendMessageToWX.Req.WXSceneTimeline;
		api.sendReq(req);
	}
	/** �������� */
	public void sendOther(String msg) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_SUBJECT, "fenxiang");
		intent.putExtra(Intent.EXTRA_TEXT, msg);
		startActivity(Intent.createChooser(intent, getTitle()));
	}
	
	/** �������� */
	public void sendEmail(String msg1,String name) {
		 //����Intent ����
        final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND); 
        //�����ı���ʽ
        emailIntent.setType("plain/text"); 
        //���öԷ��ʼ���ַ
      //  emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{[email={[email=email=%7B%C3%97%C3%97%C3%97%C3%97%C3%97%C3%97@gmail.com]������������@gmail.com[/email]]������������@gmail.com[/email]}); 
        //���ñ�������
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, name); 
        //�����ʼ��ı�����
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, msg1); 
        //����һ���µ�ACTIVITY,"Sending mail..."�����������ACTIVITY�ĵȴ�ʱ��ʱ����ʾ������
        startActivity(Intent.createChooser(emailIntent, "FYX Sending mail..."));
	}
	/**
	@param contentҪ������ַ���
	**/
	 @SuppressLint("NewApi")
	public void setClipBoard(String content) {
	  int currentapiVersion = android.os.Build.VERSION.SDK_INT;
	  if (currentapiVersion >= android.os.Build.VERSION_CODES.HONEYCOMB) {
	    android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
	   android.content.ClipData clip = android.content.ClipData.newPlainText("label", content);
	   clipboard.setPrimaryClip(clip);
	  } else {
	   android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
	   clipboard.setText(content);
	  }
	 }
	 
	 @Override
	protected void onDestroy() {
		 menuShow.dis();
		super.onDestroy();
	}
	 
	 

}
