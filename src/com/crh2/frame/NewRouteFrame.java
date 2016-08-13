/**
 * �½�·�ߣ���Ϊ�������֣�1.��������  2.���ݵ���
 */
package com.crh2.frame;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.crh.calculation.mintime.brakepoint.ReverseRunDataCalculatorMinTime;
import com.crh2.calculate.MergeLists;
import com.crh2.calculate.TrainAttribute;
import com.crh2.calculate.excel.ExcelAction;
import com.crh2.dao.SQLHelper;
import com.crh2.javabean.RouteName;
import com.crh2.javabean.Rundata;
import com.crh2.service.PathFileService;
import com.crh2.util.MyUtillity;

public class NewRouteFrame extends JDialog implements ActionListener {

	/**
	 * @param args
	 */
	//��������panel
	JPanel parameterPanel,dataImportPanel,buttonPanel;
	//����label
	JLabel newRouteNameLabel,trainTypeLabel,speedLimitLabel;
	//����combobox
	JComboBox trainTypeComboBox;
	//����textfield
	JTextField newRouteNameField,speedLimitField,excelFilePathField;
	//�����ļ�ѡ����
	JFileChooser excelFileChooser;
	//�����ļ�������
	FileNameExtensionFilter fileFilter;
	//����button
	JButton fileChooseButton,calDataButton,finishButton;
	//����SQLHelper
	SQLHelper sqlHelper = new SQLHelper();
	//��ǰrouteid
	int generatedId = TrainAttribute.CRH_DEFAULT_ROUTE_ID;
	//���ֳ��͵�����
	HashMap<String,Double> trainLimitSpeedMap = null;
	
	
	// ���캯������ʼ�������ؼ�������
	public NewRouteFrame(Frame owner, boolean modal){
		super(owner,modal);
		//������������
		parameterPanel = new JPanel(new GridLayout(3, 2,10,9));
		newRouteNameLabel = new JLabel("      ��·���ƣ�");
		newRouteNameField = new JTextField();
		trainTypeLabel = new JLabel("      �������ã�");
		trainTypeComboBox = new JComboBox();
		trainTypeComboBox.setModel(new DefaultComboBoxModel(new String[]{"4��4��","1��7��","2��6��","3��5��","5��3��","6��2��","7��1��","8��0��"," 250km/h����"}));//���ó�������
		speedLimitLabel = new JLabel("      �������ã�");
		speedLimitField = new JTextField("300");
		parameterPanel.add(newRouteNameLabel);
		parameterPanel.add(newRouteNameField);
		parameterPanel.add(trainTypeLabel);
		parameterPanel.add(trainTypeComboBox);
		parameterPanel.add(speedLimitLabel);
		parameterPanel.add(speedLimitField);
		parameterPanel.setBorder(BorderFactory.createTitledBorder("��������"));
		
		//���ݵ�������
		dataImportPanel = new JPanel();
		excelFilePathField = new JTextField(49);
		excelFilePathField.setEditable(false);//����Ϊ���ɱ༭
		fileChooseButton = new JButton("�ļ�ѡ��");
		fileChooseButton.addActionListener(this);
		fileChooseButton.setActionCommand("ChooseFile");
		calDataButton = new JButton("�������");
		calDataButton.addActionListener(this);
		calDataButton.setActionCommand("Calculate");
		dataImportPanel.add(excelFilePathField,BorderLayout.WEST);
		dataImportPanel.add(fileChooseButton,BorderLayout.EAST);
		dataImportPanel.add(calDataButton,BorderLayout.SOUTH);
		dataImportPanel.setBorder(BorderFactory.createTitledBorder("���ݵ���"));
		
		//����ɡ�����
		buttonPanel = new JPanel();
		finishButton = new JButton("���");
		//�Ȱ�finishButton���ó�false���ȼ�����ɣ��ڱ�Ϊtrue
		finishButton.setEnabled(false);
		finishButton.addActionListener(this);
		finishButton.setActionCommand("Finish");
		buttonPanel.add(finishButton);
		
		//��ʼ���г����ٵ�map
		this.initTrainLimitSpeedMap();
		
		//����frame
		this.setTitle("�½���·");
		this.add(parameterPanel,BorderLayout.NORTH);
		this.add(dataImportPanel,BorderLayout.CENTER);
		this.add(buttonPanel,BorderLayout.SOUTH);
		this.setSize(430, 300);
		MyUtillity.setFrameOnCenter(this);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setResizable(false);
		this.setVisible(true);
		
	}
	
