package com.crh.calculation.mintime;

import com.crh2.calculate.TrainAttribute;

/**
 * 计算列车速度Vn
 * @author huhui
 */
public class VnMinTime {
	
	/**
	 * 实时运行的Vn
	 */
	public static double VnRT = 0;
	
	/**
	 * 计算速度
	 * @param speed
	 * @param time
	 * @param cp
	 * @return
	 */
	public static double calSpeed(double speed,double time,double cp){
		double Vn = 0;
		if(speed == 0){
			Vn = ((CpMinTime.getCp(speed)-cp)/30) * time;
		}else if(0 < speed && speed < TrainAttribute.CRH_MAX_SPEED ){//最大速度以下，则继续加速
			Vn = speed + ((CpMinTime.getCp(speed)-cp)/30) * time;
		}else if(speed >= TrainAttribute.CRH_MAX_SPEED){//如果大于或者等于最大速度，则以最大速度匀速运行
			if(CpMinTime.hasElectricity == true){//如果分相区有电，匀速
				Vn = TrainAttribute.CRH_MAX_SPEED;
				//设置动车组状态为匀速
				TrainAttribute.CRH_IS_CONSTANT_SPEED= true;
			}else{//如果没电，按照正常计算
				Vn = speed + ((CpMinTime.getCp(speed)-cp)/30) * time;
				//设置动车组状态为非匀速
				TrainAttribute.CRH_IS_CONSTANT_SPEED= false;
			}
		}
		return Vn;
	}
	
	/**
	 *  2014.10.25 惰行求速度
	 * @param speed
	 * @param time
	 * @param cp
	 * @return
	 */
	public static double calSpeedSlow(double speed,double time,double cp){
		double Vn = 0; //相当于Vn+1
		if(0 < speed && speed < TrainAttribute.CRH_MAX_SPEED ){//最大速度以下，则继续加速
			Vn = speed + ((CpMinTime.getCpSlow(speed)-cp)/30) * time;
		}
		return Vn;
	}
	
	/**
	 * 2014.10.29 常态运行求速度
	 * @param speed
	 * @param time
	 * @param cp
	 * @param brakeLevel
	 * @return
	 */
	public static double calSpeedRealTime(double speed,double time,double cp, int brakeLevel){
		if(speed == 0){
			VnRT = ((CpMinTime.getCpRealTime(speed, brakeLevel)-cp)/30) * time;
		}else if(0 < speed && speed < TrainAttribute.CRH_MAX_SPEED ){//最大速度以下，则继续加速
			double tempV = ((CpMinTime.getCpRealTime(speed, brakeLevel)-cp)/30) * time;
			if(TrainAttribute.CRH_IS_BRAKE){
				VnRT = speed - tempV;
			}else{
				VnRT = speed + tempV;
			}
		}else if(speed >= TrainAttribute.CRH_MAX_SPEED){//如果大于或者等于最大速度，则以最大速度匀速运行
			VnRT = TrainAttribute.CRH_MAX_SPEED;
			if(TrainAttribute.CRH_IS_BRAKE){
				VnRT -= 0.001;
			}
		}
		return VnRT < 0 ? 0 : VnRT;
	}
	
}
