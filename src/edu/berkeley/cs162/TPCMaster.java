/**
 * Master for Two-Phase Commits
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

import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.Socket;
import java.util.TreeSet;

public class TPCMaster<K extends Serializable, V extends Serializable>  {
	
	private class SlaveInfo {
		private int slaveID = -1;
		private String hostName = null;
		private int port = -1;
		private KVClient<K, V> kvClient = null;
		private Socket kvSocket = null;

		/**
		 * 
		 * @param slaveInfo as "SlaveServerID@HostName:Port"
		 * @throws KVException
		 */
		public SlaveInfo(String slaveInfo) throws KVException {
			// implement me
		}
		
		public int getSlaveID() {
			return slaveID;
		}

		public KVClient<K, V> getKvClient() {
			return kvClient;
		}

		public Socket getKvSocket() {
			return kvSocket;
		}

		public void setKvSocket(Socket kvSocket) {
			this.kvSocket = kvSocket;
		}
	}
	
	private static final int TIMEOUT_MILLISECONDS = 5000;
	
	private KVCache<K, V> masterCache = new KVCache<K, V>(1000);
	
	private SocketServer regServer = null;
	
	private Long tpcOpId = 0L;
	
	/**
	 * Creates TPCMaster where SlaveServers actually register
	 * 
	 * @throws Exception
	 */
	public TPCMaster() throws Exception {
		// Create registration server
		regServer = new SocketServer(InetAddress.getLocalHost().getHostAddress(), 9090);
	}
	
	private String getNextTpcOpId() {
		tpcOpId++;
		return tpcOpId.toString();		
	}
	
	/**
	 * Start registration server in a separate thread
	 * 
	 * @throws IOException
	 */
	public void run() {
		// implement me
	}
	
	/**
	 * Find first replica location
	 * @param key
	 * @return
	 */
	private SlaveInfo findFirstReplica(K key) {
		// implement me
		return null;
	}
	
	/**
	 * Find the successor of firstReplica to put the second replica
	 * @param firstReplica
	 * @return
	 */
	private SlaveInfo findSuccessor(SlaveInfo firstReplica) {
		// implement me
		return null;
	}
	
	/**
	 * Synchronized method to perform TPC operations one after another. 
	 *  
	 * @param msg
	 * @param isPutReq
	 * @return True if the TPC operation has succeeded
	 */
	public synchronized boolean performTPCOperation(KVMessage msg, boolean isPutReq) {
		// implement me
		return false;
	}

	public V handleGet(KVMessage msg) {
		// implement me
		return null;
	}
}
