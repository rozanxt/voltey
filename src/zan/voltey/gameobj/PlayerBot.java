package zan.voltey.gameobj;

import java.awt.Graphics2D;

import zan.util.Sprite;
import zan.voltey.game.GameWorld;

public abstract class PlayerBot extends GameObject {
	
	protected int playerside;
	
	protected float rtint, gtint, btint, ctint, mtint, ytint;
	
	protected boolean moving;
	protected boolean jumping;
	
	protected float speed;
	protected float jumppower;
	
	public PlayerBot(GameWorld gw, int ps) {
		super(gw, 40f, 1f, new Sprite[16]);
		playerside = ps;
		moving = false;
		jumping = false;
		speed = 7.5f;
		jumppower = 20f;
		rtint = 0f; gtint = 0f; btint = 0f;
		ctint = 0f; mtint = 0f; ytint = 0f;
	}
	public void init() {
		if (playerside == 0) {
			setPos(200f, gworld.ground);
		} else if (playerside == 1) {
			setPos(600f, gworld.ground);
		}
		
		setActivation(true);
	}
	
	public void setSprites(Sprite is, Sprite iw, Sprite ir, Sprite ig, Sprite ib, Sprite ic, Sprite im, Sprite iy, Sprite ms, Sprite mw, Sprite mr, Sprite mg, Sprite mb, Sprite mc, Sprite mm, Sprite my) {
		objSprite[0] = is;	// IDLE MASK
		objSprite[1] = iw;	// IDLE WHITE MASK
		objSprite[2] = ir;	// IDLE RED MASK
		objSprite[3] = ig;	// IDLE GREEN MASK
		objSprite[4] = ib;	// IDLE BLUE MASK
		objSprite[5] = ic;	// IDLE CYAN MASK
		objSprite[6] = im;	// IDLE MAGENTA MASK
		objSprite[7] = iy;	// IDLE YELLOW MASK
		objSprite[8] = ms;	// MOVE MASK
		objSprite[9] = mw;	// MOVE WHITE MASK
		objSprite[10] = mr;	// MOVE RED MASK
		objSprite[11] = mg;	// MOVE GREEN MASK
		objSprite[12] = mb;	// MOVE BLUE MASK
		objSprite[13] = mc;	// MOVE CYAN MASK
		objSprite[14] = mm;	// MOVE MAGENTA MASK
		objSprite[15] = my;	// MOVE YELLOW MASK
		
		for (int i=8;i<16;i++) objSprite[i].setAnimation(true, 20f);
	}
	public void setTint(int hue) {
		rtint = 0f; gtint = 0f; btint = 0f;
		ctint = 0f; mtint = 0f; ytint = 0f;
		if (hue >= 0 && hue < 50) {
			rtint = (float)(50 - hue) / 50f;
			ytint = (float)(hue) / 50f;
		} else if (hue >= 50 && hue < 100) {
			ytint = (float)(100 - hue) / 50f;
			gtint = (float)(hue - 50) / 50f;
		} else if (hue >= 100 && hue < 150) {
			gtint = (float)(150 - hue) / 50f;
			ctint = (float)(hue - 100) / 50f;
		} else if (hue >= 150 && hue < 200) {
			ctint = (float)(200 - hue) / 50f;
			btint = (float)(hue - 150) / 50f;
		} else if (hue >= 200 && hue < 250) {
			btint = (float)(250 - hue) / 50f;
			mtint = (float)(hue - 200) / 50f;
		} else if (hue >= 250 && hue <= 300) {
			mtint = (float)(300 - hue) / 50f;
			rtint = (float)(hue - 250) / 50f;
		}
	}
	
	public void setBot() {
		
	}
	
	public int getPlayerSide() {return playerside;}
	public boolean isJumping() {return jumping;}
	
	public abstract void bounceFB();
	
	protected void actionIdle() {
		moving = false;
	}
	protected void actionRight() {
		moving = true;
		dx = speed;
	}
	protected void actionLeft() {
		moving = true;
		dx = -speed;
	}
	protected void actionJump() {
		if (!jumping) {
			dy = -jumppower;
		}
		jumping = true;
	}
	
	protected abstract void gainInput();
	
	public void update(long gameTicker) {
		gainInput();
		
		if (moving) {
			x += dx;
			
			if (playerside == 0) {
				if (x > gworld.middleborder - gworld.interlude) {
					x = gworld.middleborder - gworld.interlude;
				}
				if (x < gworld.leftborder - (gworld.interlude / 5f)) {
					x = gworld.leftborder - (gworld.interlude / 5f);
				}
			} else if (playerside == 1) {
				if (x < gworld.middleborder + gworld.interlude) {
					x = gworld.middleborder + gworld.interlude;
				}
				if (x > gworld.rightborder + (gworld.interlude / 5f)) {
					x = gworld.rightborder + (gworld.interlude / 5f);
				}
			}
			
		} else {
			dx = 0f;
			objSprite[1].setFrame(0);
		}
		if (jumping) {
			y += dy;
			dy += gworld.gravitation * mass;
			if (y > gworld.ground) {
				y = gworld.ground;
				jumping = false;
				dy = 0f;
			}
		} else {
			dy = 0f;
		}
		
		for (int i=0;i<objSprite.length;i++) objSprite[i].update(gameTicker);
	}
	
	public void draw(Graphics2D g) {
		if (moving && !jumping) {
			if (playerside == 0) {
				if (dx > 0f) {
					for (int i=8;i<16;i++) objSprite[i].setBackAnimation(false);
				} else if (dx < 0f) {
					for (int i=8;i<16;i++) objSprite[i].setBackAnimation(true);
				}
			} else if (playerside == 1) {
				if (dx > 0f) {
					for (int i=8;i<16;i++) objSprite[i].setBackAnimation(true);
				} else if (dx < 0f) {
					for (int i=8;i<16;i++) objSprite[i].setBackAnimation(false);
				}
			}
			objSprite[10].setAlpha(rtint);
			objSprite[11].setAlpha(gtint);
			objSprite[12].setAlpha(btint);
			objSprite[13].setAlpha(ctint);
			objSprite[14].setAlpha(mtint);
			objSprite[15].setAlpha(ytint);
			
			objSprite[9].draw(g, x, y);
			if (rtint > 0f) objSprite[10].draw(g, x, y);
			if (gtint > 0f) objSprite[11].draw(g, x, y);
			if (btint > 0f) objSprite[12].draw(g, x, y);
			if (ctint > 0f) objSprite[13].draw(g, x, y);
			if (mtint > 0f) objSprite[14].draw(g, x, y);
			if (ytint > 0f) objSprite[15].draw(g, x, y);
			objSprite[8].draw(g, x, y);
		} else {
			objSprite[2].setAlpha(rtint);
			objSprite[3].setAlpha(gtint);
			objSprite[4].setAlpha(btint);
			objSprite[5].setAlpha(ctint);
			objSprite[6].setAlpha(mtint);
			objSprite[7].setAlpha(ytint);
			
			objSprite[1].draw(g, x, y);
			if (rtint > 0f) objSprite[2].draw(g, x, y);
			if (gtint > 0f) objSprite[3].draw(g, x, y);
			if (btint > 0f) objSprite[4].draw(g, x, y);
			if (ctint > 0f) objSprite[5].draw(g, x, y);
			if (mtint > 0f) objSprite[6].draw(g, x, y);
			if (ytint > 0f) objSprite[7].draw(g, x, y);
			objSprite[0].draw(g, x, y);
		}
	}
}