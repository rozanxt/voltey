package zan.voltey.game;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.*;

import org.lwjgl.openal.AL;
import paulscode.sound.SoundSystem;
import paulscode.sound.SoundSystemConfig;
import paulscode.sound.SoundSystemException;
import paulscode.sound.SoundSystemJPCT;
import paulscode.sound.libraries.LibraryJavaSound;
import paulscode.sound.libraries.LibraryLWJGLOpenAL;
import paulscode.sound.codecs.CodecJOrbis;

import zan.voltey.effect.FadeEffect;
import zan.util.*;

public class Game extends Canvas {
	private static final long serialVersionUID = 1L;
	
	public static final int SCR_WIDTH = 800;
	public static final int SCR_HEIGHT = 600;
	
	public static final int PERIOD = 1000 / 50;			// 1000 / FRAMES PER SECOND
	private static final int MAX_FRAME_SKIPS = 5;
	private static final int NO_DELAYS_PER_YIELD = 16;
	
	private GraphicsDevice gd;
	private DisplayMode defaultDispMode;
	private BufferStrategy strategy;
	
	private static boolean gameRunning = true;
	private static boolean fullScreen = false;
	private static boolean timedUpdate;
	private int fullScreenDelay = 0;
	
	private ImagesLoader imgLoader;
	private static InputManager inputMan;
	private static FontManager fontMan;
	private static SoundSystem sndSystem;
	private static float sfxVolume;
	private static float bgmVolume;
	
	private FadeEffect fadeFX;
	private static Cursor defaultCursor;
	private static Cursor hoveredCursor;
	private static Cursor clickedCursor;
	private static Cursor blankCursor;
	private static int currentCursor;
	private static int mouseHideCounter;
	private static final int mouseHideLimit = 500;
	
	private WorldFrame scrFrame;
	private GameTitle gameTitle;
	private GameWorld gameWorld;
	
	public static Random rnd;
	
	private static JFrame container;
	private static JPanel panel;
	
