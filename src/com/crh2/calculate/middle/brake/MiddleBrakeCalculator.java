package com.crh2.calculate.middle.brake;

import java.util.ArrayList;
import java.util.HashMap;

import com.crh2.calculate.TrainAttribute;
import com.crh2.javabean.Curve;
import com.crh2.javabean.Rundata;
import com.crh2.util.Arith;

/**
 * �м������ƶ�������
 * @author huhui
 *
 */
public class MiddleBrakeCalculator {
	/**
	 * ���ڱ������е������Ϣ
	 */
	private ArrayList<Curve> curveList = null;
	/**
	 * �������������˶�ʱ��index
	 */
	private HashMap<Integer,Integer> curveFlagMap = null;
	
	//���캯��
	public MiddleBrakeCalculator(ArrayList<Curve> curveList){
		this.curveList = curveList;
		curveFlagMap = new HashMap<Integer, Integer>();
	}
	
	/**
	 * ������һ���µ����������ʱ�򣬿�ʼ����ٶ�
	 * @param currentSpeed
	 * @param curveFlag
	 * @return
	 */
	public boolean isBeyondLimitSpeed(double currentSpeed,int curveFlag) {
//		System.out.println("curveFlag="+curveFlag+",size="+curveList.size());
		Curve curve = curveList.get(curveFlag);
		if(currentSpeed > curve.getSpeedLimit()){//����
			System.out.println("����"+curveFlag+"��������٣�������"+curve.getSpeedLimit()+"��ʵ���ٶ���"+currentSpeed);
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * ����泬�������Ƶ��ٶȣ���ô�ͻ���
	 * @param currentSpeed
	 * @param curveFlag
	 * @param rundataBeansList
	 * @return
	 */
	public Rundata rollBackRundata(double currentSpeed,int curveFlag,ArrayList<Rundata> rundataBeansList){
		//�õ���ǰ���ٵ���µ���Ϣ
		Curve curve = curveList.get(curveFlag);
		//����ƽ���ٶ�    averageSpeed = (currentSpeed + curve.getSpeedLimit())/2
		double averageSpeed = Arith.div(Arith.add(currentSpeed, curve.getSpeedLimit()), 2);
//		System.out.println("currentSpeed = "+currentSpeed);
		//�õ���averageSpeed��Ӧ��Si������ĵ�
		Rundata rundata = this.nearestRundataBeanCalculator(curveFlag,averageSpeed, rundataBeansList);
		return rundata;
	}
	
	/**
	 * ������averageSpeed��Ӧ��Si������ĵ�
	 * @param curveFlag
	 * @param averageSpeed
	 * @param rundataBeansList
	 * @return
	 */
	public Rundata nearestRundataBeanCalculator(int curveFlag,double averageSpeed,ArrayList<Rundata> rundataBeansList){
		Rundata rundata = null;
		double min = Integer.MAX_VALUE;//��minΪһ���ܴ����
		int index = 0;//���ڼ�¼�ҵ���Rundata���±�
		if(TrainAttribute.CRH_IS_CONSTANT_SPEED == false){//�����������״̬
			//����rundataBeansList��Ѱ������ĵ�
			for(int i=0;i<rundataBeansList.size();i++){
				//rundataBeansList.get(i).getSpeed()-averageSpeed
				double temp = Math.abs(Arith.sub(rundataBeansList.get(i).getSpeed(), averageSpeed));
				if(temp < min){
					min = temp;
					index = i;
					
				}
			}
		}else{//���������״̬
			addIntoCurveFlagMap(curveFlag);//�ȼ���hashmap
			index = rundataBeansList.size() - 50*curveFlagMap.get(curveFlag);
		}
		//�õ�����ĵ�
		rundata = rundataBeansList.get(index);
		//ɾ��index֮�������rundata
		this.removeRundata(index, rundataBeansList);
		return rundata;
	}
	
	/**
	 * �Ƴ�index֮�����������
	 * @param index
	 * @param rundataBeansList
	 */
	public void removeRundata(int index,ArrayList<Rundata> rundataBeansList){
		for(int i=rundataBeansList.size()-1;i>=index;i--){
			rundataBeansList.remove(i);
		}
	}
	
	/**
	 * ��curveFlagMap������Ԫ��
	 * @param curveFlag
	 */
	public void addIntoCurveFlagMap(int curveFlag){
		if(curveFlagMap.containsKey(curveFlag)){//��������˸�curveFlag�����1
			int newValue = curveFlagMap.get(curveFlag) + 1;
			curveFlagMap.put(curveFlag, newValue);
		}else{//��������������¼���
			curveFlagMap.put(curveFlag, 1);
		}
	}
	
}
