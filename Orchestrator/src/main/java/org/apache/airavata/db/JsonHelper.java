package org.apache.airavata.db;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class JsonHelper {
	private static final Gson GSON = new GsonBuilder().disableHtmlEscaping().create();
	private static final Type TT_MAPSS = new TypeToken<Map<String, ArrayList<String>>>(){}.getType();
	
	private static final Type TT_MAPStr = new TypeToken<Map<String, String>>(){}.getType();
	
	public static Map <String, ArrayList<String>> jsonToMapSS(String json){
		Map<String, ArrayList<String>> ret = new HashMap<String, ArrayList<String>>();
		if(json == null || json.isEmpty())
			return ret;
		return GSON.fromJson(json, TT_MAPSS);
	}
	
	public static String mapSSToJson(Map<String, ArrayList<String>> map){
		if(map == null)
			map = new HashMap<String, ArrayList<String>>();
		return GSON.toJson(map);
	}
	
	public static String mapStrToJson(Map<String, String> map){
		if(map == null)
			map = new HashMap<String, String>();
		return GSON.toJson(map);
	}
	
	public static Map<String, String> jsonToMapStr(String json){
		Map<String, String> ret = new HashMap<String, String>();
		if(json == null || json.isEmpty())
			return ret;
		return GSON.fromJson(json, TT_MAPStr);
	}
}
