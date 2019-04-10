package io.pivotal.pal.tracker;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.util.JSONPObject;
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

    private Map<Long, TimeEntry> myresp = new HashMap<>();

    public TimeEntryController(TimeEntryRepository timeEntryRepository) {
        this.timeEntryRepository = timeEntryRepository;
    }


    @PostMapping(value = "/time-entries")
    public ResponseEntity<TimeEntry> create(@RequestBody TimeEntry timeEntryToCreate) {

        //myresp.put(timeEntryToCreate.getId(), timeEntryToCreate);
        return new ResponseEntity<TimeEntry>(timeEntryRepository.create(timeEntryToCreate), HttpStatus.CREATED);

    }

    @GetMapping(value = "/time-entries/{timeEntryId}")
    public ResponseEntity<TimeEntry> read(@PathVariable("timeEntryId") long timeEntryId) {

        /*if (null != myresp.get(timeEntryId)) {


            return new ResponseEntity(myresp.get(timeEntryId), HttpStatus.OK);
        } else {
            return new ResponseEntity(null, HttpStatus.NOT_FOUND);

        }*/
        TimeEntry obj = timeEntryRepository.find(timeEntryId);

        if (null == obj)
            return new ResponseEntity(obj, HttpStatus.NOT_FOUND);
        else return new ResponseEntity(obj, HttpStatus.OK);

    }

    @GetMapping(value = "/time-entries")
    public ResponseEntity<List<TimeEntry>> list() {

       /* List<TimeEntry> timList = new ArrayList<>();
        for (Map.Entry<Long, TimeEntry> myt : myresp.entrySet()) {
            timList.add(myt.getValue());
        }*/

        ResponseEntity<List<TimeEntry>> list = new ResponseEntity(timeEntryRepository.list(), HttpStatus.OK);
        return list;
    }

    @PutMapping(value = "/time-entries/{timeEntryId}")
    public ResponseEntity<TimeEntry> update(@PathVariable("timeEntryId") long timeEntryId, @RequestBody TimeEntry expected) {


        TimeEntry obj = timeEntryRepository.update(timeEntryId, expected);
        if (obj == null)
            return new ResponseEntity<TimeEntry>(obj, HttpStatus.NOT_FOUND);
        return new ResponseEntity<TimeEntry>(obj, HttpStatus.OK);

    }

    @DeleteMapping(value = "/time-entries/{timeEntryId}")
    public ResponseEntity delete(@PathVariable("timeEntryId") long timeEntryId) {


        List<TimeEntry> res = timeEntryRepository.delete(timeEntryId);
        if (res.isEmpty())
            return new ResponseEntity<>(res, HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(timeEntryRepository.delete(timeEntryId), HttpStatus.NO_CONTENT);

    }
}
