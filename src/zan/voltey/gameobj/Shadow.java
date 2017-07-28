package zan.voltey.gameobj;

import java.awt.Graphics2D;

import zan.util.Sprite;
import zan.voltey.game.GameWorld;

public class Shadow extends GameObject {
	
	GameObject refObj;
	
	public Shadow(GameWorld gw, GameObject ro) {
		super(gw, 40f, 0f, new Sprite[1]);
		refObj = ro;
	}
	public void init() {
		setPos(refObj.getX(), gworld.ground + gworld.shadowoffset);
		setActivation(true);
	}
	
	public void setSprites(Sprite s0) {
		objSprite[0] = s0;
	}
	
	public void update(long gameTicker) {
		x = (float) (refObj.getX() + (Math.tan(10.0*(Math.PI/180.0))*(gworld.ground - refObj.getY())));
		y = (float) (gworld.ground + gworld.shadowoffset - (Math.tan(2.0*(Math.PI/180.0))*(gworld.ground - refObj.getY())));
	}
	
	public void draw(Graphics2D g) {
		objSprite[0].draw(g, x, y);
	}
}