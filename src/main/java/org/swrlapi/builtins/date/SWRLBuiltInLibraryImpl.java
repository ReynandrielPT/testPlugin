package org.swrlapi.builtins.date;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.swrlapi.builtins.AbstractSWRLBuiltInLibrary;
import org.swrlapi.builtins.arguments.SWRLBuiltInArgument;
import org.swrlapi.exceptions.SWRLBuiltInException;
import org.swrlapi.exceptions.SWRLBuiltInLibraryException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import org.swrlapi.literal.XSDDate;

/**
 * SWRL date built-in library implementation using the discovery pattern required by SWRLBuiltInLibraryManager.
 * Class name MUST be: org.swrlapi.builtins.<prefix>.SWRLBuiltInLibraryImpl where prefix == 'date'.
 * Built-ins provided (prefixed with 'date:'):
 *   year(?year, ?date)
 *   month(?month, ?date)
 *   day(?day, ?date)
 *   nowDate(?date)
 *   nowYear(?year)
 *   nowMonth(?month)
 *   nowDay(?day)
 */
public class SWRLBuiltInLibraryImpl extends AbstractSWRLBuiltInLibrary {

    private static final String PREFIX = "date";
    private static final String NAMESPACE = "http://example.com/swrl/date#";
    private static final Set<String> BUILT_INS = new HashSet<>(Arrays.asList(
            "year", "month", "day", "nowDate", "nowYear", "nowMonth", "nowDay"
    ));

    public SWRLBuiltInLibraryImpl() {
        super(PREFIX, NAMESPACE, BUILT_INS);
    }

    @Override
    public void reset() throws SWRLBuiltInLibraryException {
        // Stateless; nothing to clear.
    }

    // Utility: parse xsd:date or xsd:dateTime literal lex form to LocalDate.
    private LocalDate toLocalDate(OWLLiteral literal) throws SWRLBuiltInException {
        String lex = literal.getLiteral();
        try {
            if (lex.contains("T")) {
                // Trim trailing Z if present (basic support)
                String cleaned = lex.replaceFirst("Z$", "");
                LocalDateTime ldt = LocalDateTime.parse(cleaned);
                return ldt.toLocalDate();
            } else {
                return LocalDate.parse(lex);
            }
        } catch (Exception e) {
            throw new SWRLBuiltInException("Unable to parse date literal '" + lex + "' as xsd:date or xsd:dateTime", e);
        }
    }

    private boolean bindOrCheckInt(@NonNull List<@NonNull SWRLBuiltInArgument> args, int index, int value) throws SWRLBuiltInException {
        if (isUnboundArgument(index, args)) {
            return processResultArgument(args, index, createLiteralBuiltInArgument(value));
        } else {
            checkThatArgumentIsNumeric(index, args);
            int existing = getArgumentAsAnInt(index, args);
            return existing == value;
        }
    }

    private boolean bindOrCheckDate(@NonNull List<@NonNull SWRLBuiltInArgument> args, int index, LocalDate value) throws SWRLBuiltInException {
        if (isUnboundArgument(index, args)) {
            // Produce an xsd:date typed literal using XSDDate wrapper
            return processResultArgument(args, index, createLiteralBuiltInArgument(new XSDDate(value.toString())));
        } else {
            // Accept bound literal (string, xsd:date, or xsd:dateTime); parse and compare.
            checkThatArgumentIsALiteral(index, args);
            LocalDate bound = toLocalDate(getArgumentAsAnOWLLiteral(index, args));
            return bound.equals(value);
        }
    }

    private OWLLiteral getDateLiteral(@NonNull List<@NonNull SWRLBuiltInArgument> arguments, int dateArgIndex) throws SWRLBuiltInException {
        checkThatArgumentIsALiteral(dateArgIndex, arguments);
        return getArgumentAsAnOWLLiteral(dateArgIndex, arguments);
    }

    // Built-in: date:year(?year, ?dateLiteral)
    public boolean year(@NonNull List<@NonNull SWRLBuiltInArgument> arguments) throws SWRLBuiltInException {
        checkNumberOfArgumentsEqualTo(2, arguments.size());
        OWLLiteral dateLiteral = getDateLiteral(arguments, 1);
        LocalDate date = toLocalDate(dateLiteral);
        return bindOrCheckInt(arguments, 0, date.getYear());
    }

    // Built-in: date:month(?month, ?dateLiteral) (1-12)
    public boolean month(@NonNull List<@NonNull SWRLBuiltInArgument> arguments) throws SWRLBuiltInException {
        checkNumberOfArgumentsEqualTo(2, arguments.size());
        OWLLiteral dateLiteral = getDateLiteral(arguments, 1);
        LocalDate date = toLocalDate(dateLiteral);
        return bindOrCheckInt(arguments, 0, date.getMonthValue());
    }

    // Built-in: date:day(?day, ?dateLiteral) (1-31)
    public boolean day(@NonNull List<@NonNull SWRLBuiltInArgument> arguments) throws SWRLBuiltInException {
        checkNumberOfArgumentsEqualTo(2, arguments.size());
        OWLLiteral dateLiteral = getDateLiteral(arguments, 1);
        LocalDate date = toLocalDate(dateLiteral);
        return bindOrCheckInt(arguments, 0, date.getDayOfMonth());
    }

    // Built-in: date:nowYear(?year)
    public boolean nowYear(@NonNull List<@NonNull SWRLBuiltInArgument> arguments) throws SWRLBuiltInException {
        checkNumberOfArgumentsEqualTo(1, arguments.size());
        int year = LocalDate.now(ZoneId.systemDefault()).getYear();
        return bindOrCheckInt(arguments, 0, year);
    }

    // Built-in: date:nowMonth(?month)
    public boolean nowMonth(@NonNull List<@NonNull SWRLBuiltInArgument> arguments) throws SWRLBuiltInException {
        checkNumberOfArgumentsEqualTo(1, arguments.size());
        int month = LocalDate.now(ZoneId.systemDefault()).getMonthValue();
        return bindOrCheckInt(arguments, 0, month);
    }

    // Built-in: date:nowDay(?day)
    public boolean nowDay(@NonNull List<@NonNull SWRLBuiltInArgument> arguments) throws SWRLBuiltInException {
        checkNumberOfArgumentsEqualTo(1, arguments.size());
        int day = LocalDate.now(ZoneId.systemDefault()).getDayOfMonth();
        return bindOrCheckInt(arguments, 0, day);
    }

    // Built-in: date:nowDate(?date)
    public boolean nowDate(@NonNull List<@NonNull SWRLBuiltInArgument> arguments) throws SWRLBuiltInException {
        checkNumberOfArgumentsEqualTo(1, arguments.size());
        LocalDate today = LocalDate.now(ZoneId.systemDefault());
        return bindOrCheckDate(arguments, 0, today);
    }
}
