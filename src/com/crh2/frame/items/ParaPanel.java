/**
 * 放置参数的panel
 */
package com.crh2.frame.items;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.crh2.javabean.StationInfo;
import com.crh2.service.DataService;
import com.crh2.util.MyTools;

public class ParaPanel extends JPanel {
	//距离
	JLabel length = new JLabel("累计距离：");
	static JLabel lengthValue = new JLabel();
	//前方到站
	JLabel nextStation = new JLabel("前方到站：");
	static JLabel nextStationValue = new JLabel();
	//工况
	JLabel runType = new JLabel("工况：");
	static JLabel runTypeValue = new JLabel();
	//单位合力
	JLabel unitTotalForce = new JLabel("单位合力：");
	static JLabel unitTotalForceValue = new JLabel();
	//加速度
	JLabel accSpeed = new JLabel("加速度");
	static JLabel accSpeedValue = new JLabel();
	//速度
	JLabel speed = new JLabel("速度：");
	static JLabel speedValue = new JLabel();
	//累计时分
	JLabel time = new JLabel("累计时分：");
	static JLabel timeValue = new JLabel();
	
	//构造函数初始化
	public ParaPanel(){
		Font font = new Font("宋体", Font.BOLD, 18);
		this.setLayout(new GridLayout(7, 2,20,20));
		this.setPreferredSize(new Dimension(50, 50));
		this.add(length);this.add(lengthValue);
		length.setFont(font);lengthValue.setFont(font);
		this.add(nextStation);this.add(nextStationValue);
		nextStation.setFont(font);nextStationValue.setFont(font);
		this.add(runType);this.add(runTypeValue);
		runType.setFont(font);runTypeValue.setFont(font);
		this.add(unitTotalForce);this.add(unitTotalForceValue);
		unitTotalForce.setFont(font);unitTotalForceValue.setFont(font);
		this.add(accSpeed);this.add(accSpeedValue);
		accSpeed.setFont(font);accSpeedValue.setFont(font);
		this.add(speed);this.add(speedValue);
		speed.setFont(font);speedValue.setFont(font);
		this.add(time);this.add(timeValue);
		time.setFont(font);timeValue.setFont(font);
		
	}
	
	//设置各个Panel的值
	public static void setPanelValue(int id,double length,double unitTotalForce,double speed,double time,int routeId){
		String lengthStr = MyTools.numFormat(length);
		String unitTotalForceStr = MyTools.numFormat(unitTotalForce);
		lengthValue.setText(lengthStr+"km");//设置length
		setNextStation(routeId,lengthStr);//设置前方到站
		setRunType(id,unitTotalForceStr);//设置工况
		unitTotalForceValue.setText(unitTotalForceStr+"N/KN");//设置单位合力
		accSpeedValue.setText(MyTools.numFormat(unitTotalForce*9.8/1000)+"m/s2");
		speedValue.setText(MyTools.numFormat(speed)+"km/h");//设置速度
		timeValue.setText(MyTools.timeFormat(time));//设置时间
	}
	
	//设置工况
	public static void setRunType(int id,String value){
		double d = Double.parseDouble(value);
		String str = "";
		if(d==0){
			str = "匀速";
		}else if(d>0){
			str = "牵引";
		}else if(d<0){
			str = "制动";
		}
		runTypeValue.setText(str);
	}
	
	//设置前方到站
	public static void setNextStation(int routeId,String lengthValue){
		DataService dataService = new DataService();
		List<StationInfo> stationInfoList = dataService.getStationInfoData(routeId);
		String str = "";
		String s = "";
		double length = Double.parseDouble(lengthValue);
		
		//遍历所有车站数据,从第二个站开始，因为第一个站是起点站
		for(int i=1;i<stationInfoList.size();i++){
			StationInfo station = stationInfoList.get(i);
			if(length <= station.getLocation()){
				s = MyTools.numFormat(station.getLocation()-length);
//				str = station.getStationName()+",距离"+s+"km";
				str = station.getStationName();
				break;//找到最近的站，就退出循环
			}
		}
		
		/*if(length<=20.4){
			s = MyTools.numFormat(20.4-length);
			str = "亦庄，距离"+s+"km";
		}else if(length>20.4 && length<=44.75){
			s = MyTools.numFormat(44.75-length);
			str = "永乐，距离"+s+"km";
		}else if(length>44.75 && length<=82.08){
			s = MyTools.numFormat(82.08-length);
			str = "武清，距离"+s+"km";
		}else if(length>82.08 && length<=115.73071){
			s = MyTools.numFormat(115.73071-length);
			str = "天津，距离"+s+"km";
		}*/
		nextStationValue.setText(str);
	}
	
}
