package com.crh2.util;

import java.awt.Toolkit;
import java.awt.Window;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.crh2.calculate.TrainAttribute;
import com.crh2.javabean.Rundata;
import com.crh2.javabean.StationInfo;

/**
 *  一些工具方法
 * @author huhui
 *
 */
public class MyUtillity {
	/**
	 * 定义一个hashmap，用于维护列车型号的id的关系
	 */
	private static HashMap<String,Integer> trainTypeLinkIdHashMap = null;
	static{
		if(trainTypeLinkIdHashMap == null){
			trainTypeLinkIdHashMap = new HashMap<String,Integer>();
			trainTypeLinkIdHashMap.put("4动4拖",1);
			trainTypeLinkIdHashMap.put("1动7拖",2);
			trainTypeLinkIdHashMap.put("2动6拖",3);
			trainTypeLinkIdHashMap.put("3动5拖",4);
			trainTypeLinkIdHashMap.put("5动3拖",5);
			trainTypeLinkIdHashMap.put("6动2拖",6);
			trainTypeLinkIdHashMap.put("7动1拖",7);
			trainTypeLinkIdHashMap.put("8动0拖",8);
			trainTypeLinkIdHashMap.put(" 250km/h车型",9);
		}
	}
	
	/**
	 * 给动车组最大速度和终点里程赋值
	 * @param stationInfoList
	 * @param maxSpeed
	 */
	public static void assignmentParameters(ArrayList<StationInfo> stationInfoList,double maxSpeed){
		TrainAttribute.CRH_DESTINATION_LOCATION = stationInfoList.get(stationInfoList.size()-1).getLocation();
		TrainAttribute.CRH_MAX_SPEED = maxSpeed;
	}
	
	/**
	 * 提供一个根据动车类型得到traintypeid的函数
	 * @param trainType
	 * @return
	 */
	public static int getTrainIdByTrainType(String trainType){
		return trainTypeLinkIdHashMap.get(trainType);
	}
	
	/**
	 * 由于trainTypeLinkIdHashMap的key和value都是唯一的，所以可以提供一个方法，通过value得到key
	 * @param id
	 * @return
	 */
	public static String getTrainTypeByTrainId(int id){
		Set<String> keySet=trainTypeLinkIdHashMap.keySet();
		for(String key : keySet){
		    if(id == trainTypeLinkIdHashMap.get(key)){
		        return key;
		    }
		}
		return null;
	}

	/**
	 * 获得屏幕宽度
	 * @return
	 */
	public static double getScreenWidth(){
		return Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	}
	
	/**
	 * 获得屏幕高度
	 * @return
	 */
	public static double getScreenHeight(){
		return Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	}
	
	/**
	 * 将Frame居中
	 * @param win
	 */
	public static void setFrameOnCenter(Window win){
		Window window = win;
		int width = (int) MyUtillity.getScreenWidth();
		int height = (int) MyUtillity.getScreenHeight();
		window.setLocation((width - window.getWidth()) / 2, (height - window.getHeight()) / 2);
	}
	
	/**
	 * 判断字符串是否全是数字
	 * @param s
	 * @return
	 */
	public static boolean isNumber(String s) {  
        Pattern pattern = Pattern.compile("-?[0-9]*.?[0-9]*");  
        return pattern.matcher(s).matches();  
    }  
	
	/**
	 * 检查字符串是否为空
	 * @param str
	 * @return
	 */
	public static boolean isBlankString(String str){
		return "".equals(str)?true:false;
	}
	
	/**
	 * 提取字符串中的数字
	 * @param str
	 * @return
	 */
	public static int getNumFromString(String str){
		String regEx="[^0-9]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		int routeId = Integer.parseInt(m.replaceAll("").trim());
		return routeId;
	}
	
	/**
	 * 提取n动m拖的n
	 * @param str
	 * @return
	 */
	public static int getFirstNumFromString(String str){
		String firstChar = str.substring(0, 1);
		if(firstChar.equals(" ")){
			return 9;
		}else{
			return Integer.parseInt(firstChar);
		}
	}
	
	/**
	 * 2015.4.25修改，校正牵引能量
	 */
	public static ArrayList<Rundata> checkTractionPower(ArrayList<Rundata> rundataList){
		double maxTractionPower = getMaxTractionPower(rundataList);
		for(int i=10;i<rundataList.size();i++){
			Rundata bean = rundataList.get(i);
			if(bean.getTractionPower() == 0){
				bean.setTractionPower(maxTractionPower);
			}
		}
		return rundataList;
	}
	
	/**
	 * 寻找最大的牵引能量
	 * @param rundataList
	 * @return
	 */
	private static double getMaxTractionPower(ArrayList<Rundata> rundataList){
		double temp = 0;
		for(int i=0;i<rundataList.size();i++){
			double power = rundataList.get(i).getTractionPower();
			if(power > temp){
				temp = power;
			}
		}
		return temp;
	}
	
}
