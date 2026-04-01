### PETree

### assembledMatch.java

The `assembledMatch` class is a data structure designed to store and manage information related to a sequence of matched elements, typically used in event pattern or interval-based computations.



### Event.java

The `Event` class represents an event in the system, containing key temporal and descriptive attributes:

- `eventType` (String): the event type or label.
- `lower`, `upper` (int): time bounds of the event.
- `id` (int): unique event identifier.
- `vlb`, `vub` (int): valid lower and upper bounds.
- `otherAtt` (double): an additional numeric attribute (e.g., probability or weight).



### match.java

The `match` class is used to store **sub-pattern match results**, including the time intervals, durations, and IDs of the events involved. 



### function.java

The `function` class 包含了系统的一些实现的必须要的函数

1.`checkAllBuffer()` checks whether all operator nodes have non-empty event buffers.
 **Parameters:**

- `operatorLists`: a list of `TreeNode` objects representing the operator nodes in the pattern tree.

------

2.`AndMatch()` performs the matching process for an **AND** operator node. It combines events from multiple child nodes that can occur within the same time window and satisfy temporal constraints.
 **Parameters:**

- `andNode`: the AND node to process.
- `benchMark`: whether the current node is the benchmark node (true for root-level processing).
- `benchmarkEvent`: the reference event used as the timing anchor when `benchMark` is true.
- `timeWindow`: the maximum allowed time interval for matching.
- `pattern`:  pattern that we input
- `patternType`: an array describing the operator type for each part of the pattern.

---------------

3.`SeqMatch()` performs the matching process for a **SEQ (sequence)** operator node. It identifies valid event sequences that satisfy order and timing constraints.
 **Parameters:**

- `seqNode`: the SEQ node to process.
- `benchMark`: whether the node is being processed as the benchmark node.
- `benchmarkEvent`: the reference event used as the starting point when `benchMark` is true.
- `timeWindow`: the total allowed time interval for a valid sequence.
- `pattern`: a 2D array describing the event pattern.
- `patternType`: an array of operator types corresponding to the pattern components.

--------------

4.`countMinBound4LeafNode() `calculates the **minimum lower bound index** for a leaf node based on its position and operator type within a pattern tree.
 This method determines how many positions are already occupied by preceding operators and adjusts the lower bound accordingly.

**Parameters:**

- `i` — index of the current sub-pattern in the overall pattern structure.
- `j` — index of the current element within the sub-pattern.
- `beLower` — base lower bound from which the offset is calculated.
- `timeWindow` — the total time window size allowed for the pattern.
- `pattern` — a 2D array describing all sub-patterns and their event elements.
- `patternType` — an array describing the operator type (`SEQ`, `NSEQ`, `AND`, `OR`, etc.) of each sub-pattern.

---------

5.`NseqMatch()`  performs **Non-Sequential (NSEQ) pattern matching** for a given `NSEQNode` in an event pattern tree.
 It computes partial matches considering optional negation nodes and time constraints.

**Parameters:**

- `nseqNode` — the NSEQ operator node to match.
- `benchMark` — whether to use a benchmark event as the starting reference.
- `benchmarkEvent` — the event used as a benchmark when `benchMark` is true.
- `timeWindow` — allowed time window for pattern matching.
- `pattern` — 2D array describing sub-patterns and event sequences.
- `patternType` — array specifying operator type (`SEQ`, `NSEQ`, `AND`, `OR`) for each sub-pattern.

--------------

6.`countInterval()` calculates the **remaining length or “occupied positions”** for a sub-pattern element, used to limit time windows during matching.

**Parameters:**

- `i` — index of the sub-pattern in the full pattern.

- `j` — index of the element in the sub-pattern.

- `pattern` — 2D array of pattern elements.

- `operatorType` — array of operator types for each sub-pattern.

  -----------

7.`canFormUniqueSequence()` checks whether the given event ranges can form a **unique, non-overlapping sequence**.

**Parameters:**

- `lowerList` — array of lower bounds for each event.
- `upperList` — array of upper bounds for each event.

------

8.`isOverlap(Event event, int lower, int upper)` determines whether a given event overlaps with a time interval.

**Parameters:**

- `event` — the event to check.
- `lower` — start of the interval.
- `upper` — end of the interval.

------

9.`trimInnerMatch4SEQ()` adjusts **lower and upper bounds** of a partial SEQ match to maintain valid sequential order.

**Parameters:**

- `vlbList` — array of current lower bounds of events.
- `vubList` — array of current upper bounds of events.
- `index` — current index to adjust.

------

10.`countPatthernLenghth()` counts the **total number of positions/events** in a pattern considering operator types (`NSEQ` counts as 2).

**Parameters:**

- `pattern` — 2D array of pattern elements.
- `patternType` — array of operator types.





### PETree.java

`PETree` is a **custom Java library** designed to model and match temporal event patterns. It supports operators such as **SEQ**, **AND**, **OR**, and **NSEQ**, and allows for complex partial and negation-based matches. This library is implemented for research or project-specific use and provides flexible tree structures for representing and evaluating event patterns.



### PETreeSystem.java

`PETreeSystem` is a **custom Java system** for processing temporal events and matching them against user-defined patterns. It works in conjunction with the `PETree` library to handle complex sequences, AND/OR logic, and non-occurrence (NSEQ) patterns.

**Core Methods**

`eventProcess()` parses a line of event data:

- Input format: `eventType,id,eventId,lowerBound,upperBound`
- Returns an `Event` object.

------

`storeEvent()` stores an incoming event in the correct leaf node of the PETree:

- Finds the target leaf node based on the event type and pattern.
- Returns `true` if the event is successfully stored.

------

`updateEvent()` removes outdated events from leaf node buffers:

- Iterates over all leaf nodes.
- Deletes events whose upper bounds are earlier than the computed minimum bound.

------

`matchPhase()` performs the main matching logic:

- Processes partial matches for each operator node (SEQ, AND, NSEQ).
- Handles negations and partial match assembly.
- Clears partial matches when patterns cannot be matched.

------

`assembleRound()` combines partial matches from sub-patterns into complete matches:

- Iterates over operator nodes in order.
- Builds `assembledMatch` objects representing full-pattern matches.
- Ensures temporal constraints between pattern segments are maintained.

------

`AverageMemory()` computes the average memory consumption of `PETreeSystem` over multiple runs:

- Performs several warm-up executions to stabilize JVM behavior.
- Triggers garbage collection before each measurement.
- Records memory usage before and after each execution.
- Computes the memory difference for each round.
- Averages the results and converts them to megabytes (MB).

------



### Experiment

`Fig. 8` uses the tree structures `petree1-4` and their corresponding pattern structures from `PETree.java`.

`Fig. 9(a)` uses the tree structures `petree1-4` and  `petree5-8`  and their corresponding pattern structures from `PETree.java`.

`Fig. 9(b)` and `Fig. 9(b) ` use the tree structures `petree1,5 and 11` and their corresponding pattern structures from `PETree.java`.

`Fig. 9(c)` uses the tree structure `petree1` and its corresponding pattern structure from `PETree.java`.

`Fig. 10(c)` and `Fig. 10(d)` use the tree structures `petree1`, `petree12`, and `petree13` and their corresponding pattern structures from `PETree.java`.

`Fig. 11(a)` uses the tree structure `petree1` and its corresponding pattern structure from `PETree.java`.

`Fig. 11(b)` uses the tree structures `petree12` and `petree13` and their corresponding pattern structures from `PETree.java`.

