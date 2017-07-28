package zan.voltey.game;

import java.awt.Graphics2D;

import zan.voltey.gui.GameModeGUI;
import zan.voltey.gui.OptionsGUI;
import zan.voltey.gui.PlayerModeGUI;
import zan.voltey.gui.SelectionGUI;
import zan.voltey.gui.TitleGUI;
import zan.util.Sprite;
import zan.util.WorldFrame;

public class GameTitle extends WorldFrame {
	
	private Sprite bgTitle;
	
	private int gameModeSetting;
	private int leftPlayerTypeSetting;
	private int leftPlayerTintSetting;
	private int rightPlayerTypeSetting;
	private int rightPlayerTintSetting;
	
	public GameTitle(Game gm) {
		super(gm);
		
		bgTitle = new Sprite(gmain.getImages("bg/volteytitleblue"));
		bgTitle.setCentered(false);
		
		sGUI = new SelectionGUI[4];
		sGUI[0] = new TitleGUI(this);
		sGUI[1] = new GameModeGUI(this);
		sGUI[1].setBackGUI(0);
		sGUI[2] = new PlayerModeGUI(this);
		sGUI[2].setBackGUI(1);
		sGUI[3] = new OptionsGUI(this);
		sGUI[3].setOffset(300f, 320f);
		sGUI[3].setBackGUI(0);
		
		scrGUI = sGUI[0];
		
		gameModeSetting = 0;
		leftPlayerTypeSetting = 0;
		rightPlayerTypeSetting = 0;
		leftPlayerTintSetting = 100;
		rightPlayerTintSetting = 0;
	}
	public void initWorld() {
		for (int i=0;i<sGUI.length;i++) sGUI[i].init();
		scrGUI = sGUI[0];
	}
	
	public void modifyPlayer(int mp) {
		sGUI[2].setModifiedPlayer(mp);
		changeGUI(2);
	}
	
	public void setGameFrame() {
		gmain.setGameFrame(gameModeSetting, 
						   leftPlayerTypeSetting, 
						   leftPlayerTintSetting, 
						   rightPlayerTypeSetting, 
						   rightPlayerTintSetting);
	}
	
	public void setGameMode(int s) {gameModeSetting = s;}
	public int getGameMode() {return gameModeSetting;}
	public void setLeftPlayerType(int s) {leftPlayerTypeSetting = s;}
	public void setRightPlayerType(int s) {rightPlayerTypeSetting = s;}
	public int getLeftPlayerType() {return leftPlayerTypeSetting;}
	public int getRightPlayerType() {return rightPlayerTypeSetting;}
	public void setLeftPlayerTint(int s) {
		leftPlayerTintSetting = s;
		if (leftPlayerTintSetting > 300) leftPlayerTintSetting = 300;
		if (leftPlayerTintSetting < 0) leftPlayerTintSetting = 0;
	}
	public void setRightPlayerTint(int s) {
		rightPlayerTintSetting = s;
		if (rightPlayerTintSetting > 300) rightPlayerTintSetting = 300;
		if (rightPlayerTintSetting < 0) rightPlayerTintSetting = 0;
	}
	public void changeLeftPlayerTint(int s) {
		leftPlayerTintSetting += s;
		if (leftPlayerTintSetting > 300) leftPlayerTintSetting = 300;
		if (leftPlayerTintSetting < 0) leftPlayerTintSetting = 0;
	}
	public void changeRightPlayerTint(int s) {
		rightPlayerTintSetting += s;
		if (rightPlayerTintSetting > 300) rightPlayerTintSetting = 300;
		if (rightPlayerTintSetting < 0) rightPlayerTintSetting = 0;
	}
	public int getLeftPlayerTint() {return leftPlayerTintSetting;}
	public int getRightPlayerTint() {return rightPlayerTintSetting;}
	
	public void updateWorld(long gameTicker) {
		bgTitle.update(gameTicker);
		
		if (scrGUI != null) scrGUI.update(gameTicker);
	}
	
	public void drawWorld(Graphics2D g) {
		bgTitle.draw(g, 0, 0);
		
		if (scrGUI != null) scrGUI.draw(g);
	}
}
