package com.crh.view.dialog;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

import com.crh.service.TractionConfPanelService;
import com.crh.view.panel.TractionLevelConfPanel;
import com.crh2.javabean.TrainTractionLevelConf;
import com.crh2.util.MyUtillity;

/**
 * 牵引级位编辑界面
 * @author huhui
 *
 */
public class TractionLevelEditDialog extends JDialog {
	private JLabel label_1_0;
	private JLabel label_1_1;
	private JLabel label_1_2;
	private JLabel label_1_1_;
	private JLabel label_1_3;
	private JLabel label_4_0;
	private JLabel label_4_1;
	private JLabel label_4_2;
	private JLabel label_4_3;
	private JLabel label_4_2_;
	private JLabel label_4_4;
	private JLabel label_5_0;
	private JLabel label_5_1;
	private JLabel label_5_2;
	private JLabel label_5_3;
	private JLabel label_5_2_;
	private JLabel label_5_4;
	private JLabel label_6_0;
	private JLabel label_6_1;
	private JLabel label_6_2;
	private JLabel label_6_3;
	private JLabel label_6_2_;
	private JLabel label_6_4;
	private JLabel label_10_0;
	private JLabel label_10_1;
	private JLabel label_10_2;
	private JLabel label_10_3;
	private JLabel label_10_2_;
	private JLabel label_10_4;
	private JLabel label_10_5;
	private JLabel label_10_4_;
	private JLabel label_10_6;
	private JLabel label_2_0;
	private JLabel label_2_2;
	private JLabel label_2_1;
	private JLabel label_2_1_1;
	private JLabel label_2_3;
	private JLabel label_3_0;
	private JLabel label_3_1;
	private JLabel label_3_2;
	private JLabel label_3_1_1;
	private JLabel label_3_3;
	private JLabel label_7_0;
	private JLabel label_7_1;
	private JLabel label_7_2;
	private JLabel label_7_3;
	private JLabel label_7_2_1;
	private JLabel label_7_4;
	private JLabel label_8_0;
	private JLabel label_8_1;
	private JLabel label_8_2;
	private JLabel label_8_3;
	private JLabel label_8_2_1;
	private JLabel label_8_4;
	private JLabel label_9_0;
	private JLabel label_9_1;
	private JLabel label_9_2;
	private JLabel label_9_3;
	private JLabel label_9_2_1;
	private JLabel label_9_4;
	private JLabel label_11_0;
	private JLabel label_11_1;
	private JLabel label_11_2;
	private JLabel label_11_3;
	private JLabel label_11_2_1;
	private JLabel label_11_4;
	private JLabel label_11_5;
	private JLabel label_11_4_1;
	private JLabel label_11_6;
	private JLabel label_12_0;
	private JLabel label_12_1;
	private JLabel label_12_2;
	private JLabel label_12_3;
	private JLabel label_12_2_1;
	private JLabel label_12_4;
	private JLabel label_12_5;
	private JLabel label_12_4_1;
	private JLabel label_12_6;
	private JLabel label_13_0;
	private JLabel label_13_1;
	private JLabel label_13_2;
	private JLabel label_13_3;
	private JLabel label_13_2_1;
	private JLabel label_13_4;
	private JLabel label_13_6;
	private JLabel label_14_0;
	private JLabel label_14_1;
	private JLabel label_14_2;
	private JLabel label_14_3;
	private JLabel label_14_2_1;
	private JLabel label_14_4;
	private JLabel label_14_5;
	private JLabel label_14_4_1;
	private JLabel label_14_6;
	private JLabel label_15_0;
	private JLabel label_15_1;
	private JLabel label_15_2;
	private JLabel label_15_3;
	private JLabel label_15_2_1;
	private JLabel label_15_4;
	private JLabel label_13_5;
	private JLabel label_13_4_1;
	
	/**
	 * 初始化对话框，并对用户输入的数值进行正确性校验
	 * @param trainName
	 * @param trainCategoryId
	 */
	public TractionLevelEditDialog(String trainName, final int trainCategoryId) {
		setBounds(100, 100, 1067, 659);
		getContentPane().setLayout(new GridLayout(1, 1, 0, 0));

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		//获取TrainTractionLevelConf
		TrainTractionLevelConf bean = TractionConfPanelService.getTrainTractionLevelConf(trainCategoryId);

		JPanel panel = new JPanel();
		panel.setLocation(-130, -150);
		panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), trainName+"\u7275\u5F15\u7EA7\u4F4D\u8BBE\u7F6E", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		getContentPane().add(panel);
		panel.setLayout(null);

