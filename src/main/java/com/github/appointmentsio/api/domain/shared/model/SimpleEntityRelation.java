package com.github.appointmentsio.api.domain.shared.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "Relation", requiredProperties = {"id", "name"})
public record SimpleEntityRelation(String id, String name) { }
