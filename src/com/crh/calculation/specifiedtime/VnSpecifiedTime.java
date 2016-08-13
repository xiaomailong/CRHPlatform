package com.crh.calculation.specifiedtime;

import com.crh.calculation.mintime.CpMinTime;
import com.crh2.calculate.TrainAttribute;

/**
 * 计算列车速度Vn
 * @author huhui
 */
public class VnSpecifiedTime {
	/**
	 * /**
	 * 相当于速度计算公式中的Vn+1
	 */
	private static double Vn=0;
	 /**
	  * 根据速度公式计算实时速度
	  * @param speed 上一时刻速度
	  * @param time 计算时间间隔
	  * @param cp 加速度
	  * @param maxSpeed 最大限速
	  * @return 下一时刻速度
	  */
	public static double calSpeed(double speed, double time, double cp, double maxSpeed){
		if(speed == 0){
			Vn = ((CpMinTime.getCp(speed)-cp)/30) * time;
		}else if(0 < speed && speed <= maxSpeed ){//最大速度以下，则继续加速
			Vn = speed + ((CpMinTime.getCp(speed)-cp)/30) * time;
		}else if(speed > maxSpeed){//如果大于或者等于最大速度，则以最大速度匀速运行
			if(CpMinTime.hasElectricity == true){//如果分相区有电，匀速
				Vn = maxSpeed;
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
	
}
