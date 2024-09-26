import { handleSend } from "./js/utilities.js";
import { startChat } from "./js/utilities.js";

// Listen to taps on send button
let sendButton = document.querySelector(".icon");
sendButton.addEventListener('click', () => handleSend());

// Listen to taps from welcome button
let welcomeButton = document.getElementById("welcome-button"); // Welcome button 
welcomeButton.addEventListener('click', () => startChat());