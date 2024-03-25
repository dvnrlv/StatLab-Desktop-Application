package com.example.ia_fxgui.services;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.IOException;
import java.net.MalformedURLException;
import java.time.LocalDate;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.File;
import com.itextpdf.text.Image;



public class PDFCreator {

    static Object[][] resultStatArray = StatFunctions.resultStatArray;


    static void createPDF(String chartImagePath, String pdfDest){

        String fileName = (String)resultStatArray[0][1];
        fileName = fileName.replace(".csv","");
        String fileDate = LocalDate.now().toString();

        Document document = new Document();
        try
        {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pdfDest));
            document.open();

            document.addAuthor(user.getName);
            Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
            document.add(new Paragraph("Laboratory Report:"+fileName + ", " + fileDate));



            //ImageData data = ImageDataFactory.create(chartImagePath);
            //Image image = new Image(data);


            Image image = Image.getInstance(chartImagePath);
            // Set formatting properties

            // Add the image to the document
            image.scaleToFit(100f, 200f);

            document.add(image);




            PdfPTable table = new PdfPTable(2); // 3 columns.
            table.setWidthPercentage(100); //Width 100%
            table.setSpacingBefore(10f); //Space before table
            table.setSpacingAfter(10f); //Space after table

            //Set Column widths
            float[] columnWidths = {1f, 1f, 1f};
            table.setWidths(columnWidths);

            PdfPCell[][] cellArray = new PdfPCell[resultStatArray.length][2];
            for (int i = 0; i <  2; i++) {

                for (int j = 0; j < resultStatArray.length; j++) {

                    cellArray[j][i]=new PdfPCell(new Paragraph((String)resultStatArray[j][i]));
                    cellArray[j][i].setBorderColor(BaseColor.BLUE);
                    cellArray[j][i].setPaddingLeft(10);
                    cellArray[j][i].setHorizontalAlignment(Element.ALIGN_CENTER);
                    cellArray[j][i].setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cellArray[j][i].setUseBorderPadding(true); //To avoid having the cell border and the content overlap, if you are having thick cell borders
                    table.addCell(cellArray[j][i]);
                }

            }





            document.add(table);
            document.close();
            writer.close();


            try {
                // Get the file object representing the resource
                File resourceFile = new File(chartImagePath);

                // Delete the image
                if (resourceFile.exists()) {
                    boolean deleted = resourceFile.delete();
                    if (deleted) {
                        System.out.println("File deleted successfully.");
                    } else {
                        System.out.println("Failed to delete the file.");
                    }
                } else {
                    System.out.println("File does not exist.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }



        } catch (DocumentException e)
        {
            e.printStackTrace();
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}