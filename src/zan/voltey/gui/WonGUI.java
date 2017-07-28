package zan.voltey.gui;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.font.TextAttribute;
import java.text.AttributedString;

import zan.util.Sprite;
import zan.voltey.game.Game;
import zan.voltey.game.GameWorld;

public class WonGUI extends SelectionGUI {
	
	private GameWorld wFrame;
	
	public WonGUI(GameWorld wf) {
		super(2);
		wFrame = wf;
		
		btns = new GUIButton[selnum];
		for(int i=0;i<btns.length;i++) btns[i] = new SelButton();
		for(int i=0;i<btns.length;i++) btns[i].setSprites(new Sprite[]{new Sprite(wFrame.getImages("gui/selbutton"))});
		btns[0].setPos(0f, 0f);
		btns[1].setPos(0f, 40f);
		btns[0].setStringText("Play Again");
		btns[1].setStringText("Back to Main Menu");
	}
	
	protected void pollSelection() {
		super.pollSelection();
		if (selection == 0) wFrame.initMatch();
		else if (selection == 1) wFrame.setTitleFrame();
	}
	
	public void draw(Graphics2D g) {
		if (wFrame.getWinner() == 0) {
			AttributedString win = new AttributedString("Left Player Won!");
			win.addAttribute(TextAttribute.FONT, Game.getFont(2));
			win.addAttribute(TextAttribute.FOREGROUND, new GradientPaint(0, 0, Color.green, 30, 0, Color.white, true));
			g.drawString(win.getIterator(), offsetX, offsetY - 90);
		} else if (wFrame.getWinner() == 1) {
			AttributedString win = new AttributedString("Right Player Won!");
			win.addAttribute(TextAttribute.FONT, Game.getFont(2));
			win.addAttribute(TextAttribute.FOREGROUND, new GradientPaint(0, 0, Color.red, 30, 0, Color.white, true));
			g.drawString(win.getIterator(), offsetX, offsetY - 90);
		}
		
		for(int i=0;i<btns.length;i++) btns[i].draw(g, offsetX, offsetY);
	}
}
