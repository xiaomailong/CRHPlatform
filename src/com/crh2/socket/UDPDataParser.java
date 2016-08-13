package com.crh2.socket;

import com.crh.calculation.mintime.TractionForceMinTime;
import com.crh2.calculate.TrainAttribute;
import com.crh2.javabean.Serverinfo;
import com.crh2.service.DataService;

/**
 * 解析UDP连接服务器传输的数据
 * @author huhui
 *
 */
public class UDPDataParser {
	
	private static DataService dataService = new DataService();
	private static Serverinfo bean = null;
	private static String [] key = {"1", "0", "1", "0", "0"};
	/**
	 * 牵引级位
	 */
	private static double traction;
	/**
	 * 制动级位
	 */
	private static int brake;
	/**
	 * 最大速度
	 */
	private static double maxSpeed;
	
	public static void main(String args []){
		UDPDataParser.dataParser("0020082F1122223333444455556666");//1111130010009
	}
		
	/**
	 * 解析数据
	 * @param receivedData
	 */
	public static void dataParser(String receivedData){
		String prefix = receivedData.substring(0, 8);
		String data = receivedData.substring(8, 30);
		//2015.3.15修改新的通信协议
		if(bean == null){
			bean = dataService.getServerinfo();
		}
		if(prefix.equals(bean.getPrefix2())){ //钥匙占用 index2
			String binaryDataStr = hexString2binaryString(data);
			String keyEle = getKeyElement(bean.getIndex2(), binaryDataStr);
			key[1] = keyEle;
		}else if(prefix.equals(bean.getPrefix4())){ //受电弓 index4
			String binaryDataStr = hexString2binaryString(data);
			String keyEle = getKeyElement(bean.getIndex4(), binaryDataStr);
			key[3] = keyEle;
		}else if(prefix.equals(bean.getPrefix5())){ //主断路器 index5
			String binaryDataStr = hexString2binaryString(data);
			String keyEle = getKeyElement(bean.getIndex5(), binaryDataStr);
			key[4] = keyEle;
		}else if(prefix.equals(bean.getPrefix6())){ //牵引力设定百分比 index6
			traction = getTraction(bean.getIndex6(), data);
		}else if(prefix.equals(bean.getPrefix7())){ //制动手柄级位 index7
			brake = getBrake(bean.getIndex7(), data);
		}else if(prefix.equals(bean.getPrefix8())){ //最高速度 index8
			maxSpeed = getMaxSpeed(bean.getIndex8(), data);
		}else if(prefix.equals(bean.getPrefix1())){ //蓄电池开关
			String binaryDataStr = hexString2binaryString(data);
			String keyEle = getKeyElement(bean.getIndex1(), binaryDataStr);
			key[0] = keyEle;
		}else if(prefix.equals(bean.getPrefix3())){ //方向开关
			String binaryDataStr = hexString2binaryString(data);
			String keyEle = getKeyElement(bean.getIndex3(), binaryDataStr);
			key[2] = keyEle;
		}
		
		//首先判断五个参数是否都为1
		if(key2String()!=11111){
			TrainAttribute.CRH_IS_START = false;
		}else if(key2String()==11111){
			if(traction != 0){
				TractionForceMinTime.mode = traction / 100.0;//牵引级别
				TrainAttribute.CRH_MAX_SPEED = maxSpeed;//最高速度
				TrainAttribute.CRH_IS_START = true;
				//设置是否是刹车
				if(brake == 0){
					TrainAttribute.CRH_IS_BRAKE = false;
					TrainAttribute.CRH_BRAKE_LEVEL = 0;
				}else{//只要不为0就表示要制动
					TractionForceMinTime.mode = 0;
					if(TrainAttribute.CRH_IS_RUN == 0){
						TrainAttribute.CRH_IS_START = false;
					}
					TrainAttribute.CRH_IS_BRAKE = true;
					TrainAttribute.CRH_BRAKE_LEVEL = brake; //设置刹车级位
				}
			}
		}
		
		if(key2String()==0){//重置
			if(TrainAttribute.CRH_TEMP == 0){
				TrainAttribute.CRH_IS_RESET = 1;//1为结束计算
				System.err.println("reset");
			}
		}
		System.out.println("key="+key2String()+", maxSpeed="+maxSpeed+", traction="+traction+", brake="+brake);
		
	}
	
	/**
	 * 解析最大速度数据
	 * @param index
	 * @param dataStr
	 * @return
	 */
	public static double getMaxSpeed(String index, String dataStr) {
		int newIndex = Integer.parseInt(index) / 4;
		double data = Integer.parseInt(dataStr.substring(newIndex, newIndex+4), 16);
		return data;
	}

	/**
	 * 解析制动级位数据
	 * @param index
	 * @param dataStr
	 * @return
	 */
	public static int getBrake(String index, String dataStr){
		int newIndex = Integer.parseInt(index) / 4;
		int data = Integer.parseInt(dataStr.substring(newIndex, newIndex+2), 16);
		return data;
	}
	
	/**
	 * 解析牵引级位数据
	 * @param index
	 * @param dataStr
	 * @return
	 */
	public static double getTraction(String index, String dataStr){
		int newIndex = Integer.parseInt(index) / 4;
		double data = Integer.parseInt(dataStr.substring(newIndex, newIndex+4), 16);
		return data;
		
	}
	
	/**
	 * 将key数组中元素组成字符串
	 * @return
	 */
	public static int key2String(){
		String str = "";
		for(int i=0;i<key.length;i++){
			str += key[i];
		}
		return Integer.parseInt(str);
	}
	
	public static String getKeyElement(String index, String binaryDataStr){
		return binaryDataStr.substring(Integer.parseInt(index), Integer.parseInt(index)+1);
	}
	
	/**
	 * 将十六进制转二进制
	 */
	public static String hexString2binaryString(String hexString) {
	    if (hexString == null || hexString.length() % 2 != 0)
	      return null;
	    String bString = "", tmp;
	    for (int i = 0; i < hexString.length(); i++) {
	      tmp = "0000" + Integer.toBinaryString(Integer.parseInt(hexString.substring(i, i + 1), 16));
	      bString += tmp.substring(tmp.length() - 4);
	    }
	    return bString;
	  }
	
	/**
	 * 获取key
	 */
	public static int getKey(String str) { // 00010001000100010001
		int value = 0;
		char[] charArray = str.toCharArray();
		int count = 0;
		for (int i = 0; i < charArray.length; i++) {
			if (charArray[i] == '1') {
				count++;
			}
		}
		if (count == 5) {
			value = 11111;
		} else if (count == 0) {
			value = 0;
		} else {
			value = 1;
		}
		return value;
	}
	
}
