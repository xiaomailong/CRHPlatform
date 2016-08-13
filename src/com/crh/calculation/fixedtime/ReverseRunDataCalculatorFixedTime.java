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
 * 计算从各个车站反向计算得到刹车点
 * @author huhui
 */
public class ReverseRunDataCalculatorFixedTime {

	/**
	 * 计算反向计算中其它附加阻力
	 */
	private ReverseOtherResistance reverseOtherResistance = null;
	/**
	 * 用于保存RundataBack的javabean
	 */
	private ArrayList<Rundata> rundataBackBeansList = null;
	/**
	 * 网流计算
	 */
	private NetCurrent netCurrent = null;

	/**
	 * 构造函数，初始化数据
	 * @param slopeList 坡度数据
	 * @param curveList 弯道数据
	 * @param netCurrent 网络数据
	 */
	public ReverseRunDataCalculatorFixedTime(ArrayList<Slope> slopeList, ArrayList<Curve> curveList, NetCurrent netCurrent){
		reverseOtherResistance = new ReverseOtherResistance(slopeList, curveList);
		this.netCurrent = netCurrent;
	}
	
	/**
	 * 获取反向数据
	 * @return 反向计算结果rundataBackBeansList
	 */
	public ArrayList<Rundata> getRundataBackBeansList(){
		return rundataBackBeansList;
	}
	
	/**
	 * 保存RundataBack的javabean
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
	 * 负责计算t3和s3，即反向加速阶段
	 * @param v 速度
	 * @param mode
	 * @return paraArray
	 */
	public double [] calculateT3AndS3(double v, int mode) {
		int i = 0;
		double currentSpeed = 0;// 相当于Vi+1
		double lastSpeed = 0;// 相当于Vi
		double Si = 0, S = 0;// 相当于Si+1
		String str = "";// 写入txt中的内容
		double cp = 0;// 因为阻力需要修改的cp值
		double slopeCp = 0, curveCp = 0;// 分别用于记录因为坡道和弯道所修正的Cp值
		ArrayList<String> strList = new ArrayList<String>();//用于保存需要打印的数据
		double comBrakeForce = 0, comBrakePower = 0, elecBrakePOwer = 0, tractionGridCurrent = 0, comBrakeGridCurrent = 0, acceleration = 0;
		rundataBackBeansList = new ArrayList<Rundata>();
		double [] paraArray = new double [3];//0表示时间t，1表示里程s
		while (true) {
			str = i + "		" + currentSpeed + "		" + Si + "		" + comBrakePower+"		"+BrakeForceMinTime.getComBrakingForce(currentSpeed);
			if(mode == RunDataCalculatorFixedTime.CAL_SAVE_MODE){
				double location = TrainAttribute.CRH_DESTINATION_LOCATION - Si;
				this.saveRundataBackBeans(i, currentSpeed, Si, location, comBrakePower, elecBrakePOwer, cp, tractionGridCurrent, comBrakeGridCurrent, acceleration);
			}
			currentSpeed = VnBpMinTime.calSpeed(currentSpeed, TrainAttribute.CRH_CAL_TIME_DIVISION, cp);
			slopeCp = ReverseOtherResistance.bySlope(Si);// 坡道附加阻力
			double [] paras = ReverseOtherResistance.byCurve(currentSpeed, Si);// 弯道附加
			currentSpeed = paras[0];
			curveCp = paras[1];
			cp = slopeCp + curveCp;//附加阻力和
			Si = SnBpMinTime.calShift(currentSpeed, lastSpeed, Si);
			
			strList.add(str);

			i++;
			acceleration = BrakeForceMinTime.getBrakingAcceleration(currentSpeed, 9);//8级制动
			comBrakeForce = BrakeForceMinTime.getComBrakingForce(currentSpeed);
			comBrakeGridCurrent = netCurrent.getComBrakeForceNetCurrent(comBrakeForce, currentSpeed);
			tractionGridCurrent = comBrakeGridCurrent;
			comBrakePower += comBrakeForce * (Si-S);//计算阻力做功
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
//		WriteIntoTxt.writeIntoTxt(strList, "10.23_t3_s3 _计算.txt");
		return paraArray;
	}

	/**
	 * 反向计算
	 * @param station 车站信息
	 * @return 运行数据rundataBackBeansList
	 */
	public ArrayList<Rundata> calculatorRundataBack(StationInfo station) {
		int i = 0;
		double currentSpeed = 0;// 相当于Vi+1
		double lastSpeed = 0;// 相当于Vi
		double Si = 0, S = 0;// 相当于Si+1
		String str = "";// 写入txt中的内容
		double cp = 0;// 附加阻力和
		double slopeCp = 0, curveCp = 0;// 坡道和弯道附加阻力
		ArrayList<String> strList = new ArrayList<String>();//用于保存需要打印的数据
		double power = 0;//阻力做功
		double location = 0;
		rundataBackBeansList = new ArrayList<Rundata>();
		while (true) {
			location = station.getLocation() - Si;
			str = i + "		" + currentSpeed + "		" + Si + "		" + location + "		"+power;
//			this.saveRundataBackBeans(i, currentSpeed, Si, location, power , 0, cp);
			currentSpeed = VnBpMinTime.calSpeed(currentSpeed, TrainAttribute.CRH_CAL_TIME_DIVISION, cp);
			slopeCp = ReverseOtherResistance.bySlope(Si);// 坡道附加阻力
			double [] paras = ReverseOtherResistance.byCurve(currentSpeed, Si);// 弯道附加
			currentSpeed = paras[0];
			curveCp = paras[1];
			cp = slopeCp + curveCp;//附加阻力和
			Si = SnBpMinTime.calShift(currentSpeed, lastSpeed, Si);
				
			strList.add(str);
			
			i++;
			power += BrakeForceMinTime.getComBrakingForce(currentSpeed)*(Si-S);//计算阻力做功
			lastSpeed = currentSpeed;
			S = Si;
			if (lastSpeed >= TrainAttribute.CRH_MAX_SPEED) {
				break;
			}
		}
//		WriteIntoTxt.writeIntoTxt(strList, "CRH_10.23_固定时分_反向计算.txt");
		return rundataBackBeansList;
	}
	
}
