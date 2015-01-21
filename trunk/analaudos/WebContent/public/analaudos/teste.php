<?php

include 'dao.php';

$dao = new Dao();
$dao->reset();
$dao->insert("owner1", 'digraph G {w0[fontcolor="blue", label="ultrassonografia"];w1[fontcolor="#008000", label="transvaginal"];w2[fontcolor="#008000", label="bexiga"];w3[fontcolor="#008000", label="vazia."];w4[fontcolor="#008000", label="utero"];w5[fontcolor="#008000", label="visualizado"];w0->w1;w0->w2;w0->w3;w0->w4;w0->w5;}');

phpinfo();
?>
