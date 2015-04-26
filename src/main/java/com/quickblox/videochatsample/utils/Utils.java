package com.quickblox.videochatsample.utils;

import org.json.JSONObject;

import java.math.BigInteger;
import java.security.SecureRandom;

public class Utils {

	public static String generateBigCode() {
		SecureRandom random = new SecureRandom();
		return new BigInteger(130, random).toString(32);
	}
	
	public static String generateCode() {
		SecureRandom random = new SecureRandom();
		return new BigInteger(30, random).toString(32);
	}


    public static String safeStringFromJson(JSONObject node, String key, String default_key) {
        try {
            return node.getString(key);
        } catch (Exception e) {
            return default_key;
        }
    }
/*
    public static boolean checkJsonInput(JSONObject req){
		try{
			JsonNode jsonReq = req.body().asJson();
			return !jsonReq.isNull();
		}catch(Exception e){
			return false;
		}
	}
	

	public static String safeStringFromJson(JsonNode node, String key) {
		try {
			return node.get(key).asText();
		} catch (Exception e) {
			return "";
		}
	}

	public static long safeLongFromJson(JsonNode node, String key) {
		try {
			return node.get(key).asLong();
		} catch (Exception e) {
			return 0;
		}
	}
	
	public static Double safeDoubleFromJson(JsonNode node, String key) {
		try {
			return node.get(key).asDouble();
		} catch (Exception e) {
			return 0.0;
		}
	}
	
	public static boolean safeBooleanFromJson(JsonNode node, String key) {
		try {
			return node.get(key).asBoolean();
		} catch (Exception e) {
			return false;
		}
	}*/
}
