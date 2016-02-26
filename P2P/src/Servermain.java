import java.io.*;
import java.net.*;
import java.util.*;

public class Servermain implements Runnable{
	
	public static Socket connection;
	static boolean first;
	private static String process = "";
	static HashMap<String, Set<String>> fm= new HashMap<String, Set<String>>(); 
	public static Set<String> activenodes = new HashSet<String>();
	public static Set<Socket> conns = new HashSet<Socket>();
	static HashMap<String, HashMap<String, Set<String>>> fm2 = new HashMap<String, HashMap<String, Set<String>>>(); 
	String[] process1;
	String ipaddress = new String();
	
	public static void  updatelist(String a[], String ipaddr)
	{
		for(int i=0; i<a.length; i++)
		{
		   String x=a[i];
		   if(fm.containsKey(x))
			{
			   if(fm.get(x).contains(ipaddr))
				{}
			   else
			      {
		   		fm.get(x).add(ipaddr);
			       }	
	          }
		   else
		     {
	 		String key=x;
			Set<String> set = new HashSet<String>();
					set.add(ipaddr);
	                fm.put(key, set);
	                
		       }
		}
	}

	public static void deletelist(String a[], String ipaddr)
	{
		for(int i=0; i<a.length; i++)
		{
		   String x=a[i];
			fm.get(x).remove(ipaddr);
			if (fm.get(x).isEmpty())
				{
				fm.remove(x);
				}
		}
	}
	
	public void broadcast(){
		try{
			System.out.println("in broadcast:");
			
			String d = new String();
			d = "Server";
			System.out.println("before activenode loop:");
			String g = new String();
			for(Iterator<String> it = activenodes.iterator(); it.hasNext();){
				g = g+it.next();
			}
			System.out.println("after activenode loop:");
			Iterator<Socket> conns1 = conns.iterator();
			
			while(conns1.hasNext()){
				System.out.println("in conns1 loop:");
				Socket s = conns1.next();
				BufferedOutputStream bos1 = new BufferedOutputStream(s.getOutputStream());
				ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
				OutputStreamWriter osw1 = new OutputStreamWriter(bos1);
				fm2.clear();
				fm2.put(g, fm);
				System.out.println(d);
				System.out.println(fm2);
				osw1.write(d);
				osw1.flush();
				osw1.write(fm2.toString()+(char)0);
				osw1.flush();
				oos.writeObject(fm2);
				oos.flush();
				System.out.println("Done broadcasting");
			}
		}
		catch(Exception d){
			System.out.println("in broadcast error: "+d);
		}
	}
	
	public void run(){
		try{
			ipaddress = connection.getInetAddress().toString();
			System.out.println();
			System.out.println("Connected to Client: "+ipaddress);
			process = "";
			BufferedInputStream bis = new BufferedInputStream(connection.getInputStream());
			InputStreamReader isr = new InputStreamReader(bis);
			char character;
			while((character=(char)isr.read()) != (char)3){
				process = process+character;
			}
			
			
			process1 = process.split(",");
			
			if(process1[0].equals("con")){
				System.out.println("if loop for con: ");
				updatelist(process1, ipaddress);
				fm.remove("con");
				broadcast();
				System.out.println("Back to run");
			}
			else if(process1[0].equals("add")){
				System.out.println("loop add ");
				updatelist(process1, ipaddress);
				fm.remove("add");
				broadcast();
			}
			else{
				System.out.println("Wrong input for loop broadcast!!!");
			}
			boolean condition = true;
			System.out.println("waiting for news ... ");
			while(condition){
				Thread.sleep(1000);
/*				process = "";
				while((character=(char)isr.read()) != (char)3){
					process = process+character;
				}
				if(process.contains("disconnect")){
					condition = false;
					System.out.println("Client "+ipaddress+" is disconnected!");
					activenodes.remove(ipaddress);
					System.out.println("1");
					for(int z=0; z<process1.length; z++){
						System.out.println(process1[z]);
					}
					System.out.println(ipaddress);
					deletelist(process1, ipaddress);
					System.out.println("2");
					broadcast();
				}*/
				if(connection.isClosed() == true){
					System.out.println("Client "+ipaddress+" is disconnected!");
					condition = false;
				}
			}
		}
		catch(Exception f){
			System.out.println("in run exception: "+f);
		}
		finally{
				try{
					connection.close();
				}
				catch(Exception e1){
					System.out.println("in finally of thread: "+e1);
				}
		}
	}
	
	public static void main(String args[]){
		try {
			int port = 19999;
			
			@SuppressWarnings("resource")
			ServerSocket sock1 = new ServerSocket(port);
			System.out.println("Server initialized");
			System.out.println("Server's IP: "+InetAddress.getLocalHost().getHostAddress());
			
			while(true){
				connection = sock1.accept();
				
				Runnable runnable = new Servermain(connection);
				Thread thread = new Thread(runnable);
				
				String ipaddress = connection.getInetAddress().toString();
				activenodes.add(ipaddress);
				System.out.println("ActiveNodes: ");
				for(Iterator<String> it = activenodes.iterator(); it.hasNext();){
					System.out.println(it.next());
				}
				
				thread.start();
			}
		}
		catch(Exception e){
			System.out.println(e);
		}
		finally{
			try{
				connection.close();
			}
			catch(Exception e1){
				System.out.println("in finally of main"+e1);
			}
		}
	}
	
	Servermain(Socket s){
		Servermain.connection = s;
		conns.add(Servermain.connection);
	}
	
}