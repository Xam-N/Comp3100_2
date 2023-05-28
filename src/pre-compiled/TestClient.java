import java.io.*;
import java.net.*;

public class TestClient {
	public static void main(String[] args) {
		try {
			Socket s = new Socket("localhost", 50000);
			DataOutputStream dout = new DataOutputStream(s.getOutputStream());
			BufferedReader din = new BufferedReader(new InputStreamReader(s.getInputStream()));
			dout.write(("HELO\n").getBytes());
			dout.flush();
			din.readLine(); // should be OK
			dout.write(("AUTH me\n").getBytes());
			dout.flush();
			din.readLine(); // should be ok
			// Start
			dout.write(("REDY\n").getBytes());
			dout.flush();
			String temp = din.readLine(); // reads JOBN and ignores it //can optimise maybe
			dout.write(("GETS All\n").getBytes());
			dout.flush();
			Integer sData = Integer.parseInt(din.readLine().split(" ")[1]);// reads DATA
			String[][] servers = new String[sData][9];
			dout.write(("OK\n").getBytes());
			dout.flush();
			for (int i = 0; i < servers.length; i++) {
				servers[i] = din.readLine().split(" "); // reads Server Info
			}
			dout.write(("OK\n").getBytes());
			dout.flush();
			din.readLine(); // reads*/
			String[] job = new String[7]; // can optimise
			Integer bTime;
			Integer sTime;
			Integer sCore;
			Integer sMem;
			Integer sDisk;
			Integer jID;
			Integer jTime;
			Integer jCore;
			Integer jMem;
			Integer jDisk;
			Integer bNum;
			Integer sRun;
			String sID = "";
			String sType = "";
			while (true) {
				if (temp.contains("NONE")) {
					System.out.println("NONE");
					break;
				}
				else if (temp.contains("JCPL")) {
				} 
				else if (temp.contains("JOBN")) {
					dout.write(("GETS All\n").getBytes());
					dout.flush();
					String test = din.readLine();
					System.out.println(test + "Not Data??");
					sData = Integer.parseInt(test.split(" ")[1]);// reads DATA
					dout.write(("OK\n").getBytes());
					dout.flush();
					String timeTemp;
					String ok;
					for (int i = 0; i < servers.length; i++) {
						//System.out.println("Before");
						timeTemp = servers[i][3];
						ok = din.readLine();
						System.out.println(ok + " This should be a server");
						servers[i] = ok.split(" "); // reads Server Info
						servers[i][3] = timeTemp;
						//System.out.println("After");
					}
					dout.write(("OK\n").getBytes());
					dout.flush();
					din.readLine();
					//System.out.println(din.readLine() + " = .");	

					job = temp.split(" ");
					//System.out.println(temp + " This is Temp");
					jID = Integer.parseInt(job[2]);
					jTime = Integer.parseInt(job[3]);
					jCore = Integer.parseInt(job[4]);
					jMem = Integer.parseInt(job[5]);
					jDisk = Integer.parseInt(job[6]);
					bTime = 0; // holds shortest wait time
					bNum = -1; // holds chosen server
					for (int i = 0; i < servers.length; i++) {
						sCore = Integer.parseInt(servers[i][4]);
						sMem = Integer.parseInt(servers[i][5]);
						sDisk = Integer.parseInt(servers[i][6]);
						sRun = Integer.parseInt(servers[i][7]);
						sTime = Integer.parseInt(servers[i][3]);
						if (jCore <= sCore && jMem <= sMem && jDisk <= sDisk) {
							if (sTime == 0) {
								bNum = i;
								break;
							}
							/*if (sRun>0){
								bNum = i;
								break;
							}*/
							if (bTime == 0) {
								bTime = sTime;
								bNum = i;
							} else if (bTime > sTime) {
								bNum = i;
								bTime = sTime;
							}
						}
					}
					if (bNum == -1) {
						System.out.println("bNum is -1");
						dout.write(("GETS Capable " + Integer.toString(jCore) + " " + Integer.toString(jMem) + " "
								+ Integer.toString(jDisk) + "\n").getBytes());
						dout.flush();
						sData = Integer.parseInt(din.readLine().split(" ")[1]);
						dout.write(("OK\n").getBytes());
						dout.flush();
						String[] serverTemp2;
						Integer lowJobs = 999;
						for (int i = 0; i < sData; i++) {
							serverTemp2 = din.readLine().split(" ");
							if (Integer.parseInt(serverTemp2[7]) < lowJobs) {
								lowJobs = Integer.parseInt(serverTemp2[7]);
								sType = serverTemp2[0];
								sID = serverTemp2[1];
							}
						}
						System.out.println("Hello");
						dout.write(("OK\n").getBytes());
						dout.flush();
						System.out.println(din.readLine()+" ????");
						dout.write(("SCHD " + jID + " " + sType + " " + sID + "\n").getBytes());
						dout.flush();
						for(int i = 0;i<servers.length;i++){
							if(servers[i][0]==sType&&servers[i][1]==sID){
								servers[i][3] = Integer.toString(Integer.parseInt(servers[i][3]) + jTime);
							}
						}
						System.out.println(din.readLine() + " = OK"); // reads OK
					} else {
						dout.write(("SCHD " + jID + " " + sType + " " + sID + "\n").getBytes());
						dout.flush();
						servers[bNum][3] = Integer.toString(Integer.parseInt(servers[bNum][3]) + jTime);
						System.out.println(din.readLine()); // reads OK
						sID = servers[bNum][1];
						sType = servers[bNum][0];
					}

				}
				dout.write(("REDY\n").getBytes());
				dout.flush();
				temp = din.readLine();
				System.out.println("Temp at the end of the loop " +temp);
			}
			dout.write(("QUIT\n").getBytes());
			din.readLine();
			din.close();
			dout.close();
			s.close();
		} catch (

		Exception e) {
			System.out.println(e);
		}
	}
}