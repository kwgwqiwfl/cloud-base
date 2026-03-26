package com.ring.welkin.common.persistence.i18n;

import com.ring.welkin.common.core.i18n.MessageSourceLocator;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class PersistenceMessageSourceLocator implements MessageSourceLocator {

    @Override
    public List<String> baseNames() {
        return Arrays.asList("com.ring.welkin.common.persistence.i18n.persistence");
    }

}
