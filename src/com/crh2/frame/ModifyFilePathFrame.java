/**
 * �޸�txt����·����txt
 */
package com.crh2.frame;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.crh2.dao.SQLHelper;
import com.crh2.service.PathFileService;
import com.crh2.util.MyUtillity;

public class ModifyFilePathFrame extends JFrame implements ActionListener {

	/**
	 * @param args
	 */
	// ����label
	private JLabel pathLabel;
	private JTextField pathText;
	private JButton fileChooseButton,confirmButton, cancelButton;
	//�����ļ�ѡ����
	private JFileChooser fileChooser;
	//�����ļ�������
	private FileNameExtensionFilter fileFilter;
	//����panel
	private JPanel contentPanel,buttonPanel;
	//�µ�·��path
	private String newPath = "";

	// ���캯������ʼ�������ؼ�������
	public ModifyFilePathFrame() {
		contentPanel = new JPanel();
		// ��ʼ��label
		pathLabel = new JLabel("����·����");
		// ��ʼ��textfield
		pathText = new JTextField(41);
		pathText.setText(PathFileService.getDefaultPath());
		// ��ʼ��button
		fileChooseButton = new JButton("·���޸�");
		contentPanel.add(pathLabel,BorderLayout.WEST);
		contentPanel.add(pathText,BorderLayout.CENTER);
		contentPanel.add(fileChooseButton,BorderLayout.SOUTH);
		contentPanel.setBorder(BorderFactory.createTitledBorder("·��ѡ��"));
		
		buttonPanel = new JPanel();
		confirmButton = new JButton("����");
		cancelButton = new JButton("ȡ��");
		buttonPanel.add(confirmButton);
		buttonPanel.add(cancelButton);
		
		// Ϊ��ť����¼���Ӧ
		fileChooseButton.addActionListener(this);
		fileChooseButton.setActionCommand("chooseFile");
		confirmButton.addActionListener(this);
		confirmButton.setActionCommand("confirm");
		cancelButton.addActionListener(this);
		cancelButton.setActionCommand("cancel");

		// ����frameΪtop
		this.setAlwaysOnTop(true);
		// ���ؼ����뵽frame��
		this.add(contentPanel,BorderLayout.CENTER);
		this.add(buttonPanel,BorderLayout.SOUTH);
		
		this.setSize(360, 240);
		this.setTitle("\"��������\"�ļ�����·��");
		// ����frame����
		MyUtillity.setFrameOnCenter(this);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setVisible(true);
		this.setResizable(false);
	}
	
	// ������ļ�ѡ�񡱰�ť
	public void fileChooseButtonClicked() {
		// ��ʼ��JFileChooser
		fileChooser = new JFileChooser();
		fileFilter = new FileNameExtensionFilter(".txt", ".txt");
		fileChooser.setFileFilter(fileFilter);
		fileChooser.setVisible(true);// ��ʾ
		int retval = fileChooser.showOpenDialog(this);
		if (retval == JFileChooser.APPROVE_OPTION) {
			// �õ��û�ѡ���ļ��ľ���·��
			String filePath = fileChooser.getSelectedFile().getAbsolutePath();
			// ���ļ�·���ı���ֵ
			pathText.setText(filePath.trim()+".txt");
			newPath = filePath.trim()+".txt";
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
		ModifyFilePathFrame modifyFilePathFrame = new ModifyFilePathFrame();
	}

	// �޸����ݿ���path
	public boolean modifyPath(String newPath) {
		newPath = newPath.replaceAll("\\\\", "\\\\\\\\");
		SQLHelper sqlHelper = new SQLHelper();
		// ��֯sql���
		String sql = "UPDATE traintypesavepath SET path='"+newPath+"'";
		return sqlHelper.update(sql, null);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getActionCommand().equals("confirm")) {
			if(this.modifyPath(newPath)){
				JOptionPane.showMessageDialog(this, "·���޸ĳɹ���");
			}else{
				JOptionPane.showMessageDialog(this, "·���޸�ʧ�ܣ�");
			}
		} else if (e.getActionCommand().equals("cancel")) {
			this.dispose();
		}else if(e.getActionCommand().equals("chooseFile")){
			this.fileChooseButtonClicked();
		}
	}

}
