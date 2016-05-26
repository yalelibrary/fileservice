package edu.yale.library.fileservice;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

public class Crawler {

    private final static Logger logger = getLogger(Crawler.class);

    Multimap<String, String> map = ArrayListMultimap.create();


    public Multimap<String, String> doIndex(final String path) throws IOException {
        logger.debug("Path exists:" + new File(path).exists());
        logger.debug("For path:" + path);
        index(new File(path));
        logger.debug("Map size:{}", map.size());
        return map;
    }

    private void index(final File sourceFile) throws IOException {
        if (sourceFile.isDirectory()) {
            final String absPath = sourceFile.getAbsolutePath();

            logger.debug("Looking in:{}", absPath);

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
