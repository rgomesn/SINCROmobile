package com.isel.sincroserver.resources;

import java.util.Date;

public class Resource {
    String resourceName;
    Date resourceLastReadAt;
    Class klass;

    public Resource(String resourceName, Date resourceLastReadAt, Class klass) {
        this.resourceName = resourceName;
        this.resourceLastReadAt = resourceLastReadAt;
        this.klass = klass;
    }
}
