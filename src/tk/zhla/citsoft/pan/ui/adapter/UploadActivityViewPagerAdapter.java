package tk.zhla.citsoft.pan.ui.adapter;

import tk.zhla.citsoft.pan.ui.fragment.PhotoUploadingFragment;
import tk.zhla.citsoft.pan.ui.fragment.UpLoadOtherFileFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.KeyEvent;

public class UploadActivityViewPagerAdapter extends FragmentPagerAdapter {

	private Fragment[] fragments = null;

	public UploadActivityViewPagerAdapter(FragmentManager fm) {
		super(fm);
		fragments = new Fragment[2];
		fragments[0] = new PhotoUploadingFragment();
		fragments[1] = new UpLoadOtherFileFragment();
	

	}

	@Override
	public android.support.v4.app.Fragment getItem(int arg0) {
		return fragments[arg0];
	}

	@Override
	public int getCount() {
		return fragments.length;
	}

}
