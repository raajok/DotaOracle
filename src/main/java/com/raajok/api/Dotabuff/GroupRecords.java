package com.raajok.api.Dotabuff;

import java.io.IOException;
import java.util.List;

/**
 * Best records of a Group.
 */
public class GroupRecords {

    private final List<Records> records;

    public GroupRecords(List<Records> records) {
        this.records = records;
    }

    /**
     * Builds the records of the group.
     * @return Map<String, Record> where String is the author and Record is the record.
     * @throws IOException if connecting to Dotabuff fails
     */
    public List<Record> records() throws IOException {
        List<Record> filteredRecords = records.get(0).records();
        for (int i = 1; i < records.size(); i++) { // loop all Records
            List<Record> playerRecords = records.get(i).records();
            for (int j = 0; j < playerRecords.size(); j++) { // loop each Record in Records
                if (playerRecords.get(j).isGreater(filteredRecords.get(j))) {
                    filteredRecords.set(j, playerRecords.get(j));
                }
            }
        }
        return filteredRecords;
    }
}
