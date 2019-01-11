/**
 * 
 */
package com.jfc.util;

import java.text.DecimalFormat;
import java.util.*;
import java.util.Map.Entry;

/**
 * @author yu.hou
 *
 */
public class StrUtil {

	public static String genarateUUID() {
		/*****************************************************************************************************************************************************
		return UUID.randomUUID().toString();太慢了, 自己改写为, 机器mac地址+nanotime
		*****************************************************************************************************************************************************/
		//return StrUtil.joint(AddressUtil.getMacAddress(), "-", System.nanoTime());//这种方式有重复种子产生.
		return UUID.randomUUID().toString();
	}

	/**
	 * 首字母变大写
	 */
	public static String firstCharToUpperCase(String str) {
		char firstChar = str.charAt(0);
		if (firstChar >= 'a' && firstChar <= 'z') {
			char[] arr = str.toCharArray();
			arr[0] -= ('a' - 'A');
			return new String(arr);
		}
		return str;
	}

	public static String[] list2array(List<String> list) {
		return list.toArray(new String[list.size()]);
	}

	// 判断字符串里是否含有中文
	public static boolean isChineseStr(String pValue) {
		for (int i = 0; i < pValue.length(); i++) {
			if ((int) pValue.charAt(i) > 256){
				return true;
			}
		}
		return false;
	}

	public static int randomInt(final int min, final int max) {
		Random rand = new Random();
		int value = rand.nextInt();
		if (value == Integer.MIN_VALUE) {
			value = rand.nextInt();
		}
		int tmp = Math.abs(value);
		return tmp % (max - min + 1) + min;
	}

	/**
	 * 小数点后即为
	 * @param length
	 */
	public static double parseDouble(double a, int length) {
		String xiaoshuwei = "";
		for (int i = 0; i < length; i++) {
			xiaoshuwei += "0";
		}
		DecimalFormat df = new DecimalFormat("#0." + xiaoshuwei);
		return Double.parseDouble(df.format(a));
	}

	/**
	 * 高效分割, 性能极其优秀
	 * @param str
	 * @param separator
	 * @return
	 */
	public static List<String> split(final String str, String separator) {
		if (isBlank(str)) {
			return new ArrayList<>();
		}
		final List<String> res = new ArrayList<String>(10);
		int pos, prev = 0;
		int length = separator.length();
		while ((pos = str.indexOf(separator, prev)) != -1) {
			res.add(str.substring(prev, pos));
			prev = pos + length;
		}
		res.add(str.substring(prev));
		return res;
	}

	/**
	 * 拼接字符串的类
	 * @param objects
	 * @return
	 */
	public static String joint(Object... objects) {
		StringBuilder sb = new StringBuilder();
		for (Object obj : objects) {
			sb.append(obj);
		}
		return sb.toString();
	}

	public static String joinWith(String s, Object... objects) {
		StringBuilder sb = new StringBuilder();
		for (Object obj : objects) {
			sb.append(null == obj ? "" : obj.toString()).append(s);
		}
		return removeLastWord(sb.toString(), s);
	}

	/**
	 * 主要用于toString方法
	 * @param s
	 * @param map
	 * @return
	 */
	public static String joinMapWith(String s, Map map) {
		StringBuilder sb = new StringBuilder();
		Iterator<Entry> iter = map.entrySet().iterator();
		while (iter.hasNext()) {
			Entry entry = iter.next();
			sb.append(null == entry ? "" : joint(entry.getKey(), "->", entry.getValue())).append(s);
		}
		return removeLastWord(sb.toString(), s);
	}

	public static <T> String joinSetWith(String s, Set<T> objects) {
		StringBuilder sb = new StringBuilder();
		for (T obj : objects) {
			sb.append(null == obj ? "" : obj.toString()).append(s);
		}
		return removeLastWord(sb.toString(), s);
	}

	public static String joinWithComma(Object... objects) {
		StringBuilder sb = new StringBuilder();
		for (Object obj : objects) {
			sb.append(null == obj ? "" : obj.toString()).append(",");
		}
		return removeLastWord(sb.toString(), ",");
	}

	/**
	 *  'a','b'
	 * @param objects
	 * @return
	 */
	public static String joinWithQuotesAndComma(Object... objects) {
		StringBuilder sb = new StringBuilder();
		for (Object obj : objects) {
			sb.append("'").append(null == obj ? "" : obj.toString()).append("',");
		}
		return removeLastWord(sb.toString(), ",");
	}

	public static String joinWithDoubleQuotesAndComma(Object... objects) {
		StringBuilder sb = new StringBuilder();
		for (Object obj : objects) {
			sb.append("\"").append(null == obj ? "" : obj.toString()).append("\",");
		}
		return removeLastWord(sb.toString(), ",");
	}

	/**
	 * 保留两位有效数字
	 * @param obj
	 * @return
	 */
	public static String parsePercent(Object obj) {
		if (obj instanceof Number) {
			DecimalFormat df = new DecimalFormat("#0.00");
			return df.format(obj);
		} else {
			return obj == null ? "" : obj.toString();
		}
	}

	/**
	 * 字符串为 null 或者为  "" 时返回 true
	 */
	public static boolean isBlank(String str) {
		return null == str || "".equals(str.trim()) ? true : false;
	}

	/**
	 * 不为空
	 * @param str
	 * @return
	 */
	public static boolean isNotBlank(String str) {
		return !isBlank(str);
	}

	/**
	 * 去掉最后一个符号
	 * @param str  123123，
	 * @param word   ，
	 */
	public static String removeLastWord(String str, String word) {
		int length = str.length();
		if (str.length() == 0) {
			return "";
		}
		if (str.lastIndexOf(word) == length - word.length()) {
			return str.substring(0, length - word.length());
		} else {
			return str;
		}
	}

	/**
	 * 转义,去掉 引号
	 * @param s
	 * @return
	 */
	public static String escape4MySQL(String s) {
		if (null == s) {
			return s;
		}
		return s.replace("'", "\\'");
	}

	/**
	 * 
	 * 16进制转10进制
	 * @param s
	 * @return
	 */
	public static int hex2Octal(String s) {
		return Integer.valueOf(s, 16);
	}

	public static long hex2LongOctal(String s) {
		return Long.valueOf(s, 16);
	}

	/**
	 * 10进制转16进制
	 * @param o
	 * @return
	 */
	public static String octal2Hex(int o) {
		return Integer.toHexString(o);
	}

	public static String octal2Hex(long o) {
		return Long.toHexString(o);
	}

	public static String toHexString(long o) {
		return Long.toHexString(o);
	}

	public static String short2Hex(int o) {
		return Integer.toHexString(o & 0xffff);
	}

	public static String byte2Hex(int o) {
		o = o & 0xff;
		if (o <= 0x0f) {
			return "0" + Integer.toHexString(o);
		}
		return Integer.toHexString(o);
	}
}
