package com.codefellows.xisbi;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class XisbiApplicationTests {
	@LocalServerPort
	private int port;

	@Autowired
	UserController userController;

	@Autowired
	PartyContoller partyController;

	@Test
	public void contextLoads() throws Exception {
		assertThat(userController).isNotNull();
		assertThat(partyController).isNotNull();
	}

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void testIndexRoute() {
		ResponseEntity<String> response =  this.restTemplate.getForEntity("http://localhost:" + port + "/", String.class);
		assertEquals("The response code should be 200", 200, response.getStatusCodeValue());
		assertTrue("response contain Your Event. Three Steps.", response.toString().contains("<h1>Your Event. Three Steps.</h1>"));
	}

	@Test
	public void testSignUpRoute() {
		ResponseEntity<String> response = this.restTemplate.getForEntity("http://localhost:" + port + "/signup", String.class);
		assertEquals("The server should give back a 200 response", 200, response.getStatusCodeValue());
		assertTrue("There should be a form that makes a post to /signup within the page", response.toString().contains("<form action=\"/signup\" method=\"POST\">"));
	}
	@Test
	public void testLoginRoute() {
		ResponseEntity<String> response = this.restTemplate.getForEntity("http://localhost:" + port + "/login", String.class);
		assertEquals("The server should give back a 200 response", 200, response.getStatusCodeValue());
		assertTrue("There should be a form that makes a post to /perform_login within the page", response.toString().contains("<form action=\"/perform_login\" method=\"POST\">"));
	}
	
}

