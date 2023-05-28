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
			din.readLine(); // reads JOBN and ignores it //can optimise maybe
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
			din.readLine(); // reads .
			String[] job = new String[7]; // can optimise
			String temp;
			Integer bTime;
			Integer sTime;;
			Integer sCore;
			Integer sMem;
			Integer sDisk;
			Integer jID;
			Integer jRun;
			Integer jCore;
			Integer jMem;
			Integer jDisk;
			Integer bNum;
			while (true) {
				dout.write(("REDY\n").getBytes());
				dout.flush();
				temp = din.readLine();// reads JOBN
				if (temp.contains("NONE")) {
					break;
				}
				if (temp.contains("JOBN")) {
					job = temp.split(" ");
					jID = Integer.parseInt(job[2]);
					jRun = Integer.parseInt(job[3]);
					jCore = Integer.parseInt(job[4]);
					jMem = Integer.parseInt(job[5]);
					jDisk = Integer.parseInt(job[6]);
					bTime = 0; // holds shortest wait time
					bNum = 0; //holds chosen server
					for (int i = 0; i < servers.length; i++) {
						sCore = Integer.parseInt(servers[i][4]);
						sMem = Integer.parseInt(servers[i][5]);
						sDisk = Integer.parseInt(servers[i][6]);
						sTime = Integer.parseInt(servers[i][8]);
						if (jCore <= sCore && jMem <= sMem && jDisk <= sDisk) {
							if (sTime==0) {
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
					dout.write(("SCHD " + jID + " " + servers[bNum][0] + " " + servers[bNum][1] + "\n").getBytes());
					dout.flush();
					servers[bNum][8] = Integer.toString(jRun + Integer.parseInt(servers[bNum][8]));
					din.readLine(); // reads OK
				}
			}
			dout.write(("QUIT\n").getBytes());
			temp = din.readLine();
			if (temp.contains("QUIT")) {
				din.close();
				dout.close();
				s.close();
			} else {
				System.out.println("????" + temp);
			}
		} 
		catch (Exception e) {
			System.out.println(e);
		}
	}
}