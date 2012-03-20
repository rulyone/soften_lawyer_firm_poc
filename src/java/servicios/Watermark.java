/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servicios;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

/**
 *
 * @author rulyone
 */
public class Watermark extends PdfPageEventHelper {

    private Font FONT = new Font(FontFamily.HELVETICA, 52, Font.BOLD, new GrayColor(0.75f));

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        ColumnText.showTextAligned(writer.getDirectContentUnder(), Element.ALIGN_CENTER, new Phrase("Servicios y Asesorias Legales AMB", FONT), 297.5f, 421, writer.getPageNumber() % 2 == 1 ? 45 : -45);
    }
}