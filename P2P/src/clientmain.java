

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.StreamCorruptedException;
import java.lang.*;
import java.lang.Exception;
import java.lang.Iterable;
import java.lang.String;
import java.lang.StringBuffer;
import java.lang.System;
import java.lang.reflect.Array;
import java.net.*;
import java.io.*;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;



public class clientmain implements Runnable{

    public static Socket connection;
    static HashMap<String, Set<String>> fm = new HashMap<String, Set<String>>();
    public static Set<String> activenodes = new HashSet<String>();
    private JFrame frame;

    //@SuppressWarnings("unchecked")

    public static byte[] upload(String fileName) {
        try {
            System.out.println("in upload");
            String fName = "\\shared\\" + fileName;
            File myFile = new File(fName);
            byte[] mybytearray = new byte[(int) myFile.length()];
            System.out.println("created byte array");
            FileInputStream fis = new FileInputStream(myFile);
            BufferedInputStream bis = new BufferedInputStream(fis);
            System.out.println("ready to write");
            bis.read(mybytearray, 0, mybytearray.length);
            System.out.println("done writing");
            return mybytearray;
        }
        catch (Exception e) {
            System.out.println("in upload " + e);
        }
        byte[] a = new byte[1];
        return a;

        /*OutputStream os = ***sock***.getOutputStream();
        //System.out.println("Sending " + FILE_TO_SEND + "(" + mybytearray.length + " bytes)");
        os.write(mybytearray,0,mybytearray.length);
        os.flush();*/
    }

    public static void main(String[] args){

        //StringBuffer instr = new StringBuffer();

        String currentDirectory = System.getProperty("user.dir");
        currentDirectory = currentDirectory+"\\shared";
        File f = new File(currentDirectory);
        //System.out.println(currentDirectory);
        ArrayList<String> names = new ArrayList<String>(Arrays.asList(f.list()));
        String filesList = "";

        for (Iterator<String> it = names.iterator(); it.hasNext();) {
            filesList+= it.next();
            filesList+= ",";
        }

        filesList = filesList.substring(0,filesList.length()-1);
        System.out.println(filesList);

        try {
            InetAddress host = InetAddress.getByName(args[0]);
            int serverPort = 19999;
            int clientPort = 20000;
            Socket serverSocket = new Socket(host, serverPort);

            BufferedOutputStream bos = new BufferedOutputStream(serverSocket.getOutputStream());
            OutputStreamWriter osw = new OutputStreamWriter(bos, "US-ASCII");
            String s = "con," + filesList + (char) 3;
            System.out.println(s);
            osw.write(s);
            osw.flush();

            System.out.println("server socket created");


            //Socket connectServer = serverSocket.accept();
            Runnable r1 = new clientmain(serverSocket);
            Thread serverThread = new Thread(r1);
            serverThread.start();

            System.out.println("created server thread");

            ServerSocket clientSocket = new ServerSocket(clientPort);
            System.out.println("created client socket");
            Socket clientConnect = clientSocket.accept();
            System.out.println("client socket created");
            Runnable r2 = new clientmain(clientConnect);
            Thread clientThread = new Thread(r2);
            clientThread.start();
            System.out.println("created client thread");
        }

         /*   InetAddress host = InetAddress.getByName(args[0]);
            int serverPort = 19999;
            int clientPort = 20000;
            Socket serverSocket = new Socket(host, serverPort);

            BufferedOutputStream bos = new BufferedOutputStream(serverSocket.getOutputStream());
            OutputStreamWriter osw = new OutputStreamWriter(bos, "US-ASCII");
            String s = "con," + filesList + (char)3;
            System.out.println(s);
            osw.write(s);
            osw.flush();

            ServerSocket clientSocket = new ServerSocket(clientPort);
            Socket clientConnect = clientSocket.accept();

            /*try{
                BufferedInputStream bis = new BufferedInputStream(serverSocket.getInputStream());
                InputStreamReader isr = new InputStreamReader(bis, "US-ASCII");
                //System.out.println("waiting for reply...");
                int c;
                while ((c = isr.read()) != 0){
                    System.out.print((char) c);
                    instr.append((char) c);
                }
                System.out.println(instr);
            }
            catch(Exception e){
                System.out.println(e);
            }

            while(true){
                try{
                    /*BufferedInputStream bis = new BufferedInputStream(serverSocket.getInputStream());
                    InputStreamReader isr = new InputStreamReader(bis, "US-ASCII");
                    //System.out.println("waiting for reply...");
                    int c;
                    while ((c = isr.read()) != 0){
                        //System.out.print((char) c);
                        instr.append((char) c);
                    }
                    //System.out.println(instr);

                    @SuppressWarnings("unchecked")

                    //if(instr.equals(new StringBuffer("Server"))){
                        ObjectInputStream ois = new ObjectInputStream(serverSocket.getInputStream());
                        HashMap<String, HashMap<String, Set<String>>> fm = new HashMap<String, HashMap<String, Set<String>>>((HashMap<String, HashMap<String, Set<String>>>)ois.readObject());

                    //}

                    BufferedInputStream bis = new BufferedInputStream(clientConnect.getInputStream());
                    InputStreamReader isr = new InputStreamReader(bis, "US-ASCII");
                    //System.out.println("waiting for reply...");
                    int c;
                    instr = new StringBuffer();
                    while ((c = isr.read()) != 0){
                        //System.out.print((char) c);
                        instr.append((char) c);
                    }
                    if (instr.equals(new StringBuffer("upload"))){
                        byte[] fileStream = upload(instr.toString());
                        OutputStream os = clientConnect.getOutputStream();
                        os.write(fileStream, 0, fileStream.length);
                        os.flush();
                    }
                }
                catch(Exception e){
                    System.out.println(e);
                }
            }
        }*/
        catch(Exception e){
            System.out.println(e);
        }
    }


