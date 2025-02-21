/*
SQLyog Ultimate v12.09 (64 bit)
MySQL - 8.0.31 
*********************************************************************
*/
/*!40101 SET NAMES utf8 */;

create table `user` (
	`id` int (11),
	`password` varchar (150),
	`name` varchar (150),
	`salt` varchar (765),
	`anonymousName` varchar (765)
); 
insert into `user` (`id`, `password`, `name`, `salt`, `anonymousName`) values('1','121','121',NULL,NULL);
insert into `user` (`id`, `password`, `name`, `salt`, `anonymousName`) values('2','121','1211',NULL,NULL);
insert into `user` (`id`, `password`, `name`, `salt`, `anonymousName`) values('4','111','111',NULL,NULL);
insert into `user` (`id`, `password`, `name`, `salt`, `anonymousName`) values('5','333','333',NULL,NULL);
