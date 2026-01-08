package oracle;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * Holds the count data - number of files and total size, in Bytes.
 */
class FileCount {
    final long size;
    final long count;

    public FileCount(final long size, final long count) {
        this.count = count;
        this.size = size;
    }

    public long getSize() {
        return this.size;
    }

    public long getCount() {
        return this.count;
    }
}

public class ListDir {

    /**
     * Counts the number of files, and their total size, within a directory tree.
     * @param dirName The directory to process, count files within
     * @throws IOException
     */
    public static final FileCount list(final String dirName) throws IOException {
		long[] size = {0};
		long[] count = {0};


		try (Stream<Path> paths = Files.walk(Path.of(dirName))) {
			paths.filter(Files::isRegularFile).forEach((Path p) -> {
				File f = p.toFile();

                size[0] += f.length();
				count[0] += 1;
			});
        }
        return new FileCount(size[0], count[0]);
    }

    /**
     * Converts bytes into something fit for non-robots.
     *
     * @param bytes
     * @return Human readable string
     */
    public static final String humanReadableByteCountBin(final long bytes) {
        long b = bytes == Long.MIN_VALUE ? Long.MAX_VALUE : Math.abs(bytes);
        return b < 1024L ? bytes + " B"
                : b <= 0xfffccccccccccccL >> 40 ? "%.1f KiB".formatted(bytes / 0x1p10)
                : b <= 0xfffccccccccccccL >> 30 ? "%.1f MiB".formatted(bytes / 0x1p20)
                : b <= 0xfffccccccccccccL >> 20 ? "%.1f GiB".formatted(bytes / 0x1p30)
                : b <= 0xfffccccccccccccL >> 10 ? "%.1f TiB".formatted(bytes / 0x1p40)
                : b <= 0xfffccccccccccccL ? "%.1f PiB".formatted((bytes >> 10) / 0x1p40)
                : "%.1f EiB".formatted((bytes >> 20) / 0x1p40);
    }
}
