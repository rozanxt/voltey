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

public class GameModeGUI extends SelectionGUI {
	
	private GameTitle wFrame;
	
	public GameModeGUI(GameTitle wf) {
		super(4);
		wFrame = wf;
		
		setOffset(300f, 320f);
		
		btns = new GUIButton[selnum];
		for(int i=0;i<btns.length;i++) btns[i] = new SelButton();
		for(int i=0;i<btns.length;i++) btns[i].setSprites(new Sprite[]{new Sprite(wFrame.getImages("gui/selbutton"))});
		btns[0].setPos(0f, 0f);
		btns[1].setPos(0f, 40f);
		btns[2].setPos(0f, 80f);
		btns[3].setPos(0f, 120f);
		btns[0].setStringText("GO!");
		btns[1].setStringText("Modify Left Player");
		btns[2].setStringText("Modify Right Player");
		btns[3].setStringText("Back");
	}
	
	protected void pollSelection() {
		super.pollSelection();
		if (selection == 0) {
			wFrame.setGameFrame();
		} else if (selection == 1) {
			wFrame.modifyPlayer(0);
		} else if (selection == 2) {
			wFrame.modifyPlayer(1);
		} else if (selection == 3) {
			wFrame.changeGUI(backGUI);
		}
	}
	protected void keySelection() {
		if (Game.getKeyPress(IK.ESC)) {
			wFrame.changeGUI(backGUI);
			Game.playSFX("Click");
		}
	}
	
	public void draw(Graphics2D g) {
		AttributedString win = new AttributedString("Game Settings");
		win.addAttribute(TextAttribute.FONT, Game.getFont(2));
		win.addAttribute(TextAttribute.FOREGROUND, new GradientPaint(0, 0, Color.yellow, 30, 0, Color.white, true));
		g.drawString(win.getIterator(), offsetX + 250, offsetY - 90);
		
		for(int i=0;i<btns.length;i++) btns[i].draw(g, offsetX, offsetY);
	}
}
