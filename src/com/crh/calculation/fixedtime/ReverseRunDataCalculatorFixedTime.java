package com.crh.calculation.fixedtime;

import java.util.ArrayList;

import com.crh.calculation.mintime.BrakeForceMinTime;
import com.crh.calculation.mintime.NetCurrent;
import com.crh.calculation.mintime.brakepoint.ReverseOtherResistance;
import com.crh.calculation.mintime.brakepoint.SnBpMinTime;
import com.crh.calculation.mintime.brakepoint.VnBpMinTime;
import com.crh2.calculate.TrainAttribute;
import com.crh2.javabean.Curve;
import com.crh2.javabean.Rundata;
import com.crh2.javabean.Slope;
import com.crh2.javabean.StationInfo;

/**
 * ����Ӹ�����վ�������õ�ɲ����
 * @author huhui
 */
public class ReverseRunDataCalculatorFixedTime {

	/**
	 * ���㷴�������������������
	 */
	private ReverseOtherResistance reverseOtherResistance = null;
	/**
	 * ���ڱ���RundataBack��javabean
	 */
	private ArrayList<Rundata> rundataBackBeansList = null;
	/**
	 * ��������
	 */
	private NetCurrent netCurrent = null;

	/**
	 * ���캯������ʼ������
	 * @param slopeList �¶�����
	 * @param curveList �������
	 * @param netCurrent ��������
	 */
	public ReverseRunDataCalculatorFixedTime(ArrayList<Slope> slopeList, ArrayList<Curve> curveList, NetCurrent netCurrent){
		reverseOtherResistance = new ReverseOtherResistance(slopeList, curveList);
		this.netCurrent = netCurrent;
	}
	
	/**
	 * ��ȡ��������
	 * @return ���������rundataBackBeansList
	 */
	public ArrayList<Rundata> getRundataBackBeansList(){
		return rundataBackBeansList;
	}
	
	/**
	 * ����RundataBack��javabean
	 * @param runtime
	 * @param speed
	 * @param distance
	 * @param location
	 * @param comBrakePower
	 * @param elecBrakePower
	 * @param cp
	 * @param tractionGridCurrent
	 * @param comBrakeGridCurrent
	 * @param acceleration
	 */
	public void saveRundataBackBeans(int runtime,double speed,double distance,double location, double comBrakePower, double elecBrakePower, double cp, double tractionGridCurrent, double comBrakeGridCurrent, double acceleration){
		Rundata rundata = new Rundata();
		rundata.setRuntime(runtime);
		rundata.setSpeed(speed);
		rundata.setDistance(distance);
		rundata.setLocation(location);
		rundata.setCp(cp);
		rundata.setComBrakeForcePower(comBrakePower);
		rundata.setElecBrakeForcePower(elecBrakePower);
		rundata.setTractionGridCurrent(tractionGridCurrent);
		rundata.setComBrakeGridCurrent(comBrakeGridCurrent);
		rundata.setAcceleration(acceleration);
		rundataBackBeansList.add(rundata);
	}

