package org.celllife.ivr.interfaces.web;

import org.celllife.ivr.application.ContactService;
import org.celllife.ivr.domain.Contact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvMapReader;
import org.supercsv.io.ICsvMapReader;
import org.supercsv.prefs.CsvPreference;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class CsvUploadController {

    @Autowired
    ContactService contactService;

    @ResponseBody
    @RequestMapping(value = "/service/contacts", method = RequestMethod.POST)
    public String upload(@RequestParam("file") MultipartFile uploadedFile) throws IOException {

        ICsvMapReader mapReader = new CsvMapReader(new InputStreamReader(uploadedFile.getInputStream()), CsvPreference.STANDARD_PREFERENCE);

        try {

            final String[] header = {"msisdn", "password"};
            final CellProcessor[] processors = getProcessors();

            Map<String, Object> contactMap;
            List<Contact> contactDTOList = new ArrayList<>();

            while ((contactMap = mapReader.read(header, processors)) != null) {
                Contact contact = new Contact();
                contact.setMsisdn(contactMap.get("msisdn").toString());
                contact.setPassword(contactMap.get("password").toString());
                contactDTOList.add(contact);
            }

            contactService.saveContacts(contactDTOList);

        } finally {
            mapReader.close();
        }

        return "success";

    }

    /**
     * Sets up the processors used for the examples. There are 10 CSV columns, so 10 processors are defined. Empty
     * columns are read as null (hence the NotNull() for mandatory columns).
     *
     * @return the cell processors
     */
    private static CellProcessor[] getProcessors() {

        final CellProcessor[] processors = new CellProcessor[]{
                new NotNull(), // firstName
                new NotNull()
        };

        return processors;
    }

}
