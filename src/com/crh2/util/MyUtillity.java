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
 *  һЩ���߷���
 * @author huhui
 *
 */
public class MyUtillity {
	/**
	 * ����һ��hashmap������ά���г��ͺŵ�id�Ĺ�ϵ
	 */
	private static HashMap<String,Integer> trainTypeLinkIdHashMap = null;
	static{
		if(trainTypeLinkIdHashMap == null){
			trainTypeLinkIdHashMap = new HashMap<String,Integer>();
			trainTypeLinkIdHashMap.put("4��4��",1);
			trainTypeLinkIdHashMap.put("1��7��",2);
			trainTypeLinkIdHashMap.put("2��6��",3);
			trainTypeLinkIdHashMap.put("3��5��",4);
			trainTypeLinkIdHashMap.put("5��3��",5);
			trainTypeLinkIdHashMap.put("6��2��",6);
			trainTypeLinkIdHashMap.put("7��1��",7);
			trainTypeLinkIdHashMap.put("8��0��",8);
			trainTypeLinkIdHashMap.put(" 250km/h����",9);
		}
	}
	
	/**
	 * ������������ٶȺ��յ���̸�ֵ
	 * @param stationInfoList
	 * @param maxSpeed
	 */
	public static void assignmentParameters(ArrayList<StationInfo> stationInfoList,double maxSpeed){
		TrainAttribute.CRH_DESTINATION_LOCATION = stationInfoList.get(stationInfoList.size()-1).getLocation();
		TrainAttribute.CRH_MAX_SPEED = maxSpeed;
	}
	
	/**
	 * �ṩһ�����ݶ������͵õ�traintypeid�ĺ���
	 * @param trainType
	 * @return
	 */
	public static int getTrainIdByTrainType(String trainType){
		return trainTypeLinkIdHashMap.get(trainType);
	}
	
	/**
	 * ����trainTypeLinkIdHashMap��key��value����Ψһ�ģ����Կ����ṩһ��������ͨ��value�õ�key
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
	 * �����Ļ���
	 * @return
	 */
	public static double getScreenWidth(){
		return Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	}
	
	/**
	 * �����Ļ�߶�
	 * @return
	 */
	public static double getScreenHeight(){
		return Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	}
	
	/**
	 * ��Frame����
	 * @param win
	 */
	public static void setFrameOnCenter(Window win){
		Window window = win;
		int width = (int) MyUtillity.getScreenWidth();
		int height = (int) MyUtillity.getScreenHeight();
		window.setLocation((width - window.getWidth()) / 2, (height - window.getHeight()) / 2);
	}
	
	/**
	 * �ж��ַ����Ƿ�ȫ������
	 * @param s
	 * @return
	 */
	public static boolean isNumber(String s) {  
        Pattern pattern = Pattern.compile("-?[0-9]*.?[0-9]*");  
        return pattern.matcher(s).matches();  
    }  
	
	/**
	 * ����ַ����Ƿ�Ϊ��
	 * @param str
	 * @return
	 */
	public static boolean isBlankString(String str){
		return "".equals(str)?true:false;
	}
	
	/**
	 * ��ȡ�ַ����е�����
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
	 * ��ȡn��m�ϵ�n
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
	 * 2015.4.25�޸ģ�У��ǣ������
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
	 * Ѱ������ǣ������
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
