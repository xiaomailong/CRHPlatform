package com.crh.view.panel;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import org.jfree.chart.ChartPanel;

import com.crh.service.TopTargetService;
import com.crh.view.dialog.ComfortIndexDialog;
import com.crh.view.dialog.SaftyIndexDialog;
import com.crh.view.dialog.SpeedIndexDialog;
import com.crh2.javabean.TrainCategory;
import com.crh2.javabean.TrainTopTarget;
import javax.swing.UIManager;

/**
 * 顶层目标的panel
 * @author huhui
 *
 */
public class TopTargetPanel extends JPanel {
	
	private final JComboBox<Integer> trainIdComboBox = new JComboBox<Integer>();
	private final JComboBox<String> trainNameComboBox = new JComboBox<String>();
	
	/**
	 *  所有“列车名称”
	 */
	private Integer [] trainCategoryIdArray;
	private String[] trainCategoryNameArray;
	/**
	 * 一个全局的JavaBean，方便顶层目标修改数据
	 */
	private TrainTopTarget trainTopTarget = null;
	private JTextField speed0Field;
	private JTextField speed1Field;
	private JTextField speed2Field;
	private JTextField speed3Field;
	private JTextField speed4Field;
	private JTextField comfort0Field;
	private JTextField comfort1Field;
	private JTextField comfort2Field;
	private JTextField comfort3Field;
	private JTextField comfort4Field;
	private JTextField comfort5Field;
	private JTextField comfort6Field;
	private JTextField safty0Field;
	private JTextField safty1Field;
	private JTextField safty3Field;
	private JTextField safty4Field;
	private JTextField safty2Field;
	private JTextField energy0Field;
	private JTextField energy1Field;
	private JTextField energy2Field;
	private JTextField energy3Field;
	private JTextField energy4Field;
	
	private JPanel brakeChartPanel = null;
	private final int maxSpeed = 300;
	private JPanel panel_1;
	
