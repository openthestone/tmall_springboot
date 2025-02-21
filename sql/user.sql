/*
SQLyog Ultimate v12.09 (64 bit)
MySQL - 8.0.31 
*********************************************************************
*/
/*!40101 SET NAMES utf8 */;
use tmall_springboot;
drop table if exists `user`;
create table `user` (
	`id` int not null auto_increment,
	`name` varchar (150) not null,
	`password` varchar (150) not null,
	`salt` varchar (765),
	`anonymousName` varchar (765),
    primary key (`id`),
	unique(`name`)
) engine=innodb charset=utf8mb4; 
insert into `user` (`id`, `name`, `password`, `salt`, `anonymousName`) values(1,'121','121',NULL,NULL);
insert into `user` (`id`, `name`, `password`, `salt`, `anonymousName`) values(3,'111','111',NULL,NULL);
insert into `user` (`id`, `name`, `password`, `salt`, `anonymousName`) values(4,'333','333',NULL,NULL);
insert into `user` (`id`, `name`, `password`, `salt`, `anonymousName`) values(5,'openthestone','2oo21oo1',NULL,NULL);
