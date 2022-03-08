package com.github.dynolog.api.domain.project.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "User", requiredProperties = {"id", "name"})
public record ProjectUser(String id, String name) { }