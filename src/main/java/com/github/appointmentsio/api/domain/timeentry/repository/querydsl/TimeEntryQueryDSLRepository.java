package com.github.appointmentsio.api.domain.timeentry.repository.querydsl;

import com.github.appointmentsio.api.domain.pagination.Page;
import com.github.appointmentsio.api.domain.timeentry.entity.TimeEntry;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.github.appointmentsio.api.domain.project.entity.QProject.project;
import static com.github.appointmentsio.api.domain.timeentry.entity.QTimeEntry.timeEntry;
import static com.github.appointmentsio.api.domain.user.entity.QUser.user;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Optional.ofNullable;

@Repository
public class TimeEntryQueryDSLRepository {
    private final EntityManager manager;

    @Autowired
    public TimeEntryQueryDSLRepository(EntityManager manager) {
        this.manager = manager;
    }

    public List<TimeEntry> findAll(
            LocalDateTime start,
            LocalDateTime end,
            String userNanoId
    ) {
        var query = new JPAQuery<Tuple>(manager);

        query.from(timeEntry);

        query.where(timeEntry.start.goe(start).and(timeEntry.stop.loe(end)));
        query.where(timeEntry.user.nanoId.eq(userNanoId.getBytes(UTF_8)));

        query
                .leftJoin(timeEntry.user, user)
                .leftJoin(timeEntry.project, project);

        query.select(
                timeEntry.id,
                timeEntry.nanoId,
                timeEntry.description,
                timeEntry.start,
                timeEntry.stop,
                timeEntry.user.id,
                timeEntry.user.nanoId,
                timeEntry.user.name,
                timeEntry.project.id,
                timeEntry.project.nanoId,
                timeEntry.project.name,
                timeEntry.project.color,
                timeEntry.project.hourlyRate,
                timeEntry.project.currency
        );

        query.orderBy(timeEntry.start.desc());

        var result = query.fetch();
        return extract(result);
    }

    public Page<TimeEntry> findAll(
            Pageable pageable,
            Optional<LocalDateTime> optionalStart,
            Optional<LocalDateTime> optionalEnd,
            String userNanoId
    ) {
        var query = new JPAQuery<Tuple>(manager);
        var count = new JPAQuery<Long>(manager);

        count.from(timeEntry);
        query.from(timeEntry);

        if (optionalStart.isPresent() && optionalEnd.isPresent()) {
            var start = optionalStart.get();
            var end = optionalEnd.get();

            count.where(timeEntry.start.goe(start).and(timeEntry.stop.loe(end)));
            query.where(timeEntry.start.goe(start).and(timeEntry.stop.loe(end)));
        }

        count.where(timeEntry.user.nanoId.eq(userNanoId.getBytes(UTF_8)));
        query.where(timeEntry.user.nanoId.eq(userNanoId.getBytes(UTF_8)));

        query
                .leftJoin(timeEntry.user, user)
                .leftJoin(timeEntry.project, project);

        query.select(
                timeEntry.id,
                timeEntry.nanoId,
                timeEntry.description,
                timeEntry.start,
                timeEntry.stop,
                timeEntry.user.id,
                timeEntry.user.nanoId,
                timeEntry.user.name,
                timeEntry.project.id,
                timeEntry.project.nanoId,
                timeEntry.project.name,
                timeEntry.project.color,
                timeEntry.project.hourlyRate,
                timeEntry.project.currency
        );

        query.offset(pageable.getOffset());
        query.limit(pageable.getPageSize());

        query.orderBy(timeEntry.start.desc());

        var result = query.fetch();
        var content = extract(result);

        var countResult = count.select(timeEntry.count()).fetchFirst();

        return new Page<>(content, pageable, countResult);
    }

    public Optional<TimeEntry> findRunningByUserNanoId(String userNanoId) {
        var query = new JPAQuery<Tuple>(manager);

        query.from(timeEntry);
        query
                .where(timeEntry.stop.isNull())
                .where(timeEntry.user.nanoId.eq(userNanoId.getBytes(UTF_8)));

        query
                .leftJoin(timeEntry.user, user)
                .leftJoin(timeEntry.project, project);

        query.select(
                timeEntry.id,
                timeEntry.nanoId,
                timeEntry.description,
                timeEntry.start,
                timeEntry.stop,
                timeEntry.user.id,
                timeEntry.user.nanoId,
                timeEntry.user.name,
                timeEntry.project.id,
                timeEntry.project.nanoId,
                timeEntry.project.name,
                timeEntry.project.color,
                timeEntry.project.hourlyRate,
                timeEntry.project.currency
        );

        query.limit(1);

        query.orderBy(timeEntry.start.desc());

        var result = query.fetchFirst();

        return ofNullable(result).map(this::toTimeEntry);
    }

    private List<TimeEntry> extract(List<Tuple> result) {
        return result.stream().map(this::toTimeEntry).toList();
    }

    private TimeEntry toTimeEntry(Tuple tuple) {
        return new TimeEntry(
            tuple.get(timeEntry.id),
            tuple.get(timeEntry.nanoId),
            tuple.get(timeEntry.description),
            tuple.get(timeEntry.start),
            tuple.get(timeEntry.stop),
            tuple.get(timeEntry.user.id),
            tuple.get(timeEntry.user.nanoId),
            tuple.get(timeEntry.user.name),
            tuple.get(timeEntry.project.id),
            tuple.get(timeEntry.project.nanoId),
            tuple.get(timeEntry.project.name),
            tuple.get(timeEntry.project.color),
            tuple.get(timeEntry.project.hourlyRate),
            tuple.get(timeEntry.project.currency)
        );
    }
}
