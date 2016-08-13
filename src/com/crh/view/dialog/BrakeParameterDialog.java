package com.crh.view.dialog;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import com.crh.service.BrakeConfPanelService;
import com.crh.service.TractionConfPanelService;
import com.crh2.javabean.TrainBrakeFactor;
import com.crh2.javabean.TrainElectricBrake;
import com.crh2.util.MyTools;
import com.crh2.util.MyUtillity;

/**
 * 设置制动特性参数的对话框类
 * @author huhui
 *
 */
public class BrakeParameterDialog extends JDialog {
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private int categoryId;

	/**
	 *  存放三个参数
	 */
	private double[] trainParameters;

	/**
	 *  “减速度设置”的Panel
	 */
	private JPanel slowDownPanel = null;
	/**
	 * “再生制动设置”的Panel
	 */
	private JPanel electricBrakePanel = null;

	private static JTable brakeFactorTable;
	/**
	 * 列名
	 */
	private Vector<String> columnNamesVector;
	private JScrollPane brakeFactorTableScrollPane;
	public static DefaultTableModel brakeFactorTableModel;
	private JTextField v1;
	private JTextField v2;
	private JTextField P00;
	private JTextField N;
	private JTextField F11;
	private JTextField k1;
	private JTextField k2;
	private JTextField D;
	private JTextField T;
	private JTextField P11;

	/**
	 * 构造函数，创建Dialog
	 * @param trainCategoryId
	 * @param trainCategory
	 */
	public BrakeParameterDialog(final int trainCategoryId, String trainCategory) {
		setTitle("制动特性设置");
		categoryId = trainCategoryId;
		setBounds(100, 100, 1000, 581);
		getContentPane().setLayout(null);

		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(Color.LIGHT_GRAY));
		panel.setBounds(10, 10, 960, 35);
		getContentPane().add(panel);
		panel.setLayout(null);

		JLabel label = new JLabel("\u5217\u8F66\u7F16\u7EC4\u540D\u79F0\uFF1A");
		label.setBounds(10, 10, 84, 15);
		panel.add(label);

		textField = new JTextField();
		textField.setEditable(false);
		textField.setText(trainCategory);
		textField.setBounds(104, 7, 94, 21);
		panel.add(textField);
		textField.setColumns(10);

		final JPanel panel_1 = new JPanel();
		panel_1.setLayout(null);
		panel_1.setBorder(new LineBorder(Color.LIGHT_GRAY));
		panel_1.setBounds(10, 55, 960, 446);
		getContentPane().add(panel_1);

		JLabel label_1 = new JLabel(
				"\u6700\u9AD8\u8FD0\u884C\u901F\u5EA6\uFF1A");
		label_1.setBounds(10, 10, 84, 15);
		panel_1.add(label_1);

		// 获取三个列车参数
		trainParameters = BrakeConfPanelService
				.getThreeParameters(trainCategoryId);

		textField_1 = new JTextField();
		textField_1.setText(trainParameters[0] + "");
		textField_1.setEditable(false);
		textField_1.setBounds(104, 7, 66, 21);
		panel_1.add(textField_1);
		textField_1.setColumns(10);

		JLabel lblKmh = new JLabel("km/h");
		lblKmh.setBounds(180, 10, 54, 15);
		panel_1.add(lblKmh);

		JLabel label_2 = new JLabel("\u5217\u8F66\u8D28\u91CF\uFF1A");
		label_2.setBounds(222, 10, 84, 15);
		panel_1.add(label_2);

		textField_2 = new JTextField();
		textField_2.setText(trainParameters[1] + "");
		textField_2.setEditable(false);
		textField_2.setColumns(10);
		textField_2.setBounds(288, 7, 66, 21);
		panel_1.add(textField_2);

		JLabel lblTon = new JLabel("ton");
		lblTon.setBounds(364, 10, 54, 15);
		panel_1.add(lblTon);

		JLabel label_3 = new JLabel("\u60EF\u6027\u7CFB\u6570\uFF1A");
		label_3.setBounds(407, 10, 84, 15);
		panel_1.add(label_3);

		textField_3 = new JTextField();
		textField_3.setText(trainParameters[2] + "");
		textField_3.setEditable(false);
		textField_3.setColumns(10);
		textField_3.setBounds(473, 7, 66, 21);
		panel_1.add(textField_3);

