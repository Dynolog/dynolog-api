package com.github.dynolog.api.domain.timeentry.form;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "CreateTimeEntry")
public class CreateTimeEntryProps {

    @Schema(example = "Refatoração no back-end")
    private String description;

    @Schema(example = "2022-02-14T00:32:00.000Z")
    @NotNull(message = "{timeentry.start.notnull}")
    private LocalDateTime start;

    @Schema(example = "2022-02-14T00:48:00.000Z")
    private LocalDateTime stop;

    @Schema(description = "nano id", example = "V1StGXR8_Z5jdHi6B-myT")
    @NotNull(message = "{timeentry.user-id.notnull}")
    private String userId;

    @Schema(description = "nano id", example = "V1StGXR8_Z5jdHi6B-myT")
    private String projectId;
}
