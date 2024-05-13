package chat.demo.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldNameConstants;
import jakarta.validation.constraints.NotBlank;

public class InviteKeyPayload {

    @Data
    public static class InviteKeyRequest {
        @NotBlank(message = "inviteKey should not be null")
        private String inviteKey;
    }

    @FieldNameConstants
    @Data
    @AllArgsConstructor
    public static class InviteKeyResponse {
        private String callId;
        private String inviteKey;
        private boolean isExpired;
        private boolean isUsed;
    }
}