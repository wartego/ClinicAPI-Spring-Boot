package pl.clinic.demoexporttoexcel.controller;

import pl.clinic.demoexporttoexcel.entity.Customer;
import pl.clinic.demoexporttoexcel.repository.CustomerRepository;
import pl.clinic.demoexporttoexcel.utils.ExcelUtils;
import pl.clinic.demoexporttoexcel.utils.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerRepository customerRepository;

    @GetMapping("/export")
    public ResponseEntity<Resource> exportPostList() throws Exception {
        List<Customer> customerList = customerRepository.findAll();

        if (!CollectionUtils.isEmpty(customerList)) {
            String fileName = "Customer" + ".xlsx";
            ByteArrayInputStream in = ExcelUtils.exportCustomer(customerList, fileName);
            InputStreamResource file = new InputStreamResource(in);
            return ResponseEntity.ok()
                    .header(
                            HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8))
                    .contentType(MediaType.parseMediaType("application/vnd.ms-excel; charset=UTF-8"))
                    .body(file);
        } else {
            throw new ResourceNotFoundException("No data");
        }
    }


}
