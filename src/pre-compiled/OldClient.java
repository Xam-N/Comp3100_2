import java.io.*;  
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
public class OldClient {  
    public static void main(String[] args) {  
        try{      
    		Socket s=new Socket("localhost",50000);  
    		DataOutputStream dout=new DataOutputStream(s.getOutputStream());
      		BufferedReader din = new BufferedReader(new InputStreamReader(s.getInputStream()));	
    		dout.write(("HELO\n").getBytes());
		//System.out.println("helo");
      		dout.flush();
		String hold = "";
		if(din.readLine().contains("OK")){
		}
		else{
			System.out.println("?");
			din.readLine();
		}
		dout.write(("AUTH me\n").getBytes());
		//System.out.println("auth username\n");
                dout.flush();
		if(din.readLine().contains("OK")){
		}
		else{
			System.out.println("??");
			din.readLine();
		}
		dout.write(("REDY\n").getBytes());
		dout.flush();
		String[] job = new String[7];
		hold = din.readLine();
		dout.write(("GETS All\n").getBytes());
		//System.out.println("gets all\n");
		dout.flush();
		String[] data = ((String)din.readLine()).split(" ");
		//System.out.println(data[1]);
		String[] temp = new String[(Integer.parseInt(data[1]))];
		dout.write(("OK\n").getBytes());
		//System.out.println("ok");
		dout.flush();
		for (int i = 0; i<temp.length;i++){
			temp[i] = (String)din.readLine();
		}
		int mc = 0;
		for(int i=0;i<temp.length;i++){
			if(mc<Integer.parseInt(temp[i].split(" ")[4])){
				mc = Integer.parseInt(temp[i].split(" ")[4]);
			}
		}
		ArrayList<String> server = new ArrayList<String>();
		String serverType = "";
		//System.out.println("Here 1");
		for(int i=0;i<temp.length;i++){
			if(mc==Integer.parseInt(temp[i].split(" ")[4])){
				serverType = temp[i].split(" ")[0];
				break;
				//server.add(temp[i]);
			}
		}
		//System.out.println(serverType);
		//System.out.println("Here 2");
		for(int i = 0;i<temp.length;i++){
			if(serverType.equals(temp[i].split(" ")[0])){
				server.add(temp[i]);
				//System.out.println("Adding");
			}
			//System.out.println("OK"+serverType+"OK");
			//System.out.println("OK"+temp[i].split(" ")[0]+"OK");
		}
		//System.out.println("Here 3");
		dout.write(("OK\n").getBytes());
		dout.flush();
		String kkkk = "";
		kkkk = din.readLine();
		if(kkkk.contains(".")){
		}
		else{
			System.out.println("???"+kkkk);
			din.readLine();
		}
		//String hold = "";	
		int serverCount = 0;
		String kk = "";
		while(true){
			//System.out.println("Iteration" + counter);
			//dout.write(("REDY\n").getBytes());
			//dout.flush();
			//hold = din.readLine();
			//System.out.println("The current line is: "+hold);
			if((hold.contains("JOBN"))){
				job = hold.split(" ");
				dout.write(("SCHD "+job[2]+" "+serverType+" "+ server.get(serverCount).split(" ")[1] + "\n").getBytes());
				kk = din.readLine();
				if(kk.contains("OK")){
					//System.out.println("Read OK");
				}
				else{
					System.out.println("Broken? ");
					System.out.println(kk);
					System.out.println(hold);
					break;
				}
				if(serverCount<server.size()-1){
					serverCount++;
				}
				else{
					serverCount = 0;
				}
			}
			else if(hold.contains("ERR")){
				break;
			}
			else if(hold.contains("JCPL")){
			}
			else if(hold.contains("CHKQ")){
			}
			else if(hold.contains("NONE")){
				break;
			}
			dout.write(("REDY\n").getBytes());
                        dout.flush();
                        hold = din.readLine();
		}
		dout.write(("QUIT\n").getBytes());
		kk = din.readLine();
		if(kk.contains("QUIT")){
			din.close();
			dout.close();
			s.close();
		}
		else{
			System.out.println("????"+kk);
		}
	}
	catch(Exception e){System.out.println(e);}  
    }  
}  
