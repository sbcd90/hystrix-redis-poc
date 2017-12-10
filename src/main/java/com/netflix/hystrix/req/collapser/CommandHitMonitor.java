package com.netflix.hystrix.req.collapser;

import org.apache.commons.lang3.tuple.Pair;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class CommandHitMonitor implements Serializable {

  private static final long serialVersionUID = 1L;

  private static Map<String, Integer> monitorMap = new HashMap<>();

  public static void createCommandMonitor(Pair<String, Integer> commandEntry) {
    monitorMap.put(commandEntry.getKey(), commandEntry.getValue());
  }

  public static void updateCommandHitValue(Pair<String, Integer> commandEntry) {
    monitorMap.put(commandEntry.getKey(), commandEntry.getValue());
  }

  public static void incrementCommandHitValue(String commandKey) {
    monitorMap.put(commandKey, monitorMap.get(commandKey) + 1);
  }

  public static int getCommandHitValue(String commandKey) {
    return monitorMap.get(commandKey);
  }
}