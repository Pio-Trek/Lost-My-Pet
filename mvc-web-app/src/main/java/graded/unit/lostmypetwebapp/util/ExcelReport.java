package graded.unit.lostmypetwebapp.util;

import graded.unit.lostmypetwebapp.model.pets.Cat;
import graded.unit.lostmypetwebapp.model.pets.Dog;
import graded.unit.lostmypetwebapp.model.pets.Pet;
import graded.unit.lostmypetwebapp.model.posts.Found;
import graded.unit.lostmypetwebapp.model.posts.Lost;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.util.HSSFColor.HSSFColorPredefined;
import org.apache.poi.ss.usermodel.*;
import org.springframework.web.servlet.view.document.AbstractXlsView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * This is the excel view class it is used to create a helper class for generating Excel documents.
 * It extends from {@link AbstractXlsView}
 */
@SuppressWarnings("unchecked")
public class ExcelReport extends AbstractXlsView {

    private CellStyle rowStyle;

    /**
     * This is the override method used to build the Excel document.
     *
     * @param model    This is the model map.
     * @param workbook This is the Excel workbook to populate.
     * @param request  This is current HTTP request.
     * @param response This is the HTTP response we are building.
     */
    @Override
    protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) {

        response.setHeader("Content-Disposition", ReportFileName.attachmentName());

        String datePattern = "yyyy-MM-dd";

        // create style for header cells
        CellStyle headerStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 12);
        headerStyle.setFillForegroundColor(HSSFColorPredefined.GREY_50_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        font.setBold(true);
        font.setColor(HSSFColorPredefined.WHITE.getIndex());
        headerStyle.setFont(font);

        // create style for row cells
        rowStyle = workbook.createCellStyle();
        Font rowFont = workbook.createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 12);
        rowStyle.setFont(rowFont);

        // create sheet for Lost Announcements
        if (model.get("lostList") != null) {
            List<Lost> lostList = (List<Lost>) model.get("lostList");
            Sheet lostSheet = workbook.createSheet("Lost Announcement");
            lostSheet.setDefaultColumnWidth(20);
            createLostHeaders(headerStyle, lostSheet);
            createLostRows(datePattern, lostList, lostSheet);
        }
        // create sheet for Found Announcements
        if (model.get("foundList") != null) {
            List<Found> foundList = (List<Found>) model.get("foundList");
            Sheet foundSheet = workbook.createSheet("Found Announcement");
            foundSheet.setDefaultColumnWidth(20);
            createFoundHeaders(headerStyle, foundSheet);
            createFoundRows(datePattern, foundList, foundSheet);
        }

    }

    /**
     * This method will create a header in a table for the lost announcements.
     *
     * @param headerStyle All header style information.
     * @param lostSheet   Lost announcement workbook sheet.
     */
    private void createLostHeaders(CellStyle headerStyle, Sheet lostSheet) {
        Row lostHeader = lostSheet.createRow(0);
        lostHeader.createCell(0).setCellValue("Lost ID");
        lostHeader.getCell(0).setCellStyle(headerStyle);
        lostHeader.createCell(1).setCellValue("Added date");
        lostHeader.getCell(1).setCellStyle(headerStyle);
        lostHeader.createCell(2).setCellValue("Lost date");
        lostHeader.getCell(2).setCellStyle(headerStyle);
        lostHeader.createCell(3).setCellValue("Lost location");
        lostHeader.getCell(3).setCellStyle(headerStyle);
        lostHeader.createCell(4).setCellValue("Owner name");
        lostHeader.getCell(4).setCellStyle(headerStyle);
        lostHeader.createCell(5).setCellValue("Owner email");
        lostHeader.getCell(5).setCellStyle(headerStyle);
        lostHeader.createCell(6).setCellValue("Pet type");
        lostHeader.getCell(6).setCellStyle(headerStyle);
        lostHeader.createCell(7).setCellValue("Pet breed");
        lostHeader.getCell(7).setCellStyle(headerStyle);
        lostHeader.createCell(8).setCellValue("Pet name");
        lostHeader.getCell(8).setCellStyle(headerStyle);
        lostHeader.createCell(9).setCellValue("Pet gender");
        lostHeader.getCell(9).setCellStyle(headerStyle);
        lostHeader.createCell(10).setCellValue("Pet colours");
        lostHeader.getCell(10).setCellStyle(headerStyle);
        lostHeader.createCell(11).setCellValue("Pet chipped?");
        lostHeader.getCell(11).setCellStyle(headerStyle);
        lostHeader.createCell(12).setCellValue("Pet have a collar?");
        lostHeader.getCell(12).setCellStyle(headerStyle);
        lostHeader.createCell(13).setCellValue("Description");
        lostHeader.getCell(13).setCellStyle(headerStyle);
    }

    /**
     * Create rows in a table with the data about the lost announcements.
     *
     * @param datePattern This is the date format pattern.
     * @param lostList    List of all {@link Lost} announcements.
     * @param lostSheet   Lost announcement workbook sheet.
     */
    private void createLostRows(String datePattern, List<Lost> lostList, Sheet lostSheet) {
        int rowNum = 1;
        for (Lost lost : lostList) {
            Pet pet = lost.getPet();
            Row row = lostSheet.createRow(rowNum++);
            row.createCell(0).setCellValue(lost.getId());
            row.getCell(0).setCellStyle(rowStyle);
            row.createCell(1).setCellValue(new SimpleDateFormat(datePattern).format(lost.getAddedDate()));
            row.getCell(1).setCellStyle(rowStyle);
            row.createCell(2).setCellValue(new SimpleDateFormat(datePattern).format(lost.getLostDate()));
            row.getCell(2).setCellStyle(rowStyle);
            row.createCell(3).setCellValue(lost.getLocation().getName());
            row.getCell(3).setCellStyle(rowStyle);
            row.createCell(4).setCellValue(lost.getMember().getFirstName() + lost.getMember().getLastName());
            row.getCell(4).setCellStyle(rowStyle);
            row.createCell(5).setCellValue(lost.getMember().getEmail());
            row.getCell(5).setCellStyle(rowStyle);
            setPetTypeAndBreed(row, pet);
            row.createCell(8).setCellValue(pet.getName());
            row.getCell(8).setCellStyle(rowStyle);
            row.createCell(9).setCellValue(pet.getGender() == null
                    ? "Gender not specified"
                    : pet.getGender().name());
            row.getCell(9).setCellStyle(rowStyle);
            row.createCell(10).setCellValue(pet.getColours().isEmpty()
                    ? "Colour not specified"
                    : StringUtils.replaceEach(pet.getColours().toString(),
                    new String[]{"[", "]"}, new String[]{"", ""}));
            row.getCell(10).setCellStyle(rowStyle);
            row.createCell(11).setCellValue(pet.getChipped() ? "Yes" : "No");
            row.getCell(11).setCellStyle(rowStyle);
            row.createCell(12).setCellValue(pet.getCollar() ? "Yes" : "No");
            row.getCell(12).setCellStyle(rowStyle);
            row.createCell(13).setCellValue(pet.getDescription());
            row.getCell(13).setCellStyle(rowStyle);
        }
    }

    /**
     * This method will create a header in a table for the found announcements.
     *
     * @param headerStyle All header style information.
     * @param foundSheet  Found announcement workbook sheet.
     */
    private void createFoundHeaders(CellStyle headerStyle, Sheet foundSheet) {
        Row foundHeader = foundSheet.createRow(0);
        foundHeader.createCell(0).setCellValue("Found ID");
        foundHeader.getCell(0).setCellStyle(headerStyle);
        foundHeader.createCell(1).setCellValue("Added date");
        foundHeader.getCell(1).setCellStyle(headerStyle);
        foundHeader.createCell(2).setCellValue("Found date");
        foundHeader.getCell(2).setCellStyle(headerStyle);
        foundHeader.createCell(3).setCellValue("Found location");
        foundHeader.getCell(3).setCellStyle(headerStyle);
        foundHeader.createCell(4).setCellValue("Finder name");
        foundHeader.getCell(4).setCellStyle(headerStyle);
        foundHeader.createCell(5).setCellValue("Finder email");
        foundHeader.getCell(5).setCellStyle(headerStyle);
        foundHeader.createCell(6).setCellValue("Pet type");
        foundHeader.getCell(6).setCellStyle(headerStyle);
        foundHeader.createCell(7).setCellValue("Pet breed");
        foundHeader.getCell(7).setCellStyle(headerStyle);
        foundHeader.createCell(8).setCellValue("Pet gender");
        foundHeader.getCell(8).setCellStyle(headerStyle);
        foundHeader.createCell(9).setCellValue("Pet colours");
        foundHeader.getCell(9).setCellStyle(headerStyle);
        foundHeader.createCell(10).setCellValue("Pet chipped?");
        foundHeader.getCell(10).setCellStyle(headerStyle);
        foundHeader.createCell(11).setCellValue("Pet have a collar?");
        foundHeader.getCell(11).setCellStyle(headerStyle);
        foundHeader.createCell(12).setCellValue("Description");
        foundHeader.getCell(12).setCellStyle(headerStyle);
    }

    /**
     * Create rows in a table with the data about the found announcements.
     *
     * @param datePattern This is the date format pattern.
     * @param foundList   List of all {@link Found} announcements.
     * @param foundSheet  Found announcement workbook sheet.
     */
    private void createFoundRows(String datePattern, List<Found> foundList, Sheet foundSheet) {
        int rowNum = 1;
        for (Found found : foundList) {
            Pet pet = found.getPet();
            Row row = foundSheet.createRow(rowNum++);
            row.createCell(0).setCellValue(found.getId());
            row.getCell(0).setCellStyle(rowStyle);
            row.createCell(1).setCellValue(new SimpleDateFormat(datePattern).format(found.getAddedDate()));
            row.getCell(1).setCellStyle(rowStyle);
            row.createCell(2).setCellValue(new SimpleDateFormat(datePattern).format(found.getFoundDate()));
            row.getCell(2).setCellStyle(rowStyle);
            row.createCell(3).setCellValue(found.getLocation().getName());
            row.getCell(3).setCellStyle(rowStyle);
            row.createCell(4).setCellValue(found.getMember().getFirstName() + found.getMember().getLastName());
            row.getCell(4).setCellStyle(rowStyle);
            row.createCell(5).setCellValue(found.getMember().getEmail());
            row.getCell(5).setCellStyle(rowStyle);
            setPetTypeAndBreed(row, pet);
            row.createCell(8).setCellValue(pet.getGender().name());
            row.getCell(8).setCellStyle(rowStyle);
            row.createCell(9).setCellValue(pet.getColours().isEmpty()
                    ? "Colour not specified"
                    : StringUtils.replaceEach(pet.getColours().toString(),
                    new String[]{"[", "]"}, new String[]{"", ""}));
            row.getCell(9).setCellStyle(rowStyle);
            row.createCell(10).setCellValue(pet.getChipped() ? "Yes" : "No");
            row.getCell(10).setCellStyle(rowStyle);
            row.createCell(11).setCellValue(pet.getCollar() ? "Yes" : "No");
            row.getCell(11).setCellStyle(rowStyle);
            row.createCell(12).setCellValue(pet.getDescription());
            row.getCell(12).setCellStyle(rowStyle);
        }
    }

    /**
     * This is a helper method that allows to determinate if the pet is an instance of
     * {@link Dog} or {@link Cat} object and display the current pet breed information.
     * It is used for creating table rows for lost and found announcements.
     */
    private void setPetTypeAndBreed(Row row, Pet pet) {
        if (pet instanceof Dog) {
            row.createCell(6).setCellValue("Dog");
            row.getCell(6).setCellStyle(rowStyle);
            Dog dog = (Dog) pet;
            row.createCell(7).setCellValue(dog.getBreed() == null
                    ? "Breed not specified"
                    : dog.getBreed().getName());
            row.getCell(7).setCellStyle(rowStyle);
        } else if (pet instanceof Cat) {
            row.createCell(6).setCellValue("Cat");
            row.getCell(6).setCellStyle(rowStyle);
            Cat cat = (Cat) pet;
            row.createCell(7).setCellValue(cat.getBreed() == null
                    ? "Breed not specified"
                    : cat.getBreed().getName());
            row.getCell(7).setCellStyle(rowStyle);
        }
    }

}