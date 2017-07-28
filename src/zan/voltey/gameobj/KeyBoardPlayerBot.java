package zan.voltey.gameobj;

import zan.util.IK;
import zan.voltey.game.Game;
import zan.voltey.game.GameWorld;

public class KeyBoardPlayerBot extends PlayerBot {
	
	public KeyBoardPlayerBot(GameWorld gw, int ps) {
		super(gw, ps);
	}
	
	public void bounceFB() {}
	
	protected void gainInput() {
		if (playerside == 0) {
			if (Game.getKey(IK.W)) {
				actionJump();
			}
			if (Game.getKey(IK.D) && Game.getKey(IK.A)) {
				actionIdle();
			} else if (Game.getKey(IK.D)) {
				actionRight();
			} else if (Game.getKey(IK.A)) {
				actionLeft();
			} else {
				actionIdle();
			}
		} else if (playerside == 1) {
			if (Game.getKey(IK.UP)) {
				actionJump();
			}
			if (Game.getKey(IK.RIGHT) && Game.getKey(IK.LEFT)) {
				actionIdle();
			} else if (Game.getKey(IK.RIGHT)) {
				actionRight();
			} else if (Game.getKey(IK.LEFT)) {
				actionLeft();
			} else {
				actionIdle();
			}
		}
	}
}