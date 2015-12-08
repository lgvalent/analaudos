-- MySQL dump 10.13  Distrib 5.5.35, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: analaudos
-- ------------------------------------------------------
-- Server version	5.5.35-0+wheezy1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `analaudos_document_category`
--

DROP TABLE IF EXISTS `analaudos_document_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `analaudos_document_category` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `analaudos_document_category`
--

LOCK TABLES `analaudos_document_category` WRITE;
/*!40000 ALTER TABLE `analaudos_document_category` DISABLE KEYS */;
INSERT INTO `analaudos_document_category` VALUES (1,'Laudo de Ultrassonografia Transvaginal');
/*!40000 ALTER TABLE `analaudos_document_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `analaudos_document_content`
--

DROP TABLE IF EXISTS `analaudos_document_content`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `analaudos_document_content` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `content` text,
  `name` varchar(100) DEFAULT NULL,
  `documentCategory_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `category` (`documentCategory_id`),
  CONSTRAINT `category` FOREIGN KEY (`documentCategory_id`) REFERENCES `analaudos_document_category` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `analaudos_document_content`
--

LOCK TABLES `analaudos_document_content` WRITE;
/*!40000 ALTER TABLE `analaudos_document_content` DISABLE KEYS */;
INSERT INTO `analaudos_document_content` VALUES (1,'ULTRASSONOGRAFIA TRANSVAGINAL Bexiga vazia. Útero visualizado (histerectomia sub-total). O colo mede: 3,1 x 3,0 x 1,8 cm. Ovário direito: Medindo 3,1 x 2,2 x 2,3 cm nos seus maiores eixos. Volume de 3,4 cm³. Aprsentando uma imagem cistica, de aspecto simples, medindo 21 mm (funcional?). Ovário esquerdo: nao visualizado (grande interposicao gasosa). Ausência de líquido livre na escavação retro uterina. Não evidenciam-se massas ou tumores nas regiões anexiais. CONCLUSÃO Cisto em ovario direito.','Ultrassonografia de teste',1);
/*!40000 ALTER TABLE `analaudos_document_content` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `analaudos_document_graph`
--

DROP TABLE IF EXISTS `analaudos_document_graph`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `analaudos_document_graph` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `author` varchar(50) DEFAULT NULL,
  `source` text,
  `timeStamp` datetime DEFAULT NULL,
  `documentContent_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `documentContent` (`documentContent_id`),
  CONSTRAINT `documentContent` FOREIGN KEY (`documentContent_id`) REFERENCES `analaudos_document_content` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `analaudos_document_graph`
--

