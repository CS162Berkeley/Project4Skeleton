/**
 * Persistent Key-Value storage layer. Current implementation is transient, 
 * but assume to be backed on disk when you do your project.
 * 
 * @author Prashanth Mohan (http://www.cs.berkeley.edu/~prmohan)
 * 
 * Copyright (c) 2011, University of California at Berkeley
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

import java.io.Serializable;
import java.util.Dictionary;
import java.util.Hashtable;
/**
 * This is a dummy KeyValue Store. Ideally this would go to disk, 
 * or some other backing store. For this project, we simulate the disk like 
 * system using a manual delay.
 *
 * @param <K> Java Generic type for the Key
 * @param <V> Java Generic type for the Value
 */
public class KVStore<K extends Serializable, V extends Serializable> implements KeyValueInterface<K, V> {
	Dictionary<K, V> store = null;
	private static long SLEEP_TIME = 1000;
	
	public KVStore() {
		store = new Hashtable<K, V>();
	}
	
	@Override
	public boolean put(K key, V value) throws KVException {
		putDelay();
		store.put(key, value);
		return false;
	}
	@Override
	public V get(K key) throws KVException {
		getDelay();
		return this.store.get(key);
	}
	@Override
	public void del(K key) throws KVException {
		delDelay();
		if(key!=null)
			this.store.remove(key);
	}
	
	private void delay() {
		try {
			Thread.sleep(SLEEP_TIME);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void getDelay() {
		delay();
	}
	
	private void putDelay() {
		delay();
	}
	
	private void delDelay() {
		delay();
	}
}
