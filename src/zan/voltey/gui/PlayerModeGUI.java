package zan.voltey.gui;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.font.TextAttribute;
import java.text.AttributedString;

import zan.util.IK;
import zan.util.Sprite;
import zan.voltey.game.Game;
import zan.voltey.game.GameTitle;

public class PlayerModeGUI extends SelectionGUI {
	
	private int modifiedPlayer;
	
	private GameTitle wFrame;
	
	public PlayerModeGUI(GameTitle wf) {
		super(3);
		wFrame = wf;
		
		setOffset(300f, 320f);
		
		btns = new GUIButton[selnum];
		btns[0] = new SelButton();
		btns[1] = new GaugeButton();
		btns[2] = new SelButton();
		btns[0].setSprites(new Sprite[]{new Sprite(wFrame.getImages("gui/selbutton"))});
		btns[1].setSprites(new Sprite[]{new Sprite(wFrame.getImages("gui/selcolor")), new Sprite(wFrame.getImages("gui/gaugebar"))});
		btns[2].setSprites(new Sprite[]{new Sprite(wFrame.getImages("gui/selbutton"))});
		btns[0].setPos(0f, 0f);
		btns[1].setPos(0f, 40f);
		btns[2].setPos(0f, 80f);
		btns[0].setStringText("Type: ");
		btns[2].setStringText("Back");
		
		modifiedPlayer = 0;
	}
	public void init() {
		super.init();
		
		if (modifiedPlayer == 0) btns[1].setGauge((float) (wFrame.getLeftPlayerTint() / 300f));
		else if (modifiedPlayer == 1) btns[1].setGauge((float) (wFrame.getRightPlayerTint() / 300f));
	}
	
	public void setModifiedPlayer(int mp) {modifiedPlayer = mp;}
	
	protected void pollSelection() {
		super.pollSelection();
		if (selection == 0) {
			if (modifiedPlayer == 0) {
				if (wFrame.getLeftPlayerType() == 0) wFrame.setLeftPlayerType(1);
				else if (wFrame.getLeftPlayerType() == 1) wFrame.setLeftPlayerType(0);
			} else if (modifiedPlayer == 1) {
				if (wFrame.getRightPlayerType() == 0) wFrame.setRightPlayerType(1);
				else if (wFrame.getRightPlayerType() == 1) wFrame.setRightPlayerType(0);
			}
		} else if (selection == 2) {
			wFrame.changeGUI(backGUI);
		}
	}
	protected void holdSelection() {
		super.holdSelection();
		if (selection == 1) {
			if (btns[selection].checkMouseOrigin(offsetX, offsetY) && btns[selection].checkMouseStayHorizontal(offsetY)) {
				if (modifiedPlayer == 0) wFrame.setLeftPlayerTint((int)(((float) (Game.getMouseX() - offsetX - 30.0f) / (float) (btns[selection].getW() - 60.0f)) * 300.0f));
				else if (modifiedPlayer == 1) wFrame.setRightPlayerTint((int)(((float) (Game.getMouseX() - offsetX - 30.0f) / (float) (btns[selection].getW() - 60.0f)) * 300.0f));
			}
		}
	}
	protected void keySelection() {
		super.keySelection();
		if (selection == 1) {
			if (Game.getKey(IK.RIGHT)) {
				if (modifiedPlayer == 0) wFrame.changeLeftPlayerTint(+1);
				else if (modifiedPlayer == 1) wFrame.changeRightPlayerTint(+1);
			} else if (Game.getKey(IK.LEFT)) {
				if (modifiedPlayer == 0) wFrame.changeLeftPlayerTint(-1);
				else if (modifiedPlayer == 1) wFrame.changeRightPlayerTint(-1);
			}
		}
		if (Game.getKeyPress(IK.ESC)) {
			wFrame.changeGUI(backGUI);
			Game.playSFX("Click");
		}
	}
	protected void displaySelection() {
		super.displaySelection();
		if (modifiedPlayer == 0) {
			if (wFrame.getLeftPlayerType() == 0) btns[0].setStringText("Type: Keyboard");
			else if (wFrame.getLeftPlayerType() == 1) btns[0].setStringText("Type: CPU AI");
			btns[1].setGauge((float) (wFrame.getLeftPlayerTint() / 300f));
		} else if (modifiedPlayer == 1) {
			if (wFrame.getRightPlayerType() == 0) btns[0].setStringText("Type: Keyboard");
			else if (wFrame.getRightPlayerType() == 1) btns[0].setStringText("Type: CPU AI");
			btns[1].setGauge((float) (wFrame.getRightPlayerTint() / 300f));
		}
	}
	
	public void draw(Graphics2D g) {
		String strg = "Player Settings";
		if (modifiedPlayer == 0) strg = "Left Player";
		else if (modifiedPlayer == 1) strg = "Right Player";
		AttributedString win = new AttributedString(strg);
		win.addAttribute(TextAttribute.FONT, Game.getFont(2));
		win.addAttribute(TextAttribute.FOREGROUND, new GradientPaint(0, 0, Color.yellow, 30, 0, Color.white, true));
		g.drawString(win.getIterator(), offsetX + 250, offsetY - 90);
		
		for(int i=0;i<btns.length;i++) btns[i].draw(g, offsetX, offsetY);
	}
}
