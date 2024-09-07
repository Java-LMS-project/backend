package rhap.library.lms.controller;

import rhap.library.lms.service.QRCodeService;
import com.google.zxing.WriterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class QRCodeController {

    @Autowired
    private QRCodeService qrCodeService;

    @GetMapping("/generateQRCode")
    public ResponseEntity<byte[]> generateQRCode(@RequestParam String text) {
        try {
            byte[] qrCodeBytes = qrCodeService.generateQRCode(text);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(org.springframework.http.MediaType.IMAGE_PNG);
            headers.setContentLength(qrCodeBytes.length);
            return new ResponseEntity<>(qrCodeBytes, headers, HttpStatus.OK);
        } catch (WriterException | IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
