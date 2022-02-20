package com.github.appointmentsio.api.domain.timeentry.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "User", requiredProperties = {"id", "name"})
public record TimeEntryUser(String id, String name) { }