package com.amahoro.amahoro_stadium_ticketing.controller;

import com.amahoro.amahoro_stadium_ticketing.model.AmahoroEntity;
import com.amahoro.amahoro_stadium_ticketing.repository.AmahoroRepository;
import com.amahoro.amahoro_stadium_ticketing.service.AmahoroService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "https://stadium-fontend-v1.netlify.app")
public class AmahoroController {

//    @Autowired
//    private JavaMailSender emailSender;
    @Autowired
    private AmahoroRepository amahoroRepository;

    @Autowired
    AmahoroService amahoroService;

    @GetMapping("/view")
    public ResponseEntity<?> showList(Model model) {
        System.out.println("I am inside the view controller");
        List<AmahoroEntity> viewclient = amahoroService.listAll();
//        model.addAttribute("viewclient", viewclient);
        return ResponseEntity.status(200).body(viewclient);
    }

    @GetMapping("/query")
    public String showQueries(Model model) {
        List<AmahoroEntity> viewclient = amahoroRepository.findAll();
        model.addAttribute("viewclient", viewclient);
        return "admin/clientQueriesReply";
    }

//    @PostMapping("/sendEmail")
//    public String sendEmail(@RequestParam String email, @RequestParam String message) {
//        SimpleMailMessage mailMessage = new SimpleMailMessage();
//        mailMessage.setTo(email);
//        mailMessage.setSubject("Meeting Request");
//        mailMessage.setText(message);
//        emailSender.send(mailMessage);
//        return "Admin/dashboard";
//    }

    @GetMapping("/edit/{id}")
    public ResponseEntity<?> editArena(@PathVariable("id") Long id, Model model) {
        System.out.println("im inside edit controller");
        AmahoroEntity viewClient= amahoroRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid booking Id:" + id));
        model.addAttribute("viewClient", viewClient);
        return ResponseEntity.status(200).body(viewClient);
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable(value = "id") long id, @Valid AmahoroEntity viewClient, BindingResult bindingResult) throws IOException {
        if (bindingResult.hasErrors()) {
            viewClient.setId(id);
            return "admin/dashboard";
        }
        amahoroRepository.save(viewClient);
        return "redirect:/view";
    }
    @PutMapping("/edit/{id}")
    public ResponseEntity<String> updateClient(@PathVariable("id") Long id, @RequestBody AmahoroEntity updatedClient) {
        AmahoroEntity existingClient = amahoroRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid client Id: " + id));
        existingClient.setClientName(updatedClient.getClientName());
        existingClient.setPhoneNumber(updatedClient.getPhoneNumber());
        existingClient.setEmail(updatedClient.getEmail());
        existingClient.setTicketclass(updatedClient.getTicketclass());
        amahoroRepository.save(existingClient);
        return ResponseEntity.ok("Client updated successfully");
    }


    @RequestMapping(value = "/image/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public ResponseEntity<byte[]> getImage(@PathVariable("id") Long id) {
        Optional<AmahoroEntity> booking = amahoroService.findClientById(id);
        byte[] imageBytes = booking.get().getProfilePhoto();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        headers.setContentLength(imageBytes.length);
        return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
    }


//    @RequestMapping(value = {"/client"}, method = RequestMethod.GET)
//    public String showForm(Model model) {
//        model.addAttribute("booking", new AmahoroEntity());
//        System.out.println("I am inside client form");
//        return "booking";
//    }



    @PostMapping("/clientform")
    public String submitForm(
//            @RequestBody AmahoroEntity booking,
            @RequestParam("clientName") String clientName,
            @RequestParam("email") String email,
            @RequestParam("phoneNumber") String phoneNumber,
            @RequestParam("ticketClass") String ticketClass,
            @RequestParam(value = "file", required = false) MultipartFile profilePhoto) throws IOException {

        AmahoroEntity booking = new AmahoroEntity();
        try {
            // Handle profile photo if provided
            if (profilePhoto != null && !profilePhoto.isEmpty()) {
                if (isValidImageType(profilePhoto.getContentType())) {
                    byte[] imageBytes = profilePhoto.getBytes();
                    booking.setEmail(email);
                    booking.setClientName(clientName);
                    booking.setTicketclass(ticketClass);
                    booking.setProfilePhoto(imageBytes);
                } else {
                    // Log invalid file type
                    System.out.println("Invalid file type: " + profilePhoto.getContentType());
                    return "admin/dashboard"; // Redirect to admin page for invalid file
                }
            }

            // Save booking details to the repository
            amahoroRepository.save(booking);
            return "user/dashboard"; // Redirect to user dashboard on success

        } catch (IOException ex) {
            // Log the error
            System.err.println("Error while processing the file: " + ex.getMessage());
            ex.printStackTrace();
            return "error-page"; // Redirect to a generic error page
        }
    }

