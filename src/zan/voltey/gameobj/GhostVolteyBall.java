package zan.voltey.gameobj;

import zan.voltey.game.GameWorld;

public class GhostVolteyBall extends VolteyBall {
	
	public GhostVolteyBall(GameWorld gw) {
		super(gw);
	}
	
	public void setGhost(VolteyBall s) {
		x = s.getX();
		y = s.getY();
		dx = s.getDX();
		dy = s.getDY();
		speed = s.speed;
		moving = s.moving;
		hitable = false;
	}
	
	public void calcGhost(PlayerBot s) {
		int repeat = 0;
		while (repeat < 25) {
			repeat++;
			if (moving) {
				dy += gworld.gravitation * mass;
				x += dx;
				y += dy;
				
				if (x > gworld.rightborder) {
					dx = -dx;
					x = gworld.rightborder;
				}
				if (x < gworld.leftborder) {
					dx = -dx;
					x = gworld.leftborder;
				}
				if (y > gworld.ground) {
					dy = -dy;
					adjustSpeed();
					
					y = gworld.ground;
				}
				
				if (gworld.getNet().collide(this)) {
					bounce(gworld.getNet());
				}
			}
			
			if (y > s.getY() - 50f) return;
			
		}
	}
	
}