	public TopTargetPanel() {
		setLayout(null);
		
		JLabel lblNewLabel = new JLabel("\u7F16\u7EC4\u9009\u62E9\uFF1A");
		lblNewLabel.setBounds(10, 10, 60, 15);
		add(lblNewLabel);
		
		// 初始化JComboBox的数据
		this.getAllTrainCategoryToArray();

		trainIdComboBox.setModel(new DefaultComboBoxModel<Integer>(trainCategoryIdArray));
		trainIdComboBox.setBounds(378, 7, 120, 21);
		add(trainIdComboBox);
		trainIdComboBox.setVisible(false);
		
		trainNameComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//1.两个JComboBox级联
				int selectedIndex = trainNameComboBox.getSelectedIndex();
				trainIdComboBox.setSelectedIndex(selectedIndex);
				//2.填充数据
				if(selectedIndex != 0){
					int trainCategoryId = (Integer) trainIdComboBox.getSelectedItem();
					setTopTargetParameters(trainCategoryId);//设置顶层目标数据
					createBrakeChart();
				}
			}
		});
		trainNameComboBox.setModel(new DefaultComboBoxModel<String>(trainCategoryNameArray));
		trainNameComboBox.setBounds(78, 7, 120, 21);
		add(trainNameComboBox);
		
		JLabel lblNewLabel_1 = new JLabel("\u63D0\u793A\uFF1A\u60A8\u53EF\u4EE5\u901A\u8FC7\u9009\u62E9\u5DF2\u5B58\u5728\u7684\u7F16\u7EC4\u6570\u636E\u67E5\u770B\u6216\u7F16\u8F91\u9876\u5C42\u76EE\u6807\u548C\u9876\u5C42\u6280\u672F\u6307\u6807\u6570\u636E");
		lblNewLabel_1.setForeground(Color.RED);
		lblNewLabel_1.setBounds(20, 38, 1313, 15);
		add(lblNewLabel_1);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "\u5217\u8F66\u7275\u5F15\u8BA1\u7B97", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(622, 63, 625, 200);
		add(panel);
		panel.setLayout(null);
		
		JLabel label_5 = new JLabel("\u5217\u8F66\u8FD0\u884C\u65F6\u7684\u963B\u529B\u7279\u6027(N/t)\uFF1A(f = a + b*v + c*v^2)");
		label_5.setBounds(20, 25, 310, 15);
		panel.add(label_5);
		
		JLabel label_17 = new JLabel("a\uFF1A");
		label_17.setBounds(20, 50, 54, 15);
		panel.add(label_17);
		
		energy0Field = new JTextField();
		energy0Field.setEditable(false);
		energy0Field.setColumns(10);
		energy0Field.setBounds(41, 47, 289, 21);
		panel.add(energy0Field);
		
		JLabel label_18 = new JLabel("b\uFF1A");
		label_18.setBounds(20, 81, 54, 15);
		panel.add(label_18);
		
		energy1Field = new JTextField();
		energy1Field.setEditable(false);
		energy1Field.setColumns(10);
		energy1Field.setBounds(41, 78, 289, 21);
		panel.add(energy1Field);
		
		JLabel label_19 = new JLabel("c\uFF1A");
		label_19.setBounds(20, 109, 54, 15);
		panel.add(label_19);
		
		energy2Field = new JTextField();
		energy2Field.setEditable(false);
		energy2Field.setColumns(10);
		energy2Field.setBounds(41, 106, 289, 21);
		panel.add(energy2Field);
		
		JLabel label_20 = new JLabel("\u8F74\u91CD(t)\uFF1A");
		label_20.setBounds(20, 137, 54, 15);
		panel.add(label_20);
		
		energy3Field = new JTextField();
		energy3Field.setEditable(false);
		energy3Field.setColumns(10);
		energy3Field.setBounds(72, 134, 258, 21);
		panel.add(energy3Field);
		
		JLabel label_21 = new JLabel("\u7275\u5F15\u4F20\u52A8\u7CFB\u7EDF\u3001\u518D\u751F\u5236\u52A8\u7CFB\u7EDF\u6548\u7387(%)\uFF1A");
		label_21.setBounds(20, 165, 210, 15);
		panel.add(label_21);
		
		energy4Field = new JTextField();
		energy4Field.setEditable(false);
		energy4Field.setColumns(10);
		energy4Field.setBounds(228, 162, 279, 21);
		panel.add(energy4Field);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new TitledBorder(null, "\u901F\u5EA6", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_2.setBounds(43, 63, 431, 191);
		add(panel_2);
		panel_2.setLayout(null);
		
		JLabel label = new JLabel("\u6700\u9AD8\u8FD0\u884C\u901F\u5EA6(km/h)\uFF1A");
		label.setBounds(10, 23, 120, 15);
		panel_2.add(label);
		
		speed0Field = new JTextField();
		speed0Field.setEditable(false);
		speed0Field.setColumns(10);
		speed0Field.setBounds(133, 20, 255, 21);
		panel_2.add(speed0Field);
		
		JLabel label_9 = new JLabel("\u6700\u9AD8\u8FD0\u884C\u901F\u5EA6->\u5269\u4F59\u52A0\u901F\u5EA6(m/s^2)\uFF1A");
		label_9.setBounds(10, 48, 198, 15);
		panel_2.add(label_9);
		
		speed1Field = new JTextField();
		speed1Field.setEditable(false);
		speed1Field.setColumns(10);
		speed1Field.setBounds(207, 45, 181, 21);
		panel_2.add(speed1Field);
		
		JLabel label_10 = new JLabel("\u6301\u7EED\u8FD0\u884C\u901F\u5EA6(km/h)\uFF1A");
		label_10.setBounds(10, 73, 120, 15);
		panel_2.add(label_10);
		
		speed2Field = new JTextField();
		speed2Field.setEditable(false);
		speed2Field.setColumns(10);
		speed2Field.setBounds(133, 70, 255, 21);
		panel_2.add(speed2Field);
		
		JLabel label_11 = new JLabel("\u6301\u7EED\u8FD0\u884C\u901F\u5EA6->\u5269\u4F59\u52A0\u901F\u5EA6(m/s^2)\uFF1A");
		label_11.setBounds(10, 98, 198, 15);
		panel_2.add(label_11);
		
		speed3Field = new JTextField();
		speed3Field.setEditable(false);
		speed3Field.setColumns(10);
		speed3Field.setBounds(207, 95, 181, 21);
		panel_2.add(speed3Field);
		
		JLabel label_13 = new JLabel("\u6307\u5B9A\u901F\u5EA6\u533A\u6BB5\u5185\u7684\u5E73\u5747\u542F\u52A8\u52A0\u901F\u5EA6(m/s^2)\uFF1A");
		label_13.setBounds(10, 123, 234, 15);
		panel_2.add(label_13);
		
		speed4Field = new JTextField();
		speed4Field.setEditable(false);
		speed4Field.setColumns(10);
		speed4Field.setBounds(242, 120, 146, 21);
		panel_2.add(speed4Field);
		
		JButton button = new JButton("\u4FEE\u6539");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(trainTopTarget != null){
					int trainCategoryId = (Integer) trainIdComboBox.getSelectedItem();
					new SpeedIndexDialog(TopTargetPanel.this, trainCategoryId, trainTopTarget);
				}
			}
		});
		button.setBounds(151, 158, 93, 23);
		panel_2.add(button);
		
		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new TitledBorder(null, "\u8212\u9002\u5EA6", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_3.setBounds(43, 260, 431, 214);
		add(panel_3);
		panel_3.setLayout(null);
		
		JLabel label_1 = new JLabel("\u5BA4\u5185\u6E29\u5EA6(\u2103)\uFF1A");
		label_1.setBounds(10, 26, 84, 15);
		panel_3.add(label_1);
		
		comfort0Field = new JTextField();
		comfort0Field.setEditable(false);
		comfort0Field.setColumns(10);
		comfort0Field.setBounds(93, 23, 295, 21);
		panel_3.add(comfort0Field);
		
		JLabel label_2 = new JLabel("\u5BA4\u5185\u6E7F\u5EA6(%)\uFF1A");
		label_2.setBounds(10, 51, 84, 15);
		panel_3.add(label_2);
		
		comfort1Field = new JTextField();
		comfort1Field.setEditable(false);
		comfort1Field.setColumns(10);
		comfort1Field.setBounds(93, 48, 295, 21);
		panel_3.add(comfort1Field);
		
		comfort2Field = new JTextField();
		comfort2Field.setEditable(false);
		comfort2Field.setColumns(10);
		comfort2Field.setBounds(198, 73, 190, 21);
		panel_3.add(comfort2Field);
		
		comfort3Field = new JTextField();
		comfort3Field.setEditable(false);
		comfort3Field.setColumns(10);
		comfort3Field.setBounds(124, 98, 264, 21);
		panel_3.add(comfort3Field);
		
		comfort4Field = new JTextField();
		comfort4Field.setEditable(false);
		comfort4Field.setColumns(10);
		comfort4Field.setBounds(93, 123, 295, 21);
		panel_3.add(comfort4Field);
		
		JLabel label_3 = new JLabel("\u8F66\u5185\u566A\u58F0(dB)\uFF1A");
		label_3.setBounds(10, 126, 93, 15);
		panel_3.add(label_3);
		
		JLabel label_14 = new JLabel("\u8F66\u5185\u65B0\u98CE\u91CF(m^3/h)\uFF1A");
		label_14.setBounds(10, 101, 114, 15);
		panel_3.add(label_14);
		
		JLabel label_15 = new JLabel("\u8F66\u5185\u6C14\u538B\u6C34\u5E73\u4E0E\u6C14\u538B\u6CE2\u52A8(kPa/s)\uFF1A");
		label_15.setBounds(10, 76, 203, 15);
		panel_3.add(label_15);
		
		JButton button_1 = new JButton("\u4FEE\u6539");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(trainTopTarget != null){
					int trainCategoryId = (Integer) trainIdComboBox.getSelectedItem();
					new ComfortIndexDialog(TopTargetPanel.this, trainCategoryId, trainTopTarget);
				}
			}
		});
		button_1.setBounds(151, 181, 93, 23);
		panel_3.add(button_1);
		
		JLabel label_16 = new JLabel("\u8F66\u4F53\u632F\u52A8(\u5782\u5411\u3001\u6A2A\u5411)(Hz)\uFF1A");
		label_16.setBounds(10, 154, 156, 15);
		panel_3.add(label_16);
		
		comfort5Field = new JTextField();
		comfort5Field.setEditable(false);
		comfort5Field.setColumns(10);
		comfort5Field.setBounds(164, 151, 66, 21);
		panel_3.add(comfort5Field);
		
		comfort6Field = new JTextField();
		comfort6Field.setEditable(false);
		comfort6Field.setColumns(10);
		comfort6Field.setBounds(250, 151, 66, 21);
		panel_3.add(comfort6Field);
		
		JPanel panel_4 = new JPanel();
		panel_4.setBorder(new TitledBorder(null, "\u5B89\u5168", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_4.setBounds(43, 480, 431, 185);
		add(panel_4);
		panel_4.setLayout(null);
		
		JLabel label_4 = new JLabel("\u4E34\u754C\u5931\u7A33\u5B9A\u901F\u5EA6(km/h)\uFF1A");
		label_4.setBounds(10, 28, 135, 15);
		panel_4.add(label_4);
		
		JLabel label_6 = new JLabel("\u8131\u8F68\u7CFB\u6570\uFF1A");
		label_6.setBounds(10, 53, 198, 15);
		panel_4.add(label_6);
		
		JLabel label_7 = new JLabel("\u8F6E\u91CD\u51CF\u8F7D\u7387\uFF1A");
		label_7.setBounds(10, 78, 120, 15);
		panel_4.add(label_7);
		
		JLabel label_8 = new JLabel("\u52A8\u6001\u6A2A\u5411\u529B\uFF1A");
		label_8.setBounds(10, 103, 198, 15);
		panel_4.add(label_8);
		
		JLabel label_12 = new JLabel("\u7D27\u6025\u5236\u52A8\u8DDD\u79BB(m)\uFF1A");
		label_12.setBounds(10, 128, 234, 15);
		panel_4.add(label_12);
		
		safty0Field = new JTextField();
		safty0Field.setEditable(false);
		safty0Field.setColumns(10);
		safty0Field.setBounds(143, 25, 245, 21);
		panel_4.add(safty0Field);
		
		safty1Field = new JTextField();
		safty1Field.setEditable(false);
		safty1Field.setColumns(10);
		safty1Field.setBounds(71, 50, 317, 21);
		panel_4.add(safty1Field);
		
		safty2Field = new JTextField();
		safty2Field.setEditable(false);
		safty2Field.setColumns(10);
		safty2Field.setBounds(81, 75, 307, 21);
		panel_4.add(safty2Field);
		
		safty3Field = new JTextField();
		safty3Field.setEditable(false);
		safty3Field.setColumns(10);
		safty3Field.setBounds(81, 100, 307, 21);
		panel_4.add(safty3Field);
		
		safty4Field = new JTextField();
		safty4Field.setEditable(false);
		safty4Field.setColumns(10);
		safty4Field.setBounds(117, 125, 271, 21);
		panel_4.add(safty4Field);
		
		JButton button_2 = new JButton("\u4FEE\u6539");
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(trainTopTarget != null){
					int trainCategoryId = (Integer) trainIdComboBox.getSelectedItem();
					new SaftyIndexDialog(TopTargetPanel.this, trainCategoryId, trainTopTarget);
				}
			}
		});
		button_2.setBounds(151, 153, 93, 23);
		panel_4.add(button_2);
		
		panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "\u963B\u529B\u7279\u6027\u66F2\u7EBF", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBounds(622, 273, 625, 392);
		add(panel_1);
		panel_1.setLayout(new GridLayout(1, 1, 0, 0));
		
		brakeChartPanel = new ChartPanel(ChartFactoryPanel.createBlankChart("阻力特性曲线"));
		panel_1.add(brakeChartPanel);
	}
	
	/**
	 * 创建阻力特性曲线
	 */
	public void createBrakeChart(){
		double a = Double.parseDouble(energy0Field.getText().trim());
		double b = Double.parseDouble(energy1Field.getText().trim());
		double c =Double.parseDouble(energy2Field.getText().trim());
		double [][] dataArray = new double [2][maxSpeed]; //maxSpeed = 300
		for(int v=0;v<maxSpeed;v++){
			dataArray[0][v] = v;
			dataArray[1][v] = a + b * v + c * v * v;
		}
		brakeChartPanel = ChartFactoryPanel.createSingleLineChartPanel(dataArray, "阻力特性曲线", " f-v", "速度(km/h)", "阻力(KN)");
		panel_1.removeAll();
		panel_1.add(brakeChartPanel);
		panel_1.updateUI();
	}
	
	/**
	 * 当修改top target之后刷新panle中数据
	 */
	public void reflash(int trainCategoryId){
		setTopTargetParameters(trainCategoryId);
	}
	
	/**
	 * 给速度、舒适度、安全、节能设置数值
	 * @param trainCategoryId
	 */
	public void setTopTargetParameters(int trainCategoryId){
		trainTopTarget = TopTargetService.getTrainTopTarget(trainCategoryId);
		speed0Field.setText(trainTopTarget.getSpeed0() + "");
		speed1Field.setText(trainTopTarget.getSpeed1() + "");
		speed2Field.setText(trainTopTarget.getSpeed2() + "");
		speed3Field.setText(trainTopTarget.getSpeed3() + "");
		speed4Field.setText(trainTopTarget.getSpeed4() + "");
		comfort0Field.setText(trainTopTarget.getComfort0() + "");
		comfort1Field.setText(trainTopTarget.getComfort1() + "");
		comfort2Field.setText(trainTopTarget.getComfort2() + "");
		comfort3Field.setText(trainTopTarget.getComfort3() + "");
		comfort4Field.setText(trainTopTarget.getComfort4() + "");
		comfort5Field.setText(trainTopTarget.getComfort5() + "");
		comfort6Field.setText(trainTopTarget.getComfort6() + "");
		safty0Field.setText(trainTopTarget.getSafty0() + "");
		safty1Field.setText(trainTopTarget.getSafty1() + "");
		safty2Field.setText(trainTopTarget.getSafty2() + "");
		safty3Field.setText(trainTopTarget.getSafty3() + "");
		safty4Field.setText(trainTopTarget.getSafty4() + "");
		energy0Field.setText(trainTopTarget.getEnergy0() + "");
		energy1Field.setText(trainTopTarget.getEnergy1() + "");
		energy2Field.setText(trainTopTarget.getEnergy2() + "");
		energy3Field.setText(trainTopTarget.getEnergy3() + "");
		energy4Field.setText(trainTopTarget.getEnergy4() + "");
	}
	
	/**
	 *  从数据表train_category获得所有列车名称，并赋值
	 */
	public void getAllTrainCategoryToArray() {
		ArrayList<TrainCategory> tc = TopTargetService.getTrainCategory();
		trainCategoryIdArray = new Integer[tc.size() + 1];
		trainCategoryNameArray = new String[tc.size() + 1];
		trainCategoryIdArray[0] = -1;
		trainCategoryNameArray[0] = "请选择……";
		for (int i = 0; i < tc.size(); i++) {
			trainCategoryIdArray[i+1] = tc.get(i).getId();
			trainCategoryNameArray[i+1] = tc.get(i).getName();
		}
	}
}
