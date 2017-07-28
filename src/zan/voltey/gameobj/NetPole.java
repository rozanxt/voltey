package zan.voltey.gameobj;

import java.awt.Graphics2D;

import zan.util.Sprite;
import zan.voltey.game.GameWorld;

public class NetPole extends GameObject {
	
	public NetPole(GameWorld gw) {
		super(gw, 8f, 0f, new Sprite[1]);
	}
	public void init() {
		setPos(gworld.middleborder, gworld.ground - gworld.netheight);
		setActivation(true);
	}
	
	public void setSprites(Sprite s0) {
		objSprite[0] = s0;
	}
	
	public boolean collide(GameObject s) {
		if (s.getY() > getY()) {
			if (Math.abs(s.getX() - getX()) <= getColRad()+s.getColRad()) return true;
		}
		return super.collide(s);
	}
	
	public void update(long gameTicker) {}
	
	public void draw(Graphics2D g) {
		objSprite[0].setCentered(false);
		objSprite[0].draw(g, x - 8f, y - 8f);
	}
}