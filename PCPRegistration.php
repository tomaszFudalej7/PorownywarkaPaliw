<?php
date_default_timezone_set('Europe/Warsaw');
$logDestination = "/home/a7083098//public_html/php_error_logger.log";
	if($_SERVER['REQUEST_METHOD']=='POST'){
		
		require_once('DB_Connection_Helper.php');
		$db_Connection_Helper = new DB_Connection_Helper();
		
		$jsonResponse = array("error" => false);
		
		if(isset($_POST['name']) && isset($_POST['email']) && isset($_POST['permission']) && isset($_POST['password'])){
		$name = $_POST['name'];
		$surname = $_POST['surname'];
		$email = $_POST['email'];
		$town = $_POST['town'];
		$phoneNumber = $_POST['phoneNumber'];
		$permission = $_POST['permission'];
		$password = $_POST['password'];
		
		
			if(!($db_Connection_Helper->checkIfUserExistInDB($email))){
		
			$userData = $db_Connection_Helper->addNewUser($name,$surname,$email,$town,$phoneNumber,$permission,$password);
		
				while($row = array_shift($userData)){	
				$jsonResponse["error"] = false;
				$jsonResponse["userData"]["name"] = $row["imie"];
				$jsonResponse["userData"]["surname"] = $row["nazwisko"];
				$jsonResponse["userData"]["email"] = $row["email"];
				$jsonResponse["userData"]["town"] = $row["miasto"];
				$jsonResponse["userData"]["phoneNumber"] = $row["telefon"];
				echo json_encode($jsonResponse);
				}
			}
			else{
			$jsonResponse["error"] = true;
			$jsonResponse["errorMessage"] = "User with this email " . $email . " exist";
			echo json_encode($jsonResponse);
                error_log("\n " .date("F j, Y, g:i a") . "  PCPRegistration \$db_Connection_Helper->checkIfUserExistInDB(\$email)) error ",3,logDestination);
            }
		}
		else{
            $jsonResponse["error"] = true;
            $jsonResponse["errorMessage"] = "isset error";
            echo json_encode($jsonResponse);
            error_log("\n " .date("F j, Y, g:i a") . "  PCPRegistration isset error (false)",3,logDestination);
        }
	}
	else{
		$jsonResponse["error"] = true;
		$jsonResponse["errorMessage"] = "isset error";
		echo json_encode($jsonResponse);
        error_log("\n " .date("F j, Y, g:i a") . "  PCPRegistration \$_SERVER['REQUEST_METHOD'] == 'POST' error (false)",3,logDestination);
    }
