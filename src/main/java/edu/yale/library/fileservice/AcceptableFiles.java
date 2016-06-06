package edu.yale.library.fileservice;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileFilter;

public class AcceptableFiles implements FileFilter {

    public boolean accept(File dir) {
        String path = dir.getAbsolutePath();

        if (dir.isDirectory() && (path.contains(".DS_Store") || path.contains("CaptureOne"))) {  //TODO confirm
            return false;
        }

        if (dir.isFile() && !(FilenameUtils.getExtension(path).equals("tif") || FilenameUtils.getExtension(path).equals("tiff"))) {
            return false;
        }
        return true;
    }
}