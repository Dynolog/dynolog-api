package com.github.appointmentsio.api.domain.timeentry.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "Project", requiredProperties = {"name"})
public record TimeEntryProjectInfo(String id, String name, String color)  { }
