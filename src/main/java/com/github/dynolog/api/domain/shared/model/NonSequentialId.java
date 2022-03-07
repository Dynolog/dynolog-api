package com.github.dynolog.api.domain.shared.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import static com.github.dynolog.api.utils.Random.nanoid;
import static java.nio.charset.StandardCharsets.UTF_8;

@MappedSuperclass
public abstract class NonSequentialId {

    public NonSequentialId() {
        this.nanoId = nanoid().getBytes(UTF_8);
    }

    @Column(name = "nano_id", unique = true)
    protected byte[] nanoId;

    public String getNanoId() {
        return new String(nanoId);
    }
}
