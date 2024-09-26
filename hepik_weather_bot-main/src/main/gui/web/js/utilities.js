const url = "http://localhost";
const port = "";

/*
 * Path name after "htdocs" folder. If not serving using xampp update accordingly!
 * Follow the SETUP.md.md file in the repository to only copy the required folder to a top level folder!
 *
 * Example:
 *      directory with the gui = "/htdocs/folder/..
 *
 * Add only the "folder" here without the next slash
 */
const pathToProject = "chatbot";

function getUrl() {
    let postUrl = url;

    if (port) postUrl += ":" + port;

    return postUrl + "/" + pathToProject + "/chatbot.php";
}

/* Main sections */
let welcomeSection = document.querySelector(".welcome"); // Holds welcome section
let chatScreen = document.getElementById("chat-screen"); // Holds message blocks
let inputSection = document.getElementById("input"); // Holds anything input related
/* */

/* Input related */
let sendIcon = document.getElementById("send"); // Send icon
let progress = document.getElementById("progress"); // Progress spinner
let input = document.getElementById("chat-input"); // Has user input
/* */

// SVGs for user & chatbot
const userSVG = '<svg xmlns="http://www.w3.org/2000/svg" height="24" viewBox="0 -960 960 960" width="24"><path d="M480-492q-76 0-129-53t-53-129q0-76 53-129t129-53q76 0 129 53t53 129q0 76-53 129t-129 53ZM130-205v-37q0-41 19.5-74t52.5-50q66-34 136-51t142-17q72 0 142 17t136 51q33 17 52.5 50t19.5 74v37q0 45-30.5 75.5T724-99H236q-45 0-75.5-30.5T130-205Z"/></svg>';
const chatbotSVG = '<svg xmlns="http://www.w3.org/2000/svg" height="24" viewBox="0 -960 960 960" width="24"><path d="M130-320q-62 0-105-43.03t-43-104.5Q-18-529 25.5-571.5T130-614v-57q0-43.72 31.14-74.86Q192.27-777 236-777h98q0-61 42.62-103.5t103.5-42.5q60.88 0 103.38 42.58Q626-837.83 626-777h98q43.72 0 74.86 31.14T830-671v57q61 0 104.5 42.58Q978-528.83 978-468q0 61.67-43.17 104.83Q891.67-320 830-320v137q0 43.73-31.14 74.86Q767.72-77 724-77H236q-43.73 0-74.86-31.14Q130-139.27 130-183v-137Zm228-95q30.42 0 51.71-21.29T431-488q0-30.42-21.29-51.71T358-561q-30.42 0-51.71 21.29T285-488q0 30.42 21.29 51.71T358-415Zm244 0q30.42 0 51.71-21.29T675-488q0-30.42-21.29-51.71T602-561q-30.42 0-51.71 21.29T529-488q0 30.42 21.29 51.71T602-415ZM357-252h246q22 0 37.5-15.5T656-305q0-22-15.5-37.5T603-358H357q-22 0-37.5 15.5T304-305q0 22 15.5 37.5T357-252Z"/></svg>';

/**
 * Creates a div element and adds any classes specified
 * 
 * @param {Array} classNames denotes a list of class names to append
 * @returns HTMLDivElement {@link HTMLDivElement}
 */
function createDiv(classNames) {
    const div = document.createElement("div");

    for (let i = 0; i < classNames.length; i++) {
        div.classList.add(classNames[i]);
    }
    
    return div;
}

/**
 * Creates a message block that can be appended into the document tree
 * 
 * @param {boolean} isUser denotes whether it is a user's input / a chatbot's response
 * @param {String} displayMessage denotes message to display
 * @returns 
 */
function createMessageBlock(isUser, displayMessage) {
    // Create message block
    let messageBlockDiv = createDiv(["message-block"]);

    // Create avatar div
    let avatarDiv = createDiv(["avatar"]);
    avatarDiv.innerHTML += isUser ? userSVG : chatbotSVG; // Add svg

    // Create message block
    let message = createDiv(["message"]);
    let paragraph = document.createElement('p');

    // Add actual message nodes
    paragraph.innerHTML = displayMessage;
    message.appendChild(paragraph);

    messageBlockDiv.appendChild(avatarDiv);
    messageBlockDiv.appendChild(message);

    // Document tree ignores non-unique elements appended dynamically. Return copy.
    return messageBlockDiv.cloneNode(true); 
}

function createMessageAndEnsureVisible(isUser, displayMessage) {
    let messageBlock = createMessageBlock(isUser, displayMessage);
    chatScreen.appendChild(messageBlock);

    // Scroll it into view
    messageBlock.scrollIntoView({ behavior: 'smooth', block: 'end' });
}

/**
 * Shows/hides progress indicator when the user triggers a sent message
 * 
 * @param {boolean} hideProgress denotes whether to hide the progress
 */
async function toggle(hideProgress) {
    progress.style.display = hideProgress ? "none" : "inline-block";
    sendIcon.style.display = hideProgress ? "inline-block" : "none";
}

function extractResponses(actualResponse) {
    let responses = Array.from(actualResponse['response']);
    return responses.join("");
}

/**
 * Makes an API request to server and injects the chatbot's response into the dom
 *
 * @param {String} input
 */
async function sendToChatbot(input) {
    // Make request to PHP with chatbot logic
    let response = await fetch(getUrl(), {
        method: "POST",
        body: JSON.stringify({"input": input}),
    });

    let wasSuccessful = response.ok;
    let message = wasSuccessful
        ? await response.json().then((data) => extractResponses(data))
        : "Sorry! Your request was not sent.";

    createMessageAndEnsureVisible(false, message);

    await toggle(true);
    return wasSuccessful;
}

/**
 * Handles user click for sending a message.
 */
export async function handleSend() {
    // Ignore any clicks when handling a click
    if (progress.style.display !== "" && progress.style.display !== "none") return;

    await toggle(false);

    // Get input value
    let userInput = input.value;

    // If nothing actionable is present, return
    if (!userInput) return await toggle(true);

    // Append user input immediately
    createMessageAndEnsureVisible(true, userInput);

    if (await sendToChatbot(userInput)) input.value = "";
}

/**
 * Initializes chat with an hello message from the user and makes the initial connection to php to initialize
 * a session for the chat.
 */
export async function startChat() {
    // Inject "hello" message into chat ready for display
    chatScreen.appendChild(createMessageBlock(true, "Hello"));

    let main = document.querySelector("main");

    main.style.minHeight = '';
    main.style.maxHeight = '100%';

    // Hide welcome page & show chat screen
    welcomeSection.style.display = "none";
    chatScreen.style.display = "inline-block";
    inputSection.style.display = "inline-flex";

    await toggle(false);

    await sendToChatbot("Hello");
}