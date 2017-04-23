<?php
date_default_timezone_set('Europe/Warsaw');
define('logDestination','/home/a7083098//public_html/php_error_logger.log');
    require_once('AdminHelper.php');
    $adminHelper = new AdminHelper();

        $userDataArray = $adminHelper->getAllUsersData("U");
        $arrayRes = array();
        if ($userDataArray != null) {
            while ($row = array_shift($userDataArray)) {
                array_push($arrayRes,array("id" => $row["id_uzytkownika"],"name" => $row["imie"],
                    "surname" => $row["nazwisko"], "email" => $row["email"],"town" => $row["miasto"],
                    "phoneNumber" => $row["telefon"],"permission" => $row["uprawnienia"],
                    "creationDate" => $row["dataStworzenia"], "blockStatus" => $row["zablokowany"]));
            }
            echo json_encode($arrayRes);
        }
        else {
            error_log("\n " .date("F j, Y, g:i a") . "  AdminGetAllUsers \$userDataArray != null error (false)",3,logDestination);

        }