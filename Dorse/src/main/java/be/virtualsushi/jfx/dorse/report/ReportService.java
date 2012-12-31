package be.virtualsushi.jfx.dorse.report;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

import be.virtualsushi.jfx.dorse.activities.ViewInvoiceActivity;
import be.virtualsushi.jfx.dorse.model.*;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import be.virtualsushi.jfx.dorse.model.report.OrderLineReport;
import be.virtualsushi.jfx.dorse.utils.EntityCollectionUtils;

@Service("reportSerivce")
public class ReportService {

  public static final String PDF_INVOICE_FILE_NAME_PATTER = "invoice-%s.pdf";
  public static final String PDF_REMINDER_FILE_NAME_PATTER = "rappel-%s.pdf";
  public static final String LABEL_FILE_NAME_PATTER = "label_%s_%s.pdf";

  @Autowired
  private ResourceBundle resources;

  @Value("#{systemProperties.getProperty('user.home')}")
  private String userHome;

  @Value("${report.date.format}")
  private String reportDateFormat;

  @Value("${print.container}")
 	private String printContainer;

  public String createInvoiceReport(int iReportType, Invoice invoice, Address invoiceAddress, Address deliveryAddress, List<OrderLineProperty> orderLines) {
    String reportType;
    if (iReportType == ViewInvoiceActivity.PRINT_INVOICE)
      reportType = "invoice_printout.jasper";
    else if (iReportType == ViewInvoiceActivity.PRINT_REMINDER)
      reportType = "reminder_printout.jasper";
    else return null;
    List<OrderLineProperty> decoupled = decoupleFreeArticles(orderLines);
    HashMap<String, Object> parameters = new HashMap<String, Object>();
    parameters.put("invoiceCode", invoice.getCode());
    parameters.put("invoiceId", invoice.getId());
    parameters.put("created", invoice.getCreationDate());
    parameters.put("now", new Date());
    parameters.put("goods", invoice.getProducts());
    parameters.put("tpt", invoice.getShipping());
    parameters.put("total", invoice.getTotal());
    parameters.put("report.date.format", reportDateFormat);
    parameters.put(JRParameter.REPORT_FORMAT_FACTORY, new DorseFormatFactory(resources));
    if (invoice.getCustomer() != null) {
      Customer customer = invoice.getCustomer();
      if (StringUtils.isNotBlank(customer.getName())) {
        parameters.put("customer", customer.getName());
      }
      if (customer.getPerson() != null) {
        List<Person> persons = customer.getPerson();
        if (persons.size() > 0) {
          Person first = persons.get(0);
          if (StringUtils.isNotBlank(first.getName())) {
            parameters.put("person", first.getName());
          }
        }
      }
    }
    if (invoiceAddress != null) {
      parameters.put("invoiceAddress", invoiceAddress);
    }
    if (deliveryAddress != null) {
      parameters.put("deliveryAddress", deliveryAddress);
    }

    File out = new File(createOutputFileUri(iReportType, invoice.getCode()));
    FileOutputStream outStream = null;
    try {
      outStream = new FileOutputStream(out);
      JasperPrint print = JasperFillManager.fillReport(new ClassPathResource(reportType).getInputStream(), parameters, new JRBeanCollectionDataSource(
          decoupled));
      JRExporter exporter = new JRPdfExporter();
      exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
      exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outStream);
      exporter.exportReport();
    } catch (JRException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      IOUtils.closeQuietly(outStream);
    }
    return out.getAbsolutePath();
  }


  public String createAddressLabel(Customer customer, Address address) {
    String reportType = "labelNoPerson.jasper";
    HashMap<String, Object> parameters = new HashMap<String, Object>();
    parameters.put("customer", customer.getName());
    parameters.put("address", address);

    File out = new File(createLabelOutputFileUri(customer));
    FileOutputStream outStream = null;
    try {
      outStream = new FileOutputStream(out);
      JasperPrint print = JasperFillManager.fillReport(new ClassPathResource(reportType).getInputStream(), parameters, new JREmptyDataSource());
      JRExporter exporter = new JRPdfExporter();
      exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
      exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outStream);
      exporter.exportReport();
    } catch (JRException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      IOUtils.closeQuietly(outStream);
    }
    return out.getAbsolutePath();
  }


  public String createAddressLabel(Customer customer, Address address, Person person) {
    String reportType = "label.jasper";
    HashMap<String, Object> parameters = new HashMap<String, Object>();
    parameters.put("customer", customer.getName());
    parameters.put("address", address);
    if (person != null)
      parameters.put("person", person.getName());

    File out = new File(createLabelOutputFileUri(customer));
    FileOutputStream outStream = null;
    try {
      outStream = new FileOutputStream(out);
      JasperPrint print = JasperFillManager.fillReport(new ClassPathResource(reportType).getInputStream(), parameters, new JREmptyDataSource());
      JRExporter exporter = new JRPdfExporter();
      exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
      exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outStream);
      exporter.exportReport();
    } catch (JRException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      IOUtils.closeQuietly(outStream);
    }
    return out.getAbsolutePath();
  }

  private String createOutputFileUri(int iReportType, String invoiceCode) {
    if(iReportType==ViewInvoiceActivity.PRINT_INVOICE)
      return userHome + printContainer + File.separator + String.format(PDF_INVOICE_FILE_NAME_PATTER, getApprovedName(invoiceCode));
    else
      return userHome + printContainer + File.separator + String.format(PDF_REMINDER_FILE_NAME_PATTER, getApprovedName(invoiceCode));
  }

  private String createLabelOutputFileUri(Customer customer) {
    return userHome + printContainer + File.separator + String.format(LABEL_FILE_NAME_PATTER, getApprovedName(customer.getName()), new Date().getTime());
  }

  private String getApprovedName(String original){
    return original.replaceAll(" ", "_");
  }

  private List<OrderLineProperty> decoupleFreeArticles(List<OrderLineProperty> orderLines) {
    List<OrderLineProperty> decoupled = new ArrayList<OrderLineProperty>();
    for (OrderLineProperty orderLine : orderLines) {
      if (orderLine.getApplyFree() && orderLine.getArticleFreeQuantity() > 0) {
        try {
          if (orderLine.getArticleFreeQuantity() < orderLine.getQuantity()) {
            OrderLineProperty free = (OrderLineProperty) BeanUtils.cloneBean(orderLine);
            free.setQuantity(orderLine.getArticleFreeQuantity());
            free.setArticlePrice(0F);
            free.setUnitDiscount(0F);
            free.setLineTotal(0F);
            orderLine.setQuantity(orderLine.getQuantity() - orderLine.getArticleFreeQuantity());
            decoupled.add(orderLine);
            decoupled.add(free);
          } else
            decoupled.add(orderLine);
        } catch (IllegalAccessException e) {
          e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InstantiationException e) {
          e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InvocationTargetException e) {
          e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (NoSuchMethodException e) {
          e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
      } else
        decoupled.add(orderLine);
    }
    return decoupled;
  }

}
