package es.caib.regweb3.utils;

import eu.medsea.mimeutil.MimeType;
import eu.medsea.mimeutil.MimeUtil;

import java.util.Collection;

/**
 * Created by Fundació Bit
 * Date: 11/07/2016.
 */
public class MimeTypeUtils {

    static {
        MimeUtil.registerMimeDetector("eu.medsea.mimeutil.detector.ExtensionMimeDetector");
        MimeUtil.registerMimeDetector("eu.medsea.mimeutil.detector.MagicMimeMimeDetector");
    }

    public static String getMimeTypeExtension(String extension) {
        return getMimeTypeFileName("fichero." + extension);
    }

    public static String getMimeTypeFileName(String fileName) {
        Collection mimeTypes = MimeUtil.getMimeTypes(fileName.toLowerCase());
        return getMostSpecificMimeType(mimeTypes);
    }

    protected static String getMostSpecificMimeType(Collection mimeTypes) {

        String mostSpecificMimeType = null;

        if ((mimeTypes != null) && !mimeTypes.isEmpty()) {
            MimeType mimeType = MimeUtil.getMostSpecificMimeType(mimeTypes);
            if (mimeType != null) {
                mostSpecificMimeType = mimeType.toString();
            }
        }

        return mostSpecificMimeType;
    }
}
