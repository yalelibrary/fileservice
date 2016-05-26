package edu.yale.library.fileservice;

import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;

import static org.slf4j.LoggerFactory.getLogger;

public class Crawler {

    private final static Logger logger = getLogger(DBManager.class);

    private void index(final File sourceFile, final File targetFile) throws IOException {
        if (sourceFile.isDirectory()) {
            final String absPath = sourceFile.getAbsolutePath();

            logger.info("Looking in:{}", absPath);

            if (absPath.contains("CaptureOne") || absPath.contains(".DS_Store") || sourceFile.getName().startsWith(".") ) { // TODO
                return;
            }
            final String[] paths = sourceFile.list();

            for (final String filePath : paths) {
                final File src = new File(sourceFile, filePath);
                final File dest = new File(targetFile, filePath);

                index(src, dest);
            }
        } else { // add file object to map
            final String fileName = sourceFile.getName();
            final String absPath = sourceFile.getAbsolutePath();

            if (fileName.startsWith(".DS_Store") || fileName.startsWith("._.DS_Store")) {
                return;
            }
            //filesMap.put(fileName, sourceFile.getAbsolutePath());
            //map.put(fileName, absPath);
        }
    }
}
