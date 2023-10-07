<?php
/*
Plugin Name: JSON to Post
Description: Erstellt einen neuen Beitrag aus JSON-Daten über eine URL.
Version: 1.0
Author: Ihr Name
*/

function create_post_from_json() {
    // Holen Sie die rohen POST-Daten
    $json_data = file_get_contents('php://input');
    $data = json_decode($json_data, true);

    // Überprüfen Sie, ob die Daten das 'content' Feld enthalten
    if (isset($data['content'])) {
        // Erstellen Sie einen neuen Beitrag
        $post_id = wp_insert_post(array(
            'post_title'  => $data['title'],
            'post_content'  => $data['content'],
            'post_status'   => 'publish',
            'post_author'   => 1, // Setzen Sie die ID des Autors
        ));

        // Überprüfen Sie, ob der Beitrag erfolgreich erstellt wurde
        if ($post_id) {
            echo "Beitrag erfolgreich erstellt!";
        } else {
            echo "Fehler beim Erstellen des Beitrags.";
        }
    } else {
        echo "Fehler: Das JSON enthält kein 'content' Feld.";
    }

    die();
}

// Fügen Sie die Aktion hinzu, um auf die URL zu hören
add_action('wp_ajax_nopriv_create_post_from_json', 'create_post_from_json');
add_action('wp_ajax_create_post_from_json', 'create_post_from_json');
?>
