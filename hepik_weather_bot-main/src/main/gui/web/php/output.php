<?php
/**
 * Generates a generic welcome message.
 *
 * @return string welcome message
 */
function generate_initial_message() : string {
    return "Hello, welcome! What's your name?";
}

/**
 * Generates a generic destination request
 *
 * @param string $name denotes the name of the user
 * @return string destination request
 */
function generate_destination_request(string $name, bool $is_restart = false) : string {
    $default_message = "How many locations do you plan on visiting?";

    if ($is_restart) return $default_message;
    return "Nice to meet you, $name. My name is HEPIK. I can help you plan your trip based on the weather forecast. I am currently limited to only 16 days ahead. ".$default_message;
}

/**
 * Generates a generic location request
 *
 * @param int $current_index denotes the current index of the trip being requested
 * @param bool $isLastDestination denotes whether this is the last location
 * @return string location request
 */
function generate_location_request(int $current_index, bool $isLastDestination) : string {
    $main_message = $current_index == 1 ? "What's your first destination?" : ($isLastDestination ? "What's your final destination?" : "Where are you travelling to next?");

    $main_message .= " Please provide both the city and country. eg. Cork,Ireland";
    return generate_location_request_quip($current_index)." ".$main_message;
}

/**
 * Generates a generic quip
 *
 * @param int $current_index denotes the current index of the trip being requested
 * @return string a quip for the chatbot
 */
function generate_location_request_quip(int $current_index) : string {
    if ($current_index == 1) return "Off we go, planning wise ;)";
    if ($current_index > 1 && $current_index < 5) return "Choo! Choo! Full steam ahead!";
    return "I can do this all day!";
}

/**
 * Generates a generic date request
 *
 * @param bool $is_start_date
 * @param bool $append_format
 * @return string date request
 */
function generate_date_request(bool $is_start_date, bool $append_format = true) : string {
    $main_message = $is_start_date ? "When do you expect to be in this location?" : "When do you expect to leave this location?";

    $main_message .= " (At least 1 day ahead)";

    if ($append_format) return $main_message." Use the DD-MM-YYYY format. e.g. 31-12-2024";

    return $main_message;
}

/**
 * Generates a generic parting shot
 *
 * @param string $name denotes the name of the user
 * @return string a parting shot with an option to restart
 */
function generate_parting_shot(string $name) : string {
    return "Enjoy your trip, $name! Would you like to plan another trip? Type 'yes' or 'no'";
}

function generate_error_message(string $current_state) : string {
    if ($current_state == "initial") return "Please provide a valid name. Your name cannot be blank/empty.";
    if ($current_state == "req_destination_number") return "Invalid number provided. Please provide a valid number with no decimal places.";
    if ($current_state == "req_location" || $current_state == "req_location_again") return "Invalid location format. Please provide both the city and country. eg. Cork,Ireland";
    if ($current_state == "req_start_date" || $current_state == "req_end_date") return "Invalid date. The date should be 1 - 16 days ahead. Use the DD-MM-YYYY format. e.g. 31-12-2024";

    return "Sorry! We could not process your request";
}