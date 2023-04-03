import java.io.*;
import java.net.*;
public class MyServer{
    public static void main(String[] args) {
        String test1 = "Helo";
        String test2 = "BYE";
        try{
		ServerSocket ss = new ServerSocket(6666);
		Socket s = ss.accept();
                DataOutputStream dout=new DataOutputStream(s.getOutputStream());
		DataInputStream din = new DataInputStream(s.getInputStream());
        	String str = (String)din.readUTF();
	        //if((str == test1)){
        	        dout.writeUTF("G'DAY");
			System.out.println("G'DAY");
                	dout.flush();
         	/*}		
		else{
			System.out.println("Fuck");
			s.close();
		}*/
	        String str2 = (String)din.readUTF();
        	//if(str2 == test2){
			dout.writeUTF("BYE");
			System.out.println("BYE");
			dout.flush();
			dout.close();
                	din.close();
                	s.close();
        	/*}
        	else{
                	System.out.println("Fuck");
			s.close();
        }*/}catch(Exception e){System.out.println(e);}
    }
}

