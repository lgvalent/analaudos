<?php
	include 'dao.php';

	echo "BEGIN\n";
	
	$dao = new Dao();
	$dao->insert($_POST[Dao::$OWNER], $_POST[Dao::$GRAPH]);

	echo $_POST[Dao::$OWNER]." <br/>\n".$_POST[Dao::$GRAPH];

	echo "END\n";
?>
