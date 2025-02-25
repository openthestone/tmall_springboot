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
	`nx` varchar (30) not null,
	`ny` varchar (30) not null,
	`elecX` varchar (30) not null,
	`elecY` varchar (30) not null,
	`elecZ` varchar (30) not null,
	`xy_Fig` varchar (765),
	`xz_Fig` varchar (765),
	`xyz_Fig` varchar (765),
	`data_File` varchar (765) not null,
    primary key (`id`)
) engine=innodb charset=utf8mb4;
insert into `temp_product` (`id`, `name`, `dataType`, `sizeX`, `sizeY`, `sizeZ`, `strainX`, `strainY`, `nx`, `ny`, `elecX`, `elecY`, `elecZ`, `xy_Fig`, `xz_Fig`, `xyz_Fig`, `data_File`) values('57','1','1','1','1','1','1','1','1','1','1','1','1',NULL,NULL,NULL,'./temp_data_fig/temp_XYZ_file/1.txt');
