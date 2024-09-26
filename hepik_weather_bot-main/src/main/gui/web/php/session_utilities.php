<?php
/**
 * Persists data in the global session variable
 *
 * @param string $session_key denotes key used to store the data
 * @param mixed $data denotes data to persist
 * @param bool $encode_as_json denotes whether to encode into a json string
 * @return void
 */
function store_in_session(string $session_key, mixed $data, bool $encode_as_json) : void {
    $data = $encode_as_json ? json_encode($data) : $data;
    $_SESSION[$session_key] = $data;
}

/**
 * Reads the data persisted to the global session variable
 *
 * @param string $session_key denotes key used to store data
 * @param bool $decode_as_json denotes whether to decode from a json string
 * @return string|array|null a string, if data is needed as is. an array, if data was encoded as a json. null, if the
 * key doesn't exist
 */
function read_session_data(string $session_key, bool $decode_as_json) : null|string|array {
    if (key_exists($session_key, $_SESSION)) {
        $data = $_SESSION[$session_key];
        return $decode_as_json ? json_decode($data, true) : $data;
    }

    return null;
}

function clear_session_data(array $keys) : void {
    foreach ($keys as $key => $value) {
        $is_array = (bool) $value;

        store_in_session($key, $is_array ? [] : "", $is_array);
    }
}

/**
 * Retrieves the current state of the chatbot
 *
 * @return array with the previous & current state
 */
function get_chatbot_state() : array {
    $state = read_session_data("state", true);

    if (is_null($state)) return ["previous" => "none", "current" => "none"];
    return $state;
}

function update_chat_bot_state(string $previous_state, string $next_state) : void {
    $state = ["previous" => $previous_state, "current" => $next_state];
    store_in_session("state", $state, true);
}

function initialize_destination_info(int $limit) : void {
    $destination_info = ['current_count' => 1, 'limit' => $limit];

    store_in_session("destination_info", $destination_info, true);
}

function read_latest_location_data_object(bool $clear_data = false) : array {
    $data_key = "data";

    $data_object = read_session_data($data_key, true);

    if (is_null($data_object)) return [];

    if ($clear_data) store_in_session($data_key, [], true);

    return $data_object;
}

function update_location_data_object(array $data_object, array $keys_with_data) : void {
    foreach ($keys_with_data as $key => $value) {
        $data_object[$key] = $value;
    }

    store_in_session("data", $data_object, true);
}

function update_location_info_cache() : void {
    $location_info_key = "location_info";

    $data_object = read_latest_location_data_object(true);

    // Returns an array of integer linked to location data objects
    $location_info = read_session_data($location_info_key, true);
    $location_info[] = $data_object;

    // Store location info
    store_in_session($location_info_key, $location_info, true);
}

function flush_location_data_to_location_store() : array {
    $destination_info_key = "destination_info";

    $destination_info = read_session_data($destination_info_key, true);

    $current_count = $destination_info['current_count'];

    update_location_info_cache();

    $current_count += 1; // Go to next index

    $destination_info['current_count'] = $current_count;

    // Store destination info
    store_in_session($destination_info_key, $destination_info, true);

    return [
        "has_more" => $current_count <= $destination_info['limit'], // Tell caller whether to request more
        "next_index" => $current_count,
    ];
}

function get_validation_error(bool $go_to_next) : array {
    $session_key = "validation_errors";

    $validation_errors = read_session_data($session_key, true);

    if ($go_to_next) update_location_info_cache();

    $next_validation_index = max((int)$validation_errors['current'], 0) + ($go_to_next ? 1 : 0);
    $errors = $validation_errors['errors'];

    // If no more errors are present
    if ($next_validation_index >= sizeof($errors)) return [];

    store_in_session($session_key, ["current" => $next_validation_index, "errors" => $errors], true);

    return $errors[$next_validation_index];
}