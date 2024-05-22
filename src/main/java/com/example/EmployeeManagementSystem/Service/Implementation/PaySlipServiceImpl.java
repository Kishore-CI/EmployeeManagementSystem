package com.example.EmployeeManagementSystem.Service.Implementation;

import com.example.EmployeeManagementSystem.Exception.ApiRequestException;
import com.example.EmployeeManagementSystem.Model.Attendance;
import com.example.EmployeeManagementSystem.Model.Employee;
import com.example.EmployeeManagementSystem.Repository.PaySlipRepository;
import com.example.EmployeeManagementSystem.Service.AttendanceService;
import com.example.EmployeeManagementSystem.Service.EarnedSalaryService;
import com.example.EmployeeManagementSystem.Service.EmployeeService;
import com.example.EmployeeManagementSystem.Service.PaySlipService;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.layout.Document;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.YearMonth;
import java.util.*;

@Service
public class PaySlipServiceImpl implements PaySlipService {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private Map<String,EarnedSalaryService> earnedSalaryService;

    @Autowired
    private PaySlipRepository paySlipRepository;

    private Logger log = LoggerFactory.getLogger(PaySlipServiceImpl.class);

    private static final String TEMPLATEPATH =  "C:\\Users\\LENOVO\\Desktop\\Payslip_template.docx";

    @Override
    public byte[] generatePaySlipForEmployee(Long id, Month month, Year year) throws IOException {
//        log the request
        log.info("generatePaySlipForEmployee -> request received");

//        Check if the employee exists
        Employee employee = employeeService.findByEmpId(id);
        if(employee == null){
            throw new ApiRequestException("No employee found for id :"+id, HttpStatus.NOT_FOUND);
        }

//        Validate if the month and year are for current and past dates
        YearMonth yearMonth = YearMonth.of(year.getValue(),month.getValue());
        LocalDate startDate = LocalDate.of(year.getValue(),month.getValue(),1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        LocalDate today = LocalDate.now();

        if(today.isBefore(endDate)){
            throw new ApiRequestException("Cannot generate payslip for future dates. Today's date: "+today+". Last day of the month: "+endDate, HttpStatus.BAD_REQUEST);
        }

//        Check if there are attendance records for the given month and year for the employee
        Attendance firstDay = attendanceService.findAttendanceRecord(employee,startDate);
        if(firstDay == null){
            throw new ApiRequestException("No attendance record found for start of the month: "+startDate, HttpStatus.NOT_FOUND);
        }
        Attendance lastDay = attendanceService.findAttendanceRecord(employee,endDate);
        if(lastDay == null){
            throw new ApiRequestException("No attendance record found for end of the month: "+endDate, HttpStatus.NOT_FOUND);
        }

//        initialize the output path for payslip that will be generated
        String OUTPUTPATH = "C:\\Users\\LENOVO\\Desktop\\Output_Payslips\\EmpId_" + employee.getId() + "_" + month + "_" + year+".pdf";

//        Initialize the replacement map with necessary params
        Map<String,String> replacementMap = generateReplacementMap(employee,yearMonth,month,year,startDate,endDate);

//        Read the content from template file
        String templateContent = readTemplateFileContent();

//        Replace the placeholders with the attribute values
        String updatedContent = replacePlaceholders(replacementMap,templateContent);

//        generate pdf file for the payslip with the new content
        byte[] paySlipPdfBytes = generatePdfFile(updatedContent);

//        save the pdf bytes as a new file
        FileOutputStream fileOutputStream = new FileOutputStream(OUTPUTPATH);
        fileOutputStream.write(paySlipPdfBytes);

        return paySlipPdfBytes;
    }


    private String readTemplateFileContent() throws IOException {
        FileInputStream fileInputStream = new FileInputStream(TEMPLATEPATH);
        XWPFDocument document = new XWPFDocument(fileInputStream);
        XWPFWordExtractor extractor = new XWPFWordExtractor(document);
        String content = extractor.getText();
        extractor.close();
        return content;
    }

    private String replacePlaceholders(Map<String,String> replacementMap,String content){
        for(Map.Entry<String,String> entry: replacementMap.entrySet()){
            content = content.replace(entry.getKey(),entry.getValue());
        }
        return content;
    }

    private Map<String,String> generateReplacementMap(Employee employee,YearMonth yearMonth, Month month,
                                                      Year year, LocalDate startDate, LocalDate endDate){

        Map<String,String> replacementMap = new HashMap<>();

        replacementMap.put("{name}", employee.getName());
        replacementMap.put("{department}", employee.getDepartment());
        replacementMap.put("{position}", employee.getPosition());
        replacementMap.put("{monthyear}", month +" "+ year);
        replacementMap.put("{workingdays}",String.valueOf(yearMonth.lengthOfMonth()));
        replacementMap.put("{monthlysalary}",String.valueOf(employee.getSalary()));

        Double dailySalary = (double) employee.getSalary() / (double) yearMonth.lengthOfMonth();
        dailySalary = Math.round(dailySalary*100.0)/100.0;

        Long presentDays = attendanceService.findTotalDaysPresentInMonth(employee.getId(),month,year);

        Double earnedSalary = earnedSalaryService.get("earnedSalaryMonthly").calculateEarnedSalary(employee,startDate,endDate);

        replacementMap.put("{dailysalary}",String.valueOf(dailySalary));
        replacementMap.put("{presentdays}",String.valueOf(presentDays));
        replacementMap.put("{earnedsalary}",String.valueOf(earnedSalary));

        return replacementMap;
    }

    private byte[] generatePdfFile(String pdfContent) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        PdfWriter pdfWriter = new PdfWriter(byteArrayOutputStream);

        String[] paragraphStrings = pdfContent.split("\\n");
        List<Paragraph> paragraphList = new ArrayList<>();
        for(String paragraphString : paragraphStrings){
            paragraphList.add(new Paragraph(paragraphString));
        }

        paragraphList = formatParagraphsAndDateStamp(paragraphList);

        PdfDocument pdfDocument = new PdfDocument(pdfWriter);

        Document document = new Document(pdfDocument);

        pdfDocument.setDefaultPageSize(PageSize.A4);

        paragraphList.forEach(document::add);

        document.close();

        byte[] pdfBytes = byteArrayOutputStream.toByteArray();

        return pdfBytes;
    }

