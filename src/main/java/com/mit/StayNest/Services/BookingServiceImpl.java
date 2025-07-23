package com.mit.StayNest.Services;

import com.mit.StayNest.Entity.Booking;
import com.mit.StayNest.Entity.Listing;
import com.mit.StayNest.Entity.Owner;
import com.mit.StayNest.Entity.User;
import com.mit.StayNest.Repository.BookingRepository;
import com.mit.StayNest.Services.ListingServiceImpl;
import jakarta.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
public class BookingServiceImpl implements BookingService {
	private static final Logger logger = LoggerFactory.getLogger(BookingServiceImpl.class);


    @Autowired
    private BookingRepository bookingRepository;
    
    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private ListingServiceImpl listingServiceImpl;
    @Override
    public Booking createBooking(Booking booking) {
    	logger.info("Creating booking for listing ID: {} by tenant ID: {}",
                booking.getListing().getId(), booking.getTenant().getId());

    	booking.setListing(listingServiceImpl.getSpecificListing(booking.getListing().getId()));
        booking.setStatus("PENDING");

        Booking saved = bookingRepository.save(booking);
        
        logger.info("Booking created successfully with ID: {}", saved.getId());
        
        Owner owner = booking.getListing().getOwner();
        
        logger.info("Owner fetched from db : " + owner.getEmail());
        
        User tenant = booking.getTenant();    
        
        String subject = "New Booking Request on StayNest";
        String htmlBody = "<!DOCTYPE html>"
        	    + "<html lang=\"en\">"
        	    + "<head>"
        	    + "    <meta charset=\"UTF-8\">"
        	    + "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">"
        	    + "    <title>New Booking on StayNest</title>"
        	    + "    <style>"
        	    + "        body { font-family: Arial, sans-serif; background-color: #f8f8f8; margin: 0; padding: 0; -webkit-text-size-adjust: none; width: 100% !important; }"
        	    + "        table { border-collapse: collapse; width: 100%; }"
        	    + "        td { padding: 0; }"
        	    + "        .container { max-width: 600px; margin: 20px auto; background-color: #ffffff; border-radius: 8px; overflow: hidden; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); }"
        	    + "        .header { background: linear-gradient(to right, #FFC06A, #FFA36A); padding: 20px; text-align: center; }"
        	    + "        .header h1 { color: #ffffff; font-size: 28px; margin: 0; font-weight: bold; }"
        	    + "        .content { padding: 30px; color: #333333; line-height: 1.6; }"
        	    + "        .content p { margin-bottom: 15px; font-size: 16px; }"
        	    + "        .button-container { text-align: center; padding: 20px 0; }"
        	    + "        .button { display: inline-block; background-color: #FF8C42; color: #ffffff !important; padding: 12px 25px; text-decoration: none; border-radius: 5px; font-weight: bold; font-size: 16px; box-shadow: 0 4px 8px rgba(255, 140, 66, 0.3); transition: background-color 0.3s ease; }"
        	    + "        .button:hover { background-color: #e67e32; }"
        	    + "        .footer { background-color: #f2f2f2; padding: 20px; text-align: center; font-size: 12px; color: #777777; }"
        	    + "        .footer p { margin: 0; }"
        	    + "        a { color: #FF8C42; text-decoration: none; }"
        	    + "        a:hover { text-decoration: underline; }"
        	    + "    </style>"
        	    + "</head>"
        	    + "<body>"
        	    + "    <table role=\"presentation\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\">"
        	    + "        <tr><td align=\"center\">"
        	    + "            <table role=\"presentation\" class=\"container\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\">"
        	    + "                <tr><td class=\"header\"><h1>StayNest</h1></td></tr>"
        	    + "                <tr><td class=\"content\">"
        	    + "                    <p>Hello owner</p>"
        	    + "                    <p>You’ve received a new booking request for your listing: <strong>" + booking.getListing().getTitle() + "</strong>.</p>"
        	    + "                    <p><strong>Tenant Details:</strong><br>"
        	    + "                    Name: " + tenant.getName() + "<br>"
        	    + "                    Email: " + tenant.getEmail() + "<br>"
        	    + "                    Phone: " + tenant.getPhoneNumber() + "</p>"
        	    + "                    <p>Please log in to your dashboard to confirm or decline this booking.</p>"
        	    + "                    <div class=\"button-container\">"
        	    + "                        <a href=\"http://localhost:3000/owner-dashboard\" class=\"button\" target=\"_blank\">Manage Booking</a>"
        	    + "                    </div>"
        	    + "                    <p>Thanks for being part of StayNest!<br>The StayNest Team</p>"
        	    + "                </td></tr>"
        	    + "                <tr><td class=\"footer\">"
        	    + "                    <p>&copy; 2025 StayNest. All rights reserved.</p>"
        	    + "                    <p><a href=\"#\" target=\"_blank\">Privacy Policy</a> | <a href=\"#\" target=\"_blank\">Terms of Service</a></p>"
        	    + "                </td></tr>"
        	    + "            </table>"
        	    + "        </td></tr>"
        	    + "    </table>"
        	    + "</body>"
        	    + "</html>";
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(owner.getEmail());
            helper.setSubject(subject);
            helper.setText(htmlBody, true);
            javaMailSender.send(message);
            logger.info("Booking email sent to owner: {}", owner.getEmail());
        } catch (Exception e) {
            logger.error("Failed to send booking email to owner: {}", e.getMessage());
        }

        
        return saved;
    }

    @Override
    public List<Booking> getBookingsByTenant(User tenant) {
    	 logger.info("Fetching bookings for tenant ID: {}", tenant.getId());
        return bookingRepository.findByTenant(tenant);
    }

    @Override
    public List<Booking> getBookingsByListing(Listing listing) {
    	 logger.info("Fetching bookings for listing ID: {}", listing.getId());
        return bookingRepository.findByListing(listing);
    }

    @Override
    public Optional<Booking> getBookingById(Long id) {
    	logger.info("Fetching booking by ID: {}", id);
        return bookingRepository.findById(id);
    }

    @Override
    public List<Booking> getBookingsByStatus(String status) {
    	logger.info("Fetching bookings with status: {}", status.toUpperCase());
        return bookingRepository.findByStatus(status.toUpperCase());
    }

    @Override
    public Booking cancelBooking(Long id) {
    	logger.info("Cancellation request received for booking ID: {}", id);

        Booking booking = bookingRepository.findById(id)
            .orElseThrow(() -> {
                logger.warn("Booking not found with ID: {}", id);
                return new RuntimeException("Booking not found with ID: " + id);
            });
        booking.setStatus("CANCELLED");
        Booking cancelled = bookingRepository.save(booking);
        logger.info("Booking cancelled successfully with ID: {}", cancelled.getId());
        return cancelled;
    }

    
    @Override
    public void updateBookingStatus(Long bookingId, String action, Owner owner) {
        Booking booking = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new RuntimeException("Booking not found with ID: " + bookingId));

        // Ownership validation
        if (!booking.getListing().getOwner().getId().equals(owner.getId())) {
            throw new RuntimeException("You are not authorized to update this booking.");
        }

        String newStatus;
        switch (action.toUpperCase()) {
            case "ACCEPT":
                newStatus = "ACCEPTED";
                break;
            case "REJECT":
                newStatus = "REJECTED";
                booking.setTotalRent(new Double(0));
                break;
            default:
                throw new IllegalArgumentException("Invalid action. Use 'ACCEPT' or 'REJECT'.");
        }

        booking.setStatus(newStatus);
        bookingRepository.save(booking);
        logger.info("Booking ID {} status changed to {}", bookingId, newStatus);

        // Send email to tenant
        User tenant = booking.getTenant();
        Listing listing = booking.getListing();

        String subject = "Your Booking on StayNest has been " + newStatus;

        String htmlBody = "<!DOCTYPE html>"
            + "<html lang=\"en\">"
            + "<head>"
            + "    <meta charset=\"UTF-8\">"
            + "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">"
            + "    <title>Booking Status Update</title>"
            + "    <style>"
            + "        body { font-family: Arial, sans-serif; background-color: #f8f8f8; margin: 0; padding: 0; -webkit-text-size-adjust: none; width: 100% !important; }"
            + "        table { border-collapse: collapse; width: 100%; }"
            + "        td { padding: 0; }"
            + "        .container { max-width: 600px; margin: 20px auto; background-color: #ffffff; border-radius: 8px; overflow: hidden; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); }"
            + "        .header { background: linear-gradient(to right, #6AAEFF, #6AD1FF); padding: 20px; text-align: center; }"
            + "        .header h1 { color: #ffffff; font-size: 28px; margin: 0; font-weight: bold; }"
            + "        .content { padding: 30px; color: #333333; line-height: 1.6; }"
            + "        .content p { margin-bottom: 15px; font-size: 16px; }"
            + "        .status { font-weight: bold; color: " + (newStatus.equals("ACCEPTED") ? "#28a745" : "#dc3545") + "; font-size: 18px; }"
            + "        .button-container { text-align: center; padding: 20px 0; }"
            + "        .button { display: inline-block; background-color: #007BFF; color: #ffffff !important; padding: 12px 25px; text-decoration: none; border-radius: 5px; font-weight: bold; font-size: 16px; box-shadow: 0 4px 8px rgba(0, 123, 255, 0.3); transition: background-color 0.3s ease; }"
            + "        .button:hover { background-color: #0056b3; }"
            + "        .footer { background-color: #f2f2f2; padding: 20px; text-align: center; font-size: 12px; color: #777777; }"
            + "        .footer p { margin: 0; }"
            + "        a { color: #007BFF; text-decoration: none; }"
            + "        a:hover { text-decoration: underline; }"
            + "    </style>"
            + "</head>"
            + "<body>"
            + "    <table role=\"presentation\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\">"
            + "        <tr><td align=\"center\">"
            + "            <table role=\"presentation\" class=\"container\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\">"
            + "                <tr><td class=\"header\"><h1>StayNest</h1></td></tr>"
            + "                <tr><td class=\"content\">"
            + "                    <p>Hi " + tenant.getName() + ",</p>"
            + "                    <p>Your booking request for the listing <strong>" + listing.getTitle() + "</strong> has been:</p>"
            + "                    <p class=\"status\">" + newStatus + "</p>"
            + "                    <p>You can view more details or check other listings in your StayNest dashboard.</p>"
            + "                    <div class=\"button-container\">"
            + "                        <a href=\"http://localhost:3000/login\" class=\"button\" target=\"_blank\">Go to Dashboard</a>"
            + "                    </div>"
            + "                    <p>Thank you for using StayNest!<br>The StayNest Team</p>"
            + "                </td></tr>"
            + "                <tr><td class=\"footer\">"
            + "                    <p>&copy; 2025 StayNest. All rights reserved.</p>"
            + "                    <p><a href=\"#\" target=\"_blank\">Privacy Policy</a> | <a href=\"#\" target=\"_blank\">Terms of Service</a></p>"
            + "                </td></tr>"
            + "            </table>"
            + "        </td></tr>"
            + "    </table>"
            + "</body>"
            + "</html>";

        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(tenant.getEmail());
            helper.setSubject(subject);
            helper.setText(htmlBody, true);
            javaMailSender.send(message);
            logger.info("Status update email sent to tenant: {}", tenant.getEmail());
        } catch (Exception e) {
            logger.error("Failed to send status update email to tenant: {}", e.getMessage());
        }
    }




	@Override
	public double getRentForBooking(Long id, double month) {
		Optional<Booking> booking = bookingRepository.findById(id);
		if(booking.isPresent()) {
		logger.info("Rent requested for Booking : {}",booking.get().getId());
		double rent = booking.get().getListing().getRent();
		logger.info("Total rent for Booking : {}",rent * month);
		booking.get().setTotalRent(rent * month);
		bookingRepository.save(booking.get());
		return rent*month;
		}
		else {
			logger.warn("Booking not found with id  : {}", id);
			throw new RuntimeException("Booking not found");
		}
	}

	@Override
	public List<Booking> getBookingsByOwner(Owner owner) {
		// TODO Auto-generated method stub
		Long id = owner.getId();
		logger.info("Owner with id {} accessing for bookings",id);
		List<Booking> result = bookingRepository.getBookingsByOwner(id);
		logger.info("Total bookings found for owner {} " + result.size());
		return result;
	}
    

}
