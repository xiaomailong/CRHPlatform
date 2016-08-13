package com.crh.view.dialog;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

import com.crh.service.TopTargetService;
import com.crh.view.panel.TopTargetPanel;
import com.crh2.javabean.TrainTopTarget;
import com.crh2.util.MyUtillity;

/**
 * “顶层目标”中的“速度”
 * @author huhui
 *
 */
public class SpeedIndexDialog extends JDialog {
	private JTextField speedField0;
	private JTextField speedField1;
	private JTextField speedField2;
	private JTextField speedField3;
	private JTextField speedField4;
	
	private TrainTopTarget trainTopTarget = null;

	public SpeedIndexDialog(final TopTargetPanel owner, final int trainCategoryId, final TrainTopTarget trainTopTarget) {
		this.trainTopTarget = trainTopTarget;
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "\u901F\u5EA6", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel label = new JLabel("\u6700\u9AD8\u8FD0\u884C\u901F\u5EA6(km/h)\uFF1A");
		label.setBounds(96, 54, 120, 15);
		panel.add(label);
		
		speedField0 = new JTextField();
		speedField0.setText(trainTopTarget.getSpeed0() + "");
		speedField0.setColumns(10);
		speedField0.setBounds(219, 51, 255, 21);
		panel.add(speedField0);
		
		JLabel label_1 = new JLabel("\u6700\u9AD8\u8FD0\u884C\u901F\u5EA6->\u5269\u4F59\u52A0\u901F\u5EA6(m/s^2)\uFF1A");
		label_1.setBounds(96, 99, 198, 15);
		panel.add(label_1);
		
		speedField1 = new JTextField();
		speedField1.setText(trainTopTarget.getSpeed1() + "");
		speedField1.setColumns(10);
		speedField1.setBounds(293, 96, 181, 21);
		panel.add(speedField1);
		
		JLabel label_2 = new JLabel("\u6301\u7EED\u8FD0\u884C\u901F\u5EA6(km/h)\uFF1A");
		label_2.setBounds(96, 140, 120, 15);
		panel.add(label_2);
		
		speedField2 = new JTextField();
		speedField2.setText(trainTopTarget.getSpeed2() + "");
		speedField2.setColumns(10);
		speedField2.setBounds(219, 137, 255, 21);
		panel.add(speedField2);
		
		JLabel label_3 = new JLabel("\u6301\u7EED\u8FD0\u884C\u901F\u5EA6->\u5269\u4F59\u52A0\u901F\u5EA6(m/s^2)\uFF1A");
		label_3.setBounds(96, 184, 198, 15);
		panel.add(label_3);
		
		speedField3 = new JTextField();
		speedField3.setText(trainTopTarget.getSpeed3() + "");
		speedField3.setColumns(10);
		speedField3.setBounds(293, 181, 181, 21);
		panel.add(speedField3);
		
		JLabel label_4 = new JLabel("\u6307\u5B9A\u901F\u5EA6\u533A\u6BB5\u5185\u7684\u5E73\u5747\u542F\u52A8\u52A0\u901F\u5EA6(m/s^2)\uFF1A");
		label_4.setBounds(96, 221, 234, 15);
		panel.add(label_4);
		
		speedField4 = new JTextField();
		speedField4.setText(trainTopTarget.getSpeed4() + "");
		speedField4.setColumns(10);
		speedField4.setBounds(328, 218, 146, 21);
		panel.add(speedField4);
		
		JButton button = new JButton("修改");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(checkParameters()){
					TopTargetService.saveTrainTopTarget(trainTopTarget, trainCategoryId);
					owner.reflash(trainCategoryId);
					JOptionPane.showMessageDialog(SpeedIndexDialog.this, "保存成功！");
				}else{
					JOptionPane.showMessageDialog(SpeedIndexDialog.this, "参数不正确，请检查");
				}
			}
		});
		button.setBounds(135, 272, 93, 23);
		panel.add(button);
		
		JButton button_1 = new JButton("\u53D6\u6D88");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		button_1.setBounds(328, 272, 93, 23);
		panel.add(button_1);
		
		setBounds(100, 100, 583, 372);
		getContentPane().setLayout(new GridLayout(1, 1, 0, 0));
		MyUtillity.setFrameOnCenter(this);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setModal(true);
		setVisible(true);
	}
	
	/**
	 * 参数检查
	 */
	public boolean checkParameters(){
		boolean b = false;
		String speed0Str = speedField0.getText().trim();
		String speed1Str = speedField1.getText().trim();
		String speed2Str = speedField2.getText().trim();
		String speed3Str = speedField3.getText().trim();
		String speed4Str = speedField4.getText().trim();
		try {
			trainTopTarget.setSpeed0(Double.parseDouble(speed0Str));
			trainTopTarget.setSpeed1(Double.parseDouble(speed1Str));
			trainTopTarget.setSpeed2(Double.parseDouble(speed2Str));
			trainTopTarget.setSpeed3(Double.parseDouble(speed3Str));
			trainTopTarget.setSpeed4(Double.parseDouble(speed4Str));
			b = true;
		} catch (NumberFormatException e) {
			b = false;
			e.printStackTrace();
		}
		return b;
	}

}
