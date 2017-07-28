package zan.voltey.gui;

import java.awt.Graphics2D;

import zan.util.IK;
import zan.voltey.game.Game;

public abstract class SelectionGUI {
	
	protected float offsetX, offsetY;
	
	protected int backGUI;
	
	protected int selection;
	protected int selnum;
	
	protected GUIButton[] btns;
	
	public SelectionGUI(int sn) {
		offsetX = 0f;
		offsetY = 0f;
		backGUI = 0;
		selection = 0;
		selnum = sn;
		btns = null;
	}
	public void init() {
		selection = 0;
		for(int i=0;i<btns.length;i++) btns[i].init();
	}
	
	public void setOffset(float sx, float sy) {
		offsetX = sx;
		offsetY = sy;
	}
	public void setBackGUI(int sg) {backGUI = sg;}
	
	public int getSelection() {return selection;}
	
	//MUST BE UPDATED
	public void setModifiedPlayer(int mp) {}
	
	protected void pollSelection() {
		Game.playSFX("Click");
	}
	protected void holdSelection() {}
	protected void keySelection() {}
	protected void displaySelection() {}
	
	public void update(long gameTicker) {
		if (Game.getKeyPress(IK.DOWN)) {
			selection++;
			Game.playSFX("Click");
		} else if (Game.getKeyPress(IK.UP)) {
			selection--;
			Game.playSFX("Click");
		}
		
		boolean mouseOver = false;
		if (btns != null) {
			for(int i=0;i<btns.length;i++) {
				if (btns[i].checkMouseOver(offsetX, offsetY)) {
					if (Game.isMouseMoving() && !Game.getMouseL()) {
						if (selection != i) {
							selection = i;
							Game.playSFX("Click");
						}
					}
					mouseOver = true;
				}
			}
		}
		if (mouseOver) Game.changeCursor(1);
		else Game.changeCursor(0);
		
		if (selection < 0) selection = selnum-1;
		if (selection >= selnum) selection = 0;
		
		if (btns != null) {
			keySelection();
			if (Game.getKeyPress(IK.ENTER) || Game.getKeyPress(IK.SPACE)) {
				pollSelection();
			} else if (btns[selection].getBtnType() == 0 && Game.getClickL() && btns[selection].checkMouseStay(offsetX, offsetY)) {
				pollSelection();
			} else if (btns[selection].getBtnType() == 1 && Game.getMouseL()) {
				holdSelection();
			}
			
			for(int i=0;i<btns.length;i++) btns[i].deselect();
			btns[selection].select();
			
			for(int i=0;i<btns.length;i++) btns[i].unclick();
			if (btns[selection].checkMouseStay(offsetX, offsetY) && Game.getMouseL()) {
				btns[selection].click();
			}
			
			for(int i=0;i<btns.length;i++) btns[i].update(gameTicker);
		}
		
		displaySelection();
	}
	
	public abstract void draw(Graphics2D g);
}