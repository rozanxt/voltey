package zan.voltey.gameobj;

import java.awt.Graphics2D;

import zan.util.Sprite;
import zan.voltey.game.GameWorld;

public abstract class CPUAIPlayerBot extends PlayerBot {
	
	protected GhostVolteyBall ghostBall;
	
	protected int maxstamina;
	protected int stamina;
	
	public CPUAIPlayerBot(GameWorld gw, int ps) {
		super(gw, ps);
		
		ghostBall = new GhostVolteyBall(gw);
		ghostBall.setSprites(new Sprite(gworld.getImages("obj/volteyball")));
		ghostBall.init();
		
		maxstamina = 20;
		stamina = maxstamina;
	}
	
	protected abstract void anticipationBehavior();
	protected abstract void serveBehavior();
	protected abstract void idleBehavior();
	
	protected void gainInput() {
		ghostBall.setGhost(gworld.getBall());
		if ((stamina*2) >= gworld.getBall().getRebound()) ghostBall.calcGhost(this);
		if (!gworld.getBall().isMoving() && gworld.getBallServer() == playerside) {
			serveBehavior();
		} else if (playerside == 0 && ghostBall.getX() <= 400f && gworld.getBall().isHitAble()) {
			anticipationBehavior();
		} else if (playerside == 1 && ghostBall.getX() >= 400f && gworld.getBall().isHitAble()) {
			anticipationBehavior();
		} else {
			idleBehavior();
		}
	}
	
	public void draw(Graphics2D g) {
		//if (ghostBall != null) ghostBall.draw(g);
		super.draw(g);
	}
}