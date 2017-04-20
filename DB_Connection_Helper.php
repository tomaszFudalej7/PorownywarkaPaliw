<?php

class DB_Connection_Helper{
	
	private $connectionToDB;
	private $getResultFunction;
	
	function __construct(){
		require_once('DB_Connection.php');
		$db_Connection = new DB_Connection();
		$this->connectionToDB = $db_Connection->getConnection();
		
		require_once('GetResultFunction.php');
		$this->getResultFunction = new GetResultFunction();
	}
	
	function __destruct(){
		
	}
	
	public function getUserDataByEmailAndPassword($email,$password){		

		$sqlStatement = $this->connectionToDB->prepare("SELECT * FROM uzytkownicy WHERE email = ?");		
		$sqlStatement->bind_param("s",$email);
		
		if($sqlStatement->execute()){
			$sqlResultArray = $this->getResultFunction->get_result_overwrite($sqlStatement);
			$sqlStatement->close();
			$sqlResultArrayCopy = $sqlResultArray; //copy for return, because fun. array_shift() every step delete row from array (in result empty array)
			while($row = array_shift($sqlResultArray)){
				$passwordKey = $row["passwordKey"];
				$encryptedPassword = $row["encryptedPassword"];
			}
			
				$hashResult = $this->getEncryptedPassword($password,$passwordKey);
				if($encryptedPassword === $hashResult){
					return $sqlResultArrayCopy;
				}
				else{
					return false;
				}
		}
		else{
			$sqlStatement->close();
			return false;
		}
	}
	
	public function addNewUser($name,$surname,$email,$town,$phoneNumber,$permission,$password){
		
		$hashResult = $this->generateHushPasswordAndKey($password);
		
		$encryptedPassword = $hashResult["encryptedPassword"];
		$passwordKey = $hashResult["passwordKey"];
		
		$sqlStatement = $this->connectionToDB->prepare("INSERT INTO uzytkownicy(passwordKey,encryptedPassword,imie,nazwisko,email,miasto,telefon,uprawnienia,dataStworzenia) VALUES(?,?,?,?,?,?,?,?,NOW())");
		$sqlStatement->bind_param("ssssssss",$passwordKey,$encryptedPassword,$name,$surname,$email,$town,$phoneNumber,$permission);
		$sqlResult = $sqlStatement->execute();
		$sqlStatement->close();
		
		if($sqlResult){
			$sqlStatement = $this->connectionToDB->prepare("SELECT * FROM uzytkownicy WHERE email= ?");
			$sqlStatement->bind_param("s",$email);
			$sqlStatement->execute();
			$sqlResultArray = $this->getResultFunction->get_result_overwrite($sqlStatement);
			
			$sqlStatement->close();
			return $sqlResultArray;
		}
		else{
			$jsonRespon["error"] = true;
			$jsonRespon["errorMessage"] = "sqlResult error";
			echo json_encode($jsonRespon);
			return false;
		}
	}
	
	public function checkIfUserExistInDB($email){
		$sqlStatement =$this->connectionToDB->prepare("SELECT email from uzytkownicy WHERE email = ?");
		$sqlStatement->bind_param("s",$email);
		$sqlStatement->execute();
		$sqlStatement->store_result();
		
		if($sqlStatement->num_rows > 0){
			$sqlStatement->close();
			return true;
		}
		else{
			$sqlStatement->close();
			return false;
			}
	}
	
	
	
	public function generateHushPasswordAndKey($password){
		$passwordKey = sha1(rand());
		$passwordKey = substr($passwordKey,0,10);
		
		$encryptedPassword = base64_encode(sha1($password . $passwordKey, false) . $passwordKey);
		$hashResult = array("passwordKey" => $passwordKey, "encryptedPassword"=> $encryptedPassword);
		return $hashResult;
	}
	
	public function getEncryptedPassword($password,$passwordKey){
		$hashResult = base64_encode(sha1($password . $passwordKey, false) . $passwordKey);
		return $hashResult;
	}
}
?>