    private List<Paragraph> formatParagraphsAndDateStamp(List<Paragraph> paragraphList){
//        get the sections that need to be formatted
        Paragraph heading = paragraphList.get(0);
        Paragraph company = paragraphList.get(1);
        Paragraph salaryHeading = paragraphList.get(13);
        Paragraph netEarnings = paragraphList.get(17);
        Paragraph signatureHeading = paragraphList.get(20);
        Paragraph signatureUnderline = paragraphList.get(21);

//        perform formatting for each section
        heading.setTextAlignment(TextAlignment.CENTER).setBold().setFontSize(28).setMarginTop(7);

        company.setTextAlignment(TextAlignment.CENTER).setFontSize(16).setUnderline();

        salaryHeading.setTextAlignment(TextAlignment.CENTER).setBold().setFontSize(16).setMarginBottom(5);

        netEarnings.setBold().setFontSize(14);

        signatureHeading.setTextAlignment(TextAlignment.RIGHT).setBold().setFontSize(14).setMarginTop(20);

        signatureUnderline.setTextAlignment(TextAlignment.RIGHT).setBold().setFontSize(14);

//        generate and format a date stamp as a paragraph
        Paragraph generatedOn = new Paragraph("Generated on: "+LocalDate.now().toString());
        generatedOn.setTextAlignment(TextAlignment.RIGHT).setUnderline();

//        replace the old values with the newly formatted paragraphs
        paragraphList.set(0,heading);
        paragraphList.set(1,company);
        paragraphList.set(13,salaryHeading);
        paragraphList.set(17, netEarnings);
        paragraphList.set(20,signatureHeading);
        paragraphList.set(21,signatureUnderline);

//        Add the newly generated paragraph at the last to not mess up indexing
        paragraphList.add(3,generatedOn);

//        return the formatted paragraphs list
        return paragraphList;
    }
}

