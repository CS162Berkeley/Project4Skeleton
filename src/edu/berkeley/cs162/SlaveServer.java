/**
 * Sample instantiation of the slave server
 * 
 * @author Mosharaf Chowdhury (http://www.mosharaf.com)
 *
 * Copyright (c) 2012, University of California at Berkeley
 * All rights reserved.
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *  * Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *  * Neither the name of University of California, Berkeley nor the
 *    names of its contributors may be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 *    
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 *  ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED. IN NO EVENT SHALL PRASHANTH MOHAN BE LIABLE FOR ANY
 *  DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *  (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *  LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package edu.berkeley.cs162;

import java.net.InetAddress;

public class SlaveServer {
	static String logPath = null;
	static TPCLog<String, String> tpcLog = null;	
	
	static KeyServer<String, String> keyServer = null;
	static SocketServer server = null;
	
	// 64-bit globally unique ID of this SlaveServer
	static long slaveID = -1;	
	// Name of the host Master/Coordinator Server is running on
	static String masterHostName = null;
	// Port which Master/Coordinator is listening to client requests
	static int masterPort = -1;
	// Port which Master/Coordinator is listening to for SlaveServers to register themselves
	static int registrationPort = -1;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		if (args.length != 4) {
			System.err.println("USAGE: SlaveServer <slaveID> <masterHostName> <masterPort> <registrationPort>");
			System.exit(1);
		}
		
		// Read Master info from command line
		slaveID = Long.parseLong(args[0]);
		masterHostName = args[1];
		masterPort = Integer.parseInt(args[2]);
		registrationPort = Integer.parseInt(args[3]);
		
		// Create TPCMasterHandler
		System.out.println("Binding SlaveServer:");
		keyServer = new KeyServer<String, String>(1000);
		server = new SocketServer(InetAddress.getLocalHost().getHostAddress());
		TPCMasterHandler<String, String> handler = new TPCMasterHandler<String, String>(keyServer);
		server.addHandler(handler);
		server.connect();
		System.out.println("Starting SlaveServer at " + server.getHostname() + ":" + server.getPort());
		// fix me not to block
		server.run();
		
		// Create TPCLog
		logPath = slaveID + "@" + server.getHostname();
		tpcLog = new TPCLog<String, String>(logPath, keyServer);
		
		// Load from disk and rebuild logs
		tpcLog.rebuildKeyServer();
		
		// Set log for TPCMasterHandler
		handler.setTPCLog(tpcLog);
		
		// Register with the Master
		// implement me
	}

}
