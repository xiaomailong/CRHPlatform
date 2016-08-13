package com.crh2.socket;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import com.crh2.service.DataService;

/**
 * 常态运行中，仿真软件向服务器发送运行时的数据
 * @author huhui
 *
 */
public class SendTrainInfo implements Runnable {
	
	/**
	 * 建立套间字udpsocket服务
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
	 * 数据+T   速度，里程，制动力，是否超速，1超速，0不超速
	 */
	private String info = "";
	
	/**
	 * 从数据库中拿到端口信息
	 */
	private DataService dataService = new DataService();
	
	/**
	 * 构造函数，初始化连接
	 */
	public SendTrainInfo(){
		PORT_SELF = Integer.parseInt(dataService.getServerinfo().getPort());
		PORT_SEND_TO = Integer.parseInt(dataService.getServerinfo().getSendToPort());
		SEND_TO_IP = dataService.getServerinfo().getSendToIp();
//		System.out.println(PORT_SELF+", "+PORT_SEND_TO+", "+SEND_TO_IP);
		try {
			ds = new DatagramSocket(7778);// 实例化套间字，指定自己的port
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 发送数据
	 * @param info
	 */
	public void sendInfo(String info){
		if(info == null || info.equals("")){
			return;
		}
		buf = info.getBytes(); // 数据
		try {
			destination = InetAddress.getByName(SEND_TO_IP);
			dp = new DatagramPacket(buf, buf.length, destination, PORT_SEND_TO);// 打包到DatagramPacket类型中（DatagramSocket的send()方法接受此类，注意10000是接受地址的端口，不同于自己的端口！）
			ds.send(dp); // 发送数据
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