   /* int disconnect(Socket con){
    	try{
        BufferedOutputStream bos = new BufferedOutputStream(con.getOutputStream());
        OutputStreamWriter osw = new OutputStreamWriter(bos, "US-ASCII");
        String s = "disconnect" + (char)3;
        System.out.println(s);
        osw.write(s);
        osw.flush();
        
    }
    	catch(Exception e){
    		System.out.print("in disconnect"+e);
    		
    	}
    	finally{
    		return JFrame.EXIT_ON_CLOSE;
    	}
      }*/
    public void run(){
        while(true){
            //@SuppressWarnings("unchecked")

            StringBuffer instr = new StringBuffer();
            try{
            	System.out.println(connection.getLocalPort());
            	System.out.println(connection.getPort());
                BufferedInputStream bis = new BufferedInputStream(connection.getInputStream());
                InputStreamReader isr = new InputStreamReader(bis, "US-ASCII");
                System.out.println("waiting for reply...");
                int c;
                instr = new StringBuffer();
                while ((c = isr.read()) != 0){
                    System.out.print((char) c);
                    instr.append((char) c);
                    ///System.out.print((char)c);
                }
                System.out.println();
                System.out.println("Server/upload->" + instr.toString());

                System.out.println(instr.toString());

                String server = "Server";

                System.out.println(instr.toString().indexOf(server));


                if(instr.toString().indexOf(server) == 0){
                    System.out.println("In server!!!");

                    fm = new HashMap<String, Set<String>>();
                    activenodes = new HashSet<String>();

                    //String[] temp = instr.toString().substring(8, instr.toString().indexOf('='));
                    String[] temp = instr.toString().split("=", 2);
                    /*for (int i = 0; i < temp.length; i++)
                        System.out.println(""+i+">"+temp[i]);*/

                    temp[0] = temp[0].substring(8);
                    //temp.replaceAll(",", "");
                    //temp.replaceAll();
                    System.out.println("!!!"+temp[0]);
                    String[] act = temp[0].split("/");
                    for(int i = 0; i < act.length; i++)
                        activenodes.add(act[i]);
                    System.out.println(activenodes);

                    String[] tempHash = temp[1].substring(1,temp[1].length() - 3).split("],");

                    String[][] keyValueHash = new String[20][2];
                    for(int i = 0; i < tempHash.length; i++){
                        //tempHash[i].replaceAll('[',"");
                        tempHash[i].replaceAll("/","");
                        keyValueHash[i][0] = tempHash[i].split("=")[0];
                        //System.out.println("+keyValueHash["+i+"][0]: "+keyValueHash[i][0]);
                        //System.out.println("tempHash["+i+"].split: "+tempHash[i].split("=")[1].replaceAll("/",""));
                        keyValueHash[i][1] = tempHash[i].split("=")[1];//.replaceAll("/","");
                        //System.out.println("keyValueHash["+"i][1]: "+keyValueHash[i][1]);
                        System.out.println(""+keyValueHash[i][0]+"==>"+keyValueHash[i][1]);
                        String[] ips = keyValueHash[i][1].substring(2).split(", /");
                        Set<String> s = new HashSet<String>();
                        for(int j = 0; j < ips.length; j++){
                            s.add(ips[j]);
                        }
                        fm.put(keyValueHash[i][0], s);
                    }
                    System.out.println(fm);


                    /*ObjectInputStream ois = new ObjectInputStream(connection.getInputStream());
                    InputStreamReader oisr = new InputStreamReader(ois);*/

                    /*BufferedInputStream bis1 = new BufferedInputStream(connection.getInputStream());
                    InputStreamReader isr1 = new InputStreamReader(bis1, "US-ASCII");
                    
                    //HashMap<String, HashMap<String, Set<String>>> fm = new HashMap<String, HashMap<String, Set<String>>>();
                    int w;
                    StringBuffer instr1 = new StringBuffer();
                    while((w = isr1.read()) != 0){
                    	instr1.append((char) w);
                    }
                    System.out.print(instr1.toString());*/
                }

                else if (instr.toString().indexOf("upload") == 0){
                    String fn = instr.toString().substring(6);
                    System.out.println("In else if");
                    byte[] fileStream = upload(fn);
                    OutputStream os = connection.getOutputStream();
                    os.write(fileStream, 0, fileStream.length);
                    os.flush();
                }

                else {
                    System.out.println("In else");
                }

                System.out.println("Outside if else-if else");


                if(frame != null){
                    frame.dispose();
                }

                frame = new JFrame("P2P");
                
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                treeview gui=new treeview(fm , activenodes);
                frame.add(gui);
                //Display the window.
                frame.pack();
                //frame.revalidate();
                frame.setVisible(true);


            }
            catch(Exception e){
                System.out.println("bhaago " + e);
            }
        }

    }

