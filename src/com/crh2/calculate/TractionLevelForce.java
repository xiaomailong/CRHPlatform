package com.crh2.calculate;

import com.crh2.javabean.TrainTractionLevelConf;

/**
 * 计算牵引力（有级位）
 * @author huhui
 *
 */
public class TractionLevelForce {
	
	public static TrainTractionLevelConf trainTractionLevelConf = null;
	
	/*
	 * 2015.1.11增加。根据不同牵引级位获取牵引力
	 */
	public static double getTractionLevelForce(int mode, double speed) {//speed单位km/h
		double F = 0.0;
		double v = speed;
		if(mode == 1){
			if (v >= 0 && v <= trainTractionLevelConf.get_1_1()) {
				F = trainTractionLevelConf.get_1_0();
			} else if (v > trainTractionLevelConf.get_1_1() && v <= trainTractionLevelConf.get_1_3()) {
				F = trainTractionLevelConf.get_1_2() / (v * v);
			}
		}else if(mode == 2){
			if (v >= 0 && v <= trainTractionLevelConf.get_2_1()) {
				F = trainTractionLevelConf.get_2_0();
			} else if (v > trainTractionLevelConf.get_2_1() && v <= trainTractionLevelConf.get_2_3()) {
				F = trainTractionLevelConf.get_2_2() / (v * v);
			}
		}else if(mode == 3){
			if (v >= 0 && v <= trainTractionLevelConf.get_3_1()) {
				F = trainTractionLevelConf.get_3_0();
			} else if (v > trainTractionLevelConf.get_3_1() && v <= trainTractionLevelConf.get_3_3()) {
				F = trainTractionLevelConf.get_3_2() / (v * v);
			}
		}else if(mode == 4){
			if(v >= 0 && v<= trainTractionLevelConf.get_4_2()){
				F = trainTractionLevelConf.get_4_0() * v + trainTractionLevelConf.get_4_1();
			} else if (v > trainTractionLevelConf.get_4_2() && v <= trainTractionLevelConf.get_4_4()){
				F = trainTractionLevelConf.get_4_3() / (v * v);
			}
		}else if(mode == 5){
			if(v >= 0 && v<= trainTractionLevelConf.get_5_2()){
				F = trainTractionLevelConf.get_5_0() * v + trainTractionLevelConf.get_5_1();
			} else if (v > trainTractionLevelConf.get_5_2() && v <= trainTractionLevelConf.get_5_4()){
				F = trainTractionLevelConf.get_5_3() / (v * v);
			}
		}else if(mode == 6){
			if(v >= 0 && v<= trainTractionLevelConf.get_6_2()){
				F = trainTractionLevelConf.get_6_0() * v + trainTractionLevelConf.get_6_1();
			} else if (v > trainTractionLevelConf.get_6_2() && v <= trainTractionLevelConf.get_6_4()){
				F = trainTractionLevelConf.get_6_3() / (v * v);
			}
		}else if(mode == 7){
			if(v >= 0 && v<= trainTractionLevelConf.get_7_2()){
				F = trainTractionLevelConf.get_7_0() * v + trainTractionLevelConf.get_7_1();
			} else if (v > trainTractionLevelConf.get_7_2() && v <= trainTractionLevelConf.get_7_4()){
				F = trainTractionLevelConf.get_7_3() / (v * v);
			}
		}else if(mode == 8){
			if(v >= 0 && v<= trainTractionLevelConf.get_8_2()){
				F = trainTractionLevelConf.get_8_0() * v + trainTractionLevelConf.get_8_1();
			} else if (v > trainTractionLevelConf.get_8_2() && v <= trainTractionLevelConf.get_8_4()){
				F = trainTractionLevelConf.get_8_3() / (v * v);
			}
		}else if(mode == 9){
			if(v >= 0 && v<= trainTractionLevelConf.get_9_2()){
				F = trainTractionLevelConf.get_9_0() * v + trainTractionLevelConf.get_9_1();
			} else if (v > trainTractionLevelConf.get_9_2() && v <= trainTractionLevelConf.get_9_4()){
				F = trainTractionLevelConf.get_9_3() / (v * v);
			}
		}else if(mode == 10){
			if(v >= 0 && v<= trainTractionLevelConf.get_10_2()){
				F = trainTractionLevelConf.get_10_0() * v + trainTractionLevelConf.get_10_1();
			} else if (v > trainTractionLevelConf.get_10_2() && v <= trainTractionLevelConf.get_10_4()){
				F = trainTractionLevelConf.get_10_3() / v;
			} else if(v > trainTractionLevelConf.get_10_4() && v<= trainTractionLevelConf.get_10_6()){
				F = trainTractionLevelConf.get_10_5() / (v * v);
			}
		}else if(mode == 11){
			if(v >= 0 && v<= trainTractionLevelConf.get_11_2()){
				F = trainTractionLevelConf.get_11_0() * v + trainTractionLevelConf.get_11_1();
			} else if (v > trainTractionLevelConf.get_11_2() && v <= trainTractionLevelConf.get_11_4()){
				F = trainTractionLevelConf.get_11_3() / v;
			} else if(v > trainTractionLevelConf.get_11_4() && v<= trainTractionLevelConf.get_11_6()){
				F = trainTractionLevelConf.get_11_5() / (v * v);
			}
		}else if(mode == 12){
			if(v >= 0 && v<= trainTractionLevelConf.get_12_2()){
				F = trainTractionLevelConf.get_12_0() * v + trainTractionLevelConf.get_12_1();
			} else if (v > trainTractionLevelConf.get_12_2() && v <= trainTractionLevelConf.get_12_4()){
				F = trainTractionLevelConf.get_12_3() / v;
			} else if(v > trainTractionLevelConf.get_12_4() && v<= trainTractionLevelConf.get_12_6()){
				F = trainTractionLevelConf.get_12_5() / (v * v);
			}
		}else if(mode == 13){
			if(v >= 0 && v<= trainTractionLevelConf.get_13_2()){
				F = trainTractionLevelConf.get_13_0() * v + trainTractionLevelConf.get_13_1();
			} else if (v > trainTractionLevelConf.get_13_2() && v <= trainTractionLevelConf.get_13_4()){
				F = trainTractionLevelConf.get_13_3() / v;
			} else if(v > trainTractionLevelConf.get_13_4() && v<= trainTractionLevelConf.get_13_6()){
				F = trainTractionLevelConf.get_13_5() / (v * v);
			}
		}else if(mode == 14){
			if(v >= 0 && v<= trainTractionLevelConf.get_14_2()){
				F = trainTractionLevelConf.get_14_0() * v + trainTractionLevelConf.get_14_1();
			} else if (v > trainTractionLevelConf.get_14_2() && v <= trainTractionLevelConf.get_14_4()){
				F = trainTractionLevelConf.get_14_3() / v;
			} else if(v > trainTractionLevelConf.get_14_4() && v<= trainTractionLevelConf.get_14_6()){
				F = trainTractionLevelConf.get_14_5() / (v * v);
			}
		}else if(mode == 15){
			if(v >= 0 && v<= trainTractionLevelConf.get_15_2()){
				F = trainTractionLevelConf.get_15_0() * v + trainTractionLevelConf.get_15_1();
			} else if (v > trainTractionLevelConf.get_15_2() && v <= trainTractionLevelConf.get_15_4()){
				F = trainTractionLevelConf.get_15_3() / v;
			}
		}
		return F;//单位是KN
	}
	
}
