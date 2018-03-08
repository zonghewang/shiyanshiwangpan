package tk.zhla.citsoft.pan.ui.adapter;

import tk.zhla.citsoft.pan.ui.fragment.BaseFragment;
import tk.zhla.citsoft.pan.ui.fragment.FileListFragment;
import tk.zhla.citsoft.pan.ui.fragment.PhotoListFragment;
import tk.zhla.citsoft.pan.ui.fragment.SettingFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;


public class MainFragmentPagerAdapter extends FragmentPagerAdapter {

	public BaseFragment[] fragments = null;
	
	private FragmentManager fm;

	public MainFragmentPagerAdapter(FragmentManager fm) {
		super(fm);
		fragments = new BaseFragment[3];
		fragments[0] = new FileListFragment();
		fragments[1] = new PhotoListFragment();
		fragments[2] = new SettingFragment();
		this.fm = fm;

	}

	@Override
	public android.support.v4.app.Fragment getItem(int arg0) {
		return fragments[arg0];
	}

	@Override
	public int getCount() {
		return 3;
	}
	
	
}