	//������ļ�ѡ�񡱰�ť�����
	public void fileChooseButtonClicked(){
		excelFileChooser = new JFileChooser();
		fileFilter = new FileNameExtensionFilter(".xls", "xls");
		excelFileChooser.setFileFilter(fileFilter);
		excelFileChooser.setVisible(true);//��ʾ
		int retval = excelFileChooser.showOpenDialog(this);
		 if(retval ==JFileChooser.APPROVE_OPTION){
			//�õ��û�ѡ���ļ��ľ���·��
			String filePath = excelFileChooser.getSelectedFile().getAbsolutePath();
			//���ļ�·���ı���ֵ
			excelFilePathField.setText(filePath.trim());
		 }
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		NewRouteFrame newRouteFrame = new NewRouteFrame(null,false);
	}
	
	//�ж��Ƿ񳬹�����
	public boolean isBeyondSpeedLimit(String trainTypeStr,String speedStr){
		if(trainLimitSpeedMap.containsKey(trainTypeStr)){
			double speedLimit = trainLimitSpeedMap.get(trainTypeStr);
			if(Double.parseDouble(speedStr)>speedLimit){
				JOptionPane.showMessageDialog(this, "��ѡ����г������� "+trainTypeStr+" ,����ٶȲ��ܳ��� "+speedLimit+" km/h");
				return false;
			}else{
				return true;
			}
		}else{
			return false;
		}
	}
	
	//��ʼ�����ֳ��͵�����ֵ
	public void initTrainLimitSpeedMap(){
		trainLimitSpeedMap = new HashMap<String,Double>();
		trainLimitSpeedMap.put("4��4��", 350.0);
		trainLimitSpeedMap.put("1��7��", 200.0);
		trainLimitSpeedMap.put("2��6��", 250.0);
		trainLimitSpeedMap.put("3��5��", 300.0);
		trainLimitSpeedMap.put("5��3��", 350.0);
		trainLimitSpeedMap.put("6��2��", 350.0);
		trainLimitSpeedMap.put("7��1��", 350.0);
		trainLimitSpeedMap.put("8��0��", 350.0);
		trainLimitSpeedMap.put(" 250km/h����", 250.0);
	}
	
