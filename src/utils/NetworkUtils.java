package utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;

public class NetworkUtils {
	public static String getIpsFomThisMachine(){
		try {
			StringBuilder strBuilder = new StringBuilder(); 
//			return InetAddress.getLocalHost().getHostAddress();
			Enumeration<NetworkInterface> interfacesList =  NetworkInterface.getNetworkInterfaces();
			
			while (interfacesList.hasMoreElements()) {
				Enumeration<InetAddress> inetList = interfacesList.nextElement().getInetAddresses();				
				while(inetList.hasMoreElements()){
					String ip = inetList.nextElement().getHostAddress();
					if(ip.contains(".")){
						String separador = strBuilder.length() > 0 ? " , " : "";
						strBuilder.append(separador+ip);
					}					
				}
			}			
			return strBuilder.toString();
		} catch (Exception e) {
			return "<erro ao pegar ip["+e.getMessage()+"]>";
		}
	}
}
