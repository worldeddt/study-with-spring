package web.coviewpractice.common.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString

@Getter
@RequiredArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum FermiErrorCode {

    // Common
    METHOD_ARGUMENT_TYPE_MISMATCH(400, "C001", "bad request"),
    METHOD_ARGUMENT_NOT_VALID(400, "C002", "bad request"),
    HTTP_METHOD_NOT_SUPPORTED(405, "C003", "Method Not Allowed"),
    NO_HANDLER_FOUND(404, "C004", "Not Found"),
    ACCESS_DENIED(403, "C005", "access denied"),
    INTERNAL_SERVER_ERROR(500, "C006", "Internal server error"),
    UNKNOWN_INVITE_KEY(404, "C-001", "The key has expired or is not a valid key."),
    ISSUER_CANNOT_USE_INVITE_KEY(400, "C-002", "The key issuer cannot use that key."),
    NOT_FOUND_AGENT(404, "C-003", "not found agent"),
    NOT_INVITE_YOURSELF(400, "C-004", "You can't invite yourself."),
    NOT_CI_STATUS(400, "C-005", " Invitations are only possible during consultations."),
    NOT_RG_STATUS(400, "C-006", "NOT_RG_STATUS")
    , LICENCE_EXPIRED(90002, "C-007", "Call License Expired")
    ;

    private final int status;
    private final String code;
    private final String message;
}
