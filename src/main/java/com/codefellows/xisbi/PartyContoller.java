package com.codefellows.xisbi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;
import java.util.Optional;

@Controller
public class PartyContoller {

    // This annotation allows Spring to resolve and inject collaborating beans into this bean.
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PartyRepository partyRepo;

    // Displays party creation page via party.html template
    @RequestMapping(value = "/party/create", method = RequestMethod.GET)
    public String displayPartyTemplate(Model model, Principal p) {
        XisbiUser user = (XisbiUser) ((UsernamePasswordAuthenticationToken) p).getPrincipal();
        model.addAttribute("user", userRepo.findById(user.id).get());
        model.addAttribute("update", false);
        return "party";
    }

    // Creates a party from the creation page via party.html template
    @RequestMapping(value = "/party/create", method = RequestMethod.POST)
    public RedirectView createPartyFromTemplate(
            Principal p,
            Model model,
            @RequestParam String partyTitle,
            @RequestParam String partyTime,
            @RequestParam String partyDate,
            @RequestParam String partyLocation,
            @RequestParam String partyDescription) {

        Party newParty = new Party(
                partyTitle, partyTime, partyDate, partyLocation, partyDescription
        );

        XisbiUser current = (XisbiUser) ((UsernamePasswordAuthenticationToken) p).getPrincipal();

        newParty.partyHost = userRepo.findById(current.id).get();
        current = userRepo.findById(current.id).get();
        partyRepo.save(newParty);

        current.hosting.add(newParty);
        userRepo.save(current);

        model.addAttribute("update", false);

        // redirect user to the party update page once a party is created
        return new RedirectView("/party/" + newParty.id);
    }

    // Displays update version of party creation page via party.html template
    @RequestMapping(value = "/party/{id}/update", method = RequestMethod.GET)
    public String displayUpdateTemplate(
            @PathVariable long id,
            Model model, Principal p) {
        XisbiUser user = (XisbiUser) ((UsernamePasswordAuthenticationToken) p).getPrincipal();

        if (partyRepo.findById(id).isPresent()) {
            model.addAttribute("user", userRepo.findById(user.id).get());
            model.addAttribute("party", partyRepo.findById(id).get());
            model.addAttribute("update", true);

            return "party";
        } else {
            throw new PartyNotFoundException("Event does not exist");
        }
    }

    // Displays XISBI update section in the party creation page via party.html template
    @RequestMapping(value = "/party/{id}/update", method = RequestMethod.PUT)
    public RedirectView updateParty(
            @PathVariable long id,
            Model model, @RequestParam String partyTitle,
            @RequestParam String partyTime,
            @RequestParam String partyDate,
            @RequestParam String partyLocation,
            @RequestParam String partyDescription) {

        Optional<Party> partyOptional = partyRepo.findById(id);
        Party partyToUpdate = partyRepo.findById(id).get();

        partyToUpdate.updateParty(partyTitle, partyTime, partyDate, partyLocation, partyDescription);
        partyRepo.save(partyToUpdate);

        model.addAttribute("party", partyRepo.findById(id).get());
        return new RedirectView("/party/" + id);
    }

    //message for User not found in database
    @RequestMapping(value = "/party/{id}/user-not-found", method = RequestMethod.GET)
    public String userNotFound(@PathVariable long id, Model model, Principal p) {
        XisbiUser user = (XisbiUser) ((UsernamePasswordAuthenticationToken) p).getPrincipal();

        if (partyRepo.findById(id).isPresent()) {
            Party party = partyRepo.findById(id).get();
            model.addAttribute("party", party);
            model.addAttribute("userNotFound", true);
            model.addAttribute("user", userRepo.findById(user.id).get());
            model.addAttribute("update", true);
            model.addAttribute("host", Auth.isHost(user, party));


        } else {
            throw new PartyNotFoundException("Event does not exist");
        }
        return "oneParty";
    }

    @RequestMapping(value = "/party/{id}/add-guest", method = RequestMethod.POST)
    public RedirectView updateGuestInvited(
            @RequestParam String guestUsername,
            @PathVariable long id) {

        //  find the guest by username
        XisbiUser guest = userRepo.findByUsername(guestUsername);
        //  add guest to the party by their ID
        System.out.println(guest);
        if (guest == null) {
            return new RedirectView("/party/" + id + "/user-not-found");
        } else if (userRepo.findById(guest.id).isPresent()) {
            Party party = partyRepo.findById(id).get();
            //  add to guest list and then save to party repo
            party.guestInvited.add(guest);
            partyRepo.save(party);

            //add party to user's invited set

            guest.invited.add(party);
            userRepo.save(guest);
        }
        return new RedirectView("/party/" + id);
    }

    @RequestMapping(value = "/party/{id}/confirm", method = RequestMethod.PUT)
    public RedirectView updateGuestConfirmation(
            @RequestParam String guestUsername,
            @PathVariable long id) {

        XisbiUser guestToConfirm = userRepo.findByUsername(guestUsername);
        Party party = partyRepo.findById(id).get();
        party.guestInvited.remove(guestToConfirm);
        party.guestConfirmed.add(guestToConfirm);
        partyRepo.save(party);

        return new RedirectView("/party/" + id);
    }

    @RequestMapping(value = "/party/{id}/delete-guest", method = RequestMethod.PUT)
    public RedirectView deleteGuestFromParty(@PathVariable long id,
                                             @RequestParam String guestUsername) {

        XisbiUser guestToRemove = userRepo.findByUsername(guestUsername);
        Party party = partyRepo.findById(id).get();
        party.guestInvited.remove(guestToRemove);
        partyRepo.save(party);

        return new RedirectView("/party/" + id);
    }

    // Deletes a specific version of party page via party.html template
    @RequestMapping(value = "/party/{id}", method = RequestMethod.DELETE)
    public RedirectView deleteAParty(@PathVariable long id) {

        Optional<Party> partyToDelete = partyRepo.findById(id);

        if (partyToDelete.isPresent()) {
            partyRepo.deleteById(id);
        }

        return new RedirectView("/my-dashboard-delete-event");
    }

    //message for party deleted
    @RequestMapping(value = "/my-dashboard-delete-event", method = RequestMethod.GET)
    public String deleteMessage(Model model, Principal p) {

        model.addAttribute("deleteMessage", true);
        model.addAttribute("user", ((UsernamePasswordAuthenticationToken) p).getPrincipal());

        return "my-dashboard";
    }

    // Displays a specific version of party page via oneParty.html template
    @RequestMapping(value = "/party/{id}", method = RequestMethod.GET)
    public String viewAParty(
            @PathVariable long id,
            Model model, Principal p) {
        if (partyRepo.findById(id).isPresent()) {
            Party party = partyRepo.findById(id).get();
            XisbiUser current = (XisbiUser) ((UsernamePasswordAuthenticationToken) p).getPrincipal();

            model.addAttribute("party", party);
            model.addAttribute("user", userRepo.findById(current.id).get());
            model.addAttribute("host", Auth.isHost(current, party));
            return "oneParty";
        } else {
            throw new PartyNotFoundException("Event does not exist");
        }
    }

    //Error message for parties
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    class PartyNotFoundException extends RuntimeException {

        public PartyNotFoundException() {
            super();
        }

        public PartyNotFoundException(String message) {
            super(message);
        }
    }
}
