package zan.util;

import java.awt.Font;

public class FontManager {
	
	private Font[] fonts;
	
	public FontManager() {
		fonts = new Font[4];
		fonts[0] = new Font("Arial", Font.PLAIN, 16);
		fonts[1] = new Font("Showcard Gothic", Font.PLAIN, 48);
		fonts[2] = new Font("Showcard Gothic", Font.PLAIN, 32);
		fonts[3] = new Font("Showcard Gothic", Font.PLAIN, 18);
	}
	
	public Font getFont(int s) {
		if (s >= 0 && s < fonts.length) return fonts[s];
		return null;
	}
}
