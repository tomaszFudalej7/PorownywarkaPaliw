<?php
date_default_timezone_set('Europe/Warsaw');
define('logDestination','/home/a7083098//public_html/php_error_logger.log');

if($_SERVER['REQUEST_METHOD'] == 'POST'){
    require_once('AdminHelper.php');
    $adminHelper = new AdminHelper();

    $jsonResponse = array("error" => false);
    if(isset($_POST['email']) && isset($_POST['permission'])){
        $email = $_POST['email'];
        $permissionToSet = $_POST['permission'];

        $result = $adminHelper->updateUserPermissionStatus($permissionToSet,$email);

        if(!$result){
            $jsonResponse["error"] = true;
            $jsonResponse["errorMessage"] = " Wrong email or permission please try again";
            echo json_encode($jsonResponse);
            error_log("\n " .date("F j, Y, g:i a") . "  AdminChangeUserPermission \$result is false  error (false)",3,logDestination);
        }
        else{
            $jsonResponse["error"] = false;
            $jsonResponse["message"] = "Permission was successfully changed ;)";
            echo json_encode($jsonResponse);
        }
    }
    else{
        $jsonResponse["error"] = true;
        $jsonResponse["errorMessage"] = "isset error";
        echo json_encode($jsonResponse);
        error_log("\n " .date("F j, Y, g:i a") . "  AdminChangeUserPermission isset(_POST['email']) && isset(_POST['permission']) error (false)",3,logDestination);
    }
}
else{
    $jsonResponse["error"] = true;
    $jsonResponse["errorMessage"] = "method is not POST";
    echo json_encode($jsonResponse);
    error_log("\n " .date("F j, Y, g:i a") . "  AdminChangeUserPermission \$_SERVER['REQUEST_METHOD'] == 'POST' error (false)",3,logDestination);
}