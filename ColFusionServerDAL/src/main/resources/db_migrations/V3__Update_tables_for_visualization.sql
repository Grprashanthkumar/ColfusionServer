CREATE TABLE `colfusion`.`colfusion_story` (
  `sid` INT NOT NULL AUTO_INCREMENT,
  `did` VARCHAR(45) NULL,
  `dname` VARCHAR(45) NULL,
  `tname` VARCHAR(45) NULL,
  `cdate` TIMESTAMP NULL,
  `vid` INT NULL,
  PRIMARY KEY (`sid`),
  INDEX `cav_idx` (`vid` ASC),
  CONSTRAINT `cav`
    FOREIGN KEY (`vid`)
    REFERENCES `colfusion`.`colfusion_canvases` (`vid`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);