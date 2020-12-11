/**  
 * 项目名称  ：  bright
 * 文件名称  ：  StringUtil.java
 * 日期时间  ：  config.properties文件
 */
package com.bright.push.util;

import org.apache.commons.lang.StringUtils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

/**
 * 字符串工具类。
 * 
 * @author 王晓峰
 * @since 1.0
 */
public class StringUtil {

	private static String[] _PChar = { "\\", "/", ":", ";", ".", ",", "?", "\"", "<", ">", "!", "|", "-", "'" };

	private static String[] _InjectionChar = { "\\", "/", ":", ";", "?", "\"", "<", ">", "!", "%", "|", "'", "and",
			"or" };

	/**
	 * if str is empty that return true
	 * 
	 * @param str
	 * @return boolean
	 */
	public static boolean isEmpty(String str) {
		if (str == null || "".equals(str)) {
			return true;
		}
		return false;
	}

	/**
	 * if obj is empty that return true
	 * 
	 * @param obj
	 * @return boolean
	 */
	public static boolean isEmpty(Object obj) {
		if (obj == null || "".equals(obj)) {
			return true;
		}
		return false;
	}

	/**
	 * convert the not exist string to empty
	 * 
	 * @param String str
	 * @return String empty string
	 */
	public static String convertEmpty(String str) {
		if (str == null || str.length() == 0) {
			return "";
		} else {
			return str;
		}
	}

	/**
	 * get the filter string
	 * 
	 * @param String str
	 * @param String mark ["\\\\"]
	 * @return String string
	 */
	public static String filter(String str, String mark) {
		str = str.replaceAll(mark, "");
		return str;
	}

	/**
	 * get the param length
	 * 
	 * @param String str
	 * @return int the length of the param
	 */
	public static int getLength(String str) {
		return str == null ? 0 : str.length();
	}

	/**
	 * if strInput is empty then replace it with strReplace
	 * 
	 * @param String strInput
	 * @param String strReplace
	 * @return String
	 */
	public static String replaceEmpty(String strInput, String strReplace) {
		if (strInput == null || "".equals(strInput)) {
			return strReplace;
		}
		return strInput;
	}

	/**
	 * get the numeration id
	 * 
	 * @return String nativeId
	 */
	public static String getNativeId() {
		String nativeId = "";
		String system_ymd = new SimpleDateFormat("yyMMddHHmmss").format(new Date());
		String rundum_no = Double.toString(Math.random());
		nativeId = delProhibitionChar(system_ymd + rundum_no.substring(2, 6));
		return nativeId;
	}

	/**
	 * replace the empty char to special char
	 * 
	 * @param String str
	 * @return String strBuf
	 */
	public static String delProhibitionChar(String str) {
		StringBuffer strBuf = new StringBuffer(str);
		int ind = 0;
		for (int i = 0; i < _PChar.length; i++) {
			ind = str.indexOf(_PChar[i]);
			while (ind > -1) {
				strBuf.replace(ind, ind + _PChar[i].length(), "");
				str = strBuf.toString();
				strBuf = new StringBuffer(str);
				ind = str.indexOf(_PChar[i]);
			}
		}
		return strBuf.toString();
	}

	/**
	 * delSqlInjectionChar (防止基本的SQL注入) (这里描述这个方法适用条件 – 可选)
	 * 
	 * @param str
	 * @return String
	 */
	public static String delSqlInjectionChar(String str) {
		StringBuffer strBuf = new StringBuffer(str);
		int ind = 0;
		for (int i = 0; i < _InjectionChar.length; i++) {
			ind = str.toLowerCase().indexOf(_InjectionChar[i]);
			while (ind > -1) {
				strBuf.replace(ind, ind + _InjectionChar[i].length(), "");
				str = strBuf.toString();
				strBuf = new StringBuffer(str);
				ind = str.indexOf(_InjectionChar[i]);
			}
		}
		return strBuf.toString();
	}

	/**
	 * get the special system date that like you give the formate
	 * 
	 * @example:getFormatSysDate("YYMMDD")->20081103
	 * @param String format
	 * @return String system_ymd
	 * @throws Exception
	 */
	public static String getFormatSysDate(String format) throws Exception {
		try {
			String system_ymd = new SimpleDateFormat(format).format(new Date());
			return system_ymd;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * in strSource that subString appear times
	 * 
	 * @param String strSource
	 * @param String subStr
	 * @return int count
	 */
	public static int getSubCount(String strSource, String subStr) {
		if (strSource == null || strSource.length() == 0) {
			return 0;
		}
		int count = 0;
		int index = strSource.indexOf(subStr);
		while (index >= 0) {
			count++;
			index = strSource.indexOf(subStr, index + 1);
		}
		return count;
	}

	/**
	 * the param is int
	 * 
	 * @param String str
	 * @return boolean
	 */
	public static boolean isInt(String str) {
		try {
			Integer.valueOf(str);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	/**
	 * the param is double
	 * 
	 * @param String str
	 * @return boolean
	 */
	public static boolean isDouble(String str) {
		try {
			Double.valueOf(str);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	/**
	 * the param is float
	 * 
	 * @param String str
	 * @return boolean
	 */
	public static boolean isFloat(String str) {
		try {
			Float.valueOf(str);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	/**
	 * 排序
	 * 
	 * @param nums 要排序的数组
	 * @param str  asc 或 desc
	 * @return
	 */
	public static Long[] sort(Long[] nums, String str) {
		Arrays.sort(nums);
		int length = nums.length;
		Long[] l = new Long[length];
		if (str.equalsIgnoreCase("desc")) {
			for (int i = 0; i < nums.length; i++) {
				l[i] = nums[--length];
			}
		} else {
			l = nums;
		}
		return l;
	}

	/**
	 * 截取指定长度的字符串。
	 * 
	 * @param str      字符串
	 * @param location 0表示在前截取，1表示在后截取。
	 * @param length   截取的长度
	 * @return
	 */
	public static String subString(String str, int location, int length) {
		switch (location) {
		case 0:
			str = str.substring(0, length);
			break;
		case 1:
			str = str.substring(str.length() - length, str.length());
			break;
		}
		return str;
	}

	/**
	 * inputStreamToString (输入流转换成字符串)
	 * 
	 * @param is 输入流
	 * @return 字符串
	 * @throws IOException String
	 */
	public static String inputStreamToString(InputStream is) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int i = -1;
		while ((i = is.read()) != -1) {
			baos.write(i);
		}
		return baos.toString();
	}

	/**
	 * inputStreamToString (输入流转换成字符串，指定编码格式)
	 * 
	 * @param is      输入流
	 * @param charset 编码格式
	 * @return 字符串
	 * @throws IOException String
	 */
	public static String inputStreamToString(InputStream is, String charset) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is, charset));
		StringBuilder sb = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
			sb.append(line + "/n");
		}
		is.close();
		return sb.toString();
	}

	/**
	 * 将Map的Key转换成","分割的字符串
	 * 
	 * @param source
	 * @return
	 */
	public static String convertKeyOfMapToString(Map<?, ?> source) {
		return StringUtils.join(source.keySet().toArray(), ",");
	}
}
