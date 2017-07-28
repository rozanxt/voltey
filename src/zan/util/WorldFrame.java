package zan.util;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import zan.voltey.gui.SelectionGUI;
import zan.voltey.game.Game;

public abstract class WorldFrame {
	
	protected Game gmain;
	
	protected SelectionGUI[] sGUI;
	protected SelectionGUI scrGUI;
	
	public WorldFrame(Game gm) {
		gmain = gm;
		
		sGUI = null;
		scrGUI = null;
	}
	
	public void toggleFullScreen() {
		gmain.toggleFullScreen();
	}
	
	public void setTitleFrame() {
		gmain.setTitleFrame();
	}
	
	public void changeGUI(int sg) {
		if (sGUI == null) return;
		if (sg == -1) scrGUI = null;
		else if (sg >= 0 && sg < sGUI.length) {
			sGUI[sg].init();
			scrGUI = sGUI[sg];
		}
	}
	
	public ArrayList<BufferedImage> getImages(String s) {
		return gmain.getImages(s);
	}
	
	public abstract void initWorld();
	
	public abstract void updateWorld(long gameTicker);
	
	public abstract void drawWorld(Graphics2D g);
}
