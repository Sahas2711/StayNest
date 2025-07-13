package com.mit.StayNest.Services;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.mit.StayNest.Entity.Owner;
import com.mit.StayNest.Repository.OwnerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
public class OwnerServiceImpl implements OwnerService {
	private static final Logger logger = LoggerFactory.getLogger(OwnerServiceImpl.class);

	@Autowired
	private OwnerRepository ownerRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Override
	public Owner register(Owner owner) {
		logger.info("Attempting to register owner with email: {}", owner.getEmail());

		Optional<Owner> existingowner = ownerRepo.findByEmail(owner.getEmail());
		if (existingowner.isPresent()) {
			logger.warn("Registration failed — owner already exists with email: {}", owner.getEmail());
			throw new RuntimeException("owner already exists with email: " +owner.getEmail());
		}
		String encodedPassword = passwordEncoder.encode(owner.getPassword());
	    owner.setPassword(encodedPassword);
		return ownerRepo.save(owner);
	}

	@Override
	public Owner login(Owner owner) {
        if (owner.getEmail() == null || owner.getPassword() == null) {
            logger.warn("Login failed — missing email or password");
            throw new RuntimeException("Credentials not found");
        }
		Optional<Owner> existingowner = ownerRepo.findByEmail(owner.getEmail());

		if (existingowner.isPresent() && existingowner.get().getPassword().equals(owner.getPassword())) {
			 logger.info("Login successful for owner ID: {}", existingowner.get().getId());
			return existingowner.get();
		}
		 logger.warn("Login failed — invalid credentials for email: {}", owner.getEmail());
		throw new RuntimeException("Invalid email or password");
	}


	@Override
	public Owner updateOwner(Owner owner) {
		logger.info("Updating owner with email: {}", owner.getEmail());
		Optional<Owner> currentowner = ownerRepo.findByEmail(owner.getEmail());
		if (currentowner.isPresent()) {
			Owner existingowner = currentowner.get();

			if (owner.getName() != null) {
				existingowner.setName(owner.getName());
			}
			if (owner.getPassword() != null) {
				existingowner.setPassword(owner.getPassword());
			}
			if (owner.getPhoneNumber() != null) {
				existingowner.setPhoneNumber(owner.getPhoneNumber());
			}
			return ownerRepo.save(existingowner);
		} else {
			throw new RuntimeException("owner does not exist");
		}
	}

	@Override
	public List<Owner> getOwner() {
		logger.info("Fetching all owners");
		return ownerRepo.findAll();
	}

	@Override
	public Owner currentOwner(Owner user) {
		logger.info("Fetching current owner with email: {}", user.getEmail());

		Optional<Owner> currentowner = ownerRepo.findByEmail(user.getEmail());
		if (currentowner.isPresent()) {
			return currentowner.get();
		} else {
			 logger.warn("Current owner not found with email: {}", user.getEmail());
			throw new RuntimeException("owner does not exist");
		}
	}

	@Override
	public Owner deleteOwner(Owner owner) {
		logger.info("Attempting to delete owner with ID: {}", owner.getId());
		Optional<Owner> currentowner = ownerRepo.findById(owner.getId());
		if(currentowner.isPresent()) {
			ownerRepo.deleteById(owner.getId());
			logger.info("Owner deleted successfully with ID: {}", owner.getId());
			return currentowner.get();
		}
		else {
			logger.warn("Delete failed — owner not found with ID: {}", owner.getId());
			throw new RuntimeException("No owner exist with this id");
		}
	}

	@Override
	public Owner getOwnerById(String id) {
		logger.info("Fetching owner with ID: {}", id);
		Optional<Owner> owner = ownerRepo.findById(Long.parseLong(id));
		if(owner.isPresent()) {
			return owner.get();
		}
		else {
			 logger.warn("Owner not found with ID: {}", id);
			throw new RuntimeException("No owner exist with this id");
		}
	}
}

