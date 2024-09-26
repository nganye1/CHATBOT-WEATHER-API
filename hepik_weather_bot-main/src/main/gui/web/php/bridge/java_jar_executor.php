<?php

function send_java_request(string $request_target, array $input) : array {
    $shell_input = json_encode(["request" => $request_target, "input" => $input]);
    $shell_input = escapeshellarg($shell_input);

    $jar_directory = __DIR__.DIRECTORY_SEPARATOR.'HEPIK_weather_bot.jar';

    // Make PHP-Java request via shell
    $shell_output = shell_exec("java -jar $jar_directory $shell_input");

    $did_fail = !$shell_output || str_starts_with($shell_output, "E: ");

    if ($did_fail) {
        return ["executed" => false, "error_messages" => str_replace("E: ", "", $shell_output)];
    }

    $parsed_output = json_decode($shell_output, true);

    $error_param = $parsed_output['error'];

    return [
        "executed" => true,
        "success" => json_decode($parsed_output['success'], true),
        'error_type' => $error_param['type'],
        "error_messages" => $error_param['output']
    ];
}

function interpret_shell_output(array $shell_output_info) : array {
    $shell_error_output = $shell_output_info['error_messages'];

    // If Java did not execute to completion or an input error was encountered
    if (!$shell_output_info['executed'] || $shell_output_info['error_type'] == 'input') {
        return ['unrecoverable' => $shell_error_output];
    }

    return ['success' => $shell_output_info['success'], 'errors' => $shell_error_output];
}

function send_request(bool $is_geolocation_request) : array {
    $session_key = $is_geolocation_request ? "location_info" : "geolocation_info";
    $request_target = $is_geolocation_request ? "location" : "weather";

    // Read all location info as a JSON string.
    $session_info = read_session_data($session_key, true);

    // Send request to Java
    $shell_output_info = send_java_request($request_target, $session_info);

    return interpret_shell_output($shell_output_info);
}