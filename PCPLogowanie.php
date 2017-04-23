<?php
date_default_timezone_set('Europe/Warsaw');
$logDestination = "/home/a7083098//public_html/php_error_logger.log";
if($_SERVER['REQUEST_METHOD'] == 'POST'){
	
	require_once('DB_Connection_Helper.php');
	$db_Connection_Helper = new DB_Connection_Helper();
	
	$jsonResponse = array("error" => false);

	
	if(isset($_POST['email']) && isset($_POST['password'])){
		
		$email = $_POST['email'];
		$password = $_POST['password'];	
		
		$userData = $db_Connection_Helper->getUserDataByEmailAndPassword($email,$password);
	
		if($userData == false){
			$jsonResponse["error"] = true;
			$jsonResponse["errorMessage"] = " Wrong email or password please try again";
			echo json_encode($jsonResponse);
            error_log("\n " .date("F j, Y, g:i a") . "  PCPLogowanie \$userData == false  error ",3,logDestination);
        }else if($userData == null){
				$jsonResponse["error"] = true;
				$jsonResponse["errorMessage"] = "userData = null";
            error_log("\n " .date("F j, Y, g:i a") . "  PCPLogowanie \$userData == null  error ",3,logDestination);
            echo json_encode($jsonResponse);
				}
		else{
            $ErrorTest = "if error I am empty";
			while($row = array_shift($userData)){
				$jsonResponse["error"] = false;
				$jsonResponse["userData"]["permission"] = $row["uprawnienia"];
				$jsonResponse["userData"]["name"] = $row["imie"];
				$jsonResponse["userData"]["surname"] = $row["nazwisko"];
				$jsonResponse["userData"]["email"] = $row["email"];
                $ErrorTest = $row["email"];
                $jsonResponse["userData"]["town"] = $row["miasto"];
				$jsonResponse["userData"]["phoneNumber"] = $row["telefon"];
				$jsonResponse["userData"]["blockStatus"] = $row["zablokowany"];
				echo json_encode($jsonResponse);
			}
            error_log("\n " .date("F j, Y, g:i a") . "  Error test user : " .$ErrorTest. " had enter account" ,3,logDestination);
		}
	}
	else{
		$jsonResponse["error"] = true;
		$jsonResponse["errorMessage"] = "isset error";
		echo json_encode($jsonResponse);
        error_log("\n " .date("F j, Y, g:i a") . "  PCPLogowanie isset(\$_POST['email']) && isset(\$_POST['password']) error (false)",3,logDestination);

    }
}
else{
	$jsonResponse["error"] = true;
	$jsonResponse["errorMessage"] = "method is not POST";
	echo json_encode($jsonResponse);
    error_log("\n " .date("F j, Y, g:i a") . "  PCPLogowanie \$_SERVER['REQUEST_METHOD'] == 'POST' error (false)",3,logDestination);
}