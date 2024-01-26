package web.coviewpractice.dto;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.security.Principal;
import java.util.UUID;


@ToString
@Getter
@EqualsAndHashCode
@RequiredArgsConstructor
public class StompPrincipal implements Principal {

    private final String name;

    public StompPrincipal() {
        this.name = UUID.randomUUID().toString();

    }

    @Override
    public String getName(){ return name;}


}
