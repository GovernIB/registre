package es.caib.regweb3.webapp.utils;


import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 * User: jpernia
 * Date: 16/04/14
 * Time: 12:08
 * To change this template use File | Settings | File Templates.
 */
@Component
public class ConvertirTexto {

    // Converteix caràcters especials
    public static String toCp1252(String texto) {
        if (texto==null) return null;

        final String conversion[][] = {
                {"80",    "\\u20AC"},
                {"82",    "\\u201A"},
                {"83",    "\\u0192"},
                {"84",    "\\u201E"},
                {"85",    "\\u2026"},
                {"86",    "\\u2020"},
                {"87",    "\\u2021"},
                {"88",    "\\u02C6"},
                {"89",    "\\u2030"},
                {"8A",    "\\u0160"},
                {"8B",    "\\u2039"},
                {"8C",    "\\u0152"},
                {"8E",    "\\u017D"},
                {"91",    "\\u2018"},
                {"92",    "\\u2019"},
                {"93",    "\\u201C"},
                {"94",    "\\u201D"},
                {"95",    "\\u2022"},
                {"96",    "\\u2013"},
                {"97",    "\\u2014"},
                {"98",    "\\u02DC"},
                {"99",    "\\u2122"},
                {"9A",    "\\u0161"},
                {"9B",    "\\u203A"},
                {"9C",    "\\u0153"},
                {"9E",    "\\u017E"},
                {"9F",    "\\u0178"},
                {"A0",    "\\u00A0"},
                {"A1",    "\\u00A1"},
                {"A2",    "\\u00A2"},
                {"A3",    "\\u00A3"},
                {"A4",    "\\u00A4"},
                {"A5",    "\\u00A5"},
                {"A6",    "\\u00A6"},
                {"A7",    "\\u00A7"},
                {"A8",    "\\u00A8"},
                {"A9",    "\\u00A9"},
                {"AA",    "\\u00AA"},
                {"AB",    "\\u00AB"},
                {"AC",    "\\u00AC"},
                {"AD",    "\\u00AD"},
                {"AE",    "\\u00AE"},
                {"AF",    "\\u00AF"},
                {"B0",    "\\u00B0"},
                {"B1",    "\\u00B1"},
                {"B2",    "\\u00B2"},
                {"B3",    "\\u00B3"},
                {"B4",    "\\u00B4"},
                {"B5",    "\\u00B5"},
                {"B6",    "\\u00B6"},
                {"B7",    "\\u00B7"},
                {"B8",    "\\u00B8"},
                {"B9",    "\\u00B9"},
                {"BA",    "\\u00BA"},
                {"BB",    "\\u00BB"},
                {"BC",    "\\u00BC"},
                {"BD",    "\\u00BD"},
                {"BE",    "\\u00BE"},
                {"BF",    "\\u00BF"},
                {"C0",    "\\u00C0"},
                {"C1",    "\\u00C1"},
                {"C2",    "\\u00C2"},
                {"C3",    "\\u00C3"},
                {"C4",    "\\u00C4"},
                {"C5",    "\\u00C5"},
                {"C6",    "\\u00C6"},
                {"C7",    "\\u00C7"},
                {"C8",    "\\u00C8"},
                {"C9",    "\\u00C9"},
                {"CA",    "\\u00CA"},
                {"CB",    "\\u00CB"},
                {"CC",    "\\u00CC"},
                {"CD",    "\\u00CD"},
                {"CE",    "\\u00CE"},
                {"CF",    "\\u00CF"},
                {"D0",    "\\u00D0"},
                {"D1",    "\\u00D1"},
                {"D2",    "\\u00D2"},
                {"D3",    "\\u00D3"},
                {"D4",    "\\u00D4"},
                {"D5",    "\\u00D5"},
                {"D6",    "\\u00D6"},
                {"D7",    "\\u00D7"},
                {"D8",    "\\u00D8"},
                {"D9",    "\\u00D9"},
                {"DA",    "\\u00DA"},
                {"DB",    "\\u00DB"},
                {"DC",    "\\u00DC"},
                {"DD",    "\\u00DD"},
                {"DE",    "\\u00DE"},
                {"DF",    "\\u00DF"},
                {"E0",    "\\u00E0"},
                {"E1",    "\\u00E1"},
                {"E2",    "\\u00E2"},
                {"E3",    "\\u00E3"},
                {"E4",    "\\u00E4"},
                {"E5",    "\\u00E5"},
                {"E6",    "\\u00E6"},
                {"E7",    "\\u00E7"},
                {"E8",    "\\u00E8"},
                {"E9",    "\\u00E9"},
                {"EA",    "\\u00EA"},
                {"EB",    "\\u00EB"},
                {"EC",    "\\u00EC"},
                {"ED",    "\\u00ED"},
                {"EE",    "\\u00EE"},
                {"EF",    "\\u00EF"},
                {"F0",    "\\u00F0"},
                {"F1",    "\\u00F1"},
                {"F2",    "\\u00F2"},
                {"F3",    "\\u00F3"},
                {"F4",    "\\u00F4"},
                {"F5",    "\\u00F5"},
                {"F6",    "\\u00F6"},
                {"F7",    "\\u00F7"},
                {"F8",    "\\u00F8"},
                {"F9",    "\\u00F9"},
                {"FA",    "\\u00FA"},
                {"FB",    "\\u00FB"},
                {"FC",    "\\u00FC"},
                {"FD",    "\\u00FD"},
                {"FE",    "\\u00FE"},
                {"FF",    "\\u00FF"}
        };

        String temp = texto;
        for (String[] aConversion : conversion) {
            temp = temp.replaceAll(aConversion[1], "\\\\'" + aConversion[0]);
        }
        return temp;
    }

    /** Converts a <CODE>String</CODE> into a <CODE>Byte</CODE> array
     * according to the ISO-8859-1 codepage.
     * @param text the text to be converted
     * @return the conversion result
     */

    public static final byte[] getISOBytes(String text)
    {
        if (text == null)
            return null;
        int len = text.length();
        byte b[] = new byte[len];
        for (int k = 0; k < len; ++k)
            b[k] = (byte)text.charAt(k);
        return b;
    }

}
