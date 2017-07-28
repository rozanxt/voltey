package zan.util;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.util.ArrayList;

public class Sprite {
	
	private GraphicsConfiguration gc;
	
	protected BufferedImage[] spriteImage;
	protected int spriteWidth;
	protected int spriteHeight;
	protected int numFrames;
	protected int curFrame;
	
	protected boolean animated;
	protected boolean backAni;
	protected long frameTicker;
	protected int framePeriod;
	
	protected boolean centered;
	protected float alpha;
	protected int flip;					// 0 = No Flip; 1 = Vertical; 2 = Horizontal; 3 = Double;
	protected float angle;
	protected float scalew;
	protected float scaleh;
	
	public void initSprite() {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	    gc = ge.getDefaultScreenDevice().getDefaultConfiguration();
	    
	    spriteImage = null;
	    spriteWidth = 1;
	    spriteHeight = 1;
	    numFrames = 0;
	    curFrame = 0;
	    animated = false;
		backAni = false;
		frameTicker = 0L;
		framePeriod = 0;
		
		restoreSprite();
	}
	public Sprite(BufferedImage img) {
		initSprite();
		if (img != null) {
			numFrames = 1;
			spriteImage = new BufferedImage[numFrames];
			setSprite(0, img);
		} else setEmptySprite();
	}
	public Sprite(ArrayList<BufferedImage> img) {
		initSprite();
		if (img != null) {
			numFrames = img.size();
			spriteImage = new BufferedImage[numFrames];
			for (int i=0;i<numFrames;i++) setSprite(i, img.get(i));
		} else setEmptySprite();
	}
	public Sprite(int nf) {
		initSprite();
		if (nf > 0) numFrames = nf;
		else numFrames = 1;
		spriteImage = new BufferedImage[numFrames];
		for (int i=0;i<numFrames;i++) spriteImage[i] = null;
	}
	public void restoreSprite() {
		centered = true;
		alpha = 1.0f;
		flip = 0;
		angle = 0.0f;
		scalew = 1.0f;
		scaleh = 1.0f;
	}
	
	public void setSprite(int sf, BufferedImage img) {
		if (sf >= 0 && sf < numFrames && img != null) {
			spriteImage[sf] = img;
			if (sf == 0) {
				spriteWidth = spriteImage[0].getWidth();
				spriteHeight = spriteImage[0].getHeight();
			}
		}
	}
	public void setEmptySprite() {
		numFrames = 1;
		spriteImage = new BufferedImage[numFrames];
		spriteImage[0] = null;
	}
	
	public void update(long gameTicker) {
		if (animated) {
			if (gameTicker > frameTicker + framePeriod) {
				frameTicker = gameTicker;
				if (backAni) curFrame--;
				else curFrame++;
				if (curFrame < 0) curFrame = numFrames + curFrame;
				if (curFrame >= numFrames) curFrame = curFrame % numFrames;
			}
		}
	}
	
	public void setAnimation(boolean sa, float fps) {
		if (sa) framePeriod = (int)(1000.0 / fps);
		else framePeriod = 0;
		frameTicker = 0L;
		animated = sa;
	}
	public void setBackAnimation(boolean sa) {
		backAni = sa;
	}
	
	public void setFrame(int sf) {curFrame = sf;}
	public void setFlip(int sf) {flip = sf;}
	public void setAngle(float sa) {angle = sa;}
	public void setAlpha(float sa) {alpha = sa;}
	public void setCentered(boolean sc) {centered = sc;}
	public void setScale(float ss) {
		scalew = ss;
		scaleh = ss;
	}
	
	public void draw(Graphics2D g, float sx, float sy) {
		draw(g, sx, sy, curFrame);
	}
	private void draw(Graphics2D g, float sx, float sy, int cf) {
		Composite c = g.getComposite();
		
		int frame = cf;
		if (frame < 0) frame = 0;
		if (frame >= numFrames) frame = frame % numFrames;
		if (spriteImage[frame] == null) return;
		BufferedImage dest = spriteImage[frame];
		
		int ix, iy;
		if (centered) {
			ix = Math.round(sx-(getWidth()/2.0f));
			iy = Math.round(sy-(getHeight()/2.0f));
		} else {
			ix = Math.round(sx);
			iy = Math.round(sy);
		}
		
		if (flip > 0 && flip <= 3) dest = flipImage(dest, flip-1);
		if (angle != 0.0f) dest = rotateImage(dest);
		
		if (alpha >= 0.0f && alpha < 1.0f) {
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha)); 
		}
		
		if (dest != null) {
			if (scalew != 1.0f || scaleh != 1.0f) {
				int destWidth = (int) (dest.getWidth() * scalew);
			    int destHeight = (int) (dest.getHeight() * scaleh);
			    
			    int destX = ix + dest.getWidth()/2 - destWidth/2;
			    int destY = iy + dest.getHeight()/2 - destHeight/2;
			    
			    g.drawImage(dest, destX, destY, destWidth, destHeight, null);
			}
			else g.drawImage(dest, ix, iy, null);
		}
		
		g.setComposite(c);
	}
	
	private BufferedImage flipImage(BufferedImage src, int flipKind) {
		if (src == null) return null;
		
		int imWidth = src.getWidth();
		int imHeight = src.getHeight();
		int transparency = src.getColorModel().getTransparency();
		
		BufferedImage dest =  gc.createCompatibleImage(imWidth, imHeight, transparency);
		Graphics2D g = dest.createGraphics();
		
		if (flipKind == 0)
			g.drawImage(src, imWidth, 0,  0, imHeight, 0, 0,  imWidth, imHeight, null);
		else if (flipKind == 1)
			g.drawImage(src, 0, imHeight,  imWidth, 0, 0, 0,  imWidth, imHeight, null);
		else if (flipKind == 2)
			g.drawImage(src, imWidth, imHeight,  0, 0, 0, 0,  imWidth, imHeight, null);
		g.dispose();
		
		return dest; 
	}
	private BufferedImage rotateImage(BufferedImage src) {
		if (src == null) return null;
		
		int transparency = src.getColorModel().getTransparency();
		BufferedImage dest = gc.createCompatibleImage(src.getWidth(), src.getHeight(), transparency);
		Graphics2D g = dest.createGraphics();
		
		AffineTransform at = g.getTransform();
		
		AffineTransform rot = new AffineTransform(); 
		rot.rotate(Math.toRadians(angle), src.getWidth()/2, src.getHeight()/2); 
		g.transform(rot); 
		
		g.drawImage(src, 0, 0, null);
		    
		g.setTransform(at);
		g.dispose();
		
		return dest; 
	}
	
	public BufferedImage getSprite() {return spriteImage[0];}
	public BufferedImage getSprite(int sf) {return spriteImage[sf];}
	public int getWidth() {return spriteWidth;}
	public int getHeight() {return spriteHeight;}
	public int getNumFrames() {return numFrames;}
	public int getCurFrame() {return curFrame;}
	
}
