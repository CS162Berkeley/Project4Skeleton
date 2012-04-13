/**
 * 
 * XML Parsing library for the key-value store
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.xml.bind.DatatypeConverter;


/**
 * This is the object that is used to generate messages the XML based messages 
 * for communication between clients and servers. Data is stored in a 
 * marshalled String format in this object.
 */
public class KVMessage implements Serializable {
	private static final long serialVersionUID = 6473128480951955693L;

	private String msgType = null;
	private String key = null;
	private String value = null;
	private String status = null;
	private String message = null;
	private String tpcOpId = null;

	public KVMessage(String msgType) {
		this.msgType = msgType;
	}
	
	public KVMessage(String msgType, String message) {
		this.msgType = msgType;
		this.message = message;
	}
	
	public KVMessage(KVMessage kvm) {
		this.msgType = kvm.msgType;
		this.key = kvm.key;
		this.value = kvm.value;
		this.status = kvm.status;
		this.message = kvm.message;
		this.tpcOpId = kvm.tpcOpId;
	}

	public KVMessage(String msgType, String key, String value) {
		this.msgType = msgType;
		this.key = key;
		this.value = value;
	}
	
	/* Hack for ensuring XML libraries does not close input stream by default.
	 * Solution from http://weblogs.java.net/blog/kohsuke/archive/2005/07/socket_xml_pitf.html */
	private class NoCloseInputStream extends FilterInputStream {
	    public NoCloseInputStream(InputStream in) {
	        super(in);
	    }
	    
	    public void close() {} // ignore close
	}
	
	public KVMessage(InputStream input) throws KVException {
		// implement me	
	}

	
	/**
	 * Generate the XML representation for this message.
	 * @return the XML String
	 */
	public String toXML() {
		// implement me
		return null;
	}
	
	/**
	 * Encode Object to base64 String 
	 * @param obj
	 * @return
	 */
	public static String encodeObject(Object obj) throws KVException {
        String encoded = null;
        try{
            ByteArrayOutputStream bs = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(bs);
            os.writeObject(obj);
            byte [] bytes = bs.toByteArray();
            encoded = DatatypeConverter.printBase64Binary(bytes);
            bs.close();
            os.close();
        } catch(IOException e) {
            throw new KVException(new KVMessage("resp", "Unknown Error: Error serializing object"));
        }
        return encoded;
	}

	/**
	 * Decode base64 String to Object
	 * @param str
	 * @return
	 */
	public static Object decodeObject(String str) throws KVException {
		Object obj = null;
		try{
	        byte[] decoded = DatatypeConverter.parseBase64Binary(str);
	        ObjectInputStream is = new ObjectInputStream(new ByteArrayInputStream(decoded));
	        obj = is.readObject();
	        is.close();
		} catch(IOException e) {
	        throw new KVException(new KVMessage("resp", "Unknown Error: Unable to decode object"));
		}
		catch (ClassNotFoundException e) {
	        throw new KVException(new KVMessage("resp", "Unknown Error: Decoding object class not found"));
		}
		return obj;
	}
}
