import java.io.*;
import java.net.*;

public class TestClient {
	public static void write(DataOutputStream dout, String message) {
		try {
			dout.write((message + "\n").getBytes());
			dout.flush();
		} catch (

		Exception e) {
			System.out.println(e);
		}

	}

	public static void main(String[] args) {
		try {
			Socket s = new Socket("localhost", 50000);
			DataOutputStream dout = new DataOutputStream(s.getOutputStream());
			BufferedReader din = new BufferedReader(new InputStreamReader(s.getInputStream()));
			write(dout,"HELO");
			din.readLine(); // should be OK
			write(dout,"AUTH me");
			din.readLine(); // should be ok
			// Start
			write(dout,"REDY");
			String temp = din.readLine(); // reads JOBN and ignores it //can optimise maybe
			write(dout,"GETS All");
			Integer sData = Integer.parseInt(din.readLine().split(" ")[1]);// reads DATA
			String[][] servers = new String[sData][9];
			write(dout,"OK");
			for (int i = 0; i < servers.length; i++) {
				servers[i] = din.readLine().split(" "); // reads Server Info
			}
			write(dout,"OK");
			din.readLine(); // reads . 
			String[] job = new String[7];
			Integer bTime;//Best time
			Integer bNum;//Best server number
			String sID = "";//ServerID
			String sType = "";//ServerType
			Integer sTime;//Server Time
			Integer sCore;//Server Core
			Integer sMem;//Server Memory
			Integer sDisk;//Server Disk
			Integer jID;//Job ID
			Integer jTime;//Job Time
			Integer jCore;//Job Core
			Integer jMem;//Job Memory
			Integer jDisk;//Job Disk
			while (true) {
				if (temp.contains("NONE")) {//End program
					System.out.println("NONE");
					break;
				} else if (temp.contains("JCPL")) {//skip JOBN and restart the loop
				} else if (temp.contains("JOBN")) {
					job = temp.split(" ");
					jID = Integer.parseInt(job[2]);
					jTime = Integer.parseInt(job[3]);
					jCore = Integer.parseInt(job[4]);
					jMem = Integer.parseInt(job[5]);
					jDisk = Integer.parseInt(job[6]);
					write(dout,"GETS All");
					sData = Integer.parseInt(din.readLine().split(" ")[1]);// reads DATA
					write(dout,"OK");
					String timeTemp;
					String serverTemp;
					for (int i = 0; i < servers.length; i++) {
						timeTemp = servers[i][3];//hold current estimated job time 
						serverTemp = din.readLine();
						servers[i] = serverTemp.split(" "); // reads Server Info
						servers[i][3] = timeTemp;//replaces current estimated job time
					}
					write(dout,"OK");
					din.readLine();//reads . 

					bTime = 0; // holds best/shortest wait time
					bNum = -1; // holds best/chosen server
					for (int i = 0; i < servers.length; i++) {
						sCore = Integer.parseInt(servers[i][4]);
						sMem = Integer.parseInt(servers[i][5]);
						sDisk = Integer.parseInt(servers[i][6]);
						sTime = Integer.parseInt(servers[i][3]);
						if (jCore <= sCore && jMem <= sMem && jDisk <= sDisk) {
							if (sTime == 0) {
								bNum = i;
								break;
							}
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
						write(dout,"GETS Capable " + Integer.toString(jCore) + " " + Integer.toString(jMem) + " "
						+ Integer.toString(jDisk));
						sData = Integer.parseInt(din.readLine().split(" ")[1]);//Reads number of servers about to be sent
						write(dout,"OK");
						String[] serverTemp2;
						Integer lowJobs = 999;
						for (int i = 0; i < sData; i++) {//reads server info
							serverTemp2 = din.readLine().split(" ");
							if (Integer.parseInt(serverTemp2[7]) < lowJobs) {
								lowJobs = Integer.parseInt(serverTemp2[7]);
								sType = serverTemp2[0];
								sID = serverTemp2[1];
							}
						}
						// System.out.println("Hello");
						write(dout,"OK");
						din.readLine();//Reads .
						write(dout,"SCHD " + jID + " " + sType + " " + sID);
						for (int i = 0; i < servers.length; i++) {//Add time to server list 
							if (servers[i][0] == sType && servers[i][1] == sID) {
								servers[i][3] = Integer.toString(Integer.parseInt(servers[i][3]) + jTime);
							}
						}
						din.readLine();//Reads OK
					} else {
						write(dout,"SCHD " + jID + " " + sType + " " + sID);
						din.readLine();//Reads OK
						servers[bNum][3] = Integer.toString(Integer.parseInt(servers[bNum][3]) + jTime);//Changing the job timings
						sID = servers[bNum][1];
						sType = servers[bNum][0];
					}

				}
				write(dout,"REDY");
				temp = din.readLine();//Restarts the loop with temp being the holder
			}
			write(dout,"QUIT");//Quitting
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