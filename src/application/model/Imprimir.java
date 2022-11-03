package application.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.PageRanges;

import org.apache.log4j.BasicConfigurator;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;
import org.apache.pdfbox.printing.PDFPrintable;

import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import application.Main;

import java.awt.print.*;

public class Imprimir {
    private String tamanho = "";
    private int rotation = 1;
    private File file;
    //Variaveis para ser usado no setSize
    final public static int A6 = 3,A5 = 2, A4 = 1;
    //Tamnho A4 padrão
    private double width = 8.27,hight=11.69;
    public Imprimir(){}

    //Instancia o objeto passando a rotação e o arquivo a ser imprimido
    public Imprimir(int rotation, File file){
        this.rotation = rotation;
        this.file = file;
    }

    //Instancia o objeto passando o tamanho a rotacao e o arquivo a ser imprimido
    public Imprimir(String tamanho, int rotation, File file){
        if(tamanho.equals("A4")){
                setSize(A4);
        }else if(tamanho.equals("A5")){
                setSize(A5);
        }else if(tamanho.equals("A6")){
                setSize(A6);
        }
        this.rotation = rotation;
        this.file = file;
    }

    public void imprimir(){
        File pdf = null;
        try {
                 pdf = File.createTempFile("pdf", ".pdf");
                 Converter(file.getPath(), pdf.getPath());
        } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
        }
        
            try (PDDocument document = PDDocument.load(pdf)) {
                PrinterJob job = PrinterJob.getPrinterJob();
                job.setPageable(new PDFPageable(document));

                // define custom paper
                Paper paper = new Paper();
                //paper.setSize(4.13*72, 5.83*72); // 1/72 inch
                paper.setSize(3.14961*72, 3.14961*72);
                paper.setImageableArea(0, 0, paper.getWidth(), paper.getHeight()); // no margins

                // custom page format
                PageFormat pageFormat = new PageFormat();
                pageFormat.setPaper(paper);
            
                if(rotation == 1){
                        pageFormat.setOrientation(PageFormat.PORTRAIT);
                }else{
                        pageFormat.setOrientation(PageFormat.LANDSCAPE);
                }
                
        
                // override the page format
                Book book = new Book();
                // append all pages
                book.append(new PDFPrintable(document), pageFormat, document.getNumberOfPages());
                job.setPageable(book);
               // PrintRequestAttributeSet att = new HashPrintRequestAttributeSet(new PageRanges(1,30));
                
                BasicConfigurator.configure();
            
                job.print();
                
            
        } catch (NullPointerException | IOException | PrinterException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Main.dialogBox("Erro ao imprimir o documento!", 1);
        }
    }

   

    //Seta o tamanho do papel em tamanho inch
    public void setSize(int Size){
        switch(Size){
                //A4 
                case 1 :
                        width = 8.27;
                        hight = 11.69;
                        break;
                //A5
                case 2 :
                        width = 5.83;
                        hight= 8.27;
                        break;
                //A6
                case 3 :
                        width = 4.13;
                        hight = 5.83;
                        break;

        }
    }

    private void Converter(String input, String pdf){
        try { 
                FileReader reader = new FileReader(input);
                BufferedReader leitor = new BufferedReader(reader);
                
                //criação do pdf                         //  (Es, Dir,Sup,Inf)
                Document document = new Document(PageSize.A4,0,0,150,0);
                PdfWriter.getInstance(document, new FileOutputStream(pdf)); 
                document.open(); 
                
                //Ler txt linha a linha 
                while(leitor.ready()){
                        document.add(new Paragraph(leitor.readLine(),FontFactory.getFont(FontFactory.HELVETICA,48, Font.BOLD)));
                }
                leitor.close(); 
                document.close(); 
        } catch (Exception e){
                e.printStackTrace();
        } 
    }
}
