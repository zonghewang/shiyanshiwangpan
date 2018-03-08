package tk.zhla.citsoft.pan.ui.fragment.popupwindow;

import java.util.ArrayList;
import java.util.List;

import tk.zhla.citsoft.pan.parse.entity.FileDataFatherEntity;
import tk.zhla.citsoft.pan.ui.fragment.FileListFragment;

import android.widget.Toast;


public class PopDelete {
	public PopDelete(FileListFragment fragment) {
		List<FileDataFatherEntity> delete = new ArrayList<FileDataFatherEntity>();
		for (int i = 0; i < fragment.getFilesListEntity().getFatherEntities().size(); i++) {
			if (fragment.getFilesListEntity().getFatherEntities().get(i).isChecked) {
				delete.add(fragment.getFilesListEntity().getFatherEntities().get(i));
			}
		}
		if ( delete.size() > 0) {
			JDDialogPopupFromBottom d = new JDDialogPopupFromBottom(fragment);
		} else if (delete.size() == 0) {
			Toast.makeText(fragment.getActivity(), "请选择要删除的文件", 0).show();
		} 
	}
}
