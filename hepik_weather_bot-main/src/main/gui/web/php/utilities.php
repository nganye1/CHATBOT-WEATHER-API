<?php require_once "output.php";
/**
 * Represents the chatbot state while interacting with the user
 */
$chat_states = [
    "initial",
    "req_destination_number",
    "req_location",
    "req_location_again",
    "req_start_date",
    "req_end_date",
    "validate_location",
];

function get_next_state(string $current, bool $has_more = false) : string {
    if ($current == "initial") return "req_destination_number";
    if ($current == "req_destination_number") return "req_location";
    if ($current == "req_location") return "req_start_date";
    if ($current == "req_location_again") return "validate_location";
    if ($current == "req_start_date") return "req_end_date";
    if ($current == "req_end_date") return $has_more ? "req_location" : "validate_location";

    return "initial";
}

function clean_user_input(string $input) : string {
    $input = htmlentities($input); // Remove tampered code
    return trim($input); // Remove any surrounding space
}

function validate_and_format_input(string $current_state, $input) : array {
    $validation = ['has_error' => false];

    if ($current_state == "initial") {
        add_error($validation, empty($input));  // Name should be provided
    } elseif ($current_state == "req_location" || $current_state == "req_location_again") {
        add_error($validation, !str_contains($input, ",")); // Must be separated
    } elseif ($current_state == "req_start_date" || $current_state == "req_end_date") {
        // Must be a valid date
        add_error($validation, !is_valid_date($input));

    } elseif ($current_state == "req_destination_number") {
        add_error($validation, !is_valid_destination_count($input));
    }

    if ($validation['has_error']) {
        $validation['error'] = generate_error_message($current_state);
    } else {
        $validation['output'] = format_input($current_state, $input);
    }

    return $validation;
}

function is_valid_destination_count($count) : bool {
    if (!is_numeric($count)) return false;
    return $count >= 1;
}

function is_valid_date($date) : bool {
    $exploded_date = explode("-", $date);

    foreach ($exploded_date as $key => $value) {
        $value = trim($value);

        if (!is_numeric($value)) return false;

        $exploded_date[$key] = (int) $value;
    }

    if (sizeof($exploded_date) != 3) return false;

    $day = $exploded_date[0];
    $month = $exploded_date[1];
    $year = $exploded_date[2];

    if (!checkdate($month, $day, $year)) return false;

    $converted = convert_user_date_input(implode("-", $exploded_date), 'd-m-Y');

    if (!$converted) return false;

    $num_days = calculate_interval(date_create()->diff($converted));

    return $num_days > 0 && $num_days < 16;
}

function calculate_interval(DateInterval $interval) : int {
    // Inclusive of end date
    return (int) $interval->format("%r%a") + 1;
}

function convert_user_date_input(string $date, string $format) : DateTime|false {
    return date_create_from_format($format, $date);
}

function days_in_between(string $start_date, string $end_date) : int {
    $start_datetime = convert_user_date_input($start_date, 'Y-m-d');
    $end_datetime = convert_user_date_input($end_date, 'Y-m-d');

    return calculate_interval($start_datetime->diff($end_datetime));
}

function add_error(array &$validation, bool $isError) : void {
    $validation['has_error'] = $isError;
}

function format_input(string $current_state, string $input) : string|int|array {
    // If location is requested, we need to split into city & country
    if ($current_state == "req_location" || $current_state == "req_location_again") {
        $cleaned_input = [];

        $exploded_input = explode(",", $input, 2);

        foreach ($exploded_input as $value) {
            $cleaned_input[] = ucfirst(strtolower(trim($value)));
        }
        return $cleaned_input;
    }

    // Format date to YYYY-MM-DD
    if ($current_state == "req_start_date" || $current_state == "req_end_date") {
        return date_format(convert_user_date_input($input, 'd-m-Y'), "Y-m-d");
    }

    if ($current_state == "req_destination_number") return (int) $input;

    return $input;
}