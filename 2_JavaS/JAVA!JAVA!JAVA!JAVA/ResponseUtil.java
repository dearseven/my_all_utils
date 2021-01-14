package com.qqjf.moudle.recommend;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ResponseUtil {
	private Gson __json = new GsonBuilder().disableHtmlEscaping().create();
	private Map<String,Object> map = new HashMap<String, Object>();

	
	private static HashMap<String, String> __errs = null;

	public static ResponseUtil makesucceeded() {
		if (__errs == null) {
			__makeErrs();
		}
		ResponseUtil ru = new ResponseUtil();
		try {
//			ru.__json.("errCode", "TJ000");
//			ru.__json.put("errMsg", __errs.get("TJ000"));
			ru.map.put("errCode", "TJ000");
			ru.map.put("errMsg", __errs.get("TJ000"));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return ru;
	}

	public static ResponseUtil makeFailed(String errCode) {
		if (__errs == null) {
			__makeErrs();
		}
		ResponseUtil ru = new ResponseUtil();
		try {
//			ru.__json.put("errCode", errCode);
//			ru.__json.put("errMsg", __errs.get(errCode));
			ru.map.put("errCode", errCode);
			ru.map.put("errMsg", __errs.get(errCode));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ru;
	}

	/**
	 * 添加数据
	 * @param k
	 * @param v
	 * @return
	 */
	public ResponseUtil appendData(String k,Object v) {
		try {
			//this.__json.put(k, v);
			this.map.put(k,v);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}
	
	/**
	 * 返回结果数据
	 * @return
	 */
	public String end() {
		return this.__json.toJson(map).toString();
	}
	
	private static void __makeErrs() {
		if (__errs == null) {
			synchronized (ResponseUtil.class) {
				if (__errs == null) {
					__errs=new HashMap<String, String>();
					__errs.put("TJ999", "系统错误");
					__errs.put("TJ000", "执行成功");
				}
			}
		}
	}
}
