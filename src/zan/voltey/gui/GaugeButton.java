package zan.voltey.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.font.TextAttribute;
import java.text.AttributedString;

import zan.util.Sprite;
import zan.voltey.game.Game;



public class GaugeButton extends GUIButton {
	
	public GaugeButton() {
		super(new Sprite[2]);
		btntype = 1;
	}
	
	public void setSprites(Sprite[] ss) {
		guiSprite[0] = ss[0];	// Button
		guiSprite[0].setCentered(false);
		setButtonArea((int)x, (int)y, guiSprite[0].getWidth(), guiSprite[0].getHeight());
		guiSprite[1] = ss[1];	// Gauge Bar
	}
	
	public void update(long gameTicker) {
		super.update(gameTicker);
		
		guiSprite[0].update(gameTicker);
	}
	
	public void draw(Graphics2D g, float ox, float oy) {
		if (selected) {
			guiSprite[0].setAlpha(selalpha);
			guiSprite[0].setFrame(1);
		} else {
			guiSprite[0].setAlpha(1f);
			guiSprite[0].setFrame(0);
		}
		guiSprite[0].draw(g, x + ox, y + oy);
		
		guiSprite[1].draw(g, x + ox + 30f + (gauge * 240f), y + oy + 16f);
		
		if (stringtext != null) {
			AttributedString s = new AttributedString(stringtext);
			s.addAttribute(TextAttribute.FONT, Game.getFont(3));
			s.addAttribute(TextAttribute.FOREGROUND, Color.white);
			g.drawString(s.getIterator(), x + ox + 50, y + oy + 23);
		}
	}
	
}