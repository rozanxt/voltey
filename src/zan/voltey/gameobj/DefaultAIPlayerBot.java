package zan.voltey.gameobj;

import zan.voltey.game.Game;
import zan.voltey.game.GameWorld;

public class DefaultAIPlayerBot extends CPUAIPlayerBot {
	
	public DefaultAIPlayerBot(GameWorld gw, int ps) {
		super(gw, ps);
		
	}
	
	public void setBot() {
		stamina += 10;
		if (stamina > maxstamina) stamina = maxstamina;
	}
	
	public void bounceFB() {
		if (stamina > 0) stamina--;
	}
	
	protected void serveBehavior() {
		float targetdistx = 400f - getX();
		float targetdisty = (float) Game.rnd.nextInt(100) - getY();
		float targetdistance = (float) Math.sqrt(targetdistx*targetdistx + targetdisty*targetdisty);
		if (targetdistance == 0f);
		float targetangle = (float) (Math.acos(targetdistx/targetdistance) * (180.0/Math.PI));
		
		float balldistx = ghostBall.getX() - getX();
		float balldisty = 50f;
		float balldistance = (float) Math.sqrt(balldistx*balldistx + balldisty*balldisty);
		if (balldistance == 0f) return;
		float ballangle = (float) (Math.acos(balldistx/balldistance) * (180.0/Math.PI));
		
		if (ballangle < targetangle - 5f) {
			actionRight();
		} else if (ballangle > targetangle + 5f) {
			actionLeft();
		} else {
			actionJump();
		}
	}
	
	protected void anticipationBehavior() {
		float targetdistx = 400f - getX();
		float targetdisty = (float) Game.rnd.nextInt(200) - getY();
		float targetdistance = (float) Math.sqrt(targetdistx*targetdistx + targetdisty*targetdisty);
		if (targetdistance == 0f) return;
		float targetangle = (float) (Math.acos(targetdistx/targetdistance) * (180.0/Math.PI));
		
		if (jumping) {
			ghostBall.setGhost(gworld.getBall());
			if (playerside == 0) {
				targetangle = 10f + (Math.abs(targetdistx) / 10f);
			} else if (playerside == 1) {
				targetangle = 170f - (Math.abs(targetdistx) / 10f);
			}
		}
		
		float balldistx = ghostBall.getX() - getX();
		float balldisty = ghostBall.getY() - getY();
		float balldistance = (float) Math.sqrt(balldistx*balldistx + balldisty*balldisty);
		if (balldistance == 0f) return;
		float ballangle = (float) (Math.acos(balldistx/balldistance) * (180.0/Math.PI));
			
		if (ghostBall.getY() > getY() - 50f) {
			if (ballangle < targetangle - 10f) {
				actionRight();
			} else if (ballangle > targetangle + 10f) {
				actionLeft();
			} else {
				actionIdle();
			}
		} else if (ghostBall.getY() < 350f && ghostBall.getY() > 50f) {
			balldistx = ghostBall.getX() - getX();
			balldisty = 50f;
			balldistance = (float) Math.sqrt(balldistx*balldistx + balldisty*balldisty);
			if (balldistance == 0f) return;
			ballangle = (float) (Math.acos(balldistx/balldistance) * (180.0/Math.PI));
			
			if (ballangle < targetangle - 10f) {
				actionRight();
			} else if (ballangle > targetangle + 10f) {
				actionLeft();
			} else {
				actionIdle();
			}
			
			if ((ghostBall.getY() < 250f && ghostBall.getY() > 200f) || (ghostBall.getY() < 350f && ghostBall.getY() > 320f))
				if (ghostBall.getDY() > 0 && ballangle > targetangle - 5f && ballangle < targetangle + 5f) {
					actionJump();
				}
		} else {
			idleBehavior();
		}
	}
	
	protected void idleBehavior() {
		if (playerside == 0) {
			if (x < 150f) {
				actionRight();
			} else if (x > 250f) {
				actionLeft();
			} else {
				actionIdle();
			}
		} else if (playerside == 1) {
			if (x < 550f) {
				actionRight();
			} else if (x > 650f) {
				actionLeft();
			} else {
				actionIdle();
			}
		}
	}
}