		JButton button_2 = new JButton("保存");
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				saveTractionLevel(trainCategoryId);
			}
		});
		button_2.setBounds(411, 590, 93, 23);
		panel.add(button_2);

		JButton button_3 = new JButton("\u5173\u95ED");
		button_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		button_3.setBounds(567, 590, 93, 23);
		panel.add(button_3);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "1\u7EA7", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBounds(10, 23, 331, 98);
		panel.add(panel_1);
		panel_1.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("{");
		lblNewLabel.setFont(new Font("宋体", Font.PLAIN, 64));
		lblNewLabel.setBounds(29, 26, 38, 53);
		panel_1.add(lblNewLabel);
		
		JLabel lblF = new JLabel("F = ");
		lblF.setFont(new Font("宋体", Font.PLAIN, 16));
		lblF.setBounds(13, 46, 32, 15);
		panel_1.add(lblF);
		
		label_1_0 = new JLabel(bean.get_1_0()+"");
		label_1_0.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				if(event.getClickCount() == 2){
					String value = JOptionPane.showInputDialog("请输入数值");
					if(value != null && !"".equals(value.trim())){
						if(MyUtillity.isNumber(value)){
							label_1_0.setText(value);
						}else{
							JOptionPane.showMessageDialog(TractionLevelEditDialog.this, "请输入数字");
						}
					}
				}
			}
		});
		label_1_0.setFont(new Font("宋体", Font.PLAIN, 16));
		label_1_0.setBounds(70, 20, 54, 15);
		panel_1.add(label_1_0);
		
		JLabel label_1 = new JLabel("0 < v <");
		label_1.setFont(new Font("宋体", Font.PLAIN, 16));
		label_1.setBounds(197, 22, 56, 15);
		panel_1.add(label_1);
		
		label_1_1 = new JLabel(bean.get_1_1()+"");
		label_1_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				if(event.getClickCount() == 2){
					String value = JOptionPane.showInputDialog("请输入数值");
					if(value != null && !"".equals(value.trim())){
						if(MyUtillity.isNumber(value)){
							label_1_1.setText(value);
						}else{
							JOptionPane.showMessageDialog(TractionLevelEditDialog.this, "请输入数字");
						}
					}
				}
			}
		});
		label_1_1.setFont(new Font("宋体", Font.PLAIN, 16));
		label_1_1.setBounds(258, 22, 44, 15);
		panel_1.add(label_1_1);
		
		label_1_2 = new JLabel(bean.get_1_2()+"");
		label_1_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				if(event.getClickCount() == 2){
					String value = JOptionPane.showInputDialog("请输入数值");
					if(value != null && !"".equals(value.trim())){
						if (MyUtillity.isNumber(value)) {
							label_1_2.setText(value);
						} else {
							JOptionPane.showMessageDialog(TractionLevelEditDialog.this, "请输入数字");
						}
					}
				}
			}
		});
		label_1_2.setFont(new Font("宋体", Font.PLAIN, 16));
		label_1_2.setBounds(60, 62, 64, 15);
		panel_1.add(label_1_2);
		
		JLabel label_4 = new JLabel("/");
		label_4.setFont(new Font("宋体", Font.PLAIN, 16));
		label_4.setBounds(130, 63, 20, 15);
		panel_1.add(label_4);
		
		JLabel lblV = new JLabel("v^2");
		lblV.setFont(new Font("宋体", Font.PLAIN, 16));
		lblV.setBounds(140, 63, 32, 15);
		panel_1.add(lblV);
		
		label_1_1_ = new JLabel(bean.get_1_1()+"");
		label_1_1_.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					String value = JOptionPane.showInputDialog("请输入数值");
					if(value != null && !"".equals(value.trim())){
						if (MyUtillity.isNumber(value)) {
							label_1_1_.setText(value);
						} else {
							JOptionPane.showMessageDialog(TractionLevelEditDialog.this, "请输入数字");
						}
					}
				}
			}
		});
		label_1_1_.setFont(new Font("宋体", Font.PLAIN, 16));
		label_1_1_.setBounds(176, 64, 38, 15);
		panel_1.add(label_1_1_);
		
		JLabel label_6 = new JLabel(" < v <");
		label_6.setFont(new Font("宋体", Font.PLAIN, 16));
		label_6.setBounds(207, 64, 56, 15);
		panel_1.add(label_6);
		
		label_1_3 = new JLabel(bean.get_1_3()+"");
		label_1_3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					String value = JOptionPane.showInputDialog("请输入数值");
					if(value != null && !"".equals(value.trim())){
						if (MyUtillity.isNumber(value)) {
							label_1_3.setText(value);
						} else {
							JOptionPane.showMessageDialog(TractionLevelEditDialog.this, "请输入数字");
						}
					}
				}
			}
		});
		label_1_3.setFont(new Font("宋体", Font.PLAIN, 16));
		label_1_3.setBounds(259, 64, 43, 15);
		panel_1.add(label_1_3);
		
		JPanel panel_5 = new JPanel();
		panel_5.setLayout(null);
		panel_5.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "5\u7EA7", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_5.setBounds(344, 135, 345, 98);
		panel.add(panel_5);
		
		JLabel label_44 = new JLabel("{");
		label_44.setFont(new Font("宋体", Font.PLAIN, 64));
		label_44.setBounds(29, 26, 38, 53);
		panel_5.add(label_44);
		
		JLabel label_45 = new JLabel("F = ");
		label_45.setFont(new Font("宋体", Font.PLAIN, 16));
		label_45.setBounds(13, 46, 32, 15);
		panel_5.add(label_45);
		
		label_5_0 = new JLabel(bean.get_5_0()+"");
		label_5_0.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					String value = JOptionPane.showInputDialog("请输入数值");
					if(value != null && !"".equals(value.trim())){
						if (MyUtillity.isNumber(value)) {
							label_5_0.setText(value);
						} else {
							JOptionPane.showMessageDialog(TractionLevelEditDialog.this, "请输入数字");
						}
					}
				}
			}
		});
		label_5_0.setFont(new Font("宋体", Font.PLAIN, 16));
		label_5_0.setBounds(60, 20, 64, 15);
		panel_5.add(label_5_0);
		
		JLabel label_47 = new JLabel("0 < v <");
		label_47.setFont(new Font("宋体", Font.PLAIN, 16));
		label_47.setBounds(237, 20, 56, 15);
		panel_5.add(label_47);
		
		label_5_2 = new JLabel(bean.get_5_2()+"");
		label_5_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					String value = JOptionPane.showInputDialog("请输入数值");
					if(value != null && !"".equals(value.trim())){
						if (MyUtillity.isNumber(value)) {
							label_5_2.setText(value);
						} else {
							JOptionPane.showMessageDialog(TractionLevelEditDialog.this, "请输入数字");
						}
					}
				}
			}
		});
		label_5_2.setFont(new Font("宋体", Font.PLAIN, 16));
		label_5_2.setBounds(298, 20, 47, 15);
		panel_5.add(label_5_2);
		
		label_5_3 = new JLabel(bean.get_5_3()+"");
		label_5_3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					String value = JOptionPane.showInputDialog("请输入数值");
					if(value != null && !"".equals(value.trim())){
						if (MyUtillity.isNumber(value)) {
							label_5_3.setText(value);
						} else {
							JOptionPane.showMessageDialog(TractionLevelEditDialog.this, "请输入数字");
						}
					}
				}
			}
		});
		label_5_3.setFont(new Font("宋体", Font.PLAIN, 16));
		label_5_3.setBounds(60, 62, 98, 15);
		panel_5.add(label_5_3);
		
		JLabel label_50 = new JLabel("/");
		label_50.setFont(new Font("宋体", Font.PLAIN, 16));
		label_50.setBounds(155, 62, 20, 15);
		panel_5.add(label_50);
		
		JLabel label_51 = new JLabel("v^2");
		label_51.setFont(new Font("宋体", Font.PLAIN, 16));
		label_51.setBounds(168, 62, 32, 15);
		panel_5.add(label_51);
		
		label_5_2_ = new JLabel(bean.get_5_2()+"");
		label_5_2_.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					String value = JOptionPane.showInputDialog("请输入数值");
					if(value != null && !"".equals(value.trim())){
						if (MyUtillity.isNumber(value)) {
							label_5_2_.setText(value);
						} else {
							JOptionPane.showMessageDialog(TractionLevelEditDialog.this, "请输入数字");
						}
					}
				}
			}
		});
		label_5_2_.setFont(new Font("宋体", Font.PLAIN, 16));
		label_5_2_.setBounds(212, 62, 55, 15);
		panel_5.add(label_5_2_);
		
		JLabel label_53 = new JLabel(" < v <");
		label_53.setFont(new Font("宋体", Font.PLAIN, 16));
		label_53.setBounds(250, 62, 56, 15);
		panel_5.add(label_53);
		
		label_5_4 = new JLabel(bean.get_5_4()+"");
		label_5_4.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					String value = JOptionPane.showInputDialog("请输入数值");
					if(value != null && !"".equals(value.trim())){
						if (MyUtillity.isNumber(value)) {
							label_5_4.setText(value);
						} else {
							JOptionPane.showMessageDialog(TractionLevelEditDialog.this, "请输入数字");
						}
					}
				}
			}
		});
		label_5_4.setFont(new Font("宋体", Font.PLAIN, 16));
		label_5_4.setBounds(302, 62, 43, 15);
		panel_5.add(label_5_4);
		
		JLabel label_55 = new JLabel("* v +");
		label_55.setFont(new Font("宋体", Font.PLAIN, 16));
		label_55.setBounds(128, 20, 40, 15);
		panel_5.add(label_55);
		
		label_5_1 = new JLabel(bean.get_5_1()+"");
		label_5_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					String value = JOptionPane.showInputDialog("请输入数值");
					if(value != null && !"".equals(value.trim())){
						if (MyUtillity.isNumber(value)) {
							label_5_1.setText(value);
						} else {
							JOptionPane.showMessageDialog(TractionLevelEditDialog.this, "请输入数字");
						}
					}
				}
			}
		});
		label_5_1.setFont(new Font("宋体", Font.PLAIN, 16));
		label_5_1.setBounds(172, 21, 50, 15);
		panel_5.add(label_5_1);
		
		JPanel panel_6 = new JPanel();
		panel_6.setLayout(null);
		panel_6.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "6\u7EA7", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_6.setBounds(693, 135, 345, 98);
		panel.add(panel_6);
		
		JLabel label_57 = new JLabel("{");
		label_57.setFont(new Font("宋体", Font.PLAIN, 64));
		label_57.setBounds(29, 26, 38, 53);
		panel_6.add(label_57);
		
		JLabel label_58 = new JLabel("F = ");
		label_58.setFont(new Font("宋体", Font.PLAIN, 16));
		label_58.setBounds(13, 46, 32, 15);
		panel_6.add(label_58);
		
		label_6_0 = new JLabel(bean.get_6_0()+"");
		label_6_0.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					String value = JOptionPane.showInputDialog("请输入数值");
					if(value != null && !"".equals(value.trim())){
						if (MyUtillity.isNumber(value)) {
							label_6_0.setText(value);
						} else {
							JOptionPane.showMessageDialog(TractionLevelEditDialog.this, "请输入数字");
						}
					}
				}
			}
		});
		label_6_0.setFont(new Font("宋体", Font.PLAIN, 16));
		label_6_0.setBounds(60, 20, 64, 15);
		panel_6.add(label_6_0);
		
		JLabel label_60 = new JLabel("0 < v <");
		label_60.setFont(new Font("宋体", Font.PLAIN, 16));
		label_60.setBounds(237, 20, 56, 15);
		panel_6.add(label_60);
		
		label_6_2 = new JLabel(bean.get_6_2()+"");
		label_6_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					String value = JOptionPane.showInputDialog("请输入数值");
					if(value != null && !"".equals(value.trim())){
						if (MyUtillity.isNumber(value)) {
							label_6_2.setText(value);
						} else {
							JOptionPane.showMessageDialog(TractionLevelEditDialog.this, "请输入数字");
						}
					}
				}
			}
		});
		label_6_2.setFont(new Font("宋体", Font.PLAIN, 16));
		label_6_2.setBounds(298, 20, 47, 15);
		panel_6.add(label_6_2);
		
		label_6_3 = new JLabel(bean.get_6_3()+"");
		label_6_3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					String value = JOptionPane.showInputDialog("请输入数值");
					if(value != null && !"".equals(value.trim())){
						if (MyUtillity.isNumber(value)) {
							label_6_3.setText(value);
						} else {
							JOptionPane.showMessageDialog(TractionLevelEditDialog.this, "请输入数字");
						}
					}
				}
			}
		});
		label_6_3.setFont(new Font("宋体", Font.PLAIN, 16));
		label_6_3.setBounds(60, 62, 98, 15);
		panel_6.add(label_6_3);
		
		JLabel label_63 = new JLabel("/");
		label_63.setFont(new Font("宋体", Font.PLAIN, 16));
		label_63.setBounds(155, 62, 20, 15);
		panel_6.add(label_63);
		
		JLabel label_64 = new JLabel("v^2");
		label_64.setFont(new Font("宋体", Font.PLAIN, 16));
		label_64.setBounds(168, 62, 32, 15);
		panel_6.add(label_64);
		
		label_6_2_ = new JLabel(bean.get_6_2()+"");
		label_6_2_.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					String value = JOptionPane.showInputDialog("请输入数值");
					if(value != null && !"".equals(value.trim())){
						if (MyUtillity.isNumber(value)) {
							label_6_2_.setText(value);
						} else {
							JOptionPane.showMessageDialog(TractionLevelEditDialog.this, "请输入数字");
						}
					}
				}
			}
		});
		label_6_2_.setFont(new Font("宋体", Font.PLAIN, 16));
		label_6_2_.setBounds(210, 62, 47, 15);
		panel_6.add(label_6_2_);
		
		JLabel label_66 = new JLabel(" < v <");
		label_66.setFont(new Font("宋体", Font.PLAIN, 16));
		label_66.setBounds(250, 62, 56, 15);
		panel_6.add(label_66);
		
		label_6_4 = new JLabel(bean.get_6_4()+"");
		label_6_4.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					String value = JOptionPane.showInputDialog("请输入数值");
					if(value != null && !"".equals(value.trim())){
						if (MyUtillity.isNumber(value)) {
							label_6_4.setText(value);
						} else {
							JOptionPane.showMessageDialog(TractionLevelEditDialog.this, "请输入数字");
						}
					}
				}
			}
		});
		label_6_4.setFont(new Font("宋体", Font.PLAIN, 16));
		label_6_4.setBounds(302, 62, 43, 15);
		panel_6.add(label_6_4);
		
		JLabel label_68 = new JLabel("* v +");
		label_68.setFont(new Font("宋体", Font.PLAIN, 16));
		label_68.setBounds(128, 20, 40, 15);
		panel_6.add(label_68);
		
		label_6_1 = new JLabel(bean.get_6_1()+"");
		label_6_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					String value = JOptionPane.showInputDialog("请输入数值");
					if(value != null && !"".equals(value.trim())){
						if (MyUtillity.isNumber(value)) {
							label_6_1.setText(value);
						} else {
							JOptionPane.showMessageDialog(TractionLevelEditDialog.this, "请输入数字");
						}
					}
				}
			}
		});
		label_6_1.setFont(new Font("宋体", Font.PLAIN, 16));
		label_6_1.setBounds(172, 21, 50, 15);
		panel_6.add(label_6_1);
		
		JPanel panel_4 = new JPanel();
		panel_4.setLayout(null);
		panel_4.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "4\u7EA7", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_4.setBounds(10, 135, 331, 98);
		panel.add(panel_4);
		
		JLabel label_33 = new JLabel("{");
		label_33.setFont(new Font("宋体", Font.PLAIN, 64));
		label_33.setBounds(29, 26, 38, 53);
		panel_4.add(label_33);
		
		JLabel label_34 = new JLabel("F = ");
		label_34.setFont(new Font("宋体", Font.PLAIN, 16));
		label_34.setBounds(13, 46, 32, 15);
		panel_4.add(label_34);
		
		label_4_0 = new JLabel(bean.get_4_0()+"");
		label_4_0.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					String value = JOptionPane.showInputDialog("请输入数值");
					if(value != null && !"".equals(value.trim())){
						if (MyUtillity.isNumber(value)) {
							label_4_0.setText(value);
						} else {
							JOptionPane.showMessageDialog(TractionLevelEditDialog.this, "请输入数字");
						}
					}
				}
			}
		});
		label_4_0.setFont(new Font("宋体", Font.PLAIN, 16));
		label_4_0.setBounds(60, 20, 64, 15);
		panel_4.add(label_4_0);
		
		JLabel label_36 = new JLabel("0 < v <");
		label_36.setFont(new Font("宋体", Font.PLAIN, 16));
		label_36.setBounds(237, 20, 56, 15);
		panel_4.add(label_36);
		
		label_4_2 = new JLabel(bean.get_4_2()+"");
		label_4_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					String value = JOptionPane.showInputDialog("请输入数值");
					if(value != null && !"".equals(value.trim())){
						if (MyUtillity.isNumber(value)) {
							label_4_2.setText(value);
						} else {
							JOptionPane.showMessageDialog(TractionLevelEditDialog.this, "请输入数字");
						}
					}
				}
			}
		});
		label_4_2.setFont(new Font("宋体", Font.PLAIN, 16));
		label_4_2.setBounds(298, 20, 37, 15);
		panel_4.add(label_4_2);
		
		label_4_3 = new JLabel(bean.get_4_3()+"");
		label_4_3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					String value = JOptionPane.showInputDialog("请输入数值");
					if(value != null && !"".equals(value.trim())){
						if (MyUtillity.isNumber(value)) {
							label_4_3.setText(value);
						} else {
							JOptionPane.showMessageDialog(TractionLevelEditDialog.this, "请输入数字");
						}
					}
				}
			}
		});
		label_4_3.setFont(new Font("宋体", Font.PLAIN, 16));
		label_4_3.setBounds(60, 62, 86, 15);
		panel_4.add(label_4_3);
		
		JLabel label_39 = new JLabel("/");
		label_39.setFont(new Font("宋体", Font.PLAIN, 16));
		label_39.setBounds(144, 62, 20, 15);
		panel_4.add(label_39);
		
		JLabel label_40 = new JLabel("v^2");
		label_40.setFont(new Font("宋体", Font.PLAIN, 16));
		label_40.setBounds(160, 62, 32, 15);
		panel_4.add(label_40);
		
		label_4_2_ = new JLabel(bean.get_4_2()+"");
		label_4_2_.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					String value = JOptionPane.showInputDialog("请输入数值");
					if(value != null && !"".equals(value.trim())){
						if (MyUtillity.isNumber(value)) {
							label_4_2_.setText(value);
						} else {
							JOptionPane.showMessageDialog(TractionLevelEditDialog.this, "请输入数字");
						}
					}
				}
			}
		});
		label_4_2_.setFont(new Font("宋体", Font.PLAIN, 16));
		label_4_2_.setBounds(204, 62, 40, 15);
		panel_4.add(label_4_2_);
		
		JLabel label_42 = new JLabel(" < v <");
		label_42.setFont(new Font("宋体", Font.PLAIN, 16));
		label_42.setBounds(233, 62, 56, 15);
		panel_4.add(label_42);
		
		label_4_4 = new JLabel(bean.get_4_4()+"");
		label_4_4.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					String value = JOptionPane.showInputDialog("请输入数值");
					if(value != null && !"".equals(value.trim())){
						if (MyUtillity.isNumber(value)) {
							label_4_4.setText(value);
						} else {
							JOptionPane.showMessageDialog(TractionLevelEditDialog.this, "请输入数字");
						}
					}
				}
			}
		});
		label_4_4.setFont(new Font("宋体", Font.PLAIN, 16));
		label_4_4.setBounds(285, 62, 50, 15);
		panel_4.add(label_4_4);
		
		JLabel label_70 = new JLabel("* v +");
		label_70.setFont(new Font("宋体", Font.PLAIN, 16));
		label_70.setBounds(128, 20, 40, 15);
		panel_4.add(label_70);
		
		label_4_1 = new JLabel(bean.get_4_1()+"");
		label_4_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					String value = JOptionPane.showInputDialog("请输入数值");
					if(value != null && !"".equals(value.trim())){
						if (MyUtillity.isNumber(value)) {
							label_4_1.setText(value);
						} else {
							JOptionPane.showMessageDialog(TractionLevelEditDialog.this, "请输入数字");
						}
					}
				}
			}
		});
		label_4_1.setFont(new Font("宋体", Font.PLAIN, 16));
		label_4_1.setBounds(172, 21, 50, 15);
		panel_4.add(label_4_1);
		
		JPanel panel_10 = new JPanel();
		panel_10.setLayout(null);
		panel_10.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "10\u7EA7", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_10.setBounds(10, 359, 331, 104);
		panel.add(panel_10);
		
		JLabel label_72 = new JLabel("{");
		label_72.setFont(new Font("宋体", Font.PLAIN, 64));
		label_72.setBounds(29, 12, 38, 80);
		panel_10.add(label_72);
		
		JLabel label_73 = new JLabel("F = ");
		label_73.setFont(new Font("宋体", Font.PLAIN, 16));
		label_73.setBounds(13, 46, 32, 15);
		panel_10.add(label_73);
		
		label_10_0 = new JLabel(bean.get_10_0()+"");
		label_10_0.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					String value = JOptionPane.showInputDialog("请输入数值");
					if(value != null && !"".equals(value.trim())){
						if (MyUtillity.isNumber(value)) {
							label_10_0.setText(value);
						} else {
							JOptionPane.showMessageDialog(TractionLevelEditDialog.this, "请输入数字");
						}
					}
				}
			}
		});
		label_10_0.setFont(new Font("宋体", Font.PLAIN, 16));
		label_10_0.setBounds(60, 17, 64, 15);
		panel_10.add(label_10_0);
		
		JLabel label_75 = new JLabel("0 < v <");
		label_75.setFont(new Font("宋体", Font.PLAIN, 16));
		label_75.setBounds(224, 17, 56, 15);
		panel_10.add(label_75);
		
		label_10_2 = new JLabel(bean.get_10_2()+"");
		label_10_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					String value = JOptionPane.showInputDialog("请输入数值");
					if(value != null && !"".equals(value.trim())){
						if (MyUtillity.isNumber(value)) {
							label_10_2.setText(value);
						} else {
							JOptionPane.showMessageDialog(TractionLevelEditDialog.this, "请输入数字");
						}
					}
				}
			}
		});
		label_10_2.setFont(new Font("宋体", Font.PLAIN, 16));
		label_10_2.setBounds(285, 17, 50, 15);
		panel_10.add(label_10_2);
		
		label_10_5 = new JLabel(bean.get_10_5()+"");
		label_10_5.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					String value = JOptionPane.showInputDialog("请输入数值");
					if(value != null && !"".equals(value.trim())){
						if (MyUtillity.isNumber(value)) {
							label_10_5.setText(value);
						} else {
							JOptionPane.showMessageDialog(TractionLevelEditDialog.this, "请输入数字");
						}
					}
				}
			}
		});
		label_10_5.setFont(new Font("宋体", Font.PLAIN, 16));
		label_10_5.setBounds(60, 74, 100, 15);
		panel_10.add(label_10_5);
		
		JLabel lblV_3 = new JLabel(" / v^2");
		lblV_3.setFont(new Font("宋体", Font.PLAIN, 16));
		lblV_3.setBounds(140, 74, 50, 15);
		panel_10.add(lblV_3);
		
		label_10_4_ = new JLabel(bean.get_10_4()+"");
		label_10_4_.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					String value = JOptionPane.showInputDialog("请输入数值");
					if(value != null && !"".equals(value.trim())){
						if (MyUtillity.isNumber(value)) {
							label_10_4_.setText(value);
						} else {
							JOptionPane.showMessageDialog(TractionLevelEditDialog.this, "请输入数字");
						}
					}
				}
			}
		});
		label_10_4_.setFont(new Font("宋体", Font.PLAIN, 16));
		label_10_4_.setBounds(202, 74, 49, 15);
		panel_10.add(label_10_4_);
		
		JLabel label_81 = new JLabel(" < v <");
		label_81.setFont(new Font("宋体", Font.PLAIN, 16));
		label_81.setBounds(235, 74, 56, 15);
		panel_10.add(label_81);
		
		label_10_6 = new JLabel(bean.get_10_6()+"");
		label_10_6.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					String value = JOptionPane.showInputDialog("请输入数值");
					if(value != null && !"".equals(value.trim())){
						if (MyUtillity.isNumber(value)) {
							label_10_6.setText(value);
						} else {
							JOptionPane.showMessageDialog(TractionLevelEditDialog.this, "请输入数字");
						}
					}
				}
			}
		});
		label_10_6.setFont(new Font("宋体", Font.PLAIN, 16));
		label_10_6.setBounds(288, 74, 45, 15);
		panel_10.add(label_10_6);
		
		JLabel label_83 = new JLabel("* v +");
		label_83.setFont(new Font("宋体", Font.PLAIN, 16));
		label_83.setBounds(128, 17, 40, 15);
		panel_10.add(label_83);
		
		label_10_1 = new JLabel(bean.get_10_1()+"");
		label_10_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					String value = JOptionPane.showInputDialog("请输入数值");
					if(value != null && !"".equals(value.trim())){
						if (MyUtillity.isNumber(value)) {
							label_10_1.setText(value);
						} else {
							JOptionPane.showMessageDialog(TractionLevelEditDialog.this, "请输入数字");
						}
					}
				}
			}
		});
		label_10_1.setFont(new Font("宋体", Font.PLAIN, 16));
		label_10_1.setBounds(172, 17, 50, 15);
		panel_10.add(label_10_1);
		
		label_10_3 = new JLabel(bean.get_10_3()+"");
		label_10_3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					String value = JOptionPane.showInputDialog("请输入数值");
					if(value != null && !"".equals(value.trim())){
						if (MyUtillity.isNumber(value)) {
							label_10_3.setText(value);
						} else {
							JOptionPane.showMessageDialog(TractionLevelEditDialog.this, "请输入数字");
						}
					}
				}
			}
		});
		label_10_3.setFont(new Font("宋体", Font.PLAIN, 16));
		label_10_3.setBounds(60, 46, 64, 15);
		panel_10.add(label_10_3);
		
		JLabel lblV_2 = new JLabel("/ v");
		lblV_2.setFont(new Font("宋体", Font.PLAIN, 16));
		lblV_2.setBounds(120, 46, 40, 15);
		panel_10.add(lblV_2);
		
		label_10_2_ = new JLabel(bean.get_10_2()+"");
		label_10_2_.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					String value = JOptionPane.showInputDialog("请输入数值");
					if(value != null && !"".equals(value.trim())){
						if (MyUtillity.isNumber(value)) {
							label_10_2_.setText(value);
						} else {
							JOptionPane.showMessageDialog(TractionLevelEditDialog.this, "请输入数字");
						}
					}
				}
			}
		});
		label_10_2_.setFont(new Font("宋体", Font.PLAIN, 16));
		label_10_2_.setBounds(202, 46, 50, 15);
		panel_10.add(label_10_2_);
		
		JLabel label_89 = new JLabel(" < v <");
		label_89.setFont(new Font("宋体", Font.PLAIN, 16));
		label_89.setBounds(236, 46, 56, 15);
		panel_10.add(label_89);
		
		label_10_4 = new JLabel(bean.get_10_4()+"");
		label_10_4.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					String value = JOptionPane.showInputDialog("请输入数值");
					if(value != null && !"".equals(value.trim())){
						if (MyUtillity.isNumber(value)) {
							label_10_4.setText(value);
						} else {
							JOptionPane.showMessageDialog(TractionLevelEditDialog.this, "请输入数字");
						}
					}
				}
			}
		});
		label_10_4.setFont(new Font("宋体", Font.PLAIN, 16));
		label_10_4.setBounds(285, 46, 50, 15);
		panel_10.add(label_10_4);
		
		JPanel panel_7 = new JPanel();
		panel_7.setLayout(null);
		panel_7.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "7\u7EA7", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_7.setBounds(10, 247, 331, 98);
		panel.add(panel_7);
		
		JLabel label = new JLabel("{");
		label.setFont(new Font("宋体", Font.PLAIN, 64));
		label.setBounds(29, 26, 38, 53);
		panel_7.add(label);
		
		JLabel label_2 = new JLabel("F = ");
		label_2.setFont(new Font("宋体", Font.PLAIN, 16));
		label_2.setBounds(13, 46, 32, 15);
		panel_7.add(label_2);
		
		label_7_0 = new JLabel(bean.get_7_0()+"");
		label_7_0.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					String value = JOptionPane.showInputDialog("请输入数值");
					if(value != null && !"".equals(value.trim())){
						if (MyUtillity.isNumber(value)) {
							label_7_0.setText(value);
						} else {
							JOptionPane.showMessageDialog(TractionLevelEditDialog.this, "请输入数字");
						}
					}
				}
			}
		});
		label_7_0.setFont(new Font("宋体", Font.PLAIN, 16));
		label_7_0.setBounds(60, 20, 64, 15);
		panel_7.add(label_7_0);
		
		JLabel label_5 = new JLabel("0 < v <");
		label_5.setFont(new Font("宋体", Font.PLAIN, 16));
		label_5.setBounds(237, 20, 56, 15);
		panel_7.add(label_5);
		
		label_7_2 = new JLabel(bean.get_7_2()+"");
		label_7_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					String value = JOptionPane.showInputDialog("请输入数值");
					if(value != null && !"".equals(value.trim())){
						if(MyUtillity.isNumber(value)){
							label_7_2.setText(value);
						}else{
							JOptionPane.showMessageDialog(TractionLevelEditDialog.this, "请输入数字");
						}
					}
				}
			}
		});
		label_7_2.setFont(new Font("宋体", Font.PLAIN, 16));
		label_7_2.setBounds(298, 20, 37, 15);
		panel_7.add(label_7_2);
		
		label_7_3 = new JLabel(bean.get_7_3()+"");
		label_7_3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					String value = JOptionPane.showInputDialog("请输入数值");
					if(value != null && !"".equals(value.trim())){
						if(MyUtillity.isNumber(value)){
							label_7_3.setText(value);
						}else{
							JOptionPane.showMessageDialog(TractionLevelEditDialog.this, "请输入数字");
						}
					}
				}
			}
		});
		label_7_3.setFont(new Font("宋体", Font.PLAIN, 16));
		label_7_3.setBounds(60, 62, 98, 15);
		panel_7.add(label_7_3);
		
		JLabel label_12 = new JLabel("/");
		label_12.setFont(new Font("宋体", Font.PLAIN, 16));
		label_12.setBounds(155, 62, 20, 15);
		panel_7.add(label_12);
		
		JLabel label_13 = new JLabel("v^2");
		label_13.setFont(new Font("宋体", Font.PLAIN, 16));
		label_13.setBounds(168, 62, 32, 15);
		panel_7.add(label_13);
		
		label_7_2_1 = new JLabel(bean.get_7_2()+"");
		label_7_2_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					String value = JOptionPane.showInputDialog("请输入数值");
					if(value != null && !"".equals(value.trim())){
						if(MyUtillity.isNumber(value)){
							label_7_2_1.setText(value);
						}else{
							JOptionPane.showMessageDialog(TractionLevelEditDialog.this, "请输入数字");
						}
					}
				}
			}
		});
		label_7_2_1.setFont(new Font("宋体", Font.PLAIN, 16));
		label_7_2_1.setBounds(204, 62, 40, 15);
		panel_7.add(label_7_2_1);
		
		JLabel label_18 = new JLabel(" < v <");
		label_18.setFont(new Font("宋体", Font.PLAIN, 16));
		label_18.setBounds(233, 62, 56, 15);
		panel_7.add(label_18);
		
		label_7_4 = new JLabel(bean.get_7_4()+"");
		label_7_4.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					String value = JOptionPane.showInputDialog("请输入数值");
					if(value != null && !"".equals(value.trim())){
						if(MyUtillity.isNumber(value)){
							label_7_4.setText(value);
						}else{
							JOptionPane.showMessageDialog(TractionLevelEditDialog.this, "请输入数字");
						}
					}
				}
			}
		});
		label_7_4.setFont(new Font("宋体", Font.PLAIN, 16));
		label_7_4.setBounds(285, 62, 50, 15);
		panel_7.add(label_7_4);
		
		JLabel label_22 = new JLabel("* v +");
		label_22.setFont(new Font("宋体", Font.PLAIN, 16));
		label_22.setBounds(128, 20, 40, 15);
		panel_7.add(label_22);
		
		label_7_1 = new JLabel(bean.get_7_1()+"");
		label_7_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					String value = JOptionPane.showInputDialog("请输入数值");
					if(value != null && !"".equals(value.trim())){
						if(MyUtillity.isNumber(value)){
							label_7_1.setText(value);
						}else{
							JOptionPane.showMessageDialog(TractionLevelEditDialog.this, "请输入数字");
						}
					}
				}
			}
		});
		label_7_1.setFont(new Font("宋体", Font.PLAIN, 16));
		label_7_1.setBounds(172, 21, 50, 15);
		panel_7.add(label_7_1);
		
		JPanel panel_8 = new JPanel();
		panel_8.setLayout(null);
		panel_8.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "8\u7EA7", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_8.setBounds(344, 247, 345, 98);
		panel.add(panel_8);
		
		JLabel label_25 = new JLabel("{");
		label_25.setFont(new Font("宋体", Font.PLAIN, 64));
		label_25.setBounds(29, 26, 38, 53);
		panel_8.add(label_25);
		
		JLabel label_28 = new JLabel("F = ");
		label_28.setFont(new Font("宋体", Font.PLAIN, 16));
		label_28.setBounds(13, 46, 32, 15);
		panel_8.add(label_28);
		
		label_8_0 = new JLabel(bean.get_8_0()+"");
		label_8_0.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					String value = JOptionPane.showInputDialog("请输入数值");
					if(value != null && !"".equals(value.trim())){
						if(MyUtillity.isNumber(value)){
							label_8_0.setText(value);
						}else{
							JOptionPane.showMessageDialog(TractionLevelEditDialog.this, "请输入数字");
						}
					}
				}
			}
		});
		label_8_0.setFont(new Font("宋体", Font.PLAIN, 16));
		label_8_0.setBounds(60, 20, 64, 15);
		panel_8.add(label_8_0);
		
		JLabel label_32 = new JLabel("0 < v <");
		label_32.setFont(new Font("宋体", Font.PLAIN, 16));
		label_32.setBounds(237, 20, 56, 15);
		panel_8.add(label_32);
		
		label_8_2 = new JLabel(bean.get_8_2()+"");
		label_8_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					String value = JOptionPane.showInputDialog("请输入数值");
					if(value != null && !"".equals(value.trim())){
						if(MyUtillity.isNumber(value)){
							label_8_2.setText(value);
						}else{
							JOptionPane.showMessageDialog(TractionLevelEditDialog.this, "请输入数字");
						}
					}
				}
			}
		});
		label_8_2.setFont(new Font("宋体", Font.PLAIN, 16));
		label_8_2.setBounds(298, 20, 47, 15);
		panel_8.add(label_8_2);
		
		label_8_3 = new JLabel(bean.get_8_3()+"");
		label_8_3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					String value = JOptionPane.showInputDialog("请输入数值");
					if(value != null && !"".equals(value.trim())){
						if(MyUtillity.isNumber(value)){
							label_8_3.setText(value);
						}else{
							JOptionPane.showMessageDialog(TractionLevelEditDialog.this, "请输入数字");
						}
					}
				}
			}
		});
		label_8_3.setFont(new Font("宋体", Font.PLAIN, 16));
		label_8_3.setBounds(60, 62, 98, 15);
		panel_8.add(label_8_3);
		
		JLabel label_38 = new JLabel("/");
		label_38.setFont(new Font("宋体", Font.PLAIN, 16));
		label_38.setBounds(155, 62, 20, 15);
		panel_8.add(label_38);
		
		JLabel label_41 = new JLabel("v^2");
		label_41.setFont(new Font("宋体", Font.PLAIN, 16));
		label_41.setBounds(168, 62, 32, 15);
		panel_8.add(label_41);
		
		label_8_2_1 = new JLabel(bean.get_8_2()+"");
		label_8_2_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					String value = JOptionPane.showInputDialog("请输入数值");
					if(value != null && !"".equals(value.trim())){
						if(MyUtillity.isNumber(value)){
							label_8_2_1.setText(value);
						}else{
							JOptionPane.showMessageDialog(TractionLevelEditDialog.this, "请输入数字");
						}
					}
				}
			}
		});
		label_8_2_1.setFont(new Font("宋体", Font.PLAIN, 16));
		label_8_2_1.setBounds(212, 62, 55, 15);
		panel_8.add(label_8_2_1);
		
		JLabel label_46 = new JLabel(" < v <");
		label_46.setFont(new Font("宋体", Font.PLAIN, 16));
		label_46.setBounds(250, 62, 56, 15);
		panel_8.add(label_46);
		
		label_8_4 = new JLabel(bean.get_8_4()+"");
		label_8_4.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					String value = JOptionPane.showInputDialog("请输入数值");
					if(value != null && !"".equals(value.trim())){
						if(MyUtillity.isNumber(value)){
							label_8_4.setText(value);
						}else{
							JOptionPane.showMessageDialog(TractionLevelEditDialog.this, "请输入数字");
						}
					}
				}
			}
		});
		label_8_4.setFont(new Font("宋体", Font.PLAIN, 16));
		label_8_4.setBounds(302, 62, 43, 15);
		panel_8.add(label_8_4);
		
		JLabel label_49 = new JLabel("* v +");
		label_49.setFont(new Font("宋体", Font.PLAIN, 16));
		label_49.setBounds(128, 20, 40, 15);
		panel_8.add(label_49);
		
		label_8_1 = new JLabel(bean.get_8_1()+"");
		label_8_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					String value = JOptionPane.showInputDialog("请输入数值");
					if(value != null && !"".equals(value.trim())){
						if(MyUtillity.isNumber(value)){
							label_8_1.setText(value);
						}else{
							JOptionPane.showMessageDialog(TractionLevelEditDialog.this, "请输入数字");
						}
					}
				}
			}
		});
		label_8_1.setFont(new Font("宋体", Font.PLAIN, 16));
		label_8_1.setBounds(172, 21, 50, 15);
		panel_8.add(label_8_1);
		
		JPanel panel_9 = new JPanel();
		panel_9.setLayout(null);
		panel_9.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "9\u7EA7", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_9.setBounds(693, 247, 345, 98);
		panel.add(panel_9);
		
		JLabel label_54 = new JLabel("{");
		label_54.setFont(new Font("宋体", Font.PLAIN, 64));
		label_54.setBounds(29, 26, 38, 53);
		panel_9.add(label_54);
		
		JLabel label_56 = new JLabel("F = ");
		label_56.setFont(new Font("宋体", Font.PLAIN, 16));
		label_56.setBounds(13, 46, 32, 15);
		panel_9.add(label_56);
		
		label_9_0 = new JLabel(bean.get_9_0()+"");
		label_9_0.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					String value = JOptionPane.showInputDialog("请输入数值");
					if(value != null && !"".equals(value.trim())){
						if(MyUtillity.isNumber(value)){
							label_9_0.setText(value);
						}else{
							JOptionPane.showMessageDialog(TractionLevelEditDialog.this, "请输入数字");
						}
					}
				}
			}
		});
		label_9_0.setFont(new Font("宋体", Font.PLAIN, 16));
		label_9_0.setBounds(60, 20, 64, 15);
		panel_9.add(label_9_0);
		
		JLabel label_61 = new JLabel("0 < v <");
		label_61.setFont(new Font("宋体", Font.PLAIN, 16));
		label_61.setBounds(237, 20, 56, 15);
		panel_9.add(label_61);
		
		label_9_2 = new JLabel(bean.get_9_2()+"");
		label_9_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					String value = JOptionPane.showInputDialog("请输入数值");
					if(value != null && !"".equals(value.trim())){
						if(MyUtillity.isNumber(value)){
							label_9_2.setText(value);
						}else{
							JOptionPane.showMessageDialog(TractionLevelEditDialog.this, "请输入数字");
						}
					}
				}
			}
		});
		label_9_2.setFont(new Font("宋体", Font.PLAIN, 16));
		label_9_2.setBounds(298, 20, 47, 15);
		panel_9.add(label_9_2);
		
		label_9_3 = new JLabel(bean.get_9_3()+"");
		label_9_3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					String value = JOptionPane.showInputDialog("请输入数值");
					if(value != null && !"".equals(value.trim())){
						if(MyUtillity.isNumber(value)){
							label_9_3.setText(value);
						}else{
							JOptionPane.showMessageDialog(TractionLevelEditDialog.this, "请输入数字");
						}
					}
				}
			}
		});
		label_9_3.setFont(new Font("宋体", Font.PLAIN, 16));
		label_9_3.setBounds(60, 62, 98, 15);
		panel_9.add(label_9_3);
		
		JLabel label_67 = new JLabel("/");
		label_67.setFont(new Font("宋体", Font.PLAIN, 16));
		label_67.setBounds(155, 62, 20, 15);
		panel_9.add(label_67);
		
		JLabel label_69 = new JLabel("v^2");
		label_69.setFont(new Font("宋体", Font.PLAIN, 16));
		label_69.setBounds(168, 62, 32, 15);
		panel_9.add(label_69);
		
		label_9_2_1 = new JLabel(bean.get_9_2()+"");
		label_9_2_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					String value = JOptionPane.showInputDialog("请输入数值");
					if(value != null && !"".equals(value.trim())){
						if(MyUtillity.isNumber(value)){
							label_9_2_1.setText(value);
						}else{
							JOptionPane.showMessageDialog(TractionLevelEditDialog.this, "请输入数字");
						}
					}
				}
			}
		});
		label_9_2_1.setFont(new Font("宋体", Font.PLAIN, 16));
		label_9_2_1.setBounds(210, 62, 47, 15);
		panel_9.add(label_9_2_1);
		
		JLabel label_74 = new JLabel(" < v <");
		label_74.setFont(new Font("宋体", Font.PLAIN, 16));
		label_74.setBounds(250, 62, 56, 15);
		panel_9.add(label_74);
		
		label_9_4 = new JLabel(bean.get_9_4()+"");
		label_9_4.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					String value = JOptionPane.showInputDialog("请输入数值");
					if(value != null && !"".equals(value.trim())){
						if(MyUtillity.isNumber(value)){
							label_9_4.setText(value);
						}else{
							JOptionPane.showMessageDialog(TractionLevelEditDialog.this, "请输入数字");
						}
					}
				}
			}
		});
		label_9_4.setFont(new Font("宋体", Font.PLAIN, 16));
		label_9_4.setBounds(302, 62, 43, 15);
		panel_9.add(label_9_4);
		
		JLabel label_77 = new JLabel("* v +");
		label_77.setFont(new Font("宋体", Font.PLAIN, 16));
		label_77.setBounds(128, 20, 40, 15);
		panel_9.add(label_77);
		
		label_9_1 = new JLabel(bean.get_9_1()+"");
		label_9_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					String value = JOptionPane.showInputDialog("请输入数值");
					if(value != null && !"".equals(value.trim())){
						if(MyUtillity.isNumber(value)){
							label_9_1.setText(value);
						}else{
							JOptionPane.showMessageDialog(TractionLevelEditDialog.this, "请输入数字");
						}
					}
				}
			}
		});
		label_9_1.setFont(new Font("宋体", Font.PLAIN, 16));
		label_9_1.setBounds(172, 21, 50, 15);
		panel_9.add(label_9_1);
		
		JPanel panel_2 = new JPanel();
		panel_2.setLayout(null);
		panel_2.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "2\u7EA7", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_2.setBounds(351, 23, 331, 98);
		panel.add(panel_2);
		
		JLabel label_8 = new JLabel("{");
		label_8.setFont(new Font("宋体", Font.PLAIN, 64));
		label_8.setBounds(29, 26, 38, 53);
		panel_2.add(label_8);
		
		JLabel label_9 = new JLabel("F = ");
		label_9.setFont(new Font("宋体", Font.PLAIN, 16));
		label_9.setBounds(13, 46, 32, 15);
		panel_2.add(label_9);
		
		label_2_0 = new JLabel(bean.get_2_0()+"");
		label_2_0.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					String value = JOptionPane.showInputDialog("请输入数值");
					if(value != null && !"".equals(value.trim())){
						if (MyUtillity.isNumber(value)) {
							label_2_0.setText(value);
						} else {
							JOptionPane.showMessageDialog(TractionLevelEditDialog.this, "请输入数字");
						}
					}
				}
			}
		});
		label_2_0.setFont(new Font("宋体", Font.PLAIN, 16));
		label_2_0.setBounds(70, 20, 54, 15);
		panel_2.add(label_2_0);
		
		JLabel label_14 = new JLabel("0 < v <");
		label_14.setFont(new Font("宋体", Font.PLAIN, 16));
		label_14.setBounds(197, 22, 56, 15);
		panel_2.add(label_14);
		
		label_2_1 = new JLabel(bean.get_2_1()+"");
		label_2_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					String value = JOptionPane.showInputDialog("请输入数值");
					if(value != null && !"".equals(value.trim())){
						if (MyUtillity.isNumber(value)) {
							label_2_1.setText(value);
						} else {
							JOptionPane.showMessageDialog(TractionLevelEditDialog.this, "请输入数字");
						}
					}
				}
			}
		});
		label_2_1.setFont(new Font("宋体", Font.PLAIN, 16));
		label_2_1.setBounds(258, 22, 44, 15);
		panel_2.add(label_2_1);
		
		label_2_2 = new JLabel(bean.get_2_2()+"");
		label_2_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					String value = JOptionPane.showInputDialog("请输入数值");
					if(value != null && !"".equals(value.trim())){
						if (MyUtillity.isNumber(value)) {
							label_2_2.setText(value);
						} else {
							JOptionPane.showMessageDialog(TractionLevelEditDialog.this, "请输入数字");
						}
					}
				}
			}
		});
		label_2_2.setFont(new Font("宋体", Font.PLAIN, 16));
		label_2_2.setBounds(60, 62, 64, 15);
		panel_2.add(label_2_2);
		
		JLabel label_20 = new JLabel("/");
		label_20.setFont(new Font("宋体", Font.PLAIN, 16));
		label_20.setBounds(130, 63, 20, 15);
		panel_2.add(label_20);
		
		JLabel label_21 = new JLabel("v^2");
		label_21.setFont(new Font("宋体", Font.PLAIN, 16));
		label_21.setBounds(140, 63, 32, 15);
		panel_2.add(label_21);
		
		label_2_1_1 = new JLabel(bean.get_2_1()+"");
		label_2_1_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					String value = JOptionPane.showInputDialog("请输入数值");
					if(value != null && !"".equals(value.trim())){
						if (MyUtillity.isNumber(value)) {
							label_2_1_1.setText(value);
						} else {
							JOptionPane.showMessageDialog(TractionLevelEditDialog.this, "请输入数字");
						}
					}
				}
			}
		});
		label_2_1_1.setFont(new Font("宋体", Font.PLAIN, 16));
		label_2_1_1.setBounds(176, 64, 38, 15);
		panel_2.add(label_2_1_1);
		
		JLabel label_26 = new JLabel(" < v <");
		label_26.setFont(new Font("宋体", Font.PLAIN, 16));
		label_26.setBounds(207, 64, 56, 15);
		panel_2.add(label_26);
		
		label_2_3 = new JLabel(bean.get_2_3()+"");
		label_2_3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					String value = JOptionPane.showInputDialog("请输入数值");
					if(value != null && !"".equals(value.trim())){
						if (MyUtillity.isNumber(value)) {
							label_2_3.setText(value);
						} else {
							JOptionPane.showMessageDialog(TractionLevelEditDialog.this, "请输入数字");
						}
					}
				}
			}
		});
		label_2_3.setFont(new Font("宋体", Font.PLAIN, 16));
		label_2_3.setBounds(259, 64, 43, 15);
		panel_2.add(label_2_3);
		
		JPanel panel_3 = new JPanel();
		panel_3.setLayout(null);
		panel_3.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "3\u7EA7", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_3.setBounds(693, 23, 345, 98);
		panel.add(panel_3);
		
		JLabel label_29 = new JLabel("{");
		label_29.setFont(new Font("宋体", Font.PLAIN, 64));
		label_29.setBounds(29, 26, 38, 53);
		panel_3.add(label_29);
		
		JLabel label_31 = new JLabel("F = ");
		label_31.setFont(new Font("宋体", Font.PLAIN, 16));
		label_31.setBounds(13, 46, 32, 15);
		panel_3.add(label_31);
		
		label_3_0 = new JLabel(bean.get_3_0()+"");
		label_3_0.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					String value = JOptionPane.showInputDialog("请输入数值");
					if(value != null && !"".equals(value.trim())){
						if (MyUtillity.isNumber(value)) {
							label_3_0.setText(value);
						} else {
							JOptionPane.showMessageDialog(TractionLevelEditDialog.this, "请输入数字");
						}
					}
				}
			}
		});
		label_3_0.setFont(new Font("宋体", Font.PLAIN, 16));
		label_3_0.setBounds(70, 20, 54, 15);
		panel_3.add(label_3_0);
		
		JLabel label_80 = new JLabel("0 < v <");
		label_80.setFont(new Font("宋体", Font.PLAIN, 16));
		label_80.setBounds(197, 22, 56, 15);
		panel_3.add(label_80);
		
		label_3_1 = new JLabel(bean.get_3_1()+"");
		label_3_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					String value = JOptionPane.showInputDialog("请输入数值");
					if(value != null && !"".equals(value.trim())){
						if (MyUtillity.isNumber(value)) {
							label_3_1.setText(value);
						} else {
							JOptionPane.showMessageDialog(TractionLevelEditDialog.this, "请输入数字");
						}
					}
				}
			}
		});
		label_3_1.setFont(new Font("宋体", Font.PLAIN, 16));
		label_3_1.setBounds(258, 22, 44, 15);
		panel_3.add(label_3_1);
		
		label_3_2 = new JLabel(bean.get_3_2()+"");
		label_3_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					String value = JOptionPane.showInputDialog("请输入数值");
					if(value != null && !"".equals(value.trim())){
						if (MyUtillity.isNumber(value)) {
							label_3_2.setText(value);
						} else {
							JOptionPane.showMessageDialog(TractionLevelEditDialog.this, "请输入数字");
						}
					}
				}
			}
		});
		label_3_2.setFont(new Font("宋体", Font.PLAIN, 16));
		label_3_2.setBounds(60, 62, 64, 15);
		panel_3.add(label_3_2);
		
		JLabel label_85 = new JLabel("/");
		label_85.setFont(new Font("宋体", Font.PLAIN, 16));
		label_85.setBounds(130, 63, 20, 15);
		panel_3.add(label_85);
		
		JLabel label_86 = new JLabel("v^2");
		label_86.setFont(new Font("宋体", Font.PLAIN, 16));
		label_86.setBounds(140, 63, 32, 15);
		panel_3.add(label_86);
		
		label_3_1_1 = new JLabel(bean.get_3_1()+"");
		label_3_1_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					String value = JOptionPane.showInputDialog("请输入数值");
					if(value != null && !"".equals(value.trim())){
						if (MyUtillity.isNumber(value)) {
							label_3_1_1.setText(value);
						} else {
							JOptionPane.showMessageDialog(TractionLevelEditDialog.this, "请输入数字");
						}
					}
				}
			}
		});
		label_3_1_1.setFont(new Font("宋体", Font.PLAIN, 16));
		label_3_1_1.setBounds(176, 64, 38, 15);
		panel_3.add(label_3_1_1);
		
		JLabel label_88 = new JLabel(" < v <");
		label_88.setFont(new Font("宋体", Font.PLAIN, 16));
		label_88.setBounds(207, 64, 56, 15);
		panel_3.add(label_88);
		
		label_3_3 = new JLabel(bean.get_3_3()+"");
		label_3_3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					String value = JOptionPane.showInputDialog("请输入数值");
					if(value != null && !"".equals(value.trim())){
						if (MyUtillity.isNumber(value)) {
							label_3_3.setText(value);
						} else {
							JOptionPane.showMessageDialog(TractionLevelEditDialog.this, "请输入数字");
						}
					}
				}
			}
		});
		label_3_3.setFont(new Font("宋体", Font.PLAIN, 16));
		label_3_3.setBounds(259, 64, 43, 15);
		panel_3.add(label_3_3);
		
		JPanel panel_11 = new JPanel();
		panel_11.setLayout(null);
		panel_11.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "11\u7EA7", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_11.setBounds(344, 359, 331, 104);
		panel.add(panel_11);
		
		JLabel label_11 = new JLabel("{");
		label_11.setFont(new Font("宋体", Font.PLAIN, 64));
		label_11.setBounds(29, 12, 38, 80);
		panel_11.add(label_11);
		
		JLabel label_15 = new JLabel("F = ");
		label_15.setFont(new Font("宋体", Font.PLAIN, 16));
		label_15.setBounds(13, 46, 32, 15);
		panel_11.add(label_15);
		
		label_11_0 = new JLabel(bean.get_11_0()+"");
		label_11_0.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					String value = JOptionPane.showInputDialog("请输入数值");
					if(value != null && !"".equals(value.trim())){
						if(MyUtillity.isNumber(value)){
							label_11_0.setText(value);
						}else{
							JOptionPane.showMessageDialog(TractionLevelEditDialog.this, "请输入数字");
						}
					}
				}
			}
		});
		label_11_0.setFont(new Font("宋体", Font.PLAIN, 16));
		label_11_0.setBounds(60, 17, 64, 15);
		panel_11.add(label_11_0);
		
		JLabel label_23 = new JLabel("0 < v <");
		label_23.setFont(new Font("宋体", Font.PLAIN, 16));
		label_23.setBounds(224, 17, 56, 15);
		panel_11.add(label_23);
		
		label_11_2 = new JLabel(bean.get_11_2()+"");
		label_11_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					String value = JOptionPane.showInputDialog("请输入数值");
					if(value != null && !"".equals(value.trim())){
						if(MyUtillity.isNumber(value)){
							label_11_2.setText(value);
						}else{
							JOptionPane.showMessageDialog(TractionLevelEditDialog.this, "请输入数字");
						}
					}
				}
			}
		});
		label_11_2.setFont(new Font("宋体", Font.PLAIN, 16));
		label_11_2.setBounds(285, 17, 50, 15);
		panel_11.add(label_11_2);
		
		label_11_5 = new JLabel(bean.get_11_5()+"");
		label_11_5.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					String value = JOptionPane.showInputDialog("请输入数值");
					if(value != null && !"".equals(value.trim())){
						if(MyUtillity.isNumber(value)){
							label_11_5.setText(value);
						}else{
							JOptionPane.showMessageDialog(TractionLevelEditDialog.this, "请输入数字");
						}
					}
				}
			}
		});
		label_11_5.setFont(new Font("宋体", Font.PLAIN, 16));
		label_11_5.setBounds(60, 74, 100, 15);
		panel_11.add(label_11_5);
		
		JLabel label_82 = new JLabel(" / v^2");
		label_82.setFont(new Font("宋体", Font.PLAIN, 16));
		label_82.setBounds(140, 74, 50, 15);
		panel_11.add(label_82);
		
		label_11_4_1 = new JLabel(bean.get_11_4()+"");
		label_11_4_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					String value = JOptionPane.showInputDialog("请输入数值");
					if(value != null && !"".equals(value.trim())){
						if(MyUtillity.isNumber(value)){
							label_11_4_1.setText(value);
						}else{
							JOptionPane.showMessageDialog(TractionLevelEditDialog.this, "请输入数字");
						}
					}
				}
			}
		});
		label_11_4_1.setFont(new Font("宋体", Font.PLAIN, 16));
		label_11_4_1.setBounds(202, 74, 49, 15);
		panel_11.add(label_11_4_1);
		
		JLabel label_87 = new JLabel(" < v <");
		label_87.setFont(new Font("宋体", Font.PLAIN, 16));
		label_87.setBounds(235, 74, 56, 15);
		panel_11.add(label_87);
		
		label_11_6 = new JLabel(bean.get_11_6()+"");
		label_11_6.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					String value = JOptionPane.showInputDialog("请输入数值");
					if(value != null && !"".equals(value.trim())){
						if(MyUtillity.isNumber(value)){
							label_11_6.setText(value);
						}else{
							JOptionPane.showMessageDialog(TractionLevelEditDialog.this, "请输入数字");
						}
					}
				}
			}
		});
		label_11_6.setFont(new Font("宋体", Font.PLAIN, 16));
		label_11_6.setBounds(288, 74, 45, 15);
		panel_11.add(label_11_6);
		
		JLabel label_91 = new JLabel("* v +");
		label_91.setFont(new Font("宋体", Font.PLAIN, 16));
		label_91.setBounds(128, 17, 40, 15);
		panel_11.add(label_91);
		
		label_11_1 = new JLabel(bean.get_11_1()+"");
		label_11_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					String value = JOptionPane.showInputDialog("请输入数值");
					if(value != null && !"".equals(value.trim())){
						if(MyUtillity.isNumber(value)){
							label_11_1.setText(value);
						}else{
							JOptionPane.showMessageDialog(TractionLevelEditDialog.this, "请输入数字");
						}
					}
				}
			}
		});
		label_11_1.setFont(new Font("宋体", Font.PLAIN, 16));
		label_11_1.setBounds(172, 17, 50, 15);
		panel_11.add(label_11_1);
		
		label_11_3 = new JLabel(bean.get_11_3()+"");
		label_11_3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					String value = JOptionPane.showInputDialog("请输入数值");
					if(value != null && !"".equals(value.trim())){
						if(MyUtillity.isNumber(value)){
							label_11_3.setText(value);
						}else{
							JOptionPane.showMessageDialog(TractionLevelEditDialog.this, "请输入数字");
						}
					}
				}
			}
		});
		label_11_3.setFont(new Font("宋体", Font.PLAIN, 16));
		label_11_3.setBounds(60, 46, 64, 15);
		panel_11.add(label_11_3);
		
		JLabel label_94 = new JLabel("/ v");
		label_94.setFont(new Font("宋体", Font.PLAIN, 16));
		label_94.setBounds(120, 46, 40, 15);
		panel_11.add(label_94);
		
		label_11_2_1 = new JLabel(bean.get_11_2()+"");
		label_11_2_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					String value = JOptionPane.showInputDialog("请输入数值");
					if(value != null && !"".equals(value.trim())){
						if(MyUtillity.isNumber(value)){
							label_11_2_1.setText(value);
						}else{
							JOptionPane.showMessageDialog(TractionLevelEditDialog.this, "请输入数字");
						}
					}
				}
			}
		});
		label_11_2_1.setFont(new Font("宋体", Font.PLAIN, 16));
		label_11_2_1.setBounds(202, 46, 50, 15);
		panel_11.add(label_11_2_1);
		
		JLabel label_96 = new JLabel(" < v <");
		label_96.setFont(new Font("宋体", Font.PLAIN, 16));
		label_96.setBounds(236, 46, 56, 15);
		panel_11.add(label_96);
		
		label_11_4 = new JLabel(bean.get_11_4()+"");
		label_11_4.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					String value = JOptionPane.showInputDialog("请输入数值");
					if(value != null && !"".equals(value.trim())){
						if(MyUtillity.isNumber(value)){
							label_11_4.setText(value);
						}else{
							JOptionPane.showMessageDialog(TractionLevelEditDialog.this, "请输入数字");
						}
					}
				}
			}
		});
		label_11_4.setFont(new Font("宋体", Font.PLAIN, 16));
		label_11_4.setBounds(285, 46, 50, 15);
		panel_11.add(label_11_4);
		
		JPanel panel_12 = new JPanel();
		panel_12.setLayout(null);
		panel_12.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "12\u7EA7", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_12.setBounds(693, 359, 345, 104);
		panel.add(panel_12);
		
		JLabel label_98 = new JLabel("{");
		label_98.setFont(new Font("宋体", Font.PLAIN, 64));
		label_98.setBounds(29, 12, 38, 80);
		panel_12.add(label_98);
		
		JLabel label_99 = new JLabel("F = ");
		label_99.setFont(new Font("宋体", Font.PLAIN, 16));
		label_99.setBounds(13, 46, 32, 15);
		panel_12.add(label_99);
		
		label_12_0 = new JLabel(bean.get_12_0()+"");
		label_12_0.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					String value = JOptionPane.showInputDialog("请输入数值");
					if(value != null && !"".equals(value.trim())){
						if(MyUtillity.isNumber(value)){
							label_12_0.setText(value);
						}else{
							JOptionPane.showMessageDialog(TractionLevelEditDialog.this, "请输入数字");
						}
					}
				}
			}
		});
		label_12_0.setFont(new Font("宋体", Font.PLAIN, 16));
		label_12_0.setBounds(60, 17, 64, 15);
		panel_12.add(label_12_0);
		
		JLabel label_101 = new JLabel("0 < v <");
		label_101.setFont(new Font("宋体", Font.PLAIN, 16));
		label_101.setBounds(224, 17, 56, 15);
		panel_12.add(label_101);
		
		label_12_2 = new JLabel(bean.get_12_2()+"");
		label_12_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					String value = JOptionPane.showInputDialog("请输入数值");
					if(value != null && !"".equals(value.trim())){
						if(MyUtillity.isNumber(value)){
							label_12_2.setText(value);
						}else{
							JOptionPane.showMessageDialog(TractionLevelEditDialog.this, "请输入数字");
						}
					}
				}
			}
		});
		label_12_2.setFont(new Font("宋体", Font.PLAIN, 16));
		label_12_2.setBounds(285, 17, 50, 15);
		panel_12.add(label_12_2);
		
		label_12_5 = new JLabel(bean.get_12_5()+"");
		label_12_5.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					String value = JOptionPane.showInputDialog("请输入数值");
					if(value != null && !"".equals(value.trim())){
						if(MyUtillity.isNumber(value)){
							label_12_5.setText(value);
						}else{
							JOptionPane.showMessageDialog(TractionLevelEditDialog.this, "请输入数字");
						}
					}
				}
			}
		});
		label_12_5.setFont(new Font("宋体", Font.PLAIN, 16));
		label_12_5.setBounds(60, 74, 100, 15);
		panel_12.add(label_12_5);
		
		JLabel label_104 = new JLabel(" / v^2");
		label_104.setFont(new Font("宋体", Font.PLAIN, 16));
		label_104.setBounds(140, 74, 50, 15);
		panel_12.add(label_104);
		
		label_12_4_1 = new JLabel(bean.get_12_4()+"");
		label_12_4_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					String value = JOptionPane.showInputDialog("请输入数值");
					if(value != null && !"".equals(value.trim())){
						if(MyUtillity.isNumber(value)){
							label_12_4_1.setText(value);
						}else{
							JOptionPane.showMessageDialog(TractionLevelEditDialog.this, "请输入数字");
						}
					}
				}
			}
		});
		label_12_4_1.setFont(new Font("宋体", Font.PLAIN, 16));
		label_12_4_1.setBounds(202, 74, 49, 15);
		panel_12.add(label_12_4_1);
		
		JLabel label_106 = new JLabel(" < v <");
		label_106.setFont(new Font("宋体", Font.PLAIN, 16));
		label_106.setBounds(235, 74, 56, 15);
		panel_12.add(label_106);
		
		label_12_6 = new JLabel(bean.get_12_6()+"");
		label_12_6.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					String value = JOptionPane.showInputDialog("请输入数值");
					if(value != null && !"".equals(value.trim())){
						if(MyUtillity.isNumber(value)){
							label_12_6.setText(value);
						}else{
							JOptionPane.showMessageDialog(TractionLevelEditDialog.this, "请输入数字");
						}
					}
				}
			}
		});
		label_12_6.setFont(new Font("宋体", Font.PLAIN, 16));
		label_12_6.setBounds(288, 74, 45, 15);
		panel_12.add(label_12_6);
		
		JLabel label_108 = new JLabel("* v +");
		label_108.setFont(new Font("宋体", Font.PLAIN, 16));
		label_108.setBounds(128, 17, 40, 15);
		panel_12.add(label_108);
		
		label_12_1 = new JLabel(bean.get_12_1()+"");
		label_12_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					String value = JOptionPane.showInputDialog("请输入数值");
					if(value != null && !"".equals(value.trim())){
						if(MyUtillity.isNumber(value)){
							label_12_1.setText(value);
						}else{
							JOptionPane.showMessageDialog(TractionLevelEditDialog.this, "请输入数字");
						}
					}
				}
			}
		});
		label_12_1.setFont(new Font("宋体", Font.PLAIN, 16));
		label_12_1.setBounds(172, 17, 50, 15);
		panel_12.add(label_12_1);
		
		label_12_3 = new JLabel(bean.get_12_3()+"");
		label_12_3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					String value = JOptionPane.showInputDialog("请输入数值");
					if(value != null && !"".equals(value.trim())){
						if(MyUtillity.isNumber(value)){
							label_12_3.setText(value);
						}else{
							JOptionPane.showMessageDialog(TractionLevelEditDialog.this, "请输入数字");
						}
					}
				}
			}
		});
		label_12_3.setFont(new Font("宋体", Font.PLAIN, 16));
		label_12_3.setBounds(60, 46, 64, 15);
		panel_12.add(label_12_3);
		
		JLabel label_111 = new JLabel("/ v");
		label_111.setFont(new Font("宋体", Font.PLAIN, 16));
		label_111.setBounds(120, 46, 40, 15);
		panel_12.add(label_111);
		
		label_12_2_1 = new JLabel(bean.get_12_2()+"");
		label_12_2_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					String value = JOptionPane.showInputDialog("请输入数值");
					if(value != null && !"".equals(value.trim())){
						if(MyUtillity.isNumber(value)){
							label_12_2_1.setText(value);
						}else{
							JOptionPane.showMessageDialog(TractionLevelEditDialog.this, "请输入数字");
						}
					}
				}
			}
		});
		label_12_2_1.setFont(new Font("宋体", Font.PLAIN, 16));
		label_12_2_1.setBounds(202, 46, 50, 15);
		panel_12.add(label_12_2_1);
		
		JLabel label_113 = new JLabel(" < v <");
		label_113.setFont(new Font("宋体", Font.PLAIN, 16));
		label_113.setBounds(236, 46, 56, 15);
		panel_12.add(label_113);
		
		label_12_4 = new JLabel(bean.get_12_4()+"");
		label_12_4.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					String value = JOptionPane.showInputDialog("请输入数值");
					if(value != null && !"".equals(value.trim())){
						if(MyUtillity.isNumber(value)){
							label_12_4.setText(value);
						}else{
							JOptionPane.showMessageDialog(TractionLevelEditDialog.this, "请输入数字");
						}
					}
				}
			}
		});
		label_12_4.setFont(new Font("宋体", Font.PLAIN, 16));
		label_12_4.setBounds(285, 46, 50, 15);
		panel_12.add(label_12_4);
		
		JPanel panel_13 = new JPanel();
		panel_13.setLayout(null);
		panel_13.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "13\u7EA7", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_13.setBounds(10, 471, 331, 104);
		panel.add(panel_13);
		
		JLabel label_115 = new JLabel("{");
		label_115.setFont(new Font("宋体", Font.PLAIN, 64));
		label_115.setBounds(29, 12, 38, 80);
		panel_13.add(label_115);
		
		JLabel label_116 = new JLabel("F = ");
		label_116.setFont(new Font("宋体", Font.PLAIN, 16));
		label_116.setBounds(13, 46, 32, 15);
		panel_13.add(label_116);
		
		label_13_0 = new JLabel(bean.get_13_0()+"");
		label_13_0.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					String value = JOptionPane.showInputDialog("请输入数值");
					if(value != null && !"".equals(value.trim())){
						if(MyUtillity.isNumber(value)){
							label_13_0.setText(value);
						}else{
							JOptionPane.showMessageDialog(TractionLevelEditDialog.this, "请输入数字");
						}
					}
				}
			}
		});
		label_13_0.setFont(new Font("宋体", Font.PLAIN, 16));
		label_13_0.setBounds(60, 17, 64, 15);
		panel_13.add(label_13_0);
		
		JLabel label_118 = new JLabel("0 < v <");
		label_118.setFont(new Font("宋体", Font.PLAIN, 16));
		label_118.setBounds(224, 17, 56, 15);
		panel_13.add(label_118);
		
		label_13_2 = new JLabel(bean.get_13_2()+"");
		label_13_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					String value = JOptionPane.showInputDialog("请输入数值");
					if(value != null && !"".equals(value.trim())){
						if(MyUtillity.isNumber(value)){
							label_13_2.setText(value);
						}else{
							JOptionPane.showMessageDialog(TractionLevelEditDialog.this, "请输入数字");
						}
					}
				}
			}
		});
		label_13_2.setFont(new Font("宋体", Font.PLAIN, 16));
		label_13_2.setBounds(285, 17, 50, 15);
		panel_13.add(label_13_2);
		
		label_13_5 = new JLabel(bean.get_13_5()+"");
		label_13_5.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					String value = JOptionPane.showInputDialog("请输入数值");
					if(value != null && !"".equals(value.trim())){
						if(MyUtillity.isNumber(value)){
							label_13_5.setText(value);
						}else{
							JOptionPane.showMessageDialog(TractionLevelEditDialog.this, "请输入数字");
						}
					}
				}
			}
		});
		label_13_5.setFont(new Font("宋体", Font.PLAIN, 16));
		label_13_5.setBounds(60, 74, 100, 15);
		panel_13.add(label_13_5);
		
		JLabel label_121 = new JLabel(" / v^2");
		label_121.setFont(new Font("宋体", Font.PLAIN, 16));
		label_121.setBounds(140, 74, 50, 15);
		panel_13.add(label_121);
		
		label_13_4_1 = new JLabel(bean.get_13_4()+"");
		label_13_4_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					String value = JOptionPane.showInputDialog("请输入数值");
					if(value != null && !"".equals(value.trim())){
						if(MyUtillity.isNumber(value)){
							label_13_4_1.setText(value);
						}else{
							JOptionPane.showMessageDialog(TractionLevelEditDialog.this, "请输入数字");
						}
					}
				}
			}
		});
		label_13_4_1.setFont(new Font("宋体", Font.PLAIN, 16));
		label_13_4_1.setBounds(202, 74, 49, 15);
		panel_13.add(label_13_4_1);
		
		JLabel label_123 = new JLabel(" < v <");
		label_123.setFont(new Font("宋体", Font.PLAIN, 16));
		label_123.setBounds(235, 74, 56, 15);
		panel_13.add(label_123);
		
		label_13_6 = new JLabel(bean.get_13_6()+"");
		label_13_6.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					String value = JOptionPane.showInputDialog("请输入数值");
					if(value != null && !"".equals(value.trim())){
						if(MyUtillity.isNumber(value)){
							label_13_6.setText(value);
						}else{
							JOptionPane.showMessageDialog(TractionLevelEditDialog.this, "请输入数字");
						}
					}
				}
			}
		});
		label_13_6.setFont(new Font("宋体", Font.PLAIN, 16));
		label_13_6.setBounds(288, 74, 45, 15);
		panel_13.add(label_13_6);
		
		JLabel label_125 = new JLabel("* v +");
		label_125.setFont(new Font("宋体", Font.PLAIN, 16));
		label_125.setBounds(128, 17, 40, 15);
		panel_13.add(label_125);
		
		label_13_1 = new JLabel(bean.get_13_1()+"");
		label_13_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					String value = JOptionPane.showInputDialog("请输入数值");
					if(value != null && !"".equals(value.trim())){
						if(MyUtillity.isNumber(value)){
							label_13_1.setText(value);
						}else{
							JOptionPane.showMessageDialog(TractionLevelEditDialog.this, "请输入数字");
						}
					}
				}
			}
		});
		label_13_1.setFont(new Font("宋体", Font.PLAIN, 16));
		label_13_1.setBounds(172, 17, 50, 15);
		panel_13.add(label_13_1);
		
		label_13_3 = new JLabel(bean.get_13_3()+"");
		label_13_3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					String value = JOptionPane.showInputDialog("请输入数值");
					if(value != null && !"".equals(value.trim())){
						if(MyUtillity.isNumber(value)){
							label_13_3.setText(value);
						}else{
							JOptionPane.showMessageDialog(TractionLevelEditDialog.this, "请输入数字");
						}
					}
				}
			}
		});
		label_13_3.setFont(new Font("宋体", Font.PLAIN, 16));
		label_13_3.setBounds(60, 46, 64, 15);
		panel_13.add(label_13_3);
		
		JLabel label_128 = new JLabel("/ v");
		label_128.setFont(new Font("宋体", Font.PLAIN, 16));
		label_128.setBounds(120, 46, 40, 15);
		panel_13.add(label_128);
		
		label_13_2_1 = new JLabel(bean.get_13_2()+"");
		label_13_2_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					String value = JOptionPane.showInputDialog("请输入数值");
					if(value != null && !"".equals(value.trim())){
						if(MyUtillity.isNumber(value)){
							label_13_2_1.setText(value);
						}else{
							JOptionPane.showMessageDialog(TractionLevelEditDialog.this, "请输入数字");
						}
					}
				}
			}
		});
		label_13_2_1.setFont(new Font("宋体", Font.PLAIN, 16));
		label_13_2_1.setBounds(202, 46, 50, 15);
		panel_13.add(label_13_2_1);
		
		JLabel label_130 = new JLabel(" < v <");
		label_130.setFont(new Font("宋体", Font.PLAIN, 16));
		label_130.setBounds(236, 46, 56, 15);
		panel_13.add(label_130);
		
		label_13_4 = new JLabel(bean.get_13_4()+"");
		label_13_4.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					String value = JOptionPane.showInputDialog("请输入数值");
					if(value != null && !"".equals(value.trim())){
						if(MyUtillity.isNumber(value)){
							label_13_4.setText(value);
						}else{
							JOptionPane.showMessageDialog(TractionLevelEditDialog.this, "请输入数字");
						}
					}
				}
			}
		});
		label_13_4.setFont(new Font("宋体", Font.PLAIN, 16));
		label_13_4.setBounds(285, 46, 50, 15);
		panel_13.add(label_13_4);
		
		JPanel panel_14 = new JPanel();
		panel_14.setLayout(null);
		panel_14.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "14\u7EA7", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_14.setBounds(351, 471, 331, 104);
		panel.add(panel_14);
		
		JLabel label_3 = new JLabel("{");
		label_3.setFont(new Font("宋体", Font.PLAIN, 64));
		label_3.setBounds(29, 12, 38, 80);
		panel_14.add(label_3);
		
		JLabel label_133 = new JLabel("F = ");
		label_133.setFont(new Font("宋体", Font.PLAIN, 16));
		label_133.setBounds(13, 46, 32, 15);
		panel_14.add(label_133);
		
		label_14_0 = new JLabel(bean.get_14_0()+"");
		label_14_0.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					String value = JOptionPane.showInputDialog("请输入数值");
					if(value != null && !"".equals(value.trim())){
						if(MyUtillity.isNumber(value)){
							label_14_0.setText(value);
						}else{
							JOptionPane.showMessageDialog(TractionLevelEditDialog.this, "请输入数字");
						}
					}
				}
			}
		});
		label_14_0.setFont(new Font("宋体", Font.PLAIN, 16));
		label_14_0.setBounds(60, 17, 64, 15);
		panel_14.add(label_14_0);
		
		JLabel label_135 = new JLabel("0 < v <");
		label_135.setFont(new Font("宋体", Font.PLAIN, 16));
		label_135.setBounds(224, 17, 56, 15);
		panel_14.add(label_135);
		
		label_14_2 = new JLabel(bean.get_14_2()+"");
		label_14_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					String value = JOptionPane.showInputDialog("请输入数值");
					if(value != null && !"".equals(value.trim())){
						if(MyUtillity.isNumber(value)){
							label_14_2.setText(value);
						}else{
							JOptionPane.showMessageDialog(TractionLevelEditDialog.this, "请输入数字");
						}
					}
				}
			}
		});
		label_14_2.setFont(new Font("宋体", Font.PLAIN, 16));
		label_14_2.setBounds(285, 17, 50, 15);
		panel_14.add(label_14_2);
		
		label_14_5 = new JLabel(bean.get_14_5()+"");
		label_14_5.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					String value = JOptionPane.showInputDialog("请输入数值");
					if(value != null && !"".equals(value.trim())){
						if(MyUtillity.isNumber(value)){
							label_14_5.setText(value);
						}else{
							JOptionPane.showMessageDialog(TractionLevelEditDialog.this, "请输入数字");
						}
					}
				}
			}
		});
		label_14_5.setFont(new Font("宋体", Font.PLAIN, 16));
		label_14_5.setBounds(60, 74, 100, 15);
		panel_14.add(label_14_5);
		
		JLabel label_138 = new JLabel(" / v^2");
		label_138.setFont(new Font("宋体", Font.PLAIN, 16));
		label_138.setBounds(140, 74, 50, 15);
		panel_14.add(label_138);
		
		label_14_4_1 = new JLabel(bean.get_14_4()+"");
		label_14_4_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					String value = JOptionPane.showInputDialog("请输入数值");
					if(value != null && !"".equals(value.trim())){
						if(MyUtillity.isNumber(value)){
							label_14_4_1.setText(value);
						}else{
							JOptionPane.showMessageDialog(TractionLevelEditDialog.this, "请输入数字");
						}
					}
				}
			}
		});
		label_14_4_1.setFont(new Font("宋体", Font.PLAIN, 16));
		label_14_4_1.setBounds(202, 74, 49, 15);
		panel_14.add(label_14_4_1);
		
		JLabel label_140 = new JLabel(" < v <");
		label_140.setFont(new Font("宋体", Font.PLAIN, 16));
		label_140.setBounds(235, 74, 56, 15);
		panel_14.add(label_140);
		
		label_14_6 = new JLabel(bean.get_14_6()+"");
		label_14_6.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					String value = JOptionPane.showInputDialog("请输入数值");
					if(value != null && !"".equals(value.trim())){
						if(MyUtillity.isNumber(value)){
							label_14_6.setText(value);
						}else{
							JOptionPane.showMessageDialog(TractionLevelEditDialog.this, "请输入数字");
						}
					}
				}
			}
		});
		label_14_6.setFont(new Font("宋体", Font.PLAIN, 16));
		label_14_6.setBounds(288, 74, 45, 15);
		panel_14.add(label_14_6);
		
		JLabel label_142 = new JLabel("* v +");
		label_142.setFont(new Font("宋体", Font.PLAIN, 16));
		label_142.setBounds(128, 17, 40, 15);
		panel_14.add(label_142);
		
		label_14_1 = new JLabel(bean.get_14_1()+"");
		label_14_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					String value = JOptionPane.showInputDialog("请输入数值");
					if(value != null && !"".equals(value.trim())){
						if(MyUtillity.isNumber(value)){
							label_14_1.setText(value);
						}else{
							JOptionPane.showMessageDialog(TractionLevelEditDialog.this, "请输入数字");
						}
					}
				}
			}
		});
		label_14_1.setFont(new Font("宋体", Font.PLAIN, 16));
		label_14_1.setBounds(172, 17, 50, 15);
		panel_14.add(label_14_1);
		
		label_14_3 = new JLabel(bean.get_14_3()+"");
		label_14_3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					String value = JOptionPane.showInputDialog("请输入数值");
					if(value != null && !"".equals(value.trim())){
						if(MyUtillity.isNumber(value)){
							label_14_3.setText(value);
						}else{
							JOptionPane.showMessageDialog(TractionLevelEditDialog.this, "请输入数字");
						}
					}
				}
			}
		});
		label_14_3.setFont(new Font("宋体", Font.PLAIN, 16));
		label_14_3.setBounds(60, 46, 64, 15);
		panel_14.add(label_14_3);
		
		JLabel label_145 = new JLabel("/ v");
		label_145.setFont(new Font("宋体", Font.PLAIN, 16));
		label_145.setBounds(120, 46, 40, 15);
		panel_14.add(label_145);
		
		label_14_2_1 = new JLabel(bean.get_14_2()+"");
		label_14_2_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					String value = JOptionPane.showInputDialog("请输入数值");
					if(value != null && !"".equals(value.trim())){
						if(MyUtillity.isNumber(value)){
							label_14_2_1.setText(value);
						}else{
							JOptionPane.showMessageDialog(TractionLevelEditDialog.this, "请输入数字");
						}
					}
				}
			}
		});
		label_14_2_1.setFont(new Font("宋体", Font.PLAIN, 16));
		label_14_2_1.setBounds(202, 46, 50, 15);
		panel_14.add(label_14_2_1);
		
		JLabel label_147 = new JLabel(" < v <");
		label_147.setFont(new Font("宋体", Font.PLAIN, 16));
		label_147.setBounds(236, 46, 56, 15);
		panel_14.add(label_147);
		
		label_14_4 = new JLabel(bean.get_14_4()+"");
		label_14_4.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					String value = JOptionPane.showInputDialog("请输入数值");
					if(value != null && !"".equals(value.trim())){
						if(MyUtillity.isNumber(value)){
							label_14_4.setText(value);
						}else{
							JOptionPane.showMessageDialog(TractionLevelEditDialog.this, "请输入数字");
						}
					}
				}
			}
		});
		label_14_4.setFont(new Font("宋体", Font.PLAIN, 16));
		label_14_4.setBounds(285, 46, 50, 15);
		panel_14.add(label_14_4);
		
		JPanel panel_15 = new JPanel();
		panel_15.setLayout(null);
		panel_15.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "15\u7EA7", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_15.setBounds(693, 471, 345, 104);
		panel.add(panel_15);
		
		JLabel label_149 = new JLabel("{");
		label_149.setFont(new Font("宋体", Font.PLAIN, 64));
		label_149.setBounds(29, 26, 38, 53);
		panel_15.add(label_149);
		
		JLabel label_150 = new JLabel("F = ");
		label_150.setFont(new Font("宋体", Font.PLAIN, 16));
		label_150.setBounds(13, 46, 32, 15);
		panel_15.add(label_150);
		
		label_15_0 = new JLabel(bean.get_15_0()+"");
		label_15_0.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					String value = JOptionPane.showInputDialog("请输入数值");
					if(value != null && !"".equals(value.trim())){
						if(MyUtillity.isNumber(value)){
							label_15_0.setText(value);
						}else{
							JOptionPane.showMessageDialog(TractionLevelEditDialog.this, "请输入数字");
						}
					}
				}
			}
		});
		label_15_0.setFont(new Font("宋体", Font.PLAIN, 16));
		label_15_0.setBounds(60, 20, 64, 15);
		panel_15.add(label_15_0);
		
		JLabel label_152 = new JLabel("0 < v <");
		label_152.setFont(new Font("宋体", Font.PLAIN, 16));
		label_152.setBounds(237, 20, 56, 15);
		panel_15.add(label_152);
		
		label_15_2 = new JLabel(bean.get_15_2()+"");
		label_15_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					String value = JOptionPane.showInputDialog("请输入数值");
					if(value != null && !"".equals(value.trim())){
						if(MyUtillity.isNumber(value)){
							label_15_2.setText(value);
						}else{
							JOptionPane.showMessageDialog(TractionLevelEditDialog.this, "请输入数字");
						}
					}
				}
			}
		});
		label_15_2.setFont(new Font("宋体", Font.PLAIN, 16));
		label_15_2.setBounds(298, 20, 37, 15);
		panel_15.add(label_15_2);
		
		label_15_3 = new JLabel(bean.get_15_3()+"");
		label_15_3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					String value = JOptionPane.showInputDialog("请输入数值");
					if(value != null && !"".equals(value.trim())){
						if(MyUtillity.isNumber(value)){
							label_15_3.setText(value);
						}else{
							JOptionPane.showMessageDialog(TractionLevelEditDialog.this, "请输入数字");
						}
					}
				}
			}
		});
		label_15_3.setFont(new Font("宋体", Font.PLAIN, 16));
		label_15_3.setBounds(60, 62, 86, 15);
		panel_15.add(label_15_3);
		
		JLabel label_155 = new JLabel("/");
		label_155.setFont(new Font("宋体", Font.PLAIN, 16));
		label_155.setBounds(144, 62, 20, 15);
		panel_15.add(label_155);
		
		JLabel lblV_1 = new JLabel("v");
		lblV_1.setFont(new Font("宋体", Font.PLAIN, 16));
		lblV_1.setBounds(160, 62, 20, 15);
		panel_15.add(lblV_1);
		
		label_15_2_1 = new JLabel(bean.get_15_2()+"");
		label_15_2_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					String value = JOptionPane.showInputDialog("请输入数值");
					if(value != null && !"".equals(value.trim())){
						if(MyUtillity.isNumber(value)){
							label_15_2_1.setText(value);
						}else{
							JOptionPane.showMessageDialog(TractionLevelEditDialog.this, "请输入数字");
						}
					}
				}
			}
		});
		label_15_2_1.setFont(new Font("宋体", Font.PLAIN, 16));
		label_15_2_1.setBounds(204, 62, 40, 15);
		panel_15.add(label_15_2_1);
		
		JLabel label_158 = new JLabel(" < v <");
		label_158.setFont(new Font("宋体", Font.PLAIN, 16));
		label_158.setBounds(233, 62, 56, 15);
		panel_15.add(label_158);
		
		label_15_4 = new JLabel(bean.get_15_4()+"");
		label_15_4.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					String value = JOptionPane.showInputDialog("请输入数值");
					if(value != null && !"".equals(value.trim())){
						if(MyUtillity.isNumber(value)){
							label_15_4.setText(value);
						}else{
							JOptionPane.showMessageDialog(TractionLevelEditDialog.this, "请输入数字");
						}
					}
				}
			}
		});
		label_15_4.setFont(new Font("宋体", Font.PLAIN, 16));
		label_15_4.setBounds(285, 62, 50, 15);
		panel_15.add(label_15_4);
		
		JLabel label_160 = new JLabel("* v +");
		label_160.setFont(new Font("宋体", Font.PLAIN, 16));
		label_160.setBounds(128, 20, 40, 15);
		panel_15.add(label_160);
		
		label_15_1 = new JLabel(bean.get_15_1()+"");
		label_15_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					String value = JOptionPane.showInputDialog("请输入数值");
					if(value != null && !"".equals(value.trim())){
						if(MyUtillity.isNumber(value)){
							label_15_1.setText(value);
						}else{
							JOptionPane.showMessageDialog(TractionLevelEditDialog.this, "请输入数字");
						}
					}
				}
			}
		});
		label_15_1.setFont(new Font("宋体", Font.PLAIN, 16));
		label_15_1.setBounds(172, 21, 50, 15);
		panel_15.add(label_15_1);

		setModal(true);
		MyUtillity.setFrameOnCenter(this);
		setVisible(true);
	}
	
	/**
	 * 保存列车牵引级位数据
	 * @param trainCategoryId
	 */
	public void saveTractionLevel(int trainCategoryId){
		TrainTractionLevelConf bean = new TrainTractionLevelConf();
		bean.set_1_0(Double.parseDouble(label_1_0.getText()));
		bean.set_1_1(Double.parseDouble(label_1_1.getText()));
		bean.set_1_2(Double.parseDouble(label_1_2.getText()));
		bean.set_1_3(Double.parseDouble(label_1_3.getText()));
		
		bean.set_2_0(Double.parseDouble(label_2_0.getText()));
		bean.set_2_1(Double.parseDouble(label_2_1.getText()));
		bean.set_2_2(Double.parseDouble(label_2_2.getText()));
		bean.set_2_3(Double.parseDouble(label_2_3.getText()));
		
		bean.set_3_0(Double.parseDouble(label_3_0.getText()));
		bean.set_3_1(Double.parseDouble(label_3_1.getText()));
		bean.set_3_2(Double.parseDouble(label_3_2.getText()));
		bean.set_3_3(Double.parseDouble(label_3_3.getText()));
		
		bean.set_4_0(Double.parseDouble(label_4_0.getText()));
		bean.set_4_1(Double.parseDouble(label_4_1.getText()));
		bean.set_4_2(Double.parseDouble(label_4_2.getText()));
		bean.set_4_3(Double.parseDouble(label_4_3.getText()));
		bean.set_4_4(Double.parseDouble(label_4_4.getText()));
		
		bean.set_5_0(Double.parseDouble(label_5_0.getText()));
		bean.set_5_1(Double.parseDouble(label_5_1.getText()));
		bean.set_5_2(Double.parseDouble(label_5_2.getText()));
		bean.set_5_3(Double.parseDouble(label_5_3.getText()));
		bean.set_5_4(Double.parseDouble(label_5_4.getText()));
		
		bean.set_6_0(Double.parseDouble(label_6_0.getText()));
		bean.set_6_1(Double.parseDouble(label_6_1.getText()));
		bean.set_6_2(Double.parseDouble(label_6_2.getText()));
		bean.set_6_3(Double.parseDouble(label_6_3.getText()));
		bean.set_6_4(Double.parseDouble(label_6_4.getText()));
		
		bean.set_7_0(Double.parseDouble(label_7_0.getText()));
		bean.set_7_1(Double.parseDouble(label_7_1.getText()));
		bean.set_7_2(Double.parseDouble(label_7_2.getText()));
		bean.set_7_3(Double.parseDouble(label_7_3.getText()));
		bean.set_7_4(Double.parseDouble(label_7_4.getText()));
		
		bean.set_8_0(Double.parseDouble(label_8_0.getText()));
		bean.set_8_1(Double.parseDouble(label_8_1.getText()));
		bean.set_8_2(Double.parseDouble(label_8_2.getText()));
		bean.set_8_3(Double.parseDouble(label_8_3.getText()));
		bean.set_8_4(Double.parseDouble(label_8_4.getText()));
		
		bean.set_9_0(Double.parseDouble(label_9_0.getText()));
		bean.set_9_1(Double.parseDouble(label_9_1.getText()));
		bean.set_9_2(Double.parseDouble(label_9_2.getText()));
		bean.set_9_3(Double.parseDouble(label_9_3.getText()));
		bean.set_9_4(Double.parseDouble(label_9_4.getText()));
		
		bean.set_10_0(Double.parseDouble(label_10_0.getText()));
		bean.set_10_1(Double.parseDouble(label_10_1.getText()));
		bean.set_10_2(Double.parseDouble(label_10_2.getText()));
		bean.set_10_3(Double.parseDouble(label_10_3.getText()));
		bean.set_10_4(Double.parseDouble(label_10_4.getText()));
		bean.set_10_5(Double.parseDouble(label_10_5.getText()));
		bean.set_10_6(Double.parseDouble(label_10_6.getText()));
		
		bean.set_11_0(Double.parseDouble(label_11_0.getText()));
		bean.set_11_1(Double.parseDouble(label_11_1.getText()));
		bean.set_11_2(Double.parseDouble(label_11_2.getText()));
		bean.set_11_3(Double.parseDouble(label_11_3.getText()));
		bean.set_11_4(Double.parseDouble(label_11_4.getText()));
		bean.set_11_5(Double.parseDouble(label_11_5.getText()));
		bean.set_11_6(Double.parseDouble(label_11_6.getText()));
		
		bean.set_12_0(Double.parseDouble(label_12_0.getText()));
		bean.set_12_1(Double.parseDouble(label_12_1.getText()));
		bean.set_12_2(Double.parseDouble(label_12_2.getText()));
		bean.set_12_3(Double.parseDouble(label_12_3.getText()));
		bean.set_12_4(Double.parseDouble(label_12_4.getText()));
		bean.set_12_5(Double.parseDouble(label_12_5.getText()));
		bean.set_12_6(Double.parseDouble(label_12_6.getText()));
		
		bean.set_13_0(Double.parseDouble(label_13_0.getText()));
		bean.set_13_1(Double.parseDouble(label_13_1.getText()));
		bean.set_13_2(Double.parseDouble(label_13_2.getText()));
		bean.set_13_3(Double.parseDouble(label_13_3.getText()));
		bean.set_13_4(Double.parseDouble(label_13_4.getText()));
		bean.set_13_5(Double.parseDouble(label_13_5.getText()));
		bean.set_13_6(Double.parseDouble(label_13_6.getText()));
		
		bean.set_14_0(Double.parseDouble(label_14_0.getText()));
		bean.set_14_1(Double.parseDouble(label_14_1.getText()));
		bean.set_14_2(Double.parseDouble(label_14_2.getText()));
		bean.set_14_3(Double.parseDouble(label_14_3.getText()));
		bean.set_14_4(Double.parseDouble(label_14_4.getText()));
		bean.set_14_5(Double.parseDouble(label_14_5.getText()));
		bean.set_14_6(Double.parseDouble(label_14_6.getText()));
		
		bean.set_15_0(Double.parseDouble(label_15_0.getText()));
		bean.set_15_1(Double.parseDouble(label_15_1.getText()));
		bean.set_15_2(Double.parseDouble(label_15_2.getText()));
		bean.set_15_3(Double.parseDouble(label_15_3.getText()));
		bean.set_15_4(Double.parseDouble(label_15_4.getText()));
		
		boolean result = TractionConfPanelService.saveTrainTractionLevelConf(trainCategoryId, bean);
		if(result){
			JOptionPane.showMessageDialog(TractionLevelEditDialog.this, "保存成功！");
			TractionLevelConfPanel.showTractionLevelChartBtn.doClick();
		}else{
			JOptionPane.showMessageDialog(TractionLevelEditDialog.this, "保存失败！");
		}
		
	}
}