	//�����µ�·������
	public int saveNewRouteName(String routeName,String trainType){
		int i = -1;
		//���Ȳ���
		String sql = "SELECT COUNT(*) FROM routename WHERE NAME = '"+routeName+"'";
		List list = sqlHelper.query(sql, null);
		Object object[] = (Object[])list.get(0);
		if(Integer.parseInt(object[0].toString()) == 0){//�������
			sql = "INSERT INTO routename(NAME,TRAINTYPEID) VALUES('"+routeName+"',"+MyUtillity.getTrainIdByTrainType(trainType)+")";
			i = sqlHelper.insertAndGetId(sql);
			return i;
		}else{//˵�����ظ���·����
			JOptionPane.showMessageDialog(this, "��·���ظ�������������");
			return i;
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getActionCommand().equals("Finish")){//�������ɡ�
			String routeNameStr = newRouteNameField.getText().trim();
			//Ϊ�˸��²˵�������·��Ϣ
			RouteName routeName = new RouteName();
			routeName.setId(generatedId);
			routeName.setName(routeNameStr);
			CRHSimulation owner = (CRHSimulation) this.getOwner();
			owner.addNewLineToRouteMenu(routeName);
			this.dispose();
		}else if(e.getActionCommand().equals("ChooseFile")){//������ļ�ѡ��
			this.fileChooseButtonClicked();
		}else if(e.getActionCommand().equals("Calculate")){//�������ʼ���㡱
			String routeNameStr = newRouteNameField.getText().trim();
			String speedLimitStr = speedLimitField.getText().trim();
			String excelFilePathStr = excelFilePathField.getText().trim();
			String trainTypeValueStr = (String)trainTypeComboBox.getSelectedItem();
			//1.�����·���Ƽ�����ֵ
			if(MyUtillity.isBlankString(routeNameStr)){
				JOptionPane.showMessageDialog(this, "��·���Ʋ���Ϊ��");
				return;
			}
			if(MyUtillity.isBlankString(speedLimitStr) || !MyUtillity.isNumber(speedLimitStr) || Double.parseDouble(speedLimitStr)<=0){//����Ϊ���ұ���ȫ������
				JOptionPane.showMessageDialog(this, "������ֵ����");
				return;
			}
			if(!this.isBeyondSpeedLimit(trainTypeValueStr, speedLimitStr)){
				return;
			}
			//2.����ļ�·��
			if(MyUtillity.isBlankString(excelFilePathStr)){
				JOptionPane.showMessageDialog(this, "�����ļ�·������");
				return;
			}
			//3.������·����
			generatedId = this.saveNewRouteName(routeNameStr,trainTypeValueStr);
			if(generatedId == -1){//�������ʧ��
				return;
			}
			//4.��ʼ����
			//4.1�ȵõ��ղű����·�ߵ�id��trainTypeValueStr
			int routeId = generatedId;
//			TrainAttribute.CRH_TRAIN_TYPE = MyUtillity.getFirstNumFromString(trainTypeValueStr);
			//4.2��ȡexcelֵ
			ExcelAction excelAction = new ExcelAction(routeId,excelFilePathStr);
			if(!excelAction.readDataFromExcel()){
				JOptionPane.showMessageDialog(this, "�����ļ���ȡ����");
				return;
			}
			//��¼ѡ��ĳ�������
			PathFileService.writeIntoPathTxt(trainTypeValueStr);
			//4.3��ʼ����rundata
			//�����ü�������ֵ
			MyUtillity.assignmentParameters(excelAction.getStationInfoList(), Double.parseDouble(speedLimitStr));
			//4.3.1�ȷ�����㣬�����ҵ�ͣ��ɲ����
			/*ReverseRunDataCalculatorMinTime lastBrakePointCalculator = new ReverseRunDataCalculatorMinTime(excelAction.getSlopeList(), excelAction.getCurveList());
			ArrayList<Rundata> rundataBackBeansList = lastBrakePointCalculator.calculatorRundataBack();//�õ������������
			//4.3.2������㣬���ڼ���rundata����
			RunDataCalculatorRealTime runDataCalculator = new RunDataCalculatorRealTime(excelAction.getSectionList(), excelAction.getSlopeList(), excelAction.getCurveList(), excelAction.getSpecialSpeedLimitPointList());
			runDataCalculator.setStopPoint(lastBrakePointCalculator.getStopPoint());//�����������ֹͣ��
			ArrayList<Rundata> rundataBeansList = runDataCalculator.calculateRunData();//�õ������������
			//4.3.3�������������õ������ݽ��кϲ�
			MergeLists mergeLists = new MergeLists(rundataBackBeansList,rundataBeansList);
			mergeLists.saveMergedData(routeId);*/
//			mergeLists.displayMergedList();
			JOptionPane.showMessageDialog(this, "�������");
			finishButton.setEnabled(true);
			//ÿ�μ����꽫�г���Ϊ������״̬
			TrainAttribute.CRH_IS_CONSTANT_SPEED = false;
		}
	}
	
}
