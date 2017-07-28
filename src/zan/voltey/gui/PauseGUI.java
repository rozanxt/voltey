package zan.voltey.gui;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.font.TextAttribute;
import java.text.AttributedString;

import zan.util.Sprite;
import zan.voltey.game.Game;
import zan.voltey.game.GameWorld;

public class PauseGUI extends SelectionGUI {
	
	private GameWorld wFrame;
	
	public PauseGUI(GameWorld wf) {
		super(3);
		wFrame = wf;
		
		btns = new GUIButton[selnum];
		for(int i=0;i<btns.length;i++) btns[i] = new SelButton();
		for(int i=0;i<btns.length;i++) btns[i].setSprites(new Sprite[]{new Sprite(wFrame.getImages("gui/selbutton"))});
		btns[0].setPos(0f, 0f);
		btns[1].setPos(0f, 40f);
		btns[2].setPos(0f, 80f);
		btns[0].setStringText("Continue");
		btns[1].setStringText("Options");
		btns[2].setStringText("Leave Game");
	}
	
	protected void pollSelection() {
		super.pollSelection();
		if (selection == 0) wFrame.unpauseGame();
		else if (selection == 1) wFrame.changeGUI(1);
		else if (selection == 2) wFrame.setTitleFrame();
	}
	
	public void draw(Graphics2D g) {
		AttributedString win = new AttributedString("Game Paused");
		win.addAttribute(TextAttribute.FONT, Game.getFont(2));
		win.addAttribute(TextAttribute.FOREGROUND, new GradientPaint(0, 0, Color.yellow, 30, 0, Color.white, true));
		g.drawString(win.getIterator(), offsetX + 250, offsetY - 90);
		
		for(int i=0;i<btns.length;i++) btns[i].draw(g, offsetX, offsetY);
	}
}
