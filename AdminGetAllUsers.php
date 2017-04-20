<?php
if($_SERVER['REQUEST_METHOD']=='POST') {
    require_once('AdminHelper.php');
    $adminHelper = new AdminHelper();

    $jsonResponse = array("error" => false);

    if (isset($_POST['permission'])) {
        $permission = $_POST['permission'];

        $userDataArray = $adminHelper->getAllUsersData($permission);
        if ($userDataArray != null) {
            while ($row = array_shift($userDataArray)) {
                $jsonResponse["error"] = false;
                $jsonResponse["userData"]["id"] = $row["id_uzytkownika"];
                $jsonResponse["userData"]["name"] = $row["imie"];
                $jsonResponse["userData"]["surname"] = $row["nazwisko"];
                $jsonResponse["userData"]["email"] = $row["email"];
                $jsonResponse["userData"]["town"] = $row["miasto"];
                $jsonResponse["userData"]["phoneNumber"] = $row["telefon"];
                $jsonResponse["userData"]["permission"] = $row["uprawnienia"];
                $jsonResponse["userData"]["creationData"] = $row["dataStworzenia"];

                echo json_encode($jsonResponse);

            }
        }
        else {
            $jsonResponse["error"] = true;
            $jsonResponse["errorMessage"] = "AdminGetAllUsers userDataArray==null error";
            echo json_encode($jsonResponse);
        }

    }
    else {
        $jsonResponse["error"] = true;
        $jsonResponse["errorMessage"] = "AdminGetAllUsers isset permission error";
        echo json_encode($jsonResponse);
    }
}
else {
    $jsonResponse["error"] = true;
    $jsonResponse["errorMessage"] = "AdminGetAllUsers REQUEST_METHOD isn't POST";
    echo json_encode($jsonResponse);
}