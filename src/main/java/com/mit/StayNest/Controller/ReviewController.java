package com.mit.StayNest.Controller;

import java.net.http.HttpResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.mit.StayNest.Entity.Review;
import com.mit.StayNest.Services.ReviewServiceImpl;

@RestController
public class ReviewController {
	
	@Autowired
	ReviewServiceImpl reviewServiceImpl;
	

}
