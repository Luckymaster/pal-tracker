package io.pivotal.pal.tracker;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class InMemoryTimeEntryRepository implements TimeEntryRepository {

    private List<TimeEntry> list = new ArrayList<>();

    private Map<Long, TimeEntry> myresp = new HashMap<>();
    private long id = 0;

    @Override
    public TimeEntry create(TimeEntry any) {
        id++;
        any.setId(id);
        myresp.put(id, any);
        return any;
    }

    @Override
    public TimeEntry find(long timeEntryId) {

        return myresp.get(timeEntryId);

    }

    @Override
    public List<TimeEntry> list() {
        List<TimeEntry> timList = new ArrayList<>();
        for (Map.Entry<Long, TimeEntry> myt : myresp.entrySet()) {
            timList.add(myt.getValue());
        }
        return timList;

    }

    @Override
    public TimeEntry update(long eq, TimeEntry any) {

        if (null != myresp.get(eq)) {
            any.setId(eq);
            myresp.put(eq, any);
            return any;

        } else {
            return null;
        }


    }

    @Override
    public List<TimeEntry> delete(long timeEntryId) {

        //myresp.remove(timeEntryId);

        if (null != myresp.get(timeEntryId)) {
            myresp.remove(timeEntryId);
            List<TimeEntry> timList = new ArrayList<>();
            for (Map.Entry<Long, TimeEntry> myt : myresp.entrySet()) {
                timList.add(myt.getValue());
            }
            return timList;

        } else {
            List<TimeEntry> timList = new ArrayList<>();
            return timList;

        }
    }


    @Override
    public List getList() {
        List<TimeEntry> timList = new ArrayList<>();
        for (Map.Entry<Long, TimeEntry> myt : myresp.entrySet()) {
            timList.add(myt.getValue());
        }
        return timList;
    }


}
