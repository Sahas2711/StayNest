package com.mit.StayNest.Services;


import java.util.List;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mit.StayNest.Entity.Owner;
import com.mit.StayNest.Repository.OwnerRepository;

@Service
public class OwnerServiceImpl implements OwnerService {

	@Autowired
	private OwnerRepository ownerRepo;

	@Override
	public Owner register(Owner owner) {

		Optional<Owner> existingowner = ownerRepo.findByEmail(owner.getEmail());
		if (existingowner.isPresent()) {
			throw new RuntimeException("owner already exists with email: " +owner.getEmail());
		}

		return ownerRepo.save(owner);
	}

	@Override
	public Owner login(Owner owner) {
		if (owner.getEmail() == null || owner.getPassword() == null)
			throw new RuntimeException("Credentials not found");
		Optional<Owner> existingowner = ownerRepo.findByEmail(owner.getEmail());

		if (existingowner.isPresent() && existingowner.get().getPassword().equals(owner.getPassword())) {
			return existingowner.get();
		}

		throw new RuntimeException("Invalid email or password");
	}


	@Override
	public Owner updateOwner(Owner owner) {
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
		return ownerRepo.findAll();
	}

	@Override
	public Owner currentOwner(Owner user) {
		Optional<Owner> currentowner = ownerRepo.findByEmail(user.getEmail());
		if (currentowner.isPresent()) {
			return currentowner.get();
		} else {
			throw new RuntimeException("owner does not exist");
		}
	}

	@Override
	public Owner deleteOwner(Owner owner) {
		Optional<Owner> currentowner = ownerRepo.findById(owner.getId());
		if(currentowner.isPresent()) {
			ownerRepo.deleteById(owner.getId());
			return currentowner.get();
		}
		else {
			throw new RuntimeException("No owner exist with this id");
		}
	}

	@Override
	public Owner getOwnerById(String id) {
		Optional<Owner> owner = ownerRepo.findById(Long.parseLong(id));
		if(owner.isPresent()) {
			return owner.get();
		}
		else {
			throw new RuntimeException("No owner exist with this id");
		}
	}
}

