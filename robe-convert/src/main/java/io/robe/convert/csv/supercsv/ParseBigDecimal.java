package io.robe.convert.csv.supercsv;

import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.util.CsvContext;

import java.math.BigDecimal;
import java.text.DecimalFormatSymbols;

/**
 * Provides a parser for BigDecimal. This is a kind of fix for {@link org.supercsv.cellprocessor.ParseBigDecimal}.
 */
public class ParseBigDecimal extends org.supercsv.cellprocessor.ParseBigDecimal {
    /**
     * Constructs a new <tt>ParseBigDecimal</tt> processor, which converts a String to a BigDecimal.
     */
    public ParseBigDecimal() {
    }

    /**
     * Constructs a new <tt>ParseBigDecimal</tt> processor, which converts a String to a BigDecimal using the supplied
     * <tt>DecimalFormatSymbols</tt> object to convert any decimal separator to a "." before creating the BigDecimal.
     *
     * @param symbols the decimal format symbols, containing the decimal separator
     * @throws NullPointerException if symbols is null
     */
    public ParseBigDecimal(DecimalFormatSymbols symbols) {
        super(symbols);
    }

    /**
     * Constructs a new <tt>ParseBigDecimal</tt> processor, which converts a String to a BigDecimal then calls the next
     * processor in the chain.
     *
     * @param next the next processor in the chain
     * @throws NullPointerException if next is null
     */
    public ParseBigDecimal(CellProcessor next) {
        super(next);
    }

    /**
     * Constructs a new <tt>ParseBigDecimal</tt> processor, which converts a String to a BigDecimal using the supplied
     * <tt>DecimalFormatSymbols</tt> object to convert any decimal separator to a "." before creating the BigDecimal,
     * then calls the next processor in the chain.
     *
     * @param symbols the decimal format symbols, containing the decimal separator
     * @param next    the next processor in the chain
     * @throws NullPointerException if symbols or next is null
     */
    public ParseBigDecimal(DecimalFormatSymbols symbols, CellProcessor next) {
        super(symbols, next);
    }

    @Override
    public Object execute(Object value, CsvContext context) {
        validateInputNotNull(value, context);

        // FIX: If it is already bigdecimal forward it.
        final BigDecimal result;
        if (value instanceof BigDecimal) {
            result = (BigDecimal) value;
            return next.execute(result, context);
        } else {
            return super.execute(value, context);
        }
    }
}
