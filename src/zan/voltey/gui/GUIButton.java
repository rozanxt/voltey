package zan.voltey.gui;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import zan.util.Sprite;
import zan.voltey.game.Game;

public abstract class GUIButton {
	
	protected Sprite[] guiSprite;
	
	protected Rectangle btnarea;
	protected int btntype;
	
	protected boolean selected;
	protected boolean clicked;
	
	protected String stringtext;
	
	protected float x, y;
	
	protected float gauge;
	protected float selalpha;
	
	public GUIButton(Sprite[] os) {
		guiSprite = os;
		for (int i=0;i<guiSprite.length;i++) guiSprite[i] = null;
		btnarea = null;
		btntype = -1;
		selected = false;
		stringtext = null;
		x = 0f;	y = 0f;
		gauge = 0f;
		selalpha = 1f;
	}
	public void init() {
		selected = false;
	}
	
	public void setSprites(Sprite[] ss) {}
	
	public int getBtnType() {return btntype;}
	
	public void select() {selected = true;}
	public void deselect() {selected = false;}
	public boolean isSelected() {return selected;}
	
	public void click() {clicked = true;}
	public void unclick() {clicked = false;}
	public boolean isClicked() {return clicked;}
	
	public void setStringText(String s) {stringtext = s;}
	
	public void setGauge(float v) {if (v >= 0.0f && v <= 1.0f) gauge = v;}
	
	public void setPos(float sx, float sy) {
		x = sx; y = sy;
		if (btnarea != null) btnarea.setLocation((int)sx, (int)sy);
	}
	public void setButtonArea(int sx, int sy, int sw, int sh) {
		if (btnarea == null) btnarea = new Rectangle(sx, sy, sw, sh);
		else btnarea.setBounds(sx, sy, sw, sh);
	}
	
	public boolean checkMouseOver(float ox, float oy) {
		if (btnarea == null) return false;
		
		int mx = Game.getMouseX();
		int my = Game.getMouseY();
		int x0 = btnarea.x + (int)ox;
		int x1 = btnarea.x + btnarea.width + (int)ox;
		int y0 = btnarea.y + (int)oy;
		int y1 = btnarea.y + btnarea.height + (int)oy;
		
		if (mx > x0 && mx < x1 && my > y0 && my < y1) return true;
		return false;
	}
	public boolean checkMouseOrigin(float ox, float oy) {
		if (btnarea == null) return false;
		
		int fx = Game.getFixedX();
		int fy = Game.getFixedY();
		int x0 = btnarea.x + (int)ox;
		int x1 = btnarea.x + btnarea.width + (int)ox;
		int y0 = btnarea.y + (int)oy;
		int y1 = btnarea.y + btnarea.height + (int)oy;
		
		if (fx > x0 && fx < x1 && fy > y0 && fy < y1) {
			return true;
		}
		return false;
	}
	public boolean checkMouseStay(float ox, float oy) {
		if (btnarea == null) return false;
		
		int mx = Game.getMouseX();
		int my = Game.getMouseY();
		int fx = Game.getFixedX();
		int fy = Game.getFixedY();
		int x0 = btnarea.x + (int)ox;
		int x1 = btnarea.x + btnarea.width + (int)ox;
		int y0 = btnarea.y + (int)oy;
		int y1 = btnarea.y + btnarea.height + (int)oy;
		
		if (mx > x0 && mx < x1 && my > y0 && my < y1) {
			if (fx > x0 && fx < x1 && fy > y0 && fy < y1) {
				return true;
			}
		}
		return false;
	}
	public boolean checkMouseStayVertical(float ox) {
		if (btnarea == null) return false;
		
		int mx = Game.getMouseX();
		int fx = Game.getFixedX();
		int x0 = btnarea.x + (int)ox;
		int x1 = btnarea.x + btnarea.width + (int)ox;
		
		if (mx > x0 && mx < x1) {
			if (fx > x0 && fx < x1) {
				return true;
			}
		}
		return false;
	}
	public boolean checkMouseStayHorizontal(float oy) {
		if (btnarea == null) return false;
		
		int my = Game.getMouseY();
		int fy = Game.getFixedY();
		int y0 = btnarea.y + (int)oy;
		int y1 = btnarea.y + btnarea.height + (int)oy;
		
		if (my > y0 && my < y1) {
			if (fy > y0 && fy < y1) {
				return true;
			}
		}
		return false;
	}
	
	public float getX() {return btnarea.x;}
	public float getY() {return btnarea.y;}
	public int getW() {return btnarea.width;}
	public int getH() {return btnarea.height;}
	
	public void update(long gameTicker) {
		if (clicked) selalpha = 1f;
		else selalpha = (float) (0.9f + 0.1*Math.sin(0.01*gameTicker));
	}
	
	public abstract void draw(Graphics2D g, float ox, float oy);
}
