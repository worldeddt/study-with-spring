package media.ftf.enums;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum CommonCode {

    OK(200, "C000", "OK"),
    TEMP(500, "TEMP", "TEMP"),

    // Common
    NO_HANDLER_FOUND(404, "C001", "Not Found"),
    NO_RESOURCE_FOUND(404, "C002", "no resource found"),
    HTTP_METHOD_NOT_SUPPORTED(405, "C003", "http method not supported"),
    HTTP_MEDIA_TYPE_NOT_SUPPORTED(415, "C004", "http media type not supported"),
    HTTP_MESSAGE_NOT_READABLE(400, "C005", "invalid message format"),
    METHOD_ARGUMENT_TYPE_MISMATCH(400, "C006", "method argument type mismatch"),
    METHOD_ARGUMENT_NOT_VALID(400, "C007", "method argument not valid"),
    ACCESS_DENIED(403, "C008", "access denied"),
    INTERNAL_SERVER_ERROR(500, "C009", "Internal server error"),

    NOT_FOUND_RECEIVER(404, "C-007", "not found receiver"),
    NOT_FOUND_ROOM(404, "C-008", "room not found"),
    NOT_FOUND_RECORD(404, "C-009", "record not found"),
    RECORD_USER_NOT_AGENT(500, "C-020", "record user is not agent"),
    RECORD_ALREADY_STARTED(500, "C-021", "room recording already started"),
    RECORD_ALREADY_STOPPED(500, "C-022", "room recording already stopped"),
    RECORD_EP_NOT_EXIST(500, "C-023", "record endpoint not exist"),
    RECORD_HUBPORT_NOT_EXIST(500, "C-024", "record hubport not exist"),
    RECORD_FILE_NOT_EXIST(500, "C-025", "record file not exist"),
    RECORD_FILE_SIZE_ZERO(500, "C-026", "record file size zero"),
    RECORD_FILE_CHECK_ERROR(500, "C-027", "record file check error"),
    RECORD_ENCODING_ERROR(500, "C-028", "encoding error occured"),
    MS_RECORD_ERROR(500, "C-030", "media server record error"),
    WEBRTC_EP_NOT_EXIST(500, "C-040", "webrtc endpoint not exist"),
    PARTICIPANT_NOT_EXIST(500, "C-041", "participant not exist"),
    INVALID_TOKEN_PAYLOAD_TOKEN(404, "C-028", "invalid token payload"),
    INVALID_TOKEN_PAYLOAD_TENANT_ID(404, "C-029", "invalid token payload"),
    INVALID_TOKEN_PAYLOAD_USER_ID(404, "C-030", "invalid token payload"),
    INVALID_TOKEN_PAYLOAD_USER_TYPE(404, "C-031", "invalid token payload"),
    DUPLICATED_LOGIN(500, "C-032", "duplicated login");


    private final int status;
    private final String code;
    private final String message;
}
