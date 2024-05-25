package media.ftf.handler.dto;

import lombok.Builder;
import lombok.Data;
import media.ftf.enums.RecordType;

import java.nio.file.Files;
import java.nio.file.Paths;


@Data
@Builder
public class RecordInfo {

    final String kurentoRootPath;
    final String hostRootPath;
    final String dirPath;
    final String fileName;
    final String suffix;
    final RecordType recordType;

    public String getFileUri() {
        return dirPath + "/" + fileName + "." + suffix;
    }

    public String getFileUriWithHostPath() {
        return hostRootPath + getFileUri();
    }

    public String getFileUriWithKurentoPath() {
        return kurentoRootPath + getFileUri();
    }

    public String getDirPathWithHostPath() {
        return hostRootPath + dirPath;
    }

    public long getFileSize() {
        try {
            return Files.size(Paths.get(getFileUriWithHostPath()));
        } catch (Exception e) {
            return 0;
        }
    }
}
