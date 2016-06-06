package edu.yale.library.fileservice;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Crawls filesystem and populates a map
 */
public class FileCrawler {

    private final static Logger logger = getLogger(FileCrawler.class);

    private String path;

    private String[] includeFolders;

    private final Multimap<String, String> map = ArrayListMultimap.create();

    public Multimap<String, String> getIndex() throws IOException {
        final File f = new File(path);

        if (!f.exists()) {
            logger.error("Share does not exist:{}", path);
            return map;
        }

        logger.debug("Share:{} exists:{}", path, new File(path).exists());
        final AcceptableFiles acceptableFiles = new AcceptableFiles();

        for (String s : includeFolders) {
            s = s.trim();
            String dir = path + System.getProperty("file.separator") + s;
            dir = dir.replace("[", "");
            dir = dir.replace("]", "");
            final File folder = new File(dir);

            if (!folder.exists()) {
                logger.error("Folder not found:{}", dir);
                continue;
            }
            logger.debug("Indexing top-level folder:{}", folder);
            index(folder, acceptableFiles);
            logger.debug("Computed index size:{} for folder:{}", map.size(), folder);
        }

        return map;
    }

    private void index(final File sourceFile, final AcceptableFiles fileFilter) throws IOException {
        if (sourceFile.isDirectory()) {
            final File[] paths = sourceFile.listFiles(fileFilter);

            for (final File filePath : paths) {
                index(filePath, fileFilter);
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

    public FileCrawler(String path, String[] includeFolders) {
        this.path = path;
        this.includeFolders = includeFolders;
    }
}
