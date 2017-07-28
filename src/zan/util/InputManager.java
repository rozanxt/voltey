package zan.util;

public class InputManager {
	
	private boolean inputKey[];
	private boolean inputBuffer[];
	private int inputBump[];
	private int inputPress[];
	private boolean inputLock;
	
	private int mx, my;
	private int fx, fy;
	private boolean mleft, mright;
	private int cleft, cright;
	private int mmoving;
	private boolean fixed;
	
	public InputManager() {
		inputKey = new boolean[IK.values().length];
		for (int i=0;i<inputKey.length;i++) inputKey[i] = false;
		inputBuffer = new boolean[IK.values().length];
		for (int i=0;i<inputBuffer.length;i++) inputBuffer[i] = false;
		inputBump = new int[IK.values().length];
		for (int i=0;i<inputBump.length;i++) inputBump[i] = 0;
		inputPress = new int[IK.values().length];
		for (int i=0;i<inputPress.length;i++) inputPress[i] = 0;
		inputLock = false;
		mx = 0; my = 0; fx = 0; fy = 0;
		mleft = false; mright = false;
		cleft = 0; cright = 0;
		mmoving = 0;
		fixed = false;
	}
	
	public void updateInput() {
		for (int i=0;i<inputPress.length;i++) if (inputPress[i] > 0) inputPress[i]--;
		for (int i=0;i<inputBump.length;i++) if (inputBump[i] > 0) inputBump[i]--;
		
		if (cleft > 0) cleft--;
		if (cright > 0) cright--;
		if (mmoving > 0) mmoving--;
	}
	
	public void lockInput() {inputLock = true;}
	public void unlockInput() {inputLock = false;}
	public boolean isInputLocked() {return inputLock;}
	
	public void setKey(IK k, boolean s) {
		inputKey[k.ordinal()] = s;
		if (!s) {
			inputBuffer[k.ordinal()] = false;
			inputBump[k.ordinal()] = 5;
		} else if (!inputBuffer[k.ordinal()]) {
			inputBuffer[k.ordinal()] = true;
			inputPress[k.ordinal()] = 5;
		}
	}
	public boolean getKey(IK k) {
		if (inputLock) return false;
		return inputKey[k.ordinal()];
	}
	public boolean getKeyBump(IK k) {
		if (inputLock) return false;
		if (inputBump[k.ordinal()] > 0) {
			inputBump[k.ordinal()] = 1;
			return true;
		}
		return false;
	}
	public boolean getKeyPress(IK k) {
		if (inputLock) return false;
		if (inputPress[k.ordinal()] > 0) {
			inputPress[k.ordinal()] = 1;
			return true;
		}
		return false;
	}
	
	public void setMousePos(int sx, int sy) {
		mx = sx;
		my = sy;
		mmoving = 2;
	}
	public int getMouseX() {return mx;}
	public int getMouseY() {return my;}
	
	public void setMouseL(boolean s) {
		mleft = s;
		if (!s) cleft = 5;
	}
	public void setMouseR(boolean s) {
		mright = s;
		if (!s) cright = 5;
	}
	public boolean getMouseL() {
		if (inputLock) return false;
		return mleft;
	}
	public boolean getMouseR() {
		if (inputLock) return false;
		return mright;
	}
	public boolean getClickL() {
		if (inputLock) return false;
		if (cleft > 0) {
			cleft = 1;
			return true;
		}
		return false;
	}
	public boolean getClickR() {
		if (inputLock) return false;
		if (cright > 0) {
			cright = 1;
			return true;
		}
		return false;
	}
	
	public boolean isMouseMoving() {
		if (inputLock) return false;
		if (mmoving > 0) return true;
		return false;
	}
	
	public void setFixedPos(int sx, int sy) {
		if (!fixed) {
			fx = sx;
			fy = sy;
		}
		fixed = true;
	}
	public void releaseFixedPos() {fixed = false;}
	public int getFixedX() {return fx;}
	public int getFixedY() {return fy;}
	
	/*
	public static boolean mouseInRect(Rectangle s) {
		if (s.contains(fx, fy) && s.contains(mx, my)) return true;
		return false;
	}*/
}
