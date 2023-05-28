import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;

public class MyClient {
	public static void main(String[] args) {
		try {
			Socket s = new Socket("localhost", 50000);
			DataOutputStream dout = new DataOutputStream(s.getOutputStream());
			BufferedReader din = new BufferedReader(new InputStreamReader(s.getInputStream()));
			dout.write(("HELO\n").getBytes());
			dout.flush();	
			if (din.readLine().contains("OK")) {
			} else {
				System.out.println("?");
				din.readLine();
			}
			dout.write(("AUTH me\n").getBytes());
			dout.flush();
			if (din.readLine().contains("OK")) {
			} else {
				System.out.println("??");
				din.readLine();
			}
			// Start
			int jobCount = 0;
			while (true) {
				dout.write(("REDY\n").getBytes());
				dout.flush();
				String temp = din.readLine();//should be JOBN or CHKQ, I dont think can be NONE
				if(temp.contains("CHKQ")){
					break;
				}
				else if(temp.contains("JOBN")){
					jobCount++;
					dout.write(("ENQJ GQ\n").getBytes());						dout.flush();
					temp = din.readLine(); //should be OK
				}
				else{
					System.out.println(temp);
				}		
			}
			System.out.println("Job Count is " +jobCount);
			dout.write(("LSTQ GQ *\n").getBytes());
			dout.flush();
			int jData = Integer.parseInt(din.readLine().split(" ")[1]); //should be DATA # # 
			String[] jobList = new String[jData];
			dout.write(("OK\n").getBytes());
			dout.flush();
			String jTemp = "";
			for(int i = 0; i<jData;i++){
				jTemp = din.readLine();
				jobList[i] = jTemp;
			}
			dout.write(("OK\n").getBytes());
			din.readLine();
			/*for(int i = 0; i<jData;i++){
				System.out.println("\n");
				for(int j = 0; j<8;j++){
					System.out.print(jobList[i][j]);
				}
			}*/

			Arrays.sort(jobList, (a, b) -> Integer.compare(Integer.parseInt(a.split(" ")[4]), Integer.parseInt(b.split(" ")[4])));		

			/*for(int i = 0; i<jData;i++){
				for(int j = 0; j<8;j++){
					System.out.println(jobList[i][j]);
				}
			}*/
			ArrayList<String> waitingJobs = new ArrayList<String>();

			for(int i=0;i<jobList.length;i++){
				waitingJobs.add(jobList[i]);
			}
			for(int i=0;i<waitingJobs.size();i++){
				dout.write(("DEQJ GQ 0\n").getBytes());
				dout.flush();
				System.out.println(din.readLine());
			}
			for (int i = 0; i < waitingJobs.size(); i++) {
				dout.write(("GETS Capable "+waitingJobs.get(i).split(" ")[5]+" "+waitingJobs.get(i).split(" ")[6]+ " "+waitingJobs.get(i).split(" ")[7]+"\n").getBytes());
				dout.flush();
				String dTemp = din.readLine();//should equal data
				System.out.println(dTemp);
				dout.write(("OK\n").getBytes());
                                dout.flush();
                                String server = din.readLine();
				System.out.println(server);
				String temp = "";
				int tJob1 = 0;
				int tJob2 = 0;
				if(Integer.parseInt(server.split(" ")[7])!=0){
					for(int j=1;j<Integer.parseInt(dTemp.split(" ")[1]);j++){
						temp = din.readLine();
						System.out.println("Reading "+temp);
						tJob1 = Integer.parseInt(server.split(" ")[7]);
						tJob2 = Integer.parseInt(temp.split(" ")[7]);
						if(tJob1 > tJob2){
							server = temp;
						}
					}
				}
				else{
					int counter = 1;
					while(counter<Integer.parseInt(dTemp.split(" ")[1])){
				din.readLine();
				counter++;
				}
				}

				System.out.println("3 "+server);
					dout.write(("OK\n").getBytes());
					dout.flush();
					System.out.println("5 . = "+din.readLine()); // should be "."
					dout.write(("SCHD " +waitingJobs.get(i).split(" ")[0]+ " "+ server.split(" ")[0] + " " + server.split(" ")[1] + "\n").getBytes());
					System.out.println(waitingJobs.get(i).split(" ")[2]);
					dout.flush();
					temp = din.readLine();
					System.out.println("6 "+temp);
					if(temp.contains("OK")){
						waitingJobs.remove(i);
						System.out.println("Schedule Successful! "+temp);
						i--;
					}
					else if(temp.contains("ERR")){

						break;
					}
					else{
					System.out.println("Ru Roh "+temp);
					}
				
				}
			// end
			// Start of old stuff
			/*
			 * dout.write(("REDY\n").getBytes());
			 * dout.flush();
			 * String[] job = new String[7];
			 * hold = din.readLine();
			 * dout.write(("GETS All\n").getBytes());
			 * dout.flush();
			 * String[] data = ((String)din.readLine()).split(" ");
			 * String[] temp = new String[(Integer.parseInt(data[1]))];
			 * dout.write(("OK\n").getBytes());
			 * dout.flush();
			 * for (int i = 0; i<temp.length;i++){
			 * temp[i] = (String)din.readLine();
			 * }
			 * int mc = 0;
			 * for(int i=0;i<temp.length;i++){
			 * if(mc<Integer.parseInt(temp[i].split(" ")[4])){
			 * mc = Integer.parseInt(temp[i].split(" ")[4]);
			 * }
			 * }
			 * ArrayList<String> server = new ArrayList<String>();
			 * String serverType = "";
			 * for(int i=0;i<temp.length;i++){
			 * if(mc==Integer.parseInt(temp[i].split(" ")[4])){
			 * serverType = temp[i].split(" ")[0];
			 * break;
			 * }
			 * }
			 * for(int i = 0;i<temp.length;i++){
			 * if(serverType.equals(temp[i].split(" ")[0])){
			 * server.add(temp[i]);
			 * }
			 * }
			 * dout.write(("OK\n").getBytes());
			 * dout.flush();
			 * String kkkk = "";
			 * kkkk = din.readLine();
			 * if(kkkk.contains(".")){
			 * }
			 * else{
			 * System.out.println("???"+kkkk);
			 * din.readLine();
			 * }
			 * int serverCount = 0;
			 * String kk = "";
			 * while(true){
			 * if((hold.contains("JOBN"))){
			 * job = hold.split(" ");
			 * dout.write(("SCHD "+job[2]+" "+serverType+" "+
			 * server.get(serverCount).split(" ")[1] + "\n").getBytes());
			 * kk = din.readLine();
			 * if(kk.contains("OK")){
			 * }
			 * else{
			 * System.out.println("Broken? ");
			 * System.out.println(kk);
			 * System.out.println(hold);
			 * break;
			 * }
			 * if(serverCount<server.size()-1){
			 * serverCount++;
			 * }
			 * else{
			 * serverCount = 0;
			 * }
			 * }
			 * else if(hold.contains("ERR")){
			 * break;
			 * }
			 * else if(hold.contains("JCPL")){
			 * }
			 * else if(hold.contains("CHKQ")){
			 * }
			 * else if(hold.contains("NONE")){
			 * break;
			 * }
			 * dout.write(("REDY\n").getBytes());
			 * dout.flush();
			 * hold = din.readLine();
			 * }
			 */
			dout.write(("QUIT\n").getBytes());
			String temp = din.readLine();
			if (temp.contains("QUIT")) {
				din.close();
				dout.close();
				s.close();
			} else {
				System.out.println("????" + temp);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
