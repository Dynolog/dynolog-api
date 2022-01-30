package com.github.appointmentsio.api.domain.shared.repository;

public class Queries {
    public static final String FIND_ID_BY_NANOID = "select entity.id from #{#entityName} entity where entity.nanoid = ?1";
}
