package com.deepwelldevelopment.tmos.core.research;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PlayerKnowledge {

    public Map<String, ArrayList<String>> completedResearch = new HashMap<String, ArrayList<String>>();

    public void wipePlayerKnowledge(String player) {
        this.completedResearch.remove(player);
    }
}
