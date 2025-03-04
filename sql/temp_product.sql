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
    `method` varchar (150) not null,
	`name` varchar (150) not null,
    `systemType` varchar (150) not null,
    `dataType` int not null,
	`sizeX` float not null,
	`sizeY` float not null,
	`sizeZ` float not null,
	`strainX` float not null,
	`strainY` float not null,
	`nx` float not null,
	`ny` float not null,
	`elecX` float not null,
	`elecY` float not null,
	`elecZ` float not null,
	`xy_Fig` varchar (765),
	`xz_Fig` varchar (765),
	`xyz_Fig` varchar (765),
    `data_Fig` varchar (765),
	`data_File` varchar (765) not null,
    primary key (`id`)
) engine=innodb charset=utf8mb4;
insert into `temp_product` (`id`, `method`, `name`, `systemType`, `dataType`, `sizeX`, `sizeY`, `sizeZ`, `strainX`, `strainY`, `nx`, `ny`, `elecX`, `elecY`, `elecZ`, `xy_Fig`, `xz_Fig`, `xyz_Fig`, `data_Fig`, `data_File`) values('1','1','1','1','1','1','1','1','1','1','1','1','1','1','1',NULL,NULL,NULL,NULL,'./temp_data_fig/temp_XYZ_file/8.txt');
