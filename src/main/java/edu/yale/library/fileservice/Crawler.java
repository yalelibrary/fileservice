package edu.yale.library.fileservice;

import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

public class Crawler {

    private final static Logger logger = getLogger(DBManager.class);

    private Map<String, String> map = new HashMap<String, String>(); //FIXME replace with multimap

    public Map<String, String> doIndex(final String path) throws IOException {
        index(new File(path));
        return map;
    }

    private void index(final File sourceFile) throws IOException {
        if (sourceFile.isDirectory()) {
            final String absPath = sourceFile.getAbsolutePath();

            logger.info("Looking in:{}", absPath);

            if (absPath.contains("CaptureOne") || absPath.contains(".DS_Store") || sourceFile.getName().startsWith(".") ) { // TODO
                return;
            }
            final String[] paths = sourceFile.list();

            for (final String filePath : paths) {
                final File src = new File(sourceFile, filePath);
                index(src);
            }
        } else { // add file object to map
            final String fileName = sourceFile.getName();
            final String absPath = sourceFile.getAbsolutePath();

            if (fileName.startsWith(".DS_Store") || fileName.startsWith("._.DS_Store")) {
                return;
            }
            map.put(fileName, absPath);
        }
    }
}
