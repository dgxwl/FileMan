package com.lvsen.util;

public class StringUtils {
	
	private StringUtils() {
	}

	/**
	 * 判断字符串是否为空
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNullOrEmpty(String str) {
		return str == null || str.length() == 0;
	}

	/**
	 * 判断多个参数是否为空
	 * 
	 * @param args 判断的参数
	 * @return 返回判断结果数组
	 */
	public static boolean[] multiIsNull(String... args) {
		boolean[] flags = new boolean[args.length];

		for (int i = 0; i < args.length; i++) {
			String arg = args[i];
			if (null == arg || arg.trim().equals("")) {
				flags[i] = true;
			}
		}

		return flags;
	}

	/**
	 * 判断多个参数是否存在空值或空字符串
	 * 
	 * @param args 多个字符串
	 * @return 存在则返回true 否则返回false
	 */
	public static boolean multiIsNullOrEmpty(String... args) {
		for (int i = 0; i < args.length; i++) {
			String arg = args[i];
			if (null == arg || arg.trim().equals("")) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 根据判断多个参数是否为空的结果给出对应的提示信息
	 * 
	 * @param flags 判断多个参数是否为空的结果数组
	 * @param delimiter 分隔符
	 * @param messages 各参数为空时对应的提示
	 * @return
	 */
	public static String getAllNullMessages(boolean[] flags, String delimiter, String... messages) {
		if (flags.length != messages.length) {
			throw new RuntimeException("检测参数个数与错误信息个数不一致");
		}
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < flags.length; i++) {
			if (flags[i]) {
				builder.append(messages[i]).append(delimiter); // 拼接多个信息
			}
		}
		builder.deleteCharAt(builder.length() - 1);

		return builder.toString();
	}

	/**
	 * 删除字符串的指定前缀
	 * 
	 * @param val 要处理的字符串
	 * @param prefix 前缀
	 * @return 去除前缀的字符串
	 */
	public static String deletePrefix(String val, String prefix) {
		return val.substring(prefix.length());
	}
}
