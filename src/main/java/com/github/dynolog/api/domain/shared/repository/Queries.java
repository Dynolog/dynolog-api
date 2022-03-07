package com.github.dynolog.api.domain.shared.repository;

public class Queries {
    public static final String FIND_ID_BY_NANO_ID = "select entity.id from #{#entityName} entity where entity.nanoId = ?1";
}
