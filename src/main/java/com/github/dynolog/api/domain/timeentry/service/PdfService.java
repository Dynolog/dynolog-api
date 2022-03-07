package com.github.dynolog.api.domain.timeentry.service;

import com.github.dynolog.api.domain.project.entity.Project;
import com.github.dynolog.api.domain.timeentry.entity.TimeEntry;
import com.github.dynolog.api.domain.timeentry.repository.TimeEntryRepository;
import com.github.dynolog.api.errors.exception.BadRequestException;
import com.github.dynolog.api.errors.model.FieldError;
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
import java.util.List;
import java.util.logging.Logger;

import static com.github.dynolog.api.utils.Constants.MESSAGES.DATES_INTERVAL_CANNOT_LONGER_THAN_MONTHS;
import static com.github.dynolog.api.utils.Constants.MESSAGES.SEARCH_DATE_INTERVAL_INVALID;
import static com.github.dynolog.api.utils.Messages.message;
import static com.github.dynolog.api.utils.Time.format;
import static com.itextpdf.text.Element.ALIGN_LEFT;
import static com.itextpdf.text.FontFactory.HELVETICA;
import static com.itextpdf.text.FontFactory.getFont;
import static java.time.format.DateTimeFormatter.ofPattern;
import static java.time.temporal.ChronoUnit.MONTHS;
import static java.util.logging.Level.SEVERE;

@Service
public class PdfService {

    private static final Logger LOGGER = Logger.getLogger(PdfService.class.getName());

    @Autowired
    private TimeEntryRepository timeEntryRepository;

    public ByteArrayInputStream create(LocalDateTime start, LocalDateTime end, String userNanoId) {
        var exception = new BadRequestException();

        if (start.isAfter(end) || end.isBefore(start)) {
            exception.add(new FieldError("start_date or end_date", message(SEARCH_DATE_INTERVAL_INVALID)));
        }

        if (MONTHS.between(start, end) > 6) {
            exception.add(new FieldError("start_date or end_date", message(DATES_INTERVAL_CANNOT_LONGER_THAN_MONTHS, 6)));
        }

        if (exception.hasError()) {
            throw exception;
        }

        var content = timeEntryRepository.findAll(start, end, userNanoId);

        return create(content);
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
                var duration = format(timeEntry.totalTimeInMillis());
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
