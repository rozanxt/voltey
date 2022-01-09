package zan.voltey.game;

import java.awt.Color;
import java.awt.Graphics2D;

import zan.voltey.gameobj.*;
import zan.voltey.gui.NavigationGUI;
import zan.voltey.gui.OptionsGUI;
import zan.voltey.gui.PauseGUI;
import zan.voltey.gui.SelectionGUI;
import zan.voltey.gui.WonGUI;
import zan.util.Sprite;
import zan.util.WorldFrame;

public class GameWorld extends WorldFrame {
	
	public final float netheight = 160f;
	public final float middleborder = 400f;
	public final float leftborder = 0f;
	public final float rightborder = 800f;
	public final float interlude = 50f;
	public final float ground = 450f;
	public final float shadowoffset = 43f;
	public final float gravitation = 0.85f;
	private final int defaultWinPoint = 15;
	
	private int winPoint;
	private int leftPoint;
	private int rightPoint;
	private int playerScored;
	private int ballOwner;
	private int ballServer;
	
	private int ballCounter;
	private int lastBall;
	private int ballCoolDown;
	
	private NavigationGUI navGUI;
	
	private Sprite bgField;
	
	private PlayerBot greenBot;
	private PlayerBot redBot;
	private VolteyBall vball;
	private NetPole netp;
	private BallMarker bmark;
	private Shadow objShadow[];
	
	private boolean gamePaused;
	private int gameWinner;
	
