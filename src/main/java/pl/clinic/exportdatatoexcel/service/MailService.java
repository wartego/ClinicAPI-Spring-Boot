package pl.clinic.exportdatatoexcel.service;

import pl.clinic.exportdatatoexcel.utils.CustomerDTO;

public interface MailService {
    void sendMailTest();

    void sendMailCreateCustomer(CustomerDTO dto);
}