    // Helper method to validate image types
    private boolean isValidImageType(String contentType) {
        return contentType != null &&
                (contentType.equals("image/jpeg") ||
                        contentType.equals("image/png") ||
                        contentType.equals("image/jpg"));
    }

    @GetMapping("/searchBooking")
    public String searchBooking(@RequestParam(value = "clientName", required = false) String clientName, Model model) {
        List<AmahoroEntity> results;
        if (clientName != null && !clientName.isEmpty()) {
            results = amahoroService.findByClientName(clientName);
        } else {
            results = amahoroService.listAll(); // Show all records if search is empty
        }
        model.addAttribute("viewclient", results);
        return "admin/viewclient"; // Template to display search results
    }

    @GetMapping("/viewsorted")
    public ResponseEntity<?> showSortedList(Model model) {
        List<AmahoroEntity> sortedClients = amahoroService.listAllSortedByName();
//        model.addAttribute("viewclient", sortedClients);
        return ResponseEntity.status(200).body(sortedClients);
    }
    // In AmahoroController or UserController, update the URL
    @GetMapping("/admin-dashboard")
    public String showDashboard() {
        return "admin/dashboard";  // Dashboard template path
    }


    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadBookingPdf(@PathVariable Long id) {
        try {
            // Fetch the booking details
            Optional<AmahoroEntity> bookingOptional = amahoroService.getBookingById(id);
            if (bookingOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ByteArrayResource("Booking not found".getBytes()));
            }

            AmahoroEntity booking = bookingOptional.get();

            // Generate PDF
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Document document = new Document();
            try {
                PdfWriter.getInstance(document, baos);
                document.open();

                // Title Section
                Font titleFont = FontFactory.getFont(FontFactory.HELVETICA, 18, Font.BOLD);
                Paragraph title = new Paragraph("Booking Details", titleFont);
                title.setAlignment(Element.ALIGN_CENTER);
                title.setSpacingAfter(20);
                document.add(title);

                // Booking Information Section
                Font textFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
                document.add(new Paragraph("Booking ID: " + booking.getId(), textFont));
                document.add(new Paragraph("Client Name: " + booking.getClientName(), textFont));
                document.add(new Paragraph("Client Email: " + booking.getEmail(), textFont));
                document.add(new Paragraph("Phone Number: " + booking.getPhoneNumber(), textFont));
                document.add(new Paragraph("Ticket Class: " + booking.getTicketclass(), textFont));
                document.add(Chunk.NEWLINE);

                // Profile Picture Section
                byte[] imageBytes = booking.getProfilePhoto();
                if (imageBytes != null && imageBytes.length > 0) {
                    try {
                        Image profileImage = Image.getInstance(imageBytes);
                        profileImage.setAlignment(Element.ALIGN_CENTER);
                        profileImage.scaleToFit(150, 150); // Adjust size as needed
                        document.add(profileImage);
                    } catch (IOException | BadElementException e) {
                        // Log and proceed without adding the image
                        System.err.println("Error adding profile image: " + e.getMessage());
                    }
                }

            } catch (DocumentException e) {
                System.err.println("Error generating PDF document: " + e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            } finally {
                // Make sure to close the document manually
                document.close();
            }

            // Prepare PDF for download
            byte[] pdfBytes = baos.toByteArray();
            ByteArrayResource resource = new ByteArrayResource(pdfBytes);

            String filename = "Booking_Details_" + booking.getId() + ".pdf";
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_PDF)
                    .contentLength(pdfBytes.length)
                    .body(resource);

        } catch (Exception e) {
            // General Exception Handling
            System.err.println("Error processing download request: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }



    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteClient(@PathVariable("id") Long id) {
        AmahoroEntity client = amahoroService.findClientById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid client Id:" + id));

        amahoroRepository.delete(client);

        return ResponseEntity.status(200).body(client);
    }


}