LOCK TABLES `analaudos_document_graph` WRITE;
/*!40000 ALTER TABLE `analaudos_document_graph` DISABLE KEYS */;
INSERT INTO `analaudos_document_graph` VALUES (1,'Teste','digraph G {w0[fontcolor=\"#008000\", label=\"teste\"];w1[fontcolor=\"blue\", label=\"teste\"];w2[fontcolor=\"blue\", label=\"testa\"];w2->w1;w2->w0;w1->w0;}','2015-01-20 00:00:00',1),(3,'Lucio','digraph G {w0[fontcolor=\"blue\", label=\"ultrassonografia\"];w2[fontcolor=\"#008000\", label=\"bexiga\"];w4[fontcolor=\"#008000\", label=\"utero\"];w17[fontcolor=\"blue\", label=\"ovario\"];w18[fontcolor=\"#008000\", label=\"direito:\"];w45[fontcolor=\"blue\", label=\"ovario\"];w46[fontcolor=\"#008000\", label=\"esquerdo:\"];w0->w2;w0->w4;w0->w17;w0->w45;w45->w46;w17->w18;}','2015-01-20 15:23:03',NULL),(4,'Teste','digraph G {w0[fontcolor=\"blue\", label=\"ultrassonografia\"];w18[fontcolor=\"#008000\", label=\"direito:\"];w19[fontcolor=\"blue\", label=\"medindo\"];w20[fontcolor=\"blue\", label=\"3,1\"];w22[fontcolor=\"blue\", label=\"2,2\"];w24[fontcolor=\"blue\", label=\"2,3\"];w25[fontcolor=\"#008000\", label=\"cm\"];w0->w18;w0->w19;w19->w20;w19->w22;w19->w24;w24->w25;w20->w25;w22->w25;}','2015-01-20 17:01:19',NULL);
/*!40000 ALTER TABLE `analaudos_document_graph` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `auditorship_crud`
--

DROP TABLE IF EXISTS `auditorship_crud`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `auditorship_crud` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `description` varchar(1000) DEFAULT NULL,
  `ocurrencyDate` datetime DEFAULT NULL,
  `terminal` varchar(255) DEFAULT NULL,
  `created` bit(1) DEFAULT NULL,
  `deleted` bit(1) DEFAULT NULL,
  `entityId` bigint(20) DEFAULT NULL,
  `updated` bit(1) DEFAULT NULL,
  `applicationUser` bigint(20) DEFAULT NULL,
  `applicationEntity` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `applicationEntity` (`applicationEntity`),
  KEY `applicationUser` (`applicationUser`),
  CONSTRAINT `applicationUser` FOREIGN KEY (`applicationUser`) REFERENCES `security_user` (`id`),
  CONSTRAINT `applicationEntity` FOREIGN KEY (`applicationEntity`) REFERENCES `security_entity` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `auditorship_crud`
--

LOCK TABLES `auditorship_crud` WRITE;
/*!40000 ALTER TABLE `auditorship_crud` DISABLE KEYS */;
INSERT INTO `auditorship_crud` VALUES (1,'Laudo de Ultrassonografia Transvaginal','2015-01-22 14:53:11','localhost','','\0',1,'\0',1,26),(2,'1:Ultrassonografia de teste','2015-01-22 14:54:42','localhost','','\0',1,'\0',1,27),(3,'author=\'\'->\'Teste\'; documentContent.id=->1; ','2015-01-22 14:55:32','localhost','\0','\0',1,'',1,25);
/*!40000 ALTER TABLE `auditorship_crud` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `auditorship_process`
--

DROP TABLE IF EXISTS `auditorship_process`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `auditorship_process` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `description` varchar(1000) DEFAULT NULL,
  `ocurrencyDate` datetime DEFAULT NULL,
  `terminal` varchar(255) DEFAULT NULL,
  `applicationProcess` bigint(20) DEFAULT NULL,
  `applicationUser` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `applicationProcess` (`applicationProcess`),
  CONSTRAINT `applicationProcess` FOREIGN KEY (`applicationProcess`) REFERENCES `security_process` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `auditorship_process`
--

LOCK TABLES `auditorship_process` WRITE;
/*!40000 ALTER TABLE `auditorship_process` DISABLE KEYS */;
INSERT INTO `auditorship_process` VALUES (1,NULL,'2015-01-21 17:14:54','localhost',9,1),(2,NULL,'2015-01-22 14:52:33','localhost',9,1);
/*!40000 ALTER TABLE `auditorship_process` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `auditorship_service`
--

DROP TABLE IF EXISTS `auditorship_service`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `auditorship_service` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `description` varchar(1000) DEFAULT NULL,
  `ocurrencyDate` datetime DEFAULT NULL,
  `terminal` varchar(255) DEFAULT NULL,
  `serviceName` varchar(50) DEFAULT NULL,
  `applicationUser` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `auditorship_service`
--

LOCK TABLES `auditorship_service` WRITE;
/*!40000 ALTER TABLE `auditorship_service` DISABLE KEYS */;
/*!40000 ALTER TABLE `auditorship_service` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `framework_document_model_entity`
--

DROP TABLE IF EXISTS `framework_document_model_entity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `framework_document_model_entity` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `date` datetime DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  `source` text,
  `applicationUser` bigint(20) DEFAULT NULL,
  `applicationEntity` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `framework_document_model_entity`
--

LOCK TABLES `framework_document_model_entity` WRITE;
/*!40000 ALTER TABLE `framework_document_model_entity` DISABLE KEYS */;
/*!40000 ALTER TABLE `framework_document_model_entity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `framework_email_account`
--

DROP TABLE IF EXISTS `framework_email_account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `framework_email_account` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `host` varchar(50) DEFAULT NULL,
  `password` varchar(50) DEFAULT NULL,
  `properties` varchar(500) DEFAULT NULL,
  `senderMail` varchar(100) DEFAULT NULL,
  `senderName` varchar(100) DEFAULT NULL,
  `useAsDefault` bit(1) DEFAULT NULL,
  `user` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `framework_email_account`
--

LOCK TABLES `framework_email_account` WRITE;
/*!40000 ALTER TABLE `framework_email_account` DISABLE KEYS */;
/*!40000 ALTER TABLE `framework_email_account` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `framework_label_address`
--

DROP TABLE IF EXISTS `framework_label_address`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `framework_label_address` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `line1` varchar(150) DEFAULT NULL,
  `line2` varchar(150) DEFAULT NULL,
  `line3` varchar(150) DEFAULT NULL,
  `line4` varchar(150) DEFAULT NULL,
  `line5` varchar(150) DEFAULT NULL,
  `ocurrencyDate` datetime DEFAULT NULL,
  `print` bit(1) DEFAULT NULL,
  `applicationUser` bigint(20) DEFAULT NULL,
  `applicationEntity` bigint(20) DEFAULT NULL,
  `addressLabelGroup` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `addressLabelGroup` (`addressLabelGroup`),
  CONSTRAINT `addressLabelGroup` FOREIGN KEY (`addressLabelGroup`) REFERENCES `framework_label_address_group` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `framework_label_address`
--

LOCK TABLES `framework_label_address` WRITE;
/*!40000 ALTER TABLE `framework_label_address` DISABLE KEYS */;
/*!40000 ALTER TABLE `framework_label_address` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `framework_label_address_group`
--

DROP TABLE IF EXISTS `framework_label_address_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `framework_label_address_group` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  `applicationUser` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `framework_label_address_group`
--

LOCK TABLES `framework_label_address_group` WRITE;
/*!40000 ALTER TABLE `framework_label_address_group` DISABLE KEYS */;
/*!40000 ALTER TABLE `framework_label_address_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `framework_label_model`
--

DROP TABLE IF EXISTS `framework_label_model`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `framework_label_model` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `envelope` bit(1) DEFAULT NULL,
  `fontName` varchar(50) DEFAULT NULL,
  `fontSize` int(11) DEFAULT NULL,
  `horizontalDistance` float DEFAULT NULL,
  `labelHeight` float DEFAULT NULL,
  `labelWidth` float DEFAULT NULL,
  `marginLeft` float DEFAULT NULL,
  `marginTop` float DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  `pageHeight` float DEFAULT NULL,
  `pageWidth` float DEFAULT NULL,
  `verticalDistance` float DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `framework_label_model`
--

LOCK TABLES `framework_label_model` WRITE;
/*!40000 ALTER TABLE `framework_label_model` DISABLE KEYS */;
/*!40000 ALTER TABLE `framework_label_model` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `framework_label_model_entity`
--

DROP TABLE IF EXISTS `framework_label_model_entity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `framework_label_model_entity` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `line1` varchar(255) DEFAULT NULL,
  `line2` varchar(255) DEFAULT NULL,
  `line3` varchar(255) DEFAULT NULL,
  `line4` varchar(255) DEFAULT NULL,
  `line5` varchar(255) DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  `applicationEntity` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `framework_label_model_entity`
--

LOCK TABLES `framework_label_model_entity` WRITE;
/*!40000 ALTER TABLE `framework_label_model_entity` DISABLE KEYS */;
/*!40000 ALTER TABLE `framework_label_model_entity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `framework_order_condiction`
--

DROP TABLE IF EXISTS `framework_order_condiction`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `framework_order_condiction` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `active` bit(1) DEFAULT NULL,
  `orderDirection` int(11) DEFAULT NULL,
  `propertyPath` varchar(200) DEFAULT NULL,
  `userReport` bigint(20) DEFAULT NULL,
  `orderIndex` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `userReport` (`userReport`),
  CONSTRAINT `userReport` FOREIGN KEY (`userReport`) REFERENCES `framework_user_report` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `framework_order_condiction`
--

LOCK TABLES `framework_order_condiction` WRITE;
/*!40000 ALTER TABLE `framework_order_condiction` DISABLE KEYS */;
/*!40000 ALTER TABLE `framework_order_condiction` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `framework_page_condiction`
--

DROP TABLE IF EXISTS `framework_page_condiction`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `framework_page_condiction` (
  `id` bigint(20) NOT NULL,
  `itemsCount` int(11) DEFAULT NULL,
  `page` int(11) DEFAULT NULL,
  `pageSize` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `framework_page_condiction`
--

LOCK TABLES `framework_page_condiction` WRITE;
/*!40000 ALTER TABLE `framework_page_condiction` DISABLE KEYS */;
/*!40000 ALTER TABLE `framework_page_condiction` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `framework_parent_condiction`
--

DROP TABLE IF EXISTS `framework_parent_condiction`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `framework_parent_condiction` (
  `id` bigint(20) NOT NULL,
  `property` varchar(50) DEFAULT NULL,
  `applicationEntity` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `framework_parent_condiction`
--

LOCK TABLES `framework_parent_condiction` WRITE;
/*!40000 ALTER TABLE `framework_parent_condiction` DISABLE KEYS */;
/*!40000 ALTER TABLE `framework_parent_condiction` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `framework_query_condiction`
--

DROP TABLE IF EXISTS `framework_query_condiction`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `framework_query_condiction` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `active` bit(1) DEFAULT NULL,
  `closePar` bit(1) DEFAULT NULL,
  `initOperator` int(11) DEFAULT NULL,
  `openPar` bit(1) DEFAULT NULL,
  `operatorId` int(11) DEFAULT NULL,
  `propertyPath` varchar(200) DEFAULT NULL,
  `value1` varchar(50) DEFAULT NULL,
  `value2` varchar(50) DEFAULT NULL,
  `userReport` bigint(20) DEFAULT NULL,
  `orderIndex` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `framework_query_condiction`
--

LOCK TABLES `framework_query_condiction` WRITE;
/*!40000 ALTER TABLE `framework_query_condiction` DISABLE KEYS */;
/*!40000 ALTER TABLE `framework_query_condiction` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `framework_result_condiction`
--

DROP TABLE IF EXISTS `framework_result_condiction`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `framework_result_condiction` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `propertyPath` varchar(200) DEFAULT NULL,
  `resultIndex` int(11) DEFAULT NULL,
  `userReport` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `framework_result_condiction`
--

LOCK TABLES `framework_result_condiction` WRITE;
/*!40000 ALTER TABLE `framework_result_condiction` DISABLE KEYS */;
/*!40000 ALTER TABLE `framework_result_condiction` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `framework_user_report`
--

DROP TABLE IF EXISTS `framework_user_report`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `framework_user_report` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `date` datetime DEFAULT NULL,
  `description` text,
  `filterCondiction` varchar(50) DEFAULT NULL,
  `hqlWhereCondiction` text,
  `name` varchar(100) DEFAULT NULL,
  `applicationUser` bigint(20) DEFAULT NULL,
  `applicationEntity` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `framework_user_report`
--

LOCK TABLES `framework_user_report` WRITE;
/*!40000 ALTER TABLE `framework_user_report` DISABLE KEYS */;
/*!40000 ALTER TABLE `framework_user_report` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `results`
--

DROP TABLE IF EXISTS `results`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `results` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `owner` text,
  `graph` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `results`
--

LOCK TABLES `results` WRITE;
/*!40000 ALTER TABLE `results` DISABLE KEYS */;
INSERT INTO `results` VALUES (1,'owner1','digraph G {w0[fontcolor=\"blue\", label=\"ultrassonografia\"];w1[fontcolor=\"#008000\", label=\"transvaginal\"];w2[fontcolor=\"#008000\", label=\"bexiga\"];w3[fontcolor=\"#008000\", label=\"vazia.\"];w4[fontcolor=\"#008000\", label=\"utero\"];w5[fontcolor=\"#008000\", label=\"visualizado\"];w0->w1;w0->w2;w0->w3;w0->w4;w0->w5;}'),(2,'Silva','digraph G {w4[fontcolor=\"blue\", label=\"utero\"];w5[fontcolor=\"#008000\", label=\"visualizado\"];w7[fontcolor=\"#008000\", label=\"sub-total).\"];w13[fontcolor=\"#008000\", label=\"3,0\"];w4->w5;w4->w7;w4->w13;}');
/*!40000 ALTER TABLE `results` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `security_entity`
--

DROP TABLE IF EXISTS `security_entity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `security_entity` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `className` varchar(200) DEFAULT NULL,
  `colorName` varchar(50) DEFAULT NULL,
  `description` text,
  `hint` varchar(255) DEFAULT NULL,
  `label` varchar(100) DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `runQueryOnOpen` bit(1) DEFAULT NULL,
  `applicationModule` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `applicationModule` (`applicationModule`),
  CONSTRAINT `applicationModule` FOREIGN KEY (`applicationModule`) REFERENCES `security_module` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `security_entity`
--

LOCK TABLES `security_entity` WRITE;
/*!40000 ALTER TABLE `security_entity` DISABLE KEYS */;
INSERT INTO `security_entity` VALUES (1,'br.com.orionsoft.monstrengo.crud.report.entities.ParentCondictionBean',NULL,NULL,NULL,'Relatório Personalizado - Entidade mestre','ParentCondictionBean','\0',1),(2,'br.com.orionsoft.monstrengo.auditorship.entities.AuditProcessRegister',NULL,NULL,NULL,'Auditoria dos Processos','AuditProcessRegister','\0',1),(3,'br.com.orionsoft.monstrengo.crud.report.entities.OrderCondictionBean',NULL,NULL,NULL,'Relatório Personalizado - Ordenação','OrderCondictionBean','\0',1),(4,'br.com.orionsoft.monstrengo.crud.labels.entities.ModelLabelEntity',NULL,'Esta entidade permite definir modelos de extração de informações dos cadastros para geração de etiquetas. É possível definir quais propriedades de uma entidade serão impressas em cada linha da etiqueta. Para isto, o usuário utiliza expressões que serão interpretadas pelo gerador de etiquetas.<br/> Exemplo: <b>#{NomeEntidade[?].propriedade.subPropriedade}</b>',NULL,'Modelo de Etiqueta de Entidade','ModelLabelEntity','',1),(5,'br.com.orionsoft.monstrengo.crud.report.entities.QueryCondictionBean',NULL,NULL,NULL,'Relatório Personalizado - Filtro avançado','QueryCondictionBean','\0',1),(6,'br.com.orionsoft.monstrengo.security.entities.RightCrud',NULL,'Os direitos aqui definidos não prevalecem sobre os direitos que o programador especificou para cada entidade. Assim, se o programador definir um entidade como não excluível ela não poderá ser excluída, mesmo que os direitos aqui definidos permitam essa operação.',NULL,'Direito de edição','RightCrud','\0',1),(7,'br.com.orionsoft.monstrengo.security.entities.ApplicationProcess',NULL,NULL,NULL,'Processo do sistema','ApplicationProcess','',1),(8,'br.com.orionsoft.monstrengo.crud.labels.entities.ModelLabel',NULL,'Define as configurações de impressão de etiquetas que podem ser usadas durante a impressão','Define as configurações de impressão de etiquetas que podem ser usadas durante a impressão','Modelos de Etiqueta','ModelLabel','',1),(9,'br.com.orionsoft.monstrengo.security.entities.ApplicationEntityProperty',NULL,NULL,NULL,'Propriedade da entidade do sistema','ApplicationEntityProperty','',1),(10,'br.com.orionsoft.monstrengo.crud.report.entities.UserReportBean',NULL,NULL,NULL,'Relatório Personalizado','UserReportBean','',1),(11,'br.com.orionsoft.monstrengo.crud.labels.entities.AddressLabel',NULL,NULL,'Define a lista de etiquetas de endereçamento que estão prontas para serem impressas. ','Etiqueta de Endereçamento','AddressLabel','\0',1),(12,'br.com.orionsoft.monstrengo.security.entities.ApplicationModule',NULL,NULL,NULL,'Módulo do sistema','ApplicationModule','',1),(13,'br.com.orionsoft.monstrengo.auditorship.entities.AuditCrudRegister',NULL,NULL,NULL,'Auditoria dos Cadastros','AuditCrudRegister','\0',1),(14,'br.com.orionsoft.monstrengo.crud.report.entities.PageCondictionBean',NULL,NULL,NULL,'Relatório Personalizado - Paginação','PageCondictionBean','\0',1),(15,'br.com.orionsoft.monstrengo.mail.entities.EmailAccount',NULL,'Permite configura cadastrar uma conta de e-mail que poderá ser usada nos serviços de envio de e-mail',NULL,'Conta de e-mail','EmailAccount','\0',1),(16,'br.com.orionsoft.monstrengo.crud.report.entities.ResultCondictionBean',NULL,NULL,NULL,'Relatório Personalizado - Seleção das propriedades','ResultCondictionBean','\0',1),(17,'br.com.orionsoft.monstrengo.crud.labels.entities.AddressLabelGroup',NULL,'Define grupos para as etiquetas de endereçamento que estão prontas para serem impressas. Durante a impressão é possível selecionar somente as etiquetas pertencentes a um determinado grupo. ','Define grupos para as etiquetas de endereçamento que estão prontas para serem impressas. ','Grupo de Etiquetas de Endereçamento','AddressLabelGroup','',1),(18,'br.com.orionsoft.monstrengo.security.entities.ApplicationEntityPropertyGroup',NULL,NULL,NULL,'Grupo da propriedade da entidade do sistema','ApplicationEntityPropertyGroup','',1),(19,'br.com.orionsoft.monstrengo.auditorship.entities.AuditServiceRegister',NULL,NULL,NULL,'Auditoria dos Serviços','AuditServiceRegister','\0',1),(20,'br.com.orionsoft.monstrengo.security.entities.ApplicationEntity',NULL,NULL,NULL,'Entidade do sistema','ApplicationEntity','',1),(21,'br.com.orionsoft.monstrengo.security.entities.SecurityGroup',NULL,NULL,NULL,'Grupo de Operadores','SecurityGroup','',1),(22,'br.com.orionsoft.monstrengo.security.entities.RightProcess',NULL,NULL,NULL,'Direito dos processos','RightProcess','\0',1),(23,'br.com.orionsoft.monstrengo.security.entities.ApplicationUser','red','Ao cadastrar um novo operador será atribuida uma senha inicial igual ao login do operador. O operador poderá alterar sua senha pelo atalho \'Alterar Senha\' na tela inicial.',NULL,'Operador do sistema','ApplicationUser','',1),(24,'br.com.orionsoft.monstrengo.crud.documents.entities.ModelDocumentEntity',NULL,'Esta entidade permite definir modelos de documentos para entidades. É possível definir quais propriedades de uma entidade serão impressas em cada linha do documento. Para isto, o usuário utiliza expressões que serão interpretadas pelo gerador de documentos.',NULL,'Modelo de Documento de Entidade','ModelDocumentEntity','',1),(25,'br.com.valentin.analaudos.entities.DocumentGraph',NULL,NULL,NULL,'Estrutura semântica','DocumentGraph','\0',2),(26,'br.com.valentin.analaudos.entities.DocumentCategory',NULL,NULL,NULL,'Categoria de documento','DocumentCategory','\0',2),(27,'br.com.valentin.analaudos.entities.DocumentContent',NULL,NULL,NULL,'Documento','DocumentContent','\0',2);
/*!40000 ALTER TABLE `security_entity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `security_entity_property`
--

DROP TABLE IF EXISTS `security_entity_property`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `security_entity_property` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `allowSubQuery` bit(1) DEFAULT NULL,
  `colorName` varchar(50) DEFAULT NULL,
  `defaultValue` varchar(50) DEFAULT NULL,
  `description` text,
  `displayFormat` varchar(50) DEFAULT NULL,
  `editMask` varchar(50) DEFAULT NULL,
  `editShowList` bit(1) DEFAULT NULL,
  `hint` varchar(255) DEFAULT NULL,
  `indexGroup` int(11) DEFAULT NULL,
  `indexProperty` int(11) DEFAULT NULL,
  `label` varchar(100) DEFAULT NULL,
  `maximum` double DEFAULT NULL,
  `minimum` double DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  `readOnly` bit(1) DEFAULT NULL,
  `required` bit(1) DEFAULT NULL,
  `valuesList` varchar(255) DEFAULT NULL,
  `visible` bit(1) DEFAULT NULL,
  `applicationEntity` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=213 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `security_entity_property`
--

LOCK TABLES `security_entity_property` WRITE;
/*!40000 ALTER TABLE `security_entity_property` DISABLE KEYS */;
INSERT INTO `security_entity_property` VALUES (1,'\0',NULL,NULL,NULL,'%,d',NULL,'\0','Id',0,0,'Id',999999,0,'id','','\0',NULL,'',1),(2,'\0',NULL,NULL,NULL,NULL,NULL,'\0','ApplicationEntity',-1,1,'ApplicationEntity',999999,0,'applicationEntity','\0','\0',NULL,'',1),(3,'\0',NULL,NULL,NULL,NULL,NULL,'\0','Relatório',0,2,'Relatório',999999,0,'userReport','\0','\0',NULL,'',1),(4,'\0',NULL,NULL,NULL,NULL,NULL,'\0','Indica a propriedade do tipo coleção, na qual a pesquisa avançada será realizada',0,3,'Propriedade',999999,0,'property','\0','\0',NULL,'',1),(5,'\0',NULL,NULL,NULL,'%,d',NULL,'\0','Id',-1,9,'Id',999999,0,'id','\0','\0',NULL,'',2),(6,'\0',NULL,NULL,NULL,NULL,NULL,'\0','AsCrudRegister',-1,6,'AsCrudRegister',999999,0,'asCrudRegister','','\0',NULL,'\0',2),(7,'',NULL,NULL,NULL,NULL,NULL,'\0','Processo do sistema que foi manipulado',-1,0,'Processo',999999,0,'applicationProcess','\0','\0',NULL,'',2),(8,'\0',NULL,'não',NULL,NULL,'sim,não','\0','CrudRegister',-1,8,'CrudRegister',999999,0,'crudRegister','','\0',NULL,'\0',2),(9,'\0',NULL,NULL,NULL,NULL,NULL,'\0','Terminal ou estação de trabalho de onde originaram as operações',-1,3,'Terminal',999999,0,'terminal','\0','\0',NULL,'',2),(10,'\0',NULL,NULL,NULL,NULL,NULL,'\0','AsProcessRegister',-1,5,'AsProcessRegister',999999,0,'asProcessRegister','','\0',NULL,'\0',2),(11,'',NULL,NULL,NULL,NULL,NULL,'\0','Operador do sistema que realizou as operações',-1,1,'Operador',999999,0,'applicationUser','\0','\0',NULL,'',2),(12,'\0',NULL,'não',NULL,NULL,'sim,não','\0','ProcessRegister',-1,7,'ProcessRegister',999999,0,'processRegister','','\0',NULL,'\0',2),(13,'\0',NULL,NULL,NULL,NULL,NULL,'\0','Descrição detalhada da operação',-1,4,'Descrição',999999,0,'description','\0','\0',NULL,'',2),(14,'\0',NULL,NULL,NULL,NULL,'dd/MM/yyyy HH:mm:ss','\0','Data e hora do servidor em que ocorreu a operação',-1,2,'Data e hora',999999,0,'ocurrencyDate','\0','\0',NULL,'',2),(15,'\0',NULL,NULL,NULL,'%,d',NULL,'\0','Id',0,0,'Id',999999,0,'id','','\0',NULL,'',3),(16,'\0',NULL,NULL,NULL,'%,d',NULL,'\0','Direção',0,3,'Direção',999999,0,'orderDirection','\0','\0',NULL,'',3),(17,'\0',NULL,NULL,NULL,NULL,NULL,'\0','Propriedade',0,2,'Propriedade',999999,0,'propertyPath','\0','\0',NULL,'',3),(18,'\0',NULL,NULL,NULL,NULL,NULL,'\0','Relatório',0,4,'Relatório',999999,0,'userReport','\0','\0',NULL,'',3),(19,'\0',NULL,'não',NULL,NULL,'sim,não','\0','Ativa',0,1,'Ativa',999999,0,'active','\0','\0',NULL,'',3),(20,'\0',NULL,NULL,NULL,'%,d',NULL,'\0','Id',0,0,'Id',999999,0,'id','','\0',NULL,'\0',4),(21,'\0',NULL,NULL,NULL,NULL,NULL,'\0','Define a expressão usada para compor a quarta linha da etiqueta',1,7,'Expressão da 4ª linha',999999,0,'line4','\0','\0',NULL,'',4),(22,'\0',NULL,NULL,NULL,NULL,NULL,'\0','Define uma descrição que auxilia a identificar para que este modelo foi criado',1,3,'Descrição',999999,0,'description','\0','\0',NULL,'',4),(23,'\0',NULL,NULL,NULL,NULL,NULL,'','Define a entidade a qual pertence os dados desta etiqueta',0,1,'Entidade',999999,0,'applicationEntity','\0','\0',NULL,'',4),(24,'\0',NULL,NULL,NULL,NULL,NULL,'\0','Define o nome descritivo do modelo de etiqueta de entidade',1,2,'Nome',999999,0,'name','\0','\0',NULL,'',4),(25,'\0',NULL,NULL,NULL,NULL,NULL,'\0','Define a expressão usada para compor a quinta linha da etiqueta',1,8,'Expressão da 5ª linha',999999,0,'line5','\0','\0',NULL,'',4),(26,'\0',NULL,NULL,NULL,NULL,NULL,'\0','Define a expressão usada para compor a primeira linha da etiqueta',1,4,'Expressão da 1ª linha',999999,0,'line1','\0','\0',NULL,'',4),(27,'\0',NULL,NULL,NULL,NULL,NULL,'\0','Define a expressão usada para compor a terceira linha da etiqueta',1,6,'Expressão da 3ª linha',999999,0,'line3','\0','\0',NULL,'',4),(28,'\0',NULL,NULL,NULL,NULL,NULL,'\0','Define a expressão usada para compor a segunda linha da etiqueta',1,5,'Expressão da 2ª linha',999999,0,'line2','\0','\0',NULL,'',4),(29,'\0',NULL,NULL,NULL,'%,d',NULL,'\0','Id',0,0,'Id',999999,0,'id','','\0',NULL,'',5),(30,'\0',NULL,NULL,NULL,NULL,NULL,'\0','Propriedade',0,4,'Propriedade',999999,0,'propertyPath','\0','\0',NULL,'',5),(31,'\0',NULL,NULL,NULL,'%,d',NULL,'\0','Operador',0,5,'Operador',999999,0,'operatorId','\0','',NULL,'',5),(32,'\0',NULL,'não',NULL,NULL,'sim,não','\0','Abre parêntese \'(\'',0,3,'Abre parêntese \'(\'',999999,0,'openPar','\0','',NULL,'',5),(33,'\0',NULL,NULL,NULL,NULL,NULL,'\0','Relatório',0,9,'Relatório',999999,0,'userReport','\0','\0',NULL,'',5),(34,'\0',NULL,'não',NULL,NULL,'sim,não','\0','Ativa',0,1,'Ativa',999999,0,'active','\0','\0',NULL,'',5),(35,'\0',NULL,NULL,NULL,'%,d',NULL,'\0','Operador inicial',0,2,'Operador inicial',999999,0,'initOperator','\0','',NULL,'',5),(36,'\0',NULL,'não',NULL,NULL,'sim,não','\0','Fecha parêntese \')\'',0,8,'Fecha parêntese \')\'',999999,0,'closePar','\0','',NULL,'',5),(37,'\0',NULL,NULL,NULL,NULL,NULL,'\0','Valor 1',0,6,'Valor 1',999999,0,'value1','\0','',NULL,'',5),(38,'\0',NULL,NULL,NULL,NULL,NULL,'\0','Valor 2',0,7,'Valor 2',999999,0,'value2','\0','\0',NULL,'',5),(39,'\0',NULL,NULL,NULL,'%,d',NULL,'\0','Id',0,0,'Id',999999,0,'id','','\0',NULL,'',6),(40,'\0',NULL,'não',NULL,NULL,'sim,não','\0','Permissão para atualizar',1,5,'Atualizar',999999,0,'updateAllowed','\0','',NULL,'',6),(41,'\0',NULL,'não',NULL,NULL,'sim,não','\0','Permissão para visualizar',1,4,'Visualizar	',999999,0,'retrieveAllowed','\0','',NULL,'',6),(42,'',NULL,NULL,NULL,NULL,NULL,'\0','Define o Grupo de segurança',0,2,'Grupo de segurança',999999,0,'securityGroup','','',NULL,'',6),(43,'\0',NULL,NULL,NULL,NULL,NULL,'','Define a Entidade do sistema que foi manipulada',0,1,'Entidade do Sistema',999999,0,'applicationEntity','\0','',NULL,'',6),(44,'\0',NULL,'não',NULL,NULL,'sim,não','\0','Permissão para criar',1,3,'Criar',999999,0,'createAllowed','\0','',NULL,'',6),(45,'\0',NULL,'não',NULL,NULL,'sim,não','\0','Permissão para pesquisar',1,7,'Pesquisar',999999,0,'queryAllowed','\0','',NULL,'',6),(46,'\0',NULL,'não',NULL,NULL,'sim,não','\0','Permissão para apagar',1,6,'Apagar',999999,0,'deleteAllowed','\0','',NULL,'',6),(47,'\0',NULL,NULL,NULL,'%,d',NULL,'\0','Id',-1,0,'Id',999999,0,'id','','\0',NULL,'',7),(48,'\0',NULL,NULL,NULL,NULL,NULL,'\0','Breve descrição sobre o processo',0,4,'Descrição',999999,0,'description','\0','\0',NULL,'',7),(49,'\0',NULL,NULL,NULL,NULL,NULL,'\0','Define o nome do processo',0,1,'Nome',999999,0,'name','','\0',NULL,'',7),(50,'\0',NULL,NULL,NULL,NULL,NULL,'\0','Define o módulo do Sistema',0,5,'Módulo',999999,0,'applicationModule','','\0',NULL,'',7),(51,'\0',NULL,NULL,NULL,NULL,NULL,'\0','Define o nome amigável do processo',0,2,'Label',999999,0,'label','\0','\0',NULL,'',7),(52,'\0',NULL,NULL,NULL,NULL,NULL,'\0','Breve descrição sobre o processo',0,3,'Hint',999999,0,'hint','\0','\0',NULL,'',7),(53,'\0',NULL,NULL,NULL,NULL,NULL,'','Define a fonte que será utilizada na impressão da etiqueta',1,2,'Fonte',999999,0,'fontName','\0','\0','Bitstream Charter,Bitstream Vera Sans,Bitstream Vera Sans Mono,Bitstream Vera Serif,Courier 10 Pitch,Dialog,Kochi Gothic,SansSerif,Serif','',8),(54,'\0',NULL,NULL,NULL,NULL,NULL,'\0','Indica a largura da etiqueta',1,4,'Largura da Etiqueta (cm)',999,0,'labelWidth','\0','\0',NULL,'',8),(55,'\0',NULL,'1',NULL,'%,d',NULL,'\0','Indica o número de colunas de etiquetas por página',2,14,'Colunas de etiquetas',99,1,'columnsLabel','','\0',NULL,'',8),(56,'\0',NULL,NULL,NULL,NULL,NULL,'\0','Indica a margem esquerda da folha de etiquetas',2,12,'Margem Esquerda (cm)',999,0,'marginLeft','\0','\0',NULL,'',8),(57,'\0',NULL,NULL,NULL,NULL,NULL,'\0','Indica a margem superior da folha de etiquetas',2,11,'Margem Superior (cm)',999,0,'marginTop','\0','\0',NULL,'',8),(58,'\0',NULL,'não','Marcando esta opção, as etiquetas serão impressas no formato de orientação da página em PAISAGEM (folha deitada).<br>Deixando desmarcado, será utilizada a orientação em RETRATO.',NULL,'sim,não','\0','Indica se o modelo de etiqueta é um envelope. A impressão é feita no formato de orientação da página em PAISAGEM.',2,8,'É um modelo para envelope?',999999,0,'envelope','\0','\0',NULL,'',8),(59,'\0',NULL,NULL,NULL,'%,d',NULL,'\0','Define o tamanho da fonte da etiqueta',1,3,'Tamanho',999999,0,'fontSize','\0','\0',NULL,'',8),(60,'\0',NULL,'1',NULL,NULL,NULL,'\0','Indica a altura da folha em que serão imprimidas as etiquetas',2,10,'Altura da Folha (cm)',9999,1,'pageHeight','\0','\0',NULL,'',8),(61,'\0',NULL,NULL,NULL,'%,d',NULL,'\0','Id',0,0,'Id',999999,0,'id','','\0',NULL,'',8),(62,'\0',NULL,NULL,NULL,NULL,NULL,'\0','Indica a distância horizontal entre as etiquetas',1,6,'Distância Horizontal (cm)',999,0,'horizontalDistance','\0','\0',NULL,'',8),(63,'\0',NULL,NULL,NULL,'%,d',NULL,'\0','Indica o número de linhas de etiquetas por página',2,13,'Linhas de etiquetas',999,1,'linesLabel','','\0',NULL,'',8),(64,'\0',NULL,NULL,NULL,NULL,NULL,'\0','Indica a distância vertical entre as etiquetas',1,7,'Distância Vertical (cm)',999,0,'verticalDistance','\0','\0',NULL,'',8),(65,'\0',NULL,'1',NULL,NULL,NULL,'\0','Indica a largura da folha em que serão imprimidas as etiquetas',2,9,'Largura da Folha (cm)',999,1,'pageWidth','\0','\0',NULL,'',8),(66,'\0',NULL,NULL,NULL,NULL,NULL,'\0','Indica a altura da etiqueta',1,5,'Altura da Etiqueta (cm)',999,0,'labelHeight','\0','\0',NULL,'',8),(67,'\0',NULL,NULL,NULL,NULL,NULL,'\0','Define o nome do modelo da etiqueta',0,1,'Nome',999999,0,'name','\0','\0',NULL,'',8),(68,'\0',NULL,NULL,NULL,NULL,NULL,'','Define a cor da propriedade',-1,19,'Cor',999999,0,'colorName','\0','\0','aqua,black,blue,fuchsia,gray,green,lime,maroon,navy,olive,orange,purple,red,silver,teal,white,yellow','',9),(69,'\0',NULL,'false',NULL,NULL,'sim,não','\0','Define se a listagem será mostrada',-1,16,'Mostrar listagem',999999,0,'editShowList','\0','\0',NULL,'',9),(70,'\0',NULL,'true',NULL,NULL,'sim,não','\0','Define se a propriedade é visível',-1,9,'Visível',999999,0,'visible','\0','\0',NULL,'',9),(71,'\0',NULL,NULL,NULL,'%,.2f',NULL,'\0','Define o valor mínimo para a propriedade',-1,10,'Mínimo',999999,0,'minimum','\0','\0',NULL,'',9),(72,'\0',NULL,NULL,NULL,NULL,NULL,'\0','Breve descrição sobre a propriedade',-1,5,'Hint',999999,0,'hint','\0','\0',NULL,'',9),(73,'\0',NULL,NULL,NULL,NULL,NULL,'\0','Define o nome amigável da propriedade da entidade',-1,4,'Label',999999,0,'label','\0','\0',NULL,'',9),(74,'\0',NULL,NULL,NULL,NULL,NULL,'\0','Define o formato de exibição',-1,15,'Formato de Exibição',999999,0,'displayFormat','\0','\0',NULL,'',9),(75,'\0',NULL,NULL,NULL,'%,d',NULL,'\0','Id',-1,0,'Id',999999,0,'id','','\0',NULL,'',9),(76,'\0',NULL,'false',NULL,NULL,'sim,não','\0','Define se a propriedade é somente para leitura',-1,8,'Somente leitura',999999,0,'readOnly','\0','\0',NULL,'',9),(77,'\0',NULL,'false',NULL,NULL,'sim,não','\0','Define se permitirá sub-pesquisa na propriedade',-1,17,'Permitir sub-pesquisa',999999,0,'allowSubQuery','\0','\0',NULL,'',9),(78,'\0',NULL,NULL,NULL,'%,.2f',NULL,'\0','Define o valor máximo para a propriedade',-1,11,'Máximo',999999,0,'maximum','\0','\0',NULL,'',9),(79,'\0',NULL,NULL,NULL,NULL,NULL,'\0','Define a entidade do sistema',-1,18,'Entidade',999999,0,'applicationEntity','\0','\0',NULL,'',9),(80,'\0',NULL,NULL,NULL,NULL,NULL,'\0','Breve descrição sobre a propriedade',-1,6,'Descrição',999999,0,'description','\0','\0',NULL,'',9),(81,'\0',NULL,NULL,NULL,NULL,NULL,'\0','Define o nome da propriedade da entidade',-1,3,'Nome',999999,0,'name','\0','\0',NULL,'',9),(82,'\0',NULL,NULL,NULL,NULL,NULL,'\0','Define a máscara que a propriedade deverá seguir',-1,14,'Máscara',999999,0,'editMask','\0','\0',NULL,'',9),(83,'\0',NULL,NULL,NULL,'%,d',NULL,'\0','Índice que identifica a qual grupo a propriedade pertecence',-1,2,'Índice do grupo',999999,0,'indexGroup','\0','\0',NULL,'',9),(84,'\0',NULL,NULL,NULL,NULL,NULL,'\0','Define uma lista de valores separados por vírgula',-1,13,'Lista de valores',999999,0,'valuesList','\0','\0',NULL,'',9),(85,'\0',NULL,NULL,NULL,'%,d',NULL,'\0','Índice da propriedade',-1,1,'Índice',999999,0,'indexProperty','','\0',NULL,'',9),(86,'\0',NULL,'false',NULL,NULL,'sim,não','\0','Define se a propriedade é requerida',-1,7,'Requerido',999999,0,'required','\0','\0',NULL,'',9),(87,'\0',NULL,NULL,NULL,NULL,NULL,'\0','Define o valor padrão da propriedade',-1,12,'Valor padrão',999999,0,'defaultValue','\0','\0',NULL,'',9),(88,'\0',NULL,NULL,NULL,NULL,NULL,'','Indica o dono deste relatório personalizado',0,5,'Operador',999999,0,'applicationUser','\0','\0',NULL,'',10),(89,'\0',NULL,NULL,NULL,NULL,NULL,'\0','Condições de Pesquisa',1,6,'Condições de Pesquisa',999999,0,'queryCondictions','\0','\0',NULL,'',10),(90,'\0',NULL,NULL,NULL,NULL,NULL,'\0','O conteúdo deste filtro simples será aplicado a todos os campos primitivos da entidade e ainda, nas propriedades onde os metadados indice allowSubQuery=true.',4,9,'Filtro simples',999999,0,'filterCondiction','\0','\0',NULL,'',10),(91,'\0',NULL,NULL,NULL,NULL,'dd/MM/yyyy','\0','Data de criação',0,3,'Data de criação',999999,0,'date','\0','\0',NULL,'',10),(92,'\0',NULL,NULL,'Para referenciar a entidade padrão dentro da expressão utilize o identificador \'entity.\'',NULL,NULL,'\0','Neste filtro avançado podem ser utilizadas expressões HQL.',4,12,'Filtro avançado',999999,0,'hqlWhereCondiction','\0','\0',NULL,'',10),(93,'\0',NULL,NULL,NULL,'%,d',NULL,'\0','Id',-1,0,'Id',999999,0,'id','','\0',NULL,'',10),(94,'\0',NULL,NULL,NULL,NULL,NULL,'\0','Paginação',4,10,'Paginação',999999,0,'pageCondiction','','\0',NULL,'',10),(95,'\0',NULL,NULL,NULL,NULL,NULL,'','Entidade do Sistema',0,4,'Entidade do Sistema',999999,0,'applicationEntity','\0','\0',NULL,'',10),(96,'\0',NULL,NULL,NULL,NULL,NULL,'\0','Descrição',0,2,'Descrição',999999,0,'description','\0','\0',NULL,'',10),(97,'\0',NULL,NULL,NULL,NULL,NULL,'\0','Entidade Superior do Relatório',4,11,'Entidade Superior do Relatório',999999,0,'parentCondiction','','\0',NULL,'',10),(98,'\0',NULL,NULL,NULL,NULL,NULL,'\0','Condições de Seleção Avançada',3,8,'Condições de Seleção Avançada',999999,0,'resultCondictions','\0','\0',NULL,'',10),(99,'\0',NULL,NULL,NULL,NULL,NULL,'\0','Relatório Personalizado',0,1,'Relatório Personalizado',999999,0,'name','\0','\0',NULL,'',10),(100,'\0',NULL,NULL,NULL,NULL,NULL,'\0','Condições de Ordenação',2,7,'Condições de Ordenação',999999,0,'orderCondictions','\0','\0',NULL,'',10),(101,'\0',NULL,NULL,NULL,'%,d',NULL,'\0','Id',-1,0,'Id',999999,0,'id','','\0',NULL,'\0',11),(102,'\0',NULL,NULL,'Define um grupo para a etiqueta. Este grupo facilitará a seleção de etiquetas para a impressão.',NULL,NULL,'','Permite agrupar as etiquetas para facilitar a seleção e manutenção das mesmas',0,3,'Grupo',999999,0,'addressLabelGroup','\0','\0',NULL,'',11),(103,'\0',NULL,NULL,NULL,NULL,NULL,'','Define o usuário que criou esta etiqueta',0,1,'Operador',999999,0,'applicationUser','\0','\0',NULL,'',11),(104,'\0',NULL,NULL,NULL,NULL,NULL,'\0','Define o conteúdo da quarta linha da etiqueta',1,8,'4ª linha',999999,0,'line4','\0','\0',NULL,'',11),(105,'\0',NULL,NULL,NULL,NULL,NULL,'','Define a entidade a qual pertence os dados desta etiqueta',0,2,'Entidade',999999,0,'applicationEntity','\0','\0',NULL,'',11),(106,'\0',NULL,'now()',NULL,NULL,'dd/MM/yyyy HH:mm:ss','\0','Data e hora de criação da etiqueta',0,10,'Data e Hora',999999,0,'ocurrencyDate','\0','\0',NULL,'',11),(107,'\0',NULL,NULL,NULL,NULL,NULL,'\0','Define o conteúdo da quinta linha da etiqueta',1,9,'5ª linha',999999,0,'line5','\0','\0',NULL,'',11),(108,'\0',NULL,NULL,NULL,NULL,NULL,'\0','Define o conteúdo da primeira linha da etiqueta',1,5,'1ª linha',999999,0,'line1','\0','\0',NULL,'',11),(109,'\0',NULL,'sim',NULL,NULL,'sim,não','\0','Indica quais etiquetas deverão ser impressas',1,4,'Imprimir',999999,0,'print','\0','\0',NULL,'',11),(110,'\0',NULL,NULL,NULL,NULL,NULL,'\0','Define o conteúdo da terceira linha da etiqueta',1,7,'3ª linha',999999,0,'line3','\0','\0',NULL,'',11),(111,'\0',NULL,NULL,NULL,NULL,NULL,'\0','Define o conteúdo da segunda linha da etiqueta',1,6,'2ª linha',999999,0,'line2','\0','\0',NULL,'',11),(112,'\0',NULL,NULL,NULL,NULL,NULL,'\0','Define os processos que pertencem ao módulo',-1,3,'Processos',999999,0,'processes','\0','\0',NULL,'',12),(113,'\0',NULL,NULL,NULL,'%,d',NULL,'\0','Id',-1,0,'Id',999999,0,'id','\0','\0',NULL,'',12),(114,'\0',NULL,NULL,NULL,NULL,NULL,'\0','Define o nome do módulo',-1,1,'Nome',999999,0,'name','\0','\0',NULL,'',12),(115,'\0',NULL,NULL,NULL,NULL,NULL,'\0','Define as entidades que pertencem ao módulo',-1,2,'Entidades',999999,0,'entities','\0','\0',NULL,'',12),(116,'\0',NULL,'não',NULL,NULL,'sim,não','\0','CrudRegister',-1,10,'CrudRegister',999999,0,'crudRegister','','\0',NULL,'\0',13),(117,'',NULL,NULL,NULL,NULL,NULL,'\0','Operador do sistema que realizou as operações',-1,5,'Operador',999999,0,'applicationUser','\0','\0',NULL,'',13),(118,'\0',NULL,'não',NULL,NULL,'sim,não','\0','ProcessRegister',-1,9,'ProcessRegister',999999,0,'processRegister','','\0',NULL,'\0',13),(119,'\0',NULL,NULL,NULL,'%,d',NULL,'\0','Indentifica a entidade dentro do conjunto ao qual ela pertence.',-1,4,'Entity Id',999999,0,'entityId','\0','\0',NULL,'',13),(120,'\0',NULL,'não','Indica que a entidade foi apagada pelo operador',NULL,'sim,não','\0','Apagada',-1,2,'X',999999,0,'deleted','\0','\0',NULL,'',13),(121,'\0',NULL,NULL,NULL,NULL,NULL,'\0','AsCrudRegister',-1,11,'AsCrudRegister',999999,0,'asCrudRegister','','\0',NULL,'\0',13),(122,'\0',NULL,NULL,NULL,'%,d',NULL,'\0','Id',-1,13,'Id',999999,0,'id','\0','\0',NULL,'',13),(123,'\0',NULL,NULL,NULL,NULL,NULL,'\0','AsProcessRegister',-1,12,'AsProcessRegister',999999,0,'asProcessRegister','','\0',NULL,'\0',13),(124,'\0',NULL,NULL,NULL,NULL,NULL,'\0','Terminal ou estação de trabalho de onde originaram as operações',-1,7,'Terminal',999999,0,'terminal','\0','\0',NULL,'',13),(125,'\0',NULL,'não','Indica que a entidade foi alterada pelo operador',NULL,'sim,não','\0','Alterada',-1,1,'A',999999,0,'updated','\0','\0',NULL,'',13),(126,'\0',NULL,'não','Indica que a entidade foi criada pelo operador',NULL,'sim,não','\0','Criada',-1,0,'C',999999,0,'created','\0','\0',NULL,'',13),(127,'',NULL,NULL,NULL,NULL,NULL,'\0','Entidade do sistema que foi manipulada',-1,3,'Application Entity',999999,0,'applicationEntity','\0','\0',NULL,'',13),(128,'\0',NULL,NULL,NULL,NULL,NULL,'\0','Descrição detalhada da operação',-1,8,'Descrição',999999,0,'description','\0','\0',NULL,'',13),(129,'\0',NULL,NULL,NULL,NULL,'dd/MM/yyyy HH:mm:ss','\0','Data e hora do servidor em que ocorreu a operação',-1,6,'Data e hora',999999,0,'ocurrencyDate','\0','\0',NULL,'',13),(130,'\0',NULL,NULL,NULL,'%,d',NULL,'\0','Id',0,0,'Id',999999,0,'id','','\0',NULL,'',14),(131,'\0',NULL,NULL,NULL,'%,d',NULL,'\0','ItemsCount',-1,3,'ItemsCount',999999,0,'itemsCount','\0','\0',NULL,'',14),(132,'\0',NULL,NULL,NULL,'%,d',NULL,'\0','Página ativa',0,1,'Página ativa',9999,0,'page','\0','\0',NULL,'',14),(133,'\0',NULL,NULL,NULL,NULL,NULL,'\0','Relatório',0,4,'Relatório',999999,0,'userReport','\0','\0',NULL,'',14),(134,'\0',NULL,NULL,NULL,'%,d',NULL,'\0','Itens por página',0,2,'Itens por página',9999,0,'pageSize','\0','\0',NULL,'',14),(135,'\0',NULL,NULL,NULL,'%,d',NULL,'\0',NULL,0,0,'Id',999999,0,'id','','\0',NULL,'',15),(136,'\0',NULL,NULL,'Define o nome do remetente que aparecerá nos e-mails enviados por esta conta',NULL,NULL,'\0','Define o nome do remetente para esta conta',0,4,'Nome do remetente',999999,0,'senderName','\0','\0',NULL,'',15),(137,'\0',NULL,NULL,'mail.servername.com.br:portNumber',NULL,NULL,'\0','Define o endereço do servidor SMTP que será utilizado para enviar os e-mails',0,1,'Host',999999,0,'host','\0','\0',NULL,'',15),(138,'\0',NULL,'não','Marcando esta propriedade, você definirá esta conta como a padrão para envio de e-mail. Outra conta padrão marcada será automaticamente desmarcada. A conta padrão é utilizada pelo serviço de envio de e-Mail quando nenhuma conta é especificada.',NULL,'sim,não','\0','Define esta conta de e-mail como a conta padrão',1,7,'Usar esta conta como padrão',999999,0,'useAsDefault','\0','\0',NULL,'',15),(139,'\0',NULL,NULL,'Define o e-mail do remetente que aparecerá nos e-mails enviados por esta conta',NULL,NULL,'\0','Define o e-mail do remetente para esta conta',0,5,'e-mail do remetente',999999,0,'senderMail','\0','\0',NULL,'',15),(140,'\0',NULL,NULL,'Ex.: mail.smtp.starttls.enable=true;mail.smtp.auth=true;',NULL,NULL,'\0','Permite definir configurações adicionais exigidas por alguns serviços SMTP',1,6,'Configurações adicionais',999999,0,'properties','\0','\0',NULL,'',15),(141,'\0',NULL,NULL,'Preencha este campo apenas se a conexão com o servidor exigir autenticação.',NULL,NULL,'\0','Define o usuário que será utilizado para a autenticação no servidor',0,2,'Nome do usuário',999999,0,'user','\0','\0',NULL,'',15),(142,'\0',NULL,NULL,'Preencha este campo apenas se a conexão com o servidor exigir autenticação.',NULL,NULL,'\0','Define a senha que será utilizado para autenticação no servidor',0,3,'Senha do usuário',999999,0,'password','\0','\0',NULL,'',15),(143,'\0',NULL,NULL,NULL,'%,d',NULL,'\0','Id',0,0,'Id',999999,0,'id','','\0',NULL,'',16),(144,'\0',NULL,NULL,NULL,NULL,NULL,'\0','Propriedade',0,2,'Propriedade',999999,0,'propertyPath','\0','\0',NULL,'',16),(145,'\0',NULL,NULL,NULL,'%,d',NULL,'\0','Entidade',0,1,'Entidade',999999,0,'resultIndex','\0','\0',NULL,'',16),(146,'\0',NULL,NULL,NULL,NULL,NULL,'\0','Relatório',0,3,'Relatório',999999,0,'userReport','\0','\0',NULL,'',16),(147,'\0',NULL,NULL,NULL,'%,d',NULL,'\0','Id',0,0,'Id',999999,0,'id','','\0',NULL,'\0',17),(148,'\0',NULL,NULL,NULL,NULL,NULL,'','Define o usuário que criou este grupo',0,1,'Operador',999999,0,'applicationUser','\0','\0',NULL,'',17),(149,'\0',NULL,NULL,'Define o nome de identificação do grupo. Exemplo:<li>Financeiro</li><li>Manuais</li><li>Minhas favoritas</li>',NULL,NULL,'\0','Define o nome de identificação do grupo',0,2,'Nome do grupo',999999,0,'name','\0','\0',NULL,'',17),(150,'\0',NULL,NULL,NULL,'%,d',NULL,'\0','Id',-1,0,'Id',999999,0,'id','','\0',NULL,'',18),(151,'\0',NULL,NULL,NULL,NULL,NULL,'\0','ColorName',-1,5,'ColorName',999999,0,'colorName','\0','\0',NULL,'',18),(152,'\0',NULL,NULL,NULL,NULL,NULL,'\0','Description',-1,4,'Description',999999,0,'description','\0','\0',NULL,'',18),(153,'\0',NULL,NULL,NULL,NULL,NULL,'\0','Define a entidade do sistema',-1,3,'Entidade',999999,0,'applicationEntity','','\0',NULL,'',18),(154,'\0',NULL,NULL,NULL,NULL,NULL,'\0','Define o nome amigável do grupo',-1,2,'Nome',999999,0,'name','\0','\0',NULL,'',18),(155,'\0',NULL,NULL,NULL,'%,d',NULL,'\0','Índice que identifica a qual grupo pertence',-1,1,'Índice do grupo',999999,0,'indexGroup','\0','\0',NULL,'',18),(156,'\0',NULL,NULL,NULL,NULL,NULL,'\0','Label',-1,6,'Label',999999,0,'label','\0','\0',NULL,'',18),(157,'\0',NULL,NULL,NULL,NULL,NULL,'\0','Hint',-1,7,'Hint',999999,0,'hint','\0','\0',NULL,'',18),(158,'\0',NULL,NULL,NULL,'%,d',NULL,'\0','Id',-1,9,'Id',999999,0,'id','\0','\0',NULL,'',19),(159,'\0',NULL,NULL,NULL,NULL,NULL,'\0','AsCrudRegister',-1,5,'AsCrudRegister',999999,0,'asCrudRegister','','\0',NULL,'',19),(160,'\0',NULL,'não',NULL,NULL,'sim,não','\0','CrudRegister',-1,8,'CrudRegister',999999,0,'crudRegister','','\0',NULL,'',19),(161,'\0',NULL,NULL,NULL,NULL,NULL,'\0','Terminal ou estação de trabalho de onde originaram as operações',-1,3,'Terminal',999999,0,'terminal','\0','\0',NULL,'',19),(162,'\0',NULL,NULL,NULL,NULL,NULL,'\0','AsProcessRegister',-1,7,'AsProcessRegister',999999,0,'asProcessRegister','','\0',NULL,'',19),(163,'',NULL,NULL,NULL,NULL,NULL,'\0','Operador do sistema que realizou as operações',-1,1,'Operador',999999,0,'applicationUser','\0','\0',NULL,'',19),(164,'\0',NULL,'não',NULL,NULL,'sim,não','\0','ProcessRegister',-1,6,'ProcessRegister',999999,0,'processRegister','','\0',NULL,'',19),(165,'\0',NULL,NULL,NULL,NULL,NULL,'\0','Descrição detalhada da operação',-1,4,'Descrição',999999,0,'description','\0','\0',NULL,'',19),(166,'\0',NULL,NULL,NULL,NULL,'dd/MM/yyyy HH:mm:ss','\0','Data e hora do servidor em que ocorreu a operação',-1,2,'Data e hora',999999,0,'ocurrencyDate','\0','\0',NULL,'',19),(167,'\0',NULL,NULL,NULL,NULL,NULL,'\0','Nome do serviço que foi executado',-1,0,'Serviço',999999,0,'serviceName','\0','\0',NULL,'',19),(168,'\0',NULL,NULL,NULL,'%,d',NULL,'\0','Id',0,0,'Id',999999,0,'id','','\0',NULL,'',20),(169,'\0',NULL,NULL,NULL,NULL,NULL,'','Define a cor da entidade',-1,10,'Cor',999999,0,'colorName','\0','\0','aqua,black,blue,fuchsia,gray,green,lime,maroon,navy,olive,orange,purple,red,silver,teal,white,yellow','',20),(170,'\0',NULL,NULL,NULL,NULL,NULL,'\0','Breve descrição sobre a entidade',0,6,'Descrição',999999,0,'description','\0','\0',NULL,'',20),(171,'\0',NULL,NULL,NULL,NULL,NULL,'\0','Define o nome técnico da entidade',0,3,'Nome técnico',999999,0,'name','','\0',NULL,'',20),(172,'\0',NULL,'não',NULL,NULL,'sim,não','\0','Realiza a execução da pesquisa ao abrir a tela',0,7,'Executa a pesquisa ao abrir',999999,0,'runQueryOnOpen','\0','\0',NULL,'',20),(173,'\0',NULL,NULL,NULL,NULL,NULL,'\0','Define o módulo do Sistema',0,5,'Módulo',999999,0,'applicationModule','','\0',NULL,'',20),(174,'\0',NULL,NULL,NULL,NULL,NULL,'\0','Define o nome amigável da entidade',0,1,'Nome',999999,0,'label','\0','\0',NULL,'',20),(175,'\0',NULL,NULL,NULL,NULL,NULL,'\0','Uma breve descrição sobre a entidade',0,2,'Hint',999999,0,'hint','\0','\0',NULL,'',20),(176,'\0',NULL,NULL,NULL,NULL,NULL,'\0','Define o nome completo da classe que é manipulada pela entidade',0,4,'Nome completo da classe da entidade',999999,0,'className','','\0',NULL,'',20),(177,'\0',NULL,NULL,NULL,NULL,NULL,'\0','Define a propriedade da entidade do sistema',0,8,'Propriedade da entidade',999999,0,'applicationEntityProperty','','\0',NULL,'',20),(178,'\0',NULL,NULL,NULL,NULL,NULL,'\0','Define o grupo da propriedade da entidade do sistema',1,9,'Grupo da propriedade da entidade',999999,0,'applicationEntityPropertyGroup','\0','\0',NULL,'',20),(179,'\0',NULL,NULL,NULL,NULL,NULL,'\0','Define os direitos para Criação, Visualização, Atualização e Exclusão',-1,3,'Direitos CRUD',999999,0,'rightsCrud','\0','',NULL,'',21),(180,'\0',NULL,NULL,NULL,'%,d',NULL,'\0','Id',-1,0,'Id',999999,0,'id','','\0',NULL,'',21),(181,'\0',NULL,NULL,NULL,NULL,NULL,'\0','Define os operadores do grupo',-1,2,'Operadores',999999,0,'users','\0','\0',NULL,'',21),(182,'\0',NULL,NULL,NULL,NULL,NULL,'\0','Define os direitos para execução de processos',-1,4,'Direitos de Processos',999999,0,'rightsProcess','\0','',NULL,'',21),(183,'\0',NULL,NULL,NULL,NULL,NULL,'\0','Define o nome do grupo de operadores',-1,1,'Nome',999999,0,'name','\0','',NULL,'',21),(184,'\0',NULL,NULL,NULL,'%,d',NULL,'\0','Id',-1,0,'Id',999999,0,'id','','\0',NULL,'',22),(185,'\0',NULL,NULL,NULL,NULL,NULL,'','Define o processo do sistema que foi manipulado',-1,2,'Processo',999999,0,'applicationProcess','\0','',NULL,'',22),(186,'',NULL,NULL,NULL,NULL,NULL,'\0','Define o grupo de segurança',-1,3,'Grupo de segurança',999999,0,'securityGroup','','',NULL,'',22),(187,'\0',NULL,'não',NULL,NULL,'sim,não','\0','Permissão para executar',-1,1,'Executar',999999,0,'executeAllowed','\0','',NULL,'',22),(188,'\0','fuchsia',NULL,NULL,'%,d',NULL,'\0',NULL,0,0,'id',999999,0,'id','','\0',NULL,'',23),(189,'\0','#0F0','não','Operadores inativos não poderão acessar o Sistema',NULL,'sim,não','\0','Define se operador atual está inativo',0,3,'Inativo',999999,0,'inactive','\0','\0',NULL,'',23),(190,'\0','green',NULL,NULL,NULL,NULL,'\0','Indica os grupos de segurança que aos quais operador pertence',1,5,'Grupos de segurança',999999,0,'securityGroups','\0','',NULL,'',23),(191,'\0','#F00',NULL,'Define o nome do operador que será exibido em algumas impressões e outras áreas do sistema',NULL,NULL,'\0','Define o nome completo do operador',0,1,'Nome Completo',999999,0,'name','\0','',NULL,'',23),(192,'\0','#F00',NULL,'O login é um nome curto usado para junto com a senha para realizar a autenticação no sistema',NULL,NULL,'\0','Define o login do operador',0,2,'Login',999999,0,'login','\0','',NULL,'',23),(193,'\0','#00F',NULL,NULL,NULL,NULL,'\0','Define a senha do operador',0,4,'Senha',999999,0,'password','\0','',NULL,'',23),(194,'\0',NULL,NULL,NULL,'%,d',NULL,'\0','Id',-1,0,'Id',999999,0,'id','','\0',NULL,'\0',24),(195,'\0',NULL,NULL,NULL,NULL,'HTML','\0','Define o código fonte HTML que é utilizado para imprimir o documento',1,6,'Código fonte HTML',999999,0,'source','\0','\0',NULL,'',24),(196,'\0',NULL,NULL,NULL,NULL,NULL,'','Define o dono deste modelo de documento',0,2,'Operador',999999,0,'applicationUser','\0','\0',NULL,'',24),(197,'\0',NULL,NULL,NULL,NULL,NULL,'\0','Define uma descrição que auxilia a identificar para que este modelo foi criado',0,4,'Descrição',999999,0,'description','\0','\0',NULL,'',24),(198,'\0',NULL,NULL,'Esta propriedade é <b>obrigatória</b> se no documento é referenciada um entidade dinamicamente com uma expressão do tipo #{NomeEntidade[?]}.<br>Se no documento somente expressões #{NomeEntidade[123]} são utilizadas, então esta propriedade poderá ficar vazia.',NULL,NULL,'','Define a entidade a qual pertence os dados deste modelo de documento',0,1,'Entidade',999999,0,'applicationEntity','\0','\0',NULL,'',24),(199,'\0',NULL,NULL,NULL,NULL,NULL,'\0','Define o nome descritivo do modelo de documento de entidade',0,3,'Nome',999999,0,'name','\0','',NULL,'',24),(200,'\0',NULL,'nowDate()',NULL,NULL,'dd/MM/yyyy','\0','Data de criação',0,5,'Data de criação',999999,0,'date','\0','\0',NULL,'',24),(201,'\0',NULL,NULL,NULL,'%,d',NULL,'\0',NULL,0,0,'Id',999999,0,'id','','',NULL,'',25),(202,'\0',NULL,NULL,NULL,NULL,NULL,'\0',NULL,0,4,'SourceEncoded',999999,0,'sourceEncoded','','\0',NULL,'\0',25),(203,'\0',NULL,NULL,NULL,NULL,NULL,'\0','Indica o autor que compôs este grafo',0,1,'Autor',999999,0,'author','\0','\0',NULL,'',25),(204,'\0',NULL,NULL,NULL,NULL,'dd/MM/yyyy','\0','Indica a data e hora da criação desse grafo',0,5,'Data e hora',999999,0,'timeStamp','\0','\0',NULL,'',25),(205,'\0',NULL,NULL,NULL,NULL,NULL,'\0',NULL,0,3,'Grafo .dot',999999,0,'source','\0','\0',NULL,'',25),(206,'\0',NULL,NULL,NULL,NULL,NULL,'\0',NULL,0,2,'Documento original',999999,0,'documentContent','\0','\0',NULL,'',25),(207,'\0',NULL,NULL,NULL,'%,d',NULL,'\0',NULL,0,0,'Id',999999,0,'id','','\0',NULL,'',26),(208,'\0',NULL,NULL,NULL,NULL,NULL,'\0',NULL,0,1,'Nome',999999,0,'name','\0','\0',NULL,'',26),(209,'\0',NULL,NULL,NULL,'%,d',NULL,'\0',NULL,0,0,'Id',999999,0,'id','','\0',NULL,'',27),(210,'\0',NULL,NULL,NULL,NULL,NULL,'\0','Conteúdo do documento',0,2,'Conteúdo',999999,0,'content','\0','\0',NULL,'',27),(211,'\0',NULL,NULL,NULL,NULL,NULL,'\0','Nome do documento (título)',0,1,'Nome',999999,0,'name','\0','\0',NULL,'',27),(212,'\0',NULL,NULL,NULL,NULL,NULL,'\0',NULL,0,3,'Categoria',999999,0,'documentCategory','\0','\0',NULL,'',27);
/*!40000 ALTER TABLE `security_entity_property` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `security_entity_property_group`
--

DROP TABLE IF EXISTS `security_entity_property_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `security_entity_property_group` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `colorName` varchar(50) DEFAULT NULL,
  `description` text,
  `hint` varchar(255) DEFAULT NULL,
  `indexGroup` int(11) DEFAULT NULL,
  `label` varchar(100) DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `applicationEntity` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=49 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `security_entity_property_group`
--

LOCK TABLES `security_entity_property_group` WRITE;
/*!40000 ALTER TABLE `security_entity_property_group` DISABLE KEYS */;
INSERT INTO `security_entity_property_group` VALUES (1,NULL,NULL,NULL,0,'Dados Gerais','Dados Gerais',1),(2,NULL,NULL,NULL,1,NULL,NULL,1),(3,NULL,NULL,NULL,0,NULL,NULL,2),(4,NULL,NULL,NULL,0,'Dados Gerais','Dados Gerais',3),(5,NULL,NULL,NULL,0,'Dados da Entidade','Dados da Entidade',4),(6,NULL,NULL,NULL,1,'Dados da composição da etiqueta','Dados da composição da etiqueta',4),(7,NULL,NULL,NULL,0,'Dados Gerais','Dados Gerais',5),(8,NULL,NULL,NULL,0,'Dados Gerais','Dados Gerais',6),(9,NULL,NULL,NULL,1,'Direitos','Direitos',6),(10,NULL,NULL,NULL,0,'Dados gerais','Dados gerais',7),(11,NULL,NULL,NULL,1,'Entidades compatíveis','Entidades compatíveis',7),(12,NULL,NULL,NULL,2,NULL,NULL,7),(13,NULL,NULL,NULL,0,'Identificação do modelo de etiqueta','Identificação do modelo de etiqueta',8),(14,NULL,NULL,NULL,1,'Propriedades da etiqueta','Propriedades da etiqueta',8),(15,NULL,NULL,NULL,2,'Propriedades da página','Propriedades da página',8),(16,NULL,NULL,NULL,0,NULL,NULL,9),(17,NULL,NULL,NULL,0,'Dados Gerais','Dados Gerais',10),(18,NULL,NULL,NULL,1,'Condições de Filtro Avançado','Condições de Filtro Avançado',10),(19,NULL,NULL,NULL,2,'Condições de Ordenação Avançada','Condições de Ordenação Avançada',10),(20,NULL,NULL,NULL,3,'Condições de Seleção Avançada','Condições de Seleção Avançada',10),(21,NULL,NULL,NULL,4,'Outras Informações','Outras Informações',10),(22,NULL,NULL,NULL,5,NULL,NULL,10),(23,NULL,NULL,NULL,0,'Dados do Operador e Entidade','Dados do Operador e Entidade',11),(24,NULL,NULL,NULL,1,'Dados da Etiqueta','Dados da Etiqueta',11),(25,NULL,NULL,NULL,2,NULL,NULL,11),(26,NULL,NULL,NULL,0,NULL,NULL,12),(27,NULL,NULL,NULL,0,NULL,NULL,13),(28,NULL,NULL,NULL,0,'Dados Gerais','Dados Gerais',14),(29,NULL,NULL,NULL,1,NULL,NULL,14),(30,NULL,NULL,NULL,0,'Dados da conta','Geral',15),(31,NULL,NULL,NULL,1,'Configuração','Configuração',15),(32,NULL,NULL,NULL,0,'Dados Gerais','Dados Gerais',16),(33,NULL,NULL,NULL,0,'Dados gerais','Dados gerais',17),(34,NULL,NULL,NULL,0,NULL,NULL,18),(35,NULL,NULL,NULL,0,NULL,NULL,19),(36,NULL,NULL,NULL,0,'Dados gerais','Dados gerais',20),(37,NULL,NULL,NULL,1,'Grupos de propriedades','Grupos de propriedades',20),(38,NULL,NULL,NULL,2,NULL,NULL,20),(39,NULL,NULL,NULL,0,NULL,NULL,21),(40,NULL,NULL,NULL,0,NULL,NULL,22),(41,'green',NULL,NULL,0,'Informações de Autenticação','g0',23),(42,'navy',NULL,NULL,1,'Informações de grupos de direitos do operador','g1',23),(43,NULL,NULL,NULL,0,'Dados do modelo','Dados do modelo',24),(44,NULL,NULL,NULL,1,'Código fonte do modelo de documento','Código fonte do modelo de documento',24),(45,NULL,NULL,NULL,2,NULL,NULL,24),(46,NULL,NULL,NULL,0,'Geral','Geral',25),(47,NULL,NULL,NULL,0,'Geral','Geral',26),(48,NULL,NULL,NULL,0,'Geral','Geral',27);
/*!40000 ALTER TABLE `security_entity_property_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `security_group`
--

DROP TABLE IF EXISTS `security_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `security_group` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `security_group`
--

LOCK TABLES `security_group` WRITE;
/*!40000 ALTER TABLE `security_group` DISABLE KEYS */;
INSERT INTO `security_group` VALUES (1,'admin'),(2,'user');
/*!40000 ALTER TABLE `security_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `security_module`
--

DROP TABLE IF EXISTS `security_module`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `security_module` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `security_module`
--

LOCK TABLES `security_module` WRITE;
/*!40000 ALTER TABLE `security_module` DISABLE KEYS */;
INSERT INTO `security_module` VALUES (1,'br.com.orionsoft.monstrengo'),(2,'br.com.valentin.analaudos');
/*!40000 ALTER TABLE `security_module` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `security_process`
--

DROP TABLE IF EXISTS `security_process`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `security_process` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `description` text,
  `hint` varchar(255) DEFAULT NULL,
  `label` varchar(100) DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  `applicationModule` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `security_process`
--

LOCK TABLES `security_process` WRITE;
/*!40000 ALTER TABLE `security_process` DISABLE KEYS */;
INSERT INTO `security_process` VALUES (1,NULL,NULL,NULL,'UpdateProcess',1),(2,NULL,NULL,NULL,'CreateLabelFromEntityProcess',1),(3,'DESCRIPTION','HINT','LABEL','MyProcess',1),(4,'Escreva a instrução SQL, tanto de pesquisa quando de manipulação de dados.','Permite e a execução de instruções SQL nativas diretamente no banco de dados da aplicação','Pesquisa SQL nativa','SqlQueryProcess',1),(5,NULL,NULL,NULL,'ChangePasswordProcess',1),(6,NULL,NULL,NULL,'CreateSecurityStructureProcess',1),(7,NULL,NULL,NULL,'CreateProcess',1),(8,NULL,NULL,NULL,'RetrieveProcess',1),(9,NULL,NULL,NULL,'AuthenticateProcess',1),(10,NULL,NULL,NULL,'CompileDocumentProcess',1),(11,NULL,NULL,NULL,'QueryProcess',1),(12,'Permite redefinir a senha de um operador sem conhecer a sua atual senha. Útil para os administradores redefinirem a senha esquecida de um operador','Permite redefinir a senha de um operador sem conhecer a sua atual senha','Redefinir a senha de um operador','OverwritePasswordProcess',1),(13,NULL,NULL,NULL,'DeleteProcess',1);
/*!40000 ALTER TABLE `security_process` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `security_right_crud`
--

DROP TABLE IF EXISTS `security_right_crud`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `security_right_crud` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createAllowed` bit(1) DEFAULT NULL,
  `deleteAllowed` bit(1) DEFAULT NULL,
  `queryAllowed` bit(1) DEFAULT NULL,
  `retrieveAllowed` bit(1) DEFAULT NULL,
  `updateAllowed` bit(1) DEFAULT NULL,
  `applicationEntity` bigint(20) DEFAULT NULL,
  `securityGroup` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `securityGroup` (`securityGroup`),
  CONSTRAINT `securityGroup` FOREIGN KEY (`securityGroup`) REFERENCES `security_group` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=55 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `security_right_crud`
--

LOCK TABLES `security_right_crud` WRITE;
/*!40000 ALTER TABLE `security_right_crud` DISABLE KEYS */;
INSERT INTO `security_right_crud` VALUES (1,'\0','','','','',1,1),(2,'\0','\0','','','\0',2,1),(3,'\0','','','','',3,1),(4,'','','','','',4,1),(5,'\0','','','','',5,1),(6,'','','','','',6,1),(7,'\0','\0','','','\0',7,1),(8,'','','','','',8,1),(9,'\0','\0','','','',9,1),(10,'\0','','','','',10,1),(11,'','','','','',11,1),(12,'\0','\0','','','\0',12,1),(13,'\0','\0','','','\0',13,1),(14,'\0','','','','',14,1),(15,'','','','','',15,1),(16,'\0','','','','',16,1),(17,'','','','','',17,1),(18,'\0','\0','','','\0',18,1),(19,'\0','\0','','','\0',19,1),(20,'\0','\0','','','',20,1),(21,'','','','','',21,1),(22,'','','','','',22,1),(23,'','','','','',23,1),(24,'','','','','',24,1),(25,'\0','\0','\0','\0','\0',1,2),(26,'\0','\0','\0','\0','\0',2,2),(27,'\0','\0','\0','\0','\0',3,2),(28,'\0','\0','\0','\0','\0',4,2),(29,'\0','\0','\0','\0','\0',5,2),(30,'\0','\0','\0','\0','\0',6,2),(31,'\0','\0','\0','\0','\0',7,2),(32,'\0','\0','\0','\0','\0',8,2),(33,'\0','\0','\0','\0','\0',9,2),(34,'\0','\0','\0','\0','\0',10,2),(35,'\0','\0','\0','\0','\0',11,2),(36,'\0','\0','\0','\0','\0',12,2),(37,'\0','\0','\0','\0','\0',13,2),(38,'\0','\0','\0','\0','\0',14,2),(39,'\0','\0','\0','\0','\0',15,2),(40,'\0','\0','\0','\0','\0',16,2),(41,'\0','\0','\0','\0','\0',17,2),(42,'\0','\0','\0','\0','\0',18,2),(43,'\0','\0','\0','\0','\0',19,2),(44,'\0','\0','\0','\0','\0',20,2),(45,'\0','\0','\0','\0','\0',21,2),(46,'\0','\0','\0','\0','\0',22,2),(47,'\0','\0','\0','\0','\0',23,2),(48,'\0','\0','\0','\0','\0',24,2),(49,'','','','','',25,1),(50,'','','','','',26,1),(51,'','','','','',27,1),(52,'\0','\0','\0','\0','\0',25,2),(53,'\0','\0','\0','\0','\0',26,2),(54,'\0','\0','\0','\0','\0',27,2);
/*!40000 ALTER TABLE `security_right_crud` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `security_right_process`
--

DROP TABLE IF EXISTS `security_right_process`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `security_right_process` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `executeAllowed` bit(1) DEFAULT NULL,
  `securityGroup` bigint(20) DEFAULT NULL,
  `applicationProcess` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `security_right_process`
--

LOCK TABLES `security_right_process` WRITE;
/*!40000 ALTER TABLE `security_right_process` DISABLE KEYS */;
INSERT INTO `security_right_process` VALUES (1,'',1,1),(2,'',1,2),(3,'',1,3),(4,'',1,4),(5,'',1,5),(6,'',1,6),(7,'',1,7),(8,'',1,8),(9,'',1,9),(10,'',1,10),(11,'',1,11),(12,'',1,12),(13,'',1,13),(14,'\0',2,1),(15,'\0',2,2),(16,'\0',2,3),(17,'\0',2,4),(18,'\0',2,5),(19,'\0',2,6),(20,'\0',2,7),(21,'\0',2,8),(22,'\0',2,9),(23,'\0',2,10),(24,'\0',2,11),(25,'\0',2,12),(26,'\0',2,13);
/*!40000 ALTER TABLE `security_right_process` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `security_user`
--

DROP TABLE IF EXISTS `security_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `security_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `inactive` bit(1) DEFAULT NULL,
  `login` varchar(20) DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  `password` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `login` (`login`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `security_user`
--

LOCK TABLES `security_user` WRITE;
/*!40000 ALTER TABLE `security_user` DISABLE KEYS */;
INSERT INTO `security_user` VALUES (1,'\0','admin','admin','21232f297a57a5a743894a0e4a801fc3'),(2,'\0','user','user','ee11cbb19052e40b07aac0ca060c23ee');
/*!40000 ALTER TABLE `security_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `security_user_group`
--

DROP TABLE IF EXISTS `security_user_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `security_user_group` (
  `securityGroup` bigint(20) NOT NULL,
  `applicationUser` bigint(20) NOT NULL,
  PRIMARY KEY (`applicationUser`,`securityGroup`),
  KEY `applicationGroup` (`securityGroup`),
  CONSTRAINT `applicationGroup` FOREIGN KEY (`securityGroup`) REFERENCES `security_group` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `security_user_group`
--

LOCK TABLES `security_user_group` WRITE;
/*!40000 ALTER TABLE `security_user_group` DISABLE KEYS */;
INSERT INTO `security_user_group` VALUES (1,1),(2,2);
/*!40000 ALTER TABLE `security_user_group` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-01-22 15:06:38
