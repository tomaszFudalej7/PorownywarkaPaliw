<?php
if($_SERVER['REQUEST_METHOD'] == 'POST'){
    require_once('AdminHelper.php');
    $adminHelper = new AdminHelper();

    $jsonResponse = array("error" => false);
    if(isset($_POST['email']) && isset($_POST['blockStatus'])){
        $email = $_POST['email'];
        $blockStatus = $_POST['blockStatus'];

        $result = $adminHelper->blockUser($email,$blockStatus);

        if(!$result){
            $jsonResponse["error"] = true;
            $jsonResponse["errorMessage"] = " Wrong email blockUser() failed";
            echo json_encode($jsonResponse);
        }
        else{
            $jsonResponse["error"] = false;
            $jsonResponse["message"] = "User block status was successfully changed ;)";
            echo json_encode($jsonResponse);
        }
    }
    else{
        $jsonResponse["error"] = true;
        $jsonResponse["errorMessage"] = "isset error";
        echo json_encode($jsonResponse);
    }
}
else{
    $jsonResponse["error"] = true;
    $jsonResponse["errorMessage"] = "method is not POST";
    echo json_encode($jsonResponse);
}