package zan.voltey.gui;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.font.TextAttribute;
import java.text.AttributedString;

import zan.util.IK;
import zan.util.Sprite;
import zan.voltey.game.Game;
import zan.voltey.game.GameWorld;

public class NavigationGUI {
	
	private GameWorld gworld;
	
	private SelButton pauseBtn;
	
	private int exclamationBlink;
	
	public NavigationGUI(GameWorld gw) {
		gworld = gw;
		
		pauseBtn = new SelButton();
		pauseBtn.setSprites(new Sprite[]{new Sprite(gworld.getImages("gui/pausebtn"))});
		pauseBtn.setPos(760f, 10f);
		
		exclamationBlink = 0;
	}
	
	public void updateNav(long gameTicker) {
		if (Game.getKeyPress(IK.P) || Game.getKeyPress(IK.ESC)) {
			gworld.togglePause();
		}
		if (Game.getClickL()) {
			if (pauseBtn.checkMouseStay(0f, 0f)) {
				gworld.togglePause();
			}
		}
		if (Game.getClickR()) {
			gworld.togglePause();
		}
		
		if (pauseBtn.checkMouseOver(0f, 0f)) {
			if (!pauseBtn.isSelected()) {
				pauseBtn.select();
				Game.playSFX("Click");
			}
			Game.changeCursor(1);
		} else {
			pauseBtn.deselect();
			if (!gworld.isPaused()) Game.changeCursor(0);
		}
		
		pauseBtn.update(gameTicker);
	}
	
	public void update(long gameTicker) {
		exclamationBlink++;
		if (exclamationBlink >= 20) exclamationBlink = 0;
	}
	
	public void drawNav(Graphics2D g) {
		pauseBtn.draw(g, 0f, 0f);
	}
	
	public void draw(Graphics2D g) {
		
		AttributedString score = new AttributedString(":");
		score.addAttribute(TextAttribute.FONT, Game.getFont(1));
		score.addAttribute(TextAttribute.FOREGROUND, new GradientPaint(0, 0, Color.blue, 10, 0, Color.cyan, true));
		g.drawString(score.getIterator(), 391, 60);
		
		AttributedString leftScore = new AttributedString(gworld.getLeftPoint() + " ");
		leftScore.addAttribute(TextAttribute.FONT, Game.getFont(1));
		leftScore.addAttribute(TextAttribute.FOREGROUND, new GradientPaint(0, 0, Color.blue, 20, 0, Color.cyan, true));
		if (gworld.getLeftPoint() < 10) g.drawString(leftScore.getIterator(), 370 - 27, 60);
		else if (gworld.getLeftPoint() < 100) g.drawString(leftScore.getIterator(), 370 - 27*2, 60);
		else if (gworld.getLeftPoint() < 1000) g.drawString(leftScore.getIterator(), 370 - 27*3, 60);
		
		AttributedString rightScore = new AttributedString(" " + gworld.getRightPoint());
		rightScore.addAttribute(TextAttribute.FONT, Game.getFont(1));
		rightScore.addAttribute(TextAttribute.FOREGROUND, new GradientPaint(0, 0, Color.blue, 20, 0, Color.cyan, true));
		g.drawString(rightScore.getIterator(), 420, 60);
		
		AttributedString owner = new AttributedString("!");
		owner.addAttribute(TextAttribute.FONT, Game.getFont(1));
		owner.addAttribute(TextAttribute.FOREGROUND, new GradientPaint(0, 0, Color.red, 10, 0, Color.white, true));
		if (gworld.getBallOwner() == 0) {
			if (gworld.getLeftPoint() == gworld.getWinPoint()-1) {
				if (exclamationBlink < 10) {
					if (gworld.getLeftPoint() < 10) g.drawString(owner.getIterator(), 340 - 27, 60);
					else if (gworld.getLeftPoint() < 100) g.drawString(owner.getIterator(), 340 - 27*2, 60);
					else if (gworld.getLeftPoint() < 1000) g.drawString(owner.getIterator(), 340 - 27*3, 60);
				}
			} else {
				if (gworld.getLeftPoint() < 10) g.drawString(owner.getIterator(), 340 - 27, 60);
				else if (gworld.getLeftPoint() < 100) g.drawString(owner.getIterator(), 340 - 27*2, 60);
				else if (gworld.getLeftPoint() < 1000) g.drawString(owner.getIterator(), 340 - 27*3, 60);
			}
		} else if (gworld.getBallOwner() == 1) {
			if (gworld.getRightPoint() == gworld.getWinPoint()-1) {
				if (exclamationBlink < 10) {
					if (gworld.getRightPoint() < 10) g.drawString(owner.getIterator(), 435 + 27, 60);
					else if (gworld.getRightPoint() < 100) g.drawString(owner.getIterator(), 435 + 27*2, 60);
					else if (gworld.getRightPoint() < 1000) g.drawString(owner.getIterator(), 435 + 27*3, 60);
				}
			} else {
				if (gworld.getRightPoint() < 10) g.drawString(owner.getIterator(), 435 + 27, 60);
				else if (gworld.getRightPoint() < 100) g.drawString(owner.getIterator(), 435 + 27*2, 60);
				else if (gworld.getRightPoint() < 1000) g.drawString(owner.getIterator(), 435 + 27*3, 60);
			}
		}
	}
}