		final JToggleButton speedCutButton = new JToggleButton("减速度设置");
		speedCutButton.setSelected(true);
		final JToggleButton brakingButton = new JToggleButton("再生制动设置");

		speedCutButton.setBounds(10, 35, 125, 23);
		panel_1.add(speedCutButton);

		brakingButton.setBounds(145, 35, 125, 23);
		panel_1.add(brakingButton);

		// “减速度设置”panel
		slowDownPanel = new JPanel();
		slowDownPanel.setBorder(new LineBorder(Color.WHITE));
		slowDownPanel.setBounds(10, 68, 945, 368);
		panel_1.add(slowDownPanel);
		slowDownPanel.setLayout(new GridLayout(1, 1, 0, 0));
		initSlowDownPanel(trainCategoryId);
		slowDownPanel.add(brakeFactorTableScrollPane);

		// “再生制动设置”panel
		electricBrakePanel = new JPanel();
		electricBrakePanel.setBorder(new LineBorder(Color.WHITE));
		electricBrakePanel.setBounds(10, 68, 795, 368);
		electricBrakePanel.setLayout(null);
//		panel_1.add(electricBrakePanel);
		
		JLabel label_4 = new JLabel("\u7535\u5236\u52A8\u7EBF\u6027\u8870\u51CF\u8F6C\u6298\u70B9\uFF1A");
		label_4.setBounds(10, 198, 191, 15);
		electricBrakePanel.add(label_4);
		
		v1 = new JTextField();
		v1.setBounds(271, 195, 162, 21);
		electricBrakePanel.add(v1);
		v1.setColumns(10);
		
		JLabel lblKmh_1 = new JLabel("km/h");
		lblKmh_1.setBounds(443, 198, 54, 15);
		electricBrakePanel.add(lblKmh_1);
		
		JLabel label_5 = new JLabel("\u7535\u5236\u52A8\u6052\u529F\u533A\u548C\u6052\u8F6C\u77E9\u533A\u8F6C\u6298\u70B9\uFF1A");
		label_5.setBounds(10, 239, 191, 15);
		electricBrakePanel.add(label_5);
		
