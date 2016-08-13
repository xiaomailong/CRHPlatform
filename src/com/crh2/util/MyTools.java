package com.crh2.util;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 工具类
 * @author huhui
 *
 */
public class MyTools {
	
	/**
	 * 计算所有的像素
	 */
	public static double countlength = 0;
	/**
	 * 里程的比例系数0.989
	 */
	public static double lengthIndex = 1;
	
	public static void main(String [] args){
		System.out.println(MyTools.smallTime(30));
	}
	
	/**
	 * 统计所有的speed，即计算列车运行的总像素数
	 * @param speed
	 */
	public static void countSpeed(double speed){
		countlength += speed;
	}
	
	/**
	 * 判断是否到达列车的最右端
	 * @param railway_x
	 * @return
	 */
	public static boolean isMostRight(double railway_x){
		if(railway_x >= (370-5/2) && railway_x <= (370+5/2)){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 将毫秒格式化
	 * @param time
	 * @return
	 */
	public static String timeFormat(double time) {
		int t = (int) (time * 1000);
		long s;// 秒
		long h;// 小时
		long m;// 分钟
		h = t / 1000 / 60 / 60;
		m = (t - h * 60 * 60 * 1000) / 1000 / 60;
		s = t / 1000 - h * 60 * 60 - m * 60;
		return "00:" + m + ":" + s;
	}
	
	/**
	 * 将时间转化成m.s形式，用于绘制时间线
	 * @param time
	 * @return
	 */
	public static double smallTime(double time) {
		int t = (int) (time * 1000);
		double s;// 秒
		long h;// 小时
		double m;// 分钟
		h = t / 1000 / 60 / 60;
		m = (t - h * 60 * 60 * 1000) / 1000 / 60;
		s = t / 1000 - h * 60 * 60 - m * 60;
		return m + s/60;
	}
	
	/**
	 * 获取字符串中的数字
	 * @param str
	 * @return
	 */
	public static int getNumFromStr(String str){
		String a= str;
		String regEx="[^0-9]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(a);
		return Integer.parseInt(m.replaceAll("").trim());
	}
	
	/**
	 * 获取字符串中的数字，以逗号分隔开返回
	 * @param str
	 * @return
	 */
	public static String getNumStrFromStr(String str){
		String a= str;
		String regEx="[^0-9]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(a);
		String numStr = m.replaceAll(",").trim();
		return numStr.substring(0, numStr.length()-1);
	}
	
	/**
	 * 保留到小数点后面6位
	 * @param num
	 * @return
	 */
	public static double numFormat6(double num){
		DecimalFormat df2  = new DecimalFormat("###.000000");
		return Double.parseDouble(df2.format(num));
	}
	
	/**
	 * 保留到小数点后面两位
	 * @param num
	 * @return
	 */
	public static double numFormat2(double num){
		DecimalFormat df2  = new DecimalFormat("###.00");
		return Double.parseDouble(df2.format(num));
	}
	
	/**
	 * 保留到小数点后面两位
	 * @param num
	 * @return
	 */
	public static String numFormat(double num){
		DecimalFormat df2  = new DecimalFormat("###.00");
		return df2.format(num);
	}
	
}
