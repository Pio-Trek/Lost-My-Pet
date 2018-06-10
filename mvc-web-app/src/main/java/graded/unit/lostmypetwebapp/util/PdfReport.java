package graded.unit.lostmypetwebapp.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import graded.unit.lostmypetwebapp.model.pets.Cat;
import graded.unit.lostmypetwebapp.model.pets.Dog;
import graded.unit.lostmypetwebapp.model.pets.Pet;
import graded.unit.lostmypetwebapp.model.posts.Found;
import graded.unit.lostmypetwebapp.model.posts.Lost;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * This is the pdf view class it is used to generate PDF documents.
 * It extends from {@link AbstractPdfView}
 */
@SuppressWarnings("unchecked")
public class PdfReport extends AbstractPdfView {

    private DateFormat df = new SimpleDateFormat("yyyy/MM/dd");

    /**
     * This method will build the PDF document from the parameters.
     *
     * @param model    This is the model map.
     * @param document This is a generic pdf document.
     * @param writer   This is the pdf writer class. Every element added to this Document
     *                 will be written to the outputstream.
     * @param request  This is current HTTP request.
     * @param response This is the HTTP response we are building.
     */
    @Override
    protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer, HttpServletRequest request, HttpServletResponse response) throws Exception {


        // define font for table header row
        Font fontHeader = FontFactory.getFont(FontFactory.HELVETICA);
        fontHeader.setColor(BaseColor.WHITE);
        fontHeader.setSize(10f);

        // define font for table cells
        Font fontTable = FontFactory.getFont(FontFactory.HELVETICA);
        fontTable.setColor(BaseColor.BLACK);
        fontTable.setSize(9f);

        // define table header cell
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(BaseColor.GRAY);
        cell.setPadding(5);

        if (model.get("lostList") != null) {
            List<Lost> lostList = (List<Lost>) model.get("lostList");
            getLostPetsTable(document, fontHeader, fontTable, cell, lostList);
        }

        if (model.get("foundList") != null) {
            List<Found> foundList = (List<Found>) model.get("foundList");
            getFoundPetsTable(document, fontHeader, fontTable, cell, foundList);
        }

    }

    /**
     * This method will create the table for the lost pet announcements.
     *
     * @param document   This is a generic pdf document.
     * @param fontHeader This is the style for the header.
     * @param fontTable  This is the style for the table.
     * @param cell       This is a cell in the pdf table.
     * @param lostList   This is the list of a lost announcements.
     * @throws DocumentException When an error has occurred in a Document.
     */
    private void getLostPetsTable(Document document, Font fontHeader, Font fontTable, PdfPCell cell, List<Lost> lostList) throws DocumentException {
        document.add(new Paragraph("Missing Pets"));

        PdfPTable tableLost = new PdfPTable(13);
        tableLost.setWidthPercentage(100.0f);
        tableLost.setWidths(new float[]{0.6f, 1.2f, 1.2f, 1.6f, 1.6f, 1.8f, 0.8f, 1.8f, 1.2f, 1.0f, 1.5f, 1.0f, 0.9f});
        tableLost.setSpacingBefore(10);


        // write tableLost header
        cell.setPhrase(new Phrase("ID", fontHeader));
        tableLost.addCell(cell);

        cell.setPhrase(new Phrase("Added", fontHeader));
        tableLost.addCell(cell);

        cell.setPhrase(new Phrase("Lost", fontHeader));
        tableLost.addCell(cell);

        cell.setPhrase(new Phrase("Location", fontHeader));
        tableLost.addCell(cell);

        cell.setPhrase(new Phrase("Owner", fontHeader));
        tableLost.addCell(cell);

        cell.setPhrase(new Phrase("Email", fontHeader));
        tableLost.addCell(cell);

        cell.setPhrase(new Phrase("Type", fontHeader));
        tableLost.addCell(cell);

        cell.setPhrase(new Phrase("Breed", fontHeader));
        tableLost.addCell(cell);

        cell.setPhrase(new Phrase("Name", fontHeader));
        tableLost.addCell(cell);

        cell.setPhrase(new Phrase("Gender", fontHeader));
        tableLost.addCell(cell);

        cell.setPhrase(new Phrase("Colours", fontHeader));
        tableLost.addCell(cell);

        cell.setPhrase(new Phrase("Chipped", fontHeader));
        tableLost.addCell(cell);

        cell.setPhrase(new Phrase("Collar", fontHeader));
        tableLost.addCell(cell);


        // write tableLost row data
        for (Lost lost : lostList) {
            Pet pet = lost.getPet();
            tableLost.addCell(new Phrase(String.valueOf(lost.getId()), fontTable));
            tableLost.addCell(new Phrase(df.format(lost.getAddedDate()), fontTable));
            tableLost.addCell(new Phrase(df.format(lost.getLostDate()), fontTable));
            tableLost.addCell(new Phrase(lost.getLocation().getName(), fontTable));
            tableLost.addCell(new Phrase(lost.getMember().getFirstName() + ' ' + lost.getMember().getLastName(), fontTable));
            tableLost.addCell(new Phrase(lost.getMember().getEmail(), fontTable));
            setPetTypeAndBreed(tableLost, fontTable, pet);
            tableLost.addCell(new Phrase(pet.getName(), fontTable));
            tableLost.addCell(new Phrase(pet.getGender() == null
                    ? "Gender not specified"
                    : pet.getGender().name(), fontTable));
            tableLost.addCell(new Phrase(pet.getColours().isEmpty()
                    ? "Colour not specified"
                    : StringUtils.replaceEach(pet.getColours().toString(),
                    new String[]{"[", "]"}, new String[]{"", ""}), fontTable));
            tableLost.addCell(new Phrase(pet.getChipped() ? "Yes" : "No", fontTable));
            tableLost.addCell(new Phrase(pet.getCollar() ? "Yes" : "No", fontTable));
        }

        document.add(tableLost);
    }

    /**
     * This method will create the table for the found pet announcements.
     *
     * @param document   This is a generic pdf document.
     * @param fontHeader This is the style for the header.
     * @param fontTable  This is the style for the table.
     * @param cell       This is a cell in the pdf table.
     * @param foundList   This is the list of a found announcements.
     * @throws DocumentException When an error has occurred in a Document.
     */
    private void getFoundPetsTable(Document document, Font fontHeader, Font fontTable, PdfPCell cell, List<Found> foundList) throws DocumentException {
        document.add(new Paragraph("Found Pets"));

        PdfPTable tableFound = new PdfPTable(12);
        tableFound.setWidthPercentage(100.0f);
        tableFound.setWidths(new float[]{0.6f, 1.2f, 1.2f, 1.6f, 1.6f, 1.8f, 0.8f, 1.8f, 1.0f, 1.5f, 1.0f, 0.9f});
        tableFound.setSpacingBefore(10);

        // write tableFound header
        cell.setPhrase(new Phrase("ID", fontHeader));
        tableFound.addCell(cell);

        cell.setPhrase(new Phrase("Added", fontHeader));
        tableFound.addCell(cell);

        cell.setPhrase(new Phrase("Found", fontHeader));
        tableFound.addCell(cell);

        cell.setPhrase(new Phrase("Location", fontHeader));
        tableFound.addCell(cell);

        cell.setPhrase(new Phrase("Owner", fontHeader));
        tableFound.addCell(cell);

        cell.setPhrase(new Phrase("Email", fontHeader));
        tableFound.addCell(cell);

        cell.setPhrase(new Phrase("Type", fontHeader));
        tableFound.addCell(cell);

        cell.setPhrase(new Phrase("Breed", fontHeader));
        tableFound.addCell(cell);

        cell.setPhrase(new Phrase("Gender", fontHeader));
        tableFound.addCell(cell);

        cell.setPhrase(new Phrase("Colours", fontHeader));
        tableFound.addCell(cell);

        cell.setPhrase(new Phrase("Chipped", fontHeader));
        tableFound.addCell(cell);

        cell.setPhrase(new Phrase("Collar", fontHeader));
        tableFound.addCell(cell);

        for (Found found : foundList) {
            Pet pet = found.getPet();
            tableFound.addCell(new Phrase(String.valueOf(found.getId()), fontTable));
            tableFound.addCell(new Phrase(df.format(found.getAddedDate()), fontTable));
            tableFound.addCell(new Phrase(df.format(found.getFoundDate()), fontTable));
            tableFound.addCell(new Phrase(found.getLocation().getName(), fontTable));
            tableFound.addCell(new Phrase(found.getMember().getFirstName() + ' ' + found.getMember().getLastName(), fontTable));
            tableFound.addCell(new Phrase(found.getMember().getEmail(), fontTable));
            setPetTypeAndBreed(tableFound, fontTable, pet);
            tableFound.addCell(new Phrase(pet.getGender().name(), fontTable));
            tableFound.addCell(new Phrase(pet.getColours().isEmpty()
                    ? "Colour not specified"
                    : StringUtils.replaceEach(pet.getColours().toString(),
                    new String[]{"[", "]"}, new String[]{"", ""}), fontTable));
            tableFound.addCell(new Phrase(pet.getChipped() ? "Yes" : "No", fontTable));
            tableFound.addCell(new Phrase(pet.getCollar() ? "Yes" : "No", fontTable));
        }

        document.add(tableFound);
    }

    /**
     * This is a helper method that allows to determinate if the pet is an instance of
     * {@link Dog} or {@link Cat} object and display the current pet breed information.
     * It is used for creating table rows for lost and found announcements.
     */
    private void setPetTypeAndBreed(PdfPTable table, Font fontTable, Pet pet) {
        if (pet instanceof Dog) {
            Dog dog = (Dog) pet;
            table.addCell(new Phrase("Dog", fontTable));
            table.addCell(new Phrase(dog.getBreed() == null
                    ? "Breed not specified"
                    : dog.getBreed().getName(), fontTable));
        } else if (pet instanceof Cat) {
            Cat cat = (Cat) pet;
            table.addCell(new Phrase("Cat", fontTable));
            table.addCell(new Phrase(cat.getBreed() == null
                    ? "Breed not specified"
                    : cat.getBreed().getName(), fontTable));
        }
    }


}
