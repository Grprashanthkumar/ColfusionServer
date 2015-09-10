ALTER TABLE `colfusion`.`colfusion_charts` 
DROP FOREIGN KEY `FK_lym1jififkghqs6a4q15gwq5p`;
ALTER TABLE `colfusion`.`colfusion_charts` 
DROP COLUMN `note`,
DROP COLUMN `datainfo`,
DROP COLUMN `width`,
DROP COLUMN `height`,
DROP COLUMN `depth`,
DROP COLUMN `top`,
DROP COLUMN `left`,
DROP COLUMN `vid`,
CHANGE COLUMN `name` `name` VARCHAR(40) NULL DEFAULT NULL ,
CHANGE COLUMN `type` `type` VARCHAR(40) NULL DEFAULT NULL ,
ADD COLUMN `did` VARCHAR(40) NULL AFTER `type`,
ADD COLUMN `dname` VARCHAR(40) NULL AFTER `did`,
ADD COLUMN `tname` VARCHAR(40) NULL AFTER `dname`,
ADD COLUMN `columns` VARCHAR(200) NULL AFTER `tname`,
ADD COLUMN `sid` INT NULL AFTER `columns`,
ADD INDEX `st_idx` (`sid` ASC),
DROP INDEX `FK_lym1jififkghqs6a4q15gwq5p` ;
ALTER TABLE `colfusion`.`colfusion_charts` 
ADD CONSTRAINT `st`
  FOREIGN KEY (`sid`)
  REFERENCES `colfusion`.`colfusion_story` (`sid`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;
