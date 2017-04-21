<?php 
class AdminHelper {
	private $connectionToDB;
	private $getResultFunction;
	
	function __construct(){
		require_once ('DB_Connection.php');
		$db_Connection = new DB_Connection();
		$this->connectionToDB = $db_Connection->getConnection();

		
		require_once ('GetResultFunction.php');
		$this->getResultFunction = new GetResultFunction();
	}
	
	function __destruct(){
	}
	
	public function getAllUsersData(){
		$sqlStatement = $this->connectionToDB->prepare("SELECT * FROM uzytkownicy");
		
		if($sqlStatement->execute()){
			$sqlArrayResult = $this->getResultFunction->get_result_overwrite($sqlStatement);
			$sqlStatement->close();
			return $sqlArrayResult;
		}
        else{
            $sqlStatement->close();
            echo "getAllUsersData sqlStatement->execute() error";
            return false;
        }
    }
	
	
	public function updateUserPermissionStatus($permission,$email){
		$jsonResponse = array("error" => false);
		$sqlStatement = $this->connectionToDB->prepare("UPDATE uzytkownicy SET uprawnienia = ? WHERE email = ?");
		$sqlStatement->bind_param("ss", $permission,$email);
		
		if($sqlStatement->execute()){
            $sqlStatement->close();
			return true;
		}
		else{
			$sqlStatement->close();
            $jsonResponse["error"] = true;
            $jsonResponse["errorMessage"]= "updateUserPermissionStatus execute() error";
            echo json_encode($jsonResponse);
            return false;
		}
    }
}