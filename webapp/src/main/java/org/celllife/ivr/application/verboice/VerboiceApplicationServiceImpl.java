package org.celllife.ivr.application.verboice;

import org.celllife.ivr.application.campaign.CampaignService;
import org.celllife.ivr.application.contact.ContactService;
import org.celllife.ivr.application.utils.JsonUtils;
import org.celllife.ivr.domain.callog.CallLog;
import org.celllife.ivr.domain.callog.CallLogRepository;
import org.celllife.ivr.domain.campaign.Campaign;
import org.celllife.ivr.domain.contact.Contact;
import org.celllife.ivr.domain.exception.ContactExistsException;
import org.celllife.ivr.domain.exception.IvrException;
import org.celllife.ivr.domain.exception.VerboiceDatabaseException;
import org.celllife.ivr.domain.verboice.contactaddresses.ContactAddresses;
import org.celllife.ivr.domain.verboice.contactaddresses.ContactAddressesRepository;
import org.celllife.ivr.domain.verboice.contacts.Contacts;
import org.celllife.ivr.domain.verboice.contacts.ContactsRepository;
import org.celllife.ivr.domain.verboice.persistedvariables.PersistedVariables;
import org.celllife.ivr.domain.verboice.persistedvariables.PersistedVariablesRepository;
import org.celllife.ivr.domain.verboice.projectvariables.ProjectVariables;
import org.celllife.ivr.domain.verboice.projectvariables.ProjectVariablesRepository;
import org.celllife.ivr.integration.verboice.VerboiceService;
import org.dozer.util.IteratorUtils;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class VerboiceApplicationServiceImpl implements VerboiceApplicationService {

    private static Logger log = LoggerFactory.getLogger(VerboiceApplicationServiceImpl.class);

    @Autowired
    VerboiceService verboiceService;

    @Autowired
    CampaignService campaignService;

    JsonUtils jsonUtils = new JsonUtils();

    @Autowired
    CallLogRepository callLogRepository;

    @Autowired
    ContactsRepository verboiceContactsRepository;

    @Autowired
    ContactAddressesRepository contactAddressesRepository;

    @Autowired
    PersistedVariablesRepository persistedVariablesRepository;

    @Autowired
    ProjectVariablesRepository projectVariablesRepository;

    @Override
    public void enqueueCallForMsisdn(String channelName, String callFlowName, String scheduleName, String msisdn, int messageNumber, String password) throws IvrException {

        String response = "";

        try {
            response = verboiceService.enqueueCallWithPassword(channelName, callFlowName, scheduleName, msisdn, messageNumber, password);
        } catch (Exception e) {
            throw new IvrException("Could not enqueue call to verboice. Reason: " + e.getMessage());
        }

        Map<String, String> responseVariables;

        try {
            responseVariables = jsonUtils.extractJsonVariables("{\"response\":" + response + "}");
        } catch (JSONException e) {
            log.warn("Unrecognized Response from Verboice Server. Response: " + response, e);
            throw new IvrException("Unrecognized Response from Verboice Server.");
        }

        if (responseVariables.containsKey("call_id")) {
            callLogRepository.save(new CallLog(new Date(), Long.parseLong(responseVariables.get("call_id")), msisdn,
                    0, channelName, callFlowName, scheduleName, responseVariables.get("state"), messageNumber));
        } else {
            log.warn("No call ID returned from Verboice server.");
            throw new IvrException("No call ID returned from Verboice server.");
        }

    }

    @Override
    @Transactional("transactionManagerVerboice")
    public Contacts saveVerboiceContact(Contacts contact) {
        return verboiceContactsRepository.save(contact);
    }

    @Transactional("transactionManagerVerboice")
    private Integer createContactFromCelllifeContactAndSave(Contact contact, Campaign campaign, Integer projectVariableId) throws VerboiceDatabaseException {

        Long existingContacts = contactAddressesRepository.findTotalContactsWithMsisdnInProject(contact.getMsisdn(), campaign.getVerboiceProjectId().intValue());

        if (existingContacts == 0) {

            Long numberOfContactsWithPassword = persistedVariablesRepository.findTotalVariablesWithValue(contact.getPassword());
            if (numberOfContactsWithPassword > 0) {
                throw new VerboiceDatabaseException("The contact " + contact.getMsisdn() + " has not been added because another contact has the same password.");
            }

            Contacts verboiceContact = new Contacts(campaign.getVerboiceProjectId().intValue(), new Date(), new Date());
            verboiceContact = verboiceContactsRepository.save(verboiceContact);
            if (verboiceContact.getId() == null) {
                throw new VerboiceDatabaseException("There was an error saving contact " + contact.getMsisdn() + " to the database.");
            }

            ContactAddresses contactAddresses = new ContactAddresses(contact.getMsisdn(), verboiceContact.getId(), campaign.getVerboiceProjectId().intValue(), new Date(), new Date());
            contactAddressesRepository.save(contactAddresses);

            PersistedVariables persistedVariables = new PersistedVariables(contact.getPassword(), new Date(), new Date(), verboiceContact.getId(), projectVariableId);
            persistedVariablesRepository.save(persistedVariables);
            return verboiceContact.getId();

        } else {

            throw new VerboiceDatabaseException("Contact " + contact.getMsisdn() + " already exists in the verboice project "  + campaign.getVerboiceProjectId() + " and cannot be added again.");

        }

    }

    @Override
    @Transactional("transactionManagerVerboice")
    public List<String> createContactsAndSave(List<Contact> contacts, Long campaignId) throws IvrException {

        Campaign campaign = campaignService.getCampaign(campaignId);
        if (campaign == null) {
            throw new IvrException("The campaign ID is not valid.");
        }

        Iterable<ProjectVariables> projectVariables = projectVariablesRepository.findByNameAndProject(campaign.getVerboiceProjectId().intValue(), "password");
        List<ProjectVariables> projectVariablesList = IteratorUtils.toList(projectVariables.iterator());

        if (projectVariablesList.size() != 1) {
            throw new IvrException("Cannot save contacts to verboice because password variable does not exist on Verboice server.");
        }

        List<String> failedNumbers = new ArrayList<>();

        for (Contact contact : contacts) {
            log.debug("Attempting to save contact " + contact.getMsisdn() + " to Verboice. ");
            try {
                Integer returnedId = createContactFromCelllifeContactAndSave(contact, campaign, projectVariablesList.get(0).getId());
                /* TODO: use returnId somehow */
            } catch (VerboiceDatabaseException e) {
                log.warn("Contact not added to verboice. Reason: " + e.getLocalizedMessage());
                failedNumbers.add(contact.getMsisdn());
            }
        }

        return failedNumbers;

    }

}
