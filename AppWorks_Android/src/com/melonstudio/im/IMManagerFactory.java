package com.melonstudio.im;

/**
 * 
 * @author Wo
 * 
 */
public class IMManagerFactory {
	static SmackManager smackManager = null;

	public synchronized static IMManager newSmackManager() {
		if (smackManager == null) {
			smackManager = new SmackManager();
		}
		return smackManager;
	}
}
