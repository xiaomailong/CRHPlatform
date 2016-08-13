/**
 * 新建路线，分为两个部分，1.参数设置  2.数据导入
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
	//定义三个panel
	JPanel parameterPanel,dataImportPanel,buttonPanel;
	//定义label
	JLabel newRouteNameLabel,trainTypeLabel,speedLimitLabel;
	//定义combobox
	JComboBox trainTypeComboBox;
	//定义textfield
	JTextField newRouteNameField,speedLimitField,excelFilePathField;
	//定义文件选择器
	JFileChooser excelFileChooser;
	//定义文件过滤器
	FileNameExtensionFilter fileFilter;
	//定义button
	JButton fileChooseButton,calDataButton,finishButton;
	//定义SQLHelper
	SQLHelper sqlHelper = new SQLHelper();
	//当前routeid
	int generatedId = TrainAttribute.CRH_DEFAULT_ROUTE_ID;
	//各种车型的限速
	HashMap<String,Double> trainLimitSpeedMap = null;
	
	
	// 构造函数，初始化各个控件及窗口
	public NewRouteFrame(Frame owner, boolean modal){
		super(owner,modal);
		//参数设置区域
		parameterPanel = new JPanel(new GridLayout(3, 2,10,9));
		newRouteNameLabel = new JLabel("      线路名称：");
		newRouteNameField = new JTextField();
		trainTypeLabel = new JLabel("      车辆设置：");
		trainTypeComboBox = new JComboBox();
		trainTypeComboBox.setModel(new DefaultComboBoxModel(new String[]{"4动4拖","1动7拖","2动6拖","3动5拖","5动3拖","6动2拖","7动1拖","8动0拖"," 250km/h车型"}));//设置车辆类型
		speedLimitLabel = new JLabel("      限速设置：");
		speedLimitField = new JTextField("300");
		parameterPanel.add(newRouteNameLabel);
		parameterPanel.add(newRouteNameField);
		parameterPanel.add(trainTypeLabel);
		parameterPanel.add(trainTypeComboBox);
		parameterPanel.add(speedLimitLabel);
		parameterPanel.add(speedLimitField);
		parameterPanel.setBorder(BorderFactory.createTitledBorder("参数设置"));
		
		//数据导入区域
		dataImportPanel = new JPanel();
		excelFilePathField = new JTextField(49);
		excelFilePathField.setEditable(false);//设置为不可编辑
		fileChooseButton = new JButton("文件选择");
		fileChooseButton.addActionListener(this);
		fileChooseButton.setActionCommand("ChooseFile");
		calDataButton = new JButton("导入完成");
		calDataButton.addActionListener(this);
		calDataButton.setActionCommand("Calculate");
		dataImportPanel.add(excelFilePathField,BorderLayout.WEST);
		dataImportPanel.add(fileChooseButton,BorderLayout.EAST);
		dataImportPanel.add(calDataButton,BorderLayout.SOUTH);
		dataImportPanel.setBorder(BorderFactory.createTitledBorder("数据导入"));
		
		//“完成”区域
		buttonPanel = new JPanel();
		finishButton = new JButton("完成");
		//先把finishButton设置成false，等计算完成，在变为true
		finishButton.setEnabled(false);
		finishButton.addActionListener(this);
		finishButton.setActionCommand("Finish");
		buttonPanel.add(finishButton);
		
		//初始化列车限速的map
		this.initTrainLimitSpeedMap();
		
		//设置frame
		this.setTitle("新建线路");
		this.add(parameterPanel,BorderLayout.NORTH);
		this.add(dataImportPanel,BorderLayout.CENTER);
		this.add(buttonPanel,BorderLayout.SOUTH);
		this.setSize(430, 300);
		MyUtillity.setFrameOnCenter(this);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setResizable(false);
		this.setVisible(true);
		
	}
	
	//如果“文件选择”按钮被点击
	public void fileChooseButtonClicked(){
		excelFileChooser = new JFileChooser();
		fileFilter = new FileNameExtensionFilter(".xls", "xls");
		excelFileChooser.setFileFilter(fileFilter);
		excelFileChooser.setVisible(true);//显示
		int retval = excelFileChooser.showOpenDialog(this);
		 if(retval ==JFileChooser.APPROVE_OPTION){
			//得到用户选择文件的绝对路径
			String filePath = excelFileChooser.getSelectedFile().getAbsolutePath();
			//给文件路径文本框赋值
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
	
	//判断是否超过限速
	public boolean isBeyondSpeedLimit(String trainTypeStr,String speedStr){
		if(trainLimitSpeedMap.containsKey(trainTypeStr)){
			double speedLimit = trainLimitSpeedMap.get(trainTypeStr);
			if(Double.parseDouble(speedStr)>speedLimit){
				JOptionPane.showMessageDialog(this, "您选择的列车类型是 "+trainTypeStr+" ,最高速度不能超过 "+speedLimit+" km/h");
				return false;
			}else{
				return true;
			}
		}else{
			return false;
		}
	}
	
	//初始化各种车型的限速值
	public void initTrainLimitSpeedMap(){
		trainLimitSpeedMap = new HashMap<String,Double>();
		trainLimitSpeedMap.put("4动4拖", 350.0);
		trainLimitSpeedMap.put("1动7拖", 200.0);
		trainLimitSpeedMap.put("2动6拖", 250.0);
		trainLimitSpeedMap.put("3动5拖", 300.0);
		trainLimitSpeedMap.put("5动3拖", 350.0);
		trainLimitSpeedMap.put("6动2拖", 350.0);
		trainLimitSpeedMap.put("7动1拖", 350.0);
		trainLimitSpeedMap.put("8动0拖", 350.0);
		trainLimitSpeedMap.put(" 250km/h车型", 250.0);
	}
	
	//保存新的路线名称
	public int saveNewRouteName(String routeName,String trainType){
		int i = -1;
		//首先查重
		String sql = "SELECT COUNT(*) FROM routename WHERE NAME = '"+routeName+"'";
		List list = sqlHelper.query(sql, null);
		Object object[] = (Object[])list.get(0);
		if(Integer.parseInt(object[0].toString()) == 0){//可以添加
			sql = "INSERT INTO routename(NAME,TRAINTYPEID) VALUES('"+routeName+"',"+MyUtillity.getTrainIdByTrainType(trainType)+")";
			i = sqlHelper.insertAndGetId(sql);
			return i;
		}else{//说明有重复线路名称
			JOptionPane.showMessageDialog(this, "线路名重复，请重新输入");
			return i;
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getActionCommand().equals("Finish")){//点击“完成”
			String routeNameStr = newRouteNameField.getText().trim();
			//为了更新菜单栏的线路信息
			RouteName routeName = new RouteName();
			routeName.setId(generatedId);
			routeName.setName(routeNameStr);
			CRHSimulation owner = (CRHSimulation) this.getOwner();
			owner.addNewLineToRouteMenu(routeName);
			this.dispose();
		}else if(e.getActionCommand().equals("ChooseFile")){//点击“文件选择”
			this.fileChooseButtonClicked();
		}else if(e.getActionCommand().equals("Calculate")){//点击“开始计算”
			String routeNameStr = newRouteNameField.getText().trim();
			String speedLimitStr = speedLimitField.getText().trim();
			String excelFilePathStr = excelFilePathField.getText().trim();
			String trainTypeValueStr = (String)trainTypeComboBox.getSelectedItem();
			//1.检查线路名称及限速值
			if(MyUtillity.isBlankString(routeNameStr)){
				JOptionPane.showMessageDialog(this, "线路名称不能为空");
				return;
			}
			if(MyUtillity.isBlankString(speedLimitStr) || !MyUtillity.isNumber(speedLimitStr) || Double.parseDouble(speedLimitStr)<=0){//不能为空且必须全是数字
				JOptionPane.showMessageDialog(this, "限速数值有误");
				return;
			}
			if(!this.isBeyondSpeedLimit(trainTypeValueStr, speedLimitStr)){
				return;
			}
			//2.检查文件路径
			if(MyUtillity.isBlankString(excelFilePathStr)){
				JOptionPane.showMessageDialog(this, "数据文件路径错误");
				return;
			}
			//3.保存线路名称
			generatedId = this.saveNewRouteName(routeNameStr,trainTypeValueStr);
			if(generatedId == -1){//如果保存失败
				return;
			}
			//4.开始计算
			//4.1先得到刚才保存的路线的id和trainTypeValueStr
			int routeId = generatedId;
//			TrainAttribute.CRH_TRAIN_TYPE = MyUtillity.getFirstNumFromString(trainTypeValueStr);
			//4.2读取excel值
			ExcelAction excelAction = new ExcelAction(routeId,excelFilePathStr);
			if(!excelAction.readDataFromExcel()){
				JOptionPane.showMessageDialog(this, "数据文件读取错误");
				return;
			}
			//记录选择的车辆类型
			PathFileService.writeIntoPathTxt(trainTypeValueStr);
			//4.3开始计算rundata
			//先设置几个常量值
			MyUtillity.assignmentParameters(excelAction.getStationInfoList(), Double.parseDouble(speedLimitStr));
			//4.3.1先反向计算，用于找到停车刹车点
			/*ReverseRunDataCalculatorMinTime lastBrakePointCalculator = new ReverseRunDataCalculatorMinTime(excelAction.getSlopeList(), excelAction.getCurveList());
			ArrayList<Rundata> rundataBackBeansList = lastBrakePointCalculator.calculatorRundataBack();//拿到反向计算数据
			//4.3.2正向计算，用于计算rundata数据
			RunDataCalculatorRealTime runDataCalculator = new RunDataCalculatorRealTime(excelAction.getSectionList(), excelAction.getSlopeList(), excelAction.getCurveList(), excelAction.getSpecialSpeedLimitPointList());
			runDataCalculator.setStopPoint(lastBrakePointCalculator.getStopPoint());//设置正向计算停止点
			ArrayList<Rundata> rundataBeansList = runDataCalculator.calculateRunData();//拿到正向计算数据
			//4.3.3将正、反向计算得到的数据进行合并
			MergeLists mergeLists = new MergeLists(rundataBackBeansList,rundataBeansList);
			mergeLists.saveMergedData(routeId);*/
//			mergeLists.displayMergedList();
			JOptionPane.showMessageDialog(this, "计算完成");
			finishButton.setEnabled(true);
			//每次计算完将列车置为非匀速状态
			TrainAttribute.CRH_IS_CONSTANT_SPEED = false;
		}
	}
	
}
