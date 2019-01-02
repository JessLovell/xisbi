package com.codefellows.xisbi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
    @RequestMapping(value="/party/create", method= RequestMethod.GET)
    public String displayPartyTemplate(Model model) {

        model.addAttribute("update", false);
        return "party";
    }

    // Creates a party from the creation page via party.html template
    @RequestMapping(value="/party/create", method= RequestMethod.POST)
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

        // TODO: Add OPTIONAL to ensure that current user is not empty
        XisbiUser current = (XisbiUser) ((UsernamePasswordAuthenticationToken) p).getPrincipal();

        newParty.partyHost = userRepo.findById(current.id).get();
        current = userRepo.findById(current.id).get();
        partyRepo.save(newParty);

        current.hosting.add(newParty);
//        newParty.partyHost.hosting.add(newParty);
        userRepo.save(current);

        model.addAttribute("update", false);

        // redirect user to the party update page once a party is created
        return new RedirectView("/party/" + newParty.id);
    }

    // Displays update version of party creation page via party.html template
    @RequestMapping(value="/party/{id}/update", method= RequestMethod.GET)
    public String displayUpdateTemplate(
            @PathVariable long id,
            Model model) {

        // TODO: IF statement required to check if the party exists
        model.addAttribute("party", partyRepo.findById(id).get());
        model.addAttribute("update", true);
        return "party";
    }

    // Displays XISBI update section in the party creation page via party.html template
    @RequestMapping(value="/party/{id}/update", method= RequestMethod.PUT)
    public RedirectView updateParty(
            @PathVariable long id,
            Model model, @RequestParam String partyTitle,
            @RequestParam String partyTime,
            @RequestParam String partyDate,
            @RequestParam String partyLocation,
            @RequestParam String partyDescription) {

        Optional<Party> partyOptional = partyRepo.findById(id);
        Party partyToUpdate = partyRepo.findById(id).get();

        // TODO: IF statement required to check if the party exists
        partyToUpdate.updateParty(partyTitle, partyTime, partyDate, partyLocation, partyDescription);
        partyRepo.save(partyToUpdate);

        model.addAttribute("party", partyRepo.findById(id).get());
        return new RedirectView("/party/"+ id);
    }

    @RequestMapping(value ="/party/{id}/add-guest", method = RequestMethod.POST)
    public RedirectView updateGuestList(
            @RequestParam String guestUsername,
            @PathVariable long id){
//  TODO: Check DB for the user, add user to party guest list, add party to attending list

        //  find the guest by username
        XisbiUser guest = userRepo.findByUsername(guestUsername);
        //  add guest to the party by their ID
        Party party = partyRepo.findById(id).get();

        //  add to guest list and then save to party repo
        party.guestList.add(guest);
        partyRepo.save(party);

        //add party to user's attending set
        guest.attending.add(party);
        userRepo.save(guest);

        return new RedirectView("/party/"+ id);
    }

    // Displays a specific version of party page via oneParty.html template
    @RequestMapping(value="/party/{id}", method= RequestMethod.GET)
    public String viewAParty(
            @PathVariable long id,
            Model model, Principal p) {

        Party party = partyRepo.findById(id).get();
        XisbiUser current = (XisbiUser) ((UsernamePasswordAuthenticationToken) p).getPrincipal();

        // TODO: IF statement required to check if the party exists
        model.addAttribute("party", party);
        model.addAttribute("host", Auth.isHost(current, party));
        return "oneParty";
    }
}