	/**
	 * �������t3��s3����������ٽ׶�
	 * @param v �ٶ�
	 * @param mode
	 * @return paraArray
	 */
	public double [] calculateT3AndS3(double v, int mode) {
		int i = 0;
		double currentSpeed = 0;// �൱��Vi+1
		double lastSpeed = 0;// �൱��Vi
		double Si = 0, S = 0;// �൱��Si+1
		String str = "";// д��txt�е�����
		double cp = 0;// ��Ϊ������Ҫ�޸ĵ�cpֵ
		double slopeCp = 0, curveCp = 0;// �ֱ����ڼ�¼��Ϊ�µ��������������Cpֵ
		ArrayList<String> strList = new ArrayList<String>();//���ڱ�����Ҫ��ӡ������
		double comBrakeForce = 0, comBrakePower = 0, elecBrakePOwer = 0, tractionGridCurrent = 0, comBrakeGridCurrent = 0, acceleration = 0;
		rundataBackBeansList = new ArrayList<Rundata>();
		double [] paraArray = new double [3];//0��ʾʱ��t��1��ʾ���s
		while (true) {
			str = i + "		" + currentSpeed + "		" + Si + "		" + comBrakePower+"		"+BrakeForceMinTime.getComBrakingForce(currentSpeed);
			if(mode == RunDataCalculatorFixedTime.CAL_SAVE_MODE){
				double location = TrainAttribute.CRH_DESTINATION_LOCATION - Si;
				this.saveRundataBackBeans(i, currentSpeed, Si, location, comBrakePower, elecBrakePOwer, cp, tractionGridCurrent, comBrakeGridCurrent, acceleration);
			}
			currentSpeed = VnBpMinTime.calSpeed(currentSpeed, TrainAttribute.CRH_CAL_TIME_DIVISION, cp);
			slopeCp = ReverseOtherResistance.bySlope(Si);// �µ���������
			double [] paras = ReverseOtherResistance.byCurve(currentSpeed, Si);// �������
			currentSpeed = paras[0];
			curveCp = paras[1];
			cp = slopeCp + curveCp;//����������
			Si = SnBpMinTime.calShift(currentSpeed, lastSpeed, Si);
			
			strList.add(str);

			i++;
			acceleration = BrakeForceMinTime.getBrakingAcceleration(currentSpeed, 9);//8���ƶ�
			comBrakeForce = BrakeForceMinTime.getComBrakingForce(currentSpeed);
			comBrakeGridCurrent = netCurrent.getComBrakeForceNetCurrent(comBrakeForce, currentSpeed);
			tractionGridCurrent = comBrakeGridCurrent;
			comBrakePower += comBrakeForce * (Si-S);//������������
			elecBrakePOwer += BrakeForceMinTime.getElecBrakeForce(currentSpeed)*(Si-S);
			lastSpeed = currentSpeed;
			S = Si;
			if (currentSpeed >= v) {
				paraArray[0] = i;
				paraArray[1] = Si;
				paraArray[2] = comBrakePower;
				break;
			}
		}
//		WriteIntoTxt.writeIntoTxt(strList, "10.23_t3_s3 _����.txt");
		return paraArray;
	}

	/**
	 * �������
	 * @param station ��վ��Ϣ
	 * @return ��������rundataBackBeansList
	 */
	public ArrayList<Rundata> calculatorRundataBack(StationInfo station) {
		int i = 0;
		double currentSpeed = 0;// �൱��Vi+1
		double lastSpeed = 0;// �൱��Vi
		double Si = 0, S = 0;// �൱��Si+1
		String str = "";// д��txt�е�����
		double cp = 0;// ����������
		double slopeCp = 0, curveCp = 0;// �µ��������������
		ArrayList<String> strList = new ArrayList<String>();//���ڱ�����Ҫ��ӡ������
		double power = 0;//��������
		double location = 0;
		rundataBackBeansList = new ArrayList<Rundata>();
		while (true) {
			location = station.getLocation() - Si;
			str = i + "		" + currentSpeed + "		" + Si + "		" + location + "		"+power;
//			this.saveRundataBackBeans(i, currentSpeed, Si, location, power , 0, cp);
			currentSpeed = VnBpMinTime.calSpeed(currentSpeed, TrainAttribute.CRH_CAL_TIME_DIVISION, cp);
			slopeCp = ReverseOtherResistance.bySlope(Si);// �µ���������
			double [] paras = ReverseOtherResistance.byCurve(currentSpeed, Si);// �������
			currentSpeed = paras[0];
			curveCp = paras[1];
			cp = slopeCp + curveCp;//����������
			Si = SnBpMinTime.calShift(currentSpeed, lastSpeed, Si);
				
			strList.add(str);
			
			i++;
			power += BrakeForceMinTime.getComBrakingForce(currentSpeed)*(Si-S);//������������
			lastSpeed = currentSpeed;
			S = Si;
			if (lastSpeed >= TrainAttribute.CRH_MAX_SPEED) {
				break;
			}
		}
//		WriteIntoTxt.writeIntoTxt(strList, "CRH_10.23_�̶�ʱ��_�������.txt");
		return rundataBackBeansList;
	}
	
}
