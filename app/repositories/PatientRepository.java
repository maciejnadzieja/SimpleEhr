package repositories;

import com.google.common.base.Charsets;
import com.google.common.base.Predicate;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import models.Patient;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class PatientRepository {
    private static Map<String, Patient> patients = Maps.newHashMap();

    static {
        try {
            List<String> lines = Files.readLines(new File("conf/demo_patients.csv"), Charsets.UTF_8);
            for (String line : lines) {
                List<String> strings = Lists.newLinkedList(Splitter.on("|").split(line));
                patients.put(strings.get(0), new Patient(strings.get(1), strings.get(2), strings.get(0), strings.get(3)));
            }
        } catch (IOException e) {
            throw new RuntimeException("can't create repository", e);
        }
    }

    public QueryResult find(final PatientQuery query) {
        Map<String, Patient> filteredPatients;

        if (query.getQuery() == null) {
            filteredPatients = patients;
        } else {
            filteredPatients = Maps.filterEntries(patients, new Predicate<Map.Entry<String, Patient>>() {
                @Override
                public boolean apply(Map.Entry<String, Patient> input) {
                    return input.getValue().firstName.contains(query.getQuery()) ||
                            input.getValue().lastName.contains(query.getQuery()) ||
                            input.getValue().id.contains(query.getQuery()) ||
                            String.format("%s %s (%s)", input.getValue().firstName, input.getValue().lastName, input.getValue().id).equals(query.getQuery());
                }
            });
        }

        List<Patient> items = Lists.newArrayList(filteredPatients.values()).subList(
                Math.min(query.getStart(), filteredPatients.size()),
                Math.min(query.getStop(), filteredPatients.size())
        );

        return new QueryResult<Patient>(
                items,
                filteredPatients.size()
        );
    }

    public Patient findById(String id) {
        return patients.get(id);
    }

    public void add(Patient patient) {
        patients.put(patient.id, patient);
    }
}
