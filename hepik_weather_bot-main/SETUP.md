# Oops! It works on my machine

By default, the repository provides a standard `jar` file compiled with `Java JDK` version 21. This is the minimum `Java` version required to run our code.

# HEPIK setup

As indicated in the specification in milestone I, our chatbot will use a `HTML-JavaScript-PHP-Java` where:

1. `HTML` - renders the chat
2. `JavaScript` - sends user input to `PHP` and manipulates the `DOM`/`HTML` to showcase the interactions.
3. `PHP` - handles the user journey. Abstracts this from the client. Sends geolocation & weather requests to `Java`.
4. `Java` - fetches the geolocation & weather requests passed on by `PHP`. Does it safely in a predictable way.

## Setting up XAMPP for PHP

- Go to this [link](https://www.apachefriends.org/download.html).
- The installation is pretty straightforward for `MacOS` and `Windows`. `Linux` may require executing from a `sh` file.
- Take note of the installation folder in `Windows`

Check [here](https://itsfoss.com/install-xampp-ubuntu/) for further instruction of adding an easy launch icon on `Linux`.

Ensure its volume is mounted on `MacOS`.

## Setting the project

Once `XAMPP` is installed everything after is fairly straightforward.

1. Clone this project via `git clone`. If the project exists, run `git fetch --prune`. The `main` branch has everything needed.
2. Run `git pull` just in case.
3. Ignore step `1` and `2` if you have the latest copy of this project.
4. Go to the `XAMPP` directory which has the `htdocs` folder
    - On Linux, `/opt/lampp/htdocs/`
    - Windows, your custom directory
    - MacOS (after mounting), `Applications/XAMPP/htdocs` .
5. Create a directory called `chatbot` inside. 
   - Open terminal and run `sudo mkdir chatbot` if needed. 
   - `Linux` restricts access on the directory to root.
   - Go to directory via `cd chatbot`.
   - Run `sudo chown -R $USER .` to give yourself permissions.
6. Go to the project you initially cloned or already have. Navigate to `/src/main/gui/web`.
7. Copy all its contents into the `chatbot` folder you created.

Give adequate permission for access of the folder.

Before we continue, in the `chatbot` folder ensure you have:
- an `assets` folder
- a `js` folder with `utilities.js` inside
- a `php` folder which has:
  - a `bridge` folder with a `HEPIK_weather_bot.jar` file, `java_jar_executor.php` file and a `php_java_bridge.php` file (MOST IMPORTANT FILES!!!)
  - a `branched_journeys.php` file
  - an `output.php` file
  - a `session_utilities.php` file
  - an `utilities.php` file
- a `chatbot.php` file
- an `index.css` file
- an `index.js` file
- an `index.html` file

## Configuring XAMPP correctly

Once everything is in order,
1. Open `XAMPP`. Tap `Apache` server. Start it using the button on the right
2. If the startup fails, it may be a port issue i.e. `port 80` is in use by other services
3. Tap `Apache` server. Tap `Configure`. Change the port number to a number that MAY be free. 
4. Start `Apache` again. Everything should be in order.

## Configuring project to match XAMPP (Ignore if you didn't change the port number)

1. Open the `chatbot` folder in an IDE or text-editor
2. Navigate to `js` folder. Open `utilities.js`.
3. On `line 2` add the port number.

Once done, if:
1. You changed the port number, type `localhost:` then add your port number then `/chatbot`.
2. You did not change the port number type `localhost/chatbot`

Enjoy!

