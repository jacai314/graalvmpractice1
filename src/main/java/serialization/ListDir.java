package serialization;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

class FileCount {
    private long size;
    private long count;

    public FileCount() {} // Required for deserialization

    @JsonCreator
    public FileCount(@JsonProperty("size") long size, @JsonProperty("count") long count) {
        this.size = size;
        this.count = count;
    }

    public long getSize() { return size; }
    public long getCount() { return count; }

    public void setSize(long size) { this.size = size; }
    public void setCount(long count) { this.count = count; }
}

public class ListDir {
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
