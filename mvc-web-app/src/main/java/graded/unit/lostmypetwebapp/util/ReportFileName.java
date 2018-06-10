package graded.unit.lostmypetwebapp.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This is the helper class used to generate a report file name.
 */
public class ReportFileName {

    /**
     * This method will generate report file name withe the date in specific date format.
     *
     * @return Document header with the file name.
     */
    public static String attachmentName() {
        String fileDateName = new SimpleDateFormat("yyyy-MM-dd_HH-mm").format(new Date());
        return "attachment;filename=\"Report-" + fileDateName + "\"";
    }
}
