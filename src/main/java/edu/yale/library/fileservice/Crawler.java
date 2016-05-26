package edu.yale.library.fileservice;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.apache.commons.io.FilenameUtils;
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
        logger.debug("Path:{} exists:{}", path, new File(path).exists());
        index(new File(path));
        logger.debug("Computed map size:{}", map.size());
        return map;
    }

    private void index(final File sourceFile) throws IOException {
        if (sourceFile.isDirectory()) {
            final String absPath = sourceFile.getAbsolutePath();
            logger.debug("Looking in:{}", absPath);
            final File[] paths = sourceFile.listFiles(new AcceptableFiles());

            for (final File filePath : paths) {
                index(filePath);
            }
        } else { // add file object to map
            final String fileName = sourceFile.getName();
            final String absPath = sourceFile.getAbsolutePath();
            map.put(fileName, absPath);
        }
    }
}