		v2 = new JTextField();
		v2.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				calculateF11();
			}
		});
		v2.setColumns(10);
		v2.setBounds(271, 236, 162, 21);
		electricBrakePanel.add(v2);
		
		JLabel label_6 = new JLabel("km/h");
		label_6.setBounds(443, 239, 54, 15);
		electricBrakePanel.add(label_6);
		
		JLabel label_7 = new JLabel("\u7535\u5236\u52A8\u6BCF\u53F0\u7535\u673A\u6700\u5927\u8F6E\u5468\u7275\u5F15\u529F\u7387\uFF1A");
		label_7.setBounds(10, 270, 204, 15);
		electricBrakePanel.add(label_7);
		
		P00 = new JTextField();
		P00.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				calculateP11();
				calculateF11();
			}
		});
		P00.setColumns(10);
		P00.setBounds(271, 267, 162, 21);
		electricBrakePanel.add(P00);
		
		JLabel lblKw = new JLabel("kw");
		lblKw.setBounds(443, 270, 54, 15);
		electricBrakePanel.add(lblKw);
		
		JLabel label_8 = new JLabel("\u6574\u8F66\u7535\u673A\u6570\u91CF\uFF1A");
		label_8.setBounds(10, 122, 204, 15);
		electricBrakePanel.add(label_8);
		
		N = new JTextField();
		N.setEditable(false);
		N.setColumns(10);
		N.setBounds(271, 119, 162, 21);
		electricBrakePanel.add(N);
		
		JLabel label_10 = new JLabel("\u7535\u5236\u52A8\u6052\u529F\u533A\u548C\u6052\u8F6C\u77E9\u533A\u8F6C\u6298\u70B9\u5BF9\u5E94\u7684\u7275\u5F15\u529B\uFF1A");
		label_10.setBounds(10, 340, 263, 15);
		electricBrakePanel.add(label_10);
		
		F11 = new JTextField();
		F11.setEditable(false);
		F11.setColumns(10);
		F11.setBounds(271, 337, 162, 21);
		electricBrakePanel.add(F11);
		
		JLabel lblKn = new JLabel("KN");
		lblKn.setBounds(443, 340, 54, 15);
		electricBrakePanel.add(lblKn);
		
		JLabel label_12 = new JLabel("\u4F20\u52A8\u88C5\u7F6E\u6548\u7387\uFF1A");
		label_12.setBounds(10, 13, 204, 15);
		electricBrakePanel.add(label_12);
		
		k1 = new JTextField();
		k1.setEditable(false);
		k1.setColumns(10);
		k1.setBounds(271, 10, 162, 21);
		electricBrakePanel.add(k1);
		
		JLabel label_13 = new JLabel("\u9F7F\u8F6E\u4F20\u52A8\u6BD4\uFF1A");
		label_13.setBounds(10, 47, 204, 15);
		electricBrakePanel.add(label_13);
		
		k2 = new JTextField();
		k2.setEditable(false);
		k2.setColumns(10);
		k2.setBounds(271, 44, 162, 21);
		electricBrakePanel.add(k2);
		
		JLabel label_14 = new JLabel("\u8F6E\u5F84\uFF1A");
		label_14.setBounds(10, 82, 204, 15);
		electricBrakePanel.add(label_14);
		
		D = new JTextField();
		D.setEditable(false);
		D.setColumns(10);
		D.setBounds(271, 79, 162, 21);
		electricBrakePanel.add(D);
		
		JLabel label_15 = new JLabel("\u7535\u673A\u542F\u52A8\u8F6C\u77E9\uFF1A");
		label_15.setBounds(10, 157, 204, 15);
		electricBrakePanel.add(label_15);
		
		T = new JTextField();
		T.setEditable(false);
		T.setColumns(10);
		T.setBounds(271, 154, 162, 21);
		electricBrakePanel.add(T);
		
		JLabel lblNm = new JLabel("NM");
		lblNm.setBounds(443, 157, 54, 15);
		electricBrakePanel.add(lblNm);
		
		JLabel lblM = new JLabel("m");
		lblM.setBounds(443, 82, 54, 15);
		electricBrakePanel.add(lblM);
		
		JLabel label_9 = new JLabel("\u7535\u5236\u52A8\u5217\u8F66\u6700\u5927\u8F6E\u5468\u7275\u5F15\u529F\u7387\uFF1A");
		label_9.setBounds(10, 307, 204, 15);
		electricBrakePanel.add(label_9);
		
		P11 = new JTextField();
		P11.setEditable(false);
		P11.setColumns(10);
		P11.setBounds(271, 304, 162, 21);
		electricBrakePanel.add(P11);
		
		JLabel label_16 = new JLabel("kw");
		label_16.setBounds(443, 307, 54, 15);
		electricBrakePanel.add(label_16);
		
		final JButton importLinesBtn = new JButton("导入");
		importLinesBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new ImportBrakeFactorFromExcelDialog(BrakeParameterDialog.this, trainCategoryId);
			}
		});
		importLinesBtn.setBounds(170, 510, 93, 23);
		getContentPane().add(importLinesBtn);

		final JButton appendLineBtn = new JButton("增加");
		appendLineBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				appendOneLine();
			}
		});
		appendLineBtn.setBounds(300, 510, 93, 23);
		getContentPane().add(appendLineBtn);

		final JButton removeLineBtn = new JButton("删除");
		removeLineBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeOneLine();
			}
		});
		removeLineBtn.setBounds(430, 510, 93, 23);
		getContentPane().add(removeLineBtn);

		JButton button_2 = new JButton("保存");
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(speedCutButton.isSelected()){
					BrakeConfPanelService.saveBrakeFactor(categoryId, getBrakeFactor(categoryId));
				}else if(brakingButton.isSelected()){
					saveElectricBrakePanel(categoryId);
				}
				JOptionPane.showMessageDialog(BrakeParameterDialog.this, "保存成功！");
			}
		});
		button_2.setBounds(560, 510, 93, 23);
		getContentPane().add(button_2);

		JButton button_3 = new JButton("取消");
		button_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		button_3.setBounds(690, 510, 93, 23);
		getContentPane().add(button_3);
		
		//给“减速度设置”和“再生制动设置”添加事件
		speedCutButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				speedCutButton.setSelected(true);
				brakingButton.setSelected(false);
				panel_1.remove(electricBrakePanel);
				panel_1.add(slowDownPanel);
				importLinesBtn.setEnabled(true);
				appendLineBtn.setEnabled(true);
				removeLineBtn.setEnabled(true);
				panel_1.updateUI();
			}
		});

		brakingButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				brakingButton.setSelected(true);
				speedCutButton.setSelected(false);
				panel_1.remove(slowDownPanel);
				panel_1.add(electricBrakePanel);
				//给参数赋值
				initElectricBrakePanel(trainCategoryId);
				importLinesBtn.setEnabled(false);
				appendLineBtn.setEnabled(false);
				removeLineBtn.setEnabled(false);
				panel_1.updateUI();
			}
		});

		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setModal(true);
		MyUtillity.setFrameOnCenter(this);
		setVisible(true);
	}
	
	/**
	 * 初始化“再生制动设置”panel
	 * @param trainCategoryId
	 */
	public void initElectricBrakePanel(int trainCategoryId){
		String [] parameters = BrakeConfPanelService.getFiveParameters(trainCategoryId);
		k1.setText(parameters[0]);
		k2.setText(parameters[1]);
		D.setText(parameters[2]);
		N.setText(TractionConfPanelService.getSumDynamicAxleNum(trainCategoryId) + "");
		T.setText(parameters[4]);
		
		TrainElectricBrake trainElectricBrake = BrakeConfPanelService.getTrainElectricBrake(trainCategoryId);
		v1.setText(trainElectricBrake.getV1()+"");
		v2.setText(trainElectricBrake.getV2()+"");
		P00.setText(trainElectricBrake.getP00()+"");
		
		double P11Num = Double.parseDouble(N.getText().trim()) * Double.parseDouble(P00.getText().trim());
		P11.setText(P11Num+"");
		double F11Num = MyTools.numFormat2((Double.parseDouble(P11.getText()) * 3.6)/Double.parseDouble(v2.getText()));
		F11.setText(F11Num+"");
	}
	
	/**
	 * 保存“再生制动设置”panel中的参数
	 * @param trainCategoryId
	 */
	public void saveElectricBrakePanel(int trainCategoryId){
		String v1Str = v1.getText().trim();
		String v2Str = v2.getText().trim();
		String P00Str = P00.getText().trim();
		if(v1Str.equals("") || v2Str.equals("") || P00Str.equals("")){
			System.out.println("v1Str="+v1Str+", v2Str="+v2Str+", P00Str="+P00Str);
			JOptionPane.showMessageDialog(BrakeParameterDialog.this, "再生制动参数保存失败");
			return;
		}
		TrainElectricBrake trainElectricBrake = new TrainElectricBrake();
		trainElectricBrake.setV1(Double.parseDouble(v1Str));
		trainElectricBrake.setV2(Double.parseDouble(v2Str));
		trainElectricBrake.setP00(Double.parseDouble(P00Str));
		trainElectricBrake.setTrainCategoryId(trainCategoryId);
		
		BrakeConfPanelService.saveTrainElectricBrake(trainCategoryId, trainElectricBrake);
	}
	
	/**
	 *  电制动列车最大轮周牵引功率:P11=N*P00(KW)
	 */
	public void calculateP11() {
		String NStr = N.getText().trim();
		String P00Str = P00.getText().trim();
		if (!NStr.equals("") && !P00Str.equals("")) {
			try {
				P11.setText(MyTools.numFormat(Double.parseDouble(NStr)
						* Double.parseDouble(P00Str)));
			} catch (NumberFormatException e) {
				// TODO: handle exception
				JOptionPane.showMessageDialog(this, "请输入正确的数字");
			}
		} else {
			P11.setText("0.0");
		}
	}

	/**
	 *  电制动恒功区和恒转矩区转折点对应的牵引力F11=P11*3.6/v2
	 */
	public void calculateF11() {
		String P11Str = P11.getText().trim();
		String v2Str = v2.getText().trim();
		if (!P11Str.equals("") && !v2Str.equals("")) {
			try {
				F11.setText(MyTools.numFormat(Double.parseDouble(P11Str) * 3.6
						/ Double.parseDouble(v2Str)));
			} catch (NumberFormatException e) {
				// TODO: handle exception
				JOptionPane.showMessageDialog(this, "请输入正确的数字");
				e.printStackTrace();
			}
		} else {
			F11.setText("0.0");
		}
	}
	
	/**
	 * 初始化“减速度设置”panel
	 * @param trainCategoryId
	 */
	public void initSlowDownPanel(int trainCategoryId) {
		columnNamesVector = new Vector<String>();
		columnNamesVector.add("速度(km/h)");
		columnNamesVector.add("1A");
		columnNamesVector.add("1B");
		columnNamesVector.add("2");
		columnNamesVector.add("3");
		columnNamesVector.add("4");
		columnNamesVector.add("5");
		columnNamesVector.add("6");
		columnNamesVector.add("7");
		columnNamesVector.add("8");
		columnNamesVector.add("9");
		columnNamesVector.add("10");
		columnNamesVector.add("11");
		columnNamesVector.add("12");
		columnNamesVector.add("13");
		columnNamesVector.add("EB");
		
		brakeFactorTableModel = new DefaultTableModel(columnNamesVector, 0);

		brakeFactorTable = new JTable(brakeFactorTableModel);
		brakeFactorTable.putClientProperty("terminateEditOnFocusLost", true);

		brakeFactorTableScrollPane = new JScrollPane(brakeFactorTable);

		//获取制动系数数据，并填充table数据
		ArrayList<TrainBrakeFactor> brakeFactorList = BrakeConfPanelService.getBrakeFactor(trainCategoryId);
		fillBrakeFactorTable(brakeFactorList);
	}
	
	/**
	 * 隐藏制动系数表的某列
	 * @param table
	 * @param index 列的索引
	 */
	public static void hideColumn(JTable table, int index) {
		TableColumn tc = table.getColumnModel().getColumn(index);
		tc.setMaxWidth(0);
		tc.setPreferredWidth(0);
		tc.setMinWidth(0);
		tc.setWidth(0);
		table.getTableHeader().getColumnModel().getColumn(index).setMaxWidth(0);
		table.getTableHeader().getColumnModel().getColumn(index).setMinWidth(0);
	}
	
	/**
	 * 刷新表格
	 * @param trainCategoryId
	 */
	public static void refreshSlowDownPanel(int trainCategoryId){
		if(brakeFactorTableModel != null){
			brakeFactorTableModel.getDataVector().removeAllElements();//清空表格数据
			brakeFactorTableModel.fireTableDataChanged();
		}
		// 填充table数据
		ArrayList<TrainBrakeFactor> brakeFactorList = BrakeConfPanelService.getBrakeFactor(trainCategoryId);
		fillBrakeFactorTable(brakeFactorList);
	}
	
	/**
	 * 通过brakeFactorList填充表格
	 * @param brakeFactorList
	 */
	public static void fillBrakeFactorTable(ArrayList<TrainBrakeFactor> brakeFactorList){
		if(brakeFactorList != null){
			for (TrainBrakeFactor factor : brakeFactorList) {
				Vector<String> line = new Vector<String>();
				line.add(factor.getSpeed() + "");
				line.add(factor.get_1A() + "");
				line.add(factor.get_1B() + "");
				line.add(factor.get_2() + "");
				line.add(factor.get_3() + "");
				line.add(factor.get_4() + "");
				line.add(factor.get_5() + "");
				line.add(factor.get_6() + "");
				line.add(factor.get_7() + "");
				line.add(factor.get_8() + "");
				line.add(factor.get_9() + "");
				line.add(factor.get_10() + "");
				line.add(factor.get_11() + "");
				line.add(factor.get_12() + "");
				line.add(factor.get_13() + "");
				line.add(factor.getEb() + "");
				brakeFactorTableModel.addRow(line);
			}
		}
		boolean [] titleBoolean = BrakeConfPanelService.getBrakeLevelBoolean(brakeFactorList);
		//隐藏系数为空的列
		for(int i=0;i<titleBoolean.length;i++){
			if(titleBoolean[i] == false){
				hideColumn(brakeFactorTable, i+1);
			}
		}
	}

	/**
	 * 获取所有brakeFactorTable中的内容
	 * @param trainCatetoryId
	 * @return
	 */
	public ArrayList<TrainBrakeFactor> getBrakeFactor(int trainCatetoryId) {
		for (int m = 0; m < brakeFactorTableModel.getRowCount(); m++) {
			for (int n = 0; n < brakeFactorTableModel.getColumnCount(); n++) {
				String cellStr = ((Vector) brakeFactorTableModel
						.getDataVector().elementAt(m)).elementAt(n).toString()
						.trim();
				if (cellStr.equals("")) {
					JOptionPane.showMessageDialog(BrakeParameterDialog.this,
							"系数值不正确！");
					return null;
				} else {
					try {
						double num = Double.parseDouble(cellStr) + 1;
					} catch (NumberFormatException e) {
						JOptionPane.showMessageDialog(
								BrakeParameterDialog.this, "系数格式不正确！");
						return null;
					}
				}
			}
		}

		ArrayList<TrainBrakeFactor> list = new ArrayList<TrainBrakeFactor>();
		for (int i = 0; i < brakeFactorTableModel.getRowCount(); i++) {
			TrainBrakeFactor factor = new TrainBrakeFactor();
			Vector line = (Vector) brakeFactorTableModel.getDataVector()
					.elementAt(i);
			factor.setSpeed(Double.parseDouble(line.get(0).toString()));
			factor.set_1A(Double.parseDouble(line.get(1).toString()));
			factor.set_1B(Double.parseDouble(line.get(2).toString()));
			factor.set_2(Double.parseDouble(line.get(3).toString()));
			factor.set_3(Double.parseDouble(line.get(4).toString()));
			factor.set_4(Double.parseDouble(line.get(5).toString()));
			factor.set_5(Double.parseDouble(line.get(6).toString()));
			factor.set_6(Double.parseDouble(line.get(7).toString()));
			factor.set_7(Double.parseDouble(line.get(8).toString()));
			factor.set_8(Double.parseDouble(line.get(9).toString()));
			factor.set_9(Double.parseDouble(line.get(10).toString()));
			factor.set_10(Double.parseDouble(line.get(11).toString()));
			factor.set_11(Double.parseDouble(line.get(12).toString()));
			factor.set_12(Double.parseDouble(line.get(13).toString()));
			factor.set_13(Double.parseDouble(line.get(14).toString()));
			factor.setEb(Double.parseDouble(line.get(15).toString()));
			factor.setTrainCategoryId(trainCatetoryId);
			list.add(factor);
		}
		return list;
	}

	/**
	 *  增加一行
	 */
	public void appendOneLine() {
		TrainBrakeFactor factor = new TrainBrakeFactor();
		Vector<String> line = new Vector<String>();
		line.add(factor.getSpeed() + "");
		line.add(factor.get_1A() + "");
		line.add(factor.get_1B() + "");
		line.add(factor.get_2() + "");
		line.add(factor.get_3() + "");
		line.add(factor.get_4() + "");
		line.add(factor.get_5() + "");
		line.add(factor.get_6() + "");
		line.add(factor.get_7() + "");
		line.add(factor.get_8() + "");
		line.add(factor.get_9() + "");
		line.add(factor.get_10() + "");
		line.add(factor.get_11() + "");
		line.add(factor.get_12() + "");
		line.add(factor.get_13() + "");
		line.add(factor.getEb() + "");

		brakeFactorTableModel.addRow(line);
		int rowCount = brakeFactorTableModel.getRowCount();
		Rectangle rect = brakeFactorTable.getCellRect(rowCount - 1, 0, true);
		brakeFactorTable.scrollRectToVisible(rect);
	}

	/**
	 *  减少一行
	 */
	public void removeOneLine() {
		int selectedIndex = brakeFactorTable.getSelectedRow();
		if (selectedIndex == -1) {
			JOptionPane.showMessageDialog(BrakeParameterDialog.this,
					"请选择一行再删除！");
			return;
		}
		int option = JOptionPane.showConfirmDialog(BrakeParameterDialog.this,
				"确认删除？");
		if (option == 0) {
			brakeFactorTableModel.removeRow(selectedIndex);
		}
	}
}
