package crystal.champions.util;

import crystal.champions.Champions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FilesWriter {
    private FilesWriter() {
        /* This utility class should not be instantiated */
    }

    public static void writer(Path path, Map<String, Object> changes) {
        try {
            if (!Files.exists(path)) return;

            List<String> l = Files.readAllLines(path);
            List<String> newLines = new ArrayList<>();

            for (String line : l) {
                String trimmed = line.trim();
                if (!trimmed.startsWith("#") && trimmed.contains("=")) {
                    String key = trimmed.split("=")[0].trim();
                    if (changes.containsKey(key)) {
                        newLines.add(key + " = " + changes.get(key));
                        continue;
                    }
                }
                newLines.add(line);
            }
            Files.write(path, newLines);
            Champions.LOGGER.info("Saved config");
        } catch (IOException e) {
            Champions.LOGGER.error("Failed to save config - path: {}", path);
        }
    }
}
