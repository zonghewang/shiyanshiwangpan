package tk.zhla.citsoft.pan.parse;

import org.json.JSONException;
import org.json.JSONObject;

import tk.zhla.citsoft.pan.parse.entity.DeleteFileEntity;


public class DeleteFileParse implements Parse<DeleteFileEntity> {

	@Override
	public DeleteFileEntity parse(String str) {
		DeleteFileEntity entity = new DeleteFileEntity();
		
		// {"state":true,"error":"","errno":""}
		try {
			JSONObject jsonObject = new JSONObject(str);
			boolean state = jsonObject.getBoolean("state");
			String error = jsonObject.getString("error");
			String errno = jsonObject.getString("errno");
			entity = new DeleteFileEntity();
			entity.setErrno(errno);
			entity.setError(error);
			entity.setState(state);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return entity;
	}

}
