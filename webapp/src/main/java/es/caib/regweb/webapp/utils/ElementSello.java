package es.caib.regweb.webapp.utils;

import org.apache.log4j.Logger;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.BaseFont;

public class ElementSello {
    	
	protected final Logger log = Logger.getLogger(getClass());
	
	// Propietats estàtiques per a calcular paràmetres per defecte
	public static float posxRefLinia = 0;
	public static float posxRef = 0;
	public static float posyRef = 0;
	public static float fontSizeRef = 10f;
	public static int fontStyleRef = Font.NORMAL;
	public static BaseColor colorRef = BaseColor.BLACK;
	public static float maxPosx = 0;
	public static float maxPosy = 0;
	
	// Propietats de l'element del Segell
	private String param;
	private String text;
	private Float posx;
	private Float posy;
	private Integer ample;
	private Integer alineacio = Element.ALIGN_LEFT;
	private float fontSize;
	private int fontStyle;
	private BaseColor color;
	private BaseFont bf;
	private boolean changedPosy = false;
	
	public ElementSello() throws Exception {
		bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.EMBEDDED);
		this.text = "";
		this.fontSize = fontSizeRef;
		this.fontStyle = fontStyleRef;
		this.posx = posxRef;
		this.posy = posyRef - getAltFila();
		maxPosy = Math.min(maxPosy, this.posy);
		posyRef = this.posy;
		this.color = colorRef;
	}
	
	public ElementSello(String texte, boolean param) throws Exception {
		bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.EMBEDDED);
		this.text = "";
		this.fontSize = fontSizeRef;
		this.fontStyle = fontStyleRef;
		this.posx = posxRef;
		this.color = colorRef;
		
		if (!param) {
			this.setText(texte);
		} else {
			String[] params = texte.split(",");
			this.param = params[0];
	
			for (int i = 1; i < params.length; i++) {
				String[] parametre = params[i].split(":");
				String paramName = parametre[0].trim();
				String paramValue = parametre[1].trim();
				
				if ("posx".equalsIgnoreCase(paramName)) {
					this.posx = Float.parseFloat(paramValue);
					// Si es defineix una nova posició x,
					// aquesta posició serà el nou origen del text.
					posxRefLinia = this.posx;
					posxRef = this.posx;
				} else if ("posy".equalsIgnoreCase(paramName)) {
					this.posy = -Float.parseFloat(paramValue);
					posyRef = this.posy;
					// Si es defineix una nova posició y,
					// aquesta posició serà el nou origen del text.
					changedPosy = true;
				} else if ("font".equalsIgnoreCase(paramName)) {
					this.fontSize = Integer.parseInt(paramValue);
					fontSizeRef = this.fontSize;
				} else if ("estil".equalsIgnoreCase(paramName)) {
					String[] estils = paramValue.split("\\|");
					int style = Font.NORMAL;
					for(String estil: estils) {
						if ("BOLD".equals(estil)) {
							style |= Font.BOLD;
						} else if ("ITALIC".equals(estil)) {
							style |= Font.ITALIC;
						} else if ("UNDERLINE".equals(estil)) {
							style |= Font.UNDERLINE;
						}
					}
					this.fontStyle = style;
					fontStyleRef = this.fontStyle;
				} else if ("ample".equalsIgnoreCase(paramName)) {
					this.ample = Integer.parseInt(paramValue);
				} else if ("color".equalsIgnoreCase(paramName)) {
					String strColor = paramValue.toUpperCase();
					if (strColor.startsWith("RGB(")) {
						String[] rgb = paramValue.substring(4, strColor.length() - 1).split(",");
						this.color = new BaseColor(
								Integer.parseInt(rgb[0]));
					} else if ("BLACK".equals(strColor)) {
						this.color = BaseColor.BLACK;
					} else if ("BLUE".equals(strColor)) {
						this.color = BaseColor.BLUE;
					} else if ("CYAN".equals(strColor)) {
						this.color = BaseColor.CYAN;
					} else if ("DARK_GRAY".equals(strColor)) {
						this.color = BaseColor.DARK_GRAY;
					} else if ("GRAY".equals(strColor)) {
						this.color = BaseColor.GRAY;
					} else if ("GREEN".equals(strColor)) {
						this.color = BaseColor.GREEN;
					} else if ("LIGHT_GRAY".equals(strColor)) {
						this.color = BaseColor.LIGHT_GRAY;
					} else if ("MAGENTA".equals(strColor)) {
						this.color = BaseColor.MAGENTA;
					} else if ("ORANGE".equals(strColor)) {
						this.color = BaseColor.ORANGE;
					} else if ("PINK".equals(strColor)) {
						this.color = BaseColor.PINK;
					} else if ("RED".equals(strColor)) {
						this.color = BaseColor.RED;
					} else if ("WHITE".equals(strColor)) {
						this.color = BaseColor.WHITE;
					} else if ("YELLOW".equals(strColor)) {
						this.color = BaseColor.YELLOW;
					}
					colorRef = this.color;
				} else if("alineacio".equalsIgnoreCase(paramName)) {
					String strAlign = paramValue.toUpperCase();
					if (strAlign.equalsIgnoreCase("LEFT")) {
						alineacio = Element.ALIGN_LEFT;
					} else if (strAlign.equalsIgnoreCase("RIGHT")) {
						alineacio = Element.ALIGN_RIGHT;
					} else if (strAlign.equalsIgnoreCase("CENTER")) {
						alineacio = Element.ALIGN_CENTER;
					} else if (strAlign.equalsIgnoreCase("JUSTIFIED")) {
						alineacio = Element.ALIGN_JUSTIFIED;
					}
				}
			}
		}
	}

	public String getParam() {
		return param;
	}

	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
		if (this.ample != null) {
			this.fontSize = getMaxFontSize(bf, text, this.ample, this.fontSize);
		}
		posxRef += bf.getWidthPoint(text, this.fontSize);
		maxPosx = Math.max(maxPosx, posxRef);
	}
	
	public float getAltText() {
		String txt = "".equals(this.text) ? "dp" : this.text;
	    float ascend = bf.getAscentPoint(txt, this.fontSize); 
	    float descend = bf.getDescentPoint(txt, this.fontSize); 
	    return ascend - descend; 
	}
	
	public float getAltFila() {
		return Math.round(this.fontSize * 1.2);
	}
	
	public Float getPosx() {
		return posx;
	}
	public void setPosx(Float posx) {
		this.posx = posx;
	}

	public Float getPosy() {
		return posy;
	}
	public void setPosy(Float posy) {
		this.posy = posy;
		maxPosy = Math.min(maxPosy, this.posy);
	}

	public Integer getAmple() {
		return ample;
	}
	public void setAmple(Integer ample) {
		this.ample = ample;
	}

	public float getFontSize() {
		return fontSize;
	}
	public void setFontSize(float fontSize) {
		this.fontSize = fontSize;
	}

	public BaseColor getColor() {
		return color;
	}
	public void setColor(BaseColor color) {
		this.color = color;
	}
	
	public BaseFont getBf() {
		return bf;
	}

	public int getFontStyle() {
		return fontStyle;
	}

	public Integer getAlineacio() {
		return alineacio;
	}

	private static float getMaxFontSize(BaseFont bf, String text, int ample, float maxSize){
	    if(text == null || "".equals(text)){
	        return fontSizeRef;
	    }

	    float fontSize = maxSize;
	    while(bf.getWidthPoint(text, fontSize) > ample){
	        fontSize -= 0.1f;
	    }

	    return fontSize;
	}

	public boolean isChangedPosy() {
		return changedPosy;
	}

	public static void clear() {
		posxRefLinia = 0;
		posxRef = 0;
		posyRef = 0;
		fontSizeRef = 10f;
		fontStyleRef = Font.NORMAL;
		colorRef = BaseColor.BLACK;
		maxPosx = 0;
		maxPosy = 0;
	}
}
