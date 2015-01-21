<?php

class Dao {
	public static $ID = 'id';
	public static $OWNER = 'owner';
	public static $GRAPH = 'graph';
	
	private $db;
	function __construct() {
		try {
			$this->db = new PDO("mysql:host=localhost;dbname=analaudos", "user", "usuario2010");
			// set the PDO error mode to exception
			$this->db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

		}catch(PDOException $e){
			echo "Não foi possível conectar: ".$e->getMessage();
		}
	}

	function __destruct() {
		unset($this->db);
	}

	function reset(){
		$results = $this->db->exec("DROP TABLE results");
		$results = $this->db->exec("CREATE TABLE results(id INTEGER PRIMARY KEY AUTO_INCREMENT, owner TEXT, graph TEXT);");
	}

	function insert($owner, $graph){
		$stmt = $this->db->prepare("INSERT INTO results (owner, graph) VALUES (:owner, :graph)");
		$stmt->bindParam(":owner", $owner);
		$stmt->bindParam(":graph", $graph);
		$stmt->execute();
	}

	function query(){
    	$data = $this->db->query("SELECT * FROM results");
		return $data->fetchAll(PDO::FETCH_ASSOC);
	}
}

?>
