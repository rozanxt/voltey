package zan.voltey.gui;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.font.TextAttribute;
import java.text.AttributedString;

import zan.util.IK;
import zan.util.Sprite;
import zan.util.WorldFrame;
import zan.voltey.game.Game;

public class OptionsGUI extends SelectionGUI {
	
	private WorldFrame wFrame;
	
	public OptionsGUI(WorldFrame wf) {
		super(4);
		wFrame = wf;
		
		btns = new GUIButton[selnum];
		btns[0] = new GaugeButton();
		btns[1] = new GaugeButton();
		btns[2] = new SelButton();
		btns[3] = new SelButton();
		btns[0].setSprites(new Sprite[]{new Sprite(wFrame.getImages("gui/selbutton")), new Sprite(wFrame.getImages("gui/gaugebar"))});
		btns[1].setSprites(new Sprite[]{new Sprite(wFrame.getImages("gui/selbutton")), new Sprite(wFrame.getImages("gui/gaugebar"))});
		btns[2].setSprites(new Sprite[]{new Sprite(wFrame.getImages("gui/selbutton"))});
		btns[3].setSprites(new Sprite[]{new Sprite(wFrame.getImages("gui/selbutton"))});
		btns[0].setPos(0f, 0f);
		btns[1].setPos(0f, 40f);
		btns[2].setPos(0f, 80f);
		btns[3].setPos(0f, 120f);
		btns[0].setStringText("Music Volume");
		btns[1].setStringText("Sound Volume");
		btns[2].setStringText("Fullscreen");
		btns[3].setStringText("Back");
	}
	public void init() {
		super.init();
		
		btns[0].setGauge(Game.getBgmVolume());
		btns[1].setGauge(Game.getSfxVolume());
	}
	
	protected void pollSelection() {
		super.pollSelection();
		if (selection == 2) wFrame.toggleFullScreen();
		if (selection == 3) wFrame.changeGUI(backGUI);
	}
	protected void holdSelection() {
		super.holdSelection();
		if (selection == 0) {
			if (btns[selection].checkMouseOrigin(offsetX, offsetY) && btns[selection].checkMouseStayHorizontal(offsetY)) {
				Game.setBgmVolume((float) (Game.getMouseX() - offsetX - 30f) / (float) (btns[selection].getW() - 60f));
			}
		} else if (selection == 1) {
			if (btns[selection].checkMouseOrigin(offsetX, offsetY) && btns[selection].checkMouseStayHorizontal(offsetY)) {
				Game.setSfxVolume((float) (Game.getMouseX() - offsetX - 30f) / (float) (btns[selection].getW() - 60f));
			}
		}
	}
	protected void keySelection() {
		super.keySelection();
		if (selection == 0) {
			if (Game.getKey(IK.RIGHT)) Game.changeBgmVolume(0.01f);
			else if (Game.getKey(IK.LEFT)) Game.changeBgmVolume(-0.01f);
		}
		if (selection == 1) {
			if (Game.getKey(IK.RIGHT)) Game.changeSfxVolume(0.01f);
			else if (Game.getKey(IK.LEFT)) Game.changeSfxVolume(-0.01f);
		}
		if (Game.getKeyPress(IK.ESC)) {
			wFrame.changeGUI(backGUI);
			Game.playSFX("Click");
		}
	}
	protected void displaySelection() {
		super.displaySelection();
		btns[0].setGauge(Game.getBgmVolume());
		btns[1].setGauge(Game.getSfxVolume());
		btns[0].setStringText("Music Volume      " + (int)(Game.getBgmVolume()*100) + "%");
		btns[1].setStringText("Sound Volume     " + (int)(Game.getSfxVolume()*100) + "%");
		if (Game.isFullScreen()) btns[2].setStringText("Fullscreen            ON");
		else btns[2].setStringText("Fullscreen            OFF");
	}
	
	public void draw(Graphics2D g) {
		AttributedString win = new AttributedString("Options");
		win.addAttribute(TextAttribute.FONT, Game.getFont(2));
		win.addAttribute(TextAttribute.FOREGROUND, new GradientPaint(0, 0, Color.yellow, 30, 0, Color.white, true));
		g.drawString(win.getIterator(), offsetX + 250, offsetY - 90);
		
		for(int i=0;i<btns.length;i++) btns[i].draw(g, offsetX, offsetY);
	}
}