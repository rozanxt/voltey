package zan.voltey.gameobj;

import java.awt.Graphics2D;

import zan.util.Sprite;
import zan.voltey.game.GameWorld;

public abstract class GameObject {
	
	protected GameWorld gworld;
	
	protected Sprite[] objSprite;
	
	protected float x, y;
	protected float dx, dy;
	
	protected float colrad;
	protected float mass;
	
	protected boolean active;
	
	public GameObject(GameWorld gw, float cr, float ms, Sprite[] os) {
		gworld = gw;
		objSprite = os;
		for (int i=0;i<objSprite.length;i++) objSprite[i] = null;
		colrad = cr;
		mass = ms;
		x = 0f;	y = 0f;
		dx = 0f; dy = 0f;
		active = false;
	}
	
	public void setActivation(boolean s) {active = s;}
	public void setPos(float sx, float sy) {
		x = sx; y = sy;
	}
	
	public boolean isActive() {return active;}
	public float getX() {return x;}
	public float getY() {return y;}
	public float getDX() {return dx;}
	public float getDY() {return dy;}
	public float getColRad() {return colrad;}
	public float getMass() {return mass;}
	
	public boolean collide(GameObject s) {
		float distance = (float)Math.sqrt((getX()-s.getX())*(getX()-s.getX())+(getY()-s.getY())*(getY()-s.getY()));
		if (distance <= (getColRad()+s.getColRad())) return true;
		return false;
	}
	
	public abstract void init();
	public abstract void update(long gameTicker);
	public abstract void draw(Graphics2D g);
}