	public GameWorld(Game gm, int gms, int lpt, int lph, int rpt, int rph) {
		super(gm);
		
		initVariables();
		
		navGUI = new NavigationGUI(this);
		
		bgField = new Sprite(gmain.getImages("bg/futurefield"));
		bgField.setCentered(false);
		
		if (lpt == 0) {
			greenBot = new KeyBoardPlayerBot(this, 0);
		} else if (lpt == 1) {
			greenBot = new DefaultAIPlayerBot(this, 0);
		} else {
			greenBot = new KeyBoardPlayerBot(this, 0);
		}
		greenBot.setSprites(new Sprite(gmain.getImages("obj/leftbot_idle")), 
							new Sprite(gmain.getImages("obj/leftbot_idle_mask")), 
							new Sprite(gmain.getImages("obj/leftbot_idle_rmask")), 
							new Sprite(gmain.getImages("obj/leftbot_idle_gmask")), 
							new Sprite(gmain.getImages("obj/leftbot_idle_bmask")), 
							new Sprite(gmain.getImages("obj/leftbot_idle_cmask")), 
							new Sprite(gmain.getImages("obj/leftbot_idle_mmask")), 
							new Sprite(gmain.getImages("obj/leftbot_idle_ymask")), 
							new Sprite(gmain.getImages("obj/leftbot_move")), 
							new Sprite(gmain.getImages("obj/leftbot_move_mask")), 
							new Sprite(gmain.getImages("obj/leftbot_move_rmask")), 
							new Sprite(gmain.getImages("obj/leftbot_move_gmask")), 
							new Sprite(gmain.getImages("obj/leftbot_move_bmask")), 
							new Sprite(gmain.getImages("obj/leftbot_move_cmask")), 
							new Sprite(gmain.getImages("obj/leftbot_move_mmask")), 
							new Sprite(gmain.getImages("obj/leftbot_move_ymask")));
		greenBot.setTint(lph);
		
		if (rpt == 0) {
			redBot = new KeyBoardPlayerBot(this, 1);
		} else if (rpt == 1) {
			redBot = new DefaultAIPlayerBot(this, 1);
		} else {
			redBot = new KeyBoardPlayerBot(this, 1);
		}
		//redBot.setSprites(new Sprite(gmain.getImages("obj/redbot_idle")), new Sprite(gmain.getImages("obj/redbot_move")));
		redBot.setSprites(new Sprite(gmain.getImages("obj/rightbot_idle")), 
						  new Sprite(gmain.getImages("obj/rightbot_idle_mask")), 
						  new Sprite(gmain.getImages("obj/rightbot_idle_rmask")), 
						  new Sprite(gmain.getImages("obj/rightbot_idle_gmask")), 
						  new Sprite(gmain.getImages("obj/rightbot_idle_bmask")), 
						  new Sprite(gmain.getImages("obj/rightbot_idle_cmask")), 
						  new Sprite(gmain.getImages("obj/rightbot_idle_mmask")), 
						  new Sprite(gmain.getImages("obj/rightbot_idle_ymask")), 
						  new Sprite(gmain.getImages("obj/rightbot_move")), 
						  new Sprite(gmain.getImages("obj/rightbot_move_mask")), 
						  new Sprite(gmain.getImages("obj/rightbot_move_rmask")), 
						  new Sprite(gmain.getImages("obj/rightbot_move_gmask")), 
						  new Sprite(gmain.getImages("obj/rightbot_move_bmask")), 
						  new Sprite(gmain.getImages("obj/rightbot_move_cmask")), 
						  new Sprite(gmain.getImages("obj/rightbot_move_mmask")), 
						  new Sprite(gmain.getImages("obj/rightbot_move_ymask")));
		redBot.setTint(rph);
		
		vball = new VolteyBall(this);
		vball.setSprites(new Sprite(gmain.getImages("obj/volteyball")));
		
		objShadow = new Shadow[3];
		objShadow[0] = new Shadow(this, greenBot);
		objShadow[1] = new Shadow(this, redBot);
		objShadow[2] = new Shadow(this, vball);
		for (int i=0;i<objShadow.length;i++) objShadow[i].setSprites(new Sprite(gmain.getImages("misc/shadow")));
		
		netp = new NetPole(this);
		netp.setSprites(new Sprite(gmain.getImages("obj/pole")));
		
		bmark = new BallMarker(this);
		bmark.setSprites(new Sprite(gmain.getImages("misc/ballmarker")));
		
		sGUI = new SelectionGUI[3];
		sGUI[0] = new PauseGUI(this);
		sGUI[0].setOffset(290f, 260f);
		sGUI[0].setBackGUI(-1);
		sGUI[1] = new OptionsGUI(this);
		sGUI[1].setOffset(290f, 260f);
		sGUI[1].setBackGUI(0);
		sGUI[2] = new WonGUI(this);
		sGUI[2].setOffset(290f, 260f);
		sGUI[2].setBackGUI(-1);
		
		scrGUI = null;
	}
	public void initWorld() {
		initMatch();
	}
	public void initMatch() {
		initVariables();
		
		greenBot.init();
		redBot.init();
		vball.init();
		netp.init();
		bmark.init();
		
		for (int i=0;i<objShadow.length;i++) objShadow[i].init();
		
		changeGUI(-1);
		initRound(0);
	}
	public void initRound(int ps) {
		playerScored = -1;
		ballCounter = 0;
		lastBall = -1;
		ballCoolDown = 0;
		ballServer = ps;
		greenBot.setBot();
		redBot.setBot();
		vball.setBall(ballServer);
	}
	public void initVariables() {
		winPoint = defaultWinPoint;
		leftPoint = 0;
		rightPoint = 0;
		playerScored = -1;
		ballOwner = -1;
		ballServer = -1;
		ballCounter = 0;
		lastBall = -1;
		ballCoolDown = 0;
		gamePaused = false;
		gameWinner = -1;
	}
	
	public void pauseGame() {
		changeGUI(0);
		gamePaused = true;
		Game.playSFX("Click");
	}
	public void unpauseGame() {
		changeGUI(-1);
		gamePaused = false;
		Game.playSFX("Click");
	}
	public void togglePause() {
		if (gamePaused) unpauseGame();
		else pauseGame();
	}
	public boolean isPaused() {return gamePaused;}
	
