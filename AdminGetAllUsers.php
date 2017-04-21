<?php
    require_once('AdminHelper.php');
    $adminHelper = new AdminHelper();

        $userDataArray = $adminHelper->getAllUsersData("U");
        $arrayRes = array();
        if ($userDataArray != null) {
            while ($row = array_shift($userDataArray)) {
                array_push($arrayRes,array("id" => $row["id_uzytkownika"],"name" => $row["imie"],
                    "surname" => $row["nazwisko"], "email" => $row["email"],"town" => $row["miasto"],
                    "phoneNumber" => $row["telefon"],"permission" => $row["uprawnienia"],
                    "creationDate" => $row["dataStworzenia"]));
            }
            echo json_encode($arrayRes);
        }
        else {
            echo "AdminGetAllUsers userDataArray==null error";
        }