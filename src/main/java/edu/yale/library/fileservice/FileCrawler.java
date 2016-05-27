package edu.yale.library.fileservice;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Crawls filesystem and populates a map
 */
public class FileCrawler {

    private final static Logger logger = getLogger(FileCrawler.class);

    private String path;

    private final Multimap<String, String> map = ArrayListMultimap.create();

    public Multimap<String, String> getIndex() throws IOException {
        logger.debug("Path:{} exists:{}", path, new File(path).exists());
        index(new File(path));
        logger.debug("Computed file map size:{}", map.size());
        logger.debug("Computed map:{}", map.toString());
        return map;
    }

    private void index(final File sourceFile) throws IOException {
        if (sourceFile.isDirectory()) {
            final File[] paths = sourceFile.listFiles(new AcceptableFiles());

            for (final File filePath : paths) {
                index(filePath);
            }
        } else {
            final String fileName = sourceFile.getName();
            final String absPath = sourceFile.getAbsolutePath();
            map.put(fileName, absPath);
        }
    }

    public FileCrawler(String path) {
        this.path = path;
    }
}
