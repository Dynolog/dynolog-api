package com.github.dynolog.api.domain.timeentry.repository.querydsl;

import com.github.dynolog.api.domain.pagination.Page;
import com.github.dynolog.api.domain.timeentry.entity.TimeEntry;
import com.github.dynolog.api.domain.project.entity.querydsl.QProject;
import com.github.dynolog.api.domain.timeentry.entity.querydsl.QTimeEntry;
import com.github.dynolog.api.domain.user.entity.querydsl.QUser;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

        query.from(QTimeEntry.timeEntry);

        query.where(QTimeEntry.timeEntry.start.goe(start).and(QTimeEntry.timeEntry.stop.loe(end)));
        query.where(QTimeEntry.timeEntry.user.nanoId.eq(userNanoId.getBytes(UTF_8)));

        query
                .leftJoin(QTimeEntry.timeEntry.user, QUser.user)
                .leftJoin(QTimeEntry.timeEntry.project, QProject.project);

        query.select(
                QTimeEntry.timeEntry.id,
                QTimeEntry.timeEntry.nanoId,
                QTimeEntry.timeEntry.description,
                QTimeEntry.timeEntry.start,
                QTimeEntry.timeEntry.stop,
                QTimeEntry.timeEntry.user.id,
                QTimeEntry.timeEntry.user.nanoId,
                QTimeEntry.timeEntry.user.name,
                QTimeEntry.timeEntry.project.id,
                QTimeEntry.timeEntry.project.nanoId,
                QTimeEntry.timeEntry.project.name,
                QTimeEntry.timeEntry.project.color,
                QTimeEntry.timeEntry.project.hourlyRate,
                QTimeEntry.timeEntry.project.currency
        );

        query.orderBy(QTimeEntry.timeEntry.start.desc());

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

        count.from(QTimeEntry.timeEntry);
        query.from(QTimeEntry.timeEntry);

        if (optionalStart.isPresent() && optionalEnd.isPresent()) {
            var start = optionalStart.get();
            var end = optionalEnd.get();

            count.where(QTimeEntry.timeEntry.start.goe(start).and(QTimeEntry.timeEntry.stop.loe(end)));
            query.where(QTimeEntry.timeEntry.start.goe(start).and(QTimeEntry.timeEntry.stop.loe(end)));
        }

        count.where(QTimeEntry.timeEntry.user.nanoId.eq(userNanoId.getBytes(UTF_8)));
        query.where(QTimeEntry.timeEntry.user.nanoId.eq(userNanoId.getBytes(UTF_8)));

        query
                .leftJoin(QTimeEntry.timeEntry.user, QUser.user)
                .leftJoin(QTimeEntry.timeEntry.project, QProject.project);

        query.select(
                QTimeEntry.timeEntry.id,
                QTimeEntry.timeEntry.nanoId,
                QTimeEntry.timeEntry.description,
                QTimeEntry.timeEntry.start,
                QTimeEntry.timeEntry.stop,
                QTimeEntry.timeEntry.user.id,
                QTimeEntry.timeEntry.user.nanoId,
                QTimeEntry.timeEntry.user.name,
                QTimeEntry.timeEntry.project.id,
                QTimeEntry.timeEntry.project.nanoId,
                QTimeEntry.timeEntry.project.name,
                QTimeEntry.timeEntry.project.color,
                QTimeEntry.timeEntry.project.hourlyRate,
                QTimeEntry.timeEntry.project.currency
        );

        query.offset(pageable.getOffset());
        query.limit(pageable.getPageSize());

        query.orderBy(QTimeEntry.timeEntry.start.desc());

        var result = query.fetch();
        var content = extract(result);

        var countResult = count.select(QTimeEntry.timeEntry.count()).fetchFirst();

        return new Page<>(content, pageable, countResult);
    }

    public Optional<TimeEntry> findRunningByUserNanoId(String userNanoId) {
        var query = new JPAQuery<Tuple>(manager);

        query.from(QTimeEntry.timeEntry);
        query
                .where(QTimeEntry.timeEntry.stop.isNull())
                .where(QTimeEntry.timeEntry.user.nanoId.eq(userNanoId.getBytes(UTF_8)));

        query
                .leftJoin(QTimeEntry.timeEntry.user, QUser.user)
                .leftJoin(QTimeEntry.timeEntry.project, QProject.project);

        query.select(
                QTimeEntry.timeEntry.id,
                QTimeEntry.timeEntry.nanoId,
                QTimeEntry.timeEntry.description,
                QTimeEntry.timeEntry.start,
                QTimeEntry.timeEntry.stop,
                QTimeEntry.timeEntry.user.id,
                QTimeEntry.timeEntry.user.nanoId,
                QTimeEntry.timeEntry.user.name,
                QTimeEntry.timeEntry.project.id,
                QTimeEntry.timeEntry.project.nanoId,
                QTimeEntry.timeEntry.project.name,
                QTimeEntry.timeEntry.project.color,
                QTimeEntry.timeEntry.project.hourlyRate,
                QTimeEntry.timeEntry.project.currency
        );

        query.limit(1);

        query.orderBy(QTimeEntry.timeEntry.start.desc());

        var result = query.fetchFirst();

        return ofNullable(result).map(this::toTimeEntry);
    }

    private List<TimeEntry> extract(List<Tuple> result) {
        return result.stream().map(this::toTimeEntry).toList();
    }

    private TimeEntry toTimeEntry(Tuple tuple) {
        return new TimeEntry(
            tuple.get(QTimeEntry.timeEntry.id),
            tuple.get(QTimeEntry.timeEntry.nanoId),
            tuple.get(QTimeEntry.timeEntry.description),
            tuple.get(QTimeEntry.timeEntry.start),
            tuple.get(QTimeEntry.timeEntry.stop),
            tuple.get(QTimeEntry.timeEntry.user.id),
            tuple.get(QTimeEntry.timeEntry.user.nanoId),
            tuple.get(QTimeEntry.timeEntry.user.name),
            tuple.get(QTimeEntry.timeEntry.project.id),
            tuple.get(QTimeEntry.timeEntry.project.nanoId),
            tuple.get(QTimeEntry.timeEntry.project.name),
            tuple.get(QTimeEntry.timeEntry.project.color),
            tuple.get(QTimeEntry.timeEntry.project.hourlyRate),
            tuple.get(QTimeEntry.timeEntry.project.currency)
        );
    }
}
