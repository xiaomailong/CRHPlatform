package com.crh2.socket;

import com.crh.calculation.mintime.TractionForceMinTime;
import com.crh2.calculate.TrainAttribute;
import com.crh2.javabean.Serverinfo;
import com.crh2.service.DataService;

/**
 * ����UDP���ӷ��������������
 * @author huhui
 *
 */
public class UDPDataParser {
	
	private static DataService dataService = new DataService();
	private static Serverinfo bean = null;
	private static String [] key = {"1", "0", "1", "0", "0"};
	/**
	 * ǣ����λ
	 */
	private static double traction;
	/**
	 * �ƶ���λ
	 */
	private static int brake;
	/**
	 * ����ٶ�
	 */
	private static double maxSpeed;
	
	public static void main(String args []){
		UDPDataParser.dataParser("0020082F1122223333444455556666");//1111130010009
	}
		
	/**
	 * ��������
	 * @param receivedData
	 */
	public static void dataParser(String receivedData){
		String prefix = receivedData.substring(0, 8);
		String data = receivedData.substring(8, 30);
		//2015.3.15�޸��µ�ͨ��Э��
		if(bean == null){
			bean = dataService.getServerinfo();
		}
		if(prefix.equals(bean.getPrefix2())){ //Կ��ռ�� index2
			String binaryDataStr = hexString2binaryString(data);
			String keyEle = getKeyElement(bean.getIndex2(), binaryDataStr);
			key[1] = keyEle;
		}else if(prefix.equals(bean.getPrefix4())){ //�ܵ繭 index4
			String binaryDataStr = hexString2binaryString(data);
			String keyEle = getKeyElement(bean.getIndex4(), binaryDataStr);
			key[3] = keyEle;
		}else if(prefix.equals(bean.getPrefix5())){ //����·�� index5
			String binaryDataStr = hexString2binaryString(data);
			String keyEle = getKeyElement(bean.getIndex5(), binaryDataStr);
			key[4] = keyEle;
		}else if(prefix.equals(bean.getPrefix6())){ //ǣ�����趨�ٷֱ� index6
			traction = getTraction(bean.getIndex6(), data);
		}else if(prefix.equals(bean.getPrefix7())){ //�ƶ��ֱ���λ index7
			brake = getBrake(bean.getIndex7(), data);
		}else if(prefix.equals(bean.getPrefix8())){ //����ٶ� index8
			maxSpeed = getMaxSpeed(bean.getIndex8(), data);
		}else if(prefix.equals(bean.getPrefix1())){ //���ؿ���
			String binaryDataStr = hexString2binaryString(data);
			String keyEle = getKeyElement(bean.getIndex1(), binaryDataStr);
			key[0] = keyEle;
		}else if(prefix.equals(bean.getPrefix3())){ //���򿪹�
			String binaryDataStr = hexString2binaryString(data);
			String keyEle = getKeyElement(bean.getIndex3(), binaryDataStr);
			key[2] = keyEle;
		}
		
		//�����ж���������Ƿ�Ϊ1
		if(key2String()!=11111){
			TrainAttribute.CRH_IS_START = false;
		}else if(key2String()==11111){
			if(traction != 0){
				TractionForceMinTime.mode = traction / 100.0;//ǣ������
				TrainAttribute.CRH_MAX_SPEED = maxSpeed;//����ٶ�
				TrainAttribute.CRH_IS_START = true;
				//�����Ƿ���ɲ��
				if(brake == 0){
					TrainAttribute.CRH_IS_BRAKE = false;
					TrainAttribute.CRH_BRAKE_LEVEL = 0;
				}else{//ֻҪ��Ϊ0�ͱ�ʾҪ�ƶ�
					TractionForceMinTime.mode = 0;
					if(TrainAttribute.CRH_IS_RUN == 0){
						TrainAttribute.CRH_IS_START = false;
					}
					TrainAttribute.CRH_IS_BRAKE = true;
					TrainAttribute.CRH_BRAKE_LEVEL = brake; //����ɲ����λ
				}
			}
		}
		
		if(key2String()==0){//����
			if(TrainAttribute.CRH_TEMP == 0){
				TrainAttribute.CRH_IS_RESET = 1;//1Ϊ��������
				System.err.println("reset");
			}
		}
		System.out.println("key="+key2String()+", maxSpeed="+maxSpeed+", traction="+traction+", brake="+brake);
		
	}
	
	/**
	 * ��������ٶ�����
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
	 * �����ƶ���λ����
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
	 * ����ǣ����λ����
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
	 * ��key������Ԫ������ַ���
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
	 * ��ʮ������ת������
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
	 * ��ȡkey
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
