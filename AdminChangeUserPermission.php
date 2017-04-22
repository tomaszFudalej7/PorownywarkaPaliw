<?php
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
    }
}
else{
    $jsonResponse["error"] = true;
    $jsonResponse["errorMessage"] = "method is not POST";
    echo json_encode($jsonResponse);
}