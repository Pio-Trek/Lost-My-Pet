package graded.unit.lostmypetwebapp.util;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.web.servlet.view.AbstractView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.util.Map;

/**
 * This is the abstract pfd view class it is used to create a helper class for generating PDF documents.
 * It extends from {@link AbstractView}
 */
public abstract class AbstractPdfView extends AbstractView {

    AbstractPdfView() {
        setContentType("application/pdf");
    }

    /**
     * This is the override method that return generates download content which is the PDF document.
     */
    @Override
    protected boolean generatesDownloadContent() {
        return true;
    }

    /**
     * This is the override method that provides support for static attributes and configuration
     * of the generated pdf view.
     *
     * @param model This is the model map.
     * @param request  This is current HTTP request
     * @param response This is the HTTP response we are building.
     * @throws Exception If rendering failed.
     */
    @Override
    protected final void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {

        // IE workaround: write into byte array first.
        ByteArrayOutputStream baos = createTemporaryOutputStream();

        // Apply preferences and build metadata.
        Document document = new Document(PageSize.A4.rotate(), 20, 20, 20, 20);
        PdfWriter writer = PdfWriter.getInstance(document, baos);
        writer.setViewerPreferences(getViewerPreferences());

        // Build PDF document.
        document.open();
        buildPdfDocument(model, document, writer, request, response);
        document.close();

        // Flush to HTTP response.
        response.setHeader("Content-Disposition", ReportFileName.attachmentName());    // make browser to ask for download/display
        writeToResponse(response, baos);
    }


    /**
     * View document preferences.
     */
    private int getViewerPreferences() {
        return PdfWriter.ALLOW_PRINTING | PdfWriter.PageLayoutSinglePage;
    }


    /**
     * Build the PDF document from the parameters.
     */
    protected abstract void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer,
                                             HttpServletRequest request, HttpServletResponse response) throws Exception;
}
