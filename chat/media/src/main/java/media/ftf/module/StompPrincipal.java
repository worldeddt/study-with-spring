package media.ftf.module;


import lombok.Builder;
import lombok.Getter;

import java.security.Principal;
import java.util.UUID;

@Getter
public class StompPrincipal implements Principal {

    private final String name;


    public StompPrincipal() {
        name = UUID.randomUUID().toString();
    }

    public String getName() {
        return this.name;
    }

}
