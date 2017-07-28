package zan.voltey.gameobj;

import java.awt.Graphics2D;

import zan.util.Sprite;
import zan.voltey.game.Game;
import zan.voltey.game.GameWorld;

public class VolteyBall extends GameObject {
	
	protected boolean moving;
	protected boolean hitable;
	
	protected float speed;
	protected float normspeed;
	protected float poutspeed;
	//private float maxspeed;
	//private float boost;
	//private float decay;
	private int bouncedelay;
	private int rebound;
	private float rotation;
	
	public VolteyBall(GameWorld gw) {
		super(gw, 40f, 0.5f, new Sprite[1]);
		moving = false;
		hitable = false;
		
		speed = 16f;
		normspeed = 16f;
		poutspeed = 5f;
		//maxspeed = 20f;
		//boost = 0.25f;
		//decay = 0.9f;
		bouncedelay = 0;
		rebound = 0;
		rotation = 0f;
	}
	public void init() {
		setActivation(true);
		setBall(0);
	}
	
	public void setSprites(Sprite s0) {
		objSprite[0] = s0;
	}
	
	public void setBall(int pt) {
		moving = false;
		hitable = true;
		if (pt == 0) {
			setPos(200f, 300f);
		} else if (pt == 1) {
			setPos(600f, 300f);
		}
	}
	
	public void phaseOut() {
		hitable = false;
	}
	
	public void rebound() {
		rebound = 20;
		if (hitable) Game.playSFX("VolteyHit");
	}
	
	public void adjustSpeed() {
		if (!hitable) speed = poutspeed;
		else speed = normspeed; // CONSTANTSPEED;
		
		float v = (float) (dx*dx + dy*dy);
		if (v != 0f && speed >= 0f) { 
			float k = (float) Math.sqrt(1f+(((speed*speed)/v)-1f));
			dx *= k;
			dy *= k;
		} else if (speed < 0f) {
			speed = 0f;
		}
	}
	/*public void decaySpeed() {
		speed *= decay;
	}*/
	
	public boolean isHitAble() {return hitable;}
	public boolean isMoving() {return moving;}
	public int getRebound() {return rebound;}
	
	public void bounce(PlayerBot s) {
		if (!moving) {
			moving = true;
			bouncedelay = 0;
		}
		
		if (bouncedelay == 0 || !gworld.checkBounce(s.getPlayerSide())) {
			bouncedelay = 10;
			rebound();
			gworld.countBounce(s.getPlayerSide());
		}
		
		float bounceangle = 90;
		
		float distx = getX() - s.getX();
		float disty = getY() - s.getY();
		float distance = (float) Math.sqrt(distx*distx + disty*disty);
		if (distance == 0f) return;
		float hitangle = (float) (Math.acos(distx/distance) * (180.0/Math.PI));
		
		/*float batterspeed = (float) Math.sqrt(s.getDX()*s.getDX() + s.getDY()*s.getDY());
		if (batterspeed != 0f) {
			float batterangle = (float) (Math.acos(s.getDX()/batterspeed) * (180.0/Math.PI));
			float boostangle = Math.abs(batterangle - hitangle);
			speed += (float) (Math.cos(boostangle * (Math.PI/180.0)) * batterspeed * boost);
			if (speed > maxspeed) speed = maxspeed;
		}*/
		
		bounceangle = hitangle;
		
		dx = (float) (Math.cos(bounceangle * (Math.PI/180.0)));
		if (s.getY() < getY()) {
			dy = (float) (Math.sin(bounceangle * (Math.PI/180.0)));
		} else if (s.getY() > getY()) {
			dy = (float) -(Math.sin(bounceangle * (Math.PI/180.0)));
		} else {
			dy = -dy;
		}
		adjustSpeed();
		
		x = (float) (1.1f*getX() - 0.1f*s.getX());
		y = (float) (1.1f*getY() - 0.1f*s.getY());
	}
	public void bounce(NetPole s) {
		if (getY() <= s.getY()) {
			float bounceangle = 90;
			
			float distx = getX() - s.getX();
			float disty = getY() - s.getY();
			float distance = (float) Math.sqrt(distx*distx + disty*disty);
			if (distance == 0f) return;
			float hitangle = (float) (Math.acos(distx/distance) * (180.0/Math.PI));
			
			bounceangle = hitangle;
			
			dx = (float) (Math.cos(bounceangle * (Math.PI/180.0)));
			dy = (float) -(Math.sin(bounceangle * (Math.PI/180.0)));
			adjustSpeed();
			
			x = (float) (1.1f*getX() - 0.1f*s.getX());
			y = (float) (1.1f*getY() - 0.1f*s.getY());
		} else {
			dx = -dx;
			
			if (getX() < s.getX()) {
				x = s.getX() - (getColRad() + s.getColRad());
			} else if (getX() > s.getX()) {
				x = s.getX() + (getColRad() + s.getColRad());
			}
		}
		
		rebound();
	}
	
	public void update(long gameTicker) {
		if (bouncedelay > 0) bouncedelay--;
		if (rebound > 0) rebound--;
		
		if (moving) {
			dy += gworld.gravitation * mass;
			x += dx;
			y += dy;
			
			if (x > gworld.rightborder) {
				dx = -dx;
				x = gworld.rightborder;
				rebound();
			}
			if (x < gworld.leftborder) {
				dx = -dx;
				x = gworld.leftborder;
				rebound();
			}
			if (y > gworld.ground) {
				if (hitable) {
					rebound();
					if (x > gworld.middleborder) {
						gworld.givePoint(0);
					} else if (x < gworld.middleborder) {
						gworld.givePoint(1);
					}
				}
				
				dy = -dy;
				adjustSpeed();
				
				y = gworld.ground;
			}
		}
		
		rotation += 5f;
		if (rotation >= 360f) rotation -= 360f;
		
		objSprite[0].update(gameTicker);
	}
	
	public void draw(Graphics2D g) {
		objSprite[0].setAngle(rotation);
		objSprite[0].draw(g, x, y);
	}
}