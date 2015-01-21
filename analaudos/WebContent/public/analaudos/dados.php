<?php
include 'dao.php';

$dao = new Dao();
$result = $dao->query();
print_r($result);

foreach($result as $row) {
	$graphData = urlencode($row[graph]);
	echo "<h1>Report ID = ".$row['id'].", OWNER = ".$row['owner']."<h1/><br/>";
	echo "<img src='https://chart.googleapis.com/chart?cht=gv&chl=$graphData'> <br/><hr>";
}
?>
