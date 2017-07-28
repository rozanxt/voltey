package zan.voltey.gameobj;

import java.awt.Graphics2D;

import zan.util.Sprite;
import zan.voltey.game.GameWorld;

public class BallMarker extends GameObject {
	
	public BallMarker(GameWorld gw) {
		super(gw, 16f, 0f, new Sprite[1]);
	}
	public void init() {
		setPos(400f, 16f);
		setActivation(true);
	}
	
	public void setSprites(Sprite s0) {
		objSprite[0] = s0;
		objSprite[0].setAnimation(true, 2f);
	}
	
	public void update(long gameTicker) {
		objSprite[0].update(gameTicker);
	}
	
	public void draw(Graphics2D g) {
		objSprite[0].draw(g, x, y);
	}
}