package com.github.appointmentsio.api.domain.timeentry.service;

import com.github.appointmentsio.api.domain.project.entity.Project;
import com.github.appointmentsio.api.domain.timeentry.entity.TimeEntry;
import com.github.appointmentsio.api.domain.timeentry.repository.TimeEntryRepository;
import com.github.appointmentsio.api.domain.user.repository.UserRepository;
import com.github.appointmentsio.api.errors.Error;
import com.github.appointmentsio.api.errors.exception.BadRequestException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static com.github.appointmentsio.api.domain.session.service.SessionService.authorizedOrThrow;
import static com.github.appointmentsio.api.utils.Constraints.MESSAGES.*;
import static com.github.appointmentsio.api.utils.Messages.message;
import static com.github.appointmentsio.api.utils.Response.unauthorized;
import static com.github.appointmentsio.api.utils.TimeUtils.millisToTime;
import static com.itextpdf.text.Element.ALIGN_LEFT;
import static com.itextpdf.text.FontFactory.HELVETICA;
import static com.itextpdf.text.FontFactory.getFont;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.time.format.DateTimeFormatter.ofPattern;
import static java.time.temporal.ChronoUnit.YEARS;
import static java.util.logging.Level.SEVERE;

@Service
public class PdfService {

    private static final Logger LOGGER = Logger.getLogger(PdfService.class.getName());

    @Autowired
    private TimeEntryRepository timeEntryRepository;

    @Autowired
    private UserRepository userRepository;

    public ByteArrayInputStream create(LocalDateTime start, LocalDateTime end, String userNanoid) {
        var authorized = authorizedOrThrow();

        if (!authorized.canRead(userNanoid)) {
            throw unauthorized(message(NOT_AUTHORIZED_TO_READ, "'summaries'"));
        }

        var errors = new ArrayList<Error>();

        if (start.isAfter(end) || end.isBefore(start)) {
            errors.add(new Error("start_date or end_date", message(SEARCH_DATE_INTERVAL_INVALID)));
        }

        if (YEARS.between(start, end) > 1) {
            errors.add(new Error("start_date or end_date", message(DATES_INTERVAL_CANNOT_LONGER_THAN_YEARS, 1)));
        }

        if (!errors.isEmpty()) {
            throw new BadRequestException(errors);
        }

        var entries = timeEntryRepository
                .findTimeEntriesByUserIdAndBetweenStartAndEndDate(start, end, userNanoid.getBytes(UTF_8));

        return create(entries);
    }

    public ByteArrayInputStream create(List<TimeEntry> timeEntries) {
        var document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {

            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{4F, 1F, 2F, 2F});

            addHeaders(table);

            timeEntries.forEach(timeEntry -> {
                var description = timeEntry.getDescription();
                var duration = millisToTime(timeEntry.totalTimeInMillis());
                var project = timeEntry.getProject().map(Project::getName).orElse("");
                var date = timeEntry.getStart().format(ofPattern("dd/MM/yyyy"));

                table.addCell(cell(description, 9L));
                table.addCell(cell(duration, 9L));
                table.addCell(cell(project, 9L));
                table.addCell(cell(date, 9L));
            });

            PdfWriter.getInstance(document, out);
            document.open();
            document.add(table);
            document.close();

        } catch (DocumentException exception) {
            LOGGER.log(SEVERE, exception.getMessage());
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    public void addHeaders(PdfPTable table) {
        table.addCell(cell("Description", 11L));
        table.addCell(cell("Duration", 11L));
        table.addCell(cell("Project", 11L));
        table.addCell(cell("Date", 11L));
    }

    public PdfPCell cell(String string, Long size) {
        var font = getFont(HELVETICA, size);
        var cell = new PdfPCell(new Phrase(string, font));
        cell.setHorizontalAlignment(ALIGN_LEFT);
        return cell;
    }
}
