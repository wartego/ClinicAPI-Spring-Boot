package pl.clinic.exportdatatoexcel.controller;

import pl.clinic.exportdatatoexcel.entity.Customer;
import pl.clinic.exportdatatoexcel.repository.CustomerRepository;
import pl.clinic.exportdatatoexcel.service.CustomerService;
import pl.clinic.exportdatatoexcel.utils.BaseResponse;
import pl.clinic.exportdatatoexcel.utils.CustomerDTO;
import pl.clinic.exportdatatoexcel.utils.ExcelUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerRepository customerRepository;

    private final CustomerService customerService;

    @GetMapping("/export")
    public ResponseEntity<Resource> exportCustomer() throws Exception {
        List<Customer> customerList = customerRepository.findAll();

        if (!CollectionUtils.isEmpty(customerList)) {
            String fileName = "Customer Export" + ".xlsx";

            ByteArrayInputStream in = ExcelUtils.exportCustomer(customerList, fileName);

            InputStreamResource inputStreamResource = new InputStreamResource(in);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8)
                    )
                    .contentType(MediaType.parseMediaType("application/vnd.ms-excel; charset=UTF-8"))
                    .body(inputStreamResource);
        } else {
            throw new Exception("No data");

        }
    }

    @PostMapping("/import")
    public ResponseEntity<BaseResponse> importCustomerData(@RequestParam("file") MultipartFile importFile) {
        BaseResponse baseResponse = customerService.importCustomerData(importFile);
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }


    @PostMapping("/create")
    public ResponseEntity<BaseResponse> createCustomer(@RequestBody CustomerDTO customerDTO){
        return new ResponseEntity<>(customerService.createCustomer(customerDTO), HttpStatus.OK);
    }

}
