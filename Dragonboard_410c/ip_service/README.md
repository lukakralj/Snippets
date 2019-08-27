# Dragonboard IP service 

This module acts as a service and will send an email containing IP information 
every time a Dragonboard starts up. This will enable SSH usage without the need for screen (to
obtain an IP of the board).
(This will only work when connecting to a familiar network. To establish a new WiFi connection
you will still need to connect a screen to the Dragonboard or use other headless approach.)



    
## Setup
1. Install NodeJs and npm.
2. In the script folder run `npm i`.
3. Test the service by running `npm start`.
4. To schedule this task to run on every start up add this line 
to `/etc/crontab`: `@reboot root cd /path/to/ip_service && npm start`

**CHANGE CONFIGURATION FILE BEFORE RUNNING THIS SCRIPT.**

In `email-config.json` use **your own email in "transporter"** field.
Change "email_to" field to the email you want to receive the IPs on.
