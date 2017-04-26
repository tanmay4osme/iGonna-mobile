package com.gonna.firebase;

import java.util.Map;

/**
 * Created by y0rsh on 12/3/16.
 */

public abstract class JSONRepresentable {

    public JSONRepresentable() {
    }

    public JSONRepresentable(Map<String, Object> json) {
        fromJSON(json);
    }

    public abstract void fromJSON(Map<String, Object> json);

    public abstract Map<String, Object> toJSON();
}