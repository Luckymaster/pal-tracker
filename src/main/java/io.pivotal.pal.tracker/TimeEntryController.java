package io.pivotal.pal.tracker;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.util.JSONPObject;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import jdk.nashorn.api.scripting.JSObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
public class TimeEntryController {

    @Autowired
    private TimeEntryRepository timeEntryRepository;

    private final DistributionSummary timeEntrySummary;
    private final Counter actionCounter;

    public TimeEntryController(TimeEntryRepository timeEntryRepository, MeterRegistry meterRegistry) {
        this.timeEntryRepository = timeEntryRepository;
        timeEntrySummary = meterRegistry.summary("timeEntry.summary");
        actionCounter = meterRegistry.counter("timeEntry.actionCounter");
    }


    @PostMapping(value = "/time-entries")
    public ResponseEntity<TimeEntry> create(@RequestBody TimeEntry timeEntryToCreate) {

        TimeEntry createdTimeEntry = timeEntryRepository.create(timeEntryToCreate);
        actionCounter.increment();
        timeEntrySummary.record(timeEntryRepository.list().size());

        //myresp.put(timeEntryToCreate.getId(), timeEntryToCreate);
        return new ResponseEntity<TimeEntry>(timeEntryRepository.create(timeEntryToCreate), HttpStatus.CREATED);

    }

    @GetMapping(value = "/time-entries/{timeEntryId}")
    public ResponseEntity<TimeEntry> read(@PathVariable("timeEntryId") long timeEntryId) {

            TimeEntry obj = timeEntryRepository.find(timeEntryId);

        if (null == obj)
            return new ResponseEntity(obj, HttpStatus.NOT_FOUND);
        else {
            actionCounter.increment();
            return new ResponseEntity(obj, HttpStatus.OK);
        }

    }

    @GetMapping(value = "/time-entries")
    public ResponseEntity<List<TimeEntry>> list() {

        actionCounter.increment();
        ResponseEntity<List<TimeEntry>> list = new ResponseEntity(timeEntryRepository.list(), HttpStatus.OK);
        return list;
    }

    @PutMapping(value = "/time-entries/{timeEntryId}")
    public ResponseEntity<TimeEntry> update(@PathVariable("timeEntryId") long timeEntryId, @RequestBody TimeEntry expected) {


        TimeEntry obj = timeEntryRepository.update(timeEntryId, expected);
        if (obj == null)
            return new ResponseEntity<TimeEntry>(obj, HttpStatus.NOT_FOUND);
        else{
            actionCounter.increment();
            return new ResponseEntity<TimeEntry>(obj, HttpStatus.OK);
        }

    }

    @DeleteMapping(value = "/time-entries/{timeEntryId}")
    public ResponseEntity delete(@PathVariable("timeEntryId") long timeEntryId) {


        List<TimeEntry> res = timeEntryRepository.delete(timeEntryId);
        actionCounter.increment();
        timeEntrySummary.record(timeEntryRepository.list().size());
            return new ResponseEntity<>(res, HttpStatus.NO_CONTENT);

    }
}