	public boolean checkBounce(int ps) {
		if (lastBall == ps) return true;
		return false;
	}
	public void countBounce(int ps) {
		if (lastBall == ps) {
			ballCounter++;
			if (ballCounter >= 3) {
				ballCounter = 0;
				lastBall = -1;
				if (ps == 0) givePoint(1);
				else if (ps == 1) givePoint(0);
			}
		} else {
			ballCounter = 0;
			lastBall = ps;
		}
	}
	
	public void givePoint(int ps) {
		playerScored = ps;
		vball.phaseOut();
		ballCoolDown = 100;
	}
	public void submitPoint() {
		if (playerScored != -1) { 
			if (ballOwner == playerScored) {
				if (ballOwner == 0) {
					leftPoint++;
				} else if (ballOwner == 1) {
					rightPoint++;
				}
			} else {
				ballOwner = playerScored;
			}
			playerScored = -1;
			
			if (leftPoint < winPoint && rightPoint < winPoint) Game.playSFX("Whistle");
		}
	}
	public void checkWinner() {
		if (gameWinner == -1) {
			if (leftPoint == winPoint-1 && rightPoint == winPoint-1) winPoint++;
			
			if (leftPoint >= winPoint) giveWinner(0);
			else if (rightPoint >= winPoint) giveWinner(1);
		}
	}
	public void giveWinner(int ps) {
		changeGUI(2);
		gamePaused = true;
		gameWinner = ps;
		Game.playSFX("WhistleLong");
	}
	public int getWinner() {return gameWinner;}
	
	public int getLeftPoint() {return leftPoint;}
	public int getRightPoint() {return rightPoint;}
	public int getBallOwner() {return ballOwner;}
	public int getWinPoint() {return winPoint;}
	
	public int getBallServer() {return ballServer;}
	
	public VolteyBall getBall() {return vball;}
	public NetPole getNet() {return netp;}
	
	public void updateWorld(long gameTicker) {
		if (!gamePaused && !Game.isInputLocked()) {
			greenBot.update(gameTicker);
			redBot.update(gameTicker);
			vball.update(gameTicker);
			netp.update(gameTicker);
			bmark.update(gameTicker);
			bmark.setPos(vball.getX(), 12f);
			
			bgField.update(gameTicker);
			for (int i=0;i<objShadow.length;i++) objShadow[i].update(gameTicker);
			
			if (greenBot.collide(vball) && vball.isHitAble()) {
				vball.bounce(greenBot);
				greenBot.bounceFB();
			}
			if (redBot.collide(vball) && vball.isHitAble()) {
				vball.bounce(redBot);
				redBot.bounceFB();
			}
			if (netp.collide(vball)) {
				vball.bounce(netp);
			}
			
			if (ballCoolDown > 0) {
				ballCoolDown--;
				if (ballCoolDown == 50) {
					submitPoint();
				}
				if (ballCoolDown == 0) {
					if (ballOwner == 0 && greenBot.isJumping()) ballCoolDown = 1;
					else if (ballOwner == 1 && redBot.isJumping()) ballCoolDown = 1;
					else initRound(ballOwner);
				}
			}
			
			navGUI.update(gameTicker);
			
		} else if (scrGUI != null) {
			scrGUI.update(gameTicker);
		}
		
		checkWinner();
		if (gameWinner == -1) navGUI.updateNav(gameTicker);
	}
	
	public void drawWorld(Graphics2D g) {
		bgField.draw(g, 0, 0);
		
		for (int i=0;i<objShadow.length;i++) objShadow[i].draw(g);
		
		vball.draw(g);
		greenBot.draw(g);
		redBot.draw(g);
		netp.draw(g);
		
		bmark.draw(g);
		
		navGUI.draw(g);
		
		if (gamePaused && scrGUI != null) {
			g.setColor(new Color(0, 0, 64, 128));
			g.fillRect(0, 0, Game.SCR_WIDTH, Game.SCR_HEIGHT);
			scrGUI.draw(g);
		}
		
		navGUI.drawNav(g);
	}
	
}
