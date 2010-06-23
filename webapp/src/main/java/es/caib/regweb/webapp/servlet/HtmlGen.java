//
// Utilidades Genericas para HTML
// 
package es.caib.regweb.webapp.servlet;

public final class HtmlGen {
    
    public HtmlGen() {
    }
    
    public static final String toHtml(String s) {
        return toHtml(s, true);
    }
    
    public static final String toHtmlWithTag(String s) {
        return toHtml(s, false);
    }
    
    public static final String toHtml(String s, boolean bRemoveTag) {
        StringBuffer s2 = null;
        if(s != null) {
            int l = s.length();
            s2 = new StringBuffer(l);
            for(int i = 0; i < l; i++) {
                char c = s.charAt(i);
                int value = c;
                if(value > 47 && value < 60 || value > 64 && value < 91 || value > 96 && value < 123) {
                    s2.append(c);
                    continue;
                }
                if(c == '&' && i + 1 < l && s.charAt(i + 1) == '#') {
                    int j;
                    for(j = i; j < l && s.charAt(j) != ';'; j++) {
                        char c2 = s.charAt(j);
                        s2.append("" + c2);
                    }
                    
                    s2.append(";");
                    i = j;
                    continue;
                }
                if(!bRemoveTag && c == '&') {
                    s2.append(c);
                    continue;
                }
                if(!bRemoveTag && c == '<') {
                    int j;
                    for(j = i; j < l && s.charAt(j) != '>'; j++) {
                        char c2 = s.charAt(j);
                        s2.append("" + c2);
                    }
                    
                    s2.append(">");
                    i = j;
                    continue;
                }
                switch(c) {
                    case 34: // '"'
                        s2.append("&quot;");
                        break;
                        
                    case 38: // '&'
                        s2.append("&amp;");
                        break;
                        
                    case 62: // '>'
                        s2.append("&gt;");
                        break;
                        
                    case 60: // '<'
                        s2.append("&lt;");
                        break;
                        
                    case 39: // '\''
                        s2.append("&#39;");
                        break;
                        
                    default:
                        if(value > 127)
                            s2.append("&#" + value + ";");
                        else
                            s2.append(c);
                        break;
                }
            }
            
        }
        return s2 != null ? s2.toString() : null;
    }
    
    public static final String toJavascript(String s) {
        StringBuffer s2 = null;
        if(s != null) {
            int l = s.length();
            s2 = new StringBuffer(l);
            for(int i = 0; i < l; i++) {
                char c = s.charAt(i);
                switch(c) {
                    case 39: // '\''
                        s2.append("\\'");
                        break;
                        
                    case 34: // '"'
                        s2.append("\\\"");
                        break;
                        
                    default:
                        if(c < ' ' || c > '\177') {
                            s2.append("\\u");
                            s2.append(zeropad(Integer.toHexString(c).toUpperCase(), 4));
                        } else {
                            s2.append(c);
                        }
                        break;
                }
            }
            
        }
        return s2 != null ? s2.toString() : null;
    }
    
    private static String zeropad(String s, int width) {
        StringBuffer result = new StringBuffer(width);
        for(; width > s.length(); width--)
            result.append('0');
        
        result.append(s);
        return result.toString();
    }
    
    public static final String toXml(String s) {
        StringBuffer s2 = null;
        if(s != null) {
            int l = s.length();
            s2 = new StringBuffer(l);
            for(int i = 0; i < l; i++) {
                char c = s.charAt(i);
                switch(c) {
                    case 38: // '&'
                        if(c == '&' && i + 1 < l && s.charAt(i + 1) == '#') {
                            int j;
                            for(j = i; j < l && s.charAt(j) != ';'; j++) {
                                char c2 = s.charAt(j);
                                s2.append("" + c2);
                            }
                            
                            s2.append(";");
                            i = j;
                        } else {
                            s2.append("&amp;");
                        }
                        break;
                        
                    case 62: // '>'
                        s2.append("&gt;");
                        break;
                        
                    case 60: // '<'
                        s2.append("&lt;");
                        break;
                        
                    case 34: // '"'
                        s2.append("&quot;");
                        break;
                        
                    case 39: // '\''
                        s2.append("&apos;");
                        break;
                        
                    default:
                        if(c > '\177')
                            s2.append("&#" + (int)c + ";");
                        else
                            s2.append(c);
                        break;
                }
            }
            
        }
        return s2 != null ? s2.toString() : null;
    }
    
    public static final String toLittleXml(String s) {
        StringBuffer s2 = null;
        if(s != null) {
            int l = s.length();
            s2 = new StringBuffer(l);
            for(int i = 0; i < l; i++) {
                char c = s.charAt(i);
                switch(c) {
                    case 38: // '&'
                        String sub = s.substring(i);
                        if(sub.startsWith("&amp;", i) || sub.startsWith("&gt;", i) || sub.startsWith("&lt;", i) || sub.startsWith("&quot;", i) || sub.startsWith("&apos;", i) || sub.startsWith("&#", i))
                            s2.append('&');
                        else
                            s2.append("&amp;");
                        break;
                        
                    case 62: // '>'
                        s2.append("&gt;");
                        break;
                        
                    case 60: // '<'
                        s2.append("&lt;");
                        break;
                        
                    case 34: // '"'
                        s2.append("&quot;");
                        break;
                        
                    case 39: // '\''
                        s2.append("&apos;");
                        break;
                        
                    default:
                        s2.append(c);
                        break;
                }
            }
            
        }
        return s2 != null ? s2.toString() : null;
    }
    
    public static String convertirCaracter(String s) {
        StringBuffer s2 = null;
        if(s != null) {
            int l = s.length();
            s2 = new StringBuffer(l);
            for(int i = 0; i < l; i++) {
                char c = s.charAt(i);
                int value = c;
                switch (value) {
                    case 8216: // `
                        s2.append("'");
                        break;
                    default:
                        s2.append(c);
                        break;
                }
            }
        }
        return s2 != null ? s2.toString() : null;
    }
    
    public static String cambiarCadena(String s) {
        String comillaSimple="'";
        s=s.replaceAll("&#8216;", comillaSimple); // Cambiamos la comilla simple del word
        String acentoSolo="´";
        s=s.replaceAll("&acute;", acentoSolo);
        return s;
    }
    
}