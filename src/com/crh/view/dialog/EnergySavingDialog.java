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
 * “节能设置”对话框
 * @author huhui
 *
 */
public class EnergySavingDialog extends JDialog {
	private JTextField textField0;
	private JTextField textField1;
	private JTextField textField2;
	private JTextField textField3;
	private JTextField textField4;
	
	private TrainTopTarget trainTopTarget = null;

	/**
	 * Create the dialog.
	 */
	public EnergySavingDialog(final TopTargetPanel owner, final int trainCategoryId, final TrainTopTarget trainTopTarget) {
		this.trainTopTarget = trainTopTarget;
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "\u8282\u80FD", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel label = new JLabel("a\uFF1A");
		label.setBounds(41, 45, 54, 15);
		panel.add(label);
		
		textField0 = new JTextField();
		textField0.setText(trainTopTarget.getEnergy0() + "");
		textField0.setColumns(10);
		textField0.setBounds(62, 42, 289, 21);
		panel.add(textField0);
		
		JLabel label_1 = new JLabel("b\uFF1A");
		label_1.setBounds(41, 92, 54, 15);
		panel.add(label_1);
		
		textField1 = new JTextField();
		textField1.setText(trainTopTarget.getEnergy1() + "");
		textField1.setColumns(10);
		textField1.setBounds(62, 89, 289, 21);
		panel.add(textField1);
		
		JLabel label_2 = new JLabel("c\uFF1A");
		label_2.setBounds(41, 130, 54, 15);
		panel.add(label_2);
		
		textField2 = new JTextField();
		textField2.setText(trainTopTarget.getEnergy2() + "");
		textField2.setColumns(10);
		textField2.setBounds(62, 127, 289, 21);
		panel.add(textField2);
		
		JLabel label_3 = new JLabel("\u8F74\u91CD(t)\uFF1A");
		label_3.setBounds(41, 173, 54, 15);
		panel.add(label_3);
		
		textField3 = new JTextField();
		textField3.setText(trainTopTarget.getEnergy3() + "");
		textField3.setColumns(10);
		textField3.setBounds(93, 170, 258, 21);
		panel.add(textField3);
		
		JLabel label_4 = new JLabel("\u7275\u5F15\u4F20\u52A8\u7CFB\u7EDF\u3001\u518D\u751F\u5236\u52A8\u7CFB\u7EDF\u6548\u7387(%)\uFF1A");
		label_4.setBounds(41, 212, 210, 15);
		panel.add(label_4);
		
		textField4 = new JTextField();
		textField4.setText(trainTopTarget.getEnergy4() + "");
		textField4.setColumns(10);
		textField4.setBounds(249, 209, 279, 21);
		panel.add(textField4);
		
		JButton button = new JButton("\u4FEE\u6539");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(checkParameters()){
					TopTargetService.saveTrainTopTarget(trainTopTarget, trainCategoryId);
					owner.reflash(trainCategoryId);
					JOptionPane.showMessageDialog(EnergySavingDialog.this, "保存成功！");
				}else{
					JOptionPane.showMessageDialog(EnergySavingDialog.this, "参数不正确，请检查");
				}
			}
		});
		button.setBounds(128, 254, 93, 23);
		panel.add(button);
		
		JButton button_1 = new JButton("\u53D6\u6D88");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		button_1.setBounds(324, 254, 93, 23);
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
		String energy0Str = textField0.getText().trim();
		String energy1Str = textField1.getText().trim();
		String energy2Str = textField2.getText().trim();
		String energy3Str = textField3.getText().trim();
		String energy4Str = textField4.getText().trim();
		try {
			trainTopTarget.setEnergy0(Double.parseDouble(energy0Str));
			trainTopTarget.setEnergy1(Double.parseDouble(energy1Str));
			trainTopTarget.setEnergy2(Double.parseDouble(energy2Str));
			trainTopTarget.setEnergy3(Double.parseDouble(energy3Str));
//			trainTopTarget.setEnergy4(Double.parseDouble(energy4Str));
			b = true;
		} catch (NumberFormatException e) {
			b = false;
			e.printStackTrace();
		}
		return b;
	}

}
