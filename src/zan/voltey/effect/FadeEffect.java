package zan.voltey.effect;

import java.awt.Color;
import java.awt.Graphics2D;

import zan.util.WorldFrame;
import zan.voltey.game.Game;

public class FadeEffect {
	
	private Game gmain;
	
	private WorldFrame frameQueue;
	
	private int fadeState;
	private int fadeCounter;
	private int loadCounter;
	
	public FadeEffect(Game gm) {
		gmain = gm;
		frameQueue = null;
		fadeState = 0;
		fadeCounter = 0;
		loadCounter = 0;
	}
	
	public void fade(WorldFrame fq) {
		frameQueue = fq;
		fadeState = 1;
		gmain.setTimedUpdate(false);
		gmain.lockInput();
	}
	
	public void update(long gameTicker) {
		if (fadeState != 0) {
			if (fadeState == 1) {
				fadeCounter += 10;
				if (fadeCounter > 255) {
					fadeCounter = 255;
					fadeState = 2;
					if (frameQueue != null) gmain.setScreenFrame(frameQueue);
				}
			} else if (fadeState == 2) {
				loadCounter++;
				if (loadCounter > 10) {
					loadCounter = 0;
					fadeState = 3;
					fadeCounter = 255;
				}
			} else if (fadeState == 3) {
				fadeCounter -= 10;
				if (fadeCounter < 0) {
					fadeCounter = 0;
					fadeState = 0;
					gmain.setTimedUpdate(true);
					gmain.unlockInput();
				}
			}
		}
	}
	
	public void draw(Graphics2D g) {
		if (fadeState != 0) {
			g.setColor(new Color(0, 0, 0, fadeCounter));
			g.fillRect(0, 0, Game.SCR_WIDTH, Game.SCR_HEIGHT);
		}
	}
}
