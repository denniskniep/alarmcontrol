package com.alarmcontrol.server.data.models;

import java.util.ArrayList;
import java.util.Collection;

public class StringList extends ArrayList<String> {
    public StringList(Collection<? extends String> c) {
        super(c);
    }

    protected StringList() {
    }
}
