package com.example.swrl.date;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.swrlapi.factory.SWRLAPIFactory;
import org.swrlapi.sqwrl.SQWRLQueryEngine;
import org.swrlapi.sqwrl.SQWRLResult;

/**
 * Simple smoke test demonstrating that the project can load the ontology and run a trivial SQWRL query.
 * (Does not directly exercise built-ins here since that typically requires embedding them in a rule engine run.)
 */
public class DateBuiltInSmokeTest {
    public static void main(String[] args) throws Exception {
        OWLOntologyManager m = OWLManager.createOWLOntologyManager();
        OWLOntology o = m.createOntology();
        SQWRLQueryEngine q = SWRLAPIFactory.createSQWRLQueryEngine(o);
        SQWRLResult r = q.runSQWRLQuery("test", "swrlb:add(?x, 2, 3) -> sqwrl:select(?x)");
        if (r.next()) {
            System.out.println("Result ?x = " + r.getLiteral("x").getInteger());
        }
        System.out.println("Smoke test complete. Built-ins library class: " + DateBuiltInLibrary.class.getName());
    }
}
