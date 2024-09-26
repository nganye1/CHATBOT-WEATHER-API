<?php
require_once "java_jar_executor.php";
require_once __DIR__.'/../output.php';
require_once __DIR__.'/../session_utilities.php';

function validate_locations(bool $clear_geo_cache = true) : string|array {
    $java_response = send_request(true);

    // Clear mutable data after a validation call
    clear_mutable_bridge_info($clear_geo_cache);

    // Reset every mutable key in session
    if (key_exists('unrecoverable', $java_response)) {
        return restart_journey(true, $java_response);
    }

    $success = $java_response['success'];
    $errors = $java_response['errors'];

    // Cache the success response
    if (is_array($success) && !empty($success)) {
        // Read old geolocation info
        $session_key = "geolocation_info";
        $cached_info = read_session_data($session_key, true) ?? [];

        // Merge old cache with recently fetched data
        $merged_cache = array_merge($cached_info, $success);

        store_in_session($session_key, $merged_cache, true);
    }

    // Request location again
    if (!empty($errors)) return revalidate_location($errors);

    $trip_info = fetch_trip_plan();

    restart_journey(false);

   return $trip_info;
}

function fetch_trip_plan() : string|array {
    $java_response = send_request(false);

    // Reset every mutable key in session
    if (key_exists('unrecoverable', $java_response)) return restart_journey(true, $java_response);

    return array_merge(
        ["Based on the provided information, I checked the weather forecast and made some clothing suggestions.<br><br>"],
        $java_response['success'],
        $java_response['errors'],
        ["Enjoy your trip! If you would like to plan another trip, <b>enter the number of destinations.<b>"]
    );
}

function revalidate_location(array $errors = null, array $location = null) : array|string {
    $already_ran = is_null($errors);

    // Location will & should not be null
    if ($already_ran) {
        update_location_data_object(
            read_latest_location_data_object(),
            ["city" => $location[0], "country" => $location[1]]
        );

    } else {
        // The first call will pass in new errors
        store_in_session("validation_errors", ["current" => -1, "errors" => $errors], true);
        update_chat_bot_state("validate_location", "req_location_again");
    }

    $next_error = get_validation_error($already_ran);

    // If empty, validate locations
    if (empty($next_error)) {
        // Update state as "validating" and not "requesting" only if no errors are present
        update_chat_bot_state("req_location_again", "validate_location");
        return validate_locations(false);
    }

    return request_location_again($next_error);
}

function request_location_again(array $current_error) : string {
    $location_info = $current_error['location'];

    $error = $current_error['error'];

    // Load object into current data param to updated in the request cycle
    update_location_data_object($location_info, []);

    return $error."<br><br>Please provide both the city and country. eg. Cork,Ireland";
}

function clear_mutable_bridge_info(bool $include_geo_info) : void {
    $mutable_keys = [
        "destination_info" => true,
        "data" => true,
        "location_info" => true,
        "validation_errors" => true,

    ];

    if ($include_geo_info) $mutable_keys["geolocation_info"] = true;

    clear_session_data($mutable_keys);
}

function restart_journey(bool $has_error, array $unrecoverable_error = null) : string|null {
    // Reset every mutable key. Mark as true if associative i.e. encoded as json
    clear_mutable_bridge_info(true);

    // Reset state to its request from the start
    update_chat_bot_state("initial", "req_destination_number");

    if ($has_error) {
        return $unrecoverable_error['unrecoverable']."<br><br>"."It seems we need to restart this conversation. ".generate_destination_request("", true);
    }

    return null;
}