	private Game() {
		container = new JFrame("Voltey 0.3.9");
		
		panel = (JPanel) container.getContentPane();
		panel.setPreferredSize(new Dimension(SCR_WIDTH, SCR_HEIGHT));
		panel.setLayout(null);
		panel.add(this);
		
		this.setBounds(0, 0, SCR_WIDTH, SCR_HEIGHT);
		
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		gd = ge.getDefaultScreenDevice();
		defaultDispMode = gd.getDisplayMode();
		
		container.setIgnoreRepaint(true);
		container.setResizable(false);
		container.pack();
		
		container.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				quit();
			}
		});
		
		this.createBufferStrategy(2);
		strategy = getBufferStrategy();
		this.setIgnoreRepaint(true);
		this.requestFocus();
		container.setVisible(true);
		
		addKeyListener(new KeyInputHandler());
		addMouseListener(new MouseInputHandler());
		addMouseMotionListener(new MouseMotionInputHandler());
		
		rnd = new Random();
		
		initInput();
		initCursor();
		initFonts();
		initImages();
		initSounds();
		initMisc();
		
		initGame();
	}
	
	private boolean isDisplayModeAvailable(int width, int height, int bitdepth) {
		DisplayMode[] modes = gd.getDisplayModes();
		for (int i=0;i<modes.length;i++) {
			if (width == modes[i].getWidth() && height == modes[i].getHeight() && bitdepth == modes[i].getBitDepth()) return true;
		}
		return false;
	}
	private void setDisplayMode(int width, int height, int bitdepth) {
		if (!gd.isDisplayChangeSupported()) {
			System.out.println("Display mode changing not supported");
			return;
		}
		if (!isDisplayModeAvailable(width, height, bitdepth)) {
			System.out.println("Display mode (" + width + ", " + height + ", " + bitdepth + ") not available");
			return;
		}
		DisplayMode dm = new DisplayMode(width, height, bitdepth, DisplayMode.REFRESH_RATE_UNKNOWN);
		
		try {
			gd.setDisplayMode(dm);
			System.out.println("Display mode set to: (" + width + ", " + height + ", " + bitdepth + ")");
		} catch(IllegalArgumentException e) {
			System.out.println("Error setting Display mode (" + width + ", " + height + ", " + bitdepth + ")");
		}
		
		try {Thread.sleep(1000);} catch(InterruptedException ex) {}
	}
	private void setFullScreen(boolean fs) {
		if (fullScreen != fs) {
			fullScreen = fs;
			
			if (fullScreen) {
				container.setVisible(false);
				container.dispose();
				container.setUndecorated(true);
				
				if (!gd.isFullScreenSupported()) {
					System.out.println("Full-screen exclusive mode not supported");
					System.exit(0);
				}
				gd.setFullScreenWindow(container);
				
				setDisplayMode(SCR_WIDTH, SCR_HEIGHT, 32);
				
				container.setVisible(true);
				this.requestFocus();
			} else {
				gd.setDisplayMode(defaultDispMode);
				
				container.setVisible(false);
				container.dispose();
				container.setUndecorated(false);
				
				gd.setFullScreenWindow(null);
				
				container.setSize(SCR_WIDTH, SCR_HEIGHT);
				container.setLocationRelativeTo(null);
				container.setVisible(true);
				this.requestFocus();
			}
		}
	}
	public void toggleFullScreen() {
		if (fullScreenDelay == 0) {
			if (!fullScreen) {
				setFullScreen(true);
			} else {
				setFullScreen(false);
			}
			fullScreenDelay = 10;
		}
	}
	public static boolean isFullScreen() {
		return fullScreen;
	}
	
	public static void stopGame() {
		gameRunning = false;
	}
	private static void quit() {
		sndSystem.cleanup();
		AL.destroy();
		System.exit(0);
	}
	
	private void initInput() {
		inputMan = new InputManager();
	}
	private void initCursor() {
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Image cimg;
		cimg = toolkit.getImage("res/img/misc/volteycursor.gif");
		defaultCursor = toolkit.createCustomCursor(cimg, new Point(panel.getX(), panel.getY()), "defcursor");
		cimg = toolkit.getImage("res/img/misc/volteycursorhover.gif");
		hoveredCursor = toolkit.createCustomCursor(cimg, new Point(panel.getX(), panel.getY()), "hovercursor");
		cimg = toolkit.getImage("res/img/misc/volteycursorclick.gif");
		clickedCursor = toolkit.createCustomCursor(cimg, new Point(panel.getX(), panel.getY()), "clickcursor");
		BufferedImage bcimg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		blankCursor = toolkit.createCustomCursor(bcimg, new Point(0, 0), "blank cursor");
		
		panel.setCursor(defaultCursor);
		
		mouseHideCounter = 0;
	}
	private void initFonts() {
		fontMan = new FontManager();
	}
	private void initImages() {
		imgLoader = new ImagesLoader("imgInfo.txt");
	}
	private void initSounds() {
		try {
			SoundSystemConfig.addLibrary(LibraryLWJGLOpenAL.class);
			SoundSystemConfig.addLibrary(LibraryJavaSound.class);
			SoundSystemConfig.setCodec("wav", CodecJOrbis.class);
			SoundSystemConfig.setCodec("ogg", CodecJOrbis.class);
		} catch(SoundSystemException e) {
		    System.err.println("Error linking with sound plug-ins");
		}
		sndSystem = new SoundSystemJPCT();
		
		try {
			URL url;
			
			url = new File("res/snd/misc/click.wav").toURI().toURL();
			sndSystem.newSource(false, "Click", url, "click.wav", false, 0f, 0f, 0f, SoundSystemConfig.ATTENUATION_ROLLOFF, SoundSystemConfig.getDefaultRolloff());
			url = new File("res/snd/sfx/volteyhit.wav").toURI().toURL();
			sndSystem.newSource(false, "VolteyHit", url, "volteyhit.wav", false, 0f, 0f, 0f, SoundSystemConfig.ATTENUATION_ROLLOFF, SoundSystemConfig.getDefaultRolloff());
			url = new File("res/snd/sfx/whistle2.wav").toURI().toURL();
			sndSystem.newSource(false, "Whistle", url, "whistle2.wav", false, 0f, 0f, 0f, SoundSystemConfig.ATTENUATION_ROLLOFF, SoundSystemConfig.getDefaultRolloff());
			url = new File("res/snd/sfx/whistlelong.wav").toURI().toURL();
			sndSystem.newSource(false, "WhistleLong", url, "whistlelong.wav", false, 0f, 0f, 0f, SoundSystemConfig.ATTENUATION_ROLLOFF, SoundSystemConfig.getDefaultRolloff());
			
			url = new File("res/snd/bgm/spacerock.ogg").toURI().toURL();
			sndSystem.newStreamingSource(true, "SpaceRockBGM", url, "spacerock.ogg", true, 0f, 0f, 0f, SoundSystemConfig.ATTENUATION_ROLLOFF, SoundSystemConfig.getDefaultRolloff());
			sndSystem.play("SpaceRockBGM");
		} catch(Exception e) {
			System.err.println("Error loading sound resources");
		}
		
		bgmVolume = 1.0f;
		sfxVolume = 1.0f;
	}
	private void initMisc() {
		fadeFX = new FadeEffect(this);
		timedUpdate = true;
	}
	private void initGame() {
		gameTitle = new GameTitle(this);
		gameTitle.initWorld();
		scrFrame = gameTitle;
	}
	
	public void setTitleFrame() {
		if (gameTitle == null) gameTitle = new GameTitle(this);
		gameTitle.initWorld();
		fadeFX.fade(gameTitle);
	}
	public void setGameFrame(int gms, int lpt, int lph, int rpt, int rph) {
		gameWorld = new GameWorld(this, gms, lpt, lph, rpt, rph);
		gameWorld.initWorld();
		fadeFX.fade(gameWorld);
	}
	public void setScreenFrame(WorldFrame wf) {
		scrFrame = wf;
	}
	
	private void startGame() {
		gameLoop();
	}
	private void gameLoop() {
		long beforeTime, afterTime, timeDiff, sleepTime;
		long overSleepTime = 0L;
		long excess = 0L;
		int noDelays = 0;
		
		while (gameRunning) {
			beforeTime = System.currentTimeMillis();
			
			inputMan.updateInput();
			updateGame(beforeTime);
			renderGame();
			
			afterTime = System.currentTimeMillis();
			timeDiff = afterTime - beforeTime;
			sleepTime = (PERIOD - timeDiff) - overSleepTime;
			
			if (sleepTime > 0) {
				try {Thread.sleep(sleepTime);} catch (Exception e) {}
				overSleepTime = (System.currentTimeMillis() - afterTime) - sleepTime;
			} else {
				excess -= sleepTime;
				overSleepTime = 0L;
				if (++noDelays >= NO_DELAYS_PER_YIELD) {
					Thread.yield();
					noDelays = 0;
				}
			}
			
			beforeTime = System.currentTimeMillis();
			
			int skips = 0;
			while (excess > PERIOD && skips < MAX_FRAME_SKIPS) {
				excess -= PERIOD;
				if (timedUpdate) updateGame(beforeTime);
				skips++;
			}
		}
		
		quit();
	}
	
	private void updateGame(long gameTicker) {
		if (scrFrame != null) scrFrame.updateWorld(gameTicker);
		fadeFX.update(gameTicker);
		
		if (getKeyPress(IK.F11)) toggleFullScreen();
		if (fullScreenDelay > 0) fullScreenDelay--;
		
		if (isMouseMoving() || getMouseL() || getMouseR()) {
			mouseHideCounter = 0;
		} else {
			if (mouseHideCounter < 500) mouseHideCounter++;
		}
	}
	
	private void renderGame() {
		Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.setColor(Color.black);
		g.fillRect(0, 0, SCR_WIDTH, SCR_HEIGHT);
		
		if (scrFrame != null) scrFrame.drawWorld(g);
		fadeFX.draw(g);
		
		g.dispose();
		strategy.show();
	}
	
	private class KeyInputHandler extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_UP) {inputMan.setKey(IK.UP, true);}
			if (e.getKeyCode() == KeyEvent.VK_DOWN) {inputMan.setKey(IK.DOWN, true);}
			if (e.getKeyCode() == KeyEvent.VK_LEFT) {inputMan.setKey(IK.LEFT, true);}
			if (e.getKeyCode() == KeyEvent.VK_RIGHT) {inputMan.setKey(IK.RIGHT, true);}
			if (e.getKeyCode() == KeyEvent.VK_W) {inputMan.setKey(IK.W, true);}
			if (e.getKeyCode() == KeyEvent.VK_S) {inputMan.setKey(IK.S, true);}
			if (e.getKeyCode() == KeyEvent.VK_A) {inputMan.setKey(IK.A, true);}
			if (e.getKeyCode() == KeyEvent.VK_D) {inputMan.setKey(IK.D, true);}
			if (e.getKeyCode() == KeyEvent.VK_P) {inputMan.setKey(IK.P, true);}
			if (e.getKeyCode() == KeyEvent.VK_SPACE) {inputMan.setKey(IK.SPACE, true);}
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {inputMan.setKey(IK.ENTER, true);}
			if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {inputMan.setKey(IK.ESC, true);}
			if (e.getKeyCode() == KeyEvent.VK_F11) {inputMan.setKey(IK.F11, true);}
		} 
		public void keyReleased(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_UP) {inputMan.setKey(IK.UP, false);}
			if (e.getKeyCode() == KeyEvent.VK_DOWN) {inputMan.setKey(IK.DOWN, false);}
			if (e.getKeyCode() == KeyEvent.VK_LEFT) {inputMan.setKey(IK.LEFT, false);}
			if (e.getKeyCode() == KeyEvent.VK_RIGHT) {inputMan.setKey(IK.RIGHT, false);}
			if (e.getKeyCode() == KeyEvent.VK_W) {inputMan.setKey(IK.W, false);}
			if (e.getKeyCode() == KeyEvent.VK_S) {inputMan.setKey(IK.S, false);}
			if (e.getKeyCode() == KeyEvent.VK_A) {inputMan.setKey(IK.A, false);}
			if (e.getKeyCode() == KeyEvent.VK_D) {inputMan.setKey(IK.D, false);}
			if (e.getKeyCode() == KeyEvent.VK_P) {inputMan.setKey(IK.P, false);}
			if (e.getKeyCode() == KeyEvent.VK_SPACE) {inputMan.setKey(IK.SPACE, false);}
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {inputMan.setKey(IK.ENTER, false);}
			if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {inputMan.setKey(IK.ESC, false);}
			if (e.getKeyCode() == KeyEvent.VK_F11) {inputMan.setKey(IK.F11, false);}
		}
		public void keyTyped(KeyEvent e) {
			
		}
	}
	
	private class MouseInputHandler extends MouseAdapter {
		public void mousePressed(MouseEvent e) {
			inputMan.setMousePos(e.getX(), e.getY());
			if (e.getButton() == MouseEvent.BUTTON1) inputMan.setMouseL(true);
	        if (e.getButton() == MouseEvent.BUTTON3) inputMan.setMouseR(true);
	        inputMan.setFixedPos(e.getX(), e.getY());
		}
		public void mouseReleased(MouseEvent e) {
			inputMan.setMousePos(e.getX(), e.getY());
			if (e.getButton() == MouseEvent.BUTTON1) inputMan.setMouseL(false);
			if (e.getButton() == MouseEvent.BUTTON3) inputMan.setMouseR(false);
			inputMan.releaseFixedPos();
		}
	}
	private class MouseMotionInputHandler extends MouseMotionAdapter {
		public void mouseMoved(MouseEvent e) {
			inputMan.setMousePos(e.getX(), e.getY());
		}
		public void mouseDragged(MouseEvent e) {
			inputMan.setMousePos(e.getX(), e.getY());
		}
	}
	
	public static void changeCursor(int c) {
		int cursor = 0;
		if (getMouseL()) cursor = 2;
		else if (mouseHideCounter >= mouseHideLimit ) cursor = -1;
		else cursor = c;
		
		if (currentCursor != cursor) {
			currentCursor = cursor;
			if (currentCursor == -1) panel.setCursor(blankCursor);
			if (currentCursor == 0) panel.setCursor(defaultCursor);
			if (currentCursor == 1) panel.setCursor(hoveredCursor);
			if (currentCursor == 2) panel.setCursor(clickedCursor);
		}
	}
	
	public ArrayList<BufferedImage> getImages(String s) {return imgLoader.getImages(s);}
	
	public void setTimedUpdate(boolean s) {timedUpdate = s;}
	
	public void lockInput() {inputMan.lockInput();}
	public void unlockInput() {inputMan.unlockInput();}
	public static boolean isInputLocked() {return inputMan.isInputLocked();}
	
	public static boolean getKey(IK k) {return inputMan.getKey(k);}
	public static boolean getKeyBump(IK k) {return inputMan.getKeyBump(k);}
	public static boolean getKeyPress(IK k) {return inputMan.getKeyPress(k);}
	
	public static boolean getMouseL() {return inputMan.getMouseL();}
	public static boolean getMouseR() {return inputMan.getMouseR();}
	public static boolean getClickL() {return inputMan.getClickL();}
	public static boolean getClickR() {return inputMan.getClickR();}
	public static boolean isMouseMoving() {return inputMan.isMouseMoving();}
	public static int getMouseX() {return inputMan.getMouseX();}
	public static int getMouseY() {return inputMan.getMouseY();}
	public static int getFixedX() {return inputMan.getFixedX();}
	public static int getFixedY() {return inputMan.getFixedY();}
	
	public static void playSFX(String s) {
		if (sndSystem.playing(s)) sndSystem.stop(s);
		sndSystem.play(s);
	}
	
	public static void setSfxVolume(float v) {
		sfxVolume = v;
		if (sfxVolume > 1.0f) sfxVolume = 1.0f;
		if (sfxVolume < 0.0f) sfxVolume = 0.0f;
		
		sndSystem.setVolume("Click", sfxVolume);
		sndSystem.setVolume("VolteyHit", sfxVolume);
		sndSystem.setVolume("Whistle", sfxVolume);
		sndSystem.setVolume("WhistleLong", sfxVolume);
		
	}
	public static void changeSfxVolume(float v) {
		sfxVolume += v;
		if (sfxVolume > 1.0f) sfxVolume = 1.0f;
		if (sfxVolume < 0.0f) sfxVolume = 0.0f;
		
		sndSystem.setVolume("Click", sfxVolume);
		sndSystem.setVolume("VolteyHit", sfxVolume);
		sndSystem.setVolume("Whistle", sfxVolume);
		sndSystem.setVolume("WhistleLong", sfxVolume);
		
	}
	public static float getSfxVolume() {return sfxVolume;}
	
	public static void setBgmVolume(float v) {
		bgmVolume = v;
		if (bgmVolume > 1.0f) bgmVolume = 1.0f;
		if (bgmVolume < 0.0f) bgmVolume = 0.0f;
		
		sndSystem.setVolume("SpaceRockBGM", bgmVolume);
		
	}
	public static void changeBgmVolume(float v) {
		bgmVolume += v;
		if (bgmVolume > 1.0f) bgmVolume = 1.0f;
		if (bgmVolume < 0.0f) bgmVolume = 0.0f;
		
		sndSystem.setVolume("SpaceRockBGM", bgmVolume);
		
	}
	public static float getBgmVolume() {return bgmVolume;}
	
	public static Font getFont(int s) {return fontMan.getFont(s);}
	
	
	public static void main(String argv[]) {
		Game g = new Game();
		g.startGame();
	}
}
