package com.github.appointmentsio.api.domain.shared.model;

import javax.persistence.MappedSuperclass;

import static com.github.appointmentsio.api.utils.Random.nanoid;
import static java.nio.charset.StandardCharsets.UTF_8;

@MappedSuperclass
public abstract class NonSequentialId {

    public NonSequentialId() {
        this.nanoid = nanoid().getBytes(UTF_8);
    }

    protected byte[] nanoid;

    public String getNanoid() {
        return new String(nanoid);
    }
}
