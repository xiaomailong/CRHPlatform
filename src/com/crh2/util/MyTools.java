package com.crh2.util;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ������
 * @author huhui
 *
 */
public class MyTools {
	
	/**
	 * �������е�����
	 */
	public static double countlength = 0;
	/**
	 * ��̵ı���ϵ��0.989
	 */
	public static double lengthIndex = 1;
	
	public static void main(String [] args){
		System.out.println(MyTools.smallTime(30));
	}
	
	/**
	 * ͳ�����е�speed���������г����е���������
	 * @param speed
	 */
	public static void countSpeed(double speed){
		countlength += speed;
	}
	
	/**
	 * �ж��Ƿ񵽴��г������Ҷ�
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
	 * �������ʽ��
	 * @param time
	 * @return
	 */
	public static String timeFormat(double time) {
		int t = (int) (time * 1000);
		long s;// ��
		long h;// Сʱ
		long m;// ����
		h = t / 1000 / 60 / 60;
		m = (t - h * 60 * 60 * 1000) / 1000 / 60;
		s = t / 1000 - h * 60 * 60 - m * 60;
		return "00:" + m + ":" + s;
	}
	
	/**
	 * ��ʱ��ת����m.s��ʽ�����ڻ���ʱ����
	 * @param time
	 * @return
	 */
	public static double smallTime(double time) {
		int t = (int) (time * 1000);
		double s;// ��
		long h;// Сʱ
		double m;// ����
		h = t / 1000 / 60 / 60;
		m = (t - h * 60 * 60 * 1000) / 1000 / 60;
		s = t / 1000 - h * 60 * 60 - m * 60;
		return m + s/60;
	}
	
	/**
	 * ��ȡ�ַ����е�����
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
	 * ��ȡ�ַ����е����֣��Զ��ŷָ�������
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
	 * ������С�������6λ
	 * @param num
	 * @return
	 */
	public static double numFormat6(double num){
		DecimalFormat df2  = new DecimalFormat("###.000000");
		return Double.parseDouble(df2.format(num));
	}
	
	/**
	 * ������С���������λ
	 * @param num
	 * @return
	 */
	public static double numFormat2(double num){
		DecimalFormat df2  = new DecimalFormat("###.00");
		return Double.parseDouble(df2.format(num));
	}
	
	/**
	 * ������С���������λ
	 * @param num
	 * @return
	 */
	public static String numFormat(double num){
		DecimalFormat df2  = new DecimalFormat("###.00");
		return df2.format(num);
	}
	
}
