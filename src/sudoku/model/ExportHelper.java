package sudoku.model;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by henne on 31.10.16.
 */
public class ExportHelper {

    public static ArrayList<int[][]> readFromFile(String name, boolean hasSolution) {
        ArrayList<int[][]> result = new ArrayList<int[][]>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(name));
            reader.readLine();
            boolean nextSudoku = true;
            while (nextSudoku) {
                int[][] grid = new int[9][9];
                int[][] solution = new int[9][9];
                for (int i = 0; i < 9; i++) {
                    if (hasSolution) {
                        for (int j = 0; j < 9; j++) {
                            grid[j][i] = Character.getNumericValue(reader.read());
                        }
                        for (int k = 0; k < 3; k++) {
                            reader.read();
                        }
                        for (int j = 0; j < 9; j++) {
                            solution[j][i] = Character.getNumericValue(reader.read());
                        }
                        reader.readLine();
                    } else {
                        for (int j = 0; j < 9; j++) {
                            grid[j][i] = Character.getNumericValue(reader.read());
                        }
                        reader.readLine();
                    }
                }
                result.add(grid);
                if (hasSolution) {
                    result.add(solution);
                }
                reader.readLine();
                if (hasSolution) {
                    int tmp = reader.read();
                    nextSudoku = (tmp == 'O' || tmp == 'S');
                } else {
                    nextSudoku = (reader.read() == 'S');
                }
                reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    public void saveToFile(String name, boolean printSolution) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(name, true));
            if (printSolution) {
                writer.write("Original    Solution");
                writer.newLine();
            } else {
                writer.write("Sudoku");
                writer.newLine();
            }
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
//                    writer.write(new Integer(originalGrid[j][i]).toString());
                }
                if (printSolution) {
                    writer.write("   ");
                    for (int j = 0; j < 9; j++) {
//                        writer.write(new Integer(grid[j][i]).toString());
                    }
                }
                writer.newLine();
            }
            writer.newLine();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void exportAsPDF(String name) {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream("pdf/" + name));
            document.open();

            PdfPTable table = new PdfPTable(9);
            table.setTotalWidth(360);
            table.setLockedWidth(true);
            Font font = FontFactory.getFont(FontFactory.HELVETICA, 30, Font.BOLD);
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
//                    String number = new Integer(grid[j][i]).toString();
//                    if (grid[j][i] == 0)
//                        number = "";
                    PdfPCell cell = new PdfPCell(new Phrase(""/*number*/, font));
                    cell.setFixedHeight(40);
                    cell.setVerticalAlignment(Element.ALIGN_CENTER);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);

                    if (i == 8 || j == 8) {
                        cell.setBorder(com.itextpdf.text.Rectangle.RIGHT | com.itextpdf.text.Rectangle.BOTTOM | com
                                .itextpdf.text.Rectangle.LEFT | com.itextpdf.text.Rectangle.TOP);
                    } else {
                        cell.setBorder(com.itextpdf.text.Rectangle.LEFT | com.itextpdf.text.Rectangle.TOP);
                    }

                    if (i == 0 || i % 3 == 0) {
                        cell.setBorderWidthTop(2);
                        cell.setBorderWidthBottom(0.5f);
                    } else if (i == 8) {
                        cell.setBorderWidthBottom(2);
                        cell.setBorderWidthTop(0.5f);
                    } else {
                        cell.setBorderWidthTop(0.5f);
                        cell.setBorderWidthBottom(0.5f);
                    }
                    if (j == 0 || j % 3 == 0) {
                        cell.setBorderWidthLeft(2);
                        cell.setBorderWidthRight(0.5f);
                    } else if (j == 8) {
                        cell.setBorderWidthRight(2);
                        cell.setBorderWidthLeft(0.5f);
                    } else {
                        cell.setBorderWidthLeft(0.5f);
                        cell.setBorderWidthRight(0.5f);
                    }

                    table.addCell(cell);
                }
            }

            document.add(table);
            document.close();
            System.out.println("Sudoku exported to PDF: " + name);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
