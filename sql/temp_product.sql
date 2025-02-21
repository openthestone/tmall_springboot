/*
SQLyog Ultimate v12.09 (64 bit)
MySQL - 8.0.31 
*********************************************************************
*/
/*!40101 SET NAMES utf8 */;
use tmall_springboot;
drop table if exists `temp_product`;
create table `temp_product` (
	`id` int not null auto_increment,
	`name` varchar (150) not null,
	`dataType` varchar (30) not null,
	`sizeX` varchar (30) not null,
	`sizeY` varchar (30) not null,
	`sizeZ` varchar (30) not null,
	`strainX` varchar (30) not null,
	`strainY` varchar (30) not null,
	`NX` varchar (30) not null,
	`NY` varchar (30) not null,
	`elecX` varchar (30) not null,
	`elecY` varchar (30) not null,
	`elecZ` varchar (30) not null,
	`XY_fig` varchar (765),
	`XZ_fig` varchar (765),
	`XYZ_fig` varchar (765),
	`data_file` varchar (765),
    primary key (`id`)
) engine=innodb charset=utf8mb4;
insert into `temp_product` (`id`, `name`, `dataType`, `sizeX`, `sizeY`, `sizeZ`, `strainX`, `strainY`, `NX`, `NY`, `elecX`, `elecY`, `elecZ`, `XY_fig`, `XZ_fig`, `XYZ_fig`, `data_file`) values('57','1','1','1','1','1','1','1','1','1','1','1','1',NULL,NULL,NULL,NULL);
