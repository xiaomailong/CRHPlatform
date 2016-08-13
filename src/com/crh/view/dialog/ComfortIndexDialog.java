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
import javax.swing.border.TitledBorder;

import com.crh.service.TopTargetService;
import com.crh.view.panel.TopTargetPanel;
import com.crh2.javabean.TrainTopTarget;
import com.crh2.util.MyUtillity;

/**
 * “顶层目标”中的“舒适度”
 * @author huhui
 *
 */
public class ComfortIndexDialog extends JDialog {
	private JTextField textField0;
	private JTextField textField1;
	private JTextField textField2;
	private JTextField textField3;
	private JTextField textField4;
	private JTextField textField5;
	private JTextField textField6;
	/**
	 * 顶层目标的JavaBean
	 */
	private TrainTopTarget trainTopTarget = null;

	
	public ComfortIndexDialog(final TopTargetPanel owner, final int trainCategoryId, final TrainTopTarget trainTopTarget) {
		this.trainTopTarget = trainTopTarget;
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "\u8212\u9002\u5EA6", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel label = new JLabel("\u5BA4\u5185\u6E29\u5EA6(\u2103)\uFF1A");
		label.setBounds(93, 45, 84, 15);
		panel.add(label);
		
		textField0 = new JTextField();
		textField0.setText(trainTopTarget.getComfort0() + "");
		textField0.setColumns(10);
		textField0.setBounds(176, 42, 295, 21);
		panel.add(textField0);
		
		JLabel label_1 = new JLabel("\u5BA4\u5185\u6E7F\u5EA6(%)\uFF1A");
		label_1.setBounds(93, 88, 84, 15);
		panel.add(label_1);
		
		textField1 = new JTextField();
		textField1.setText(trainTopTarget.getComfort1() + "");
		textField1.setColumns(10);
		textField1.setBounds(176, 85, 295, 21);
		panel.add(textField1);
		
		textField2 = new JTextField();
		textField2.setText(trainTopTarget.getComfort2() + "");
		textField2.setColumns(10);
		textField2.setBounds(281, 124, 190, 21);
		panel.add(textField2);
		
		textField3 = new JTextField();
		textField3.setText(trainTopTarget.getComfort3() + "");
		textField3.setColumns(10);
		textField3.setBounds(207, 161, 264, 21);
		panel.add(textField3);
		
		textField4 = new JTextField();
		textField4.setText(trainTopTarget.getComfort4() + "");
		textField4.setColumns(10);
		textField4.setBounds(176, 200, 295, 21);
		panel.add(textField4);
		
		JLabel label_2 = new JLabel("\u8F66\u5185\u566A\u58F0(dB)\uFF1A");
		label_2.setBounds(93, 203, 93, 15);
		panel.add(label_2);
		
		JLabel label_3 = new JLabel("\u8F66\u5185\u65B0\u98CE\u91CF(m^3/h)\uFF1A");
		label_3.setBounds(93, 164, 114, 15);
		panel.add(label_3);
		
		JLabel label_4 = new JLabel("\u8F66\u5185\u6C14\u538B\u6C34\u5E73\u4E0E\u6C14\u538B\u6CE2\u52A8(kPa/s)\uFF1A");
		label_4.setBounds(93, 127, 203, 15);
		panel.add(label_4);
		
		JButton button = new JButton("\u4FEE\u6539");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(checkParameters()){
					TopTargetService.saveTrainTopTarget(trainTopTarget, trainCategoryId);
					owner.reflash(trainCategoryId);
					JOptionPane.showMessageDialog(ComfortIndexDialog.this, "保存成功！");
				}else{
					JOptionPane.showMessageDialog(ComfortIndexDialog.this, "参数不正确，请检查");
				}
			}
		});
		button.setBounds(127, 283, 93, 23);
		panel.add(button);
		
		JLabel label_5 = new JLabel("\u8F66\u4F53\u632F\u52A8(\u5782\u5411\u3001\u6A2A\u5411)(Hz)\uFF1A");
		label_5.setBounds(93, 238, 156, 15);
		panel.add(label_5);
		
		textField5 = new JTextField();
		textField5.setText(trainTopTarget.getComfort5() + "");
		textField5.setColumns(10);
		textField5.setBounds(247, 235, 66, 21);
		panel.add(textField5);
		
		textField6 = new JTextField();
		textField6.setText(trainTopTarget.getComfort6() + "");
		textField6.setColumns(10);
		textField6.setBounds(333, 235, 66, 21);
		panel.add(textField6);
		
		JButton button_1 = new JButton("\u53D6\u6D88");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		button_1.setBounds(327, 283, 93, 23);
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
		String comfort0Str = textField0.getText().trim();
		String comfort1Str = textField1.getText().trim();
		String comfort2Str = textField2.getText().trim();
		String comfort3Str = textField3.getText().trim();
		String comfort4Str = textField4.getText().trim();
		String comfort5Str = textField5.getText().trim();
		String comfort6Str = textField6.getText().trim();
		try {
			trainTopTarget.setComfort0(Double.parseDouble(comfort0Str));
			trainTopTarget.setComfort1(Double.parseDouble(comfort1Str));
			trainTopTarget.setComfort2(Double.parseDouble(comfort2Str));
			trainTopTarget.setComfort3(Double.parseDouble(comfort3Str));
			trainTopTarget.setComfort4(Double.parseDouble(comfort4Str));
			trainTopTarget.setComfort5(Double.parseDouble(comfort5Str));
			trainTopTarget.setComfort6(Double.parseDouble(comfort6Str));
			b = true;
		} catch (NumberFormatException e) {
			b = false;
			e.printStackTrace();
		}
		return b;
	}

}
