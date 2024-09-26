<?php
require_once "./php/utilities.php";
require_once "./php/session_utilities.php";

function go_to_next_branch_in_journey(string $current_state, mixed $formatted_input) : array {
    global $local_response;
    global $update_state;
    global $has_more_destinations;

    $local_response = "";
    $update_state = true;
    $has_more_destinations = false;

    if ($current_state == "initial") {
        store_in_session("username", $formatted_input, false); // Store username for user
        $local_response = generate_destination_request($formatted_input);
    } elseif ($current_state == "req_destination_number") {
        initialize_destination_info($formatted_input); // Initialize with limit & current position as 1
        $local_response = generate_location_request(1, $formatted_input == 1);
    } else {
        $data_object = read_latest_location_data_object();

        if ($current_state == "req_location") {
            update_location_data_object($data_object, ["city" => $formatted_input[0], "country" => $formatted_input[1]]);
            $local_response = generate_date_request(true);
        } elseif ($current_state == "req_start_date") {
            update_location_data_object($data_object, ['startDate' => $formatted_input]);
            $local_response = generate_date_request(false);
        } elseif ($current_state == "req_end_date") {

            // We need to make sure that the date is either greater than or equal to start date
            $start_date = $data_object['startDate'];

            if (days_in_between($start_date, $formatted_input) < 0) {
                $update_state = false;

                // Our response will be reverted to request start date again
                $local_response = generate_error_message($current_state)." ".generate_date_request(true, false);

                // Revert back to start_date
                update_chat_bot_state("req_location", "req_start_date");
            } else {
                update_location_data_object($data_object, ['endDate' => $formatted_input]);

                // Flush data object to location info storage
                $flush_response = flush_location_data_to_location_store();

                $has_more_destinations = (bool) $flush_response['has_more'];

                $local_response = $has_more_destinations ? generate_location_request($flush_response['next_index'], false) : "Implement validation!";
            }
        }
    }

    $journey_info = ['response' => $local_response, 'state' => $current_state];

    if ($update_state) {
        $next_state = get_next_state($current_state, $has_more_destinations);
        update_chat_bot_state($current_state, $next_state);

        $journey_info['next_state'] = $next_state;
    }

    return $journey_info;
}