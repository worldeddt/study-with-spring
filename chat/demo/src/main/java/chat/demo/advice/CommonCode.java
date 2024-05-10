package chat.demo.advice;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString

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

    // Fermi
    UNKNOWN_INVITE_KEY(404, "C-001", "The key has expired or is not a valid key."),
    ISSUER_CANNOT_USE_INVITE_KEY(400, "C-002", "The key issuer cannot use that key."),
    NOT_FOUND_AGENT(404, "C-003", "not found agent"),
    NOT_INVITE_YOURSELF(400, "C-004", "You can't invite yourself."),
    NOT_CI_STATUS(400, "C-005", " Invitations are only possible during consultations."),
    NOT_RG_STATUS(400, "C-006", "Not a RG status"),
    LICENSE_EXPIRED(500, "C-007", "Call License Expired"),
    MAKE_TICKET_FAILED(400, "C-008", "http failed for make ticket"),
    NOT_FOUND_CALL_ID(404, "C-009", "Not Found callId"),
    NO_DATA(404, "C-010", "No Data"),
    ALREADY_ROOM_END(400, "C-011", "Room is Already Ended"),
    NOT_AVAILABLE_STATUS(500, "C-012", "Not Available Status"),
    CHANGE_ROOM_STATUS_FAILED(400, "C-013", "http failed for change room status"),
    END_ROOM_FAILED(400, "C-014", "http failed for end room"),
    NOT_SUCCESS_REST_RESP(500, "C-015", "REST response is not 200 OK"),
    NOT_SUCCESS_RESP(500, "C-016", "Response is not 200 OK"),
    NULL_BODY(404, "C-017", "REST response body is null"),
    OUTBOUND_FAILED(500, "C-018", "Outbound Call Failed"),
    NOT_STARTED_CALL(500, "C-019", "Call is not started"),
    LICENSE_UPDATE_ERROR(500, "C-020", "Error in license update"),
    NOT_OWNER(403, "C-021", "Only owner can access"),
    NOT_AGENT(403, "C-022", "Only agent can access"),
    NO_CALL_FOUND(404, "C-023", "No such call"),
    WRONG_CLOSED_CODE(404, "C-023", "No such CallClosedCode"),
    NOT_FOUND_SESSION(404, "C-024", "No such Session"),
    NOT_SAME_CALL(500, "C-025", "Not a same call"),
    WRONG_LICENSE_KEY(500, "C-026", "No such License Key"),
    MMS_PUSH_FAILED(500, "C-027", "http failed for mms push"),
    INVALID_TOKEN_PAYLOAD_TOKEN(404, "C-028", "invalid token payload"),
    INVALID_TOKEN_PAYLOAD_TENANT_ID(404, "C-029", "invalid token payload"),
    INVALID_TOKEN_PAYLOAD_USER_ID(404, "C-030", "invalid token payload"),
    INVALID_TOKEN_PAYLOAD_USER_TYPE(404, "C-031", "invalid token payload"),
    DUPLICATED_LOGIN(500, "C-032", "duplicated login");

    private final int status;
    private final String code;
    private final String message;
}
