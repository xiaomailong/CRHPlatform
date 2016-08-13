package com.crh2.socket;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import com.crh2.service.DataService;

/**
 * ��̬�����У�����������������������ʱ������
 * @author huhui
 *
 */
public class SendTrainInfo implements Runnable {
	
	/**
	 * �����׼���udpsocket����
	 */
	private DatagramSocket ds = null;
	private DatagramPacket dp = null;
	InetAddress destination = null;
	private byte[] buf = null;
	
	private int PORT_SELF = 0;
	private int PORT_SEND_TO = 0;
	private String SEND_TO_IP = "";
	
	private boolean isRun = true;
	/**
	 * ����+T   �ٶȣ���̣��ƶ������Ƿ��٣�1���٣�0������
	 */
	private String info = "";
	
	/**
	 * �����ݿ����õ��˿���Ϣ
	 */
	private DataService dataService = new DataService();
	
	/**
	 * ���캯������ʼ������
	 */
	public SendTrainInfo(){
		PORT_SELF = Integer.parseInt(dataService.getServerinfo().getPort());
		PORT_SEND_TO = Integer.parseInt(dataService.getServerinfo().getSendToPort());
		SEND_TO_IP = dataService.getServerinfo().getSendToIp();
//		System.out.println(PORT_SELF+", "+PORT_SEND_TO+", "+SEND_TO_IP);
		try {
			ds = new DatagramSocket(7778);// ʵ�����׼��֣�ָ���Լ���port
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * ��������
	 * @param info
	 */
	public void sendInfo(String info){
		if(info == null || info.equals("")){
			return;
		}
		buf = info.getBytes(); // ����
		try {
			destination = InetAddress.getByName(SEND_TO_IP);
			dp = new DatagramPacket(buf, buf.length, destination, PORT_SEND_TO);// �����DatagramPacket�����У�DatagramSocket��send()�������ܴ��࣬ע��10000�ǽ��ܵ�ַ�Ķ˿ڣ���ͬ���Լ��Ķ˿ڣ���
			ds.send(dp); // ��������
//			ds.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		SendTrainInfo sendTrainInfo = new SendTrainInfo();
		sendTrainInfo.setSendInfo("12345");
		Thread thread = new Thread(sendTrainInfo);
		thread.start();
	}
	
	public void setSendInfo(String info){
		this.info = info;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(isRun){
			this.sendInfo(info);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
