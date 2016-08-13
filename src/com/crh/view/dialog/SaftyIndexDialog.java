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
 * “顶层目标”中的“安全”
 * @author huhui
 *
 */
public class SaftyIndexDialog extends JDialog {
	private JTextField textField0;
	private JTextField textField1;
	private JTextField textField2;
	private JTextField textField3;
	private JTextField textField4;
	
	private TrainTopTarget trainTopTarget = null;

	/**
	 * Create the dialog.
	 */
	public SaftyIndexDialog(final TopTargetPanel owner, final int trainCategoryId, final TrainTopTarget trainTopTarget) {
		this.trainTopTarget = trainTopTarget;
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "\u5B89\u5168", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel label = new JLabel("\u4E34\u754C\u5931\u7A33\u5B9A\u901F\u5EA6(km/h)\uFF1A");
		label.setBounds(99, 44, 135, 15);
		panel.add(label);
		
		JLabel label_1 = new JLabel("\u8131\u8F68\u7CFB\u6570\uFF1A");
		label_1.setBounds(99, 87, 198, 15);
		panel.add(label_1);
		
		JLabel label_2 = new JLabel("\u8F6E\u91CD\u51CF\u8F7D\u7387\uFF1A");
		label_2.setBounds(99, 126, 120, 15);
		panel.add(label_2);
		
		JLabel label_3 = new JLabel("\u52A8\u6001\u6A2A\u5411\u529B\uFF1A");
		label_3.setBounds(99, 167, 198, 15);
		panel.add(label_3);
		
		JLabel label_4 = new JLabel("\u7D27\u6025\u5236\u52A8\u8DDD\u79BB(m)\uFF1A");
		label_4.setBounds(99, 204, 234, 15);
		panel.add(label_4);
		
		textField0 = new JTextField();
		textField0.setText(trainTopTarget.getSafty0() + "");
		textField0.setColumns(10);
		textField0.setBounds(232, 41, 245, 21);
		panel.add(textField0);
		
		textField1 = new JTextField();
		textField1.setText(trainTopTarget.getSafty1() + "");
		textField1.setColumns(10);
		textField1.setBounds(160, 84, 317, 21);
		panel.add(textField1);
		
		textField2 = new JTextField();
		textField2.setText(trainTopTarget.getSafty2() + "");
		textField2.setColumns(10);
		textField2.setBounds(170, 123, 307, 21);
		panel.add(textField2);
		
		textField3 = new JTextField();
		textField3.setText(trainTopTarget.getSafty3() + "");
		textField3.setColumns(10);
		textField3.setBounds(170, 164, 307, 21);
		panel.add(textField3);
		
		textField4 = new JTextField();
		textField4.setText(trainTopTarget.getSafty4() + "");
		textField4.setColumns(10);
		textField4.setBounds(206, 201, 271, 21);
		panel.add(textField4);
		
		JButton button = new JButton("\u4FEE\u6539");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(checkParameters()){
					TopTargetService.saveTrainTopTarget(trainTopTarget, trainCategoryId);
					owner.reflash(trainCategoryId);
					JOptionPane.showMessageDialog(SaftyIndexDialog.this, "保存成功！");
				}else{
					JOptionPane.showMessageDialog(SaftyIndexDialog.this, "参数不正确，请检查");
				}
			}
		});
		button.setBounds(128, 255, 93, 23);
		panel.add(button);
		
		JButton button_1 = new JButton("\u53D6\u6D88");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		button_1.setBounds(324, 255, 93, 23);
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
		String safty0Str = textField0.getText().trim();
		String safty1Str = textField1.getText().trim();
		String safty2Str = textField2.getText().trim();
		String safty3Str = textField3.getText().trim();
		String safty4Str = textField4.getText().trim();
		try {
			trainTopTarget.setSafty0(Double.parseDouble(safty0Str));
			trainTopTarget.setSafty1(Double.parseDouble(safty1Str));
			trainTopTarget.setSafty2(Double.parseDouble(safty2Str));
			trainTopTarget.setSafty3(Double.parseDouble(safty3Str));
			trainTopTarget.setSafty4(Double.parseDouble(safty4Str));
			b = true;
		} catch (NumberFormatException e) {
			b = false;
			e.printStackTrace();
		}
		return b;
	}

}
