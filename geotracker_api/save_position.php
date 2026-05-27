<?php

require_once "db.php";

header("Content-Type: application/json");

// Afficher erreurs PHP
ini_set('display_errors', 1);
error_reporting(E_ALL);

try {

    $db = new Database();
    $conn = $db->connect();

    $rawData = file_get_contents("php://input");

    if (!$rawData) {
        throw new Exception("Aucune donnée reçue");
    }

    $data = json_decode($rawData, true);

    if (!$data) {
        throw new Exception("JSON invalide");
    }

    if (
        !isset($data["latitude"]) ||
        !isset($data["longitude"]) ||
        !isset($data["device_id"])
    ) {
        throw new Exception("Champs manquants");
    }

    $query = "INSERT INTO positions 
              (latitude, longitude, device_id)
              VALUES
              (:latitude, :longitude, :device_id)";

    $stmt = $conn->prepare($query);

    $stmt->execute([
        ":latitude" => $data["latitude"],
        ":longitude" => $data["longitude"],
        ":device_id" => $data["device_id"]
    ]);

    echo json_encode([
        "success" => true,
        "message" => "Position enregistrée"
    ]);

} catch (Exception $e) {

    echo json_encode([
        "success" => false,
        "error" => $e->getMessage()
    ]);
}