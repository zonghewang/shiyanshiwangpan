package tk.zhla.citsoft.pan.parse;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tk.zhla.citsoft.pan.parse.entity.FileDataEntity;
import tk.zhla.citsoft.pan.parse.entity.FileDataFatherEntity;
import tk.zhla.citsoft.pan.parse.entity.FileDirDataEntity;
import tk.zhla.citsoft.pan.parse.entity.FilePathEntity;
import tk.zhla.citsoft.pan.parse.entity.FilesListEntity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class FileListParse implements Parse<FilesListEntity>{

	public  FilesListEntity parse(String result) {
		FilesListEntity entity = null;
		List<FileDataFatherEntity> fatherEntities = new ArrayList<FileDataFatherEntity>();
		List<FilePathEntity> pathEntities = new ArrayList<FilePathEntity>();
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(result);
			int count = jsonObject.getInt("count");
			String order = jsonObject.getString("order");
			int uid = jsonObject.getInt("uid");
			boolean state = jsonObject.getBoolean("state");
			String error = jsonObject.getString("error");
			String errno = jsonObject.getString("errno");
			String time = jsonObject.getString("time");
			int offset = jsonObject.getInt("offset");
			int limit = jsonObject.getInt("limit");
			int aid = jsonObject.getInt("aid");
			int cid1 = jsonObject.getInt("cid");
			int is_asc = jsonObject.getInt("is_asc");
			int star = jsonObject.getInt("star");
			int is_share = jsonObject.getInt("is_share");
			int type = jsonObject.getInt("type");
			JSONArray jsonArray = jsonObject.getJSONArray("data");
			String r = jsonObject.getString("data");
			Gson gson = new Gson();
			List<Object> temp = gson.fromJson(r, new TypeToken<Object>() {
			}.getType());
			for (int i = 0; i < temp.size(); i++) {
				JSONObject o = (JSONObject) jsonArray.get(i);
				if (temp.get(i).toString().contains("cid")) {
					FileDirDataEntity dir = new FileDirDataEntity();
					dir.setCid(o.getInt("cid"));
					dir.setAid(o.getInt("aid"));
					dir.setPid(o.getInt("pid"));
					dir.setN(o.getString("n"));
					dir.setCc(o.getString("cc"));
					dir.setM(o.getInt("m"));

					dir.setPc(o.getString("pc"));
					dir.setT(o.getString("t"));
					fatherEntities.add(dir);
				} else if (temp.get(i).toString().contains("fid")) {
					FileDataEntity file = new FileDataEntity();
					file.setFid(o.getInt("fid"));
					file.setAid(o.getInt("aid"));
					file.setPid(o.getInt("pid"));
					file.setN(o.getString("n"));
					file.setS(o.getInt("s"));
					file.setPc(o.getString("pc"));
					file.setM(o.getInt("m"));
					file.setT(o.getString("t"));
					if (temp.get(i).toString().contains("u"))
						try {
							file.setU(o.getString("u"));
						} catch (Exception e) {
							e.printStackTrace();
						}
					if (temp.get(i).toString().contains("sha1"))
						file.setShal(o.getString("sha1"));
					fatherEntities.add(file);
				}
			}
			JSONArray paths = jsonObject.getJSONArray("path");
			for (int i = 0; i < paths.length(); i++) {
				JSONObject j = (JSONObject) paths.get(i);
				FilePathEntity path = new FilePathEntity();
				path.setName(j.getString("name"));
				path.setAid(j.getInt("aid"));
				path.setCid(j.getInt("cid"));
				path.setPid(j.getInt("pid"));
				pathEntities.add(path);

			}
			entity = new FilesListEntity(count, order, uid, state, error,
					errno, time, offset, limit, aid, cid1, is_asc, star,
					is_share, type, fatherEntities, pathEntities);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// System.out.println(entity.getFilePathEntities());
		return entity;
	}

	
}
