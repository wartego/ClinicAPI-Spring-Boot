package pl.clinic.exportdatatoexcel.service;

import pl.clinic.exportdatatoexcel.utils.BaseResponse;
import pl.clinic.exportdatatoexcel.utils.CustomerDTO;
import org.springframework.web.multipart.MultipartFile;

public interface CustomerService {
    BaseResponse importCustomerData(MultipartFile importFile);

    BaseResponse createCustomer(CustomerDTO customerDTO);
}
