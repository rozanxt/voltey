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

public class TitleGUI extends SelectionGUI {
	
	private GameTitle wFrame;
	
	public TitleGUI(GameTitle wf) {
		super(3);
		wFrame = wf;
		
		setOffset(300f, 320f);
		
		btns = new GUIButton[selnum];
		for(int i=0;i<btns.length;i++) btns[i] = new SelButton();
		for(int i=0;i<btns.length;i++) btns[i].setSprites(new Sprite[]{new Sprite(wFrame.getImages("gui/selbutton"))});
		btns[0].setPos(0f, 0f);
		btns[1].setPos(0f, 40f);
		btns[2].setPos(0f, 80f);
		btns[0].setStringText("Start Game");
		btns[1].setStringText("Options");
		btns[2].setStringText("Quit Game");
	}
	
	protected void pollSelection() {
		super.pollSelection();
		if (selection == 0) wFrame.changeGUI(1);
		else if (selection == 1) wFrame.changeGUI(3);
		else if (selection == 2) Game.stopGame();
	}
	protected void keySelection() {
		if (Game.getKeyPress(IK.ESC)) {
			if (selection != 2) {
				selection = 2;
				Game.playSFX("Click");
			} else {
				Game.stopGame();
			}
		}
	}
	
	public void draw(Graphics2D g) {
		AttributedString win = new AttributedString("Main Menu");
		win.addAttribute(TextAttribute.FONT, Game.getFont(2));
		win.addAttribute(TextAttribute.FOREGROUND, new GradientPaint(0, 0, Color.yellow, 30, 0, Color.white, true));
		g.drawString(win.getIterator(), offsetX + 250, offsetY - 90);
		
		for(int i=0;i<btns.length;i++) btns[i].draw(g, offsetX, offsetY);
	}
}