    clientmain(Socket s){
        clientmain.connection = s;
    }
}
class treeview extends JPanel implements TreeSelectionListener {

    private JTree tree;
    private JList label;
    public DefaultMutableTreeNode category = null;
    public DefaultMutableTreeNode book = null;


    // private static boolean DEBUG = false;

    private static boolean playWithLineStyle = false;
    private static String lineStyle = "Horizontal";

    private static boolean useSystemLookAndFeel = false;

    public static void download(String ipAddr, String fileName){
        int bytesRead;
        int current = 0;
        System.out.println("In Download");
        try{
            InetAddress hostClient = InetAddress.getByName(ipAddr);
            int clientPort = 20000;
            Socket getFileSocket = new Socket(hostClient, clientPort);
            System.out.println("created socket");

            BufferedOutputStream clientRequestOutputBuffer = new BufferedOutputStream(getFileSocket.getOutputStream());
            OutputStreamWriter clientRequestWriter = new OutputStreamWriter(clientRequestOutputBuffer, "US-ASCII");
            System.out.println("writer created");
            clientRequestWriter.write("upload" + fileName);
            clientRequestWriter.flush();
            System.out.println("request sent");

            //BufferedInputStream clientRequestInputBuffer = new BufferedInputStream(getFileSocket.getInputStream());
            //InputStreamReader clientRequestReader = new InputStreamReader(clientRequestInputBuffer, "US-ASCII");

            int size = 1024*1024*25;
            byte [] mybytearray  = new byte [size];
            InputStream is = getFileSocket.getInputStream();
            System.out.println("created byte array on request side");
            String s = "\\shared\\"+fileName;
            FileOutputStream fos = new FileOutputStream(fileName);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            bytesRead = is.read(mybytearray,0,mybytearray.length);
            current = bytesRead;

            do {
                bytesRead = is.read(mybytearray, current, (mybytearray.length-current));
                if(bytesRead >= 0) current += bytesRead;
            } while(bytesRead > -1);

            bos.write(mybytearray, 0, current);
            bos.flush();

            fos.close();
            bos.close();
            getFileSocket.close();
            System.out.println("DONE!!!");
            //System.out.println("File " + FILE_TO_RECEIVED + " downloaded (" + current + " bytes read)");

            /*String file = "/shared/"+fileName;
            File f = new File(file);
            f.createNewFile();
            FileWriter fw = new FileWriter(f.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);

            int c;
            while ((c = clientRequestReader.read()) != 0){
            }*/
        }
        catch(Exception e){
            System.out.println("in Download, exception occured = " + e);
        }
        /*finally {
            if (fos != null) fos.close();
            if (bos != null) bos.close();
            if (getFileSocket != null) getFileSocket.close();
        }*/
    }

