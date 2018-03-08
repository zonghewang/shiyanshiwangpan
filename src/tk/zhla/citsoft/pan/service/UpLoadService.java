package tk.zhla.citsoft.pan.service;

import tk.zhla.citsoft.pan.net.ExecRunable;
import tk.zhla.citsoft.pan.net.NetworkUtils;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;


public class UpLoadService extends Service {

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		isStarting = true;
		handler.sendEmptyMessage(0);
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public void onCreate() {
		
	
		super.onCreate();
		isStarting = true;
		
	}
	
	
	private Thread t;
	
	private boolean isStarting = true;; 
	
	public Handler handler =new Handler(){
		
		public void handleMessage(android.os.Message msg) {
			if(isStarting){
				if(NetworkUtils.isNetworkAvailable(UpLoadService.this)){
					if(t==null||!t.isAlive()){
						System.out.println("�ϴ���");
						t =  new Thread(new UpLoadLocalFileBackground(UpLoadService.this, 0));
						System.out.println("��ʼ��");
						t.start();
					}
				}else {
					//Toast.makeText(UpLoadService.this, "���粻����", 0).show();
				}
				
				handler.sendEmptyMessageDelayed(0, 10000);
			}
		};
	};
	
	
	public void onDestroy() {
		isStarting =false;
		super.onDestroy();
	};
	
	public IBinder onBind(Intent intent) {
		
		return null;
	}

}
