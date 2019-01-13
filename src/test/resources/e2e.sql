set foreign_key_checks = 0;

TRUNCATE TABLE zone;

INSERT INTO `zone` (`id`,`deleted`,`name`, `transport_type`) VALUES (1002,0,'gradske_linije',0);
INSERT INTO `zone` (`id`,`deleted`,`name`, `transport_type`) VALUES (1003,0,'medjumesne_linije',0);
INSERT INTO `zone` (`id`,`deleted`,`name`, `transport_type`) VALUES (1004,0,'prigradske_linije',0);
INSERT INTO `zone` (`id`,`deleted`,`name`, `transport_type`) VALUES (1005,1,'deleted_zone',0);
