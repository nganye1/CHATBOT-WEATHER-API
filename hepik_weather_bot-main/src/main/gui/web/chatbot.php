<?php
require_once "./php/utilities.php";
require_once "./php/session_utilities.php";
require_once "./php/output.php";
require_once "./php/branched_journeys.php";
require_once "./php/bridge/php_java_bridge.php";

session_start(); // Start session

$state = get_chatbot_state(); // Get current chatbot state

$current_state = $state['current'];
$previous_state = $state['previous'];

$response = "";

if ($current_state == "none") {
    update_chat_bot_state("none", "initial");
    $response = generate_initial_message();
} else {
    // Decode request. Get the user input
    $request = json_decode(file_get_contents('php://input'), true);

    $user_input = clean_user_input($request['input']); // Clean input

    $formatted_user_input = validate_and_format_input($current_state, $user_input);

    if ($formatted_user_input['has_error']) {
        $response = (string) $formatted_user_input['error'];

    } elseif ($current_state == "req_location_again") {
        $response = revalidate_location(null, (array) $formatted_user_input['output']);

    } else {
        $journey_info = go_to_next_branch_in_journey($current_state, $formatted_user_input['output']);

        // Validates location and fetches trip plan or goes to next branch of the journey
        $response = $journey_info['next_state'] == "validate_location" ? validate_locations() : (string) $journey_info['response'];
    }
}

echo json_encode(['response' => is_array($response) ? $response : [$response]]);