    public treeview(HashMap<String, Set<String>> fm, Set<String> activenodes) {
        super(new GridLayout(1,0));



        DefaultMutableTreeNode top =
                new DefaultMutableTreeNode("The Available Files");
        createNodes(top, fm);

        tree = new JTree(top);
        //tree.setBackground(Color.CYAN);
        tree.getSelectionModel().setSelectionMode
                (TreeSelectionModel.SINGLE_TREE_SELECTION);

        //Listen for when the selection changes.
        tree.addTreeSelectionListener(this);

        if (playWithLineStyle) {
            System.out.println("line style = " + lineStyle);
            tree.putClientProperty("JTree.lineStyle", lineStyle);
        }


        label=new JList();

        String	listData[] = new String[20];

        int numIp = 0;

        for(Iterator<String> it = activenodes.iterator(); it.hasNext();numIp++){
            listData[numIp] = it.next();
        }

        label = new JList( listData );

        JScrollPane treeView = new JScrollPane(tree);
        JScrollPane listView = new JScrollPane(label);
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setLeftComponent(treeView);
        splitPane.setRightComponent(listView);

        Dimension ms = new Dimension(150, 50);
        listView.setMinimumSize(ms);
        treeView.setMinimumSize(ms);
        splitPane.setDividerLocation(250);
        splitPane.setPreferredSize(new Dimension(500, 300));

        //Add the split pane to this panel.
        add(splitPane);

        /*tree.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                doMouseClicked(me);
            }
        });*/

    }

    private void createNodes(DefaultMutableTreeNode top, HashMap<String, Set<String>> fm) {

        for(String key: fm.keySet()){
            category = new DefaultMutableTreeNode(key);
            top.add(category);

            for (String ip : fm.get(key)){
                book = new DefaultMutableTreeNode(ip);
                category.add(book);
            }
        }

        /*for(Iterator<String> it = fm.keySet().iterator(); it.hasNext();){
            String temp = it.next();
            category = new DefaultMutableTreeNode(temp);
            top.add(category);

            for(Iterator<String> jk = fm.get(temp).iterator(); it.hasNext();){
                book.add(new DefaultMutableTreeNode(it.next().toString()));
            }
        }*/
    }
        
   
    /*private static void createAndShowGUI() {
        if (useSystemLookAndFeel) {
            try {
                UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                System.err.println("Couldn't use system look and feel.");
            }
        }

        //Create and set up the window.
        JFrame frame = new JFrame("P2P");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Add content to the window.
        frame.add(new treeview());
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }*/

    /*public static void main(String[] args) {
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        /*javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });*/
    	/*if (useSystemLookAndFeel) {
            try {
                UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                System.err.println("Couldn't use system look and feel.");
            }
        }

        //Create and set up the window.
        JFrame frame = new JFrame("P2P");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Add content to the window.
        frame.add(new treeview());
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }*/
    public void valueChanged(TreeSelectionEvent arg0) {
        // TODO Auto-generated method stub
        String p = arg0.getPath().toString();
        p = p.substring(1,p.length()-1).replaceAll(" ","");
        //System.out.println(p);
        String[] path = p.split(",");
        /*System.out.println(path[0]);
        System.out.println(path[1]);
        System.out.println(path[2]);*/
        download(path[2], path[1]);
        //TreePath p = arg0.getPath();
        //System.out.println(p);


    }


}