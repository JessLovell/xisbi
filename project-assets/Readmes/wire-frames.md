# Wire Frames for XISBI

## Database schemas
- a table for `AppUser` has `firstName`, `lastName`, `username`, `password`, set of `Hosting` parties, set of `Attending` parties.
- a table for `Party` has `host`, `partyName`, `theme`, `location`, `date`, `time`, `description`, and a set of `guestList`.
- a join table for those `attending_party` with a `user`, `party`, *`response`


## Home Page (/)
![home page for xisbi](../img/home.JPG)

## Login/Signup Page (/login)
![login or signup page for xisbi](../img/login-signup.JPG)

## User Profile Page (/myprofile)
![my profile page for xisbi](../img/myprofile.JPG)

## Create/Update Page for a Party (/party/create, /party/{id}/update)
![create/update page for xisbi](../img/create-update.JPG)

## View Party Page (/party/{id})
![view party details page for xisbi](../img/view-party.JPG)

## Users Page (/users)
![view all users for xisbi](../img/users.JPG)
