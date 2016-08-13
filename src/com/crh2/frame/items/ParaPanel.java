/**
 * ���ò�����panel
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
	//����
	JLabel length = new JLabel("�ۼƾ��룺");
	static JLabel lengthValue = new JLabel();
	//ǰ����վ
	JLabel nextStation = new JLabel("ǰ����վ��");
	static JLabel nextStationValue = new JLabel();
	//����
	JLabel runType = new JLabel("������");
	static JLabel runTypeValue = new JLabel();
	//��λ����
	JLabel unitTotalForce = new JLabel("��λ������");
	static JLabel unitTotalForceValue = new JLabel();
	//���ٶ�
	JLabel accSpeed = new JLabel("���ٶ�");
	static JLabel accSpeedValue = new JLabel();
	//�ٶ�
	JLabel speed = new JLabel("�ٶȣ�");
	static JLabel speedValue = new JLabel();
	//�ۼ�ʱ��
	JLabel time = new JLabel("�ۼ�ʱ�֣�");
	static JLabel timeValue = new JLabel();
	
	//���캯����ʼ��
	public ParaPanel(){
		Font font = new Font("����", Font.BOLD, 18);
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
	
	//���ø���Panel��ֵ
	public static void setPanelValue(int id,double length,double unitTotalForce,double speed,double time,int routeId){
		String lengthStr = MyTools.numFormat(length);
		String unitTotalForceStr = MyTools.numFormat(unitTotalForce);
		lengthValue.setText(lengthStr+"km");//����length
		setNextStation(routeId,lengthStr);//����ǰ����վ
		setRunType(id,unitTotalForceStr);//���ù���
		unitTotalForceValue.setText(unitTotalForceStr+"N/KN");//���õ�λ����
		accSpeedValue.setText(MyTools.numFormat(unitTotalForce*9.8/1000)+"m/s2");
		speedValue.setText(MyTools.numFormat(speed)+"km/h");//�����ٶ�
		timeValue.setText(MyTools.timeFormat(time));//����ʱ��
	}
	
	//���ù���
	public static void setRunType(int id,String value){
		double d = Double.parseDouble(value);
		String str = "";
		if(d==0){
			str = "����";
		}else if(d>0){
			str = "ǣ��";
		}else if(d<0){
			str = "�ƶ�";
		}
		runTypeValue.setText(str);
	}
	
	//����ǰ����վ
	public static void setNextStation(int routeId,String lengthValue){
		DataService dataService = new DataService();
		List<StationInfo> stationInfoList = dataService.getStationInfoData(routeId);
		String str = "";
		String s = "";
		double length = Double.parseDouble(lengthValue);
		
		//�������г�վ����,�ӵڶ���վ��ʼ����Ϊ��һ��վ�����վ
		for(int i=1;i<stationInfoList.size();i++){
			StationInfo station = stationInfoList.get(i);
			if(length <= station.getLocation()){
				s = MyTools.numFormat(station.getLocation()-length);
//				str = station.getStationName()+",����"+s+"km";
				str = station.getStationName();
				break;//�ҵ������վ�����˳�ѭ��
			}
		}
		
		/*if(length<=20.4){
			s = MyTools.numFormat(20.4-length);
			str = "��ׯ������"+s+"km";
		}else if(length>20.4 && length<=44.75){
			s = MyTools.numFormat(44.75-length);
			str = "���֣�����"+s+"km";
		}else if(length>44.75 && length<=82.08){
			s = MyTools.numFormat(82.08-length);
			str = "���壬����"+s+"km";
		}else if(length>82.08 && length<=115.73071){
			s = MyTools.numFormat(115.73071-length);
			str = "��򣬾���"+s+"km";
		}*/
		nextStationValue.setText(str);
	}
	
}
