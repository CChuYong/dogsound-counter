package kr.swmaestro.dogsoundcounter.infrastructure.jpa.entities;

import com.google.common.base.Preconditions;
import kr.swmaestro.dogsoundcounter.core.entities.Identity;

public class IdentityMapper {
    public static Long toIdentityValue(Identity identity){
        Preconditions.checkNotNull(identity, "identity cannot be null");
        if(identity == Identity.NOTHING) return null;
        return identity.getId();
    }
}
